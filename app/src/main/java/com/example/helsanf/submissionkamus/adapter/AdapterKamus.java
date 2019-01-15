package com.example.helsanf.submissionkamus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helsanf.submissionkamus.R;
import com.example.helsanf.submissionkamus.model.KamusModel;

import java.util.ArrayList;

public class AdapterKamus extends RecyclerView.Adapter<AdapterKamus.KamusHolder> {
    private ArrayList<KamusModel> mData = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;


    public void replaceAdapter(ArrayList<KamusModel> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public AdapterKamus(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public AdapterKamus.KamusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new KamusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKamus.KamusHolder kamusHolder, int i) {
        kamusHolder.txtKata.setText(mData.get(i).getKosakata());
        kamusHolder.txtDesc.setText(mData.get(i).getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class KamusHolder extends RecyclerView.ViewHolder {
        TextView txtKata, txtDesc;

        public KamusHolder(@NonNull View itemView) {
            super(itemView);
            txtKata = itemView.findViewById(R.id.txt_kata);
            txtDesc = itemView.findViewById(R.id.txt_desc);

        }
    }
}
