package com.example.obidi.eco_shopping_mall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.fragments.ProductListFragment;
import com.example.obidi.eco_shopping_mall.models.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<Category> mCategories;

    public CategoryListAdapter(Context context, FragmentManager fm, List<Category> categories) {
        mContext = context;
        mCategories = categories;
        mFragmentManager = fm;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindCategory(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategoryImage;
        private TextView txtCategoryName;
        private String mCategoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            imgCategoryImage = itemView.findViewById(R.id.category_image);
            txtCategoryName = itemView.findViewById(R.id.category_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductListFragment fragment = ProductListFragment.newInstance(mCategoryName);
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_activity_fragment_container, fragment)
                            .addToBackStack(fragment.getClass().getSimpleName())
                            .commit();
                }
            });
        }

        public void bindCategory(Category category) {
            mCategoryName = category.getName();
            txtCategoryName.setText(category.getName());
            Glide.with(mContext)
                    .load(NetworkRequestUtil.CATEGORY_IMAGE_URL + category.getImageUrl())
                    .placeholder(R.drawable.default_image_1)
                    .into(imgCategoryImage);
        }
    }
}
