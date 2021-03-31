package com.example.week_recipe.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    private final OnFoodListItemClickListener onFoodListItemClickListener;
    private ArrayList<View> itemViewList;

    public FoodListAdapter(FoodList foodList, OnFoodListItemClickListener listener) {
        itemViewList = new ArrayList<>();
        if (foodList == null) {
            this.foodList = new FoodList();
        } else {
            this.foodList = foodList;
        }
        onFoodListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_list_item, parent, false);
        itemViewList.add(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodName.setText(foodList.getByIndex(position).getName());
        holder.foodImage.setImageResource(foodList.getByIndex(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return foodList.getSize();
    }


    public View getItemView(int position) {
        return itemViewList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView foodName;
        ImageView foodImage;
        ImageView more;
        ImageView delete;
        ImageView like;
        CardView cardView;
        CardView deleteCardView;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_recipeList_foodName);
            foodImage = itemView.findViewById(R.id.item_recipeList_foodImage);
            more = itemView.findViewById(R.id.item_recipeList_more);
            delete = itemView.findViewById(R.id.item_recipeList_delete);
            like = itemView.findViewById(R.id.item_recipeList_like);
            cardView = itemView.findViewById(R.id.item_recipeList_cardView);
            deleteCardView = itemView.findViewById(R.id.item_recipeList_deleteCardView);
            deleteCardView.setVisibility(View.GONE);
            itemView.findViewById(R.id.item_recipeList_likeCardView).setVisibility(View.GONE);

            foodImage.setOnClickListener(this);
            more.setOnClickListener(this);
            delete.setOnClickListener(this);
            like.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(foodImage)) {
                onFoodListItemClickListener.onFoodImageClick(getAdapterPosition());
            } else if (v.equals(more)) {
                switch (deleteCardView.getVisibility()) {
                    case View.GONE:
                        showMore(getItemView(getAdapterPosition()).findViewById(R.id.item_recipeList_deleteCardView), delete, 0.125);
                        showMore(getItemView(getAdapterPosition()).findViewById(R.id.item_recipeList_likeCardView), like, 0.25);
                        break;
                    case View.VISIBLE:
                        hideMore(getItemView(getAdapterPosition()).findViewById(R.id.item_recipeList_deleteCardView), delete, 0.25);
                        hideMore(getItemView(getAdapterPosition()).findViewById(R.id.item_recipeList_likeCardView), like, 0.125);

                        break;
                }
                for (int x = 0; x < itemViewList.size(); x++) {
                    if (x != getAdapterPosition()) {
                        hideMore(itemViewList.get(x).findViewById(R.id.item_recipeList_deleteCardView), itemViewList.get(x).findViewById(R.id.item_recipeList_delete), 0.25);
                        hideMore(itemViewList.get(x).findViewById(R.id.item_recipeList_likeCardView), itemViewList.get(x).findViewById(R.id.item_recipeList_like), 0.125);
                    }
                }
            } else if (v.equals(delete)) {
                onFoodListItemClickListener.onDeleteClick(getAdapterPosition());
            } else if (v.equals(like)) {
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

            insideView.setAnimation(rotateAnimation);

            view.setAnimation(animationSet);
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideMore(View view, View insideView, double second) {
        if (view.getVisibility() != View.GONE) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f);
            translateAnimation.setDuration((long) (second * 1000));

            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration((long) (second * 1000));

            RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration((long) (second * 1000));

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);

            insideView.setAnimation(rotateAnimation);
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
