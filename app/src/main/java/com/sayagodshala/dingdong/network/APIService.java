package com.sayagodshala.dingdong.network;

import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.model.Customer;
import com.sayagodshala.dingdong.model.Order;
import com.sayagodshala.dingdong.model.Product;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by sayagodshala on 9/15/2015.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("/dingdong/service/registerUser.php")
    Call<APIResponse<Customer>> registerUser(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("password") String password,
                                             @Field("gcm_token") String gcmToken,
                                             @Field("device_id") String deviceId,
                                             @Field("device_type") String deviceType,
                                             @Field("mobile_no") String mobileNo,
                                             @Field("type") String type);

    @FormUrlEncoded
    @POST("/dingdong/service/loginUser.php")
    Call<APIResponse<Customer>> loginUser(@Field("email") String email,
                                          @Field("password") String password,
                                          @Field("gcm_token") String gcmToken,
                                          @Field("device_id") String deviceId,
                                          @Field("device_type") String deviceType);

    @GET("/dingdong/service/getUserAddresses.php")
    Call<APIResponse<List<Address>>> getUserAddresses(@Header("user-id") String userId);

    @FormUrlEncoded
    @POST("/dingdong/service/postAddress.php")
    Call<APIResponse<Address>> postAddress(@Header("user-id") String userId, @Field("address") String address,
                                           @Field("latlng") String latlng,
                                           @Field("landmark") String landmark);

    @FormUrlEncoded
    @POST("/dingdong/service/updateAddress.php")
    Call<APIResponse> updateAddress(@Header("user-id") String userId, @Field("address_id") String addressId,
                                    @Field("address") String address,
                                    @Field("latlng") String latlng,
                                    @Field("landmark") String landmark);

    @FormUrlEncoded
    @POST("/service/getUserAddresses.php")
    void getUserAddresses();

    @FormUrlEncoded
    @POST("/service/postAddress.php")
    void postAddress(@Field("address") String address,
                     @Field("latlng") String latlng);

    @FormUrlEncoded
    @POST("/service/postOrder.php")
    void postOrder(@Field("address_id") String addressId,
                   @Field("product_ids") String productIds,
                   @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("/service/getUserOrders.php")
    void getUserOrders();

    @FormUrlEncoded
    @POST("/service/updateOrderStatus.php")
    void updateOrderStatus(@Field("order_id") String orderId,
                           @Field("order_status") String orderStatus);

    @FormUrlEncoded
    @POST("/service/getOrders.php")
    void getOrders(@Field("order_status") String orderStatus);


    @POST("/dingdong/service/getProducts.php")
    Call<APIResponse<List<Product>>> getProducts();

    @FormUrlEncoded
    @POST("/service/postProduct.php")
    void postProduct(@Field("title") String title,
                     @Field("description") String description,
                     @Field("image") String image,
                     @Field("price") String price);

    @FormUrlEncoded
    @POST("/dingdong/service/deleteAddress.php")
    Call<APIResponse> deleteAddress(@Header("user-id") String userId, @Field("address_id") String addressId);

    @GET("/dingdong/service/getUserOrders.php")
    Call<APIResponse<List<Order>>> getMyOrders(@Header("user-id") String userId);


}
