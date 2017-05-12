package com.dev.app.playground.Slider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dev.app.playground.JavaObject.ImagesModel;
import com.dev.app.playground.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter{
    private  Context context;
    List<ImagesModel> imagesModelList;
    private ArrayList<Integer> IMAGES = new ArrayList<>();

    public ViewPagerAdapter(Context context, List<ImagesModel> imagesModelList) {
        this.imagesModelList = imagesModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagesModelList.size();
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == ((CardView) object);

    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.imagepager_layout,null);
        ((ViewPager) collection).addView(view);
        final ImageView img = (ImageView) view.findViewById(R.id.img);

        final ImagesModel imagesModel = imagesModelList.get(position);
        Glide.with(context)
                .load(imagesModel.getImage())
                .into(img);
        return view;
    }
}
