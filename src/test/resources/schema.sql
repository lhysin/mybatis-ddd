CREATE SCHEMA ADM;

create table ADM.CUSTOMER
(
   CUST_NO varchar(255) not null,
   FIRST_NAME varchar(255) ,
   LAST_NAME varchar(255) ,
   AGE integer ,
   primary key(CUST_NO)
);

create table ADM.TORDER
(
    CUST_NO varchar(255) not null,
    ORD_NO varchar(255) not null,
    ORD_SEQ integer,
    ORD_DTM DATE ,
    NAME varchar(255) ,
    ITEM_CD varchar(255) ,
    primary key(CUST_NO, ORD_NO)
);

create table ADM.CART
(
    CUST_NO varchar(255) not null,
    CART_SEQ integer not null,
    primary key(CUST_NO, CART_SEQ)
);

create sequence ADM.CART_SEQUENCE;