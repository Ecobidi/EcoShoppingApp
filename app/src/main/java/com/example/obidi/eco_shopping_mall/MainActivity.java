package com.example.obidi.eco_shopping_mall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obidi.eco_shopping_mall.Utils.JsonParserUtil;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.fragments.CartFragment;
import com.example.obidi.eco_shopping_mall.fragments.CategoryFragment;
import com.example.obidi.eco_shopping_mall.fragments.HomeFragment;
import com.example.obidi.eco_shopping_mall.fragments.ProfileFragment;
import com.example.obidi.eco_shopping_mall.fragments.SearchFragment;
import com.example.obidi.eco_shopping_mall.fragments.WishListFragment;
import com.example.obidi.eco_shopping_mall.models.Product;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements MainActivityCallback, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private SearchFragment mSearchFragment;
    private UserSession mSession;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        // setup navigation toggle button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this); // set navigationView item listener
        hostFragment(HomeFragment.newInstance()); // host homeFragment
        mSession = UserSession.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem cartMenuItem = menu.findItem(R.id.cart_menu_item);
        View cartBadge = cartMenuItem.getActionView();
        cartBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment fragment = CartFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, fragment)
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .commit();
            }
        });
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "search bar closed");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() < 2) {
                    Toast.makeText(MainActivity.this, "at least 3 keyword length", Toast.LENGTH_SHORT).show();
                }
                performSearch(query);
                return true; // signifies that search has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        // close drawer layout if open
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: {
                hostFragment(ProfileFragment.newInstance());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.wishlist: {
                hostFragment(WishListFragment.newInstance());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.order: {
                Toast.makeText(this, "Orders Selected", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.cart: {
                hostFragment(CartFragment.newInstance());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.category: {
                hostFragment(CategoryFragment.newInstance());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            default: {
                Toast.makeText(this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
        }

        return false;
    }

    private void hostFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private void performSearch(String keyword) {
        Log.d(TAG, "searching... for " + keyword);
        if (mSearchFragment == null) {
            mSearchFragment = SearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment_container, mSearchFragment)
                    .addToBackStack(SearchFragment.TAG)
                    .commit();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkRequestUtil.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ShopApi searchApi = retrofit.create(ShopApi.class);
        Call<String> call = searchApi.searchProducts(mSession.getCookie(), keyword);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Search results successfully retrieved");
                    List<Product> products = null;
                    try {
                        products = JsonParserUtil.mapJsonStringToProducts(response.body());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        mSearchFragment.updateProductsList(products);
                    }
                } else {
                    Log.d(TAG, "Search not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Search Request Failure", t);
            }
        });
    }

    @Override
    public void updateCartBadge() {
        Log.d(TAG, "updateCartBadge() called");
        int cartQuantity = mSession.getCartQuantity();
        TextView cartBadgeTextView = findViewById(R.id.cart_badge_textview);
        if (cartBadgeTextView != null) {
            cartBadgeTextView.setText(String.valueOf(cartQuantity));
        } else {
            Log.d(TAG, "cartBadge is null");
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
