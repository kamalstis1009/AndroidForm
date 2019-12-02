package com.quickstart.androidform.utils;

public class ConstantKey {

    public final static String DATABASE_NAME = "kamal_database";
    public final static int DATABASE_VERSION = 1;

    public final static String COLUMN_ID = "id";

    public final static String USERS_TABLE_NAME = "users_table";
    public final static String USERS_COLUMN1 = "full_name";
    public final static String USERS_COLUMN2 = "designation";
    public final static String USERS_COLUMN3 = "email";
    public final static String USERS_COLUMN4 = "phone_number";
    public final static String USERS_COLUMN5 = "address";
    public final static String USERS_COLUMN6 = "username";
    public final static String USERS_COLUMN7 = "password";
    public final static String USERS_COLUMN8 = "user_role";
    public final static String USERS_COLUMN9 = "photo_name";
    public final static String USERS_COLUMN10 = "photo_path";
    public final static String USERS_COLUMN11 = "created_by_id";
    public final static String USERS_COLUMN12 = "updated_by_id";
    public final static String USERS_COLUMN13 = "created_at";

    public final static String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERS_COLUMN1 + " TEXT, " + USERS_COLUMN2 + " TEXT, " + USERS_COLUMN3 + " TEXT, " + USERS_COLUMN4 + " TEXT, " + USERS_COLUMN5 + " TEXT, " + USERS_COLUMN6 + " TEXT, " + USERS_COLUMN7 + " TEXT, " + USERS_COLUMN8 + " TEXT, " + USERS_COLUMN9 + " TEXT, " + USERS_COLUMN10 + " TEXT, " + USERS_COLUMN11 + " TEXT, " + USERS_COLUMN12 + " TEXT, " + USERS_COLUMN13 + " TEXT ) ";
    public final static String DROP_USERS_TABLE = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME + " ";
    public final static String SELECT_USERS_TABLE = "SELECT * FROM " + USERS_TABLE_NAME;

    public final static String INSERT_SALE_DATA1 = "INSERT INTO sales_table (product_name, product_id, product_quantity, purchase_product_quantity, customer_name, customer_id, sales_date, sales_discount, sales_vat, sales_amount, sales_payment, sales_balance, sales_description, created_by_id, updated_by_id, created_at) VALUES ('Soap', 'SID01', '10', '990', 'Mr. Musharoof', '1', '2018-08-26 00:05:30.729', '5', '15', '490', '490', '0', 'Nothing to say', 'created by kamal', '', '2018-08-26 00:05:30.729');";
    public final static String INSERT_SALE_DATA2 = "INSERT INTO sales_table (product_name, product_id, product_quantity, purchase_product_quantity, customer_name, customer_id, sales_date, sales_discount, sales_vat, sales_amount, sales_payment, sales_balance, sales_description, created_by_id, updated_by_id, created_at) VALUES ('Detergent', 'DID01', '5', '1095', 'Mr. Mamun', '3', '2018-08-26 00:05:30.729', '0', '5', '150', '150', '0', 'Nothing to say', 'created by kamal', '', '2018-08-26 00:05:30.729');";
    public final static String INSERT_SALE_DATA3 = "INSERT INTO sales_table (product_name, product_id, product_quantity, purchase_product_quantity, customer_name, customer_id, sales_date, sales_discount, sales_vat, sales_amount, sales_payment, sales_balance, sales_description, created_by_id, updated_by_id, created_at) VALUES ('Shampoo', 'SHID01', '2', '1098', 'Mr. X', '', '2018-08-26 00:05:30.729', '0', '15', '575', '575', '0', 'Nothing to say', 'created by kamal', '', '2018-08-26 00:05:30.729');";


}
