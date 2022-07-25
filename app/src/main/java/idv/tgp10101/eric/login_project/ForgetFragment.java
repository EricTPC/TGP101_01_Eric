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

import idv.tgp10101.eric.R;


public class ForgetFragment extends Fragment {
    private Activity activity;
    private Bundle bundle;
    private EditText et_Forget_Account,et_Forget_VerCode;
    private Button bt_Forget_Checkdata,bt_Forget_Next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        findViews(view);
        handleButton();
    }

    private void handleButton() {
        bt_Forget_Next.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_forget_to_forget2,bundle);

        });
    }

    private void findViews(View view) {
        et_Forget_Account = view.findViewById(R.id.et_Forget_Account);
        et_Forget_VerCode = view.findViewById(R.id.et_Forget_VerCode);
        bt_Forget_Checkdata = view.findViewById(R.id.bt_Forget_Checkdata);
        bt_Forget_Next = view.findViewById(R.id.bt_Forget_Next);
    }



}