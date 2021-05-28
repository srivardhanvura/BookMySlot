package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.itt.bookmyslot.Adapter.ImageAdapter;
import com.itt.bookmyslot.R;

public class HomeFragment extends Fragment {

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);

        ViewPager viewPager=view.findViewById(R.id.view_pager);
        ImageAdapter adapter=new ImageAdapter(mContext);
        viewPager.setAdapter(adapter);

        return view;
    }
}
