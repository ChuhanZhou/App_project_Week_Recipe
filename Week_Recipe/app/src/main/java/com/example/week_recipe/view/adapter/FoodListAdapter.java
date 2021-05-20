package com.example.week_recipe.view.adapter;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Food;
import com.example.week_recipe.model.domain.food.FoodList;

import java.util.HashMap;
import java.util.Map;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private FoodList foodList;
    private FoodList favouriteFoodList;
    private final OnFoodListItemClickListener onFoodListItemClickListener;
    private Map<Integer,ViewHolder> viewHolderMap;
    private final boolean hasMore;
    private final boolean hasDelete;
    private final boolean hasLike;
    private boolean showAnimation;
    private int numberOfAnimation;
    private int lastAnimationPosition;
    private boolean isReversed;

    public FoodListAdapter(boolean isReversed,FoodList foodList, OnFoodListItemClickListener listener, boolean hasMore, boolean hasDelete, boolean hasLike, FoodList favouriteFoodList) {
        this.isReversed = isReversed;
        showAnimation = true;
        numberOfAnimation = 0;
        lastAnimationPosition = -1;
        viewHolderMap = new HashMap<>();
        if (foodList == null) {
            this.foodList = new FoodList();
        } else {
            this.foodList = foodList;
        }
        onFoodListItemClickListener = listener;
        this.hasMore = hasMore;
        this.hasDelete = hasDelete;
        this.hasLike = hasLike;
        this.favouriteFoodList = favouriteFoodList;
    }

    public void updateLike(FoodList favouriteFoodList)
    {
        this.favouriteFoodList = favouriteFoodList;
        if (hasLike&&this.favouriteFoodList!=null)
        {
            for (int x=0;x<viewHolderMap.size();x++)
            {
                changeLikeState( viewHolderMap.get(x),this.favouriteFoodList.hasFood(foodList.getByIndex(x)));
            }
        }
    }

    private void changeLikeState(ViewHolder viewHolder,boolean isLike)
    {
        if (hasLike)
        {
            if (isLike)
            {
                viewHolder.like.setImageResource(R.drawable.ic_button_star_like);
                viewHolder.like.setBackgroundResource(R.color.like_yellow);
            }
            else
            {
                viewHolder.like.setImageResource(R.drawable.ic_button_star);
                viewHolder.like.setBackgroundResource(R.color.like_white);
            }
        }
    }

    public void updateFoodList(FoodList foodList,boolean showAnimation)
    {
        this.showAnimation = showAnimation;
        if (foodList == null) {
            this.foodList = new FoodList();
        } else {
            this.foodList = foodList;
        }
        lastAnimationPosition = -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateItem(ViewHolder holder, Food food)
    {
        holder.foodName.setText(food.getName());
        if (food.hasImage())
        {
            holder.foodImage.setImageBitmap(food.getImage());
            holder.foodImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            holder.foodImage.setImageResource(R.drawable.ic_action_food);
            holder.foodImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        changeLikeState(holder,favouriteFoodList!=null&&favouriteFoodList.hasFood(food));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.showPosition = position;
        viewHolderMap.put(position,holder);
        updateItem(holder,foodList.getByIndex(position));

        new Thread(()->{showItemView(holder,0.125,position);}).start();
    }

    @Override
    public int getItemCount() {
        return foodList.getSize();
    }

    public ViewHolder getItemViewHolder(int position) {
        return viewHolderMap.get(position);
    }

    private void showItemView(ViewHolder holder,double second,int newPosition)
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

    public void stopAnimation()
    {
        showAnimation = false;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int showPosition;
        private final TextView foodName;
        private final ImageView foodImage;
        private final ImageView more;
        private final ImageView delete;
        private final ImageView like;
        //private final CardView cardView;
        private final CardView deleteCardView;
        private final CardView likeCardView;
        private final View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            foodName = itemView.findViewById(R.id.item_foodList_foodName);
            foodImage = itemView.findViewById(R.id.item_foodList_foodImage);
            more = itemView.findViewById(R.id.item_foodList_more);
            delete = itemView.findViewById(R.id.item_foodList_delete);
            like = itemView.findViewById(R.id.item_foodList_like);
            //cardView = itemView.findViewById(R.id.item_foodList_cardView);
            deleteCardView = itemView.findViewById(R.id.item_foodList_deleteCardView);
            likeCardView = itemView.findViewById(R.id.item_foodList_likeCardView);

            if (hasMore||!hasDelete)
            {
                deleteCardView.setVisibility(View.GONE);
            }
            if (hasMore||!hasLike)
            {
                likeCardView.setVisibility(View.GONE);
            }

            if (!hasMore) {
                more.setVisibility(View.INVISIBLE);
            } else {
                more.setOnClickListener(this);
            }
            if (hasDelete) {
                delete.setOnClickListener(this);
            }
            if (hasLike) {
                like.setOnClickListener(this);
            }
            foodImage.setOnClickListener(this);
        }

        private boolean isShowMore()
        {
            return deleteCardView.getVisibility() == View.VISIBLE || likeCardView.getVisibility() == View.VISIBLE;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(foodImage))
            {
                if (isReversed)
                {
                    onFoodListItemClickListener.onFoodImageClick(getItemCount()-getAdapterPosition()-1);
                    System.out.println(getItemCount()-getAdapterPosition()-1);
                }
                else
                {
                    onFoodListItemClickListener.onFoodImageClick(getAdapterPosition());
                }
            }
            else if (v.equals(more) && hasMore)
            {
                if (isShowMore()) {
                    if (hasDelete) {
                        hideMore(deleteCardView, delete, 0.25);
                    }
                    if (hasLike) {
                        hideMore(likeCardView, like, 0.125);
                    }
                } else {
                    if (hasDelete) {
                        showMore(deleteCardView, delete, 0.125);
                    }
                    if (hasLike) {
                        showMore(likeCardView, like, 0.25);
                    }
                }
                for (int x = 0; x < viewHolderMap.size(); x++) {
                    if (x != getAdapterPosition()&&getItemViewHolder(x).showPosition == x) {
                        System.out.println(x+"::"+getAdapterPosition());
                        if (hasDelete) {
                            hideMore(getItemViewHolder(x).deleteCardView, getItemViewHolder(x).delete, 0.25);
                        }
                        if (hasLike) {
                            hideMore(getItemViewHolder(x).likeCardView, getItemViewHolder(x).like, 0.125);
                        }
                    }
                }
            }
            else if (v.equals(delete) && hasDelete)
            {
                if (isReversed)
                {
                    onFoodListItemClickListener.onDeleteClick(getItemCount()-getAdapterPosition()-1);
                    System.out.println(getItemCount()-getAdapterPosition()-1);
                }
                else
                {
                    onFoodListItemClickListener.onDeleteClick(getAdapterPosition());
                }
            }
            else if (v.equals(like) && hasLike)
            {
                if (isReversed)
                {
                    onFoodListItemClickListener.onLikeClick(getItemCount()-getAdapterPosition()-1);
                    System.out.println(getItemCount()-getAdapterPosition()-1);
                }
                else
                {
                    onFoodListItemClickListener.onLikeClick(getAdapterPosition());
                }
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

        public View getView() {
            return view;
        }
    }

    public interface OnFoodListItemClickListener {
        void onFoodImageClick(int clickedItemIndex);

        void onDeleteClick(int clickedItemIndex);

        void onLikeClick(int clickedItemIndex);
    }
}
