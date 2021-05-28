package com.itt.bookmyslot.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.itt.bookmyslot.R;

public class ImageAdapter extends PagerAdapter {

    Context context;

    public ImageAdapter(Context context1){
        context=context1;
    }

    private  int[] imgIds=new int[]{R.drawable.mit2,R.drawable.mit1,R.drawable.mit3};

    @Override
    public int getCount() {
        return imgIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((ImageView)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imgIds[position]);
        ((ViewPager) container).addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((ImageView) object);
//        super.destroyItem(container, position, object);
    }
}
