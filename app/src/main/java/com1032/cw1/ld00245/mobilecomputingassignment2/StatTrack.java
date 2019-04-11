package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CursorAdapter;
import android.widget.TextView;

public class StatTrack extends AppCompatActivity {

    private DBController summonerDatabase;
    private Cursor summoner = null;
    private CursorAdapter adapter;
    private SQLiteDatabase db;
    private String[] columnNames = {"profileicon", "name", "region", "rank", "tier", "level", "masterylevel", "wins", "loss", "gamesplayed"};
    private boolean exceptionHandled;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_track);


        Intent intent = getIntent();
        LocalSummoner localSummoner = intent.getExtras().getParcelable("data");
        LocalSummoner matchList = intent.getExtras().getParcelable("matchlist");
        exceptionHandled = intent.getBooleanExtra("exception", exceptionHandled);
        Intent service = new Intent(StatTrack.this, SummonerRefreshService.class);
        service.putExtra("name", localSummoner.getName());
        startService(service);


        //localSummoner.setName(intent.getExtras().getString("name"));
        //localSummoner.setRegion(intent.getExtras().getString(""));


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), localSummoner, matchList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (exceptionHandled = true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(StatTrack.this);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setTitle("Server Error");
            builder.setMessage("Due to the server error, potentially not all of the information has been returned, please try again now or later depending on the API server status.");
            builder.setIcon(R.mipmap.ic_launcher);
            final AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            SQLiteDatabase.CursorFactory factory = null;
            summonerDatabase = new DBController(StatTrack.this, localSummoner.getName(), factory);
            summonerDatabase.createAllTables();
            ContentValues cvalues = new ContentValues();
            cvalues.put("summonerID", localSummoner.getId());
            cvalues.put("profileicon", localSummoner.getProfileicon());
            cvalues.put("name", localSummoner.getName());
            cvalues.put("region", localSummoner.getRegion());
            cvalues.put("rank", localSummoner.getRank());
            cvalues.put("tier", localSummoner.getTier());
            cvalues.put("level", localSummoner.getMasteryLevel());
            cvalues.put("wins", localSummoner.getWins());
            cvalues.put("loss", localSummoner.getLoss());
            cvalues.put("gamesplayed", localSummoner.getGamesplayed());
            summonerDatabase.insertData(cvalues);
            for (LocalChampion champion : localSummoner.getChampionList()) {
                cvalues = new ContentValues();
                cvalues.put("name", champion.getChampionName());
                cvalues.put("kda", champion.getKda());
                cvalues.put("winrate", champion.getWinrate());
                cvalues.put("gamesplayed", champion.getGamesplayed());
                cvalues.put("averagecs", champion.getAveragecs());
                cvalues.put("totalgamesplayed", champion.getTotalGamesPlayed());
                summonerDatabase.insertDataChampion(cvalues);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stat_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stat_track, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private LocalSummoner localSummoner;
        private LocalSummoner matchlist;

        public SectionsPagerAdapter(FragmentManager fm, LocalSummoner ls, LocalSummoner matches) {
            super(fm);
            localSummoner = ls;
            matchlist = matches;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return SummonerFragment.newInstance(localSummoner);
                case 1:
                    return ChampionFragment.newInstance(localSummoner);
                case 2:
                    return MatchHistoryFragment.newInstance(matchlist);
                case 3:
                    return LocationFragment.newInstance();
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Summoner";
                case 1:
                    return "Champions";
                case 2:
                    return "Match History";
                case 3:
                    return "Global stats";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatTrack.this);
        builder.setTitle(R.string.Backpressstattrack_dialog_title)
                .setMessage(R.string.Backpressstattrack_dialog_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(StatTrack.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        dialog.show();
    }
}
