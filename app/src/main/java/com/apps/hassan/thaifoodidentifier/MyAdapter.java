package com.apps.hassan.thaifoodidentifier;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ClassifyResultViewHolder> {

    public static class ClassifyResultViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView className;
        TextView classConfidential;

        ClassifyResultViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            className = (TextView) itemView.findViewById(R.id.class_name);
            classConfidential = (TextView) itemView.findViewById(R.id.confidential_percentage);
        }

    }

    List<ClassifyResult> resultList;

    MyAdapter(List<ClassifyResult> resultList) {
        this.resultList = resultList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public ClassifyResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        ClassifyResultViewHolder classifyResultViewHolder = new ClassifyResultViewHolder(v);
        return classifyResultViewHolder;
    }

    @Override
    public void onBindViewHolder(ClassifyResultViewHolder classifyResultViewHolder, int i) {
        classifyResultViewHolder.className.setText(resultList.get(i).name);
        classifyResultViewHolder.classConfidential.setText(resultList.get(i).confidential);
    }

    @Override
    public int getItemCount() {
        return resultList.size();

    }

}
