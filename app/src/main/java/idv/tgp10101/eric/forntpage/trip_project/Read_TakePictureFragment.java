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


public class Read_TakePictureFragment extends Fragment {
    private static final String TAG = "TAG_Read_TakePicture";
    private static final String FILENAME = "Attractions";
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Bundle bundle;
    private Button button,bt_RdTakePic_Back;
    private TextView textView,tv_RdTakePic_Datetime;
    private EditText editText,et_RdTakePic_Title,et_RdTakePic_Des,et_RdTakePic_WriteName;
    private RecyclerView rv_RdAtt_Pic;
    private Attractions attractions;

    private Resources resources;
    private List<String> tempList = new ArrayList<>();
    private List<String> attList = new ArrayList<>();
    private List<String> localattList = new ArrayList<>();



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read__take_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resources = getResources();
        findViews(view);
        handleButton();
//        load();
        if (getArguments() != null) {
            attractions = (Attractions) getArguments().getSerializable("Attractions");
            if (attractions != null) {
                et_RdTakePic_Title.setText(attractions.getTakePic_Title());
                et_RdTakePic_Des.setText(attractions.getTakePic_Des());
                et_RdTakePic_WriteName.setText(attractions.getTakePic_WriterName());
                this.attList = attractions.getTakePic_PicList();
                Log.d(TAG,"this.attList = attractions.getTakePic_PicList()： " + this.attList);
            }
        }
        handleRecyclerView();
    }

    //RecyclerView建立
    private void handleRecyclerView() {
        rv_RdAtt_Pic.setAdapter(new Myadapter(activity, attList));
        rv_RdAtt_Pic.setLayoutManager(new GridLayoutManager(activity, 2));
    }
    private void findViews(View view) {
        bt_RdTakePic_Back = view.findViewById(R.id.bt_RdTakePic_Back);
        tv_RdTakePic_Datetime = view.findViewById(R.id.tv_RdTakePic_Datetime);
        et_RdTakePic_Title = view.findViewById(R.id.et_RdTakePic_Title);
        et_RdTakePic_Des = view.findViewById(R.id.et_RdTakePic_Des);
        et_RdTakePic_WriteName = view.findViewById(R.id.et_RdTakePic_WriteName);
        rv_RdAtt_Pic = view.findViewById(R.id.rv_RdAtt_Pic);
    }
    private void handleButton() {
        bt_RdTakePic_Back.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_read_TakePicture_to_it_CloudProject);
        } );
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
        public Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_att_pictrue,parent,false);
            return new Myadapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Myadapter.MyViewHolder ViewHolder, int position) {
            final String att_PicList = list.get(position);
            //[點擊 RecyclerView - iv_Pic 元件 ]
            //判斷 RecyclerView - iv_Pic 元件 所在的List內照片是否存在。
            if (att_PicList != null) {
                final int ONE_MEGABYTE = 1024 * 1024 * 20;
                // ---------- 判別 Android 版本  (官方定義)  ---------- //
                StorageReference imageRef = storage.getReference().child(att_PicList);
                imageRef.getBytes(ONE_MEGABYTE)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                byte[] bytes = task.getResult();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ViewHolder.iv_Pic.setImageBitmap(bitmap);
                            } else {
//                        imageView.setImageResource(R.drawable.no_image);
                                String message = task.getException() == null ?
                                        getString(R.string.textImageDownloadFail) + ": " + att_PicList :
                                        task.getException().getMessage() + ": " + att_PicList;
                                Log.e(TAG, message);
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
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

}