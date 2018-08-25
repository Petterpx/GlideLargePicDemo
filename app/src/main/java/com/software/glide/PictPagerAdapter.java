package com.software.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Copyright (C)
 *
 * @author : gongcb
 * @date : 2018/8/25 上午7:11
 * @desc :
 */
public class PictPagerAdapter extends PagerAdapter{

    private RequestManager mRequestManager;
    private List<Integer> mResId;
    private LayoutInflater mInflater;

    private static final int MAX_SIZE = 4096;
    private static final int MAX_SCALE = 8;


    public PictPagerAdapter(Context context, List<Integer> resId) {
        mInflater = LayoutInflater.from(context);
        mRequestManager = Glide.with(context);
        mResId = resId;
    }

    @Override
    public int getCount() {
        return mResId.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final Context cxt = container.getContext();

        View view = mInflater.inflate(R.layout.pict_pager_item_view,null);
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        final SubsamplingScaleImageView scaleImageView = (SubsamplingScaleImageView) view.findViewById(R.id.sub_imageview);

        photoView.setVisibility(View.VISIBLE);
        scaleImageView.setVisibility(View.GONE);

        scaleImageView.setMaxScale(10.0F);

        if(AndroidLifecycleUtils.canLoadImage(cxt)) {
            mRequestManager.load(mResId.get(position))
                    .asBitmap()
                    .dontAnimate()
                    .dontTransform()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int h = resource.getHeight();
                            int w = resource.getWidth();

                            if(h >= MAX_SIZE || h/w > MAX_SCALE) {

                                photoView.setVisibility(View.GONE);
                                scaleImageView.setVisibility(View.VISIBLE);

                                mRequestManager.load(mResId.get(position))
                                        .downloadOnly(new SimpleTarget<File>() {
                                            @Override
                                            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                                float scale = Utils.getImageScale(cxt,resource.getAbsolutePath());
                                                scaleImageView.setImage(ImageSource.uri(resource.getAbsolutePath()),
                                                        new ImageViewState(scale, new PointF(0, 0), 0));
                                            }
                                        });

                            } else {
                                photoView.setImageBitmap(resource);
                            }
                        }
                    });
        }


        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener()

        {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish(cxt);
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });


        scaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(cxt);
            }
        });

        container.addView(view);

        return view;
    }

    private void finish(Context context) {
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                ((Activity) context).onBackPressed();
            }
        }
    }


}
