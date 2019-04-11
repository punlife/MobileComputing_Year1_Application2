package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robrua.orianna.type.core.match.Match;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PunLife on 18/05/2016.
 */
public class MatchHistoryCustomAdapter extends ArrayAdapter<LocalMatch> {
    private List<LocalMatch> matchList;
    Date timeConversion;
    Date dateConversion;
    DateFormat timeFormatter = new SimpleDateFormat("mm:ss");
    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    String timeFormatted;
    String dateFormatted;

    public MatchHistoryCustomAdapter(Context context, List<LocalMatch> data) {
        super(context, R.layout.mastchhistoryrowlayout, data);
        matchList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.mastchhistoryrowlayout, parent, false);

        LinearLayout match = (LinearLayout) view.findViewById(R.id.match);
        LinearLayout matchtopbar = (LinearLayout) view.findViewById(R.id.matchtopbar);
        //topbar
        TextView queue = (TextView) view.findViewById(R.id.queuetype);
        TextView mode = (TextView) view.findViewById(R.id.gameType);
        TextView outcome = (TextView) view.findViewById(R.id.outcome);
        //bottombar
        TextView championname = (TextView) view.findViewById(R.id.championName);
        TextView kda = (TextView) view.findViewById(R.id.score);
        TextView matchlevel = (TextView) view.findViewById(R.id.matchlevel);
        TextView creepscore = (TextView) view.findViewById(R.id.creepscore);
        TextView length = (TextView) view.findViewById(R.id.length);
        TextView date = (TextView) view.findViewById(R.id.date);
        if (matchList.get(position).getQueueType().equals("TEAM_BUILDER_DRAFT_RANKED_5x5")) {
            queue.setText("Ranked");
        } else {
            queue.setText(matchList.get(position).getQueueType());
        }
        mode.setText(matchList.get(position).getGameMode());
        Resources res = getContext().getResources();
        if (matchList.get(position).getOutcome().equals("Victory")) {
            match.setBackgroundColor(res.getColor(R.color.win));
            matchtopbar.setBackgroundColor(res.getColor(R.color.wintop));
        } else {
            match.setBackgroundColor(res.getColor(R.color.loss));
            matchtopbar.setBackgroundColor(res.getColor(R.color.losstop));
        }
        outcome.setText(matchList.get(position).getOutcome());
        championname.setText(matchList.get(position).getChampionName());
        kda.setText(matchList.get(position).getKda());
        matchlevel.setText("Level:" + matchList.get(position).getLevel());
        creepscore.setText("CS: " + matchList.get(position).getCs());
        timeConversion = new Date(matchList.get(position).getLength() * 1000);
        timeFormatted = timeFormatter.format(timeConversion);
        length.setText(timeFormatted);
        dateConversion = new Date(matchList.get(position).getDate());
        dateFormatted = dateFormatter.format(dateConversion);
        date.setText(dateFormatted);

        return view;
    }
}
