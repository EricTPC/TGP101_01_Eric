package idv.tgp10101.eric.forntpage.members_projrct;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import idv.tgp10101.eric.R;


public class MemberInfoFragment extends Fragment {
    private static final String TAG = "TAG_MemberInfo_";
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private Bundle bundle;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView iv_Memberinfo_Userimage;
    private EditText et_Memberinfo_Username,et_Memberinfo_Email,et_Memberinfo_Password,et_Memberinfo_Phone;
    private EditText et_Memberinfo_Address;
    private TextView tv_Memberinfo_Uid,tv_Memberinfo_SetUid,tv_Memberinfo_Level,tv_Memberinfo_SetLevel;
    private TextView tv_Memberinfo_Username,tv_Memberinfo_Email,tv_Memberinfo_Password,tv_Memberinfo_Phone;
    private TextView tv_Memberinfo_Address;
    private Button bt_Memberinfo_Save,bt_Memberinfo_Back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        activity = getActivity();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        handleButton();
        showdata();
        Log.e(TAG, "savePreferences(\"會員名字\" , sharedPreferences );" + sharedPreferences.getString("會員名字","會員名字"));
    }

    private void showdata() {
        et_Memberinfo_Username.setText(sharedPreferences.getString("會員名字","111"));

        et_Memberinfo_Email.setText(sharedPreferences.getString("會員信箱","111"));
        et_Memberinfo_Phone.setText(sharedPreferences.getString("會員手機號碼","111"));
        et_Memberinfo_Address.setText(sharedPreferences.getString("會員地址","111"));
        tv_Memberinfo_SetUid.setText(sharedPreferences.getString("會員UID","111"));
        tv_Memberinfo_SetLevel.setText(sharedPreferences.getString("會員等級","111"));
    }

    private void findViews(View view) {
        iv_Memberinfo_Userimage = view.findViewById(R.id.iv_Memberinfo_Userimage);
        et_Memberinfo_Username = view.findViewById(R.id.et_Memberinfo_Username);
        et_Memberinfo_Email = view.findViewById(R.id.et_Memberinfo_Email);
        et_Memberinfo_Password = view.findViewById(R.id.et_Memberinfo_Password);
        et_Memberinfo_Phone = view.findViewById(R.id.et_Memberinfo_Phone);
        et_Memberinfo_Address = view.findViewById(R.id.et_Memberinfo_Address);
        tv_Memberinfo_SetUid = view.findViewById(R.id.tv_Memberinfo_SetUid);
        tv_Memberinfo_SetLevel = view.findViewById(R.id.tv_Memberinfo_SetLevel);
        bt_Memberinfo_Save = view.findViewById(R.id.bt_Memberinfo_Save);
        bt_Memberinfo_Back = view.findViewById(R.id.bt_Memberinfo_Back);
    }
    private void handleButton() {
        iv_Memberinfo_Userimage.setOnClickListener( view -> getMemberinfo_Userimage());
        tv_Memberinfo_SetUid.setOnClickListener( view -> getMemberinfo_SetUid());
        tv_Memberinfo_SetLevel.setOnClickListener( view -> getMemberinfo_SetLevel());
        bt_Memberinfo_Save.setOnClickListener( view -> getMemberinfo_Save());
        bt_Memberinfo_Back.setOnClickListener( view -> {
            bundle = new Bundle();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_memberInfo_to_it_Member,bundle);
            getMemberinfo_Back();
        });
    }

    private void getMemberinfo_Back() {

    }

    private void getMemberinfo_Save() {
        Toast.makeText(activity, "功能尚未完成：儲存修改後資料。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_SetLevel() {
        Toast.makeText(activity, "功能尚未完成：抓取FireBase會員等級。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_SetUid() {
        Toast.makeText(activity, "功能尚未完成：抓取FireBase會員UID。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_Userimage() {
        Toast.makeText(activity, "功能尚未完成：設定大頭貼。", Toast.LENGTH_SHORT).show();
    }


}