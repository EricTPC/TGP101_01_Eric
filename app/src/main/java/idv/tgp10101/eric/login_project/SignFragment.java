package idv.tgp10101.eric.login_project;

import android.app.Activity;
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


public class SignFragment extends Fragment {
    private Activity activity;
    private Bundle bundle;
    private TextView tv_Sign_Title,tv_Sign_Name,tv_Sign_Password,tv_Sign_Password2,tv_Sign_Phone,tv_Sign_Email;
    private EditText et_Sign_Title,et_Sign_Name,et_Sign_Password,et_Sign_Password2,et_Sign_Phone,et_Sign_Email;
    private Button bt_Sign_Ok;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        findViews(view);
        handleButton();
    }

    private void findViews(View view) {
        et_Sign_Name = view.findViewById(R.id.et_Sign_Name);
        et_Sign_Password = view.findViewById(R.id.et_Sign_Password);
        et_Sign_Password2 = view.findViewById(R.id.et_Sign_Password2);
        et_Sign_Phone = view.findViewById(R.id.et_Sign_Phone);
        et_Sign_Email = view.findViewById(R.id.et_Sign_Email);
        bt_Sign_Ok = view.findViewById(R.id.bt_Sign_Ok);
    }
    private void handleButton() {

        bt_Sign_Ok.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_sign_to_login,bundle);
        });
    }

}