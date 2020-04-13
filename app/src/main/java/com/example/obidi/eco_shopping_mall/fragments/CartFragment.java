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
import com.example.obidi.eco_shopping_mall.adapters.CartAdapter;
import com.example.obidi.eco_shopping_mall.dialogs.ProgressDialog;
import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.viewModels.CartFragmentViewModel;

import java.util.List;

public class CartFragment extends Fragment {
    public static final String TAG = "CartFragment";
    private MainActivityCallback mActivityCallback;
    private ProgressDialog mProgressDialog;
    private CartFragmentViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private View mNonEmptyView;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
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
        mViewModel = ViewModelProviders.of(this).get(CartFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mProgressDialog = new ProgressDialog();
        mProgressDialog.show(getFragmentManager(), ProgressDialog.TAG);
        mEmptyView = view.findViewById(R.id.empty_cart_view);
        mNonEmptyView = view.findViewById(R.id.non_empty_cart_view);
        mRecyclerView = view.findViewById(R.id.cart_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final UserSession session = UserSession.getInstance(getContext());
        mViewModel.getCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(@Nullable List<CartItem> products) {
                mProgressDialog.dismiss();
                updateCart(products);
                session.setCartQuantity(products.size());
                mActivityCallback.updateCartBadge();// update activity cartBadge
            }
        });
        mViewModel.fetchCartItems(session.getCookie(), session.getUserId());
        // add item decorator
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = outRect.left = outRect.right = 8;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(getString(R.string.cart_title));
    }

    private void updateCart(List<CartItem> cart) {
        if (cart.size() == 0) { // hide cart
            mEmptyView.setVisibility(View.VISIBLE);
            mNonEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mNonEmptyView.setVisibility(View.VISIBLE);
            CartAdapter adapter = new CartAdapter(getContext(), getFragmentManager(), mViewModel, cart);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
