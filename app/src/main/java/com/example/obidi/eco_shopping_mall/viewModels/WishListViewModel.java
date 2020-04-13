package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.models.Product;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WishListViewModel extends ViewModel {
    private static final String TAG = "WishListViewModel";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkRequestUtil.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private MutableLiveData<List<Product>> mProducts = new MutableLiveData<>();

    public MutableLiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public void fetchWishList(String cookie, String customerId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.getWishlist(cookie, customerId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Wishlist successfully retrieved");
                    try {
                        mProducts.setValue(JsonParserUtil.mapJsonStringToProducts(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failure retrieving wishList", t);
            }
        });
    }

    public void deleteWishListItem(String cookie, String customerId, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.deleteFromWishlist(cookie, customerId, productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Wish item delete success");
                    try {
                        Log.d(TAG, response.body());
                        mProducts.setValue(JsonParserUtil.mapJsonStringToProducts(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Wish item delete Failure", t);
            }
        });
    }
}
