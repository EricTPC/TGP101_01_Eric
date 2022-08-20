package idv.tgp10101.eric.login_project;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import idv.tgp10101.eric.MemberUser;
import idv.tgp10101.eric.R;
import idv.tgp10101.eric.Spot;


public class LoginFragment extends Fragment {
    private static final String TAG = "TAG_LoginFragment";
    private Activity activity;
    private SharedPreferences sharedPreferences;    //偏好設定
    private FirebaseAuth auth;                      //
    private FirebaseFirestore db;                   //
    private FirebaseStorage storage;
    private Bundle bundle;
    private EditText et_Username,et_Password;
    private TextView textView,tv_Username,tv_Password,tv_Login_Forget,tv_Login_Signup;
    private ImageView iv_Login_Gmail,iv_Login_Fb,iv_Login_Twitter,iv_Login_Line,iv_Login_Whatsapp,iv_Login_Yahoo;
    private ImageView iv_Login_Wechat,iv_Login_Ig,iv_Login_Linkedin,iv_Login_Skype,iv_Login_Apple,iv_Login_Github;
    private Button bt_Login;
    private List<MemberUser> memberUser;
    private MemberUser memberUserclass;
    private GoogleSignInClient client;
    private ActivityResultLauncher<Intent> signInGoogleLauncher;
    private CallbackManager callbackManager;
    private String paser_uid, paser_online, paser_userimage, paser_username, paser_password,paser_userloginclass;
    private String paser_phone, paser_email, paser_address, paser_level, paser_vippay, paser_viplevel,paser_usertoken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // =========================================== Firebase =========================================== //
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        activity = getActivity();
        memberUser = new ArrayList<>();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        // =========================================== Firebase =========================================== //
        // =========================================== FaceBook =========================================== //
        callbackManager = CallbackManager.Factory.create();
        // =========================================== FaceBook =========================================== //
        // =========================================== Google =========================================== //
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 由google-services.json轉出，有時會編譯失敗，但不影響執行
                .requestIdToken(getString(R.string.default_web_client_id))
                // 要求輸入email
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(requireActivity(), options);
        // =========================================== Google =========================================== //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        requireActivity().setTitle("登入頁面");
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInGoogleLauncher = getsignInGoogleLauncher();

        findviews(view);
        handleButton();
    }

    private void handleButton() {
        // =========================================== 登入按鈕 =========================================== //
        bt_Login.setOnClickListener( view -> {
            String email = et_Username.getText().toString();
            String password = et_Password.getText().toString();
            signIn(email, password);
        });
        // =========================================== 登入按鈕 =========================================== //


        // =========================================== 忘記密碼按鈕 =========================================== //
        tv_Login_Forget.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_login_to_forget,bundle);
        });
        // =========================================== 忘記密碼按鈕 =========================================== //
        // =========================================== 註冊會員按鈕 =========================================== //
        tv_Login_Signup.setOnClickListener( view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_login_to_sign,bundle);
        });
        // =========================================== 註冊會員按鈕 =========================================== //
        // =========================================== 快速登入按鈕 =========================================== //
        iv_Login_Gmail.setOnClickListener( view -> signInGoogle() );
//        iv_Login_Fb.setOnClickListener( view -> signInFB() );
        iv_Login_Fb.setOnClickListener( view -> workingMessage() );
//        iv_Login_Twitter.setOnClickListener( view -> testMessage() );
//        iv_Login_Apple.setOnClickListener( view -> testMessage() );
//        iv_Login_Github.setOnClickListener( view -> testMessage() );
//        iv_Login_Yahoo.setOnClickListener( view -> testMessage() );

