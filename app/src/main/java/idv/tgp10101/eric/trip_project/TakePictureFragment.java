package idv.tgp10101.eric.trip_project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.MainActivity;
import idv.tgp10101.eric.R;

public class TakePictureFragment extends Fragment {
    private Activity activity;
    private static final String TAG = "TAG_MainActivity";
    private Button button,bt_Save,bt_01,bt_02,bt_03;
    private TextView textView,tvTitlepc,tv_Datetime,tv_time;
    private ImageView imageView,ivPicture;
    private EditText editText,et_Searchpc,et_Des,et_Datetime;
    private ActivityResultLauncher<Uri> takePicLauncher;
    private ActivityResultLauncher<Intent> cropPicLauncher;
    private File dir;
    private File file;
    private Uri srcUri;
    private static final String FILENAME = "Attractions";
    private Handler mHandler;
    private ContentResolver contentResolver;
    private RecyclerView rv_Att_Pic;
    private List<Attractions> attList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_take_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        contentResolver = activity.getContentResolver();
        takePicLauncher = getTakePicLauncher();
        cropPicLauncher = getCropPicLauncher();

//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        tv_Datetime.setText( timeStamp );
        findViews(view);
        handleButton();
        handleRecyclerView();
        load();
    }
    private void findViews(View view) {
        bt_Save = view.findViewById(R.id.bt_Save);
        tvTitlepc = view.findViewById(R.id.tvTitlepc);
        tv_Datetime = view.findViewById(R.id.tv_Datetime);
        et_Searchpc = view.findViewById(R.id.et_Searchpc);
        et_Des = view.findViewById(R.id.et_Des);
        rv_Att_Pic = view.findViewById(R.id.rv_Att_Pic);
        bt_01 = view.findViewById(R.id.bt_01);
        bt_02 = view.findViewById(R.id.bt_02);
        bt_03 = view.findViewById(R.id.bt_03);
    }
    private ActivityResultLauncher<Uri> getTakePicLauncher() {
        return  registerForActivityResult(
                new ActivityResultContracts.TakePicture(), isOk -> {
                    if (isOk) {
                        crop();
                    }
                });
    }
    private void crop() {
        try {
            final Uri dstUri = Uri.fromFile(createImageFile());
            UCrop uCrop = UCrop.of(srcUri, dstUri);
            Intent intent = uCrop.getIntent(activity);
            cropPicLauncher.launch(intent);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }
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
                        ivPicture.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
    private void handleButton() {
        bt_Save.setOnClickListener(view ->save());
//        bt_01.setOnClickListener(view -> addgetAttList());
    }

    private void handleRecyclerView() {
        rv_Att_Pic.setAdapter(new TakePictureFragment.Myadapter(activity, getAttList()));
        rv_Att_Pic.setLayoutManager(new GridLayoutManager(activity, 2));
    }

    private List<Attractions> getAttList() {
        attList.add(new Attractions(R.drawable.ic_launcher_background));
        attList.add(new Attractions(R.drawable.ic_launcher_background));
        attList.add(new Attractions(R.drawable.ic_launcher_background));
        attList.add(new Attractions(R.drawable.ic_launcher_background));
        return attList;
    }

    private void save(){
        try (
                FileOutputStream fos = activity.openFileOutput(FILENAME,Activity.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            final String searchpc = String.valueOf(et_Searchpc.getText());
            final String des = String.valueOf(et_Des.getText());
            final String imagee = file.getAbsolutePath();
            final Attractions attractions = new Attractions(searchpc,des,imagee);
            oos.writeObject(attractions);
            Toast.makeText(activity, "檔案儲存成功", Toast.LENGTH_SHORT).show();
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
            et_Searchpc.setText(String.valueOf(attractions.getSearchpc()));
            et_Des.setText(String.valueOf(attractions.getDes()));
            File file = new File(attractions.getImageee());
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
            ivPicture.setImageBitmap(bitmap);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder>{
        Context context ;
        List<Attractions> list;
//        List<String> list;
        MainActivity mContext;

        public Myadapter(Context context, List<Attractions> list) {
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
            final Attractions att_PicList = list.get(position);
//            File file = new File(att_PicList);
//            Bitmap bitmap = null;
//            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
//                bitmap = BitmapFactory.decodeFile(file.getPath());
//            } else {
//                try {
//                    ImageDecoder.Source source = ImageDecoder.createSource(file);
//                    bitmap = ImageDecoder.decodeBitmap(source);
//                } catch (IOException e) {
//                    Log.e(TAG, e.toString());
//                }
//            }
//            ivPicture.setImageBitmap(bitmap);
//            ViewHolder.iv_Pic.setImageResource(attpic.getImageResId());
//            ViewHolder.iv_Pic.setImageResource( );
            ViewHolder.iv_Pic.setImageResource( att_PicList.getImageResId() );

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_Pic;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_Pic = itemView.findViewById(R.id.iv_Pic);
                itemView.setOnClickListener(view -> {
                    try {
                        file = createImageFile();
                        srcUri = FileProvider.getUriForFile(context,activity.getPackageName() + ".fileProvider",file);
                        takePicLauncher.launch(srcUri);
                        ivPicture = iv_Pic;
                    } catch (ActivityNotFoundException | IOException e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 6. 取得File物件，並設定路徑
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}