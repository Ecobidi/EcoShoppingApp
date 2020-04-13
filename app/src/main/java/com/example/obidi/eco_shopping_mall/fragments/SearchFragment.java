package com.example.obidi.eco_shopping_mall.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obidi.eco_shopping_mall.MainActivityCallback;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.adapters.ProductListAdapter;
import com.example.obidi.eco_shopping_mall.models.Product;

import java.util.List;

public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";

    private MainActivityCallback mActivityCallback;
    private RecyclerView mRecyclerView;
    private ProductListAdapter mProductListAdapter;
    private int colSpan;

    public static  SearchFragment newInstance() {
        return new SearchFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_list_layout, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        colSpan = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), colSpan));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 8;
            }
        });
        return view;
    }

    public void updateProductsList(List<Product> products) {
        mProductListAdapter = new ProductListAdapter(getContext(), getFragmentManager(), colSpan, products);
        mRecyclerView.setAdapter(mProductListAdapter);
    }
}
