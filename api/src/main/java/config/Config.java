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

    public static final String STATUS_NEW = "new";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_INPROGRESS = "inprogress";
    public static final String STATUS_RESOLVED = "resolved";
    public static final String STATUS_FEEDBACK = "feedback";
    public static final String STATUS_CLOSED = "closed";

    public static final short STATUS_UNREAD = 0;
    public static final short STATUS_READ = 1;

    public static final String PMS_PUT_REQUEST_TEAM = "pms_put_request_team";
    public static final String PMS_GET_REQUEST_TEAM = "pms_get_request_team";
    public static final String PMS_GET_REQUEST_PART = "pms_get_request_part";
    public static final String PMS_ALL = "pms_all";

    public static final String PRIORITY_1 = "Thấp";
    public static final String PRIORITY_2 = "Bình thường";
    public static final String PRIORITY_3 = "Cao";
    public static final String PRIORITY_4 = "Khẩn cấp";

    public static final String RATING_0 = "không hài lòng";
    public static final String RATING_1 = "hài lòng";

    public static void main(String[] args) {
        System.out.println("Working Directory = "
                + System.getProperty("user.dir"));
    }

}
