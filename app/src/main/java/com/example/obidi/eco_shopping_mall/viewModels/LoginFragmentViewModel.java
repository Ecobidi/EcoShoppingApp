package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.models.Customer;
import com.google.gson.Gson;

import java.net.HttpCookie;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginFragmentViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private MutableLiveData<Boolean> loginSuccessful = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoginSuccessful() {
        return loginSuccessful;
    }

    public void signin(final Context context, final String username, final String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkRequestUtil.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ShopApi api = retrofit.create(ShopApi.class);
        Customer customer = new Customer(username, password);
        api.signin(username, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Signin successful");
                    Headers headers = response.headers();
                    List<HttpCookie> cookies = HttpCookie.parse(headers.get("Set-Cookie"));
                    HttpCookie httpCookie = cookies.get(0);
//                    System.out.println(httpCookie);
//                    System.out.println(httpCookie.getValue());
                    UserSession session = UserSession.getInstance(context);
                    session.setCookie(httpCookie.toString());
                    session.setLoginStatus(true);
                    session.setUsername(username);
                    session.setPassword(password);
                    String userId = new Gson().fromJson(response.body(), String.class);
                    session.setUserId(userId);
                    loginSuccessful.setValue(true);
                } else {
                    Log.d(TAG, "Signin unsuccessful");
                    loginSuccessful.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "User authentication Failed", t);
                loginSuccessful.setValue(false);
            }
        });
    }
}
