package idv.tgp10101.eric.forntpage.trip_project;

import static idv.tgp10101.eric.util.Constants.PREFERENCES_FILE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.R;


public class SingleProjectListFragment extends Fragment {
    private static final String TAG = "TAG_ProjectList_";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Bundle bundle;
    private Attractions attractions;
    private RecyclerView rv_SingleProject_View;
    private FloatingActionButton fab_SingleProject_add;
    private SearchView sv_SingleProject_SearchView;
    private List<String> attList = new ArrayList<>();
    private List<Attractions> att_Project_List;
    private int rv_position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        att_Project_List = new ArrayList<>();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("專案列表(個人專區)");
        return inflater.inflate(R.layout.fragment_single_project_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
        handleRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllAttractions();
    }

    private void findViews(View view) {
        rv_SingleProject_View = view.findViewById(R.id.rv_SingleProject_View);
        fab_SingleProject_add = view.findViewById(R.id.fab_SingleProject_add);
        sv_SingleProject_SearchView = view.findViewById(R.id.sv_SingleProject_SearchView);

    }

    private void handleButton() {
        sv_SingleProject_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                showAttractions();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        fab_SingleProject_add.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_it_SingleProject_to_takePicture);
        });

    }

    private void handleRecyclerView() {
        rv_SingleProject_View.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    /** 取得所有景點資訊後顯示 */
    private void showAllAttractions() {
        String uid = sharedPreferences.getString("會員UID","null");
//        .whereEqualTo("","")
        db.collection("Attractions").whereEqualTo("takePic_WriterUid",uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // 先清除舊資料後再儲存新資料
                        if (!att_Project_List.isEmpty()) {
                            att_Project_List.clear();
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            att_Project_List.add(document.toObject(Attractions.class));
                        }
                        // 顯示景點
                        showAttractions();
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textNoSpotFound) :
                                task.getException().getMessage();
                        Log.e(TAG, "exception message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAttractions() {
        AttractionsAdapter attractionAdapter = (AttractionsAdapter) rv_SingleProject_View.getAdapter();
        if (attractionAdapter == null) {
            attractionAdapter = new AttractionsAdapter();
            rv_SingleProject_View.setAdapter(attractionAdapter);
        }
        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
        String queryStr = sv_SingleProject_SearchView.getQuery().toString();
        if (queryStr.isEmpty()) {
            attractionAdapter.setAtt_list(att_Project_List);
        } else {
            List<Attractions> searchAttractions = new ArrayList<>();
            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
            for (Attractions attraction : att_Project_List) {
                if (attraction.getTakePic_Title().toUpperCase().contains(queryStr.toUpperCase())) {
                    searchAttractions.add(attraction);
                }
            }
            attractionAdapter.setAtt_list(searchAttractions);
        }
        attractionAdapter.notifyDataSetChanged();
    }

    class AttractionsAdapter extends RecyclerView.Adapter<AttractionsAdapter.AttractionsViewHolder>{
        List<Attractions> att_list;

        AttractionsAdapter() {
            this.att_list = new ArrayList<>();
        }

        public void setAtt_list(List<Attractions> att_list) {
            this.att_list = att_list;
        }

        @Override
        public int getItemCount() {
            return att_list == null ? 0 : att_list.size();
        }
        @NonNull
        @Override
        public AttractionsAdapter.AttractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
            View itemView = layoutInflater.inflate(R.layout.cardview_att_picture_project, parent, false);
            return new AttractionsAdapter.AttractionsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AttractionsAdapter.AttractionsViewHolder ViewHolder, int position) {
            final Attractions attPic = att_list.get(position);
            if (position % 4 == 3) {
                ViewHolder.itemView.setBackgroundColor(getResources().getColor(com.facebook.login.R.color.com_facebook_blue));
            }else if (position % 4 == 2){
                ViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }else if(position % 4 == 1){
                ViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.purple_200));
            }else {
                ViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            }


            String stempPic = attPic.getTakePic_PicList().get(0);
            Log.d(TAG,"stempPicstempPic： " + stempPic);
            Log.d(TAG,"att_list.size()att_list.size()： " + att_list.size());
            attPic.setTakePic_Image(stempPic);


            if (attPic.getTakePic_Image() == null ) {
//                String stempPic2 = attPic.getTakePic_PicList().get(1);
//                Log.d(TAG,"stempPicstempPic： " + stempPic2);
//                attPic.setTakePic_Image(stempPic2);
//                if (attPic.getTakePic_Image() == null) {
                ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
//                }
//                showImage(ViewHolder.iv_Pictrue_Project, attPic.getTakePic_Image());
            } else {
//                Bitmap bitmap = null;
//                ViewHolder.iv_Pictrue_Project.setImageBitmap(bitmap);
                showImage(ViewHolder.iv_Pictrue_Project, attPic.getTakePic_Image());
//                ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
            }
            Log.d(TAG,"attPic： " + attPic);
            Log.d(TAG,"attPic.getTakePic_Name()： " + attPic.getTakePic_Title());
            ViewHolder.tv_Pictrue_Title.setText(attPic.getTakePic_Title());
            ViewHolder.tv_Pictrue_Des.setText(attPic.getTakePic_Des());
            ViewHolder.tv_Pictrue_UserName.setText(attPic.getTakePic_WriterName());
            rv_position = ViewHolder.getAdapterPosition();

            ViewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Attractions", attPic);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_it_SingleProject_to_update_TakePicture,bundle);
            });

            ViewHolder.itemView.setOnLongClickListener(v -> {
                // 刪除Firestore內的景點資料
                db.collection("Attractions").document(attPic.getTakePic_Title()).delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 刪除該景點在Firebase storage對應的圖檔
                                if (attPic.getTakePic_Title() != null) {

                                    String pathstemp = getString(R.string.app_name) + "/images/" + attPic.getTakePic_Title();
//                                    storage.getReference()  = gs://tgp101androidproject.appspot.com/
                                    Log.d(TAG,"storage.getReference()storage.getReference()： " + storage.getReference());
                                    storage.getReference().child(pathstemp).delete()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, getString(R.string.textImageDeleted));
                                                } else {
                                                    String message = task1.getException() == null ?
                                                            getString(R.string.textImageDeleteFailed) + ": " + attPic.getTakePic_Title() :
                                                            task1.getException().getMessage() + ": " + attPic.getTakePic_Title();
                                                    Log.e(TAG, message);
                                                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                Toast.makeText(requireContext(), R.string.textDeleted, Toast.LENGTH_SHORT).show();
                                // 有加上異動監聽器，會重新下載並指派給SpotListFragment.spots，而且會自動呼叫showSpots()
                                // 所以spots不需要移除被刪除的spot，也可以省略呼叫showSpots()
                                // spots.remove(spot);
                                // showSpots();
                            } else {
                                Toast.makeText(requireContext(), R.string.textDeleteFail, Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;
            });
        }

        class AttractionsViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_Pictrue_Project;
            TextView tv_Pictrue_Title,tv_Pictrue_Des,tv_Pictrue_UserName;
            public AttractionsViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_Pictrue_UserName = itemView.findViewById(R.id.tv_Pictrue_UserName);
                tv_Pictrue_Title = itemView.findViewById(R.id.tv_Pictrue_Title);
                tv_Pictrue_Des = itemView.findViewById(R.id.tv_Pictrue_Des);
                iv_Pictrue_Project = itemView.findViewById(R.id.iv_Pictrue_Project);
            }
        }
    }


    // 下載Firebase storage的照片並顯示在ImageView上
    private void showImage(final ImageView imageView, final String path) {
        final int ONE_MEGABYTE = 1024 * 1024*5;
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