package idv.tgp10101.eric.forntpage.members_projrct;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import idv.tgp10101.eric.R;


public class MemberListFragment extends Fragment {
    private Activity activity;
    private ImageView iv_Person,iv_MemberInfo,iv_Friends,iv_Support,iv_Setting,iv_LogOut,iv_VipProject,iv_MemberClass;
    private TextView tv_MemberClass,tv_MemberName,tv_MemberInfo,tv_Friends,tv_Support,tv_Setting,tv_LogOut,tv_VipProject;
    private Bundle bundle;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        requireActivity().setTitle("會員專區");
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
    }

    private void handleButton() {
        iv_Person.setOnClickListener(view -> get_iv_Person());
        iv_MemberInfo.setOnClickListener(view -> get_iv_MemberInfo());
        iv_Friends.setOnClickListener(view -> {
            bundle = new Bundle();
//            bundle.putString("nickname",nickname);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_it_Member_to_friendList,bundle);
            get_iv_Friends();
        });
        iv_Support.setOnClickListener(view -> get_iv_Support());
        iv_Setting.setOnClickListener(view -> get_iv_Setting());
        iv_VipProject.setOnClickListener(view -> get_iv_VipProject());
        iv_LogOut.setOnClickListener(view -> get_iv_LogOut());
    }




    private void findViews(View view) {

        iv_Person = view.findViewById(R.id.iv_Person);

        tv_MemberClass = view.findViewById(R.id.tv_MemberClass);
        tv_MemberName = view.findViewById(R.id.tv_MemberName);
        iv_MemberClass = view.findViewById(R.id.iv_MemberClass);

        iv_MemberInfo = view.findViewById(R.id. iv_MemberInfo);
        tv_MemberInfo = view.findViewById(R.id.tv_MemberInfo);

        iv_Friends = view.findViewById(R.id.iv_Friends);
        tv_Friends = view.findViewById(R.id.tv_Friends);

        iv_Support = view.findViewById(R.id.iv_Support);
        tv_Support = view.findViewById(R.id.tv_Support);

        iv_Setting = view.findViewById(R.id.iv_Setting);
        tv_Setting = view.findViewById(R.id. tv_Setting);

        iv_VipProject = view.findViewById(R.id.iv_VipProject);
        tv_VipProject = view.findViewById(R.id.tv_VipProject);

        iv_LogOut = view.findViewById(R.id.iv_LogOut);
        tv_LogOut = view.findViewById(R.id.tv_LogOut);
    }

    private void get_iv_Person() {
        Toast.makeText(activity, "功能尚未完成：更換大頭照。", Toast.LENGTH_SHORT).show();
    }
    private void get_iv_MemberInfo() {
        Toast.makeText(activity, "功能尚未完成：個人資料編輯。", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_LogOut() {
        Toast.makeText(activity, "功能尚未完成：登出。", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Setting() {
        Toast.makeText(activity, "功能尚未完成：設定資料。", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Support() {
        Toast.makeText(activity, "功能尚未完成：轉至客服問答。", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_VipProject() {
        Toast.makeText(activity, "功能尚未完成：VIP專案。", Toast.LENGTH_SHORT).show();
    }

    private void get_iv_Friends() {
        Toast.makeText(activity, "功能尚未完成：好友名單編輯。", Toast.LENGTH_SHORT).show();
    }

}