package com.example.obidi.eco_shopping_mall.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.obidi.eco_shopping_mall.ShopApi;
import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.models.Product;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProductListFragmentViewModel extends ViewModel {
    private static final String TAG = "Product list viewModel";

    private MutableLiveData<List<Product>> mProducts = new MutableLiveData<>();

    public MutableLiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public List<Product> fetchProducts(Context context, final String category) {
        /*AsyncTask<URL, Void, List<Product>> fetchProductsTask = new FetchProductsTask();
        fetchProductsTask.execute(buildUri());*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkRequestUtil.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ShopApi api = retrofit.create(ShopApi.class);
        UserSession session = UserSession.getInstance(context);
        String cookie = session.getCookie();
        api.findProducts(cookie, category).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, category + " products request successful");
                    List<Product> products = null;
                    try {
                        products = JsonParserUtil.mapJsonStringToProducts(response.body());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mProducts.setValue(products); // triggers ui update
                } else {
                    Log.d(TAG, category + " products request failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Error Fetching "+ category +" products", t);
            }
        });

        return getProducts().getValue();
    }

    /*private URL buildUri() {
        HashMap<String, String[]> queryHashMap = new HashMap<>();
        queryHashMap.put("category", new String[]{mCategory});
        URL url = null;
        try {
            url = new URL(PROTOCOL, DOMAIN, PORT, PATH);
            url = new URL(NetworkRequestUtil.buildUriQuery(url.toString(), queryHashMap));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
        }
    */

}
