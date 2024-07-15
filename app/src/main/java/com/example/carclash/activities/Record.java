package com.example.carclash.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carclash.R;
import com.example.carclash.fragments.ListFragment;
import com.example.carclash.fragments.MapFragment;
import com.example.carclash.interfaces.CallbackRecord;

public class Record extends AppCompatActivity implements CallbackRecord {
    private MapFragment mapFragment;
    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        mapFragment = new MapFragment();
        listFragment = new ListFragment();
        listFragment.setCallbackRecord(this);

        // Show fragments
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FRAME_map, mapFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FRAME_list, listFragment)
                .commit();
    }

    @Override
    public void recordClicked(double lat, double lon) {
        if (mapFragment != null) {
            mapFragment.zoom(lat, lon);
        }
    }
}
