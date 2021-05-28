package com.itt.bookmyslot.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itt.bookmyslot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    ListView listView;
    Button submit;
    EditText et1, et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        final ArrayList<String> selected = getIntent().getStringArrayListExtra("favs");

        listView = findViewById(R.id.list_checkout);
        et1 = findViewById(R.id.checkout_regNo);
        et2 = findViewById(R.id.checkout_pass);
        submit = findViewById(R.id.checkout_submit);

        getSupportActionBar().setTitle("Enter details");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selected);
        listView.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String reg = et1.getText().toString();
                final String pass = et2.getText().toString();

                if (reg.isEmpty() || pass.isEmpty()) {
                    Snackbar.make(view, "Enter valid credentials", Snackbar.LENGTH_SHORT).show();
                } else if (reg.length() != 9) {
                    et1.setError("Enter valid registration number");
                } else if (pass.length() < 6) {
                    et2.setError("Password too short");
                } else {

                    final boolean[] found = {false};
                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot data2 : snapshot.getChildren()) {
                                if (reg.equals(data2.getKey())) {
                                    found[0] = true;
                                    String actualPass = data2.child(data2.getKey()).getValue().toString();

                                    if (!actualPass.equals(pass)) {
                                        Snackbar.make((RelativeLayout) findViewById(R.id.checkout_root), "Password incorrect", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(CheckoutActivity.this, ValidateActivity.class);
                                        intent.putStringArrayListExtra("selected", selected);
                                        intent.putExtra("regno", reg);
                                        intent.putExtra("pass", pass);
                                        startActivity(intent);
                                    }
                                }
                                if (found[0])
                                    break;
                            }
                            if (!found[0]) {
                                db.child("Users").child(reg).child(reg).setValue(pass);
                                Toast.makeText(CheckoutActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CheckoutActivity.this, ValidateActivity.class);
                                intent.putStringArrayListExtra("selected", selected);
                                intent.putExtra("regno", reg);
                                intent.putExtra("pass", pass);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}