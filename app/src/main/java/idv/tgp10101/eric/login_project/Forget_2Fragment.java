package idv.tgp10101.eric.login_project;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import idv.tgp10101.eric.R;


public class Forget_2Fragment extends Fragment {
    private static final String TAG = "TAG_Forget_2Fragment";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Bundle bundle;
    private EditText et_Forget2_NewPassword2,et_Forget2_NewPassword;
    private Button bt_Forget2_NewPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        requireActivity().setTitle("忘記密碼");
        return inflater.inflate(R.layout.fragment_forget_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
    }

    private void handleButton() {
        bt_Forget2_NewPassword.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_forget2_to_login,bundle);

        });
    }

    private void findViews(View view) {
        et_Forget2_NewPassword2 = view.findViewById(R.id.et_Forget2_NewPassword);
        et_Forget2_NewPassword = view.findViewById(R.id.et_Forget2_NewPassword2);
        bt_Forget2_NewPassword = view.findViewById(R.id.bt_Forget2_NewPassword);

    }
}