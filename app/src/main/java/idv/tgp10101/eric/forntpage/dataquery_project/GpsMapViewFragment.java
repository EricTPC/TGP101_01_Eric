package idv.tgp10101.eric.forntpage.dataquery_project;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

import idv.tgp10101.eric.R;


public class GpsMapViewFragment extends Fragment {
    private static final String TAG = "GpsMapViewFragment";
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private Bundle bundle;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private MapView mv_GpsMapView_mapView;
    private GoogleMap googleMap;
    private Geocoder geocoder;
    private EditText et_GpsMapView_PlaceA,et_GpsMapView_PlaceB;
    private Button bt_GpsMapView_LocationA,bt_GpsMapView_StreetA,bt_GpsMapView_PlaceOK;
    private Button bt_GpsMapView_LocationB,bt_GpsMapView_StreetB,bt_GpsMapView_PlaceBack;


    // 初始化與畫面無直接關係之資料
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        activity = getActivity();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

    }
    // 載入並建立Layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps_map_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissionLauncher = getRequestPermissionLauncher();

        findViews(view);
        handleMapView(savedInstanceState);

    }


    private void findViews(View view) {
        mv_GpsMapView_mapView = view.findViewById(R.id.mv_GpsMapView_mapView);
        et_GpsMapView_PlaceA = view.findViewById(R.id.et_GpsMapView_PlaceA);
        bt_GpsMapView_LocationA = view.findViewById(R.id.bt_GpsMapView_LocationA);
        bt_GpsMapView_StreetA = view.findViewById(R.id.bt_GpsMapView_StreetA);

        et_GpsMapView_PlaceB = view.findViewById(R.id.et_GpsMapView_PlaceB);
        bt_GpsMapView_LocationB = view.findViewById(R.id.bt_GpsMapView_LocationB);
        bt_GpsMapView_StreetB = view.findViewById(R.id.bt_GpsMapView_StreetB);


        bt_GpsMapView_PlaceOK = view.findViewById(R.id.bt_GpsMapView_PlaceOK);
        bt_GpsMapView_PlaceBack= view.findViewById(R.id.bt_GpsMapView_PlaceBack);
    }

    private void handleButton() {
//        bt_GpsMapView_LocationA.setOnClickListener(view -> location(et_GpsMapView_PlaceA.getText()));
//        bt_GpsMapView_LocationB.setOnClickListener(view -> location(et_GpsMapView_PlaceB.getText()));
//        bt_GpsMapView_StreetA.setOnClickListener(view -> streetView(et_GpsMapView_PlaceA.getText()));
//        bt_GpsMapView_StreetB.setOnClickListener(view -> streetView(et_GpsMapView_PlaceB.getText()));

        bt_GpsMapView_PlaceBack.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_gpsMapView_to_it_Info);
        });
    }

    private ActivityResultLauncher<String> getRequestPermissionLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        showMyLocation();
                    }
                });
    }

    private void handleMapView(Bundle savedInstanceState) {
        mv_GpsMapView_mapView.onCreate(savedInstanceState);
        mv_GpsMapView_mapView.onStart();
        mv_GpsMapView_mapView.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
//            Toast.makeText(this,"MAPMAPMAP",Toast.LENGTH_SHORT).show();
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
            googleMap.setOnMapLongClickListener(this::addMarker);
            googleMap.setOnInfoWindowLongClickListener(Marker::remove);
            showMyLocation();
        });
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("TEST")
                .snippet("簡述文字")
                .draggable(true);
        googleMap.addMarker(markerOptions);
    }

    private void showMyLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

//        final Location location = googleMap.getMyLocation();
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng = new LatLng(25.0330, 121.5654);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(8)
//                .tilt(45)
//                .bearing(90)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // ⼀般圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // 地形圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // 衛星圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // ⼀般圖+衛星圖
//        googleMap.setTrafficEnabled(true); // 交通資訊圖層
//        googleMap.setBuildingsEnabled(true); // 建築物圖層

        UiSettings uiSettings = googleMap.getUiSettings();
        googleMap.setMyLocationEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("TEST")
                .snippet("簡述文字")
                .draggable(true);
        googleMap.addMarker(markerOptions);
    }

    //地址 經緯度轉換
    private Address nameToLatLng(final String name) {
        try {
            List<Address> addressList = geocoder.getFromLocationName(name, 1);
            if (addressList != null && addressList.size() > 0) {
                return addressList.get(0);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // 回傳訊息視窗元件
        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            final View view = View.inflate(activity, R.layout.info_of_window_mapview, null);
            final String title = marker.getTitle();
            final String snippet = marker.getSnippet();
            TextView tvTitle = view.findViewById(R.id.tv_Title);
            TextView tvSnippet = view.findViewById(R.id.tv_Snip);
            tvTitle.setText(title);
            tvSnippet.setText(snippet);
            return view;
        }

        // 回傳訊息視窗的內容元件
        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

    }
}