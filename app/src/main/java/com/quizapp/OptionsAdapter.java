package com.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Option> arrayList;
    private final Boolean isQuestion;

    private int selectedPosition = -1;
    private int lastItemSelectedPos = -1;

    public OptionsAdapter(Context context, ArrayList<Option> arrayList, Boolean isQuestion) {
        this.context = context;
        this.arrayList = arrayList;
        this.isQuestion = isQuestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_options, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.optionTextview.setText(arrayList.get(position).option);

        if (!isQuestion) {
            holder.mainCardView.setEnabled(false);
            if (arrayList.get(position).correct_answer != 0) {
                holder.mainCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_3311D147));
            }
            if (arrayList.get(position).isAnswered != 0) {
                holder.mainCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_33CC4141));
            }
            if (arrayList.get(position).correct_answer != 0 && arrayList.get(position).isAnswered != 0) {
                if (arrayList.get(position).correct_answer == arrayList.get(position).isAnswered) {
                    holder.mainCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_3311D147));
                }
            }
        } else {
            holder.mainCardView.setEnabled(true);
            holder.mainCardView.setCardBackgroundColor(Color.TRANSPARENT);
            if (selectedPosition == position) {
                holder.mainCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                arrayList.get(position).isAnswered = 1;
            } else {
                holder.mainCardView.setCardBackgroundColor(Color.TRANSPARENT);
                arrayList.get(position).isAnswered = 0;
            }
        }

        holder.mainCardView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            if (lastItemSelectedPos == -1) {
                lastItemSelectedPos = selectedPosition;
            } else {
                notifyItemChanged(lastItemSelectedPos);
                lastItemSelectedPos = selectedPosition;
            }
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView optionTextview;
        MaterialCardView mainCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            optionTextview = itemView.findViewById(R.id.option_textview);
            mainCardView = itemView.findViewById(R.id.main_cardView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

