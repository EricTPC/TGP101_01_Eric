package idv.tgp10101.eric.login_project;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import idv.tgp10101.eric.R;


public class ResultFragment extends Fragment {
    private static final String TAG = "TAG_MainActivity";
    private Activity activity;
    private Button bt_SignOut_Google,bt_SignOut_Facebook;
    private TextView editText,tv_Result;
    private Bundle bundle;
    private TextView textView;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        findviews(view);
        haneleTvResult();
    }

    private void haneleTvResult() {
        tv_Result.setText(" Welcome!! \n 敬愛的" + bundle.getString("nickname") +"");
        tv_Result.setOnClickListener( view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_result_to_takePicture,bundle);
        });
        bt_SignOut_Google.setOnClickListener( view -> {signOut_Google();});
        bt_SignOut_Facebook.setOnClickListener( view -> {signOut_Facebook();});

    }

    private void findviews(View view) {
        bt_SignOut_Google = view.findViewById(R.id.bt_SignOut_Google);
        bt_SignOut_Facebook = view.findViewById(R.id.bt_SignOut_Facebook);
        tv_Result = view.findViewById(R.id.tv_Result);

    }

    private void signOut_Google() {
        // 登出Firebase帳號
        auth.signOut();

        // 下列程式會登出Google帳號，user再次登入時會再次跳出Google登入畫面
        // 如果沒有登出，則不會再次跳出Google登入畫面
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 由google-services.json轉出
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(requireActivity(), options);
        client.signOut().addOnCompleteListener(requireActivity(), task -> {
            Navigation.findNavController(textView).popBackStack();

            Log.d(TAG, "Signed out");
        });
    }


    private void signOut_Facebook() {
        // 登出Firebase帳號
        auth.signOut();
        // 登出FB帳號
        LoginManager.getInstance().logOut();
        Navigation.findNavController(textView).popBackStack();
        Log.d(TAG, "Signed out");
    }

}