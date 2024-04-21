CREATE TABLE IF NOT EXISTS category (
    level integer,
    category_id bigint not null auto_increment,
    created_at datetime(6),
    parent_category_id bigint,
    updated_at datetime(6),
    name varchar(255),
    path_name varchar(255),
    primary key (category_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_item (
    created_at datetime(6),
    discounted_price bigint,
    discounted_sub_total_amount bigint,
    order_id bigint,
    order_item_id bigint not null auto_increment,
    price bigint,
    product_option_combination_id bigint,
    quantity bigint,
    sub_total_amount bigint,
    updated_at datetime(6),
    primary key (order_item_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
    created_at datetime(6),
    discounted_total_amount bigint,
    order_id bigint not null auto_increment,
    total_amount bigint,
    total_quantity bigint,
    updated_at datetime(6),
    user_id bigint,
    order_no varchar(255),
    order_status varchar(255),
    receiver_addr varchar(255),
    receiver_name varchar(255),
    receiver_phone_number varchar(255),
    receiver_postal_code varchar(255),
    primary key (order_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product (
    created_at datetime(6),
    product_group_id bigint,
    product_id bigint not null auto_increment,
    purchase_price bigint,
    selling_price bigint,
    updated_at datetime(6),
    description varchar(255),
    display_yn varchar(255) check (display_yn in ('N','Y')),
    model_name varchar(255),
    product_code varchar(255),
    product_name varchar(255),
    primary key (product_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_group (
    category_id bigint,
    created_at datetime(6),
    product_group_id bigint not null auto_increment,
    updated_at datetime(6),
    product_group_name varchar(255),
    primary key (product_group_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_option (
    created_at datetime(6),
    product_option_combination_id bigint,
    product_option_id bigint not null auto_increment,
    updated_at datetime(6),
    option_code varchar(255),
    option_name varchar(255),
    option_value varchar(255),
    option_value_name varchar(255),
    primary key (product_option_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_option_combination (
    created_at datetime(6),
    product_id bigint,
    product_option_combination_id bigint not null auto_increment,
    updated_at datetime(6),
    combination_code varchar(255),
    combination_name varchar(255),
    filtering_option varchar(255),
    unique_combination_code varchar(255),
    primary key (product_option_combination_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_warehousing (
    warehousing_date date,
    created_at datetime(6),
    product_option_combination_id bigint,
    quantity bigint,
    stock_id bigint,
    updated_at datetime(6),
    warehousing_id bigint not null auto_increment,
    primary key (warehousing_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS refresh_token (
    created_at datetime(6),
    refresh_token_id bigint not null auto_increment,
    updated_at datetime(6),
    user_id bigint,
    token_value varchar(255),
    primary key (refresh_token_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS stock (
    created_at datetime(6),
    product_option_combination_id bigint,
    quantity bigint,
    stock_id bigint not null auto_increment,
    updated_at datetime(6),
    product_option_combination_name varchar(255),
    primary key (stock_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    user_id bigint not null auto_increment,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    phone varchar(255),
    primary key (user_id)
) ENGINE=InnoDB;

ALTER TABLE category
ADD CONSTRAINT FK__CATEGORY
FOREIGN KEY (parent_category_id)
REFERENCES category (category_id);

ALTER TABLE order_item
ADD CONSTRAINT FK__ORDER_ITEM__ORDERS
FOREIGN KEY (order_id)
REFERENCES orders (order_id);

ALTER TABLE product
ADD CONSTRAINT FK__PRODUCT__PRODUCT_GROUP
FOREIGN KEY (product_group_id)
REFERENCES product_group (product_group_id);

ALTER TABLE product_group
ADD CONSTRAINT FK__PRODUCT_GROUP__CATEGORY
FOREIGN KEY (category_id)
REFERENCES category (category_id);

ALTER TABLE product_option
ADD CONSTRAINT FK__PRODUCT_OPTION__PRODUCT_OPTION_COMBINATION
FOREIGN KEY (product_option_combination_id)
REFERENCES product_option_combination (product_option_combination_id);

ALTER TABLE product_option_combination
ADD CONSTRAINT FK__PRODUCT_OPTION_COMBINATION__PRODUCT
FOREIGN KEY (product_id)
REFERENCES product (product_id);

ALTER TABLE product_warehousing
ADD CONSTRAINT FK__PRODUCT_WAREHOUSING__STOCK
FOREIGN KEY (stock_id)
REFERENCES stock (stock_id);
