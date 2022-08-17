package idv.tgp10101.eric.login_project;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.MainActivity;
import idv.tgp10101.eric.MainActivity2;
import idv.tgp10101.eric.MemberUser;
import idv.tgp10101.eric.R;


public class ResultFragment extends Fragment {
    private static final String TAG = "TAG_ResultFragment";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Button bt_SignOut_Google,bt_SignOut_Facebook;
    private TextView editText,tv_Result;
    private Bundle bundle;
    private TextView textView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private List<MemberUser> memberUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================================== Firebase =========================================== //
        bundle = getArguments();
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        activity = getActivity();
        memberUser = new ArrayList<>();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        addOrReplace();
        // =========================================== Firebase =========================================== //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        requireActivity().setTitle("進入頁面");
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviews(view);
        haneleTvResult();


    }

    private void haneleTvResult() {
        tv_Result.setText(" Welcome!! \n 敬愛的" + sharedPreferences.getString("會員名字",null) +"");
        tv_Result.setOnClickListener( view -> {
            Intent intent = new Intent();
            intent.setClass(activity, MainActivity2.class);
            startActivity(intent);
            activity.finish();
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.action_result_to_takePicture,bundle);
        });
        bt_SignOut_Google.setOnClickListener( view -> {signOut_Google();});
        bt_SignOut_Facebook.setOnClickListener( view -> {signOut_Facebook();});

    }

    private void findviews(View view) {
        bt_SignOut_Google = view.findViewById(R.id.bt_SignOut_Google);
        bt_SignOut_Facebook = view.findViewById(R.id.bt_SignOut_Facebook);
        tv_Result = view.findViewById(R.id.tv_Result);
        textView = view.findViewById(R.id.textView);

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
//         登出Firebase帳號
        auth.signOut();
//         登出FB帳號
        LoginManager.getInstance().logOut();
        Navigation.findNavController(textView).popBackStack();
        Log.d(TAG, "Signed out");
    }

    private void addOrReplace() {
        FirebaseUser user_1 = auth.getCurrentUser();
        String user_UID = user_1.getUid();
        db.collection("MemberUsers").document(user_UID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        MemberUser loginUser = task.getResult().toObject(MemberUser.class);
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
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textInsertFail) :
                                task.getException().getMessage();
                        Log.e(TAG, "message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
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
//    @Override
//    public void onStart() {
//        super.onStart();
//        // 檢查user是否已經登入，是則FirebaseUser物件不為null
//        FirebaseUser user = auth.getCurrentUser();
//        if (user == null) {
//            Navigation.findNavController(textView).popBackStack();
//        } else {
//            String text = "Firebase UID: " + user.getUid();
//            textView.setText(text);
//        }
//    }

}