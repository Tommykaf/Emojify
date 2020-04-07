package com.changethislater.emojify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    public Map<String, Function<String,String>> options;
    private List<OptionViewHolder> viewHolders;

    public OptionAdapter(Map<String, Function<String,String>> options) {
        this.options = options;
        this.viewHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.option_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(layoutForItem, parent, false);
        OptionViewHolder viewHolder =  new OptionViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        Set<Map.Entry<String, Function<String,String>>> ress = options.entrySet();
        int counter = 0;
        Iterator<Map.Entry<String, Function<String,String>>> it = ress.iterator();
        while(counter < position){
            it.next();
            counter++;
        }
        ((TextView) holder.view.findViewById(R.id.optText)).setText(it.next().getKey());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public String switcheroo(CharSequence input){
        Log.d("yes123",input.toString());
        List<Function<String,String>> operations = new ArrayList<>();
        for(OptionViewHolder viewHolder : viewHolders){
            if(viewHolder.isChecked()){
                String name = (String) ((TextView)viewHolder.view.findViewById(R.id.optText)).getText();
                if(options.containsKey(name)){
                    operations.add(options.get(name));
                }

            }
        }
        String forFunc = input.toString();
        for(Function<String,String> stringFunction : operations){
            forFunc = stringFunction.apply(forFunc);
        }
        return forFunc;
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
