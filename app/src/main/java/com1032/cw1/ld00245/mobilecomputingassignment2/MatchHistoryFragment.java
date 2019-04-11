package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by PunLife on 30/04/2016.
 */
public class MatchHistoryFragment extends Fragment {
    private LocalSummoner ls;
    private Context context;

    public MatchHistoryFragment() {

    }

    public static MatchHistoryFragment newInstance(LocalSummoner summoner) {
        MatchHistoryFragment fragment = new MatchHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("localsummoner", summoner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matchhistorytab, container, false);
        ls = getArguments().getParcelable("localsummoner");
        ListView lv = (ListView) rootView.findViewById(R.id.matchlist);
        ArrayAdapter<LocalMatch> adapter = new MatchHistoryCustomAdapter(context, ls.getMatchList());
        lv.setAdapter(adapter);
        setRetainInstance(true);
        return rootView;
    }
}
