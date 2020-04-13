package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.MainActivityCallback;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.models.CartItem;
import com.example.obidi.eco_shopping_mall.models.Product;
import com.example.obidi.eco_shopping_mall.viewModels.ProductDetailFragmentViewModel;

import java.util.List;

public class ProductDetailFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "Product Detail Fragment";
    private static final String PRODUCT_ID = "product id";
    private static final String PRODUCT_NAME = "product name";

    private View mView;
    private MainActivityCallback mActivityCallback;
    private ProductDetailFragmentViewModel mViewModel;
    private String mCookie;
    private String mProductID;
    private String mProductName;
    private String mCustomerId;

    public static ProductDetailFragment newInstance(String productId, String productName) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(PRODUCT_NAME, productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
        mActivityCallback.setToolbarTitle(mProductName);
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
        mProductID = getArguments().getString(PRODUCT_ID);
        mProductName = getArguments().getString(PRODUCT_NAME);
        mViewModel = ViewModelProviders.of(this).get(ProductDetailFragmentViewModel.class);
        mCookie = UserSession.getInstance(getContext()).getCookie();
        mCustomerId = UserSession.getInstance(getContext()).getUserId();
        // add to recent_viewed products
        mViewModel.addToRecentViews(mCookie, mCustomerId, mProductID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        final ImageView imageProduct = mView.findViewById(R.id.product_slide_image);
        final TextView txtName = mView.findViewById(R.id.product_name);
        final TextView txtSellingPrice = mView.findViewById(R.id.product_selling_price);
        final TextView txtRegularPrice = mView.findViewById(R.id.product_regular_price);
        final TextView txtDescription = mView.findViewById(R.id.product_description);
        final Button btnAddToCart = mView.findViewById(R.id.add_to_cart_button);
        final Button btnAddToWish = mView.findViewById(R.id.add_to_wishlist_button);

        btnAddToCart.setOnClickListener(this);
        btnAddToWish.setOnClickListener(this);
        Log.d(TAG, "product id is " + mProductID);
        // fetch product information and observe for data change
        mViewModel.fetchProduct(mCookie, mProductID);
        mViewModel.getProduct().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(@Nullable Product product) {
                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    Glide.with(ProductDetailFragment.this)
                            .load(NetworkRequestUtil.PRODUCT_IMAGE_URL + product.getImageUrl())
                            .placeholder(R.drawable.default_image_1)
                            .into(imageProduct);
                }
                txtName.setText(product.getName());
                txtDescription.setText(product.getDescription());
                txtSellingPrice.setText(String.valueOf(product.getSellingPrice()));
                txtRegularPrice.setText(String.valueOf(product.getRegularPrice()));
            }
        });

        mViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(@Nullable List<CartItem> cartItems) {
                UserSession session = UserSession.getInstance(getContext());
                session.setCartQuantity(cartItems.size());
                mActivityCallback.updateCartBadge();
                showSuccessResponse(getString(R.string.added_to_cart));
            }
        });

        mViewModel.getWishList().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                showSuccessResponse(getString(R.string.added_to_wishlist));
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(mProductName);
        mActivityCallback.updateCartBadge();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_to_cart_button: {
                mViewModel.addToCart(mCookie, mCustomerId, mProductID);
                break;
            }
            case R.id.add_to_wishlist_button: {
                mViewModel.addToWishlist(mCookie, mCustomerId, mProductID);
                break;
            }
        }
    }

    private void showSuccessResponse(String title) {
        Snackbar.make(mView, title, Snackbar.LENGTH_SHORT).show();
    }
}
