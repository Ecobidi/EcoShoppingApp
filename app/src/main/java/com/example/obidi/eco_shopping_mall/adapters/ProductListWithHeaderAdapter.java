package com.example.obidi.eco_shopping_mall.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.fragments.ProductListFragment;
import com.example.obidi.eco_shopping_mall.models.Category;
import com.example.obidi.eco_shopping_mall.models.Product;

import java.util.List;

public class ProductListWithHeaderAdapter extends RecyclerView.Adapter<ProductListWithHeaderAdapter.ProductListHolder> {
    private Context mContext;
    private List<Category> mCategories;
    private FragmentManager mFragmentManager;
    private int mNumberOfCategories;

    public ProductListWithHeaderAdapter(Context context, FragmentManager fm, List<Category> categories) {
        mContext = context;
        mCategories = categories;
        mFragmentManager = fm;
    }

    @NonNull
    @Override
    public ProductListWithHeaderAdapter.ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.category_product_list, parent, false);
        // the above inflated view is a ViewGroup that contains a header view and
        // recycler view that will hold the list of this category's products
        return new ProductListWithHeaderAdapter.ProductListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListWithHeaderAdapter.ProductListHolder holder, int position) {
        // determine whether the child recyclerView will host a single or double column layoutManager
        int colSpan = (position % 2 == 1) ? 1 : 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, colSpan);
        Category currentCategory = mCategories.get(position);
        holder.bindCategory(currentCategory.getName(), currentCategory.getProducts(), layoutManager, colSpan);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    /*
     * ProductListHolder displays a textView which contains category name,
     * a button to navigate to the full category products list,
     * and a recyclerView that displays the products in the category
     */
    public class ProductListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCategoryNameTextView;
        private Button mCategoryViewButton;
        private RecyclerView mProductsRecyclerView;
        private Context mContext;

        private String mCategoryName;

        public ProductListHolder(View itemView) {
            super(itemView);
            mCategoryNameTextView = itemView.findViewById(R.id.category_name);
            mCategoryViewButton = itemView.findViewById(R.id.category_view_button);
            mProductsRecyclerView = itemView.findViewById(R.id.product_recyler_view);
            mContext = mProductsRecyclerView.getContext();

            mCategoryViewButton.setOnClickListener(this);
        }

        public void bindCategory(String categoryName, List<Product> products,
                                 RecyclerView.LayoutManager layoutManager, int colSpan) {
            mCategoryName = categoryName; // TESTING: just for displaying category name in a Toast
            if (categoryName == null) {
                mCategoryNameTextView.setVisibility(View.GONE);
                mCategoryViewButton.setVisibility(View.GONE);
            } else {
                mCategoryNameTextView.setText(categoryName); // set the category title textView
            }
            // adapter that will service the productsRecyclerView with this category's products
            mProductsRecyclerView.setLayoutManager(layoutManager);
            mProductsRecyclerView.setAdapter(new ProductListAdapter(mContext, mFragmentManager, colSpan, products));
            // item decorator for spacing each product item in the recyclerView
            mProductsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.bottom = 16;
                    outRect.top = 16;
                    outRect.left = 16;
                    outRect.right = 16;
                }
            });
        }

        @Override
        public void onClick(View v) {
            // handle category page navigation
            Toast.makeText(mContext, "Navigating to " + mCategoryName + " category page", Toast.LENGTH_SHORT).show();
            ProductListFragment productListFragment = ProductListFragment.newInstance(mCategoryName);
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, productListFragment)
                    .addToBackStack(mCategoryName)
                    .commit();
        }
    }
}


