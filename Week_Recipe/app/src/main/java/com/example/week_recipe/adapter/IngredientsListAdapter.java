package com.example.week_recipe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.Ingredients;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.view.fragment.HorizontalIngredientsListFragment;
import com.example.week_recipe.view.fragment.IngredientsListFragment;

import java.util.ArrayList;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder>{
    private IngredientsList ingredientsList;
    private ArrayList<IngredientsList> lineList;
    private ArrayList<HorizontalIngredientsListFragment> fragmentList;
    private HorizontalIngredientsListAdapter.OnItemClickListener clickListener;
    private boolean needSet;
    private boolean countLock;
    private boolean showLine;

    public IngredientsListAdapter(IngredientsList ingredientsList,boolean needSet,HorizontalIngredientsListAdapter.OnItemClickListener clickListener)
    {
        updateIngredientsList(ingredientsList);
        this.needSet = needSet;
        this.clickListener= clickListener;
    }

    public void updateIngredientsList(IngredientsList ingredientsList)
    {
        fragmentList = new ArrayList<>();
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
        }
        lineList = new ArrayList<>();
        showLine = false;
        countLock = false;
    }

    private void updateItem(IngredientsListAdapter.ViewHolder holder, IngredientsList ingredientsList,IngredientsList overflowList)
    {
        holder.fragment.bind(ingredientsList,overflowList,needSet,clickListener);
        holder.fragment.setHeight(showLine);
    }

    private void showLine()
    {
        showLine = true;
        for (int x=0;x<fragmentList.size();x++)
        {
            fragmentList.get(x).setHeight(showLine);
            fragmentList.get(x).reShow();
        }
    }

    @NonNull
    @Override
    public IngredientsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_horizontal_ingredients_list, parent, false);
        IngredientsListAdapter.ViewHolder viewHolder = new IngredientsListAdapter.ViewHolder(view);
        System.out.println("viewHolderCreate");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (fragmentList.size()<=position&&!countLock)
        {
            if (fragmentList.size()>0&&fragmentList.get(fragmentList.size()-1).getOverflowList().getSize()==0)
            {
                countLock = true;
                System.out.println("===============locked==============");
                showLine();
            }
            fragmentList.add(holder.fragment);

            if (position!=0)
            {
                IngredientsList overflowList = fragmentList.get(position-1).getOverflowList();
                lineList.add(position-1,fragmentList.get(position-1).getShowList());
                updateItem(holder,overflowList,null);
            }
            else
            {
                updateItem(holder,ingredientsList,null);

            }
        }
        else
        {
            if (lineList.size()>position&&position>=0)
            {
                IngredientsList overflowList = new IngredientsList();
                for (int x=position+1;x<lineList.size();x++)
                {
                    for (int i=0;i<lineList.get(x).getSize();i++)
                    {
                        overflowList.add(lineList.get(x).getByIndex(i));
                    }
                }
                updateItem(holder,lineList.get(position),overflowList);
                fragmentList.set(position,holder.fragment);
            }
            else
            {
                updateItem(holder,null,new IngredientsList());
            }
        }
        System.out.println("line:"+position+"all:"+(fragmentList.size()+1));
    }

    public IngredientsList getShowListByLine(int index)
    {
        if (fragmentList.size()>index&&index>=0)
        {
            return fragmentList.get(index).getShowList();
        }
        return null;
    }

    public IngredientsList getOverFlowListByLine(int index)
    {
        if (fragmentList.size()>index&&index>=0)
        {
            return fragmentList.get(index).getOverflowList();
        }
        return null;
    }

    public boolean isCountLock() {
        return countLock;
    }

    public int getSizeOfFragmentList() {
        return fragmentList.size();
    }

    @Override
    public int getItemCount() {
        return fragmentList.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HorizontalIngredientsListFragment fragment;

        ViewHolder(View itemView) {
            super(itemView);
            fragment = new HorizontalIngredientsListFragment(itemView);
        }

        @Override
        public void onClick(View v) {
        }

        public HorizontalIngredientsListFragment getFragment() {
            return fragment;
        }
    }
}