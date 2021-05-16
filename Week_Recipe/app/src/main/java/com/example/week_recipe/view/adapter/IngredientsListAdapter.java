package com.example.week_recipe.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week_recipe.R;
import com.example.week_recipe.model.domain.food.IngredientsList;
import com.example.week_recipe.view.fragment.HorizontalIngredientsListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder>{
    private IngredientsList ingredientsList;
    private ArrayList<IngredientsList> lineList;
    private Map<Integer,HorizontalIngredientsListFragment> fragmentMap;
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
        fragmentMap = new HashMap<>();
        updateIngredientsList(null,lineList);
    }

    //load view
    public IngredientsListAdapter(IngredientsList ingredientsList,OnLoadFinishListener loadFinishListener)
    {
        this.needSet = false;
        this.clickListener= null;
        this.loadFinishListener = loadFinishListener;
        fragmentMap = new HashMap<>();
        updateIngredientsList(ingredientsList,null);
    }

    public void updateIngredientsList(IngredientsList ingredientsList,ArrayList<IngredientsList> lineList)
    {
        if (ingredientsList==null)
        {
            this.ingredientsList = new IngredientsList();
        }
        else
        {
            this.ingredientsList = ingredientsList;
            fragmentMap.clear();
        }
        if (lineList==null)
        {
            this.lineList = new ArrayList<>();
        }
        else
        {
            this.lineList = lineList;
            for (int x=0;x<this.lineList.size();x++)
            {
                updateItemByLineList(x);
            }
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

    private void updateItemByLineList(int position)
    {
        if (fragmentMap.containsKey(position))
        {
            HorizontalIngredientsListFragment fragment = fragmentMap.get(position);
            fragment.updateIngredientsList(lineList.get(position));
        }
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
            if (fragmentMap.size()<=position&&!countLock)
            {
                if (fragmentMap.size()>0&& fragmentMap.get(fragmentMap.size()-1).getOverflowList().getSize()==0||ingredientsList.getSize()==0)
                {
                    countLock = true;
                    System.out.println("===============load finish==============");
                }
                fragmentMap.put(position,holder.fragment);

                if (position!=0)
                {
                    IngredientsList overflowList = fragmentMap.get(position-1).getOverflowList();
                    lineList.add(position-1, fragmentMap.get(position-1).getShowList());
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
                    fragmentMap.put(position, holder.fragment);
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
            fragmentMap.put(position,holder.fragment);
        }
    }

    public IngredientsList getShowListByLine(int index)
    {
        if (fragmentMap.size()>index&&index>=0)
        {
            return fragmentMap.get(index).getShowList();
        }
        return null;
    }

    public IngredientsList getOverFlowListByLine(int index)
    {
        if (fragmentMap.size()>index&&index>=0)
        {
            return fragmentMap.get(index).getOverflowList();
        }
        return null;
    }

    public boolean isCountLock() {
        return countLock;
    }

    public int getSizeOfFragmentList() {
        return fragmentMap.size();
    }

    @Override
    public int getItemCount() {
        if (clickListener==null)
        {
            return fragmentMap.size()+1;
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