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
    private OnLoadFinishListener loadFinishListener;
    private boolean needSet;
    private boolean countLock;
    private boolean showLine;

    //show view
    public IngredientsListAdapter(ArrayList<IngredientsList> lineList,boolean needSet,HorizontalIngredientsListAdapter.OnItemClickListener clickListener)
    {
        this.needSet = needSet;
        this.clickListener= clickListener;
        this.loadFinishListener = null;
        updateIngredientsList(null,lineList);
    }

    //load view
    public IngredientsListAdapter(IngredientsList ingredientsList,OnLoadFinishListener loadFinishListener)
    {
        this.needSet = false;
        this.clickListener= null;
        this.loadFinishListener = loadFinishListener;
        updateIngredientsList(ingredientsList,null);
    }

    public void updateIngredientsList(IngredientsList ingredientsList,ArrayList<IngredientsList> lineList)
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
        if (lineList==null)
        {
            this.lineList = new ArrayList<>();
        }
        else
        {
            this.lineList = lineList;
        }
        if (clickListener==null)
        {
            showLine = false;
        }
        else
        {
            showLine = true;
        }
        countLock = false;
    }

    private void updateItem(IngredientsListAdapter.ViewHolder holder, IngredientsList ingredientsList,IngredientsList overflowList)
    {
        holder.fragment.bind(ingredientsList,overflowList,needSet,clickListener);
        holder.fragment.setHeight(showLine);
    }

    @NonNull
    @Override
    public IngredientsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_horizontal_ingredients_list, parent, false);
        IngredientsListAdapter.ViewHolder viewHolder = new IngredientsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (clickListener==null)
        {
            if (fragmentList.size()<=position&&!countLock)
            {
                if (fragmentList.size()>0&&fragmentList.get(fragmentList.size()-1).getOverflowList().getSize()==0||ingredientsList.getSize()==0)
                {
                    countLock = true;
                    System.out.println("===============load finish==============");
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

                if (countLock)
                {
                    ArrayList<IngredientsList> copyList = new ArrayList<>(lineList);
                    loadFinishListener.onLoadFinish(copyList);
                }
            }
            else {
                if (lineList.size() > position && position >= 0) {
                    IngredientsList overflowList = new IngredientsList();
                    for (int x = position + 1; x < lineList.size(); x++) {
                        for (int i = 0; i < lineList.get(x).getSize(); i++) {
                            overflowList.add(lineList.get(x).getByIndex(i));
                        }
                    }
                    updateItem(holder, lineList.get(position), overflowList);
                    fragmentList.set(position, holder.fragment);
                } else {
                    updateItem(holder, null, new IngredientsList());
                }
            }
        }
        else
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
            if (fragmentList.size() > position && position >= 0)
            {
                fragmentList.set(position,holder.fragment);
            }
            else
            {
                fragmentList.add(holder.fragment);
            }
        }
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
        if (clickListener==null)
        {
            return fragmentList.size()+1;
        }
        else
        {
            return lineList.size();
        }
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

    public interface OnLoadFinishListener {
        void onLoadFinish(ArrayList<IngredientsList> lineList);
    }
}