package idv.tgp10101.eric.forntpage.members_projrct;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import idv.tgp10101.eric.Friend;
import idv.tgp10101.eric.R;

public class FriendInsertFragment extends Fragment {
    private static final String TAG = "TAG_FriendInsert";
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView iv_insertFriend_image;
    private EditText et_insertFriend_Name, et_insertFriend_Phone, et_insertFriend_Address,et_insertFriend_Email;
    private Button bt_insertFriend_Cancel,bt_insertFriend_TakePicture,bt_insertFriend_PickPicture,bt_insertFriend_Ok;
    private File file;
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private Friend friend;


//    private Spot spot;
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
                    iv_insertFriend_image.setImageBitmap(bitmap);
                    pictureTaken = true;
                } else {
                    iv_insertFriend_image.setImageResource(R.drawable.no_image);
                    pictureTaken = false;
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        friend = new Friend();
//        spot = new Spot();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        // 將標題顯示
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }
        requireActivity().setTitle(R.string.textInsert);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_insert, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handletakePicture();
        handlepickPicture();
        handleButtom();
    }
    private void findViews(View view) {
        iv_insertFriend_image = view.findViewById(R.id.iv_insertFriend_image);
        et_insertFriend_Name = view.findViewById(R.id.et_insertFriend_Name);
        et_insertFriend_Phone = view.findViewById(R.id.et_insertFriend_Phone);
        et_insertFriend_Address = view.findViewById(R.id.et_insertFriend_Address);
        et_insertFriend_Email = view.findViewById(R.id.et_insertFriend_Email);
        bt_insertFriend_Cancel = view.findViewById(R.id.bt_insertFriend_Cancel);
        bt_insertFriend_PickPicture = view.findViewById(R.id.bt_insertFriend_PickPicture);
        bt_insertFriend_TakePicture = view.findViewById(R.id.bt_insertFriend_TakePicture);
        bt_insertFriend_Ok = view.findViewById(R.id.bt_insertFriend_Ok);
    }
    private void handlepickPicture() {
        bt_insertFriend_PickPicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPictureLauncher.launch(intent);
        });
    }
    private void handletakePicture() {
        bt_insertFriend_TakePicture.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (dir != null && !dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e(TAG, getString(R.string.textDirNotCreated));
                    return;
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
        });
    }
    private void handleButtom() {
        bt_insertFriend_Ok.setOnClickListener(v -> {
            // 先取得插入document的ID
            final String id = db.collection("Friends").document().getId();
            friend.setId(id);

            String name = et_insertFriend_Name.getText().toString().trim();
            if (name.length() <= 0) {
                Toast.makeText(requireContext(), R.string.textNameIsInvalid,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String phone = et_insertFriend_Phone.getText().toString().trim();
            String address = et_insertFriend_Address.getText().toString().trim();
            String email = et_insertFriend_Email.getText().toString().trim();

            friend.setName(name);
            friend.setPhone(phone);
            friend.setAddress(address);
            friend.setEmail(email);

            // 如果有拍照，上傳至Firebase storage
            if (pictureTaken) {
                // document ID成為image path一部分，避免與其他圖檔名稱重複
                final String imagePath = getString(R.string.app_name) + "/images/" + friend.getId();
                storage.getReference().child(imagePath).putFile(cropImageUri)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, getString(R.string.textImageUploadSuccess));
                                // 圖檔新增成功再將圖檔路徑存入spot物件所代表的document內
                                friend.setImagePath(imagePath);
                            } else {
                                String message = task.getException() == null ?
                                        getString(R.string.textImageUploadFail) :
                                        task.getException().getMessage();
                                Log.e(TAG, "message: " + message);
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            // 無論圖檔上傳成功或失敗都要將文字資料新增至DB
                            addOrReplaceA(friend);
                        });
            } else {
                addOrReplaceA(friend);
            }
        });

        bt_insertFriend_Cancel.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
    // 新增或修改Firestore上的景點
    private void addOrReplaceA(final Friend friend) {
        // 如果Firestore沒有該ID的Document就建立新的，已經有就更新內容
        db.collection("Friends").document(friend.getId()).set(friend)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message = getString(R.string.textInserted)
                                + " with ID: " + friend.getId();
                        Log.d(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        // 新增完畢回上頁
                        Navigation.findNavController(iv_insertFriend_image).popBackStack();
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textInsertFail) :
                                task.getException().getMessage();
                        Log.e(TAG, "message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}