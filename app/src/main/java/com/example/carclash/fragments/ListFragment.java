package com.example.carclash.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import com.example.carclash.R;
import com.example.carclash.adapters.RecordAdapter;
import com.example.carclash.interfaces.CallbackRecord;
import com.example.carclash.models.Record;
import com.example.carclash.models.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListFragment extends Fragment {
    private RecyclerView listRecords;
    private RelativeLayout notFoundLayout;
    private TableLayout recordTable;
    private CallbackRecord callbackRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void findViews(View v) {
        listRecords = v.findViewById(R.id.listRecords);
        notFoundLayout = v.findViewById(R.id.notFound);
        recordTable = v.findViewById(R.id.recordTable);
    }

    private void initViews() {
        String stringifiedRecords;
        Gson gson = new Gson();
        stringifiedRecords = SharedPreferencesManager.getInstance().getString("RecordList", null);

        if (stringifiedRecords != null) {
            Type recordListType = new TypeToken<ArrayList<Record>>() {}.getType();
            ArrayList<Record> records = gson.fromJson(stringifiedRecords, recordListType);
            recordTable.setVisibility(View.VISIBLE);
            notFoundLayout.setVisibility(View.INVISIBLE);

            RecordAdapter recordAdapter = new RecordAdapter(records, callbackRecord);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            listRecords.setLayoutManager(linearLayoutManager);
            listRecords.setAdapter(recordAdapter);
        } else {
            recordTable.setVisibility(View.INVISIBLE);
            notFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setCallbackRecord(CallbackRecord callbackRecord) {
        this.callbackRecord = callbackRecord;
    }
}
