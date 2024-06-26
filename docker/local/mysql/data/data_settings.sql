CREATE TABLE IF NOT EXISTS CATEGORY (
    CATEGORY_ID BIGINT NOT NULL AUTO_INCREMENT,
    PARENT_CATEGORY_ID BIGINT,
    NAME VARCHAR(255),
    LEVEL INT,
    PATH_NAME VARCHAR(255),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (CATEGORY_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS ORDERS (
    ORDER_ID BIGINT NOT NULL AUTO_INCREMENT,
    USER_ID BIGINT,
    ORDER_NO VARCHAR(255),
    ORDER_STATUS VARCHAR(255),
    RECEIVER_NAME VARCHAR(255),
    RECEIVER_PHONE_NUMBER VARCHAR(255),
    RECEIVER_ADDR VARCHAR(255),
    RECEIVER_POSTAL_CODE VARCHAR(255),
    TOTAL_AMOUNT BIGINT,
    DISCOUNTED_TOTAL_AMOUNT BIGINT,
    TOTAL_QUANTITY BIGINT,
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (ORDER_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS ORDER_ITEM (
    ORDER_ITEM_ID BIGINT NOT NULL AUTO_INCREMENT,
    ORDER_ID BIGINT,
    PRODUCT_OPTION_COMBINATION_ID BIGINT,
    PRICE BIGINT,
    DISCOUNTED_PRICE BIGINT,
    QUANTITY BIGINT,
    SUB_TOTAL_AMOUNT BIGINT,
    DISCOUNTED_SUB_TOTAL_AMOUNT BIGINT,
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (ORDER_ITEM_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS PRODUCT_GROUP (
    PRODUCT_GROUP_ID BIGINT NOT NULL AUTO_INCREMENT,
    CATEGORY_ID BIGINT,
    PRODUCT_GROUP_NAME VARCHAR(255),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (PRODUCT_GROUP_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS PRODUCT (
    PRODUCT_ID BIGINT NOT NULL AUTO_INCREMENT,
    PRODUCT_GROUP_ID BIGINT,
    PRODUCT_CODE VARCHAR(255),
    PRODUCT_NAME VARCHAR(255),
    MODEL_NAME VARCHAR(255),
    PURCHASE_PRICE BIGINT,
    SELLING_PRICE BIGINT,
    DESCRIPTION VARCHAR(255),
    DISPLAY_YN CHAR(1),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (PRODUCT_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS PRODUCT_OPTION (
    PRODUCT_OPTION_ID BIGINT NOT NULL AUTO_INCREMENT,
    PRODUCT_OPTION_COMBINATION_ID BIGINT,
    OPTION_CODE VARCHAR(255),
    OPTION_NAME VARCHAR(255),
    OPTION_VALUE VARCHAR(255),
    OPTION_VALUE_NAME VARCHAR(255),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (PRODUCT_OPTION_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS PRODUCT_OPTION_COMBINATION (
    PRODUCT_OPTION_COMBINATION_ID BIGINT NOT NULL AUTO_INCREMENT,
    PRODUCT_ID BIGINT,
    COMBINATION_CODE VARCHAR(255),
    UNIQUE_COMBINATION_CODE VARCHAR(255),
    COMBINATION_NAME VARCHAR(255),
    COLOR VARCHAR(255),
    SIZE VARCHAR(255),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (PRODUCT_OPTION_COMBINATION_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS PRODUCT_WAREHOUSING (
    WAREHOUSING_ID BIGINT NOT NULL AUTO_INCREMENT,
    STOCK_ID BIGINT,
    WAREHOUSING_DATE DATE,
    PRODUCT_OPTION_COMBINATION_ID BIGINT,
    QUANTITY BIGINT,
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (WAREHOUSING_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS REFRESH_TOKEN (
    REFRESH_TOKEN_ID BIGINT NOT NULL AUTO_INCREMENT,
    USER_ID BIGINT,
    TOKEN_VALUE VARCHAR(255),
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (REFRESH_TOKEN_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS STOCK (
    STOCK_ID BIGINT NOT NULL AUTO_INCREMENT,
    PRODUCT_OPTION_COMBINATION_ID BIGINT,
    PRODUCT_OPTION_COMBINATION_NAME VARCHAR(255),
    QUANTITY BIGINT,
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    PRIMARY KEY (STOCK_ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS USER (
    USER_ID BIGINT NOT NULL AUTO_INCREMENT,
    EMAIL VARCHAR(255),
    PASSWORD VARCHAR(255),
    NAME VARCHAR(255),
    PHONE VARCHAR(255),
    PRIMARY KEY (USER_ID)
) ENGINE=INNODB;

CREATE TABLE USER_ROLE (
    USER_ROLE_ID BIGINT NOT NULL AUTO_INCREMENT,
    USER_ID BIGINT,
    ROLE VARCHAR(10),
    PRIMARY KEY (`USER_ROLE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_UNICODE_CI;

ALTER TABLE CATEGORY
ADD CONSTRAINT FK__CATEGORY
FOREIGN KEY (PARENT_CATEGORY_ID)
REFERENCES CATEGORY (CATEGORY_ID);

ALTER TABLE ORDER_ITEM
ADD CONSTRAINT FK__ORDER_ITEM__ORDERS
FOREIGN KEY (ORDER_ID)
REFERENCES ORDERS (ORDER_ID);

ALTER TABLE PRODUCT
ADD CONSTRAINT FK__PRODUCT__PRODUCT_GROUP
FOREIGN KEY (PRODUCT_GROUP_ID)
REFERENCES PRODUCT_GROUP (PRODUCT_GROUP_ID);

ALTER TABLE PRODUCT_GROUP
ADD CONSTRAINT FK__PRODUCT_GROUP__CATEGORY
FOREIGN KEY (CATEGORY_ID)
REFERENCES CATEGORY (CATEGORY_ID);

ALTER TABLE PRODUCT_OPTION
ADD CONSTRAINT FK__PRODUCT_OPTION__PRODUCT_OPTION_COMBINATION
FOREIGN KEY (PRODUCT_OPTION_COMBINATION_ID)
REFERENCES PRODUCT_OPTION_COMBINATION (PRODUCT_OPTION_COMBINATION_ID);

ALTER TABLE PRODUCT_OPTION_COMBINATION
ADD CONSTRAINT FK__PRODUCT_OPTION_COMBINATION__PRODUCT
FOREIGN KEY (PRODUCT_ID)
REFERENCES PRODUCT (PRODUCT_ID);

ALTER TABLE PRODUCT_WAREHOUSING
ADD CONSTRAINT FK__PRODUCT_WAREHOUSING__STOCK
FOREIGN KEY (STOCK_ID)
REFERENCES STOCK (STOCK_ID);

ALTER TABLE USER_ROLE
ADD CONSTRAINT FK__USER_ROLE__USER
FOREIGN KEY (USER_ID)
REFERENCES USER (USER_ID);

-- 인덱스 생성
CREATE INDEX IDX__USER__PHONE ON USER(PHONE);
CREATE INDEX IDX__PRODUCT_OPTION_COMBINATION__COLOR ON PRODUCT_OPTION_COMBINATION(COLOR);
CREATE INDEX IDX__PRODUCT_OPTION_COMBINATION__SIZE ON PRODUCT_OPTION_COMBINATION(SIZE);
