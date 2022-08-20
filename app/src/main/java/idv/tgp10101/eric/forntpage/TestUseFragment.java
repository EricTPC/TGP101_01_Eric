package idv.tgp10101.eric.forntpage;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import idv.tgp10101.eric.R;


public class TestUseFragment extends Fragment {
    private static final String TAG = "TAG_TestUseFragment";
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView imageView,ivPicture;
    private File file;
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private boolean pictureTaken;
    private Activity activity;
    private ActivityResultLauncher<Intent> takePicLauncher2;
    private ActivityResultLauncher<Intent> cropPicLauncher2;
    private ActivityResultLauncher<Intent> pickPicLauncher2;
    private ContentResolver contentResolver;
    private MapView mapView;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private GoogleMap googleMap;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_use, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissionLauncher = getRequestPermissionlauncher();
        findviews(view);
        handleMapView(savedInstanceState);

    }

    private void findviews(View view) {
        mapView = view.findViewById(R.id.mapView);
    }

    private ActivityResultLauncher<String> getRequestPermissionlauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        showMyLocation();
                    }
                });
    }
    private void showMyLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        // 顯示當前位置(小藍點) 及 定位按鈕(右上角)
        googleMap.setMyLocationEnabled(true);

        // 取得定位供應器物件
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(activity);
        // 取得Task<Location>物件: 取得最後記錄位置
        Task<Location> task = fusedLocationClient.getLastLocation();
//        // 註冊/實作 定位成功監聽器
//        task.addOnSuccessListener(location -> {
//            if (location != null) {
//                // 取得緯度
//                final double lat = location.getLatitude();
//                // 取得經度
//                final double lng = location.getLongitude();
//                // 新建CameraPosition物件，並設定細節
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(new LatLng(lat, lng))  // 設定焦點
//                        .zoom(18)       // 設定縮放倍數
//                        .tilt(45)       // 設定傾斜角度
//                        .bearing(90)    // 設定旋轉角度
//                        .build();
//                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //一般圖
//                googleMap.setTrafficEnabled(true);
//                googleMap.setBuildingsEnabled(true);
//                // 新建CameraUpdate物件
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                // 使用鏡頭
//                googleMap.animateCamera(cameraUpdate);
//                UiSettings uiSettings = googleMap.getUiSettings();
//                uiSettings.setMyLocationButtonEnabled(true); //顯示位置按鈕
//                uiSettings.setZoomControlsEnabled(true); //縮放按鈕
//                uiSettings.setCompassEnabled(true); //指北針按鈕
//
//            }
//        });
//        final Location location = googleMap.getMyLocation();

        LatLng latLng = new LatLng(25.0330,121.5654);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(18)
                .tilt(45)
                .bearing(90)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); //地形圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //衛星圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //衛星圖+地形圖
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //一般圖
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        googleMap.setTrafficEnabled(true);
        googleMap.setBuildingsEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true); //顯示位置按鈕
        uiSettings.setZoomControlsEnabled(true); //縮放按鈕
        uiSettings.setCompassEnabled(true); //指北針按鈕

    }

    private void handleMapView(Bundle savedInstanceState) {
        // 初始化地圖
        mapView.onCreate(savedInstanceState);
        // 啟動地圖
        mapView.onStart();
        // 註冊/實作MapView監聽器
        mapView.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
//            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
            googleMap.setOnMapLongClickListener(this::addMarker);
            googleMap.setOnInfoWindowLongClickListener(Marker::remove);
        } );
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("1111111111111111111111")
                .snippet("testets")
//                .icon()
                .draggable(true);
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //

}