package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obidi.eco_shopping_mall.MainActivityCallback;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.adapters.WishListAdapter;
import com.example.obidi.eco_shopping_mall.dialogs.ProgressDialog;
import com.example.obidi.eco_shopping_mall.models.Product;
import com.example.obidi.eco_shopping_mall.viewModels.WishListViewModel;

import java.util.List;

public class WishListFragment extends Fragment {
    public static final String TAG = "WishListFragment";

    private MainActivityCallback mActivityCallback;
    private ProgressDialog mProgressDialog;
    private View mEmptyView;
    private WishListViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private String mCookie;
    private String mCustomerId;

    public static WishListFragment newInstance() {
        return new WishListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
        mActivityCallback.setToolbarTitle(getString(R.string.wishlist_title));
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
        mViewModel = ViewModelProviders.of(this).get(WishListViewModel.class);
        UserSession session = UserSession.getInstance(getContext());
        mCookie = session.getCookie();
        mCustomerId = session.getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_list_layout, container, false);
        mProgressDialog = new ProgressDialog();
        mProgressDialog.show(getFragmentManager(), ProgressDialog.TAG);
        mEmptyView = view.findViewById(R.id.empty_view);

        mRecyclerView = view.findViewById(R.id.recycler_view); // non_empty_view
        // fetch wishList
        mViewModel.fetchWishList(mCookie, mCustomerId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 16;
            }
        });
        mViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                mProgressDialog.dismiss();
                updateList(products);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(getString(R.string.wishlist_title));
    }

    private void updateList(List<Product> products) {
        if (products.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            WishListAdapter listAdapter = new WishListAdapter(getContext(), getFragmentManager(), mViewModel, products);
            mRecyclerView.setAdapter(listAdapter);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }
}
