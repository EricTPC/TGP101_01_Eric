package idv.tgp10101.eric.forntpage.dataquery_project;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import idv.tgp10101.eric.R;


public class GpsNavFragment extends Fragment {
    private static final String TAG = "TAG_GpsNavFragment";
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private Activity activity;
    private Bundle bundle;
    private EditText et_Gps_PlaceA,et_Gps_PlaceB;
    private Button bt_Gps_LocationA,bt_Gps_StreetA,bt_Gps_LocationB,bt_Gps_StreetB,bt_Gps_PlaceOK,bt_Gps_PlaceBack;
    private Geocoder geocoder;
    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        activity = getActivity();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps_nav, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleGeocoder();
        handleIntent();
        handleButton();
    }




    private void findViews(View view) {
        et_Gps_PlaceA = view.findViewById(R.id.et_Gps_PlaceA);
        bt_Gps_LocationA = view.findViewById(R.id.bt_Gps_LocationA);
        bt_Gps_StreetA = view.findViewById(R.id.bt_Gps_StreetA);

        et_Gps_PlaceB = view.findViewById(R.id.et_Gps_PlaceB);
        bt_Gps_LocationB = view.findViewById(R.id.bt_Gps_LocationB);
        bt_Gps_StreetB = view.findViewById(R.id.bt_Gps_StreetB);


        bt_Gps_PlaceOK = view.findViewById(R.id.bt_Gps_PlaceOK);
        bt_Gps_PlaceBack= view.findViewById(R.id.bt_Gps_PlaceBack);

    }

    private void handleGeocoder() {
        boolean isPresent = Geocoder.isPresent();
        if (!isPresent) {
            bt_Gps_LocationA.setEnabled(false);
            bt_Gps_StreetA.setEnabled(false);
            bt_Gps_LocationB.setEnabled(false);
            bt_Gps_StreetB.setEnabled(false);
            bt_Gps_PlaceOK.setEnabled(false);
            return;
        }
        geocoder = new Geocoder(requireContext());
    }

    private void handleIntent() {
        // 3. 實例化Intent物件，並設定Google Maps App的套件
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.apps.maps");
    }

    private void handleButton() {
        bt_Gps_LocationA.setOnClickListener(view -> location(et_Gps_PlaceA.getText()));
        bt_Gps_LocationB.setOnClickListener(view -> location(et_Gps_PlaceB.getText()));
        bt_Gps_StreetA.setOnClickListener(view -> streetView(et_Gps_PlaceA.getText()));
        bt_Gps_StreetB.setOnClickListener(view -> streetView(et_Gps_PlaceB.getText()));

        bt_Gps_PlaceBack.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_gpsNav_to_it_Info);
        });

        bt_Gps_PlaceOK.setOnClickListener(view -> {
            final Address addressOri = nameToLatLng(String.valueOf(et_Gps_PlaceA.getText()));
            final Address addressDest = nameToLatLng(String.valueOf(et_Gps_PlaceB.getText()));
            if (addressOri == null || addressDest == null) {
                Toast.makeText(requireContext(), "location not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. 設定欲顯示街景圖的地點之緯經度，並取得Uri物件
            final String uriStr = String.format(
                    Locale.US,
                    "https://www.google.com/maps/dir/?api=1&origin=%f,%f&destination=%f,%f",
                    addressOri.getLatitude(), addressOri.getLongitude(),
                    addressDest.getLatitude(), addressDest.getLongitude());
            Uri uri = Uri.parse(uriStr);
            intent.setData(uri);
            // 4. 檢查是否有內建的Google Maps App
            if (isIntentAvailable(intent)) {
                // 5. 跳轉至內建Google Maps App
                startActivity(intent);
            }
        });
    }
    //顯示輸入點的位置
    private void location(final Editable text) {
        final Address address = nameToLatLng(String.valueOf(text));
        if (address == null) {
            Toast.makeText(requireContext(), "location not found", Toast.LENGTH_SHORT).show();
            return;
        }
        final double lat = address.getLatitude();
        final double lng = address.getLongitude();
        // 2. 設定欲顯示地點的緯經度，並取得Uri物件
        Uri uri = Uri.parse(String.format(Locale.US, "geo:%f,%f", lat, lng));
        intent.setData(uri);
        // 4. 檢查是否有內建的Google Maps App
        if (isIntentAvailable(intent)) {
            // 5. 跳轉至內建Google Maps App
            startActivity(intent);
        }
    }

    //顯示街景的
    private void streetView(final Editable text) {
        final Address address = nameToLatLng(String.valueOf(text));
        if (address == null) {
            Toast.makeText(requireContext(), "location not found", Toast.LENGTH_SHORT).show();
            return;
        }
        final double lat = address.getLatitude();
        final double lng = address.getLongitude();
        // 2. 設定欲顯示街景圖的地點之緯經度，並取得Uri物件
        Uri uri = Uri.parse(String.format(Locale.US, "google.streetview:cbll=%f,%f", lat, lng));
        intent.setData(uri);
        // 4. 檢查是否有內建的Google Maps App
        if (isIntentAvailable(intent)) {
            // 5. 跳轉至內建Google Maps App
            startActivity(intent);
        }
    }

    private boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        return intent.resolveActivity(packageManager) != null;
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

}