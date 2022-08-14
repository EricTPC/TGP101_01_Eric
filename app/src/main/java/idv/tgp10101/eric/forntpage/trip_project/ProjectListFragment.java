package idv.tgp10101.eric.forntpage.trip_project;

import android.app.Activity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.R;

public class ProjectListFragment extends Fragment {
    private static final String TAG = "TAG_ProjectListFragment";
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
    private Uri contentUri; // 拍照需要的Uri
    private Uri cropImageUri; // 截圖的Uri
    private boolean pictureTaken;
    private File file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        att_Project_List = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
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

    private void handleRecyclerView() {
        rv_Att_Project.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void showSpots() {
        AttractionsAdapter attractionsAdapter = (AttractionsAdapter) rv_Att_Project.getAdapter();
        if (attractionsAdapter == null) {
            attractionsAdapter = new AttractionsAdapter();
            rv_Att_Project.setAdapter(attractionsAdapter);
        }
        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
        String queryStr = att_SearchView.getQuery().toString();
        if (queryStr.isEmpty()) {
            attractionsAdapter.setAtt_list(att_Project_List);
        } else {
            List<Attractions> searchSpots = new ArrayList<>();
            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
            for (Attractions spot : att_Project_List) {
                if (spot.getTakePic_Name().toUpperCase().contains(queryStr.toUpperCase())) {
                    searchSpots.add(spot);
                }
            }
            attractionsAdapter.setAtt_list(searchSpots);
        }
        attractionsAdapter.notifyDataSetChanged();
    }


    private void handleButton() {
        att_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
//                showSpots();
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

    private void findViews(View view) {
        rv_Att_Project = view.findViewById(R.id.rv_Att_Project);
        fab_Project_add = view.findViewById(R.id.fab_Project_add);
        att_SearchView = view.findViewById(R.id.att_SearchView);

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
            if (attPic.getString_Image() == null) {
                ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
            } else {
//                Bitmap bitmap = null;
//                ViewHolder.iv_Pictrue_Project.setImageBitmap();
//                showImage(ViewHolder.iv_Pictrue_Project, attPic.getString_Image());
                ViewHolder.iv_Pictrue_Project.setImageResource(R.drawable.no_image);
            }
            Log.d(TAG,"attPic： " + attPic);
            Log.d(TAG,"attPic.getTakePic_Name()： " + attPic.getTakePic_Name());
            ViewHolder.tv_Pictrue_Title.setText(attPic.getTakePic_Name());
            ViewHolder.tv_Pictrue_Des.setText(attPic.getTakePic_Des());
        }

        class AttractionsViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_Pictrue_Project;
            TextView tv_Pictrue_Title,tv_Pictrue_Des;
            public AttractionsViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_Pictrue_Project = itemView.findViewById(R.id.iv_Pictrue_Project);
                tv_Pictrue_Title = itemView.findViewById(R.id.tv_Pictrue_Title);
                tv_Pictrue_Des = itemView.findViewById(R.id.tv_Pictrue_Des);

            }
        }
    }
}