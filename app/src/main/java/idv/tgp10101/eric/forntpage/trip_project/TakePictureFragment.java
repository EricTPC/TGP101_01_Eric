package idv.tgp10101.eric.forntpage.trip_project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.R;

public class TakePictureFragment extends Fragment {
    private static final String TAG = "TAG_TakePictureFragment";
    private static final String FILENAME = "Attractions";
    private Activity activity;
    private Button button,bt_Save,bt_TakePc_NewPc,bt_02,bt_03;
    private TextView textView,tv_TakePc_Datetime;
    private ImageView imageView,ivPicture;
    private EditText editText,et_TakePc_Name,et_TakePc_Des;
    private ActivityResultLauncher<Uri> takePicLauncher;
    private ActivityResultLauncher<Intent> cropPicLauncher;
    private ActivityResultLauncher<Intent> pickPicLauncher;
    private File file;
    private Uri srcUri;
    private Attractions attractions;
    private Handler mHandler;
    private ContentResolver contentResolver;
    private RecyclerView rv_Att_Pic;
    private Resources resources;
    private List<String> attList = new ArrayList<>();
    private int rv_position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        requireActivity().setTitle("拍照頁面");
        return inflater.inflate(R.layout.fragment_take_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentResolver = activity.getContentResolver();
        takePicLauncher = getTakePicLauncher();
        cropPicLauncher = getCropPicLauncher();
        pickPicLauncher = getPickPicLauncher();
        resources = getResources();
        findViews(view);
        handleButton();
        load();
        handleRecyclerView();


    }



