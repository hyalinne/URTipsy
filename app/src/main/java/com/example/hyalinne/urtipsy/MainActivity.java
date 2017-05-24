package com.example.hyalinne.urtipsy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        String[] permissions = new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.CALL_PHONE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED) ;
                else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class HomeFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HomeFragment newInstance(int sectionNumber) {
            HomeFragment fragment = new HomeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button btn = (Button) rootView.findViewById(R.id.toMeasureBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MeasureActivity.class);
                    startActivity(intent);
                }
            });
            return rootView;
        }
    }

    public static class SettingFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
            Button agBtn = (Button)rootView.findViewById(R.id.authorizeGuardianBtn);
            agBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AuthorizeActivity.class));
                }
            });
            return rootView;
        }

        public static SettingFragment newInstance(int sectionNumber) {
            SettingFragment fragment = new SettingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public static class ReportFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private TextView tvDate;
        private GridAdapter gridAdapter;
        private ArrayList<String> dayList;
        private GridView gridView;
        private Calendar mCal;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report, container, false);
            tvDate = (TextView) rootView.findViewById(R.id.tv_date);
            gridView = (GridView) rootView.findViewById(R.id.gridview);

            // 오늘에 날짜를 세팅 해준다.
            long now = System.currentTimeMillis();
            final Date date = new Date(now);

            //연,월,일을 따로 저장
            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

            //현재 날짜 텍스트뷰에 뿌려줌
            tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

            //gridview 요일 표시
            dayList = new ArrayList<String>();
            dayList.add("일");
            dayList.add("월");
            dayList.add("화");
            dayList.add("수");
            dayList.add("목");
            dayList.add("금");
            dayList.add("토");
            mCal = Calendar.getInstance();
            //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
            mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
            int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

            //1일 - 요일 매칭 시키기 위해 공백 add
            for (int i = 1; i < dayNum; i++) {
                dayList.add("");
            }
            setCalendarDate(mCal.get(Calendar.MONTH) + 1);
            gridAdapter = new GridAdapter(super.getContext(), dayList);
            gridView.setAdapter(gridAdapter);

            return rootView;
        }

        public static ReportFragment newInstance(int sectionNumber) {
            ReportFragment fragment = new ReportFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private void setCalendarDate(int month) {
            mCal.set(Calendar.MONTH, month - 1);
            for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                dayList.add("" + (i + 1));
            }
        }

        private class GridAdapter extends BaseAdapter implements ListAdapter {
            private final List<String> list;
            private final LayoutInflater inflater;

            public GridAdapter(Context context, List<String> list) {
                this.list = list;
                this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return list.size();
            }


            @Override
            public String getItem(int position) {
                return list.get(position);
            }


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                    holder = new ViewHolder();
                    holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.tvItemGridView.setText("" + getItem(position));
                //해당 날짜 텍스트 컬러,배경 변경
                mCal = Calendar.getInstance();
                //오늘 day 가져옴
                Integer today = mCal.get(Calendar.DAY_OF_MONTH);
                String sToday = String.valueOf(today);
                if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                    holder.tvItemGridView.setTextColor(Color.CYAN);
                }
                return convertView;
            }
        }

        private class ViewHolder {
            TextView tvItemGridView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeFragment();
            } else if (position == 1) {
                return new ReportFragment();
            } else {
                return new SettingFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
