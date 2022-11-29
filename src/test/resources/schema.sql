--CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
--UPDATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

CREATE SCHEMA ADM;

create table ADM.CUSTOMER
(
   CUST_NO varchar(255) not null,
   FIRST_NAME varchar(255) ,
   LAST_NAME varchar(255) ,
   AGE integer ,
   CREATED_AT DATETIME ,
   UPDATED_AT DATETIME ,
   primary key(CUST_NO)
);

create table ADM.TORDER
(
    CUST_NO varchar(255) not null,
    ORD_NO varchar(255) not null,
    ORD_SEQ integer,
    ORD_DTM DATETIME ,
    NAME varchar(255) ,
    ITEM_CD varchar(255) ,
    CREATED_AT DATETIME ,
    UPDATED_AT DATETIME ,
    primary key(CUST_NO, ORD_NO, ORD_SEQ)
);

create table ADM.CART
(
    CUST_NO varchar(255) not null,
    CART_SEQ integer not null,
    CREATED_AT DATETIME ,
    UPDATED_AT DATETIME ,
    primary key(CUST_NO, CART_SEQ)
);


create table ADM.ITEM
(
    ITEM_SEQ integer not null,
    primary key(ITEM_SEQ)

create table ADM.STUDENT
(
    STD_SEQ bigint not null,
    GRADE varchar(255) not null,
    primary key(STD_SEQ)
);

create sequence ADM.ORDER_SEQUENCE;
create sequence ADM.ITEM_SEQUENCE;