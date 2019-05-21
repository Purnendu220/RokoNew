package com.rokoapp.utils;

public class AppUrlUtils {

    /* Base Url*/
//    public static final String BASE_URL = "http://rokoexpress.com:8000/";
    public static final String BASE_URL = "http://139.59.85.45:8000/";


    /*Sub Url*/
    public static final String SIGN_UP_URL = "user/register";
    public static final String CHECK_UNIQUE_MOBILE = "user/check_unique/phone";
    public static final String LOGIN_URL = "user/login";
    public static final String EDIT_PROFILE = "user/find/{userId}";
    public static final String FIND_ROUTE = "user/route/finder";
    public static final String RENTALS = "user/rental";
    public static final String USER_BOOKING = "user/booking";
    public static final String USER_BOOKING_CANCEL = "user/booking-cancel";
    public static final String PASSES_LIST = "passes/list";
    public static final String DEVICE_DETAIL = "user/user_devices";
    public static final String REFER_AND_EARN = "user/refer_and_earn";
    public static final String CHECK_UNIQUE_EMAIL = "user/check_unique/email";
    public static final String USER_BOOKING_HISTORY = "user/user-booking-history/{userId}/{historyType}";
    public static final String TRACK_LIVE_RECORD = "user/track_live_tracking/{userId}";
    public static final String ROUTE_ON_BOARD = "user/route_attendance";
    public static final String ROUTE_STOPS_LIST = "user/route_bus_stop_listing/{virtualRouteId}";

    public static final String LIVE_TRACKING = "user/live_tracking"; //for driver app

    public static final String PAY_REQ = "payment";
    public static final String PAY_POST = "user/payment";
    public static final String PASSES_ALLOCATION = "payment/pass-allocation";



    /*public static final String VERIFY_OTP = "optAPI.php";
    public static final String RESEND_OTP = "resendOptAPI.php";
    public static final String LOGOUT_URL = "logoutAPI.php";
    public static final String FORGOT_PASSWORD = "forgotPassword.php";
    public static final String RESET_PASSWORD = "resetPassword.php";
    public static final String VIEW_PROFILE = "viewProfile.php";
    public static final String CHANGE_PASSWORD = "changePassword.php";*/

}
