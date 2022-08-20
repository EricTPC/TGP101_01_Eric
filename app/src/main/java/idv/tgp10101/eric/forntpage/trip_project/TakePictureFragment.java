package idv.tgp10101.eric.forntpage.trip_project;

import static android.app.Activity.RESULT_OK;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.R;

public class TakePictureFragment extends Fragment {
    private static final String TAG = "TAG_TakePicture";
    private static final String FILENAME = "Attractions";
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Bundle bundle;
    private Button button,bt_Save,bt_TakePic_NewPc,bt_02,bt_03;
    private TextView textView,tv_TakePic_Datetime;
    private ImageView imageView,ivPicture;
    private EditText editText,et_TakePic_Name,et_TakePic_Des;
    private CheckBox cb_TakePic_Share;
    private ActivityResultLauncher<Intent> takePicLauncher2;
    private ActivityResultLauncher<Intent> cropPicLauncher2;
    private ActivityResultLauncher<Intent> pickPicLauncher2;
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private boolean pictureTaken;
    private File file,cropImageUritoFile;
    private Attractions attractions;
    private Handler mHandler;
    private RecyclerView rv_Att_Pic;
    private Resources resources;
    private List<String> tempList = new ArrayList<>();
    private List<String> attList = new ArrayList<>();
    private List<String> localattList = new ArrayList<>();
    private int rv_position;
    private String temp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        attractions = new Attractions();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTitle("拍照頁面");
        return inflater.inflate(R.layout.fragment_take_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        takePicLauncher2 = getTakePicLauncher2();
        cropPicLauncher2 = getCropPicLauncher2();
        pickPicLauncher2 = getPickPicLauncher2();
        resources = getResources();
        findViews(view);
        handleButton();
        load();
        handleRecyclerView();
    }

