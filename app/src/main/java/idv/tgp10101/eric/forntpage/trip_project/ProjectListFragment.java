package idv.tgp10101.eric.forntpage.trip_project;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.Friend;
import idv.tgp10101.eric.R;
import idv.tgp10101.eric.Spot;
import idv.tgp10101.eric.forntpage.members_projrct.FriendListFragment;

public class ProjectListFragment extends Fragment {
    private static final String TAG = "TAG_ProjectList_";
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Bundle bundle;
    private Attractions attractions;
    private RecyclerView rv_Att_Project;
    private FloatingActionButton fab_Project_add;
    private SearchView att_SearchView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requireActivity().setTitle("專案列表");
        return inflater.inflate(R.layout.fragment_project_list, container, false);
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
        rv_Att_Project = view.findViewById(R.id.rv_Att_Project);
        fab_Project_add = view.findViewById(R.id.fab_Project_add);
        att_SearchView = view.findViewById(R.id.att_SearchView);

    }

    private void handleButton() {
        att_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        fab_Project_add.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_it_Project_to_it_Test01);
        });

    }

    private void handleRecyclerView() {
        rv_Att_Project.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    /** 取得所有景點資訊後顯示 */
    private void showAllAttractions() {
        db.collection("Attractions").get()
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
        ProjectListFragment.AttractionsAdapter attractionAdapter = (ProjectListFragment.AttractionsAdapter) rv_Att_Project.getAdapter();
        if (attractionAdapter == null) {
            attractionAdapter = new ProjectListFragment.AttractionsAdapter();
            rv_Att_Project.setAdapter(attractionAdapter);
        }
        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
        String queryStr = att_SearchView.getQuery().toString();
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
        public AttractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
            View itemView = layoutInflater.inflate(R.layout.cardview_att_picture_project, parent, false);
            return new AttractionsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AttractionsViewHolder ViewHolder, int position) {
            final Attractions attPic = att_list.get(position);
            String stempPic = attPic.getTakePic_PicList().get(0);
            Log.d(TAG,"stempPicstempPic： " + stempPic);
            attPic.setTakePic_Image(stempPic);
            if (attPic.getTakePic_Image() == null) {
                String stempPic2 = attPic.getTakePic_PicList().get(1);
                Log.d(TAG,"stempPicstempPic： " + stempPic2);
                attPic.setTakePic_Image(stempPic2);
                if (attPic.getTakePic_Image() == null) {
                    ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
                }
                showImage(ViewHolder.iv_Pictrue_Project, attPic.getTakePic_Image());
            } else {
//                Bitmap bitmap = null;
//                ViewHolder.iv_Pictrue_Project.setImageBitmap(bitmap);
                showImage(ViewHolder.iv_Pictrue_Project, attPic.getTakePic_Image());
//                ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
            }
            Log.d(TAG,"attPic： " + attPic);
            Log.d(TAG,"attPic.getTakePic_Name()： " + attPic.getTakePic_Name());
            ViewHolder.tv_Pictrue_Title.setText(attPic.getTakePic_Title());
            ViewHolder.tv_Pictrue_Des.setText(attPic.getTakePic_Des());
            rv_position = ViewHolder.getAdapterPosition();

            ViewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Attractions", attractions);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_it_Project_to_it_Test01,bundle);
            });
        }

        class AttractionsViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_Pictrue_Project;
            TextView tv_Pictrue_Title,tv_Pictrue_Des;
            public AttractionsViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_Pictrue_Title = itemView.findViewById(R.id.tv_Pictrue_Title);
                tv_Pictrue_Des = itemView.findViewById(R.id.tv_Pictrue_Des);
                iv_Pictrue_Project = itemView.findViewById(R.id.iv_Pictrue_Project);
            }
        }
    }


    // 下載Firebase storage的照片並顯示在ImageView上
    private void showImage(final ImageView imageView, final String path) {
        final int ONE_MEGABYTE = 1024 * 1024;
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