//        iv_Login_Ig.setOnClickListener( view -> testMessage() );
//        iv_Login_Skype.setOnClickListener( view -> testMessage() );
//        iv_Login_Whatsapp.setOnClickListener( view -> testMessage() );
//        iv_Login_Line.setOnClickListener( view -> testMessage() );
        // =========================================== 快速登入按鈕 =========================================== //
    }

    private void findviews(View view) {
        et_Username = view.findViewById(R.id.et_Username);
        et_Password = view.findViewById(R.id.et_Password);
        bt_Login = view.findViewById(R.id.bt_Login);
        tv_Login_Forget = view.findViewById(R.id.tv_Login_Forget);
        tv_Login_Signup = view.findViewById(R.id.tv_Login_Signup);
        iv_Login_Gmail = view.findViewById(R.id.iv_Login_Gmail);
        iv_Login_Fb = view.findViewById(R.id.iv_Login_Fb);
        textView = view.findViewById(R.id.textView);

    }

    private void signIn(String email, String password) {
        if (isEmailOrPasswordEmpty(email, password)) {
            return;
        }
        // 利用user輸入的email與password登入

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // 登入成功轉至下頁；失敗則顯示錯誤訊息
                    if (task.isSuccessful()) {
//                        showAllMemberUsers();
                        FirebaseUser user_1 = auth.getCurrentUser();
                        String user_UID = user_1.getUid();
                        db.collection("MemberUsers").document(user_UID).get()
                                .addOnCompleteListener(task02 -> {
                                    if (task02.isSuccessful()) {
                                        MemberUser loginUser = task02.getResult().toObject(MemberUser.class);
                                        savePreferences("會員名字" , loginUser.getUsername() );
                                        Log.e(TAG, "savePreferences(\"會員名字\" , loginUser.getUsername() );" + loginUser.getUsername());
                                        Log.e(TAG, "savePreferences(\"會員名字\" , sharedPreferences );" + sharedPreferences.getString("會員名字","會員名字"));
                                        savePreferences("會員UID",  loginUser.getUid());
                                        savePreferences("會員登入類別" , loginUser.getUserloginclass() );
                                        savePreferences("會員大頭照" , loginUser.getUserimage() );
                                        savePreferences("會員名字" , loginUser.getUsername() );
                                        savePreferences("會員信箱" , loginUser.getEmail() );
                                        savePreferences("會員密碼" , loginUser.getPassword() );
                                        savePreferences("會員手機號碼" , loginUser.getPhone() );
                                        savePreferences("會員地址" , loginUser.getAddress() );
                                        savePreferences("會員是否在線上" , loginUser.getOnline() );
                                        savePreferences("會員等級" , loginUser.getLevel() );
                                        savePreferences("會員是否付費" , loginUser.getVippay() );
                                        savePreferences("VIP會員等級" , loginUser.getViplevel() );
                                        savePreferences("會員Token" , loginUser.getUsertoken() );
                                        savePreferences("管理員代號" , loginUser.getAdminclass() );
                                    } else {
                                        String message = task02.getException() == null ?
                                                getString(R.string.textInsertFail) :
                                                task02.getException().getMessage();
                                        Log.e(TAG, "message: " + message);
                                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Log.d(TAG,"會員memberUsermemberUsermemberUser： " + paser_username);
//                        bundle.putString("nickname", paser_username  );
//                        bundle.putStringArray("memberUser",memberUser);
                        NavController navController = Navigation.findNavController(et_Username);
                        navController.navigate(R.id.action_login_to_result,bundle);
                    } else {
                        String message;
                        Exception exception = task.getException();
                        if (exception == null) {
                            message = "Sign in fail.";
                        } else {
                            String exceptionType;
                            // FirebaseAuthInvalidCredentialsException代表帳號驗證不成功，例如email格式不正確
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                exceptionType = "Invalid Credential";
                            }
                            // FirebaseAuthInvalidUserException代表無此user，例如帳密錯誤
                            else if (exception instanceof FirebaseAuthInvalidUserException) {
                                exceptionType = "Invalid User";
                            } else {
                                exceptionType = exception.getClass().toString();
                            }
                            message = exceptionType + ": " + exception.getLocalizedMessage();
                        }
                        Log.e(TAG, message);
                        textView.setText(message);
                    }
                });
    }


    private void testMessage() {
        Toast.makeText(activity, "目前功能測試中，敬請期待。", Toast.LENGTH_SHORT).show();
    }

    private void workingMessage() {
        Toast.makeText(activity, "目前功能尚在維護中，敬請耐心等候。", Toast.LENGTH_SHORT).show();
    }


    private List<Map<String,String>> getUserlist() {
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> userMap = new HashMap<>();
        userMap.put("username","guest");
        userMap.put("password","guest");
        userMap.put("nickname","貴賓");
        list.add(userMap);
        userMap = new HashMap<>();
        userMap.put("username","VIPguest");
        userMap.put("password","VIPguest");
        userMap.put("nickname","VIP貴賓");
        list.add(userMap);
        return list;
    }
    // =========================================== Google =========================================== //

    private void signInGoogle() {
        Intent signInIntent = client.getSignInIntent();
        signInGoogleLauncher.launch(signInIntent); // 跳出Google登入畫面
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

    // 使用Google帳號完成Firebase驗證
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // get the unique ID for the Google account
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    String uid = task.getResult().getUser().getUid();
                    // 登入成功轉至下頁；失敗則顯示錯誤訊息
//                    Log.d(TAG, "user_UID： " + user_UID);
//                    Log.d(TAG, "memberUserclass getuser_UID： " + memberUserclass.getUid());
//                    Log.d(TAG, "user_UID： " + db.collection("MemberUsers").document(user_UID).get());
//                    Log.d(TAG, "MemberUsers.document： " + db.collection("MemberUsers").document().get());
//                    if (task.isSuccessful()) {
//                        FirebaseUser user_1 = auth.getCurrentUser();
//                        FirebaseUser user_2 = task.getResult().getUser();
////                        String user1_UID = user_1.getUid();
//                        if (user_2 != null) {
////                            String user2_UID = user_2.getUid();
//                            String user2_UID = user_2.getUid();
//                            this.memberUserclass.setUid(user2_UID);
//                            FirebaseFirestore.getInstance()
//                                    .collection("MemberUser").document(memberUserclass.getUid())
//                                    .set(memberUserclass).addOnCompleteListener(taskGoogleInsertDB -> {
//                                        if (taskGoogleInsertDB.isSuccessful()) {
//                                            Log.d(TAG,"taskGoogleInsertDB : Successful");
//                                        }
//                                    });
//                        }
                    if (task.isSuccessful()) {
//                        db.collection("MemberUsers").document(uid).get()
//                                .addOnCompleteListener(task1 -> {
//                                   MemberUser userstemp = task1.getResult().toObject(MemberUser.class);
//                                    for (DocumentSnapshot document : task1.getResult()) {
//                                        spots.add(document.toObject(Spot.class));
//                                    }
//                                   if (userstemp.getUsername() != null) {
//                                   }
//                                });

                        if (firebaseUser != null) {
                            MemberUser memberUser22 = new MemberUser();
                            memberUser22.setUid(uid);
                            FirebaseFirestore.getInstance()
                                    .collection("MemberUsers").document(memberUser22.getUid()).set(memberUser22)
                                    .addOnCompleteListener(taskGoogleInsertDB -> {
                                        if (taskGoogleInsertDB.isSuccessful()) {
                                            Log.d(TAG,"taskGoogleInsertDB : Successful");
                                        }
                                    });
                        }
                        checkUidName();
                    } else {
                        Exception exception = task.getException();
                        String message = exception == null ? "Sign in fail." : exception.getMessage();
//                        textView.setText(message);
                    }
                });
    }
    private void checkUidName() {
        FirebaseUser user_1 = auth.getCurrentUser();
        String user_UID = user_1.getUid();
        Log.d(TAG, "GMailuser_UID： " + user_UID );

        if (db.collection("MemberUsers").document(user_UID).get() != null ){
            bundle = new Bundle();
            bundle.putString("nickname","Google貴賓");
            NavController navController = Navigation.findNavController(textView);
            navController.navigate(R.id.action_login_to_result,bundle);
        }else {
            bundle = new Bundle();
            bundle.putString("nickname","Google貴賓");
            NavController navController = Navigation.findNavController(textView);
            navController.navigate(R.id.action_login_to_thirdResult,bundle);
        }

    }
    // =========================================== Google =========================================== //
    // =========================================== FaceBook =========================================== //
    // 跳出FB登入畫面
    private void signInFB() {
        Log.d(TAG, "LoginManager.getInstance().logInWithReadPermissions之前");
        LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess(): " + loginResult);
                signInFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel(): 我有經過onCancel我有經過onCancel");
                Log.d(TAG, "onCancel()");
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Log.d(TAG, "onError(): 我有經過onError我有經過onError");
                Log.e(TAG, "onError(): " + exception.getMessage());
            }
        });
    }

    // 使用FB token完成Firebase驗證
    private void signInFirebase(AccessToken token) {
        Log.d(TAG, "signInFirebase: " + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    // 登入成功轉至下頁；失敗則顯示錯誤訊息
                    if (task.isSuccessful()) {
                        bundle = new Bundle();
                        bundle.putString("nickname","FaceBook貴賓");
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
    private boolean isEmailOrPasswordEmpty(String email, String password) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            textView.setText(R.string.textEmailOrPasswordEmpty);
            return true;
        } else {
            return false;
        }
    }

    private void savePreferences(String key , String value) {
        sharedPreferences
                // 開始編輯
                .edit()
                // 寫出資料
                .putString( key, value)
                // 存檔
                .apply();

    }


    @Override
    public void onStart() {
        super.onStart();
        // 檢查user是否已經登入，是則FirebaseUser物件不為null
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            bundle = new Bundle();
            bundle.putString("nickname","貴賓");
            NavController navController = Navigation.findNavController(textView);
            navController.navigate(R.id.action_login_to_result,bundle);
        }
    }
}