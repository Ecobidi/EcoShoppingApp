package com.example.obidi.eco_shopping_mall;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopApi {
    @FormUrlEncoded
    @POST("/signin")
    Call<String> signin(@Field("username") String username, @Field("password") String password);

    @GET("/api/products/{productId}")
    Call<String> findOneProduct(@Header("Cookie") String cookie, @Path("productId") String id);

    @GET("/api/products")
    Call<String> findProducts(@Header("Cookie") String cookie, @Query("category") String category);

    @GET("api/top_categories")
    Call<String> getTopCategories(@Header("Cookie") String cookie);

    @GET("api/categories")
    Call<String> getCategories(@Header("Cookie") String cookie);

    @GET ("/api/search")
    Call<String> searchProducts(@Header("Cookie") String cookie, @Query("search") String keyword);

    @GET("/api/cart/{customerId}")
    Call<String> getCart(@Header("Cookie") String cookie, @Path("customerId") String customerId);

    @POST("/api/cart/{customerId}")
    Call<String> addToCart(@Header("Cookie") String cookie, @Path("customerId") String customerId, @Query("productId") String productId, @Query("incrementBy") int quantity);

    @DELETE("/api/cart/{customerId}")
    Call<String> deleteFromCart(@Header("Cookie") String cookie, @Path("customerId") String customerId, @Query("productId") String productId);

    @GET("/api/wishlist/{customerId}")
    Call<String> getWishlist(@Header("Cookie") String cookie, @Path("customerId") String customerId);

    @POST("/api/wishlist/{customerId}")
    Call<String> addToWishlist(@Header("Cookie") String cookie, @Path("customerId") String customerId, @Query("productId") String productId);

    @DELETE("/api/wishlist/{customerId}")
    Call<String> deleteFromWishlist(@Header("Cookie") String cookie, @Path("customerId") String customerId, @Query("productId") String productId);

    @GET("/api/recent_views/{customerId}")
    Call<String> getRecentViews(@Header("Cookie") String cookie, @Path("customerId") String customerId);

    @POST("/api/recent_views/{customerId}")
    Call<String> addToRecentViews(@Header("Cookie") String cookie, @Path("customerId") String customerId, @Query("productId") String productId);

    @GET("/api/profile")
    Call<String> getCustomer(@Header("Cookie") String cookie, @Query("customerId") String customerId);
}
