package com.example.week_recipe.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.example.week_recipe.R;
import com.example.week_recipe.view.adapter.MyCalendarAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PopupCalendarFragment extends Fragment implements MyCalendarAdapter.OnItemClickListener {
    private View view;
    //private CalendarView calendarView;
    private MyCalendarFragment myCalendarFragment;
    private boolean selectDay;
    private MutableLiveData<LocalDate> date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_popup_calendar, container, false);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    public void bind(boolean selectDay, LocalDate date)
    {
        this.date = new MutableLiveData<>();
        this.selectDay = selectDay;
        myCalendarFragment = FragmentManager.findFragment(view.findViewById(R.id.fragment_recipeWithDate_myCalendarFragment));
        if (this.selectDay)
        {
            myCalendarFragment.bind(date,this);
        }
        else
        {
            date = date.minusDays(date.getDayOfWeek().getValue()-1);
            ArrayList<LocalDate> week = new ArrayList<>();
            for (int x=0;x<7;x++)
            {
                week.add(date.plusDays(x));
            }
            myCalendarFragment.bind(week,this);
        }
        setDate(date);
        setListener();
    }

    private void setListener()
    {
        date.observe(this, new Observer<LocalDate>() {
            @Override
            public void onChanged(LocalDate localDate) {
                if (selectDay)
                {
                    myCalendarFragment.updateSelectDateList(localDate);
                }
                else
                {
                    ArrayList<LocalDate> week = new ArrayList<>();
                    for (int x=0;x<7;x++)
                    {
                        week.add(localDate.plusDays(x));
                    }
                    myCalendarFragment.updateSelectDateList(week);
                }
            }
        });
    }

    public void setDate(LocalDate date)
    {
        if (!selectDay)
        {
            date = date.minusDays(date.getDayOfWeek().getValue()-1);
        }

        if (myCalendarFragment!=null&&!date.equals(this.date.getValue()))
        {
            this.date.setValue(date);
        }
        else if (myCalendarFragment!=null&&date.equals(this.date.getValue()))
        {
            myCalendarFragment.updateFirstDayOfMonth(date.minusDays(date.getDayOfMonth()-1));
        }
    }

    public LiveData<LocalDate> getDate() {
        return date;
    }

    public void showCalendar(double second)
    {
        this.myCalendarFragment.updateFirstDayOfMonth(date.getValue().minusDays(date.getValue().getDayOfMonth()-1));
        View myCalendarFragmentView = view.findViewById(R.id.fragment_recipeWithDate_myCalendarFragment);
        if (myCalendarFragmentView!=null)
        {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            translateAnimation.setDuration((long) (second * 1000));

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);

            myCalendarFragmentView.setAnimation(animationSet);
        }
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void OnItemClick(LocalDate date) {
        setDate(date);
    }
}