package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.models.CartItem;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CartFragmentViewModel extends ViewModel {
    public static final String TAG = "CartFragmentViewModel";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkRequestUtil.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    private MutableLiveData<List<CartItem>> mCartItems = new MutableLiveData<>();

    public MutableLiveData<List<CartItem>> getCartItems() {
        return mCartItems;
    }

    public void fetchCartItems(String cookie, String userId) {
        ShopApi cartApi = retrofit.create(ShopApi.class);
        cartApi.getCart(cookie, userId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Cart Retrieved");
                    try {
                        mCartItems.setValue(JsonParserUtil.mapJsonStringToCart(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Cart response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Error fetching cart", t);
            }
        });
    }

    public void deleteCartItem(String cookie, String customerId, String productId) {
        ShopApi api = retrofit.create(ShopApi.class);
        api.deleteFromCart(cookie, customerId, productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Cart item deleted");
                    try {
                        mCartItems.setValue(JsonParserUtil.mapJsonStringToCart(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "cart item delete Failure", t);
            }
        });
    }

}