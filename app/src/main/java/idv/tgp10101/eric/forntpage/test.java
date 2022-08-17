package idv.tgp10101.eric.forntpage;

import static android.app.Activity.RESULT_OK;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import idv.tgp10101.eric.MemberUser;
import idv.tgp10101.eric.R;

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


//    private void showAllMemberUsers() {
//        db.collection("MemberUsers").get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        // 先清除舊資料後再儲存新資料
//                        Log.d(TAG,"task.getResult()： " + task.getResult());
//
//                        if (!memberUser.isEmpty()) {
//                            memberUser.clear();
//                        }
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            memberUser.add(document.toObject(MemberUser.class));
////                            MemberUser memberUser = document.getDocumentReference(MemberUser.class);
////                            MemberUser memberUser2 = document.getDocument().toObject(MemberUser.class);
////                            Log.d(TAG,"document.getData().keySet()： " + document.getData().keySet());
////                            Log.d(TAG,"document.getData().values()： " + document.getData().values());
////                            Log.d(TAG,"document.toObject(MemberUser.class))： " + document.toObject(MemberUser.class));
////                            Log.d(TAG,"memberUser： " + memberUser);
////                            Log.d(TAG,"memberUser： " + memberUser);
////                            Log.d(TAG,"memberUser2： " + memberUser1);
////                            Log.d(TAG,"document.getId()： " + document.getId());
////                            Log.d(TAG,"document.getData()： " + document.getData());
////                            paser_uid = (String) document.getData().get("uid");          //取得會員 會員UID
////                            paser_userloginclass = (String) document.getData().get("userloginclass");          //取得會員 會員登入類別
////                            paser_online = (String) document.getData().get("online");       //取得會員 會員是否在線上
////                            paser_userimage = (String) document.getData().get("userimage");    //取得會員 會員大頭照路徑
////                            paser_username = (String) document.getData().get("username");     //取得會員 會員名稱
////                            paser_password = (String) document.getData().get("password");     //取得會員 會員密碼
////                            paser_phone = (String) document.getData().get("phone");        //取得會員 會員電話
////                            paser_email = (String) document.getData().get("email");        //取得會員 會員信箱
////                            paser_address = (String) document.getData().get("address");      //取得會員 會員地址
////                            paser_level = (String) document.getData().get("level");        //取得會員 會員等級
////                            paser_vippay = (String) document.getData().get("vippay");       //取得會員 會員是否有VIP專案
////                            paser_viplevel = (String) document.getData().get("viplevel");     //取得會員 VIP等級
////                            paser_usertoken = (String) document.getData().get("usertoken");    //取得會員Token
////                            savePreferences("會員UID", paser_uid);
////                            savePreferences("會員登入類別" , paser_userloginclass );
////                            savePreferences("會員大頭照" , paser_userimage );
////                            savePreferences("會員名字" , paser_username );
////                            savePreferences("會員信箱" , paser_email );
////                            savePreferences("會員密碼" , paser_password );
////                            savePreferences("會員手機號碼" , paser_phone );
////                            savePreferences("會員地址" , paser_address );
////                            savePreferences("會員是否在線上" , paser_online );
////                            savePreferences("會員等級" , paser_level );
////                            savePreferences("會員是否付費" , paser_vippay );
////                            savePreferences("VIP會員等級" , paser_viplevel );
////                            savePreferences("會員Token" , paser_usertoken );
////                            Log.d(TAG,"document.getData().get(\"1111\")： " + paser_username );
////                            Log.d(TAG,"會員UID： " + document.getData().get("uid"));
////                            Log.d(TAG,"會員UID： " + sharedPreferences.getString("uid","000"));
////                            Log.d(TAG,"會員登入類別： " + document.getData().get("userloginclass") );
////                            Log.d(TAG,"會員大頭照： " + document.getData().get("userimage") );
////                            Log.d(TAG,"會員名字： " + document.getData().get("username") );
////                            Log.d(TAG,"會員信箱： " + document.getData().get("email") );
////                            Log.d(TAG,"會員密碼： " + document.getData().get("password") );
////                            Log.d(TAG,"會員手機號碼： " + document.getData().get("phone") );
////                            Log.d(TAG,"會員地址： " + document.getData().get("address") );
////                            Log.d(TAG,"會員是否在線上： " + document.getData().get("online") );
////                            Log.d(TAG,"會員等級： " + document.getData().get("level") );
////                            Log.d(TAG,"會員是否付費： " + document.getData().get("vippay") );
////                            Log.d(TAG,"VIP會員等級： " + document.getData().get("viplevel") );
////                            Log.d(TAG,"會員Token： " + document.getData().get("usertoken") );
//                        }
//
//                    } else {
//                        String message = task.getException() == null ?
//                                getString(R.string.textNoSpotFound) :
//                                task.getException().getMessage();
//                        Log.e(TAG, "exception message: " + message);
//                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//        Log.d(TAG,"會員UID： " + sharedPreferences.getString("uid","000"));
//        Log.d(TAG,"會員會員會員會員會員： " );
//    }

}
