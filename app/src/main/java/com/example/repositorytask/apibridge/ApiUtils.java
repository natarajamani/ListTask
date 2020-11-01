package com.example.repositorytask.apibridge;


import com.example.repositorytask.apibridge.APIService;

/**
 * This class is used to maintain API related variables
 */
public class ApiUtils {

    /**
     * Base url of API
     */

//    public static final String BASE_URL = "https://ghapi.huchen.dev/";
    public static final String BASE_URL = "https://private-anon-fa7096e636-githubtrendingapi.apiary-mock.com/";

    /**
     * To get retrofit client variable from base url
     * @return API interface from retrofit client
     */
    public static APIService getAPIService(String url) {
        return RetrofitClient.getClient(url).create(APIService.class);
    }

}
