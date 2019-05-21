package com.rokoapp.manageApi;


import com.rokoapp.model.request.BookingCancelRequest;
import com.rokoapp.model.request.ChecksumRequest;
import com.rokoapp.model.request.EditProfileRequest;
import com.rokoapp.model.request.LiveTrackingReq;
import com.rokoapp.model.request.LoginRequest;
import com.rokoapp.model.request.OnBoardingRequest;
import com.rokoapp.model.request.PassAllocationReq;
import com.rokoapp.model.request.PhoneDetailRequest;
import com.rokoapp.model.request.PostPayment;
import com.rokoapp.model.request.ReferAndEarnRequest;
import com.rokoapp.model.request.RentalReq;
import com.rokoapp.model.request.RouteFindRequest;
import com.rokoapp.model.request.SignUpRequest;
import com.rokoapp.model.request.UniqueEmailRequest;
import com.rokoapp.model.request.UniquePhoneRequest;
import com.rokoapp.model.request.UserBookingRequest;
import com.rokoapp.model.response.BookingModel;
import com.rokoapp.model.response.ChecksumMain;
import com.rokoapp.model.response.LoginModel;
import com.rokoapp.model.response.PassesListModel;
import com.rokoapp.model.response.PostPayModel;
import com.rokoapp.model.response.ReferEarnModel;
import com.rokoapp.model.response.RouteStopData;
import com.rokoapp.model.response.TripsHistoryModel;
import com.rokoapp.model.response.UniqueEmailModel;
import com.rokoapp.model.response.UserRouteFinder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.rokoapp.utils.AppUrlUtils.CHECK_UNIQUE_EMAIL;
import static com.rokoapp.utils.AppUrlUtils.CHECK_UNIQUE_MOBILE;
import static com.rokoapp.utils.AppUrlUtils.DEVICE_DETAIL;
import static com.rokoapp.utils.AppUrlUtils.EDIT_PROFILE;
import static com.rokoapp.utils.AppUrlUtils.FIND_ROUTE;
import static com.rokoapp.utils.AppUrlUtils.LIVE_TRACKING;
import static com.rokoapp.utils.AppUrlUtils.LOGIN_URL;
import static com.rokoapp.utils.AppUrlUtils.PASSES_ALLOCATION;
import static com.rokoapp.utils.AppUrlUtils.PASSES_LIST;
import static com.rokoapp.utils.AppUrlUtils.PAY_POST;
import static com.rokoapp.utils.AppUrlUtils.PAY_REQ;
import static com.rokoapp.utils.AppUrlUtils.REFER_AND_EARN;
import static com.rokoapp.utils.AppUrlUtils.RENTALS;
import static com.rokoapp.utils.AppUrlUtils.ROUTE_ON_BOARD;
import static com.rokoapp.utils.AppUrlUtils.ROUTE_STOPS_LIST;
import static com.rokoapp.utils.AppUrlUtils.SIGN_UP_URL;
import static com.rokoapp.utils.AppUrlUtils.TRACK_LIVE_RECORD;
import static com.rokoapp.utils.AppUrlUtils.USER_BOOKING;
import static com.rokoapp.utils.AppUrlUtils.USER_BOOKING_CANCEL;
import static com.rokoapp.utils.AppUrlUtils.USER_BOOKING_HISTORY;

public interface ApiClient {

   /* User Login*/
    @POST(LOGIN_URL)
    Call<LoginModel> getUserLogin(@Body LoginRequest loginRequest);
                                 /*@Field(value = "email", encoded = true) String email,
                                  @Field("password") String password,
                                  @Field("user_type") String user_type*/


   /* User SignUp*/
    @Headers("Content-Type: application/json")
    @POST(SIGN_UP_URL)
    Call<LoginModel> getUserSignUp(@Body SignUpRequest body);


   /* User SignUp*/
   /* @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST(SIGN_UP_URL)
    Call<LoginModel> getUserSignUp(@Field("name") String name,
                                   @Field(value = "email", encoded = true) String email,
                                   @Field(value = "password", encoded = true) String password,
                                   @Field("userType") String userType,
                                   @Field("gender") String gender,
                                   @Field(value = "home_address", encoded = true) String home_address,
                                   @Field(value = "office_address", encoded = true) String office_address,
                                   @Field(value = "time_you_leave_home", encoded = true) String time_you_leave_home,
                                   @Field(value = "time_you_leave_office", encoded = true) String time_you_leave_office,
                                   @Field(value = "home_lat_long", encoded = true) String home_lat_long,
                                   @Field(value = "office_lat_long", encoded = true) String office_lat_long,
                                   @Field(value = "dob", encoded = true) String dob);*/




