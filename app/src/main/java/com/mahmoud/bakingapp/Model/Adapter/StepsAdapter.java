package com.mahmoud.bakingapp.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.mahmoud.bakingapp.Model.RecipeInstruction;
import com.mahmoud.bakingapp.Model.Adapter.StepsAdapter.StepsViewHolder;
import com.mahmoud.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends Adapter<StepsViewHolder> {

    private final Context mContext;
    private List<RecipeInstruction> mInstructions;
    private final OnInstructionClickListener mOnInstructionClickListener;

    public StepsAdapter(Context context,OnInstructionClickListener onInstructionClickListener) {
        mContext = context;
        mOnInstructionClickListener = onInstructionClickListener;

    }

    public interface OnInstructionClickListener{
        void onClick(int position);
    }
    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootItem = LayoutInflater.from(mContext).inflate(R.layout.row_step_recycler_item,parent,false);

        return new StepsViewHolder(rootItem);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {


        RecipeInstruction instruction = mInstructions.get(position);
        holder.stepText.setText(instruction.getTitle());



    }

    @Override
    public int getItemCount() {
        if(mInstructions==null || mInstructions.size()==0){return 0;}

        return mInstructions.size();
    }

    public void setInstructions(List<RecipeInstruction> instructions)
    {
        mInstructions = instructions;
        notifyDataSetChanged();
    }

    class StepsViewHolder extends ViewHolder implements OnClickListener
    {
        @BindView(R.id.step_text_view)
        TextView stepText;

         StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            mOnInstructionClickListener.onClick(position);

        }
    }

}
