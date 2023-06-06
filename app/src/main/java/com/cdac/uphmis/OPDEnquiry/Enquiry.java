package com.cdac.uphmis.OPDEnquiry;

import static com.cdac.uphmis.util.AppUtilityFunctions.getIndex;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.OPDAppointmentActivity;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Enquiry extends AppCompatActivity {
    Spinner dSpinner;
    ListView departmentListView;
    GeometricProgressView progressView;

    RadioGroup radioGroup;
    private EditText etSearch;
    private Spinner hospSpinner;

    private MyAdapter adapter1;
    //private String hospCode="";
    //String hospName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        toolbar.setTitle("OPD Enquiry");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }

        radioGroup = findViewById(R.id.radio_group_enquiry);
        dSpinner = findViewById(R.id.spinner_department);
        departmentListView = findViewById(R.id.department_list);
        progressView = findViewById(R.id.progress_view);
        hospSpinner = findViewById(R.id.hosp_spinner);
        getHospitals();





        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // getHospitals();
                HospitalDetails hospitalDetails=(HospitalDetails) hospSpinner.getSelectedItem();

                Log.d("hosppppital",""+hospitalDetails.getHospCode());

                String hospCode=hospitalDetails.getHospCode();
                String hospName=hospitalDetails.getHospName();
                getList("0",hospCode);

                //getDepartments(hospCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DepartmentDetails departmentSelected = (DepartmentDetails) dSpinner.getSelectedItem();
                Log.i("hello", "onItemSelected: " + departmentSelected.getId());
                deptId = departmentSelected.getId();
                //getList(deptId);
//                getgeneralList(deptId);
                radioGroup.check(R.id.radio_general);
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });*/


       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.radio_general:
//                        getgeneralList(deptId);
                        getList(deptId);
                        break;
                    case R.id.radio_special:
//                        getSpecialList(deptId);
                        getList(deptId);
                        break;
                    case R.id.radio_all:
                        getList(deptId);
                        break;
                }
            }
        });*/
        etSearch = findViewById(R.id.etSearch);
        Selection.setSelection(etSearch.getText(), etSearch.getText().length());
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if (adapter1 != null)
                    adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                Selection.setSelection(etSearch.getText(), etSearch.getText().length());


            }
        });

    }
    private void getHospitals() {
        Log.d("department",ServiceUrl.getHospitalUrl);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<>();
//        hospitalDetailsArrayList.add(new HospitalDetails("-1", "Select Hospital"));
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHospitalUrl, response -> {
            Log.i("response is ", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                JSONArray hospitalList = new JSONArray(response);
                for (int i = 0; i < hospitalList.length(); i++) {
                    JSONObject c = hospitalList.getJSONObject(i);

                    String hospCode = c.getString("hospCode");
                    String hospName = c.getString("hospName");

                    hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName));


                }
                hospSpinner.setAdapter(new ArrayAdapter(Enquiry.this, R.layout.for_layout, hospitalDetailsArrayList));
                //hospSpinner.setSelection(getIndex(hospSpinner, AppConstants.BOKARO_GENERAL_HOSPITAL));



            } catch (final JSONException e) {
                Log.i("jsonexception", "onResponse: " + e);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
/*
    public void getDepartments(String hospCode) {
        Log.d("department",ServiceUrl.urldepartment+hospCode);

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urldepartment+hospCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList hospitalList = new ArrayList<String>();
                Log.i("response is ", "onResponse: " + response);

                JSONArray jsonObj;
                try {
                    jsonObj = new JSONArray(response);

                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        String id = c.getString("DEPT_ID");
                        String name = c.getString("DEPT_NAME");
                        DepartmentDetails departmentDetails = new DepartmentDetails(id, name);
                        hospitalList.add(departmentDetails);
                        //hospitalList.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Enquiry.this != null) {
                    ArrayAdapter adapter = new ArrayAdapter(Enquiry.this, android.R.layout.simple_spinner_dropdown_item, hospitalList);

                    //adapter.notifyDataSetChanged();
                    dSpinner.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, Enquiry.this);
                Log.i("error", "onErrorResponse: " + error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }

*/
    public void getList(String deptId,String hospCode) {
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<DepartmentValues> mProductArrayList = new ArrayList<DepartmentValues>();
        final ArrayList<DepartmentValues> departmentList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urlenquiry + "?deptCode=" + deptId + "&hospCode=" +hospCode , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  progressBar.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                // String jsonStr = response;
                try {
                    // Getting JSON Array node
                    // String response= CheckJsonString.response;
                    JSONArray deptList = new JSONArray(response);

                    for (int i = 0; i < deptList.length(); i++) {
                        JSONObject c = deptList.getJSONObject(i);
                        String deptName = c.optString("deptName");
                        String unitName = c.optString("unitName");
                        String location = c.optString("location");
                        String roomName = c.optString("roomName");
                        String unitDays = c.optString("unitDays");
                        unitDays = unitDays.replace(")", ")\n");
                        String rosterType = c.optString("rosterType");

                        mProductArrayList.add(new DepartmentValues(deptName, unitName, location, roomName, unitDays, rosterType));
                    }
                    adapter1 = new MyAdapter(Enquiry.this, mProductArrayList);
                    departmentListView.setAdapter(adapter1);

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                //   Snackbar.make(getView(), "Unable to connect server please try again later.", Snackbar.LENGTH_SHORT).show();
                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }



    public class MyAdapter extends BaseAdapter implements Filterable {

        private ArrayList<DepartmentValues> mOriginalValues; // Original Values
        private ArrayList<DepartmentValues> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<DepartmentValues> mProductArrayList) {
            this.mOriginalValues = mProductArrayList;
            this.mDisplayedValues = mProductArrayList;
            if (Enquiry.this != null) {
                inflater = LayoutInflater.from(context);
//                Toast.makeText(getActivity(), mDisplayedValues.size() + " Records found.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            RelativeLayout llContainer;
            TextView tvdeptName, tvunitName, tvroomName,tvLocation, tvunitDays, tvrosterType;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.departmentvalues, null);
                holder.llContainer = convertView.findViewById(R.id.llContainer);


                holder.tvdeptName = convertView.findViewById(R.id.tv_dept_name);
                holder.tvunitName = convertView.findViewById(R.id.tv_unit_name);

                holder.tvroomName = convertView.findViewById(R.id.tv_room_name);
                holder.tvLocation = convertView.findViewById(R.id.tv_location);
                holder.tvunitDays = convertView.findViewById(R.id.tv_unit_days);

                holder.tvrosterType = convertView.findViewById(R.id.tv_roster_type);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // holder.tvopdName.setText(mDisplayedValues.get(position).getOpdName());
            holder.tvdeptName.setText( mDisplayedValues.get(position).getDeptName());
            holder.tvunitName.setText("Unit: " + mDisplayedValues.get(position).getUnitName());
            holder.tvunitDays.setText(mDisplayedValues.get(position).getUnitDays());
            holder.tvroomName.setText("Room:  " + mDisplayedValues.get(position).getRoomName());
            holder.tvLocation.setText("Location: " + mDisplayedValues.get(position).getLocation());
            holder.tvrosterType.setText(mDisplayedValues.get(position).getRosterType());
            // holder.tvshiftTime.setText("Shift Time: " + mDisplayedValues.get(position).getShiftTime());


            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    //     Toast.makeText(getActivity(), mDisplayedValues.get(position).getHeadOfUnit(), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mDisplayedValues = (ArrayList<DepartmentValues>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
//                    Toast.makeText(getActivity(), mDisplayedValues.size() + " Records found.", Toast.LENGTH_SHORT).show();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<DepartmentValues> FilteredArrList = new ArrayList<DepartmentValues>();


                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<DepartmentValues>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;

                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getUnitName();
                            String data2 = mOriginalValues.get(i).getDeptName();
                            if (data.toLowerCase().contains(constraint.toString()) || data2.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(new DepartmentValues(mOriginalValues.get(i).getDeptName(), mOriginalValues.get(i).getUnitName(), mOriginalValues.get(i).getLocation(), mOriginalValues.get(i).getRoomName(), mOriginalValues.get(i).getUnitDays(), mOriginalValues.get(i).getRosterType()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;

                    }

                    return results;
                }
            };
            return filter;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}