    /* User Edit Profile*/
    @PUT(EDIT_PROFILE)
    Call<LoginModel> getUserEditProfile(@Header("Authorization") String token,
                                        @Path("userId") String userId,
                                        @Body EditProfileRequest body);


    /*Show Route to User*/
    @POST(FIND_ROUTE)
    Call<UserRouteFinder> getRouteForUser(@Header("Authorization") String token,
                                          @Body RouteFindRequest body);
                                     /*@Field("origin_lat") double origin_lat,
                                     @Field("origin_long") double origin_long,
                                     @Field("destination_lat") double destination_lat,
                                     @Field("destination_long") double destination_long*/


    /*Booking Confirmation for User*/
    @POST(USER_BOOKING)
    Call<BookingModel> getBookingOfUser(@Header("Authorization") String token,
                                        @Body UserBookingRequest body);


    /*PASSES List for User*/
    @GET(PASSES_LIST)
    Call<PassesListModel> getPassesData();


    /*Pay Request for Checksum*/
    @POST(PAY_REQ)
    Call<ChecksumMain> getChecksumKey(@Body ChecksumRequest body);

    /*POST PAYMENT API*/
    @POST(PAY_POST)
    Call<PostPayModel> getPostPayStatus(@Header("Authorization") String token,
                                        @Body PostPayment body);

    /*Send Rental Query*/
    @POST(RENTALS)
    Call<ReferEarnModel> sendRentalQuery(@Header("Authorization") String token,
                                         @Body RentalReq body);

    /*GET DEVICE SPECIFICATIONS DETAILS*/
    @POST(DEVICE_DETAIL)
    Call<ReferEarnModel> getDeviceDetail(@Header("Authorization") String token,
                                         @Body PhoneDetailRequest body);

    /*Refer and earn*/
    @POST(REFER_AND_EARN)
    Call<ReferEarnModel> getReferAndEarn(@Header("Authorization") String token,
                                         @Body ReferAndEarnRequest body);

    /*check whether email is unique*/
    @POST(CHECK_UNIQUE_EMAIL)
    Call<UniqueEmailModel> checkUniqueEmail(@Body UniqueEmailRequest body);

    /*check whether mobile is unique*/
    @POST(CHECK_UNIQUE_MOBILE)
    Call<UniqueEmailModel> checkUniqueMobile(@Body UniquePhoneRequest body);

    /*Allocate passes to the user*/
    @POST(PASSES_ALLOCATION)
    Call<ReferEarnModel> allocatePassToUser(@Header("Authorization") String token,
                                            @Body PassAllocationReq body);

    /*User booking history*/
    @GET(USER_BOOKING_HISTORY)
    Call<TripsHistoryModel> getUserBookingHistory(@Header("Authorization") String token,
                                                  @Path("userId") String userId,
                                                  @Path("historyType") String historyType);

    /*Track Live Location*/
    @GET(TRACK_LIVE_RECORD)
    Call<TripsHistoryModel> getLiveTrackRecord(@Header("Authorization") String token,
                                               @Path("userId") String userId);

    /* ROUTE ON BOARDING*/
    @POST(ROUTE_ON_BOARD)
    Call<ReferEarnModel> getUserLogin(@Header("Authorization") String token,
                                      @Body OnBoardingRequest data);

    /*User booking history*/
    @GET(ROUTE_STOPS_LIST)
    Call<RouteStopData> getRouteStopLIst(@Header("Authorization") String token,
                                         @Path("virtualRouteId") String virtualRouteId);

    /*Send Live Location Track*/
    @POST(LIVE_TRACKING)
    Call<ReferEarnModel> sendLiveTrack(@Header("Authorization") String token,
                                       @Body LiveTrackingReq body);

    /*User Booking Cancel*/
    @POST(USER_BOOKING_CANCEL)
    Call<ReferEarnModel> getBookingCancel(@Header("Authorization") String token,
                                          @Body BookingCancelRequest body);

}