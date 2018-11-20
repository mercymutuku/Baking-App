package com.example.android.bakingapp.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Steps;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsAdapterViewHolder> {

    private static final String TAG = RecipeDetailsAdapter.class.getSimpleName();

    private Context mContext;
    private final StepsAdapterClickListener mStepClickListener;
    private Steps[] mStep = null;
    private TextView tvStepDescr;

    public RecipeDetailsAdapter(Steps[] steps, Context context, StepsAdapterClickListener stepClickListener){
        this.mStep = steps;
        this.mContext = context;
        this.mStepClickListener = stepClickListener;
    }

    @NonNull
    @Override
    public RecipeDetailsAdapter.RecipeDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_recipe_steps, parent, false);

        return new RecipeDetailsAdapter.RecipeDetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailsAdapter.RecipeDetailsAdapterViewHolder holder, int position) {

        String shortDescription = mStep[position].getShortDescription();
        tvStepDescr.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        return mStep.length;
    }

    class RecipeDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecipeDetailsAdapterViewHolder(View itemView) {
            super(itemView);

            tvStepDescr = itemView.findViewById(R.id.tv_recipe_step);
            tvStepDescr.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            mStepClickListener.onStepClicked(clickedPosition);
        }
    }

    public interface StepsAdapterClickListener{
        void onStepClicked(int position);
    }

    private static void logInfo(String str){
        if (str.length() > 4000){
            Log.i(TAG, str.substring(0, 4000));
            logInfo(str.substring(4000));
        } else {
            Log.i(TAG, str);
        }
    }
}
