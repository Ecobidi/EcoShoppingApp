package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.MainActivityCallback;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.Utils.NetworkRequestUtil;
import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.databinding.FragmentProfileBinding;
import com.example.obidi.eco_shopping_mall.models.Customer;
import com.example.obidi.eco_shopping_mall.viewModels.ProfileFragmentViewModel;

public class ProfileFragment extends Fragment {
    public static final String TAG = ProfileFragment.class.getSimpleName();
    private MainActivityCallback mActivityCallback;
    private ProfileFragmentViewModel mViewModel;
    private FragmentProfileBinding mBinding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (MainActivityCallback) context;
        mActivityCallback.setToolbarTitle(getString(R.string.profile_title));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
        mViewModel.getCustomer().observe(this, new Observer<Customer>() {
            @Override
            public void onChanged(@Nullable Customer customer) {
                mActivityCallback.updateCartBadge();
                updateUI(customer);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
        UserSession session = UserSession.getInstance(getContext());
        mViewModel.fetchCustomer(session.getCookie(), session.getUserId());
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityCallback.setToolbarTitle(getString(R.string.profile_title));
        mActivityCallback.updateCartBadge();
    }

    private void updateUI(Customer customer) {
        mBinding.setCustomer(customer);
        mBinding.executePendingBindings();
        Glide.with(ProfileFragment.this)
                .load(NetworkRequestUtil.PROFILE_PHOTO_URL + customer.getPhoto())
                .placeholder(R.drawable.default_image_1)
                .into(mBinding.customerPhoto);
    }
}
