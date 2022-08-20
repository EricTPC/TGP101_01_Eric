package idv.tgp10101.eric.forntpage.members_projrct;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.Friend;
import idv.tgp10101.eric.R;
import idv.tgp10101.eric.Spot;


public class FriendListFragment extends Fragment {
    private static final String TAG = "TAG_FriendListFragment";
    private SharedPreferences sharedPreferences;
    private TextView textView;
    private RecyclerView rvSpots;
    private SearchView searchView;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ListenerRegistration registration;
    private List<Friend> friends;
//    private List<Spot> spots;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        friends = new ArrayList<>();
//        spots = new ArrayList<>();

        listenToSpots();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        // 將標題隱藏
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSpots = view.findViewById(R.id.rvSpots);
        rvSpots.setLayoutManager(new LinearLayoutManager(requireContext()));

        view.findViewById(R.id.btAdd).setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_friendList_to_friendInsert));

        searchView = view.findViewById(R.id.att_SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                showSpots();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }



    @Override
    //啟動 Fragment 時被回撥，此時Fragment可見，只是還沒有在前臺顯示，因此無法與使用者進行互動
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()");
        showAllSpots();

    }

    @Override
    //Fragment在前臺可見，處於活動狀態，使用者可與之互動
    public void onResume() {
        super.onResume();

    }
    @Override
    //Fragment處於暫停狀態，但依然可見，使用者不能與之互動
    public void onPause() {
        super.onPause();
    }
    @Override
    //停止Fragment回撥，Fragment完全不可見
    public void onStop() {
        super.onStop();
    }
    @Override
    //銷燬與Fragment有關的檢視，但未與Activity解除繫結
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    //與onAttach相對應，當Fragment與Activity關聯被取消時呼叫
    public void onDestroy() {
        super.onDestroy();
        // 解除異動監聽器
        Log.d(TAG,"// 解除異動監聽器// 解除異動監聽器");
        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }

    /** 取得所有景點資訊後顯示 */
    private void showAllSpots() {
        db.collection("Friends").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // 先清除舊資料後再儲存新資料
                        if (!friends.isEmpty()) {
                            friends.clear();
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            friends.add(document.toObject(Friend.class));
                        }
                        // 顯示景點
                        showSpots();
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textNoSpotFound) :
                                task.getException().getMessage();
                        Log.e(TAG, "exception message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSpots() {
        SpotAdapter spotAdapter = (SpotAdapter) rvSpots.getAdapter();
        if (spotAdapter == null) {
            spotAdapter = new SpotAdapter();
            rvSpots.setAdapter(spotAdapter);
        }
        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
        String queryStr = searchView.getQuery().toString();
        if (queryStr.isEmpty()) {
            spotAdapter.setSpots(friends);
        } else {
            List<Friend> searchFriends = new ArrayList<>();
            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
            for (Friend friend : friends) {
                if (friend.getName().toUpperCase().contains(queryStr.toUpperCase())) {
                    searchFriends.add(friend);
                }
            }
            spotAdapter.setSpots(searchFriends);
        }
        spotAdapter.notifyDataSetChanged();
    }

    private class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {
        List<Friend> friends;
//        List<Spot> spots;

        SpotAdapter() {
            this.friends = new ArrayList<>();
        }

        public void setSpots(List<Friend> friends) {
            this.friends = friends;
        }

        class SpotViewHolder extends RecyclerView.ViewHolder {
            ImageView ivSpot;
            TextView tvName, tvPhone, tvAddress, tvWeb;

            SpotViewHolder(View itemView) {
                super(itemView);
                ivSpot = itemView.findViewById(R.id.ivSpot);
                tvName = itemView.findViewById(R.id.tvName);
                tvPhone = itemView.findViewById(R.id.tvPhone);
                tvAddress = itemView.findViewById(R.id.tvAddress);
                tvWeb = itemView.findViewById(R.id.tvWeb);
            }
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        @NonNull
        @Override
        public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
            View itemView = layoutInflater.inflate(R.layout.cardview_friends_list, parent, false);
            return new SpotViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
            final Friend friend = friends.get(position);
            if (position % 4 == 3) {
                holder.itemView.setBackgroundColor(getResources().getColor(com.facebook.login.R.color.com_facebook_blue));
            }else if (position % 4 == 2){
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }else if(position % 4 == 1){
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.purple_200));
            }else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            }
            if (friend.getImagePath() == null) {
                holder.ivSpot.setImageResource(R.drawable.no_image);
            } else {
                showImage(holder.ivSpot, friend.getImagePath());
            }
            holder.tvName.setText(friend.getName());
            holder.tvPhone.setText(friend.getPhone());
            holder.tvAddress.setText(friend.getEmail());
            holder.tvWeb.setText(friend.getWeb());
            // 點選會開啟修改頁面
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("friend", friend);
                Navigation.findNavController(v)
                        .navigate(R.id.action_friendList_to_friendUpdate, bundle);
            });

            // 長按刪除資料
            holder.itemView.setOnLongClickListener(v -> {
                // 刪除Firestore內的景點資料
                db.collection("Friends").document(friend.getId()).delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 刪除該景點在Firebase storage對應的圖檔
                                if (friend.getImagePath() != null) {
                                    storage.getReference().child(friend.getImagePath()).delete()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, getString(R.string.textImageDeleted));
                                                } else {
                                                    String message = task1.getException() == null ?
                                                            getString(R.string.textImageDeleteFailed) + ": " + friend.getImagePath() :
                                                            task1.getException().getMessage() + ": " + friend.getImagePath();
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

    /**
     * 監聽資料是否發生異動，有則同步更新。
     * 開啟2台模擬器，一台新增/修改/刪除；另一台畫面會同步更新
     * 但自己做資料異動也會觸發監聽器
     */
    private void listenToSpots() {
        if (registration == null) {
            registration = db.collection("Friends").addSnapshotListener((snapshots, e) -> {
                Log.d(TAG, "event happened");
                if (e == null) {
                    List<Friend> friends = new ArrayList<>();
                    if (snapshots != null) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            Friend friend = dc.getDocument().toObject(Friend.class);
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "Added spot: " + friend.getName());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified spot: " + friend.getName());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed spot: " + friend.getName());
                                    break;
                                default:
                                    break;
                            }
                        }

                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            friends.add(document.toObject(Friend.class));
                        }
                        this.friends = friends;
                        showSpots();
                    }
                } else {
                    Log.e(TAG, e.getMessage(), e);
                }
            });
        }
    }
}