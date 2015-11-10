package com.sayagodshala.livesplash.network;

import com.sayagodshala.livesplash.model.Feed;
import com.sayagodshala.livesplash.util.Constants;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by sayagodshala on 9/15/2015.
 */

public interface APIService {

    //////////////////////// Auth ///////////////////


    @GET(Constants.BASE_PATH)
    Call<APIResponse<Feed>> loadData();


    /////////////////////////////////////////////
}
