package idv.tgp10101.eric.login_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import idv.tgp10101.eric.MainActivity2;
import idv.tgp10101.eric.R;


public class WelcomeFragment extends Fragment {
    private static final String TAG = "TAG_WelcomeFragment";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Button bt_SignOut_Google,bt_SignOut_Facebook;
    private TextView editText,tv_Result;
    private Bundle bundle;
    private TextView textView;
    private FirebaseAuth auth;
    private ImageView iv_Welcome_Logo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        activity = getActivity();


        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
//        handleImageview();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bundle = new Bundle();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_welcome_to_login,bundle);
            }
        }, 1500);


    }

//    private void handleImageview() {
//        iv_Welcome_Logo.setOnClickListener( view -> {
//            bundle = new Bundle();
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.action_welcome_to_login,bundle);
//        });
//    }

    private void findViews(View view) {
        iv_Welcome_Logo = view.findViewById(R.id.iv_Welcome_Logo);
    }
}