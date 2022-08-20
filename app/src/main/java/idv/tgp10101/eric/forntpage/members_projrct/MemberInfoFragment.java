package idv.tgp10101.eric.forntpage.members_projrct;

import static android.app.Activity.RESULT_OK;
import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import idv.tgp10101.eric.Friend;
import idv.tgp10101.eric.MemberUser;
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
    private File file;
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private String useruid,username,useremail,userphone,useraddres,userlv,userlogin,
            userimage,userpassword,useronline,userpay,uservip,usertoken,useradmin;
    private MemberUser memberUser;
    private boolean pictureTaken;

    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::takePictureResult);

    ActivityResultLauncher<Intent> pickPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::pickPictureResult);

    ActivityResultLauncher<Intent> cropPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::cropPictureResult);

    private void takePictureResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            crop(contentUri);
        }
    }

    private void pickPictureResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                crop(result.getData().getData());
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        Intent cropIntent = UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .getIntent(requireContext());
        cropPictureLauncher.launch(cropIntent);
    }

    // 拍照或挑選照片完畢後都會截圖，截圖完畢會呼叫此方法
    private void cropPictureResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            cropImageUri = UCrop.getOutput(result.getData());
            if (cropImageUri != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(
                            requireContext().getContentResolver().openInputStream(cropImageUri));
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
                if (bitmap != null) {
                    iv_Memberinfo_Userimage.setImageBitmap(bitmap);
                    pictureTaken = true;
                } else {
                    iv_Memberinfo_Userimage.setImageResource(R.drawable.no_image);
                    pictureTaken = false;
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        bundle = getArguments();
        activity = getActivity();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        memberUser = new MemberUser();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        downinfo();
    }

    private void downinfo() {

        useruid = sharedPreferences.getString("會員UID","111");
        username = sharedPreferences.getString("會員名字","111");       // 已在下方欄位
        useremail =  sharedPreferences.getString("會員信箱","111");     // 已在下方欄位
        userphone = sharedPreferences.getString("會員手機號碼","111");   // 已在下方欄位
        useraddres =  sharedPreferences.getString("會員地址","111");    // 已在下方欄位
        userlv =  sharedPreferences.getString("會員等級","111");
        userlogin =  sharedPreferences.getString("會員登入類別","111");
        userimage =  sharedPreferences.getString("會員大頭照","111");    // 已在下方欄位
        userpassword =  sharedPreferences.getString("會員密碼","111");
        useronline =  sharedPreferences.getString("會員是否在線上","111");
        userpay =  sharedPreferences.getString("會員是否付費","111");
        uservip =   sharedPreferences.getString("VIP會員等級","111");
        usertoken =  sharedPreferences.getString("會員Token","111");
        useradmin =   sharedPreferences.getString("管理員代號","111");


        //會員登入類別 = Userloginclass ( 1 = Firebase註冊 , 2 = google , 3 = Facebook , 4 = Github , 5 = Apple 預設"0")
        memberUser.setUserloginclass(userlogin);
        //會員大頭照 = Userimage  預設"0"
        memberUser.setUserimage(userimage);
        //會員密碼 = Password
        memberUser.setPassword(userpassword);
        //會員是否在線上 = Online ( 1 = 線上 , 2 = 離線 ， 預設 2 )
        memberUser.setOnline( useronline);
        //會員等級 = Level ( 一般 = ordinary , 金 = gold , 白金 = platinum , 鑽石 = diamond ， 預設 ordinary )
        memberUser.setLevel(userlv);
        //會員是否付費 = Vippay ( 1 = 付費會員 , 2 = 未付費會員 ， 預設 2 )
        memberUser.setVippay(userpay);
        //VIP會員等級 = Viplevel ( 超級VIP會員 = svip , VIP會員 = vip , 一般會員 = novip  ， 預設 novip )
        memberUser.setViplevel(uservip);
        //會員Token = Usertoken ( 預設 "0" )
        memberUser.setUsertoken(usertoken);
        //管理員代號 = Adminclass ( 預設 "0"一般會員 "1"版主 "2"管理員 )
        memberUser.setAdminclass(useradmin);
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
        tv_Memberinfo_SetUid.setText(useruid);
        et_Memberinfo_Username.setText(username);
        et_Memberinfo_Email.setText(useremail);
        et_Memberinfo_Phone.setText(userphone);
        et_Memberinfo_Address.setText(useraddres);
        tv_Memberinfo_SetLevel.setText(userlv);

        if (memberUser.getUserimage() != null) {
            showImage(iv_Memberinfo_Userimage, memberUser.getUserimage());
        }
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
        iv_Memberinfo_Userimage.setOnClickListener( view -> {
            PopupMenu popupMenu = new PopupMenu(activity, view);
            // 載入選單資源檔
            popupMenu.inflate(R.menu.menu_picture_bottom);
            // 註冊/實作 選單監聽器
            popupMenu.setOnMenuItemClickListener(item -> {
                final int resId = item.getItemId();
                //選單 --- 拍照
                if (resId == R.id.it_takePic) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    if (dir != null && !dir.exists()) {
                        if (!dir.mkdirs()) {
                            Log.e(TAG, getString(R.string.textDirNotCreated));
                        }
                    }
                    file = new File(dir, "picture.jpg");
                    contentUri = FileProvider.getUriForFile(
                            requireContext(), requireContext().getPackageName() + ".fileProvider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    try {
                        takePictureLauncher.launch(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(requireContext(), R.string.textNoCameraAppFound,
                                Toast.LENGTH_SHORT).show();
                    }
                    //選單 --- 選擇照片
                } else if (resId == R.id.it_pickPic) {
                    try {
                        // 5. 實例化Intent物件
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickPictureLauncher.launch(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    //pickPicLauncher2.launch(intent); COPY file = createImageFile();
                }
                return true;
            });
            // 顯示彈出選單
            popupMenu.show();
//            getMemberinfo_Userimage();
        });
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
        final String id = db.collection("MemberUsers").document(sharedPreferences.getString("會員UID","111")).getId();
        memberUser.setUid(id);

        String name = et_Memberinfo_Username.getText().toString().trim();
        if (name.length() <= 0) {
            Toast.makeText(requireContext(), R.string.textNameIsInvalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = et_Memberinfo_Phone.getText().toString().trim();
        String address = et_Memberinfo_Address.getText().toString().trim();
        String email = et_Memberinfo_Email.getText().toString().trim();
        memberUser.setUsername(name);
        memberUser.setPhone(phone);
        memberUser.setAddress(address);
        memberUser.setEmail(email);
        savePreferences("會員大頭照" , memberUser.getUserimage() );
        savePreferences("會員名字" , memberUser.getUsername() );
        savePreferences("會員信箱" , memberUser.getEmail() );
        savePreferences("會員手機號碼" , memberUser.getPhone() );
        savePreferences("會員地址" , memberUser.getAddress() );

        // 如果有拍照，上傳至Firebase storage
        if (pictureTaken) {
            // document ID成為image path一部分，避免與其他圖檔名稱重複
            final String imagePath = getString(R.string.app_name) + "/images/"  + memberUser.getUsername() + "/" + memberUser.getUid();
            storage.getReference().child(imagePath).putFile(cropImageUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.textImageUploadSuccess));
                            // 圖檔新增成功再將圖檔路徑存入spot物件所代表的document內
                            memberUser.setUserimage(imagePath);
                        } else {
                            String message = task.getException() == null ?
                                    getString(R.string.textImageUploadFail) :
                                    task.getException().getMessage();
                            Log.e(TAG, "message: " + message);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        // 無論圖檔上傳成功或失敗都要將文字資料新增至DB
                        addOrReplaceA(memberUser);
                    });
        } else {
            addOrReplaceA(memberUser);
        }

        Toast.makeText(activity, "功能尚未完成：儲存修改後資料。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_SetLevel() {
        Toast.makeText(activity, "功能尚未完成：抓取FireBase會員等級。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_SetUid() {
        Toast.makeText(activity, "功能尚未完成：抓取FireBase會員UID。", Toast.LENGTH_SHORT).show();
    }

    private void getMemberinfo_Userimage() {
        // 實例化PopupMenu物件，並指定錨點元件

        Toast.makeText(activity, "功能尚未完成：設定大頭貼。", Toast.LENGTH_SHORT).show();
    }

    // 新增或修改Firestore上的景點
    private void addOrReplaceA(final MemberUser memberUser) {
        // 如果Firestore沒有該ID的Document就建立新的，已經有就更新內容
        db.collection("MemberUsers").document(memberUser.getUid()).set(memberUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message = getString(R.string.textInserted)
                                + " with ID: " + memberUser.getUid();
                        Log.d(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        // 新增完畢回上頁
                        Navigation.findNavController(iv_Memberinfo_Userimage).popBackStack();
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
    private void showImage(final ImageView imageView, final String path) {
        final int ONE_MEGABYTE = 1024 * 1024 ;
        StorageReference imageRef = storage.getReference().child(path);
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        byte[] bytes = task.getResult();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textImageDownloadFail) + ": " + path :
                                task.getException().getMessage() + ": " + path;
                        Log.e(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}