package idv.tgp10101.eric.forntpage.trip_project;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import idv.tgp10101.eric.Attractions;
import idv.tgp10101.eric.R;

public class ProjectListFragment extends Fragment {
    private static final String TAG = "TAG_ProjectListFragment";
    private Activity activity;
    private Attractions attractions;
    private RecyclerView rv_Att_Project;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        rv_Att_Project.setAdapter(new ProjectListFragment.MyAdapter(activity, getAttList()));
//        rv_Att_Project.setLayoutManager(new GridLayoutManager(activity, 2));

    }


    private void handleButton() {

    }

    private void findViews(View view) {


    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        Context context;
        List<Attractions> list;

        public MyAdapter(Context context, List<Attractions> list) {
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
            View itemView = layoutInflater.inflate(R.layout.cardview_att_picture_project, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder ViewHolder, int position) {
//            ViewHolder.tv_Pictrue_Title.setText(attractions.getSearchpc());
//            ViewHolder.tv_Pictrue_Time.setText(attractions.getDes());
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_Pictrue_Project;
            TextView tv_Pictrue_Title,tv_Pictrue_Time;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_Pictrue_Project = itemView.findViewById(R.id.iv_Pictrue_Project);
                tv_Pictrue_Title = itemView.findViewById(R.id.tv_Pictrue_Title);
                tv_Pictrue_Time = itemView.findViewById(R.id.tv_Pictrue_Time);

            }
        }
    }
}