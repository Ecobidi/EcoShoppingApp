package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.example.obidi.eco_shopping_mall.viewModels.ProductListFragmentViewModel;

import java.util.List;

public class ProductListFragment extends Fragment {
    private static final String ARG_CATEGORY = "category";
    private MainActivityCallback mActivityCallback;
    private ProductListFragmentViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ProductListAdapter mProductListAdapter;
    private int mNumberOfColumns;
    private int decoratorTop, decoratorBottom, decoratorLeft, decoratorRight;
    private String mCategory;

    public static ProductListFragment newInstance(String category) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(ProductListFragment.this).get(ProductListFragmentViewModel.class);
        mCategory = getArguments().getString(ARG_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mNumberOfColumns = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mNumberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        if (mNumberOfColumns >= 2) { // spacing on all the sides of the item
            decoratorTop = decoratorLeft = decoratorRight = decoratorBottom = 8;
        } else { // spacing on only the top and bottom side
            decoratorTop = decoratorBottom = 8; decoratorLeft = decoratorRight = 0;
        }
        // item decorator for spacing items in the recyclerView
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = decoratorBottom;
                outRect.top = decoratorTop;
                outRect.left = decoratorLeft;
                outRect.right = decoratorRight;
            }
        });
        mViewModel.fetchProducts(getContext(), mCategory); //fetchProducts
        mViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                updateUI(products);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(mCategory);
    }

    private void updateUI(List<Product> products) {
        mProductListAdapter = new ProductListAdapter(getContext(), getFragmentManager(), mNumberOfColumns, products);
        mRecyclerView.setAdapter(mProductListAdapter);
    }
}
