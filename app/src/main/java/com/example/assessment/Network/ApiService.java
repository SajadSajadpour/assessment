package com.example.assessment.Network;


import com.example.assessment.Model.ApiResponse;
import com.example.assessment.Model.DataWrapper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    String API_KEY = "GA5Zls7pgTguDS3El609CfU44qMzAxnu";
    String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    String API_IMAGE_BASE_URL = "http://www.nytimes.com/";

    @GET("articlesearch.json")
    Call<ApiResponse> query(
            @Query("q") String query,
            @Query("fq") String filteredQuery,
            @Query("begin_date") String beginDate,
            @Query("end_date") String endDate,
            @Query("sort") String sort,
            @Query("page") Integer page);


    @GET("mostemailed/all-sections/30.json")
    Call<DataWrapper> getMostEmailed(@Query("api-key") String apiKey);

    @GET("mostshared/all-sections/30.json")
    Call<DataWrapper> getMostShared(@Query("api-key") String apiKey);

    @GET("mostviewed/all-sections/30.json")
    Call<DataWrapper> getMostViewed(@Query("api-key") String apiKey);

    @GET("mostviewed/all-sections/30.json")
    Call<ResponseBody> getRawJSON(@Query("api-key") String apiKey);




}
