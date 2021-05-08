package com.example.week_recipe.view.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.firebase.ui.auth.data.model.Resource;

import java.security.cert.LDAPCertStoreParameters;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.ViewHolder> {

    private LocalDate firstDayOfMonth;
    private int month;
    private int firstDayOfWeek = 7;
    private ArrayList<Integer> titleList;
    private ArrayList<LocalDate> showList;
    private ArrayList<LocalDate> selectDateList;
    private ArrayList<ViewHolder> viewHolderList;
    private OnItemClickListener listener;

    public MyCalendarAdapter(LocalDate firstDayOfMonth, ArrayList<LocalDate> selectDateList, OnItemClickListener listener)
    {
        viewHolderList = new ArrayList<>();
        this.firstDayOfMonth = firstDayOfMonth;
        this.selectDateList = selectDateList;
        this.listener = listener;
        month = this.firstDayOfMonth.getMonthValue();
        showList = getShowListByFirstDay(this.firstDayOfMonth);
        titleList = getTitleIdList();
    }

    private ArrayList<Integer> getTitleIdList()
    {
        ArrayList<Integer> titleIdList = new ArrayList<>();
        ArrayList<Integer> showTitleList = new ArrayList<>();
        titleIdList.add(R.string.calendar_monday_simple);
        titleIdList.add(R.string.calendar_tuesday_simple);
        titleIdList.add(R.string.calendar_wednesday_simple);
        titleIdList.add(R.string.calendar_thursday_simple);
        titleIdList.add(R.string.calendar_friday_simple);
        titleIdList.add(R.string.calendar_saturday_simple);
        titleIdList.add(R.string.calendar_sunday_simple);
        for (int x=firstDayOfWeek-1;x<7;x++)
        {
            showTitleList.add(titleIdList.get(x));
        }
        for (int x=0;x<firstDayOfWeek-1;x++)
        {
            showTitleList.add(titleIdList.get(x));
        }
        return showTitleList;
    }

    private ArrayList<LocalDate> getShowListByFirstDay(LocalDate firstDayOfMonth)
    {
        ArrayList<LocalDate> showList = new ArrayList<>();
        if (firstDayOfWeek>=1&&firstDayOfWeek<=7)
        {
            int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
            int minusDays = dayOfWeek-firstDayOfWeek+(firstDayOfWeek+7-dayOfWeek)/7*7;
            LocalDate addDate = firstDayOfMonth.minusDays(minusDays-minusDays/7*7);
            for (int x=0;x<42;x++)
            {
                showList.add(addDate);
                addDate = addDate.plusDays(1);
            }
        }
        return showList;
    }

    public void updateSelectDateList(ArrayList<LocalDate> selectDateList)
    {
        this.selectDateList = selectDateList;
        updateSelectDateList();
    }

    private void updateSelectDateList()
    {
        for (int x=0;x<viewHolderList.size();x++)
        {
            setStateOfDateItem(viewHolderList.get(x));
        }
    }

    public void updateFirstDayOfMonth(LocalDate firstDayOfMonth)
    {
        this.firstDayOfMonth = firstDayOfMonth;
        month = this.firstDayOfMonth.getMonthValue();
        showList = getShowListByFirstDay(this.firstDayOfMonth);
        for (int x=7;x<viewHolderList.size();x++)
        {
            if (viewHolderList.get(x).date!=null)
            {
                viewHolderList.get(x).date = showList.get(x-7);
            }
        }
        updateSelectDateList();
    }

    private void setStateOfDateItem(ViewHolder holder)
    {
        if (holder.date!=null)
        {
            Resources resources = holder.layout.getResources();
            holder.textView.setText(""+holder.date.getDayOfMonth());
            if (selectDateList.contains(holder.date))
            {
                if (selectDateList.contains(holder.date.minusDays(1))&&selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.layout.setBackgroundResource(R.drawable.my_calendar_background_select_both);
                }
                else if (selectDateList.contains(holder.date.minusDays(1))&&!selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.layout.setBackgroundResource(R.drawable.my_calendar_background_select_have_left);
                }
                else if (!selectDateList.contains(holder.date.minusDays(1))&&selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.layout.setBackgroundResource(R.drawable.my_calendar_background_select_have_right);
                }
                else
                {
                    holder.layout.setBackgroundResource(R.drawable.my_calendar_background_select_only);
                }
                holder.textView.setTextColor(resources.getColor(R.color.textView_Text1));
            }
            else
            {
                holder.layout.setBackgroundResource(R.drawable.my_calendar_background_normal);
                holder.textView.setTextColor(resources.getColor(R.color.textView_Text3));
            }
            if (holder.date.getMonthValue()!=month)
            {
                holder.layout.setAlpha(0.5f);
            }
            else
            {
                holder.layout.setAlpha(1f);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_my_calendar, parent, false);
        MyCalendarAdapter.ViewHolder viewHolder = new MyCalendarAdapter.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (viewHolderList.size()>position&&position>=0)
        {
            viewHolderList.set(position,holder);
        }
        else
        {
            viewHolderList.add(holder);
        }
        Resources resources = holder.layout.getResources();
        if (position>=0&&position<7)
        {
            holder.date = null;
            holder.textView.setText(titleList.get(position));
            holder.textView.setTextColor(resources.getColor(R.color.textView_Text2));
        }
        else
        {
            position -= 7;
            holder.date = showList.get(position);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(holder.date);
                }
            });
            setStateOfDateItem(holder);
        }
    }

    @Override
    public int getItemCount() {
        return 49;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ConstraintLayout layout;
        private LocalDate date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_myCalendar_layout);
            textView = itemView.findViewById(R.id.item_myCalendar_textView);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(LocalDate date);
    }

}
