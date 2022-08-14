package idv.tgp10101.eric.forntpage;

import static android.app.Activity.RESULT_OK;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class test {
    private ActivityResultLauncher<Uri> takePicLauncher;
    private ActivityResultLauncher<Intent> cropPicLauncher;
    private ActivityResultLauncher<Intent> pickPicLauncher;
    private ContentResolver contentResolver;

    // =========================================== 登入按鈕 =========================================== //
//        bt_Login.setOnClickListener( view -> {
//            final String username = String.valueOf(et_Username.getText());
//            final String password = String.valueOf(et_Password.getText());
//            String nickname = null;
//            for (Map<String,String> userMap:getUserlist()) {
//                final String uStr = userMap.get("username");
//                final String pStr = userMap.get("password");
//                if(Objects.equals(username,uStr) && Objects.equals(password,pStr))  {
//                    nickname = userMap.get("nickname");
//                }
//            }
//            if(nickname == null) {
//                Toast.makeText(activity, "帳號或密碼錯誤，請查閱底下使用說明。", Toast.LENGTH_SHORT).show();
//            }else {
//                bundle = new Bundle();
//                bundle.putString("nickname",nickname);
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.action_login_to_result,bundle);
//            }
//        });
    // =========================================== 登入按鈕 =========================================== //


//    contentResolver = activity.getContentResolver();
//        takePicLauncher = getTakePicLauncher();
//        cropPicLauncher = getCropPicLauncher();
//        pickPicLauncher = getPickPicLauncher();

//    //選擇相簿內照片
//    private ActivityResultLauncher<Intent> getPickPicLauncher() {
//        return registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), activityResult -> {
//                    /** 4. 取得圖像 **/
//                    if (activityResult.getResultCode() != Activity.RESULT_OK) {
//                        return;
//                    }
//                    try {
//                        // 4.1 取得Uri物件
//                        Uri uri = activityResult.getData().getData();
//
//
//                        Bitmap bitmap = null;
//                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
//                            // Android 9-
//                            // 4.2 取得InputStream物件
//                            InputStream is = activity.getContentResolver().openInputStream(uri);
//                            // 4.3 取得Bitmap物件
//                            bitmap = BitmapFactory.decodeStream(is);
//                        } else {
//                            // Android 9(+
//                            // 	4.2 從Uri物件建立ImageDecoder.Source物件
//                            ImageDecoder.Source source = ImageDecoder.createSource(
//                                    activity.getContentResolver(),
//                                    uri);
//                            // 4.3 取得Bitmap物件
//                            bitmap = ImageDecoder.decodeBitmap(source);
//                        }
//
//                        Log.d(TAG,"rv_position:  " + rv_position);
//                        Log.d(TAG,"file.getAbsolutePath():  " + file.getAbsolutePath());
//
//                        //{ position : filePath }
//                        attList.set(rv_position,file.getAbsolutePath());
//                        ivPicture.setImageBitmap(bitmap);
//                        Log.d(TAG,"attList： " + attList);
//                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
//                    }
//                }
//        );
//    }
//    //拍照
//    private ActivityResultLauncher<Uri> getTakePicLauncher() {
//        return  registerForActivityResult(
//                new ActivityResultContracts.TakePicture(), isOk -> {
//                    if (isOk) {
//                        crop();}
//                });
//    }
//    //裁切
//    private void crop() {
//        try {
//            final Uri dstUri = Uri.fromFile(createImageFile());
//            UCrop uCrop = UCrop.of(srcUri, dstUri);
//            Intent intent = uCrop.getIntent(activity);
//            cropPicLauncher.launch(intent);
//        } catch (IOException e) {
//            Log.d(TAG, e.toString());
//        }
//    }
//    //裁切
//    private ActivityResultLauncher<Intent> getCropPicLauncher() {
//        return registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                activityResult -> {
//                    if (activityResult == null || activityResult.getResultCode() != Activity.RESULT_OK) {
//                        return;
//                    }
//                    try {
//                        Intent intent = activityResult.getData();
//                        if (intent == null) {
//                            return;
//                        }
//                        Uri resultUri = UCrop.getOutput(intent);
//
//                        Bitmap bitmap = null;
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
//                            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(resultUri));
//                        } else {
//                            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, resultUri);
//                            bitmap = ImageDecoder.decodeBitmap(source);
//                        }
////                        bitmap > file
////                        { position : filePath }
//                        Log.d(TAG,"rv_position： " + rv_position);
//                        Log.d(TAG,"file.getAbsolutePath()： " + file.getAbsolutePath());
//                        Log.d(TAG,"bitmap： " + bitmap);
//
//                        //{ position : filePath }
//                        attList.set(rv_position,file.getAbsolutePath());
//                        ivPicture.setImageBitmap(bitmap);
//                        Log.d(TAG,"attList： " + attList);
//                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
//                    }
//                });
//    }


}
