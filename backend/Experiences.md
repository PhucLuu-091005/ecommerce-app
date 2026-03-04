# Experiences from doing this project

## About JWT (JSON Web Token) Security

This project utilizes **Spring Security combined with JWT** to secure REST APIs. This is a stateless authentication mechanism, optimizing performance and allowing the system to scale easily without relying on server-side Sessions.

### 1. Core Authentication Flow
The security mechanism operates through the following steps:
1. **Login:** The client sends credentials (email/password).
2. **Generate Token:** If the credentials are valid, the server generates a JWT string (signed with a `SECRET_KEY`) and returns it to the client.
3. **Request:** For subsequent requests, the client attaches the JWT to the HTTP Header: `Authorization: Bearer <token>`.
4. **Validate:** The server's `JwtAuthenticationFilter` intercepts the request and validates the JWT. If valid, access is granted to the respective Controllers.

### 2. JWT Structure & Security

A standard JWT (JWS) consists of 3 parts: `Header.Payload.Signature`.

* **Header & Payload (Encoded, Not Encrypted):** Data (like email, roles) is only Base64Url encoded for transmission. Anyone can decode and read this content. Never store sensitive information (like passwords or critical personal data) in the Payload.
* **Signature:** This is the "heart" of the system's protection. The signature is a hash of the Header, the Payload, and a `SECRET_KEY` stored exclusively on the Server.

**Anti-Tampering Protection:**
If a user intentionally alters the Payload (e.g., changing `ROLE_CUSTOMER` to `ROLE_ADMIN`), the modified Payload will cause the recalculated signature to mismatch the original signature based on the `SECRET_KEY`. When this happens, the JJWT library throws a `SignatureException` and immediately rejects the request (returning 401 Unauthorized / 403 Forbidden).

### 3. Role-Based Access Control (RBAC) with JWT
The project embeds **Roles** directly into the JWT Payload.
* **Advantages (Low Latency):** The server can immediately authorize requests based on the information present in the Token without querying the database to check permissions, significantly reducing DB load.
* **"Stale Data" Mitigation Strategy:** Because Roles are stored in the token, if a user's permissions change in the DB, an old token will still carry the old permissions. To overcome this, Access Tokens are configured with a **short lifespan** (e.g., 15 minutes), often combined with a Refresh Token mechanism.
* **Heavier Payload in JWT:** Since the JWT contains more information (like roles), the token size is larger than a simple Session ID. However, this is a trade-off for the stateless nature and reduced DB load, and it may not be too significant because role is typically a small string.

### 4. Spring Security & Database Integration
Although JWT is stateless, the validation flow is still linked to the Database to ensure consistency (e.g., checking if a user account is locked):
1. `JwtAuthenticationFilter` extracts the Email/Username from the Token.
2. Calls `UserDetailsService` to load the latest `UserDetails` object from the Database.
3. Compares the token information with the Database information and checks the token's expiration.
4. Populates the authorization info into the `SecurityContextHolder` to complete the request authentication.

