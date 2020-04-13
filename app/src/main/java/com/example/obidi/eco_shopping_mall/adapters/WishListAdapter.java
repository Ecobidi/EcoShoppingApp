package com.example.obidi.eco_shopping_mall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.fragments.ProductDetailFragment;
import com.example.obidi.eco_shopping_mall.models.Product;
import com.example.obidi.eco_shopping_mall.viewModels.WishListViewModel;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishViewHolder> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<Product> mProducts;
    private WishListViewModel mViewModel;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkRequestUtil.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build();

    public WishListAdapter(Context context, FragmentManager fragmentManager, WishListViewModel viewModel, List<Product> products) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mViewModel = viewModel;
        mProducts = products;
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new WishViewHolder(inflater.inflate(R.layout.wishlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        holder.bindProduct(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtName;
        private TextView txtPrice;
        private ImageView imgProductImage;
        private ImageButton btnDelete;
        private String mProductId;
        private String mProductName;

        public WishViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.product_name);
            txtPrice = itemView.findViewById(R.id.product_selling_price);
            imgProductImage = itemView.findViewById(R.id.product_image);
            btnDelete = itemView.findViewById(R.id.remove_wish_list_item_button);
            itemView.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        public void bindProduct(Product product) {
            txtName.setText(product.getName());
            txtPrice.setText("$" + String.valueOf(product.getSellingPrice()));
            mProductId = product.getId();
            mProductName = product.getName();
            Glide.with(mContext)
                    .load(NetworkRequestUtil.PRODUCT_IMAGE_URL + product.getImageUrl())
                    .placeholder(R.drawable.default_image_1)
                    .into(imgProductImage);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.remove_wish_list_item_button: {
                    UserSession session = UserSession.getInstance(mContext);
                    mViewModel.deleteWishListItem(session.getCookie(), session.getUserId(), mProductId);
                    break;
                }

                case R.id.wish_list_item: {
                    ProductDetailFragment fragment = ProductDetailFragment.newInstance(mProductId, mProductName);
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_activity_fragment_container, fragment)
                            .addToBackStack(ProductDetailFragment.TAG)
                            .commit();
                    break;
                }
            }
        }
    }
}
