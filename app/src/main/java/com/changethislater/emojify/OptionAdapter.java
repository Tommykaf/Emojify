package com.changethislater.emojify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changethislater.emojify.utils.ReplacementRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private List<ReplacementRule> options;
    private List<OptionViewHolder> viewHolders;
    private Activity origin;

    public OptionAdapter(List<ReplacementRule> options, Activity origin) {
        this.options = options;
        this.viewHolders = new ArrayList<>();
        this.origin = origin;

    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.option_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(layoutForItem, parent, false);
        OptionViewHolder viewHolder =  new OptionViewHolder(view);
        viewHolder.itemView.setOnTouchListener((v, event) -> {
            if(event.getActionMasked()== MotionEvent.ACTION_DOWN){
                ((EmojifyContextMenu)origin).startDragging(viewHolder);
            }
            return true;
        });
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        ((TextView) holder.view.findViewById(R.id.optText)).setText(options.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public void moveItem(int from, int to){
        Collections.swap(options,from,to);
        Collections.swap(viewHolders,from,to);
    }

    public String applyOptions(CharSequence input) {
        String result = input.toString();
        for (int i = 0; i < options.size(); i++) {
            if (viewHolders.get(i).isChecked()) {
                result = options.get(i).apply(result);
            }
        }
        Log.d("result","result is "+result);
        return result;
    }


    class OptionViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout view;
        private CheckBox checkBox;
        private String optionName;

        private OptionViewHolder(LinearLayout view) {
            super(view);
            this.view = view;
            this.checkBox = view.findViewById(R.id.checkBox);
            this.optionName = ((TextView) view.findViewById(R.id.optText)).getText().toString();

        }

        public boolean isChecked() {
            return this.checkBox.isChecked();
        }

    }
}
