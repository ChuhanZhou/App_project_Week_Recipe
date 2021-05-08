package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.week_recipe.R;
import com.example.week_recipe.dao.converter.LocalDateConverter;
import com.example.week_recipe.view.adapter.MyCalendarAdapter;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyCalendarFragment extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private RecyclerView myCalendar;
    private MyCalendarAdapter adapter;
    private LocalDate firstDayOfMonth;
    private ArrayList<LocalDate> selectDateList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_my_calendar, container, false);
    }

    public void bind(LocalDate selectDate,MyCalendarAdapter.OnItemClickListener listener)
    {
        selectDateList = new ArrayList<>();
        selectDateList.add(selectDate);
        firstDayOfMonth = selectDate.minusDays(selectDate.getDayOfMonth()-1);
        bind(listener);
    }

    public void bind(ArrayList<LocalDate> selectDateList,MyCalendarAdapter.OnItemClickListener listener)
    {
        this.selectDateList = new ArrayList<>(selectDateList);
        firstDayOfMonth = selectDateList.get(selectDateList.size()/2).minusDays(selectDateList.get(selectDateList.size()/2).getDayOfMonth()-1);
        bind(listener);
    }

    private void bind(MyCalendarAdapter.OnItemClickListener listener)
    {
        myCalendar = view.findViewById(R.id.fragment_myCalendar_calendar);
        tabLayout = view.findViewById(R.id.fragment_myCalendar_tabLayout);
        updateValueOfTabLayout();

        adapter = new MyCalendarAdapter(firstDayOfMonth,selectDateList,listener);
        myCalendar.hasFixedSize();
        myCalendar.setLayoutManager(new GridLayoutManager(view.getContext(),7));
        myCalendar.setAdapter(adapter);
        setListener();
    }

    private void setListener()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition())
                {
                    case 0:
                        updateFirstDayOfMonth(firstDayOfMonth.minusMonths(1));
                        break;
                    case 1:

                        break;
                    case 2:
                        updateFirstDayOfMonth(firstDayOfMonth.plusMonths(1));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateValueOfTabLayout()
    {
        if (tabLayout!=null&&firstDayOfMonth!=null)
        {
            tabLayout.getTabAt(1).setText(firstDayOfMonth.getYear()+"-"+ String.format("%2d", firstDayOfMonth.getMonthValue()).replace(" ", "0"));
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
    }

    public void updateFirstDayOfMonth(LocalDate firstDayOfMonth)
    {
        this.firstDayOfMonth = firstDayOfMonth;
        updateValueOfTabLayout();
        adapter.updateFirstDayOfMonth(firstDayOfMonth);
    }

    public void updateSelectDateList(LocalDate selectDate)
    {
        updateFirstDayOfMonth(selectDate.minusDays(selectDate.getDayOfMonth()-1));
        selectDateList = new ArrayList<>();
        selectDateList.add(selectDate);
        adapter.updateSelectDateList(selectDateList);
    }

    public void updateSelectDateList(ArrayList<LocalDate> selectDateList)
    {
        updateFirstDayOfMonth(selectDateList.get(selectDateList.size()/2).minusDays(selectDateList.get(selectDateList.size()/2).getDayOfMonth()-1));
        this.selectDateList = new ArrayList<>(selectDateList);
        adapter.updateSelectDateList(selectDateList);
    }
}