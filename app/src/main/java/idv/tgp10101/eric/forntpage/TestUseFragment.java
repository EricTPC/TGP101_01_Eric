package idv.tgp10101.eric.forntpage;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import idv.tgp10101.eric.R;


public class TestUseFragment extends Fragment {
    private static final String TAG = "TAG_TestUseFragment";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView imageView,ivPicture;
    private File file;
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private boolean pictureTaken;
    private Activity activity;
    private ActivityResultLauncher<Intent> takePicLauncher2;
    private ActivityResultLauncher<Intent> cropPicLauncher2;
    private ActivityResultLauncher<Intent> pickPicLauncher2;
    private ContentResolver contentResolver;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_use, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        contentResolver = activity.getContentResolver();
        takePicLauncher2 = getTakePicLauncher2();
        cropPicLauncher2 = getCropPicLauncher2();
        pickPicLauncher2 = getPickPicLauncher2();

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
    private void takePictureResult2(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            crop2(contentUri);
        }
    }
    private void pickPictureResult2(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
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
                    bitmap = BitmapFactory.decodeStream(
                            requireContext().getContentResolver().openInputStream(cropImageUri));
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
                if (bitmap != null) {
                    ivPicture.setImageBitmap(bitmap);
                    pictureTaken = true;
                } else {
                    ivPicture.setImageResource(R.drawable.no_image);
                    pictureTaken = false;
                }
            }
        }
    }

}