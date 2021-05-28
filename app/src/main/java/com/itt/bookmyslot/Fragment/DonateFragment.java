package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.itt.bookmyslot.Activity.RazorpayActivity;
import com.itt.bookmyslot.R;
import com.razorpay.Checkout;

public class DonateFragment extends Fragment {

    private Context mContext;
    Button donate_btn;
    EditText amt, regEt;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    String reg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        donate_btn = view.findViewById(R.id.donate_btn);
        amt = view.findViewById(R.id.razor_amt);
        regEt = view.findViewById(R.id.razor_reg);

        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String money = amt.getText().toString();
                reg = regEt.getText().toString();
                if (money.isEmpty())
                    Snackbar.make(view, "Enter a valid amount", Snackbar.LENGTH_SHORT).show();

                else if (reg.length() != 9)
                    Snackbar.make(view, "Enter correct registration number", Snackbar.LENGTH_SHORT).show();

                else {
                    Intent intent = new Intent(mContext, RazorpayActivity.class);
                    intent.putExtra("amount", money);
                    intent.putExtra("reg", reg);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
