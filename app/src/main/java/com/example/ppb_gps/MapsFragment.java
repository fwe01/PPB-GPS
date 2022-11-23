package com.example.ppb_gps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Spinner spinner;
    public static String[] MAP_TYPES = {
            "",
            "Normal",
            "Satellite",
            "Terrain",
            "Hybrid",
            "None"
    };

    private EditText edt_lat;
    private EditText edt_lng;
    private EditText edt_alamat;
    private EditText edt_zoom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        edt_alamat = view.findViewById(R.id.edt_alamat);
        edt_lat = view.findViewById(R.id.edt_lat);
        edt_lng = view.findViewById(R.id.edt_long);
        edt_zoom = view.findViewById(R.id.edt_zoom);

        initMapTypeDropdown(view);
        initMoveLatLongOnClickListener(view);
        initMoveLatLongOnClickListener(view);

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

    private void initMoveLatLongOnClickListener(View view) {
        view.findViewById(R.id.btn_pindah_lat_long).setOnClickListener(view1 -> {
            String alamat = edt_alamat.getText().toString();
            if (alamat.equals("")) {
                String lat = edt_lat.getText().toString();
                String lng = edt_lng.getText().toString();
                String zoom = edt_zoom.getText().toString();
                Double dbllat = Double.parseDouble((lat.equals("") ? "0" : lat));
                Double dbllng = Double.parseDouble(lng.equals("") ? "0" : lng);
                float dblzoom = Float.parseFloat(zoom.equals("") ? "0" : zoom);
                Toast.makeText(getContext(), "Move to Lat:" + dbllat + " Long:" + dbllng, Toast.LENGTH_LONG).show();
                goToPeta(dbllat, dbllng, dblzoom);
                return;
            }

            closeKeyboard(view);
            Geocoder g = new Geocoder(getContext());
            try {
                List<Address> daftar =
                        g.getFromLocationName(alamat, 1);
                if (daftar.size() > 0) {
                    Address address = daftar.get(0);
                    String nemuAlamat = address.getAddressLine(0);
                    Double lintang = address.getLatitude();
                    Double bujur = address.getLongitude();
                    goToPeta(lintang, bujur, 16);

                    edt_lat.setText(lintang.toString());
                    edt_lng.setText(bujur.toString());

                    Toast.makeText(getContext(), "Location " + nemuAlamat, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


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

    private void goToPeta(Double lat,
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

    private void closeKeyboard(View view) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}