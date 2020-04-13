package com.example.obidi.eco_shopping_mall.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.databinding.CartListItemBinding;
import com.example.obidi.eco_shopping_mall.fragments.ProductDetailFragment;
import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.viewModels.CartFragmentViewModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private CartFragmentViewModel mViewModel;
    private List<CartItem> mCart;

    public CartAdapter(Context context, FragmentManager fm, CartFragmentViewModel viewModel, List<CartItem> cart) {
        mContext = context;
        mFragmentManager = fm;
        mViewModel = viewModel;
        mCart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        CartListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_list_item, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(mCart.get(position));
    }

    @Override
    public int getItemCount() {
        return mCart.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CartListItemBinding mItemBinding;
        private String mProductId;
        private String mProductName;

        public CartViewHolder(CartListItemBinding binding) {
            super(binding.getRoot());
            mItemBinding = binding;
            mItemBinding.removeCartItemButton.setOnClickListener(this);
            mItemBinding.getRoot().setOnClickListener(this);
        }

        public void bind(CartItem item) {
            mProductId = item.getProduct().getId();
            mProductName = item.getProduct().getName();
            mItemBinding.setItem(item);
            mItemBinding.executePendingBindings();
            Glide.with(mContext)
                    .load(NetworkRequestUtil.PRODUCT_IMAGE_URL + item.getProduct().getImageUrl())
                    .placeholder(R.drawable.default_image_1)
                    .into(mItemBinding.productImage);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.remove_cart_item_button: {
                    UserSession session = UserSession.getInstance(mContext);
                    mViewModel.deleteCartItem(session.getCookie(), session.getUserId(), mProductId);
                    break;
                }

                case R.id.cart_item: {
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
