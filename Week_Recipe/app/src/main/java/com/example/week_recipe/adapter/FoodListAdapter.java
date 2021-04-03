package com.example.week_recipe.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.FoodList;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private final FoodList foodList;
    private final FoodList favouriteFoodList;
    private final OnFoodListItemClickListener onFoodListItemClickListener;
    private ArrayList<ViewHolder> viewHolderList;
    private final boolean hasMore;
    private final boolean hasDelete;
    private final boolean hasLike;

    public FoodListAdapter(FoodList foodList, OnFoodListItemClickListener listener, boolean hasMore, boolean hasDelete, boolean hasLike, FoodList favouriteFoodList) {
        viewHolderList = new ArrayList<>();
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
        if (hasLike&&favouriteFoodList!=null)
        {
            for (int x=0;x<viewHolderList.size();x++)
            {
                changeLikeState( viewHolderList.get(x),favouriteFoodList.hasFood(foodList.getByIndex(x)));
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolderList.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodName.setText(foodList.getByIndex(position).getName());
        holder.foodImage.setImageResource(foodList.getByIndex(position).getImageId());
        changeLikeState(holder,favouriteFoodList!=null&&favouriteFoodList.hasFood(foodList.getByIndex(position)));
    }

    @Override
    public int getItemCount() {
        return foodList.getSize();
    }

    public ViewHolder getItemViewHolder(int position) {
        return viewHolderList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView foodName;
        private final ImageView foodImage;
        private final ImageView more;
        private final ImageView delete;
        private final ImageView like;
        //private final CardView cardView;
        private final CardView deleteCardView;
        private final CardView likeCardView;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_recipeList_foodName);
            foodImage = itemView.findViewById(R.id.item_recipeList_foodImage);
            more = itemView.findViewById(R.id.item_recipeList_more);
            delete = itemView.findViewById(R.id.item_recipeList_delete);
            like = itemView.findViewById(R.id.item_recipeList_like);
            //cardView = itemView.findViewById(R.id.item_recipeList_cardView);
            deleteCardView = itemView.findViewById(R.id.item_recipeList_deleteCardView);
            likeCardView = itemView.findViewById(R.id.item_recipeList_likeCardView);

            deleteCardView.setVisibility(View.GONE);
            likeCardView.setVisibility(View.GONE);

            if (!hasMore) {
                more.setVisibility(View.GONE);
            } else {
                more.setOnClickListener(this);
                if (hasDelete) {
                    delete.setOnClickListener(this);
                }
                if (hasLike) {
                    like.setOnClickListener(this);
                }
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
                onFoodListItemClickListener.onFoodImageClick(getAdapterPosition());
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
                for (int x = 0; x < viewHolderList.size(); x++) {
                    if (x != getAdapterPosition()) {
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
                onFoodListItemClickListener.onDeleteClick(getAdapterPosition());
            }
            else if (v.equals(like) && hasLike)
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

    public interface OnFoodListItemClickListener {
        void onFoodImageClick(int clickedItemIndex);

        void onDeleteClick(int clickedItemIndex);

        void onLikeClick(int clickedItemIndex);
    }
}
