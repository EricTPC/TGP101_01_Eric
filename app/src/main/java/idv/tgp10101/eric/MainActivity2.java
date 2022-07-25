package idv.tgp10101.eric;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    private BottomNavigationView bnv_Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViews();
        handleBottomNavigationView();

    }

    private void handleBottomNavigationView() {
        // 3.2 取得NavHostFragment物件
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_ShowFragment);
        // 3.3 取得NavController物件
        NavController navController = navHostFragment.getNavController();
        // 3.4 加入 導覽功能 至 頁籤導覽元件
        NavigationUI.setupWithNavController(bnv_Menu, navController);
    }

    private void findViews() {
        bnv_Menu = findViewById(R.id.bnv_Menu);
    }
}