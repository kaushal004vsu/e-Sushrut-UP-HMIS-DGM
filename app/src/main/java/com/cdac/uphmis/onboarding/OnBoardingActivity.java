package com.cdac.uphmis.onboarding;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.cdac.uphmis.LoginMainScreenActivity;
import com.cdac.uphmis.R;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {



    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;



    private ViewPager onboard_pager;

    private OnBoard_Adapter mAdapter;

    private Button btn_get_started;

    int previous_pos=0;


    ArrayList<OnBoardItem> onBoardItems=new ArrayList<>();

    ArrayList<String> fetaureIdArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        
        Intent intent=getIntent();
        if (intent!=null) {
             fetaureIdArrayList = (ArrayList<String>) intent.getSerializableExtra("fetaureIdArrayList");
        }


        btn_get_started =  findViewById(R.id.btn_get_started);
        onboard_pager =  findViewById(R.id.pager_introduction);
        pager_indicator = findViewById(R.id.viewPagerCountDots);

        loadData();

        mAdapter = new OnBoard_Adapter(OnBoardingActivity.this,onBoardItems);
        onboard_pager.setAdapter(mAdapter);
        onboard_pager.setCurrentItem(0);
        onboard_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // Change the current position intimation

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));


                int pos=position+1;

                if(pos==dotsCount&&previous_pos==(dotsCount-1))
                     show_animation();
                else if(pos==(dotsCount-1)&&previous_pos==dotsCount)
                     hide_animation();

                previous_pos=pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_get_started.setOnClickListener(v -> {
            startActivity(new Intent(OnBoardingActivity.this, LoginMainScreenActivity.class));
            finish();
        });

        setUiPageViewController();

    }

    // Load data into the viewpager

    public void loadData()
    {

        int[] header = {R.string.ob_header1, R.string.ob_header2, R.string.ob_header3, R.string.ob_header4,R.string.ob_header5};
        int[] desc = {R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3,R.string.ob_desc4,R.string.ob_desc5};
        int[] imageId = {R.drawable.ic_group_1543, R.drawable.ic_group_1544, R.drawable.ic_group_1545,R.drawable.ic_group_1546,R.drawable.ic_group_1547};

        for(int i=0;i<imageId.length;i++)
        {
            //teleconsultation
            if (i==0 && fetaureIdArrayList.contains("EC"))
            {
                continue;
            }

            //Book Appointment
            if (i==1 && fetaureIdArrayList.contains("AP"))
            {
                continue;
            }

            //Lab Reports
            if (i==2 && fetaureIdArrayList.contains("IV"))
            {
                continue;
            }

            //Enquiry
            if (i==3 && fetaureIdArrayList.contains("OE"))
            {
                continue;
            }



            //Registration
            if (i==4 && fetaureIdArrayList.contains("CR"))
            {
                continue;
            }




            OnBoardItem item=new OnBoardItem();
            item.setImageID(imageId[i]);
            item.setTitle(getResources().getString(header[i]));
            item.setDescription(getResources().getString(desc[i]));

            onBoardItems.add(item);
        }
    }

    // Button bottomUp animation

    public void show_animation()
    {
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);

        btn_get_started.startAnimation(show);

        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                btn_get_started.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_get_started.setVisibility(View.VISIBLE);
                btn_get_started.clearAnimation();

            }

        });


    }

    // Button Topdown animation

    public void hide_animation()
    {
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        btn_get_started.startAnimation(hide);

        hide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_get_started.clearAnimation();
                btn_get_started.setVisibility(View.GONE);

            }

        });


    }

    // setup the
    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(6, 0, 6, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));
    }


}
