package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itt.bookmyslot.R;

public class PrevBookingFragment extends Fragment {

    private Context mContext;
    String res;
    View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prev_booking, container, false);

        final EditText et1 = view.findViewById(R.id.regNo);
        final EditText et2 = view.findViewById(R.id.pass);
        final TextView txtView = view.findViewById(R.id.booking_text);
        Button btn = view.findViewById(R.id.submit_prev);
        res = "Your previous bookings were on:\n";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                res = "Your previous bookings were on:\n";
                final String reg = et1.getText().toString();
                final String pass = et2.getText().toString();

                if (reg.isEmpty() || pass.isEmpty()) {
                    Snackbar.make(view, "Enter valid credentials", Snackbar.LENGTH_SHORT).show();
                } else if (reg.length() != 9) {
                    et1.setError("Enter valid registration number");
                } else if (pass.length() < 6) {
                    et2.setError("Password too short");
                } else {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("Users").addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            final boolean[] found = {false};
                            for (DataSnapshot data2 : snapshot.getChildren()) {
                                if (reg.equals(data2.getKey())) {

                                    found[0] = true;
                                    String actualPass = data2.child(data2.getKey()).getValue().toString();

                                    if (!actualPass.equals(pass)) {
                                        Snackbar.make(view, "Password incorrect", Snackbar.LENGTH_SHORT).show();
                                    } else {

                                        final boolean[] count = {false};

                                        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("prev_bookings");
                                        db2.child(reg).addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot data : snapshot.getChildren()) {
                                                    for (DataSnapshot data2 : data.getChildren()) {
                                                        String abcd = data2.getValue().toString();
                                                        String[] resArr = abcd.split(":");
                                                        res = res.concat(resArr[0] + "   " + resArr[1] + ":" + resArr[2] + "\n");
                                                        count[0] = true;
                                                    }
                                                    txtView.setText(res);
                                                }
                                                if (!count[0]) {
                                                    txtView.setText("No bookings found\n");
                                                    found[0] = false;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                if (found[0])
                                    break;
                            }
                            if (!found[0]) {
                                txtView.setText("No bookings found\n");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        return view;
    }
}