    private void findViews(View view) {
        bt_Save = view.findViewById(R.id.bt_TakePc_Save);
        tv_TakePc_Datetime = view.findViewById(R.id.tv_TakePc_Datetime);
        et_TakePc_Name = view.findViewById(R.id.et_TakePc_Name);
        et_TakePc_Des = view.findViewById(R.id.et_TakePc_Des);
        rv_Att_Pic = view.findViewById(R.id.rv_Att_Pic);
        bt_TakePc_NewPc = view.findViewById(R.id.bt_TakePc_NewPc);
        bt_02 = view.findViewById(R.id.bt_02);
        bt_03 = view.findViewById(R.id.bt_03);
    }
    //選擇相簿內照片
    private ActivityResultLauncher<Intent> getPickPicLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), activityResult -> {
                    /** 4. 取得圖像 **/
                    if (activityResult.getResultCode() != Activity.RESULT_OK) {
                        return;
                    }
                    try {
                        // 4.1 取得Uri物件
                        Uri uri = activityResult.getData().getData();


                        Bitmap bitmap = null;
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
                            // Android 9-
                            // 4.2 取得InputStream物件
                            InputStream is = activity.getContentResolver().openInputStream(uri);
                            // 4.3 取得Bitmap物件
                            bitmap = BitmapFactory.decodeStream(is);
                        } else {
                            // Android 9(+
                            // 	4.2 從Uri物件建立ImageDecoder.Source物件
                            ImageDecoder.Source source = ImageDecoder.createSource(
                                    activity.getContentResolver(),
                                    uri);
                            // 4.3 取得Bitmap物件
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }

                        Log.d(TAG,"rv_position:  " + rv_position);
                        Log.d(TAG,"file.getAbsolutePath():  " + file.getAbsolutePath());

                        //{ position : filePath }
                        attList.set(rv_position,file.getAbsolutePath());
                        ivPicture.setImageBitmap(bitmap);
                        Log.d(TAG,"attList： " + attList);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
        );
    }
    //拍照
    private ActivityResultLauncher<Uri> getTakePicLauncher() {
        return  registerForActivityResult(
                new ActivityResultContracts.TakePicture(), isOk -> {
                    if (isOk) {
                        crop();}
                });
    }

    private void crop() {
        try {
            final Uri dstUri = Uri.fromFile(createImageFile());
            UCrop uCrop = UCrop.of(srcUri, dstUri);
            Intent intent = uCrop.getIntent(activity);
            cropPicLauncher.launch(intent);
//            getCropPicLauncher(position).launch(intent);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }
    //裁切
    private ActivityResultLauncher<Intent> getCropPicLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if (activityResult == null || activityResult.getResultCode() != Activity.RESULT_OK) {
                        return;
                    }
                    try {
                        Intent intent = activityResult.getData();
                        if (intent == null) {
                            return;
                        }
                        Uri resultUri = UCrop.getOutput(intent);

                        Bitmap bitmap = null;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(resultUri));
                        } else {
                            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, resultUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }
//                        bitmap > file
//                        { position : filePath }
                        Log.d(TAG,"rv_position： " + rv_position);
                        Log.d(TAG,"file.getAbsolutePath()： " + file.getAbsolutePath());
                        Log.d(TAG,"bitmap： " + bitmap);

                        //{ position : filePath }
                        attList.set(rv_position,file.getAbsolutePath());
                        ivPicture.setImageBitmap(bitmap);
                        Log.d(TAG,"attList： " + attList);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void handleButton() {
        bt_Save.setOnClickListener(view -> save() );

        // 新增 RecyclerView 內 iv_Pic 的 屠格
        bt_TakePc_NewPc.setOnClickListener( view -> {
            addlist();
            rv_Att_Pic.setAdapter(new TakePictureFragment.Myadapter(activity, attList));
        });
    }
    //RecyclerView建立
    private void handleRecyclerView() {
        getAttList();
        rv_Att_Pic.setAdapter(new TakePictureFragment.Myadapter(activity, attList));
        rv_Att_Pic.setLayoutManager(new GridLayoutManager(activity, 2));
    }

    private void addlist() {
        attList.add(null);
    }

    private List<String> getAttList() {
        attList.add(null);
        attList.add(null);
        attList.add(null);
        attList.add(null);
        return attList;
    }
    //存檔
    private void save(){
        try (
                FileOutputStream fos = activity.openFileOutput(FILENAME,Activity.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            //儲存 標題 文字
            final String searchpc = String.valueOf(et_TakePc_Name.getText());
            //儲存 描述 文字
            final String des = String.valueOf(et_TakePc_Des.getText());
            //儲存 拍照 or 選擇相簿的照片
            //存檔  MAP{position:filePath}儲存////////////
            final List<String> imageList = attList;
//            final String image = file.getAbsolutePath();
            final Attractions attractions = new Attractions(searchpc,des,imageList);
            oos.writeObject(attractions);
            Toast.makeText(activity, "檔案儲存成功", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"imageList： " + imageList);
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
            et_TakePc_Name.setText(String.valueOf(attractions.getSearchpc()));
            et_TakePc_Des.setText(String.valueOf(attractions.getDes()));
//            MAP{position:filePath}  轉成LIST

            File file = new File(String.valueOf(attractions.getPicList()));
            Bitmap bitmap = null;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
                bitmap = BitmapFactory.decodeFile(file.getPath());//tostring
            } else {
                try {
                    ImageDecoder.Source source = ImageDecoder.createSource(file);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            ivPicture.setImageBitmap(bitmap);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder>{
        Context context ;
//        List<Attractions> list;
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
                            file = createImageFile();
                            srcUri = FileProvider.getUriForFile(context,activity.getPackageName() + ".fileProvider",file);
                            // 6. 執行
                            takePicLauncher.launch(srcUri);
                            ivPicture = ViewHolder.iv_Pic;
                            rv_position = position;
                        } catch (ActivityNotFoundException | IOException e) {
                            Log.e(TAG, e.toString());
                        }
                    //選單 --- 選擇照片
                    } else if (resId == R.id.it_pickPic) {
                        try {
                            file = createImageFile();
                            // 5. 實例化Intent物件
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            // 6. 執行
                            pickPicLauncher.launch(intent);
                            ivPicture = ViewHolder.iv_Pic;
                            rv_position = position;
                        } catch (ActivityNotFoundException  | IOException e) {
                            Log.e(TAG, e.toString());
                        }
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