package idv.tgp10101.eric.login_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import idv.tgp10101.eric.R;


public class LoginFragment extends Fragment {
    private static final String TAG = "TAG_MainActivity";
    private Activity activity;
    private FirebaseAuth auth;
    private EditText et_Username,et_Password;
    private TextView textView,tv_Username,tv_Password,tv_Login_Forget,tv_Login_Signup;
    private ImageView iv_Login_Gmail,iv_Login_Fb,iv_Login_Twitter,iv_Login_Line,iv_Login_Whatsapp,iv_Login_Yahoo;
    private ImageView iv_Login_Wechat,iv_Login_Ig,iv_Login_Linkedin,iv_Login_Skype,iv_Login_Apple,iv_Login_Github;
    private Button bt_Login;
    private Bundle bundle;
    private GoogleSignInClient client;
    private ActivityResultLauncher<Intent> signInGoogleLauncher;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // =========================================== Firebase =========================================== //
        auth = FirebaseAuth.getInstance();
        // =========================================== Firebase =========================================== //
        // =========================================== FaceBook =========================================== //
        callbackManager = CallbackManager.Factory.create();
        // =========================================== FaceBook =========================================== //
        // =========================================== Google =========================================== //
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // ???google-services.json???????????????????????????????????????????????????
                .requestIdToken(getString(R.string.default_web_client_id))
                // ????????????email
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(requireActivity(), options);
        // =========================================== Google =========================================== //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInGoogleLauncher = getsignInGoogleLauncher();
        activity = getActivity();
        findviews(view);
        handleButton();
    }

    private void handleButton() {
        // =========================================== ???????????? =========================================== //
        bt_Login.setOnClickListener( view -> {
            final String username = String.valueOf(et_Username.getText());
            final String password = String.valueOf(et_Password.getText());
            String nickname = null;
            for (Map<String,String> userMap:getUserlist()) {
                final String uStr = userMap.get("username");
                final String pStr = userMap.get("password");
                if(Objects.equals(username,uStr) && Objects.equals(password,pStr))  {
                    nickname = userMap.get("nickname");
                }
            }
            if(nickname == null) {
                Toast.makeText(activity, "??????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }else {
                bundle = new Bundle();
                bundle.putString("nickname",nickname);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_login_to_result,bundle);
            }
        });
        // =========================================== ???????????? =========================================== //
        // =========================================== ?????????????????? =========================================== //
        tv_Login_Forget.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_login_to_forget,bundle);
        });
        // =========================================== ?????????????????? =========================================== //
        // =========================================== ?????????????????? =========================================== //
        tv_Login_Signup.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_login_to_sign,bundle);
        });
        // =========================================== ?????????????????? =========================================== //
        // =========================================== ?????????????????? =========================================== //
        iv_Login_Gmail.setOnClickListener( view -> signInGoogle() );
        iv_Login_Fb.setOnClickListener( view -> signInFB() );
//        iv_Login_Twitter.setOnClickListener( view -> testMessage() );
        iv_Login_Apple.setOnClickListener( view -> testMessage() );
        iv_Login_Github.setOnClickListener( view -> testMessage() );
//        iv_Login_Yahoo.setOnClickListener( view -> testMessage() );

//        iv_Login_Ig.setOnClickListener( view -> testMessage() );
//        iv_Login_Skype.setOnClickListener( view -> testMessage() );
//        iv_Login_Whatsapp.setOnClickListener( view -> testMessage() );
//        iv_Login_Line.setOnClickListener( view -> testMessage() );
        // =========================================== ?????????????????? =========================================== //
    }

    private void findviews(View view) {
        et_Username = view.findViewById(R.id.et_Username);
        et_Password = view.findViewById(R.id.et_Password);
        bt_Login = view.findViewById(R.id.bt_Login);
        tv_Login_Forget = view.findViewById(R.id.tv_Login_Forget);
        tv_Login_Signup = view.findViewById(R.id.tv_Login_Signup);
        iv_Login_Gmail = view.findViewById(R.id.iv_Login_Gmail);
        iv_Login_Fb = view.findViewById(R.id.iv_Login_Fb);
        iv_Login_Apple = view.findViewById(R.id.iv_Login_Apple);
        iv_Login_Github = view.findViewById(R.id.iv_Login_Github);
        textView = view.findViewById(R.id.textView);

    }
    private void testMessage() {
        Toast.makeText(activity, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
    }


    private List<Map<String,String>> getUserlist() {
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username","guest");
        userMap.put("password","guest");
        userMap.put("nickname","??????");
        list.add(userMap);
        userMap = new HashMap<>();
        userMap.put("username","VIPguest");
        userMap.put("password","VIPguest");
        userMap.put("nickname","VIP??????");
        list.add(userMap);
        return list;
    }
    // =========================================== Google =========================================== //

    private void signInGoogle() {
        Intent signInIntent = client.getSignInIntent();
        signInGoogleLauncher.launch(signInIntent); // ??????Google????????????
    }

    private ActivityResultLauncher<Intent> getsignInGoogleLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            firebaseAuthWithGoogle(account);
                        } else {
                            Log.e(TAG, "GoogleSignInAccount is null");
                        }
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.e(TAG, e.toString());
                    }
                });
    }

    // ??????Google????????????Firebase??????
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // get the unique ID for the Google account
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    // ??????????????????????????????????????????????????????
                    if (task.isSuccessful()) {
                        bundle = new Bundle();
                        bundle.putString("nickname","Google??????");
                        NavController navController = Navigation.findNavController(textView);
                        navController.navigate(R.id.action_login_to_result,bundle);
                    } else {
                        Exception exception = task.getException();
                        String message = exception == null ? "Sign in fail." : exception.getMessage();
//                        textView.setText(message);
                    }
                });
    }
    // =========================================== Google =========================================== //
    // =========================================== FaceBook =========================================== //
    // ??????FB????????????
    private void signInFB() {
        LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess(): " + loginResult);
                signInFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel()");
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Log.e(TAG, "onError(): " + exception.getMessage());
            }
        });
    }

    // ??????FB token??????Firebase??????
    private void signInFirebase(AccessToken token) {
        Log.d(TAG, "signInFirebase: " + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    // ??????????????????????????????????????????????????????
                    if (task.isSuccessful()) {
                        bundle = new Bundle();
                        bundle.putString("nickname","FaceBook??????");
                        NavController navController = Navigation.findNavController(textView);
                        navController.navigate(R.id.action_login_to_result,bundle);
                    } else {
                        Exception exception = task.getException();
                        String message = "Sign in fail.";
                        if (exception != null && exception.getMessage() != null) {
                            message = exception.getMessage();
                        }
                        Log.e(TAG, message);
//                        textView.setText(message);
                    }
                });
    }
    // =========================================== FaceBook =========================================== //

    @Override
    public void onStart() {
        super.onStart();
        // ??????user???????????????????????????FirebaseUser????????????null
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            bundle = new Bundle();
            bundle.putString("nickname","??????");
            NavController navController = Navigation.findNavController(textView);
            navController.navigate(R.id.action_login_to_result,bundle);
        }
    }
}