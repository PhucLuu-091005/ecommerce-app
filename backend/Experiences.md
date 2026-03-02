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