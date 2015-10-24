package com.sayagodshala.dingdong.network;

import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.model.Order;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.util.Constants;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by sayagodshala on 9/15/2015.
 */

public interface APIService {

    //////////////////////// Auth ///////////////////

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/registerUser.php")
    Call<APIResponse<Customer>> registerUser(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("password") String password,
                                             @Field("gcm_token") String gcmToken,
                                             @Field("device_id") String deviceId,
                                             @Field("device_type") String deviceType,
                                             @Field("mobile_no") String mobileNo,
                                             @Field("type") String type,
                                             @Field("otp") String otp);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/generateOtp.php")
    Call<APIResponse> generateOtp(@Field("mobile_no") String mobileNo,
                                  @Field("email") String email,
                                  @Field("name") String name);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/loginUser.php")
    Call<APIResponse<Customer>> loginUser(@Field("email") String email,
                                          @Field("password") String password,
                                          @Field("gcm_token") String gcmToken,
                                          @Field("device_id") String deviceId,
                                          @Field("device_type") String deviceType);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/gcmTokenUpdate.php")
    Call<APIResponse> gcmTokenUpdate(@Header("user-id") String userId,
                                     @Field("gcm_token") String gcmToken);


    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/getOrders.php")
    void getOrders(@Field("order_status") String orderStatus);

    @GET(Constants.BASE_PATH + "/service/getProducts.php")
    Call<APIResponse<List<Product>>> getProducts(@Header("user-id") String userId, @Query("hour_of_day") String hourOfDay);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/postProduct.php")
    void postProduct(@Field("title") String title,
                     @Field("description") String description,
                     @Field("image") String image,
                     @Field("price") String price);

    /////////////////////////// orders ////////////////////////

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/postOrder.php")
    Call<APIResponse> postOrder(@Header("user-id") String userId,
                                @Field("address_id") String addressId,
                                @Field("product_ids") String productIds,
                                @Field("quantity") String quantity,
                                @Field("discount") String discount);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/cancelOrder.php")
    Call<APIResponse> cancelOrder(@Header("user-id") String userId,
                                  @Field("order_id") String orderId);

    @GET(Constants.BASE_PATH + "/service/getUserOrders.php")
    Call<APIResponse<List<Order>>> getMyOrders(@Header("user-id") String userId);

    ////////////////////////// address ////////

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/postAddress.php")
    Call<APIResponse<Address>> postAddress(@Header("user-id") String userId, @Field("address") String address,
                                           @Field("latlng") String latlng,
                                           @Field("landmark") String landmark);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/updateAddress.php")
    Call<APIResponse> updateAddress(@Header("user-id") String userId, @Field("address_id") String addressId,
                                    @Field("address") String address,
                                    @Field("latlng") String latlng,
                                    @Field("landmark") String landmark);

    @GET(Constants.BASE_PATH + "/service/getUserAddresses.php")
    Call<APIResponse<List<Address>>> getUserAddresses(@Header("user-id") String userId);

    @FormUrlEncoded
    @POST(Constants.BASE_PATH + "/service/deleteAddress.php")
    Call<APIResponse> deleteAddress(@Header("user-id") String userId, @Field("address_id") String addressId);

    /////////////////////////////////////////////
}
