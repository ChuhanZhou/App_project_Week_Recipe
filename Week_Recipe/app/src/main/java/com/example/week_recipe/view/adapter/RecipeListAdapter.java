package com.example.week_recipe.view.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.recipe.DailyRecipe;
import com.example.week_recipe.model.domain.recipe.RecipeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{

    private RecipeList recipeList;
    private OnItemClickListener onItemClickListener;
    private Map<Integer,ViewHolder> viewHolderMap;
    private ArrayList<Integer> titleDateIdList;

    public RecipeListAdapter(RecipeList recipeList,OnItemClickListener onItemClickListener)
    {
        this.recipeList = recipeList;
        this.onItemClickListener = onItemClickListener;
        viewHolderMap = new HashMap<>();
        titleDateIdList = getDateTitleIdList();
    }

    private ArrayList<Integer> getDateTitleIdList()
    {
        ArrayList<Integer> titleDateIdList = new ArrayList<>();
        titleDateIdList.add(R.string.text_monday);
        titleDateIdList.add(R.string.text_tuesday);
        titleDateIdList.add(R.string.text_wednesday);
        titleDateIdList.add(R.string.text_thursday);
        titleDateIdList.add(R.string.text_friday);
        titleDateIdList.add(R.string.text_saturday);
        titleDateIdList.add(R.string.text_sunday);
        return titleDateIdList;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateViewHolder(int position)
    {
        ViewHolder holder = viewHolderMap.get(position);
        if (holder!=null)
        {
            DailyRecipe dailyRecipe = recipeList.getByIndex(position);
            holder.dailyRecipe = dailyRecipe;
            String dateName = holder.view.getContext().getString(titleDateIdList.get(dailyRecipe.getDate().getDayOfWeek().getValue()-1));
            holder.dateTextView.setText(dailyRecipe.getDate().getMonthValue()+"/"+dailyRecipe.getDate().getDayOfMonth()+" "+dateName);
            if (dailyRecipe.getBreakfast().getSize()!=0)
            {
                holder.breakfastNameList.setText(dailyRecipe.getBreakfast().toString());
            }
            else
            {
                holder.breakfastNameList.setText(R.string.text_noFood);
            }

            if (dailyRecipe.getLunch().getSize()!=0)
            {
                holder.lunchNameList.setText(dailyRecipe.getLunch().toString());
            }
            else
            {
                holder.lunchNameList.setText(R.string.text_noFood);
            }

            if (dailyRecipe.getDinner().getSize()!=0)
            {
                holder.dinnerNameList.setText(dailyRecipe.getDinner().toString());
            }
            else
            {
                holder.dinnerNameList.setText(R.string.text_noFood);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateRecipeList(RecipeList recipeList)
    {
        this.recipeList = recipeList;
        for (int x=0;x<recipeList.getSize();x++)
        {
            updateViewHolder(x);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe_list, parent, false);
        RecipeListAdapter.ViewHolder viewHolder = new RecipeListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewHolderMap.put(position,holder);
        updateViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return recipeList.getSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private DailyRecipe dailyRecipe;
        private ConstraintLayout layout;
        private final TextView dateTextView;
        private final TextView breakfastTitle;
        private final TextView breakfastNameList;
        private final TextView lunchTitle;
        private final TextView lunchNameList;
        private final TextView dinnerTitle;
        private final TextView dinnerNameList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            layout = view.findViewById(R.id.item_recipeList_constraintLayout);
            dateTextView = view.findViewById(R.id.item_recipeList_dateTextView);
            breakfastTitle = view.findViewById(R.id.item_recipeList_breakfastTitle);
            breakfastNameList = view.findViewById(R.id.item_recipeList_breakfastNameList);
            lunchTitle = view.findViewById(R.id.item_recipeList_lunchTitle);
            lunchNameList = view.findViewById(R.id.item_recipeList_lunchNameList);
            dinnerTitle = view.findViewById(R.id.item_recipeList_dinnerTitle);
            dinnerNameList = view.findViewById(R.id.item_recipeList_dinnerNameList);

            layout.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(dailyRecipe);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DailyRecipe dailyRecipe);
    }
}
