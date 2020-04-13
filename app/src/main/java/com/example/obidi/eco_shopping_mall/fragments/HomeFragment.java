package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.obidi.eco_shopping_mall.MainActivityCallback;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.adapters.CategoryListAdapter;
import com.example.obidi.eco_shopping_mall.adapters.ProductListWithHeaderAdapter;
import com.example.obidi.eco_shopping_mall.dialogs.ProgressDialog;
import com.example.obidi.eco_shopping_mall.models.Category;
import com.example.obidi.eco_shopping_mall.viewModels.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    public static final String TAG = "HOME FRAGMENT";
    private MainActivityCallback mActivityCallback;
    private View mView;
    private ProgressDialog mProgressDialog;
    private HomeFragmentViewModel mViewModel;
    private UserSession mSession;
    private List<Category> mCategories = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
        mActivityCallback.setToolbarTitle(getString(R.string.app_name));
        mActivityCallback.updateCartBadge();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSession = UserSession.getInstance(getContext());
        mViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mProgressDialog = new ProgressDialog();
        mProgressDialog.show(getFragmentManager(), ProgressDialog.TAG);
        // fetch categories data for the home content
        mViewModel.fetchCategories(mSession.getCookie());
        mViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mActivityCallback.updateCartBadge();
                mProgressDialog.dismiss(); // dismiss loading progressBar
                mCategories = categories;
                setupCategoryList();
                setupHomeProductList();
                hostImageSlider();
            }
        });
        // fetch cart quantity
        mViewModel.fetchCartQuantity(mSession.getCookie(), mSession.getUserId());
        mViewModel.getCartQuantity().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "Should trigger cart quantity update " + integer);
                mSession.setCartQuantity(integer); // should trigger the sharedPreference change listener
                mActivityCallback.updateCartBadge();
            }
        });

        RecyclerView categoryRecyclerView = mView.findViewById(R.id.home_category_recycler_view);
        categoryRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = outRect.left = outRect.right = 8;
            }
        });

        RecyclerView categoryProductRecyclerView = mView.findViewById(R.id.home_product_list_recycler_view);
        categoryProductRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 16;
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(getString(R.string.app_name));
        mActivityCallback.updateCartBadge();
    }

    private void hostImageSlider() {
        // Host the image_slider_fragment
        ArrayList<String> imageResUrls = new ArrayList<>();
        for (int i = 0; i < 5; i++) imageResUrls.add(NetworkRequestUtil.CATEGORY_IMAGE_URL + mCategories.get(i).getImageUrl());
        ImageSliderFragment imageSliderFragment = ImageSliderFragment.newInstance(imageResUrls);
        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentById(R.id.image_slider_fragment_container) == null) {
            fm.beginTransaction()
                    .replace(R.id.image_slider_fragment_container, imageSliderFragment)
                    .commit();
        }
    }
    
    private void setupHomeProductList() {
        // references a recyclerView that will host specific category product lists
        RecyclerView categoryProductRecyclerView = mView.findViewById(R.id.home_product_list_recycler_view);
        // uses a GridLayoutManager to avoid running into the problems I encountered with LinearLayoutManager
        categoryProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        List<Category> majorCategories = mCategories.subList(0, 3);

        ProductListWithHeaderAdapter listWithHeaderAdapter =
                new ProductListWithHeaderAdapter(getContext(), getFragmentManager(), majorCategories);

        categoryProductRecyclerView.setAdapter(listWithHeaderAdapter);
    }

    private void setupCategoryList() {
        RecyclerView categoryRecyclerView = mView.findViewById(R.id.home_category_recycler_view);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        List<Category> categories = mCategories.subList(0, 4);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getContext(), getFragmentManager(), categories);
        categoryRecyclerView.setAdapter(categoryListAdapter);

        Button viewAllCategories = mView.findViewById(R.id.view_all_category);
        viewAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, CategoryFragment.newInstance())
                        .addToBackStack(CategoryFragment.TAG)
                        .commit();
            }
        });
    }

}