    private ActivityResultLauncher<Intent> getPickPicLauncher2() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::pickPictureResult2);
    }
    private ActivityResultLauncher<Intent> getTakePicLauncher2() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::takePictureResult2);
    }
    private ActivityResultLauncher<Intent> getCropPicLauncher2() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::cropPictureResult2);
    }
    //拍照
    private void takePictureResult2(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Log.d(TAG,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + contentUri);
            crop2(contentUri);
        }
    }
    //選相簿
    private void pickPictureResult2(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                contentUri = result.getData().getData();
                Log.d(TAG,"CCCCCCCCCCCCCCCCCCCCCCCCCC" + contentUri.toString());
                crop2(result.getData().getData());
            }
        }
    }
    private void crop2(Uri sourceImageUri) {
        File file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);

        Intent cropIntent = UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .getIntent(requireContext());
        cropPicLauncher2.launch(cropIntent);
    }
    // 拍照或挑選照片完畢後都會截圖，截圖完畢會呼叫此方法
    private void cropPictureResult2(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            cropImageUri = UCrop.getOutput(result.getData());
            if (cropImageUri != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(cropImageUri));
                    cropImageUritoFile = new File(new URI(cropImageUri.toString()));
                    copyPicture(cropImageUritoFile,file);
                } catch (IOException | URISyntaxException e) {
                    Log.e(TAG, e.toString());
                }
                if (bitmap != null) {
                    attList.set(rv_position,file.getAbsolutePath());
                    ivPicture.setImageBitmap(bitmap);
                    pictureTaken = true;
                } else {
                    ivPicture.setImageResource(R.drawable.no_image);
                    pictureTaken = false;
                }
            }
        }
    }


    private void findViews(View view) {
        bt_Save = view.findViewById(R.id.bt_TakePic_Save);
        tv_TakePic_Datetime = view.findViewById(R.id.tv_TakePic_Datetime);
        et_TakePic_Name = view.findViewById(R.id.et_TakePic_Name);
        et_TakePic_Des = view.findViewById(R.id.et_TakePic_Des);
        rv_Att_Pic = view.findViewById(R.id.rv_Att_Pic);
        bt_TakePic_NewPc = view.findViewById(R.id.bt_TakePic_NewPic);
        bt_02 = view.findViewById(R.id.bt_02);
        cb_TakePic_Share = view.findViewById(R.id.cb_TakePic_Share);
    }
    private void handleButton() {
        bt_Save.setOnClickListener(view -> cloudsave() );
        bt_02.setOnClickListener(view -> save() );
        // 新增 RecyclerView 內 iv_Pic 的 圖格
        bt_TakePic_NewPc.setOnClickListener( view -> {
            addlist();
            rv_Att_Pic.setAdapter(new Myadapter(activity, attList));
        });
    }
    //RecyclerView建立
    private void handleRecyclerView() {
//        getAttList();
        rv_Att_Pic.setAdapter(new Myadapter(activity, attList));
        rv_Att_Pic.setLayoutManager(new GridLayoutManager(activity, 2));
    }

    private void addlist() {
        attList.add(null);
    }
    //        attList.add(id);
    // 新增 setimagelist(attList)
    //        attractions.setTakePic_PicList(attList); // 完畢  物件要存至雲端資料庫
    private void cloudsave(){
        localattList = attList;
        //儲存 標題 文字
        final String takePic_Name = String.valueOf(et_TakePic_Name.getText());
        attractions.setTakePic_Title(takePic_Name);
        Log.d(TAG, "attractions.getTakePic_Title()：　"+ attractions.getTakePic_Title()) ;

        //儲存 描述 文字
        final String takePic_des = String.valueOf(et_TakePic_Des.getText());
        attractions.setTakePic_Des(takePic_des);
        Log.d(TAG, "attractions.getTakePic_Des()：　"+ attractions.getTakePic_Des()) ;

        //儲存 作者名字和UID
        final String takePic_WriterName = sharedPreferences.getString("會員名字","查無此會員");
        final String takePic_WriterUid = sharedPreferences.getString("會員UID","查無此會員UID");
        attractions.setTakePic_WriterUid(takePic_WriterUid);
        attractions.setTakePic_WriterName(takePic_WriterName);
        Log.d(TAG, "attractions.getTakePic_Des()：　"+ attractions.getTakePic_WriterName()) ;
        Log.d(TAG, "attractions.getTakePic_Des()：　"+ attractions.getTakePic_WriterUid()) ;

        //儲存 拍照 or 選擇相簿的照片 ( 雲端的路徑)
        final List<String> picList = attList;
        attractions.setTakePic_PicList(picList);
        Log.d(TAG, "attractions.getTakePic_PicList()：　"+ attractions.getTakePic_PicList()) ;

        //儲存 拍照 or 選擇相簿的照片 (本機的路徑)
        final List<String> localPicList = localattList;
        attractions.setTakePic_LocalPicList(localPicList);
        Log.d(TAG, "attractions.getTakePic_PicList()：　"+ attractions.getTakePic_LocalPicList()) ;
        //儲存 UID
        final String id = db.collection("Attractions").document().getId();
        Log.d(TAG, "id：　" + id) ;
        attractions.setTakePic_Uid(id);
        Log.d(TAG, "attractions.getTakePic_Uid()：　"+ attractions.getTakePic_Uid()) ;
        //建立Storage的圖片儲存路徑 TGP101_01_Eric/ images / 專案名稱(TakePic_Title) / UID.Jpg
        final String imagePath = getString(R.string.app_name) + "/images/" + attractions.getTakePic_Title() +"/"+ attractions.getTakePic_Uid();
       //        imageRef.putFile(uri); // 非同步上傳指定Uri的檔案
//        imageRef.putStream(inputStream); // 非同步上傳InputStream資料
//        imageRef.putByte(byteArray); // 非同步上傳byte[]資料，⼤檔案不建議
        Log.d(TAG, "messag_attList: " + attList);
        int count = 0;
        int index = 0;
        for(String attraction : picList) {
            Log.d(TAG, "message_attraction:  " + " 取用第 " + count + " 個 " + attraction + " 。 ");
            if ( attraction == null ){
                tempList.add(null);
                continue;
            }
            Log.d(TAG, "message_attraction : " + "目前是第" + count + "個" + attraction + "。");
            File uriFile = new File(attraction);
            String imagePath_count = imagePath + count;
            int finalCount = count;
            Log.d(TAG, "finalCount : " + finalCount +"。");
            storage.getReference().child(imagePath_count).putFile(Uri.fromFile(uriFile))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.textImageUploadSuccess));
                            if ( finalCount < tempList.size()){
                                tempList.set(finalCount,imagePath_count);
                                Log.d(TAG, " tempList.set : " + "目前是第" + finalCount + "個" + tempList + "。");
                            }else {
                                tempList.add(imagePath_count);
                            }
                            // 圖檔新增成功再將圖檔路徑存入attractions物件所代表的document內
