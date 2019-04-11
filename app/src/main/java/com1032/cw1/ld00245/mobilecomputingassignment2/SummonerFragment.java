package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robrua.orianna.type.core.common.Tier;

import java.io.InputStream;

/**
 * Created by PunLife on 30/04/2016.
 */
public class SummonerFragment extends Fragment {
    private LocalSummoner ls;

    public SummonerFragment() {

    }

    public static SummonerFragment newInstance(LocalSummoner summoner) {
        SummonerFragment fragment = new SummonerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("localsummoner", summoner);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ls = getArguments().getParcelable("localsummoner");
        View rootView = inflater.inflate(R.layout.summonertab, container, false);
        ImageView summonericon = (ImageView) rootView.findViewById(R.id.imageView3);
        ImageView summonerrankicon = (ImageView) rootView.findViewById(R.id.imageView4);
        TextView summonername = (TextView) rootView.findViewById(R.id.nameText);
        TextView summonerregion = (TextView) rootView.findViewById(R.id.regionText);
        TextView summonerlevel = (TextView) rootView.findViewById(R.id.levelText);
        TextView summonerrank = (TextView) rootView.findViewById(R.id.summonerrank);
        TextView summonerlp = (TextView) rootView.findViewById(R.id.currentlp);
        TextView masteryscore = (TextView) rootView.findViewById(R.id.champmasteryevelText);
        TextView winrate = (TextView) rootView.findViewById(R.id.summonerwinrate);

        summonericon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        new DownloadImageTask(summonericon).execute(ls.getProfileicon());
        summonername.setText(ls.getName());
        summonerregion.setText(ls.getRegion());
        summonerlevel.setText("Level: " + ls.getLevel());
        summonerrank.setText(ls.getRank());
        summonerlp.setText(ls.getLp() + " LP");
        masteryscore.setText("Mastery Score: " + ls.getMasteryLevel());
        winrate.setText("Winrate: " + ls.getWinrate());

        if (ls.getTier() == null) {
            summonerrankicon.setImageResource(R.drawable.provisional);
        } else {
            switch (ls.getTier()) {
                case "UNRANKED":
                    summonerrankicon.setImageResource(R.drawable.provisional);
                    break;
                case "BRONZE":
                    summonerrankicon.setImageResource(R.drawable.bronze);
                    break;
                case "SILVER":
                    summonerrankicon.setImageResource(R.drawable.silver);
                    break;
                case "GOLD":
                    summonerrankicon.setImageResource(R.drawable.gold);
                    break;
                case "PLATINUM":
                    summonerrankicon.setImageResource(R.drawable.platinum);
                    break;
                case "DIAMOND":
                    summonerrankicon.setImageResource(R.drawable.diamond);
                    break;
                case "MASTER":
                    summonerrankicon.setImageResource(R.drawable.master);
                    break;
                case "CHALLENGER":
                    summonerrankicon.setImageResource(R.drawable.challenger);
                    break;
                default:
                    summonerrankicon.setImageResource(R.drawable.provisional);
                    break;
            }
        }


        return rootView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


