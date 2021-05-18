package com.example.week_recipe.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipe;
import com.example.week_recipe.model.domain.recipe.FavouriteWeekRecipeList;

import java.util.HashMap;
import java.util.Map;

public class WeekRecipeListAdapter extends RecyclerView.Adapter<WeekRecipeListAdapter.ViewHolder>{

    private FavouriteWeekRecipeList weekRecipeList;
    private Map<Integer,ViewHolder> viewHolderMap;
    private OnItemClickListener listener;
    private final boolean hasMore;
    private final boolean hasDelete;
    private boolean showAnimation;
    private int lastAnimationPosition;
    private int numberOfAnimation;

    public WeekRecipeListAdapter(FavouriteWeekRecipeList weekRecipeList,OnItemClickListener listener, boolean hasMore, boolean hasDelete)
    {
        showAnimation = true;
        lastAnimationPosition = -1;
        numberOfAnimation = 0;
        this.weekRecipeList = weekRecipeList;
        this.listener = listener;
        this.hasMore = hasMore;
        this.hasDelete = hasDelete;
        viewHolderMap = new HashMap<>();
    }

    public void updateWeekRecipeList(FavouriteWeekRecipeList weekRecipeList,boolean showAnimation)
    {
        this.weekRecipeList = weekRecipeList;
        this.showAnimation = showAnimation;
        lastAnimationPosition = -1;
    }

    private void updateItem(int position)
    {
        if (viewHolderMap.containsKey(position))
        {
            ViewHolder holder = viewHolderMap.get(position);
            FavouriteWeekRecipe weekRecipe = weekRecipeList.getByIndex(position);
            if (weekRecipe!=null)
            {
                holder.nameTextView.setText(weekRecipe.getName());
                holder.foodQuantityTextView.setText(""+weekRecipe.getNumberOfFood());
            }
            new Thread(()->{showItemView(holder,0.125,position);}).start();
        }
    }

    private void showItemView(ViewHolder holder, double second, int newPosition)
    {
        if (newPosition>lastAnimationPosition)
        {
            lastAnimationPosition = newPosition;
            int index = numberOfAnimation;
            numberOfAnimation++;
            if (showAnimation)
            {
                //make a delay
                TranslateAnimation waitAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, (index+1)*-1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                waitAnimation.setDuration((long) ((index+1)*second*1000));

                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, (index+2)*-1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                translateAnimation.setDuration((long) ((index+2)*second*1000));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(waitAnimation);
                animationSet.addAnimation(translateAnimation);
                holder.view.setAnimation(translateAnimation);
                try {
                    Thread.sleep(translateAnimation.getDuration());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            numberOfAnimation--;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_week_recipe_list, parent, false);
        WeekRecipeListAdapter.ViewHolder viewHolder = new WeekRecipeListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewHolderMap.put(position,holder);
        updateItem(position);
    }

    @Override
    public int getItemCount() {
        return weekRecipeList.getSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        private final TextView nameTextView;
        private final TextView foodQuantityTextView;
        private final CardView cardView;
        private final ImageView more;
        private final ImageView delete;
        private final CardView deleteCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nameTextView = itemView.findViewById(R.id.item_weekRecipeList_nameTextView);
            more = itemView.findViewById(R.id.item_weekRecipeList_more);
            delete = itemView.findViewById(R.id.item_weekRecipeList_delete);
            foodQuantityTextView = itemView.findViewById(R.id.item_weekRecipeList_foodQuantityTextView);
            cardView = itemView.findViewById(R.id.item_weekRecipeList_cardView);
            deleteCardView = itemView.findViewById(R.id.item_weekRecipeList_deleteCardView);
            cardView.setOnClickListener(this);
            if (hasMore||!hasDelete)
            {
                deleteCardView.setVisibility(View.GONE);
            }

            if (!hasMore) {
                more.setVisibility(View.INVISIBLE);
            } else {
                more.setOnClickListener(this);
            }
            if (hasDelete) {
                delete.setOnClickListener(this);
            }
        }

        private boolean isShowMore()
        {
            return deleteCardView.getVisibility() == View.VISIBLE;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(cardView))
            {
                listener.onItemClick(nameTextView.getText().toString());
            }
            else if (v.equals(more) && hasMore)
            {
                if (isShowMore()) {
                    if (hasDelete) {
                        hideMore(deleteCardView, delete, 0.25);
                    }
                } else {
                    if (hasDelete) {
                        showMore(deleteCardView, delete, 0.125);
                    }
                }
                for (int x = 0; x < viewHolderMap.size(); x++) {
                    if (x != getAdapterPosition()) {
                        if (hasDelete) {
                            if (viewHolderMap.containsKey(x))
                            {
                                hideMore(viewHolderMap.get(x).deleteCardView, viewHolderMap.get(x).delete, 0.25);
                            }
                        }
                    }
                }
            }
            else if (v.equals(delete) && hasDelete)
            {
                listener.onDeleteClick(nameTextView.getText().toString());
            }
        }

        private void showMore(View view, View insideView, double second) {
            if (view.getVisibility() != View.VISIBLE) {
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                translateAnimation.setDuration((long) (second * 1000));

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration((long) (second * 1000));

                RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration((long) (second * 1000));

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(alphaAnimation);

                if (insideView != null) {
                    insideView.setAnimation(rotateAnimation);
                }

                view.setAnimation(animationSet);
                view.setVisibility(View.VISIBLE);
            }
        }

        private void hideMore(View view, View insideView, double second) {
            if (view.getVisibility() != View.GONE) {
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                translateAnimation.setDuration((long) (second * 1000));

                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration((long) (second * 1000));

                RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration((long) (second * 1000));

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(alphaAnimation);

                if (insideView != null) {
                    insideView.setAnimation(rotateAnimation);
                }

                view.setAnimation(animationSet);
                view.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name);
        void onDeleteClick(String name);
    }
}
