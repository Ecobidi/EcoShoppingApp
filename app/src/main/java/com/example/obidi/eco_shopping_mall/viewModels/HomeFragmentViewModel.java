package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.models.Category;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "home fragment viewModel";
    private static final String CATEGORY_BASE_URL = "http://10.0.3.2:3000";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkRequestUtil.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private MutableLiveData<List<Category>> mCategories = new MutableLiveData<>();
    private MutableLiveData<Integer> mCartQuantity = new MutableLiveData<>();

    public MutableLiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public MutableLiveData<Integer> getCartQuantity() { return mCartQuantity; }

    public void fetchCategories(String cookie) {
        ShopApi shopApi = retrofit.create(ShopApi.class);
        Call<String> call = shopApi.getTopCategories(cookie);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        System.out.println(response.body());
                        List<Category> categories = JsonParserUtil.mapJsonStringToCategories(response.body(), true);
                        if (categories != null) mCategories.setValue(categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Fetch Categories Failed", t);
            }
        });
    }

    public void fetchCartQuantity(String cookie, String customerId) {
        ShopApi shopApi = retrofit.create(ShopApi.class);
        Call<String> call = shopApi.getCart(cookie, customerId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        List<CartItem> cart = JsonParserUtil.mapJsonStringToCart(response.body());
                        mCartQuantity.setValue(cart.size());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Fetch Categories Failed", t);
            }
        });
    }
}
