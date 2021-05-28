package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itt.bookmyslot.Activity.CheckoutActivity;
import com.itt.bookmyslot.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookingFragment extends Fragment {

    ArrayList<String> typesArray = new ArrayList<>();
    ArrayList<String> namesArray = new ArrayList<>();
    Map<String, ArrayList<String>> map = new HashMap<>();
    Map<String, Map<String,String>> map2 = new HashMap<>();
    ArrayAdapter adapter1, adapter2;
    ListView listView;
    Button submit;
    Set<String> select=new HashSet<>();

    private Context mContext;
    Spinner grndTypes, grndNames;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_booking, container, false);

        typesArray.add("Select a ground type");

        grndTypes = view.findViewById(R.id.grdTypeSpinner);
        grndNames = view.findViewById(R.id.grdNameSpinner);
        listView = view.findViewById(R.id.list_booking);
        submit = view.findViewById(R.id.submit_booking);
        grndTypes.setOnItemSelectedListener(new TypesSpinnerClass());
        grndNames.setOnItemSelectedListener(new NamesSpinnerClass());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Bookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    typesArray.add(dataSnapshot.getKey());
                    ArrayList<String> temp = new ArrayList<>();

                    for (DataSnapshot data2 : dataSnapshot.getChildren()) {

                        temp.add(data2.getKey());
                        ArrayList<String> temp2 = new ArrayList<>();
                        ArrayList<String> temp3 = new ArrayList<>();

                        for (DataSnapshot data3 : data2.getChildren()) {
                            temp2.add(data3.getKey());
                            temp3.add(data3.getValue().toString());
                        }
                        Map<String,String> tempMap=new HashMap<>();
                        for(int i=0;i<temp2.size();i++){
                            tempMap.put(temp2.get(i),temp3.get(i));
                        }
                        map2.put(data2.getKey(),tempMap);
                    }
                    map.put(dataSnapshot.getKey(), temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        adapter1 = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, typesArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grndTypes.setAdapter(adapter1);

        adapter2 = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, namesArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grndNames.setAdapter(adapter2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(select.size()==0)
                    Snackbar.make(view,"Select items",Snackbar.LENGTH_SHORT).show();
                else{
//                    System.out.println("Data is "+select);
                    Intent intent=new Intent(getActivity(), CheckoutActivity.class);
                    String[] objects = new String[select.size()];
                    select.toArray(objects);
                    final ArrayList<String> list = new ArrayList<String>(Arrays.asList(objects));
                    intent.putStringArrayListExtra("favs",list);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    class TypesSpinnerClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i) != "Select a ground type") {
                grndNames.setVisibility(View.VISIBLE);
                namesArray = map.get(typesArray.get(i));
                namesArray.add(0, "Select " + typesArray.get(i).toLowerCase() + " name");
                adapter2 = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, namesArray);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                grndNames.setAdapter(adapter2);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class NamesSpinnerClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
            if (!adapterView.getItemAtPosition(i).toString().contains("Select")) {

                submit.setVisibility(View.VISIBLE);
                final ArrayList<String> temp1=new ArrayList<>(map2.get(namesArray.get(i)).keySet());
                select=new HashSet<>();
                ArrayAdapter adapter3 = new ArrayAdapter(mContext, android.R.layout.simple_list_item_checked, temp1);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CheckedTextView v = (CheckedTextView) view;
                        boolean currentCheck = v.isChecked();
                        String selected =  listView.getItemAtPosition(i).toString();

                        if(currentCheck)
                            select.add(selected);
                        else
                            select.remove(selected);
                    }
                });
                listView.setAdapter(adapter3);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
