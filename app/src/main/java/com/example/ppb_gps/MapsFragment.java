package com.example.ppb_gps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Spinner spinner;
    public static String[] MAP_TYPES = {"", "Normal", "Satellite", "Terrain", "Hybrid", "None"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        initMapTypeDropdown(view);
        initOnClikListener(view);

        return view;
    }

    private void initMapTypeDropdown(View view) {
        spinner = view.findViewById(R.id.edt_tipe_map);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, MAP_TYPES);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemAtPosition = adapterView.getItemAtPosition(i);
                if ("".equals(itemAtPosition)) {
                    return;
                }
                if ("Satellite".equals(itemAtPosition)) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if ("Terrain".equals(itemAtPosition)) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if ("Hybrid".equals(itemAtPosition)) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                spinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initOnClikListener(View view) {
        view.findViewById(R.id.btn_pindah).setOnClickListener(view1 -> {
            String lat = ((EditText) view.findViewById(R.id.edt_lat)).getText().toString();
            String lng = ((EditText) view.findViewById(R.id.edt_long)).getText().toString();
            String zoom = ((EditText) view.findViewById(R.id.edt_zoom)).getText().toString();
            Double dbllat = Double.parseDouble((lat.equals("") ? "0" : lat));
            Double dbllng = Double.parseDouble(lng.equals("") ? "0" : lng);
            float dblzoom = Float.parseFloat(zoom.equals("") ? "0" : zoom);
            Toast.makeText(getContext(), "Move to Lat:" + dbllat + " Long:" + dbllng, Toast.LENGTH_LONG).show();
            gotoPeta(dbllat, dbllng, dblzoom);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void gotoPeta(Double lat,
                          Double lng, float z) {
        LatLng Lokasibaru = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().
                position(Lokasibaru).
                title("Marker in  " + lat + ":" + lng));
        googleMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(Lokasibaru, z));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ITS = new LatLng(-7.2819705, 112.795323);
        googleMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 15));
    }
}