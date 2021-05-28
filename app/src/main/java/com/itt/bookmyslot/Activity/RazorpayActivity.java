package com.itt.bookmyslot.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.Date;
import com.itt.bookmyslot.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    String reg, amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        amt = getIntent().getStringExtra("amount");
        reg = getIntent().getStringExtra("reg");
        Checkout.preload(this);
        makePayment(amt + "00");
    }

    void makePayment(String amt) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_m1rkd8I14Rh01e");
        checkout.setImage(R.drawable.full_logo1);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "BookMySlot");
            options.put("description", "Payment for charity");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", R.color.colorAccent);
            options.put("currency", "INR");
            options.put("amount", amt);//pass amount in currency subunits
            options.put("prefill.email", "itt.project.b5@gmail.com");
            options.put("prefill.contact", "9381263595");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            System.out.println("Data is Error in starting Razorpay Checkout " + e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        System.out.println("Data is success");

        final Calendar calActual = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(calActual.getTimeInMillis());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("prev_donations").child(reg).push().child("Amount").setValue(amt+":"+dateString);
        Toast.makeText(RazorpayActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RazorpayActivity.this, HomeActivity.class));
    }

    @Override
    public void onPaymentError(int i, String s) {
        System.out.println("Data is failed");
        Toast.makeText(RazorpayActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RazorpayActivity.this, HomeActivity.class));
    }
}