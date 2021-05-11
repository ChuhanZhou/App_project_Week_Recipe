package com.example.week_recipe.view.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.ViewHolder> {

    private LocalDate firstDayOfMonth;
    private int month;
    private int firstDayOfWeek = 7;
    private ArrayList<Integer> titleList;
    private ArrayList<LocalDate> showList;
    private ArrayList<LocalDate> selectDateList;
    private Map<Integer,ViewHolder> viewHolderMap;
    private OnItemClickListener listener;

    public MyCalendarAdapter(LocalDate firstDayOfMonth, ArrayList<LocalDate> selectDateList, OnItemClickListener listener)
    {
        viewHolderMap = new HashMap<>();
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
        for (int x=0;x<getItemCount();x++)
        {
            if (viewHolderMap.containsKey(x))
            {
                setStyleOfDateItem(viewHolderMap.get(x),x);
            }
        }
    }

    public void updateFirstDayOfMonth(LocalDate firstDayOfMonth)
    {
        this.firstDayOfMonth = firstDayOfMonth;
        month = this.firstDayOfMonth.getMonthValue();
        showList = getShowListByFirstDay(this.firstDayOfMonth);
        for (int x=0;x<showList.size();x++)
        {
            if (viewHolderMap.containsKey(x+7))
            {
                if (viewHolderMap.get(x+7).date!=null)
                {
                    viewHolderMap.get(x+7).date = showList.get(x);
                }
            }
        }
        updateSelectDateList();
    }

    @SuppressLint("SetTextI18n")
    private void setStyleOfDateItem(ViewHolder holder, int position)
    {
        Resources resources = holder.textView.getResources();
        if (holder.date!=null)
        {
            holder.textView.setText(""+holder.date.getDayOfMonth());
            if (selectDateList.contains(holder.date))
            {
                if (selectDateList.contains(holder.date.minusDays(1))&&selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.backgroundImageView.setImageResource(R.drawable.my_calendar_background_select_both);
                }
                else if (selectDateList.contains(holder.date.minusDays(1))&&!selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.backgroundImageView.setImageResource(R.drawable.my_calendar_background_select_have_left);
                }
                else if (!selectDateList.contains(holder.date.minusDays(1))&&selectDateList.contains(holder.date.plusDays(1)))
                {
                    holder.backgroundImageView.setImageResource(R.drawable.my_calendar_background_select_have_right);
                }
                else
                {
                    holder.backgroundImageView.setImageResource(R.drawable.my_calendar_background_select_only);
                }
                holder.textView.setTextColor(resources.getColor(R.color.textView_Text1));
            }
            else
            {
                holder.backgroundImageView.setImageDrawable(null);
                holder.textView.setTextColor(resources.getColor(R.color.textView_Text3));
            }
            if (holder.date.getMonthValue()!=month)
            {
                holder.layout.setTranslationZ(0f);
                switch (position)
                {
                    case 48:
                        holder.layout.setBackgroundResource(R.drawable.my_calendar_background_other_last_of_last_line);
                        break;
                    case 42:
                        holder.layout.setBackgroundResource(R.drawable.my_calendar_background_other_first_of_last_line);
                        break;
                    default:
                        holder.layout.setBackgroundResource(R.drawable.my_calendar_background_other);
                        break;
                }
                holder.layout.setAlpha(0.5f);
            }
            else
            {
                holder.layout.setTranslationZ(10f);
                holder.layout.setBackgroundResource(R.drawable.my_calendar_background_normal);
                holder.layout.setAlpha(1f);
            }
        }
        else
        {
            holder.layout.setTranslationZ(15f);
            holder.layout.setBackgroundResource(R.drawable.my_calendar_background_normal);
            holder.layout.setAlpha(1f);
            holder.textView.setTextColor(resources.getColor(R.color.textView_Text2));
            holder.backgroundImageView.setImageDrawable(null);
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
        viewHolderMap.put(position,holder);
        if (position>=0&&position<7)
        {
            holder.date = null;
            holder.textView.setText(titleList.get(position));
        }
        else
        {
            holder.date = showList.get(position-7);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(holder.date);
                }
            });
        }
        setStyleOfDateItem(holder,position);
    }

    @Override
    public int getItemCount() {
        return 49;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final ConstraintLayout layout;
        private LocalDate date;
        private final ImageView backgroundImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_myCalendar_layout);
            textView = itemView.findViewById(R.id.item_myCalendar_textView);
            backgroundImageView = itemView.findViewById(R.id.item_myCalendar_backgroundImageView);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(LocalDate date);
    }

}
