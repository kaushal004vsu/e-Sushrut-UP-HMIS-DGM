package com.cdac.uphmis.phr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.phr.model.StatsDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends AppCompatActivity {
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int numberOfPoints = 12;
    float[][] randomNumbersTab;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor = true;
    private List<StatsDetails> statList;
    private String vitalId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                getSupportActionBar().setTitle("" + intent.getStringExtra("name") + " Stats");
                vitalId = intent.getStringExtra("vital_id");
            }
            chart = (LineChartView) findViewById(R.id.chart);
            ManagingSharedData msd = new ManagingSharedData(this);
            getLineChartStats(msd.getPatientDetails().getCrno());
            resetViewport();
        }
    }
    private void generateValues(List<StatsDetails> statList) {
            randomNumbersTab = new float[1][statList.size()];
            for (int i = 0; i < 1; ++i) {
                for (int j = 0; j < statList.size(); ++j) {
                    randomNumbersTab[i][j] = (float) Float.parseFloat(statList.get(j).getVITAL_VALUE());
            }
        }
    }
    private void dataNotFoundDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LineChartActivity.this);
        builder.setTitle("");
        builder.setCancelable(false);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);
        TextView success_text = customLayout.findViewById(R.id.success_text);
        success_text.setText(msg);
        builder.setView(customLayout);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void resetViewport() {
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }
    private void generateData() {
        ArrayList<AxisValue> axisValues = new ArrayList<>();
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < statList.size(); ++j) {
                AxisValue valuee = new AxisValue(j);
              String date=  statList.get(j).getRECORD_DATE().trim();
                try {

                    valuee.setLabel(AppUtilityFunctions.changeDateFormat(date, "yyyy-MM-dd hh:mm:ss.S", "dd-MMM-yyyy"));
                } catch (Exception ex) {
                    valuee.setLabel(statList.get(j).getRECORD_DATE().trim());
                    ex.printStackTrace();
                }
                axisValues.add(valuee);
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }
            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(true);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            //    line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }
        data = new LineChartData(lines);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisX.setHasTiltedLabels(true);
            axisX.setValues(axisValues);
            if (hasAxesNames) {
                axisX.setName("Date");
                axisY.setName("Value");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
    //    data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    private void toggleCubic() {
        isCubic = !isCubic;
        generateData();
        if (isCubic) {
            final Viewport v = new Viewport(chart.getMaximumViewport());
//            v.bottom = -5;
//            v.top = 105;
//            chart.setMaximumViewport(v);
            chart.setCurrentViewportWithAnimation(v);
        } else {
            final Viewport v = new Viewport(chart.getMaximumViewport());
//            v.bottom = 0;
//            v.top = 100;
            chart.setViewportAnimationListener(new ChartAnimationListener() {
                @Override
                public void onAnimationStarted() {

                }

                @Override
                public void onAnimationFinished() {
//                    chart.setMaximumViewport(v);
                    chart.setViewportAnimationListener(null);

                }
            });
            chart.setCurrentViewportWithAnimation(v);
        }

    }
    private void getLineChartStats(String crno) {
        statList = new ArrayList<>();
        String url = ServiceUrl.graphUrl + crno;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.i("response", "onResponse: " + response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String CRNO = c.optString("CRNO");
                        String SL_NO = c.optString("SL_NO");
                        String VITAL_NAME = c.optString("VITAL_NAME");
                        String VITAL_ID = c.optString("VITAL_ID");
                        String VITAL_VALUE = c.optString("VITAL_VALUE");
                        String RECORD_DATE = c.optString("RECORD_DATE");
                        String VITAL_UNIT = c.optString("VITAL_UNIT");
                        String IS_NORMAL = c.optString("IS_NORMAL");
                        String HNUM_IS_SEVERE = c.optString("HNUM_IS_SEVERE");
                        if (VITAL_ID.equalsIgnoreCase(vitalId))
                            statList.add(new StatsDetails(CRNO, SL_NO, VITAL_NAME, VITAL_ID, VITAL_VALUE, RECORD_DATE, VITAL_UNIT, IS_NORMAL, HNUM_IS_SEVERE));
                    }
                    if (statList.size() <= 1) {
                        dataNotFoundDialog("Data not found!");
                    } else {
                        generateValues(statList);
                        generateData();
                        //toggleCubic();
                        chart.setViewportCalculationEnabled(false);
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, error -> {
            AppUtilityFunctions.handleExceptions(error, this);
            Log.e("error", "onErrorResponse: ", error);
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

