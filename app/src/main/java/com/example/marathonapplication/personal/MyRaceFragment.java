package com.example.marathonapplication.personal;

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
import com.example.marathonapplication.db.race.RaceDetail;
import com.example.marathonapplication.db.race.RaceDetailDao;
import com.example.marathonapplication.db.race.RaceInfo;
import com.example.marathonapplication.db.race.RaceInfoDao;
import com.example.marathonapplication.model.Config;
import com.example.marathonapplication.personal.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MyRaceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyRaceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyRaceFragment newInstance(int columnCount) {
        MyRaceFragment fragment = new MyRaceFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_race_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RaceDetailDao raceDetailDao = RaceDetailDao.getInstance(view.getContext());
            List<RaceDetail> raceDetails = raceDetailDao.listRaceDetail(Config.local_user_id);
            List<DummyContent.DummyItem> ITEMS = new ArrayList<>();
            for(RaceDetail raceDetail:raceDetails){
                ITEMS.add(new DummyContent.DummyItem(raceDetail.getId(),raceDetail.getRaceInfo().getRaceName(),raceDetail.getRaceInfo().getStartDate()));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(ITEMS));
        }
        return view;
    }
}