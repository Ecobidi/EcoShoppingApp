package com.example.obidi.eco_shopping_mall.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.obidi.eco_shopping_mall.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSliderFragment extends Fragment {
    public static final String TAG = ImageSliderFragment.class.getSimpleName();
    private static final String ARG_IMAGE_RESOURCES = "image resources";

    private boolean mPauseSlide;
    private int mCurrentImageSlide;

    public static ImageSliderFragment newInstance(ArrayList<String> imageResources) {
        ImageSliderFragment fragment = new ImageSliderFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_RESOURCES, imageResources);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        ArrayList<String> imageUrls = getArguments().getStringArrayList(ARG_IMAGE_RESOURCES);
        final int numberOfSlides = imageUrls.size();
        final ViewPager viewPager = view.findViewById(R.id.image_slider_view_pager);
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getContext(), imageUrls);
        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        final Handler imageSliderHandler = new Handler();

        final Runnable updateSlide = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(mCurrentImageSlide, true);
                mCurrentImageSlide = (mCurrentImageSlide + 1 == numberOfSlides) ? 0 : mCurrentImageSlide + 1;
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mPauseSlide) {
                    if (getActivity() != null) getActivity().runOnUiThread(updateSlide);
                    else imageSliderHandler.post(updateSlide);
                }
            }
        }, 6000, 6000);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPauseSlide = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPauseSlide = false;
    }

    public static class ImagePagerAdapter extends PagerAdapter {
        private List<String> mImageUrls;
        private Context mContext;

        public ImagePagerAdapter(Context context, List<String> imageUrls) {
            mContext = context;
            mImageUrls = imageUrls;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            /*ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.image_slide_item, container, false);
            ImageView imageSlide = view.findViewById(R.id.image_slide);*/
            ImageView imageSlide = new ImageView(mContext);
//            imageSlide.setImageResource(mImageUrls.get(position));
            imageSlide.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(mImageUrls.get(position)).into(imageSlide);
            container.addView(imageSlide, 0);
            return imageSlide;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view.equals(o);
        }

    }
}
