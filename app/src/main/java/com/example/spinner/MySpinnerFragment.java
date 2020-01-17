package com.example.spinner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;


public class MySpinnerFragment extends Fragment {

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();

    //Spinner Api
    Spinner spClass, spSection;
    ArrayList<String> alClass = new ArrayList<>();
    ArrayList<Pojo> detailClass = new ArrayList<>();
    ArrayAdapter adapterClass;

    //Spinner Api
    ArrayList<String> alSection = new ArrayList<>();
    ArrayList<PojoSection> detailSection = new ArrayList<>();
    ArrayAdapter adapterSection;

    Button btnSearch, btnSearchByKey;
    EditText etSearchByKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_spinner, container, false);
        spClass = view.findViewById(R.id.spn_class);
        spSection = view.findViewById(R.id.spn_section);
        btnSearch = view.findViewById(R.id.btn_criteria_search);
        //Spinner Api
        GetSpinnerApi();
        alClass.add(0, "Select Ur Categories");
        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //city_id=detailClass.get(i).getCity_id();
                if (i > 0) {
                    GetSpinnerSectionApi();
                } else {
                    spSection.setAdapter(null);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        alSection.add(0, "Select Ur Categories");
        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }


    private void GetSpinnerSectionApi() {
        String url = Constants.staffSectionListUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            if (!alSection.contains(jsonArray.getJSONObject(i).getString("section"))) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                final String Id = data.getString("id");
                                final String SectionName = data.getString("section");
                                PojoSection pojo = new PojoSection(Id, SectionName);
                                HashSet<String> hashSet = new HashSet<String>();
                                hashSet.addAll(alSection);
                                alSection.clear();
                                alSection.addAll(hashSet);
                                detailSection.add(pojo);
                                alSection.add(i + 1, SectionName);
                            }


                        }
                        if (getActivity() != null) {
                            adapterSection = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, alSection);
                            spSection.setAdapter(adapterSection);
                        }
                    } else {

                        Toast.makeText(getContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                Log.e("params", "" + params);

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin");
                headers.put("Content-Type", "application/json");
                headers.put("User-ID", "1");
                headers.put("Authorization-token", "OQNgMgOANw");
                headers.put("Role", "staff");
                Log.e("Headers", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void GetSpinnerApi() {

        String url = Constants.staffClassListUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            final String Id = data.getString("id");
                            final String ClassName = data.getString("class");
                            Pojo pojo = new Pojo(Id, ClassName);
                            detailClass.add(pojo);
                            alClass.add(i + 1, ClassName);
                        }
                        if (getActivity() != null) {
                            adapterClass = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, alClass);
                            spClass.setAdapter(adapterClass);
                        }
                    } else {

                        Toast.makeText(getContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                Log.e("params", "" + params);

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin");
                headers.put("Content-Type", "application/json");
                headers.put("User-ID", "1");
                headers.put("Authorization-token", "OQNgMgOANw");
                headers.put("Role", "staff");
                Log.e("Headers", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private static class Pojo {


        String id;
        String className;

        public Pojo(String id, String className) {
            this.id = id;
            this.className = className;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    private static class PojoSection {


        String id;
        String className;

        public PojoSection(String id, String className) {
            this.id = id;
            this.className = className;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }


}