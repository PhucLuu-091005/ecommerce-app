-- ============================================================
-- PostgreSQL-compatible schema (camelCase column names)
-- Column names match Java field names directly, no @Column(name=...) needed
-- ============================================================

-- CREATE DATABASE ecommerce;
-- \c ecommerce

-- ======================================================
-- Table: UserInfo
-- ======================================================
CREATE TABLE IF NOT EXISTS UserInfo (
    userName        VARCHAR(100) PRIMARY KEY,
    hashedPassword  VARCHAR(255) NOT NULL,
    phoneNumber     VARCHAR(10),
    email           VARCHAR(255),
    displayName     VARCHAR(255) NOT NULL UNIQUE,
    gender          CHAR(1)      CHECK (gender IN ('M', 'F', 'O')),
    birthDate       DATE,
    address         VARCHAR(500),
    role            VARCHAR(20)  NOT NULL DEFAULT 'BUYER' CHECK (role IN ('BUYER', 'SELLER', 'ADMIN')),
    CONSTRAINT email_format CHECK (
        email IS NULL OR (
            email LIKE '%_@__%.__%'
            AND email NOT LIKE '% %'
            AND email NOT LIKE '%@%@%'
            AND email NOT LIKE '%.@%'
            AND email NOT LIKE '%@.%'
        )
    ),
    CONSTRAINT contact_method CHECK (phoneNumber IS NOT NULL OR email IS NOT NULL),
    CONSTRAINT phonenumber_format CHECK (
        phoneNumber IS NULL
        OR (LENGTH(phoneNumber) = 10 AND phoneNumber ~ '^[0-9]+$')
    )
);

