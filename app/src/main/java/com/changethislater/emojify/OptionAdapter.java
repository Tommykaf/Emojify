package com.changethislater.emojify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    public List<String> options;


    public OptionAdapter(List<String> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.option_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(layoutForItem, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout view;
        private CheckBox checkBox;
        private String optionName;

        private OptionViewHolder(LinearLayout view) {
            super(view);
            this.view = view;
            this.checkBox = view.findViewById(R.id.checkBox);
            this.optionName = ((TextView) view.findViewById(R.id.textView)).getText().toString();
        }

        public boolean isChecked() {
            return this.checkBox.isChecked();
        }
    }
}
