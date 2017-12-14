/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author vietduc
 */
public class Config {
    public static final int MAX_FETCH_RESUTL = 10;
    public static final int MAX_SEARCH_RESUTL = 50;
    
    public static final String VERY_SECRET_TOKEN = "z11.686868a@";
    
    public static final String USERNAME_EMAIL = "bluetulips6@gmail.com";
    public static final String PASSWORD_EMAIL = "vietnam1";

    public static final int MAX_IMAGE_SIZE = 500000; // 500k
    public static final String PROFILE_PICTURE_DIRECTORY = "profilepic";
    
    public static final String PERSISTENCE_UNIT_NAME = "http-z11-auth-api2-1.0-PU";
    public static final String EMAIL_VALIDATOR = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    public static final String PASSWORD_VALIDATOR = "((?=.*\\d)(?=.*[a-z]).{6,20})";
    public static final String USERID_VALIDATOR = "^[a-z0-9_.-]{3,40}$";
    
    public static final String PT_PAY = "PAY"; // Payment type
    public static final String PT_TRANSFER = "TRANSFER";
    public static final String PT_CHARGE = "CHARGE";
    public static final String PT_BENEFIT = "BENEFIT";
    public static final String PT_CONVERT = "CONVERT";
    
    public static final String ROLE_ADMIN = "admins";
    public static final String ROLE_MODS = "mods";
    public static final String ROLE_USERS = "users";
    
    public static final String PMS_ALL_PERMISSION = "pms_all_permissions";
    
    
    public static final String PMS_POST_PERMISSIONS = "pms_post_permissions";
    public static final String PMS_DELETE_PERMISSIONS = "pms_delete_permissions";
    public static final String PMS_GET_PERMISSIONS = "pms_get_permissions";
    public static final String PMS_PUT_PERMISSIONS = "pms_put_permissions";
    
    public static final String PMS_POST_ROLES = "pms_post_roles";
    public static final String PMS_PUT_ROLES = "pms_put_roles";
    public static final String PMS_GET_ROLES = "pms_get_roles";
    public static final String PMS_DELETE_ROLES = "pms_delete_roles";
    
    public static final String PMS_POST_ROLE_PMS = "pms_post_role_pms";
    public static final String PMS_PUT_ROLE_PMS = "pms_put_role_pms";
    public static final String PMS_GET_ROLE_PMS = "pms_get_role_pms";
    public static final String PMS_DELETE_ROLE_PMS = "pms_delete_role_pms";
    
    public static final String PMS_GET_PURCHASES = "pms_get_purchases";
    public static final String PMS_POST_PURCHASES = "pms_post_purchases";
    public static final String PMS_PUT_PURCHASES = "pms_put_purchases";
    public static final String PMS_DELETE_PURCHASES = "pms_delete_purchases";
    
    public static final String PMS_GET_PAYMENTS = "pms_get_payments";
    public static final String PMS_POST_PAYMENTS = "pms_post_payments";
    public static final String PMS_PUT_PAYMENTS = "pms_put_payments";
    public static final String PMS_DELETE_PAYMENTS = "pms_delete_payments";
    
    public static final String PMS_GET_SESSIONS = "pms_get_sessions";
    public static final String PMS_POST_SESSIONS = "pms_post_sessions";
    public static final String PMS_PUT_SESSIONS = "pms_put_sessions";
    public static final String PMS_DELETE_SESSIONS = "pms_delete_sessions";
    
    public static final String PMS_GET_USERS = "pms_get_users";
    public static final String PMS_POST_USERS = "pms_post_users";
    public static final String PMS_PUT_USERS = "pms_put_users";
    public static final String PMS_DELETE_USERS = "pms_delete_users";
    public static final String PMS_PUT_BALANCE_USERS = "pms_put_balance_users";
    
    public static final String PMS_GET_APPS = "pms_get_apps";
    public static final String PMS_PUT_APPS = "pms_put_apps";
    public static final String PMS_POST_APPS = "pms_post_apps";
    public static final String PMS_DELETE_APPS = "pms_delete_apps";
    
    public static final String PMS_GET_PRODUCTS = "pms_get_products";
    public static final String PMS_PUT_PRODUCTS = "pms_put_products";
    public static final String PMS_POST_PRODUCTS = "pms_post_products";
    public static final String PMS_DELETE_PRODUCTS = "pms_delete_products";
    
    public static final String PMS_GET_USER_APPS = "pms_get_user_apps";
    public static final String PMS_PUT_USER_APPS = "pms_put_userapps";
    public static final String PMS_POST_USER_APPS = "pms_post_userapps";
    public static final String PMS_DELETE_USER_APPS = "pms_delete_userapps";
    
    public static final String PMS_GET_EXTERNAL_LOGINS = "pms_get_external_logins";
    public static final String PMS_PUT_EXTERNAL_LOGINS = "pms_put_external_logins";
    public static final String PMS_POST_EXTERNAL_LOGINS = "pms_post_external_logins";
    public static final String PMS_DELETE_EXTERNAL_LOGINS = "pms_delete_external_logins";


    public static void main(String[] args) {
           System.out.println("Working Directory = " +
                  System.getProperty("user.dir"));
      }

}