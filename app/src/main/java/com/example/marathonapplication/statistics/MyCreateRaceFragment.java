package com.example.marathonapplication.statistics;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marathonapplication.R;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.db.race.RaceInfoDao;
import com.example.marathonapplication.model.Config;
import com.example.marathonapplication.statistics.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MyCreateRaceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyCreateRaceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyCreateRaceFragment newInstance(int columnCount) {
        MyCreateRaceFragment fragment = new MyCreateRaceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_create_race_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RaceInfoDao raceInfoDao = RaceInfoDao.getInstance(view.getContext());
            List<RaceInfo> raceInfos = raceInfoDao.listRaceInfo(Config.local_user_id);
            List<DummyContent.DummyItem> ITEMS = new ArrayList<DummyContent.DummyItem>();
            for(RaceInfo raceInfo:raceInfos){
                ITEMS.add(new DummyContent.DummyItem(raceInfo.getId(),raceInfo.getRaceName()+"(" +raceInfo.getStartDate()+"-"+raceInfo.getEndDate()+")",raceInfo.getStartDate()));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(ITEMS));
        }
        return view;
    }
}