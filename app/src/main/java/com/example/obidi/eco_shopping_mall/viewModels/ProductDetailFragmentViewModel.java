package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.models.Product;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProductDetailFragmentViewModel extends ViewModel {
    private static final String TAG = "ProductDetail ViewModel";

    private MutableLiveData<Product> mProduct = new MutableLiveData<>();
    private MutableLiveData<List<CartItem>> mCart = new MutableLiveData<>();
    private MutableLiveData<List<Product>> mWishList = new MutableLiveData<>();
    private MutableLiveData<List<Product>> mRecentViews = new MutableLiveData<>();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkRequestUtil.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    public MutableLiveData<Product> getProduct() {
        return mProduct;
    }

    public MutableLiveData<List<CartItem>> getCart() {
        return mCart;
    }

    public MutableLiveData<List<Product>> getWishList() {
        return mWishList;
    }

    public MutableLiveData<List<Product>> getRecentViews() {
        return mRecentViews;
    }

    public void fetchProduct(String cookie, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.findOneProduct(cookie, productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "Product request successful");
                    Product product = null;
                    try {
                        product = JsonParserUtil.mapJsonStringToProduct(response.body());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mProduct.setValue(product);
                } else {
                    Log.d(TAG, "Product request was not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, call.request().url() + " request Failed", t);
            }
        });
    }

    public void addToCart(String cookie, String customerId, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.addToCart(cookie, customerId, productId, 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Product added to cart");
                    try {
                        mCart.setValue(JsonParserUtil.mapJsonStringToCart(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Add To cart Failure", t);
            }
        });
    }

    public void addToWishlist(String cookie, String customerId, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.addToWishlist(cookie, customerId, productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Product added to wishlist");
                    mWishList.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Add To wishList Failure", t);
            }
        });
    }

    public void addToRecentViews(String cookie, String customerId, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.addToRecentViews(cookie, customerId, productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Product added to recent views");
                    mRecentViews.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Add To recent views Failure", t);
            }
        });
    }
}