-- ======================================================
-- Table: Buyer
-- ======================================================
CREATE TABLE IF NOT EXISTS Buyer (
    userName   VARCHAR(100) PRIMARY KEY,
    moneySpent BIGINT NOT NULL DEFAULT 0 CHECK (moneySpent >= 0),
    FOREIGN KEY (userName) REFERENCES UserInfo(userName) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: Seller
-- ======================================================
CREATE TABLE IF NOT EXISTS Seller (
    userName      VARCHAR(100) PRIMARY KEY,
    shopName      VARCHAR(100) NOT NULL UNIQUE,
    citizenIDCard VARCHAR(30)  NOT NULL UNIQUE CHECK (citizenIDCard ~ '^[0-9]+$'),
    sellerName    VARCHAR(50)  NOT NULL UNIQUE,
    moneyEarned   BIGINT       NOT NULL DEFAULT 0 CHECK (moneyEarned >= 0),
    FOREIGN KEY (userName) REFERENCES UserInfo(userName) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: AddressInfo  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS AddressInfo (
    id                 BIGSERIAL    PRIMARY KEY,
    userName           VARCHAR(100) NOT NULL,
    contactName        VARCHAR(255) NOT NULL,
    contactPhoneNumber VARCHAR(10)     NOT NULL CHECK (LENGTH(contactPhoneNumber) = 10 AND contactPhoneNumber ~ '^[0-9]+$'),
    city               VARCHAR(100) NOT NULL,
    district           VARCHAR(100) NOT NULL,
    commune            VARCHAR(100) NOT NULL,
    detailAddress      VARCHAR(500) NOT NULL,
    addressType        VARCHAR(50)  NOT NULL DEFAULT 'Home' CHECK (addressType IN ('Home', 'Office')),
    isAddressDefault   VARCHAR(1)      NOT NULL DEFAULT 'Y'    CHECK (isAddressDefault IN ('Y', 'N')),
    FOREIGN KEY (userName) REFERENCES UserInfo(userName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_UniqueDefaultAddress
    ON AddressInfo(userName)
    WHERE isAddressDefault = 'Y';

-- ======================================================
-- Table: DeliveryMethod
-- ======================================================
CREATE TABLE IF NOT EXISTS DeliveryMethod (
    methodName VARCHAR(100) PRIMARY KEY
);

-- ======================================================
-- Table: DeliveryProvider
-- ======================================================
CREATE TABLE IF NOT EXISTS DeliveryProvider (
    providerName VARCHAR(100) PRIMARY KEY
);

-- ======================================================
-- Table: ProvideDelivery  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS ProvideDelivery (
    id           BIGSERIAL    PRIMARY KEY,
    providerName VARCHAR(100) NOT NULL,
    methodName   VARCHAR(100) NOT NULL,
    UNIQUE (providerName, methodName),
    FOREIGN KEY (providerName) REFERENCES DeliveryProvider(providerName) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (methodName)   REFERENCES DeliveryMethod(methodName)     ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: OrderInfo
-- ======================================================
CREATE TABLE IF NOT EXISTS OrderInfo (
    orderId          BIGSERIAL    PRIMARY KEY,
    userName         VARCHAR(100) NOT NULL,
    orderDate        TIMESTAMP    NOT NULL DEFAULT NOW(),
    totalPrice       BIGINT       NOT NULL DEFAULT 0 CHECK (totalPrice >= 0),
    bankProviderName VARCHAR(10)  NOT NULL DEFAULT 'VCB' CHECK (bankProviderName IN ('VCB', 'OCB', 'MoMo', 'ZaloPay')),
    accountId        VARCHAR(30)  CHECK (accountId ~ '^[0-9]+$'),
    addressId        BIGINT       NOT NULL,
    FOREIGN KEY (userName)  REFERENCES Buyer(userName)      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (addressId) REFERENCES AddressInfo(id)      ON DELETE CASCADE
);

-- ======================================================
-- Table: SubOrderInfo  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS SubOrderInfo (
    id                   BIGSERIAL    PRIMARY KEY,
    orderId              BIGINT       NOT NULL,
    totalSkuPrice        BIGINT       NOT NULL DEFAULT 0,
    shippingStatus       VARCHAR(20)  NOT NULL DEFAULT 'Preparing'
                             CHECK (shippingStatus IN ('Preparing', 'Shipping', 'Done', 'Cancelled')),
    actualDate           TIMESTAMP    NOT NULL,
    expectedDate         TIMESTAMP    NOT NULL,
    deliveryMethodName   VARCHAR(100) NOT NULL,
    deliveryProviderName VARCHAR(100) NOT NULL,
    deliveryPrice        INT          NOT NULL DEFAULT 0,
    CONSTRAINT deliveryDate CHECK (actualDate <= expectedDate),
    FOREIGN KEY (orderId)              REFERENCES OrderInfo(orderId)              ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (deliveryMethodName)   REFERENCES DeliveryMethod(methodName)      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (deliveryProviderName) REFERENCES DeliveryProvider(providerName)  ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: Cart
-- ======================================================
CREATE TABLE IF NOT EXISTS Cart (
    cartId    BIGSERIAL    PRIMARY KEY,
    userName  VARCHAR(100) NOT NULL,
    totalCost BIGINT       NOT NULL DEFAULT 0,
    FOREIGN KEY (userName) REFERENCES UserInfo(userName) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: ProductInfo
-- ======================================================
CREATE TABLE IF NOT EXISTS ProductInfo (
    productId          BIGSERIAL    PRIMARY KEY,
    userName           VARCHAR(100) NOT NULL,
    productName        VARCHAR(100) NOT NULL,
    productBrand       VARCHAR(100),
    productCategory    VARCHAR(100) NOT NULL,
    productDescription VARCHAR(500),
    productMadeIn      VARCHAR(100) NOT NULL,
    productImageUrl      VARCHAR(200) NOT NULL,
    FOREIGN KEY (userName) REFERENCES Seller(userName) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: SKU  (surrogate PK; natural key: productId + skuName)
-- ======================================================
CREATE TABLE IF NOT EXISTS SKU (
    id            BIGSERIAL    PRIMARY KEY,
    productId     BIGINT       NOT NULL,
    skuName       VARCHAR(100) NOT NULL,
    size          INT,
    price         INT          NOT NULL,
    inStockNumber INT          NOT NULL DEFAULT 0,
    weight        INT,
    imageUrl      VARCHAR(200),
    UNIQUE (productId, skuName),
    FOREIGN KEY (productId) REFERENCES ProductInfo(productId) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: StoredSKU  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS StoredSKU (
    id       BIGSERIAL PRIMARY KEY,
    cartId   BIGINT    NOT NULL,
    skuId    BIGINT    NOT NULL,
    quantity INT       NOT NULL DEFAULT 0,
    UNIQUE (cartId, skuId),
    FOREIGN KEY (cartId) REFERENCES Cart(cartId) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skuId)  REFERENCES SKU(id)      ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: SubOrderDetail  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS SubOrderDetail (
    id         BIGSERIAL PRIMARY KEY,
    subOrderId BIGINT    NOT NULL,
    skuId      BIGINT    NOT NULL,
    quantity   INT       NOT NULL DEFAULT 0,
    UNIQUE (subOrderId, skuId),
    FOREIGN KEY (subOrderId) REFERENCES SubOrderInfo(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skuId)      REFERENCES SKU(id)          ON DELETE CASCADE ON UPDATE CASCADE
);


-- ======================================================
-- Table: Comment  (self-referential for replies)
-- ======================================================
CREATE TABLE IF NOT EXISTS Comment (
    commentId       BIGSERIAL    PRIMARY KEY,
    userName        VARCHAR(100) NOT NULL,
    skuId           BIGINT,
    ratings         INT  CHECK (ratings IS NULL OR ratings BETWEEN 1 AND 5),
    content         VARCHAR(500),
    parentCommentId BIGINT,
    CONSTRAINT chk_comment_not_empty CHECK (ratings IS NOT NULL OR content IS NOT NULL),
    FOREIGN KEY (userName)        REFERENCES Buyer(userName)      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skuId)           REFERENCES SKU(id)              ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (parentCommentId) REFERENCES Comment(commentId)
);

-- ======================================================
-- Table: CommentImage  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS CommentImage (
    id         BIGSERIAL    PRIMARY KEY,
    commentId  BIGINT       NOT NULL,
    commentUrl VARCHAR(200) NOT NULL,
    UNIQUE (commentId, commentUrl),
    FOREIGN KEY (commentId) REFERENCES Comment(commentId) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: Voucher
-- voucherId is BIGSERIAL; code auto-set by trigger
-- ======================================================
CREATE TABLE IF NOT EXISTS Voucher (
    voucherId         BIGSERIAL   PRIMARY KEY,
    code              VARCHAR(10) UNIQUE,
    startedTime       TIMESTAMP   NOT NULL,
    expiredTime       TIMESTAMP   NOT NULL,
    currentUsedNumber INT         NOT NULL DEFAULT 0,
    maxUsedNumber     INT         NOT NULL DEFAULT 1,
    minMoneyValue     INT         NOT NULL DEFAULT 0,
    CONSTRAINT chk_voucher_used CHECK (currentUsedNumber < maxUsedNumber)
);

CREATE OR REPLACE FUNCTION fn_generate_voucher_code()
RETURNS TRIGGER AS $$
BEGIN
    NEW.code := 'VCH-' || LPAD(CAST(NEW.voucherId AS VARCHAR), 6, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_voucher_code
BEFORE INSERT ON Voucher
FOR EACH ROW EXECUTE FUNCTION fn_generate_voucher_code();

-- ======================================================
-- Table: PercentageVoucher
-- ======================================================
CREATE TABLE IF NOT EXISTS PercentageVoucher (
    voucherId          BIGINT         PRIMARY KEY,
    percentageDiscount DECIMAL(5, 4) NOT NULL DEFAULT 0.0,
    maxAmountAllowed   INT            NOT NULL DEFAULT 1,
    FOREIGN KEY (voucherId) REFERENCES Voucher(voucherId) ON DELETE CASCADE
);

-- ======================================================
-- Table: FlatDiscountVoucher
-- ======================================================
CREATE TABLE IF NOT EXISTS FlatDiscountVoucher (
    voucherId      BIGINT PRIMARY KEY,
    discountAmount INT    NOT NULL DEFAULT 0,
    FOREIGN KEY (voucherId) REFERENCES Voucher(voucherId) ON DELETE CASCADE
);

-- ======================================================
-- Table: VoucherOffer  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS VoucherOffer (
    id        BIGSERIAL    PRIMARY KEY,
    voucherId BIGINT       NOT NULL,
    productId BIGINT       NOT NULL,
    userName  VARCHAR(100) NOT NULL,
    UNIQUE (voucherId, productId),
    FOREIGN KEY (voucherId) REFERENCES Voucher(voucherId)      ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES ProductInfo(productId),
    FOREIGN KEY (userName)  REFERENCES UserInfo(userName)
);

-- ======================================================
-- Table: AppliedVoucher  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS AppliedVoucher (
    id         BIGSERIAL PRIMARY KEY,
    subOrderId BIGINT    NOT NULL,
    voucherId  BIGINT    NOT NULL,
    UNIQUE (subOrderId, voucherId),
    FOREIGN KEY (subOrderId) REFERENCES SubOrderInfo(id)    ON DELETE CASCADE,
    FOREIGN KEY (voucherId)  REFERENCES Voucher(voucherId)  ON DELETE CASCADE
);

-- ======================================================
-- Table: DeliveryPartner  (surrogate PK)
-- ======================================================
CREATE TABLE IF NOT EXISTS DeliveryPartner (
    id           BIGSERIAL    PRIMARY KEY,
    userName     VARCHAR(100) NOT NULL,
    providerName VARCHAR(100) NOT NULL,
    UNIQUE (userName, providerName),
    FOREIGN KEY (userName)     REFERENCES UserInfo(userName)              ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (providerName) REFERENCES DeliveryProvider(providerName)  ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================================
-- Table: Withdrawal
-- ======================================================
CREATE TABLE IF NOT EXISTS Withdrawal (
    withdrawalId     BIGSERIAL    PRIMARY KEY,
    userName         VARCHAR(100) NOT NULL,
    withdrawalAmount INT          NOT NULL,
    withdrawalTime   TIMESTAMP    NOT NULL,
    accountId        VARCHAR(30)  UNIQUE,
    providerName     VARCHAR(100) NOT NULL DEFAULT 'VCB'
                         CHECK (providerName IN ('VCB', 'MoMo', 'OCB', 'ZaloPay')),
    remainingBalance INT          NOT NULL DEFAULT 0 CHECK (remainingBalance >= 0),
    FOREIGN KEY (userName) REFERENCES UserInfo(userName) ON DELETE CASCADE ON UPDATE CASCADE
);
