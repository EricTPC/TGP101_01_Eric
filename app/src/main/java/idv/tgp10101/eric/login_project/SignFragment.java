package idv.tgp10101.eric.login_project;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import idv.tgp10101.eric.MemberUser;
import idv.tgp10101.eric.R;


public class SignFragment extends Fragment {
    private final static String TAG = "TAG_SignFragment";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Bundle bundle;
    private TextView textView,tv_Sign_Title,tv_Sign_Name,tv_Sign_Password,tv_Sign_Password2,tv_Sign_Phone,tv_Sign_Email;
    private EditText et_Sign_Title,et_Sign_Name,et_Sign_Password,et_Sign_Password2,et_Sign_Phone,et_Sign_Email;
    private Button bt_Sign_Ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        requireActivity().setTitle("註冊帳號");
        return inflater.inflate(R.layout.fragment_sign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
    }

    private void findViews(View view) {
        et_Sign_Name = view.findViewById(R.id.et_Sign_Name);
        et_Sign_Password = view.findViewById(R.id.et_Sign_Password);
//        et_Sign_Password2 = view.findViewById(R.id.et_Sign_Password2);
        et_Sign_Phone = view.findViewById(R.id.et_Sign_Phone);
        et_Sign_Email = view.findViewById(R.id.et_Sign_Email);
        bt_Sign_Ok = view.findViewById(R.id.bt_Sign_Ok);
    }
    private void handleButton() {

        bt_Sign_Ok.setOnClickListener( view -> {
            bundle = new Bundle();
            MemberUser memberUser = new MemberUser();
            //會員UID = Uid ( Firebase自動抓取 )

            //會員登入類別 = Userloginclass ( 1 = Firebase註冊 , 2 = google , 3 = Facebook , 4 = Github , 5 = Apple 預設"0")
            memberUser.setUserloginclass("0");
            //會員大頭照 = Userimage  預設"0"
            memberUser.setUserimage("0");
            //會員名字 = Username
            memberUser.setUsername(et_Sign_Name.getText().toString());
            //會員信箱 = Email
            memberUser.setEmail(et_Sign_Email.getText().toString());
            //會員密碼 = Password
            memberUser.setPassword(et_Sign_Password.getText().toString());
            //會員手機號碼 = Phone
            memberUser.setPhone(et_Sign_Phone.getText().toString());
            //會員地址 = Address ( 預設 請輸入您的地址 )
            memberUser.setAddress("請輸入您的地址");
            //會員是否在線上 = Online ( 1 = 線上 , 2 = 離線 ， 預設 2 )
            memberUser.setOnline("2");
            //會員等級 = Level ( 一般 = ordinary , 金 = gold , 白金 = platinum , 鑽石 = diamond ， 預設 ordinary )
            memberUser.setLevel("ordinary");
            //會員是否付費 = Vippay ( 1 = 付費會員 , 2 = 未付費會員 ， 預設 2 )
            memberUser.setVippay("2");
            //VIP會員等級 = Viplevel ( 超級VIP會員 = svip , VIP會員 = vip , 一般會員 = novip  ， 預設 novip )
            memberUser.setViplevel("novip");
            //會員Token = Usertoken ( 預設 "0" )
            memberUser.setUsertoken("0");


            Log.d(TAG,"會員UID： " + memberUser.getUid());
            Log.d(TAG,"會員登入類別： " + memberUser.getUserloginclass() );
            Log.d(TAG,"會員大頭照： " + memberUser.getUserimage() );
            Log.d(TAG,"會員名字： " + memberUser.getUsername() );
            Log.d(TAG,"會員信箱： " + memberUser.getEmail() );
            Log.d(TAG,"會員密碼： " + memberUser.getPassword() );
            Log.d(TAG,"會員手機號碼： " + memberUser.getPhone() );
            Log.d(TAG,"會員地址： " + memberUser.getAddress() );
            Log.d(TAG,"會員是否在線上： " + memberUser.getOnline() );
            Log.d(TAG,"會員等級： " + memberUser.getLevel() );
            Log.d(TAG,"會員是否付費： " + memberUser.getVippay() );
            Log.d(TAG,"VIP會員等級： " + memberUser.getViplevel() );
            Log.d(TAG,"會員Token： " + memberUser.getUsertoken() );

            signUp(memberUser);
//            bundle.putString("nickname",nickname);
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.action_sign_to_login,bundle);
        });
    }

    private void signUp(MemberUser memberUser) {
        if (isEmailOrPasswordEmpty(memberUser.getEmail(), memberUser.getPassword())) {
            return;
        }
        /* 利用user輸入的email與password建立新的帳號 */
        auth.createUserWithEmailAndPassword(memberUser.getEmail(), memberUser.getPassword())
                .addOnCompleteListener(taskCreateUser -> {
                    // 建立成功則轉至下頁；失敗則顯示錯誤訊息
                    if (taskCreateUser.isSuccessful()) {
                        FirebaseUser firebaseUser = taskCreateUser.getResult().getUser();
                        if (firebaseUser != null) {
                            String uid = taskCreateUser.getResult().getUser().getUid();
                            memberUser.setUid(uid);
                            FirebaseFirestore.getInstance()
                                    .collection("MemberUsers").document(memberUser.getUid())
                                    .set(memberUser).addOnCompleteListener(taskInsertDB -> {
                                        if (taskInsertDB.isSuccessful()) {
                                            Navigation.findNavController(et_Sign_Email)
                                                    .navigate(R.id.action_sign_to_login);
                                        }
                                    });
                        }
                    } else {
                        String message;
                        Exception exception = taskCreateUser.getException();
                        if (exception == null) {
                            message = "Register fail.";
                        } else {
                            String exceptionType;
                            // FirebaseAuthInvalidCredentialsException 代表帳號驗證不成功，例如email格式不正確
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                exceptionType = "Invalid Credential";
                            }
                            // FirebaseAuthInvalidUserException 代表無此user，例如帳密錯誤
                            else if (exception instanceof FirebaseAuthInvalidUserException) {
                                exceptionType = "Invalid User";
                            }
                            // FirebaseAuthUserCollisionException 代表此帳號已被使用
                            else if (exception instanceof FirebaseAuthUserCollisionException) {
                                exceptionType = "User Collision";
                            } else {
                                exceptionType = exception.getClass().toString();
                            }
                            message = exceptionType + ": " + exception.getLocalizedMessage();
                        }
                        Log.e(TAG, message);
//                        textView.setText(message);
                    }
                });
    }

    private boolean isEmailOrPasswordEmpty(String email, String password) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            textView.setText(R.string.textEmailOrPasswordEmpty);
            return true;
        } else {
            return false;
        }
    }

}