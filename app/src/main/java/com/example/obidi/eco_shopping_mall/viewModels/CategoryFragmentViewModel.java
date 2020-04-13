package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.models.Category;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CategoryFragmentViewModel extends ViewModel {
    private static final String TAG = "Category ViewModel";
    private MutableLiveData<List<Category>> mCategories = new MutableLiveData<>();

    public MutableLiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public void fetchCategories(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkRequestUtil.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ShopApi shopApi = retrofit.create(ShopApi.class);
        UserSession session = UserSession.getInstance(context);
        String cookie = session.getCookie();
        Call<String> call = shopApi.getCategories(cookie);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        List<Category> categories = JsonParserUtil.mapJsonStringToCategories(response.body(), false);
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
}
