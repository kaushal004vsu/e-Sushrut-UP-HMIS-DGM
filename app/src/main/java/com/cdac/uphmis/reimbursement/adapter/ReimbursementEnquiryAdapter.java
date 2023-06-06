package com.cdac.uphmis.reimbursement.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.reimbursement.model.ClaimEnquiryDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReimbursementEnquiryAdapter extends RecyclerView.Adapter<ReimbursementEnquiryAdapter.ExampleViewHolder> implements Filterable {
    private List<ClaimEnquiryDetails> exampleList;
    private List<ClaimEnquiryDetails> exampleListFull;
    private String crno;
    Context context;


    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvClaimNo, tvSubmissionDate, tvStatus, tvDownloadPdf, tvResubmitClaim, tvAcceptance;
        ProgressBar progressBar;
        CardView cardRootView;

        ExampleViewHolder(View itemView) {
            super(itemView);
            // imageView = itemView.findViewById(R.id.image_view);
            tvClaimNo = itemView.findViewById(R.id.tv_claim_no);
            tvSubmissionDate = itemView.findViewById(R.id.tv_submission_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDownloadPdf = itemView.findViewById(R.id.tv_download_pdf);
            tvResubmitClaim = itemView.findViewById(R.id.tv_resubmit_claim);
            tvAcceptance = itemView.findViewById(R.id.tv_acceptance);
            progressBar = itemView.findViewById(R.id.progressbar);
        }

    }

    public ReimbursementEnquiryAdapter(Context context, String crno, List<ClaimEnquiryDetails> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.crno = crno;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_enquiry_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExampleViewHolder holder, int position) {
        ClaimEnquiryDetails currentItem = exampleList.get(position);
        //holder.imageView.setImageResource(R.drawable.tests);
        holder.tvClaimNo.setText("Claim No: " + currentItem.getCLAIM_REQ_NO());
        holder.tvSubmissionDate.setText("Claim Submission Date: " + currentItem.getCLAIM_SUBMIT_DT());
        holder.tvStatus.setText("Status: " + currentItem.getSTATUS());

        if (currentItem.getSTATUS_CODE().equalsIgnoreCase("3") || currentItem.getSTATUS_CODE().equalsIgnoreCase("5")) {
            holder.tvAcceptance.setVisibility(View.VISIBLE);
            holder.tvResubmitClaim.setVisibility(View.VISIBLE);
        } else {
            holder.tvAcceptance.setVisibility(View.GONE);
            holder.tvResubmitClaim.setVisibility(View.GONE);
        }

        holder.tvDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaimEnquiryDetails currentItem = exampleList.get(position);
                downloadClaimPdf(currentItem, holder.progressBar);

            }
        });

        holder.tvAcceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClaimEnquiryDetails currentItem = exampleList.get(position);
                acceptClaim(currentItem, holder.progressBar);

            }
        });
        holder.tvResubmitClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(currentItem, holder.progressBar);


            }
        });
    }

    private void resubmitClaim(ClaimEnquiryDetails currentItem, String revAmount, String remarks, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        JSONObject requestbody = getResubmitRequestBody(currentItem, revAmount, remarks);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl + "railtelService/saveReimbursement?modeval=2", requestbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("!_@@_SUCESS", response + "");
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                AlertDialog dialog = new MaterialAlertDialogBuilder(context).setTitle("Claim Re-Submitted").setMessage("Claim Resubmitted Successfully").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity)context).recreate();
                                    }
                                }).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        progressBar.setVisibility(View.GONE);
                        AppUtilityFunctions.handleExceptions(error, context);
                    }
                });
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);


        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }


    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ClaimEnquiryDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ClaimEnquiryDetails item : exampleListFull) {
                    if (item.getCLAIM_REQ_NO().toLowerCase().contains(filterPattern) || item.getHOSP_NAME().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    private void downloadClaimPdf(ClaimEnquiryDetails claimEnquiryDetails, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.downloadCliamPdf + "modeval=3&claimReqNo=" + claimEnquiryDetails.getCLAIM_REQ_NO() + "&crno=" + crno + "&hospCode=" + claimEnquiryDetails.getTO_HOSP_CODE() + "&slNo=" + claimEnquiryDetails.getSL_NO(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String data = jsonObject.getString("BillBase64");
                        AppUtilityFunctions.saveBase64Pdf(context, "claim", "claim", data);
                    } else {
                        Toast.makeText(context, "Pdf Not Found.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtilityFunctions.handleExceptions(error, context);
                progressBar.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void acceptClaim(ClaimEnquiryDetails currentItem, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        JSONObject requestbody = getAcceptanceRequestBody(currentItem);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl + "railtelService/saveReimbursement?modeval=3", requestbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("!_@@_SUCESS", response + "");
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("1")) {
//                                AppUtilityFunctions.showMessageDialog(context, "Claim Accepted", "Claim Accepted Successfully");

                                AlertDialog dialog = new MaterialAlertDialogBuilder(context).setTitle("Claim Accepted").setMessage("Claim Accepted Successfully").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity)context).recreate();
                                    }
                                }).show();

                            }else
                            {
                                Toast.makeText(context, "Unable to accept claim.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        progressBar.setVisibility(View.GONE);
                        AppUtilityFunctions.handleExceptions(error, context);
                    }
                });
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);


        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    private JSONObject getAcceptanceRequestBody(ClaimEnquiryDetails currentItem) {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject=new JSONObject();
            jsonObject.put("crno", crno);
            jsonObject.put("claimNo", currentItem.getCLAIM_REQ_NO());
            jsonObject.put("reqSlNo", currentItem.getSL_NO());
            jsonObject.put("submitToHospCode", currentItem.getTO_HOSP_CODE());
            jsonObject.put("isClaimResubmitted", "0");
            jsonObject.put("seatId", "10001");
            jsonObject.put("lastModSeatId", "10001");
            jsonObject.put("gnum_sanctioned_amt", "0");
            jsonObject.put("gnum_forward_to_user", "0");


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getResubmitRequestBody(ClaimEnquiryDetails currentItem, String revAmount, String remarks) {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject=new JSONObject();
            jsonObject.put("crno", crno);
            jsonObject.put("claimNo", currentItem.getCLAIM_REQ_NO());
            jsonObject.put("reqSlNo", currentItem.getSL_NO());
            jsonObject.put("submitToHospCode", currentItem.getTO_HOSP_CODE());
            jsonObject.put("isClaimResubmitted", "1");
            jsonObject.put("seatId", "10001");
            jsonObject.put("lastModSeatId", "10001");
            jsonObject.put("gnum_sanctioned_amt", "0");
            jsonObject.put("gnum_forward_to_user", "0");
            jsonObject.put("rev_amt", revAmount);
            jsonObject.put("rev_remarks", remarks);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }


    private void showBottomSheetDialog(ClaimEnquiryDetails currentItem, ProgressBar progressBar) {

        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout.resubmit_claim_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        //FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
        //BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

        TextInputEditText edtRevAmount = dialog.findViewById(R.id.edt_rev_amount);
        TextInputEditText edtRemarks = dialog.findViewById(R.id.edt_remarks);


        Button btnCancel = (Button) dialog.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtRevAmount.getText().toString().isEmpty()) {
                    edtRevAmount.setError("Please enter a valid amount");
                    return;
                }
                if (edtRemarks.getText().toString().isEmpty()) {
                    edtRemarks.setError("Please enter Remarks");
                    return;
                }

                int billedAmount = Integer.parseInt(currentItem.getCLAIM_AMT()) - Integer.parseInt(currentItem.getSANCTION_AMT());

                if (billedAmount < Integer.parseInt(edtRevAmount.getText().toString())) {
                    edtRevAmount.setError("Revision amount can't be greater than billed amount.");
                    return;
                }
                resubmitClaim(currentItem, edtRevAmount.getText().toString(), edtRemarks.getText().toString(), progressBar);
                dialog.cancel();
            }
        });
        dialog.show();
    }
}