//                            tempList.add(imagePath_count);
//                            attractions.setTakePic_PicList(count-1,imagePath);
//                            Log.e(TAG, "message: " + tempList);
                        } else {
                            String message = task.getException() == null ?
                                    getString(R.string.textImageUploadFail) :
                                    task.getException().getMessage();
                            Log.e(TAG, "message: " + message);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
//                        // 無論圖檔上傳成功或失敗都要將文字資料新增至DB
                        Log.d(TAG, "tempListtempListtempListtempList : " + tempList);
                        attractions.setTakePic_PicList(tempList);
                        addOrReplaceA(attractions);
                    });
//            tempList.set(count,imagePath_count);
//            Log.e(TAG, "message: " + tempList);
//            attractions.setTakePic_PicList(tempList);
//            Log.d(TAG, "tempListtempListtempListtempList : " + tempList);
//            addOrReplaceA(attractions);
            count += 1;
        }  //FOR迴圈結束
//            if (pictureTaken) {
//                // document ID成為image path一部分，避免與其他圖檔名稱重複
//            }else {
//                addOrReplaceA(attractions);
//            }
        addOrReplaceA(attractions);
        NavController navController = Navigation.findNavController(bt_02);
        navController.navigate(R.id.action_takePicture_to_it_SingleProject);
    }

    private void addOrReplaceA(final Attractions attractions) {
//         如果Firestore沒有該ID的Document就建立新的，已經有就更新內容
        db.collection("Attractions").document(attractions.getTakePic_Title()).set(attractions)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message = getString(R.string.textInserted)
                                + " with ID: " + attractions.getTakePic_Uid();
                        Log.d(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        // 新增完畢回上頁
//                        Navigation.findNavController(et_TakePic_Name).popBackStack();
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textInsertFail) :
                                task.getException().getMessage();
                        Log.e(TAG, "message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //存檔
    private void save(){
        try (
                FileOutputStream fos = activity.openFileOutput(FILENAME,Activity.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            //儲存 標題 文字
            final String takePic_Title = String.valueOf(et_TakePic_Name.getText());
            //儲存 描述 文字
            final String takePic_des = String.valueOf(et_TakePic_Des.getText());

            //儲存 拍照 or 選擇相簿的照片
            final List<String> takePic_PicList = attList;
            final String TakePic_Image = takePic_PicList.get(0);
            final Attractions attractions = new Attractions(takePic_Title,takePic_des,takePic_PicList,TakePic_Image);
            oos.writeObject(attractions);
            Toast.makeText(activity, "檔案儲存成功", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"imageList： " + takePic_PicList);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void load(){
        try (
                FileInputStream fis = activity.openFileInput(FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
             ){
            final Attractions attractions = (Attractions) ois.readObject();
            et_TakePic_Name.setText(String.valueOf(attractions.getTakePic_Title()));
            et_TakePic_Des.setText(String.valueOf(attractions.getTakePic_Des()));
//            MAP{position:filePath}  轉成LIST
            // List 讀取?????
            attList = attractions.getTakePic_PicList();
//            attList = attractions.getTakePic_LocalPicList();
            File file = new File(String.valueOf(attractions.getTakePic_PicList()));
            Log.d(TAG,"filefilefilefile： " + file);
//
//            Bitmap bitmap = null;
//            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
//                bitmap = BitmapFactory.decodeFile(file.getPath());//tostring
//            } else {
//                try {
//                    ImageDecoder.Source source = ImageDecoder.createSource(file);
//                    bitmap = ImageDecoder.decodeBitmap(source);
//                } catch (IOException e) {
//                    Log.e(TAG, e.toString());
//                }
//            }
//            ivPicture.setImageBitmap(bitmap);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void copyPicture(File from , File arrive){
        try (
                FileInputStream fis = new FileInputStream(from);
                BufferedInputStream bis = new BufferedInputStream(fis);
                FileOutputStream fos = new FileOutputStream(arrive);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            byte[] buffer = new byte[bis.available()];
            while (bis.read(buffer) != -1) {
                bos.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder>{
        Context context ;
        List<String> list;
        public Myadapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }
        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_att_pictrue,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder ViewHolder, int position) {
            final String att_PicList = list.get(position);
            //[點擊 RecyclerView - iv_Pic 元件 ]
            ViewHolder.iv_Pic.setOnClickListener(view -> {
                // 實例化PopupMenu物件，並指定錨點元件
                PopupMenu popupMenu = new PopupMenu(activity, view);
                // 載入選單資源檔
                popupMenu.inflate(R.menu.menu_picture_bottom);
                // 註冊/實作 選單監聽器
                popupMenu.setOnMenuItemClickListener(item -> {
                    final int resId = item.getItemId();
                    //選單 --- 拍照
                    if (resId == R.id.it_takePic) {
                        try {
                            // 5. 實例化Intent物件
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            file = createImageFile();
                            contentUri = FileProvider.getUriForFile(requireContext(),requireContext().getPackageName() + ".fileProvider",file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            // 6. 執行
                            takePicLauncher2.launch(intent);
                            ivPicture = ViewHolder.iv_Pic;
                            rv_position = ViewHolder.getAdapterPosition();
                        } catch (ActivityNotFoundException | IOException e) {
                            Log.e(TAG, e.toString());
                        }
                    //選單 --- 選擇照片
                    } else if (resId == R.id.it_pickPic) {
                        try {
                            // 5. 實例化Intent物件
                            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            file = createImageFile();
                            // 6. 執行
                            pickPicLauncher2.launch(intent);
                            //COPY
                            ivPicture = ViewHolder.iv_Pic;
                            rv_position = ViewHolder.getAdapterPosition();
                        } catch (ActivityNotFoundException | IOException e) {
                            Log.e(TAG, e.toString());
                        }
                        //pickPicLauncher2.launch(intent); COPY file = createImageFile();
                    }
                    return true;
                });
                // 顯示彈出選單
                popupMenu.show();
            });

            ViewHolder.iv_Pic.setOnLongClickListener(view -> {
                this.list.remove(position);
                notifyDataSetChanged();
                return false;
            });


            Log.d(TAG,"att_PicList： " + att_PicList);
            //判斷 RecyclerView - iv_Pic 元件 所在的List內照片是否存在。
            if (att_PicList != null) {
                File file = new File(att_PicList);
                // ---------- 判別 Android 版本  (官方定義)  ---------- //
                Bitmap bitmap = null;
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
                    bitmap = BitmapFactory.decodeFile(file.getPath());
                } else {
                    try {
                        ImageDecoder.Source source = ImageDecoder.createSource(file);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                // ---------- 判別 Android 版本  (官方定義)  ---------- //
                ViewHolder.iv_Pic.setImageBitmap(bitmap);
            }else {
                ViewHolder.iv_Pic.setImageResource(R.drawable.add_image);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_Pic;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_Pic = itemView.findViewById(R.id.iv_Pic);
            }
        }
    }

    // 下載Firebase storage的照片並顯示在ImageView上
    private void showImage(final ImageView imageView, final String path) {
        final int ONE_MEGABYTE = 1024 * 1024;
        Log.d(TAG,"pathpathpath： " + path);
        Log.d(TAG,"imageRefimageRef： " + storage.getReference().child(path));
        StorageReference imageRef = storage.getReference().child(path);

        imageRef.getBytes(ONE_MEGABYTE)
                .addOnCompleteListener(task -> {
                    Log.d(TAG,"task.getResult()： " + task.getResult());
                    if (task.isSuccessful() && task.getResult() != null) {
                        byte[] bytes = task.getResult();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Log.d(TAG,"bitmapbitmapbitmapbitmapbitmap： " + bitmap);

                        this.ivPicture = imageView;
                        ivPicture.setImageBitmap(bitmap);
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textImageDownloadFail) + ": " + path :
                                task.getException().getMessage() + ": " + path;
                        Log.e(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //儲存在 /sdcard/Android/data/idv.tgp10101.eric/files/Pictures
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 6. 取得File物件，並設定路徑
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}




//            ViewHolder.iv_Pic.setImageResource(attpic.getImageResId());
//            ViewHolder.iv_Pic.setImageResource( );
//            ViewHolder.iv_Pic.setImageResource( att_PicList.getImageResId() );