### 5. JJWT Library Notes
The project uses a modern version of the JJWT library (v0.12.x or higher) from (JJWT Repo)[https://github.com/jwtk/jjwt?tab=readme-ov-file#jwe-example]. The token validation APIs are written according to the latest standards:
* Uses `Jwts.parser().verifyWith(key).build().parseSignedClaims(token)` instead of the deprecated `parseClaimsJws(token)` method.
* Uses `.getPayload()` to extract data (Claims) safely and with clear semantics.

## Spring Boot 3.x & JWT Security Architecture (In-Depth Analysis)

This document provides a detailed explanation of the core mechanics behind a Stateless REST API authentication flow using Spring Security 6.x and JWT. It focuses on the architectural design and component interactions rather than implementation details.

---

### 1. Security Filter (`JwtAuthenticationFilter`)

This class acts as the "Security Checkpoint" at the gateway of the system. Every HTTP Request must pass through this filter before reaching the Controllers (Business Logic).



**Core Concepts & Mechanics:**
* **`OncePerRequestFilter`:** This filter extends a Spring abstract class to guarantee that the JWT inspection logic is executed **exactly once** per HTTP Request. This prevents wasting CPU resources on redundant token decryption if the request is forwarded internally within the server.
* **Inspection Process:** It extracts the `Authorization` header to look for the `Bearer <token>` string. If absent, it allows the request to proceed (leaving the block/allow decision to the `SecurityConfig`). If present, it delegates the token to the `JwtService` to verify the signature and integrity.
* **`SecurityContextHolder` (The Heart of the Session):** This is a thread-local storage mechanism tied to the current execution thread. If the token is valid, the filter creates an "identity badge" (`UsernamePasswordAuthenticationToken` containing the user's details and roles) and stores it in the `SecurityContextHolder`. When the request reaches the Controller/Service layers, the application simply accesses this context to identify the current user.
* **`WebAuthenticationDetailsSource`:** This utility attaches technical metadata (such as the Client's IP address and Session ID) to the identity badge, which is highly valuable for audit logging and fraud prevention.

---

### 2. Component Factory (`ApplicationConfig`)

Separating `ApplicationConfig` from `SecurityConfig` is an application of the **Separation of Concerns** principle. This class does not define HTTP access rules; instead, it acts as a "Factory" that constructs core data processing tools and injects them into Spring's central memory (IoC Container).

**Core Beans Constructed:**
* **`UserDetailsService` (The Record Retriever):** A bridge interface. Its sole responsibility is to take a `username` (or email), query the Database, and return a `UserDetails` object. It does not handle password verification.
* **`PasswordEncoder` (The Hasher):** Configures the `BCrypt` algorithm. This algorithm not only performs one-way hashing but also automatically generates a "Salt" (a random string mixed into the password) to defend against Rainbow Table attacks.
* **`AuthenticationProvider` (The Authenticator):** This is where the actual authentication logic resides. It combines the two tools above: it retrieves the user record from the `UserDetailsService`, gets the hasher from the `PasswordEncoder`, and matches the provided raw password against the Database hash. The most common implementation is `DaoAuthenticationProvider`.
* **`AuthenticationManager` (The Coordinator):** A high-level interface that manages a list of `AuthenticationProvider`s. When a login request occurs, it doesn't process it directly but delegates it to the appropriate Provider.

> **Note on Dependency Injection:** It is highly recommended to use Constructor Injection (via Lombok's `@RequiredArgsConstructor`) for Repositories in these configuration classes, rather than Field Injection (`@Autowired`). This ensures class integrity upon instantiation and simplifies Unit Testing.

---

### 3. HTTP Security Layer (`SecurityConfig`)

This is the "Command Center" of the HTTP system. It enforces authorization rules and assembles the security components.

**Established Mechanisms:**
* **Disable CSRF (`csrf.disable`):** Cross-Site Request Forgery (CSRF) is an attack that exploits the browser's behavior of automatically sending Session Cookies. Because our architecture uses **Stateless JWTs** attached to the HTTP Header (which must be manually handled by the Frontend), the browser will never automatically send the JWT. Therefore, the system is inherently immune to CSRF, and disabling it prevents unnecessary conflicts.
* **Stateless Session (`SessionCreationPolicy.STATELESS`):** Instructs Spring Security to absolutely never create a `JSESSIONID` in the Server's RAM. Every request is an independent entity. This forces the JWT filter to authenticate every incoming request, saving Server RAM and making horizontal scaling (Scale-out) effortless.
* **Access Control (`authorizeHttpRequests`):** Explicitly defines which APIs are public (e.g., `/login`, `/register`), which require specific roles (e.g., `.hasRole("ADMIN")`), and mandates that all other APIs require a valid authentication token.
* **Filter Registration (`addFilterBefore`):** Inserts our custom `JwtAuthenticationFilter` into the security chain, positioning it *before* Spring's default `UsernamePasswordAuthenticationFilter` to prioritize JWT inspection.
* **CORS (Cross-Origin Resource Sharing):** Configures permissions allowing external domains (like a React/Vue Frontend running on a different port) to call the API and read response headers without being blocked by the browser's Same-Origin Policy.

---

### 4. Authentication Flow Summary



To understand how these interfaces interact, here is the lifecycle of a Login Request:

1. **Client** sends an HTTP POST request containing `{email, password}` to the `/login` API.
2. **Filter** (`JwtAuthenticationFilter`) ignores it because the request does not yet contain a JWT.
3. **Controller** receives the payload and passes it to the **Service**.
4. **Service** packages the raw email/password into an unauthenticated `UsernamePasswordAuthenticationToken` object and passes it to the **`AuthenticationManager`**.
5. **`AuthenticationManager`** iterates through its Providers and delegates the task to the **`DaoAuthenticationProvider`**.
6. **`DaoAuthenticationProvider`** calls the **`UserDetailsService`** to query the Database and find the user by email.
7. Upon receiving the `UserDetails` object, the **`DaoAuthenticationProvider`** uses the **`PasswordEncoder`** to verify if the raw password matches the Database hash.
8. If incorrect, it throws an Exception. If correct, it returns a fully authenticated token back to the **Manager**, which passes it back to the **Service**.
9. The **Service** takes the valid user details and calls the `JwtService` "factory" to generate the actual JWT string.
10. The JWT is returned via the HTTP Response to the **Client** to be used for all subsequent requests.

---

## Spring Data JPA - Method Naming Convention

When working with Spring Data JPA repositories, the framework provides a powerful feature: **automatic query generation from method names**. This allows developers to define database queries without writing explicit SQL or using `@Query` annotations in most cases.

### 1. Basic Structure

The fundamental pattern is:
```
[Action][By][Property][Operator]...[(Optional) OrderBy][Sort Direction]
```

**Example breakdown:**
```java
findBySeller_UserInfo_UserName(String username)
│      │ └─ Property chain (Nested Relationship)
│      └─ Condition keyword (always required)
└─ Action (Method prefix)
```

### 2. Common Action Prefixes

| Prefix | SQL Operation | Return Type | Example |
|--------|---------------|------------|---------|
| `findBy` | SELECT | List/Optional | `findByName(String name)` |
| `findAll` | SELECT * | List | `findAllByStatus(String status)` |
| `countBy` | COUNT | Long | `countByStatus(String status)` |
| `deleteBy` | DELETE | Long (count) | `deleteByStatus(String status)` |
| `existsBy` | SELECT EXISTS | boolean | `existsByEmail(String email)` |
| `getBy` | SELECT | List/Optional | `getByEmail(String email)` |

### 3. Operators for Conditions

| Operator | SQL Equivalent | Example | Generated SQL |
|----------|----------------|---------|---------------|
| (none) | = | `findByName(String)` | WHERE name = ? |
| `And` | AND | `findByNameAndStatus(...)` | WHERE name = ? AND status = ? |
| `Or` | OR | `findByNameOrEmail(...)` | WHERE name = ? OR email = ? |
| `Is` / `Equals` | = | `findByNameIs(String)` | WHERE name = ? |
| `Between` | BETWEEN | `findByAgeBetween(int, int)` | WHERE age BETWEEN ? AND ? |
| `LessThan` | < | `findByAgeLessThan(int)` | WHERE age < ? |
| `LessThanEqual` | <= | `findByAgeLessThanEqual(int)` | WHERE age <= ? |
| `GreaterThan` | > | `findByAgeGreaterThan(int)` | WHERE age > ? |
| `GreaterThanEqual` | >= | `findByAgeGreaterThanEqual(int)` | WHERE age >= ? |
| `After` | > (for Date) | `findByCreatedDateAfter(LocalDate)` | WHERE created_date > ? |
| `Before` | < (for Date) | `findByCreatedDateBefore(LocalDate)` | WHERE created_date < ? |
| `IsNull` | IS NULL | `findByNameIsNull()` | WHERE name IS NULL |
| `IsNotNull` | IS NOT NULL | `findByNameIsNotNull()` | WHERE name IS NOT NULL |
| `Like` | LIKE | `findByNameLike(String)` | WHERE name LIKE ? |
| `NotLike` | NOT LIKE | `findByNameNotLike(String)` | WHERE name NOT LIKE ? |
| `StartingWith` | LIKE prefix% | `findByNameStartingWith(String)` | WHERE name LIKE ? (with %) |
| `EndingWith` | LIKE %suffix | `findByNameEndingWith(String)` | WHERE name LIKE ? (with %) |
| `Containing` | LIKE %text% | `findByNameContaining(String)` | WHERE name LIKE %?% |
| `NotContaining` | NOT LIKE %text% | `findByNameNotContaining(String)` | WHERE name NOT LIKE %?% |
| `In` | IN | `findByStatusIn(List<String>)` | WHERE status IN (?) |
| `NotIn` | NOT IN | `findByStatusNotIn(List<String>)` | WHERE status NOT IN (?) |
| `Not` | != | `findByStatusNot(String)` | WHERE status != ? |
| `True` | = true | `findByIsActivatedTrue()` | WHERE is_activated = true |
| `False` | = false | `findByIsActivatedFalse()` | WHERE is_activated = false |
| `IgnoreCase` | UPPER/LOWER | `findByNameIgnoreCase(String)` | WHERE UPPER(name) = UPPER(?) |

### 4. Traversing Relationships (Nested Properties)

Use underscore `_` to navigate through entity relationships:

```java
// ProductInfo -> seller (ManyToOne) -> userInfo (OneToOne) -> userName
findBySeller_UserInfo_UserName(String username)
```

**Generated SQL Logic:**
```sql
SELECT p FROM ProductInfo p 
JOIN Seller s ON p.seller_id = s.id
JOIN UserInfo u ON s.user_info_id = u.id
WHERE u.user_name = ?
```

**Key Points:**
- Each segment before `_` represents a property name
- Property name must match exactly (case-sensitive)
- Relationship must exist in the entity model (will throw error if not)
- Multiple levels of nesting are supported: `findByA_B_C_D_Property(...)`

### 5. Sorting and Ordering

Append `OrderBy` followed by property name and direction:

```java
// Ascending (default)
List<ProductInfo> findBySellerOrderByProductNameAsc(Seller seller);

// Descending
List<ProductInfo> findBySellerOrderByProductNameDesc(Seller seller);

// Multiple fields
List<ProductInfo> findBySellerOrderByProductNameAscPriceDesc(Seller seller);
```

### 6. Limiting Results

```java
// Top N results
List<ProductInfo> findTop10BySellerOrderByProductNameAsc(Seller seller);

// First N results (same as Top)
List<ProductInfo> findFirst5BySellerOrderByProductNameAsc(Seller seller);
```

### 7. Pagination and Sorting

Use `Pageable` parameter from `org.springframework.data.domain`:

```java
Page<ProductInfo> findBySeller(Seller seller, Pageable pageable);

// Usage in Service:
PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("productName").ascending());
Page<ProductInfo> products = repository.findBySeller(seller, pageRequest);
```

### 8. Distinct Results

```java
List<ProductInfo> findDistinctProductInfoBySellerOrderByProductName(Seller seller);
```

### 9. Complex Queries with @Query Annotation

When method names become too long or query logic is complex, use `@Query`:

```java
// JPQL Query
@Query("SELECT p FROM ProductInfo p WHERE p.seller.userInfo.userName = :username ORDER BY p.productName ASC")
List<ProductInfo> findProductsBySeller(@Param("username") String username);

// Native SQL
@Query(value = "SELECT * FROM ProductInfo WHERE user_name = ?1 ORDER BY product_name ASC", nativeQuery = true)
List<ProductInfo> findProductsBySellerNative(String username);
```

### 10. Practical Examples for E-Commerce Repository

```java
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
  
  // Find all products of a seller
  List<ProductInfo> findBySeller_UserInfo_UserName(String sellerUsername);
  
  // Count products of a seller
  Long countBySeller_UserInfo_UserName(String sellerUsername);
  
  // Check if product exists
  boolean existsByProductNameAndSeller_UserInfo_UserName(String productName, String sellerUsername);
  
  // Find products by category and seller
  List<ProductInfo> findByProductCategoryAndSeller_UserInfo_UserName(String category, String sellerUsername);
  
  // Search products by keyword (partial match, case-insensitive)
  List<ProductInfo> findByProductNameContainingIgnoreCaseAndSeller_UserInfo_UserName(String keyword, String sellerUsername);
  
  // Get products with pagination
  Page<ProductInfo> findBySeller_UserInfo_UserName(String sellerUsername, Pageable pageable);
  
  // Get top 10 newest products
  List<ProductInfo> findTop10BySellerOrderByProductIdDesc(Seller seller);
  
  // Get products sorted by name
  List<ProductInfo> findBySellerOrderByProductNameAsc(Seller seller);
}
```

### 11. Best Practices

✅ **DO:**
- Keep method names readable (2-3 conditions max)
- Use `@Query` for complex logic
- Leverage `Pageable` for large datasets
- Use `OrderBy` for consistent sorting
- Test generated queries in integration tests

❌ **DON'T:**
- Create method names with 5+ conditions (use `@Query` instead)
- Mix uppercase and lowercase incorrectly (case-sensitive!)
- Traverse relationships without verifying they exist
- Store sensitive data in `@Query` string literals (use `@Param`)
- Forget that method names generate ONE query (no N+1 optimization magic)

### 12. Key Takeaways

1. **Spring Data JPA automatically implements methods** based on naming convention - no manual implementation needed
2. **Use `_` for nested properties** when traversing relationships
3. **Method names are case-sensitive** - they must match entity property names exactly
4. **Method names are one query** - complex logic should use `@Query` annotation
5. **Combine with `Pageable`** for production-grade pagination and sorting
