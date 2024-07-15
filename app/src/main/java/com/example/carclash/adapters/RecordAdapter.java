package com.example.carclash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carclash.R;
import com.example.carclash.interfaces.CallbackRecord;
import com.example.carclash.models.Record;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private ArrayList<Record> records;
    private CallbackRecord callbackRecord;

    public RecordAdapter(ArrayList<Record> records, CallbackRecord callbackRecord) {
        this.records = prepareRecords(records);
        this.callbackRecord = callbackRecord;
    }

    private ArrayList<Record> prepareRecords(ArrayList<Record> records) {
        if (records != null) {
            Collections.sort(records, (r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));
            return new ArrayList<>(records.subList(0, Math.min(records.size(), 10)));
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = getItem(position);
        if (record != null) {

            holder.rank.setText(String.format("%d)", position + 1));
            holder.name.setText(record.getName());
            holder.score.setText(String.valueOf(record.getScore()));
        }
        holder.itemView.setOnClickListener(v -> {
            if (callbackRecord != null) {
                callbackRecord.recordClicked(record.getLatitude(), record.getLongitude());
            }
        });

    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    public Record getItem(int position) {
        return records != null && position < records.size() ? records.get(position) : null;
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout item;
        private final MaterialTextView name;
        private final MaterialTextView score;
        private final MaterialTextView rank;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}

