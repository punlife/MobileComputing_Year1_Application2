package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by PunLife on 18/05/2016.
 */
public class ChampionCustomAdapter extends ArrayAdapter<LocalChampion> {
    private List<LocalChampion> localChampionList;
    DecimalFormat df = new DecimalFormat(".00");

    public ChampionCustomAdapter(Context context, List<LocalChampion> data) {
        super(context, R.layout.championrowlayout, data);
        localChampionList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.championrowlayout, parent, false);

        TextView champnumber = (TextView) view.findViewById(R.id.number);
        TextView champname = (TextView) view.findViewById(R.id.name);
        TextView winrate = (TextView) view.findViewById(R.id.winrate);
        TextView win = (TextView) view.findViewById(R.id.win);
        TextView kda = (TextView) view.findViewById(R.id.kda);
        TextView ratio = (TextView) view.findViewById(R.id.ratio);
        TextView cs = (TextView) view.findViewById(R.id.cs);
        champnumber.setText("" + (position + 1));
        champname.setText(localChampionList.get(position).getChampionName());
        Resources res = getContext().getResources();
        if (Integer.parseInt(localChampionList.get(position).getWinrate().replace("%", "")) > 50) {
            winrate.setTextColor(res.getColor(R.color.win));
        } else if (Integer.parseInt(localChampionList.get(position).getWinrate().replace("%", "")) < 50) {
            winrate.setTextColor(res.getColor(R.color.loss));
        }
        winrate.setText(localChampionList.get(position).getWinrate());
        win.setText(localChampionList.get(position).getGamesplayed());
        String[] temp = localChampionList.get(position).getKda().split("/");
        Double k = Double.parseDouble(temp[0]);
        Double d = Double.parseDouble(temp[1]);
        Double a = Double.parseDouble(temp[2]);
        Double calculatedKDA = (k + a) / d;
        kda.setText(localChampionList.get(position).getKda());
        ratio.setText(df.format(calculatedKDA) + ":1");
        cs.setText(localChampionList.get(position).getAveragecs());

        return view;
    }
}
