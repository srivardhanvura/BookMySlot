package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.itt.bookmyslot.R;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.mit2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.mit1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.mit3, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = view.findViewById(R.id.slider);
        imageSlider.setImageList(imageList);

        return view;
    }
}
