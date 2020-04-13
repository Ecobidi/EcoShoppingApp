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
import com.example.obidi.eco_shopping_mall.adapters.CategoryListAdapter;
import com.example.obidi.eco_shopping_mall.models.Category;
import com.example.obidi.eco_shopping_mall.viewModels.CategoryFragmentViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {
    public static final String TAG = "CategoryFragment";

    private MainActivityCallback mActivityCallback;
    private CategoryFragmentViewModel mViewModel;
    private RecyclerView mRecyclerView;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
        mActivityCallback.setToolbarTitle(getString(R.string.categories_title));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CategoryFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_list_layout, container, false);
        mViewModel.fetchCategories(getContext());
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 24;
            }
        });

        mViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                mActivityCallback.updateCartBadge();
                updateList(categories);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(getString(R.string.categories_title));
        mActivityCallback.updateCartBadge();
    }

    private void updateList(List<Category> categories) {
        CategoryListAdapter adapter = new CategoryListAdapter(getContext(), getFragmentManager(), categories);
        mRecyclerView.setAdapter(adapter);
    }
}
