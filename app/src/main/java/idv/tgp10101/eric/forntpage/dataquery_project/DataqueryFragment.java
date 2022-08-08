package idv.tgp10101.eric.forntpage.dataquery_project;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import idv.tgp10101.eric.R;


public class DataqueryFragment extends Fragment {
    private static final String TAG = "TAG_DataqueryFragment";
    private Activity activity;
    private ImageView iv_GpsNav,iv_FoodSearch,iv_PlaceSearch,iv_Weather,iv_Currency;
    private ImageView iv_Emergency,iv_Working01,iv_Working02,iv_Working03;
    private TextView tv_GpsNav,tv_FoodSearch,tv_PlaceSearch,tv_Weather,tv_Currency;
    private TextView tv_Emergency,tv_Working01,tv_Working02,tv_Working03;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        requireActivity().setTitle("資料查詢");
        return inflater.inflate(R.layout.fragment_dataquery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
    }


    private void findViews(View view) {
        iv_GpsNav = view.findViewById(R.id.iv_GpsNav);
        tv_GpsNav = view.findViewById(R.id.tv_GpsNav);

        iv_FoodSearch = view.findViewById(R.id.iv_FoodSearch);
        tv_FoodSearch = view.findViewById(R.id.tv_FoodSearch);

        iv_PlaceSearch = view.findViewById(R.id.iv_PlaceSearch);
        tv_PlaceSearch = view.findViewById(R.id.tv_PlaceSearch);

        iv_Weather = view.findViewById(R.id.iv_Weather);
        tv_Weather = view.findViewById(R.id.tv_Weather);

        iv_Currency = view.findViewById(R.id.iv_Currency);
        tv_Currency = view.findViewById(R.id.tv_Currency);

        iv_Emergency = view.findViewById(R.id.iv_Emergency);
        tv_Emergency = view.findViewById(R.id.tv_Emergency);

        iv_Working01 = view.findViewById(R.id.iv_Working01);
        tv_Working01 = view.findViewById(R.id.tv_Working01);

        iv_Working02 = view.findViewById(R.id.iv_Working02);
        tv_Working02 = view.findViewById(R.id.tv_Working02);

        iv_Working03 = view.findViewById(R.id.iv_Working03);
        tv_Working03 = view.findViewById(R.id.tv_Working03);
    }

    private void handleButton() {
        iv_GpsNav.setOnClickListener(view -> get_iv_GpsNav());
        iv_FoodSearch.setOnClickListener(view -> get_iv_FoodSearch());
        iv_PlaceSearch.setOnClickListener(view -> get_iv_PlaceSearch());
        iv_Weather.setOnClickListener(view -> get_iv_Weather());
        iv_Currency.setOnClickListener(view -> get_iv_Currency());
        iv_Emergency.setOnClickListener(view -> get_iv_Emergency());
        iv_Working01.setOnClickListener(view -> get_iv_Working01());
        iv_Working02.setOnClickListener(view -> get_iv_Working02());
        iv_Working03.setOnClickListener(view -> get_iv_Working03());
    }

    private void get_iv_Working03() {
        Toast.makeText(activity, "功能尚未完成：構思中...", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Working02() {
        Toast.makeText(activity, "功能尚未完成：構思中...", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Working01() {
        Toast.makeText(activity, "功能尚未完成：構思中...", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Emergency() {
        Toast.makeText(activity, "功能尚未完成：緊急救援功能", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Currency() {
        Toast.makeText(activity, "功能尚未完成：匯率換算功能", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Weather() {
        Toast.makeText(activity, "功能尚未完成：天氣資訊功能", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_PlaceSearch() {
        Toast.makeText(activity, "功能尚未完成：景點推薦功能", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_FoodSearch() {
        Toast.makeText(activity, "功能尚未完成：美食推薦功能", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_GpsNav() {
        Toast.makeText(activity, "功能尚未完成：地圖導航功能", Toast.LENGTH_SHORT).show();
    }

}