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
import com.example.obidi.eco_shopping_mall.fragments.ProductDetailFragment;
import com.example.obidi.eco_shopping_mall.models.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private static String TAG = "Product Adapter";
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mNumberOfColumns;
    private List<Product> mProducts;

    public ProductListAdapter(Context context, FragmentManager fm, int colSpan, List<Product> products) {
        mContext = context;
        mFragmentManager = fm;
        mNumberOfColumns = colSpan;
        mProducts = products;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (mNumberOfColumns == 1) {
            return new ProductViewHolder(inflater.inflate(R.layout.product_list_item, viewGroup, false));
        } else {
            return new ProductViewHolder(inflater.inflate(R.layout.product_grid_item, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position) {
        viewHolder.bindProduct(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtName;
        private TextView txtRegularPrice;
        private TextView txtSellingPrice;
        private ImageView productImage;
        private String mProductId;
        private String mProductName;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.product_name);
            txtRegularPrice = itemView.findViewById(R.id.product_regular_price);
            txtSellingPrice = itemView.findViewById(R.id.product_selling_price);
            productImage = itemView.findViewById(R.id.product_image);
            itemView.setOnClickListener(this);
        }

        public void bindProduct(Product product) {
            txtName.setText(product.getName());
            txtRegularPrice.setText("$" + product.getRegularPrice());
            txtSellingPrice.setText("$" + product.getSellingPrice());;
            productImage.setImageResource(R.drawable.iphone);
            Glide.with(mContext).load(NetworkRequestUtil.PRODUCT_IMAGE_URL + product.getImageUrl())
                    .placeholder(R.drawable.default_image_1)
                    .into(productImage);
            mProductId = product.getId();
            mProductName = product.getName();
        }

        @Override
        public void onClick(View v) {
            ProductDetailFragment fragment = ProductDetailFragment.newInstance(mProductId, mProductName);
            // navigate to selected product detail fragment
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, fragment)
                    .addToBackStack(ProductDetailFragment.TAG)
                    .commit();
        }
    }
}




