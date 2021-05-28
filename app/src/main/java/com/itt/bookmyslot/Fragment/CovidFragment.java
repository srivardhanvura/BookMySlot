package com.itt.bookmyslot.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.itt.bookmyslot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CovidFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    LineChart casesChart;
    RequestQueue requestQueue;
    Spinner spinner;
    Map<String, Integer> rec, confirm, tested;
    ProgressBar progressBar;

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_covid, container, false);

        spinner = view.findViewById(R.id.graph_spin);
        casesChart = view.findViewById(R.id.cases_chart);
        progressBar=view.findViewById(R.id.progress);
        requestQueue = Volley.newRequestQueue(mContext);
        rec = new HashMap<>();
        tested = new HashMap<>();
        confirm = new HashMap<>();

        jsonParse();

        ArrayList<String> spinList = new ArrayList<>();
        spinList.add("Cases");
        spinList.add("Tested");
        spinList.add("Recovered");
        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, spinList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    void plotGraph(int type) {

        casesChart.setBackgroundColor(Color.WHITE);
        casesChart.setTouchEnabled(true);
        casesChart.setDrawGridBackground(false);
        casesChart.animateY(500);
        casesChart.animateX(2000);

        LineDataSet lineDataSet1;

        ArrayList<Entry> yValues = new ArrayList<>();

        int i = 0;

        if (type == 1) {
            for (Map.Entry<String, Integer> entry : confirm.entrySet()) {
                if (i == 0 || i == 8) {
                    i++;
                    continue;
                }
                System.out.println("Data is " + i + " " + entry.getValue());
                yValues.add(new Entry(i, entry.getValue()));
                i++;
            }
            lineDataSet1 = new LineDataSet(yValues, "Confirmed");
            lineDataSet1.setColor(Color.RED);
        } else if (type == 2) {
            for (Map.Entry<String, Integer> entry : tested.entrySet()) {
                if (i == 0 || i == 8) {
                    i++;
                    continue;
                }
                System.out.println("Data is " + i + " " + entry.getValue());
                yValues.add(new Entry(i, entry.getValue()));
                i++;
            }
            lineDataSet1 = new LineDataSet(yValues, "Tested");
            lineDataSet1.setColor(Color.BLUE);
        } else {
            for (Map.Entry<String, Integer> entry : rec.entrySet()) {
                if (i == 0 || i == 8) {
                    i++;
                    continue;
                }
                System.out.println("Data is " + i + " " + entry.getValue());
                yValues.add(new Entry(i, entry.getValue()));
                i++;
            }
            lineDataSet1 = new LineDataSet(yValues, "Recovered");
            lineDataSet1.setColor(Color.GREEN);
        }

        lineDataSet1.setFillAlpha(110);

        ArrayList<ILineDataSet> arrayList = new ArrayList<>();
        arrayList.add(lineDataSet1);

        lineDataSet1.setLineWidth(3f);
        lineDataSet1.setCircleColor(Color.BLACK);
        lineDataSet1.setCircleRadius(6f);
        lineDataSet1.setValueTextSize(10f);

        LineData data = new LineData(arrayList);
        casesChart.setData(data);

        casesChart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    void jsonParse() {

        String url = "https://api.covid19india.org/v4/min/timeseries.min.json";
        final ArrayList<String> list = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject kar = response.getJSONObject("KA");
                            JSONObject dates = kar.getJSONObject("dates");
                            Iterator<String> keys = dates.keys();
                            for (; keys.hasNext(); ) {
                                list.add(keys.next());
                            }
                            Collections.sort(list);
                            Collections.reverse(list);
                            for (int i = 0; i < 9; i++) {
                                JSONObject date = dates.getJSONObject(list.get(i));
                                JSONObject total = date.getJSONObject("total");
                                rec.put(list.get(i), Integer.parseInt(total.getString("recovered")));
                                confirm.put(list.get(i), Integer.parseInt(total.getString("confirmed")));
                                tested.put(list.get(i), Integer.parseInt(total.getString("tested")));
                            }

                            int a = 0, b = 0;
                            for (Map.Entry<String, Integer> entry : rec.entrySet()) {
                                a = entry.getValue();
                                entry.setValue(entry.getValue() - b);
                                b = a;
                            }
                            a = 0;
                            b = 0;
                            for (Map.Entry<String, Integer> entry : confirm.entrySet()) {
                                a = entry.getValue();
                                entry.setValue(entry.getValue() - b);
                                b = a;
                            }
                            a = 0;
                            b = 0;
                            for (Map.Entry<String, Integer> entry : tested.entrySet()) {
                                a = entry.getValue();
                                entry.setValue(entry.getValue() - b);
                                b = a;
                            }
                            plotGraph(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Data is " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getItemAtPosition(i) == "Cases") {
            plotGraph(1);
        } else if (adapterView.getItemAtPosition(i) == "Tested") {
            plotGraph(2);
        } else {
            plotGraph(3);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
