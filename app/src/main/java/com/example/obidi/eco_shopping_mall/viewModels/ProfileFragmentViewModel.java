package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.models.Customer;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileFragmentViewModel extends ViewModel {
    private static final String TAG = ProfileFragmentViewModel.class.getSimpleName();

    private MutableLiveData<Customer> mCustomer = new MutableLiveData<>();

    public MutableLiveData<Customer> getCustomer() {
        return mCustomer;
    }

    public void fetchCustomer(String cookie, String customerId) {
        Retrofit client = new Retrofit.Builder()
                .baseUrl(NetworkRequestUtil.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ShopApi api = client.create(ShopApi.class);
        api.getCustomer(cookie, customerId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Customer Profile successfully retrieved");
                    try {
                        Customer customer = JsonParserUtil.mapJsonStringToCustomer(response.body());
                        mCustomer.setValue(customer);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing customer jsonString", e);
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Customer Profile retrieval not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e(TAG, "Customer Profile retrieval Failure", throwable);
            }
        });
    }
}
