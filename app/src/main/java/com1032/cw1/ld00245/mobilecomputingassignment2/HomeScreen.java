package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.robrua.orianna.type.exception.APIException;

import java.util.Set;


public class HomeScreen extends AppCompatActivity {
    private int regionCount = 0;
    private String[] regions = regionsToArray();
    Button regionSet;
    Button searchbutton;
    private int themeSelector = 0;
    ImageView logo;
    ImageView title;
    EditText input;
    private LocalSummoner ls;
    private DBController summonerDatabase;
    private Cursor summoner = null;
    private CursorAdapter adapter;
    private SQLiteDatabase db;
    private String[] columnNames = {"profileicon", "name", "region", "rank", "tier", "level", "masterylevel", "wins", "loss", "gamesplayed"};
    private AlertDialog.Builder builder;
    private boolean errorCaught = false;
    private boolean exceptionHandled = false;

    //NOTE
    /*Pass either the username and region as intent to StatTrack
    OR
    Retrieve data(multiple arrays perhaps?)/object and pass it via an intent to StatTrack.
    Within StatTrack keep variables holding the data which can then be passed into the appropriate objects

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        regionSet = (Button) findViewById(R.id.regionset);
        searchbutton = (Button) findViewById(R.id.button);
        logo = (ImageView) findViewById(R.id.logo);
        title = (ImageView) findViewById(R.id.title);
        /**
         * Changing the colour of the logo and title as well as the UI on the home screen
         */
        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (themeSelector == 0) {
                    logo.setImageResource(R.drawable.logo1);
                    title.setImageResource(R.drawable.title1);
                    regionSet.setBackgroundColor(getResources().getColor(R.color.redtheme));
                    searchbutton.setBackgroundColor(getResources().getColor(R.color.redtheme));
                    themeSelector++;
                } else {
                    logo.setImageResource(R.drawable.logosymbol);
                    title.setImageResource(R.drawable.logotitle);
                    regionSet.setBackgroundColor(getResources().getColor(R.color.DarkTheme_accent));
                    searchbutton.setBackgroundColor(getResources().getColor(R.color.DarkTheme_accent));
                    themeSelector = 0;
                }
                return true;
            }
        });
        /**
         * Changing the region via a popup dialog on long click
         */
        regionSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regionSetter();
            }
        });
        regionSet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setTitle(R.string.region_set_dialog_title);
                builder.setItems(regions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        regionSet.setText(regions[which]);
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }

        });
        searchbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                /**
                                                 * If empty, prompt user otherwise check if
                                                 * database field is null and if it's equal to inputted summoner name
                                                 */
                                                if (input.getText().toString().trim().equals("") || input.getText().toString().equals("Summoner")) {
                                                    Toast.makeText(HomeScreen.this, "Please input a Summoner's name", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SQLiteDatabase.CursorFactory factory = null;
                                                    summonerDatabase = new DBController(HomeScreen.this, input.getText().toString(), factory);
                                                    db = summonerDatabase.getReadableDatabase();
                                                    summoner = db.query(input.getText().toString(), columnNames, null, null, null, null, null);
                                                    summoner.moveToFirst();
                                                    if (summoner.getString(3) != null) {
                                                        if (summoner.getString(3).toLowerCase().equals(input.getText().toString().toLowerCase())) {
                                                            ls = new LocalSummoner((long) summoner.getInt(1), summoner.getString(2), summoner.getString(3), summoner.getString(4), summoner.getString(5), summoner.getString(6),
                                                                    (long) summoner.getInt(7), summoner.getInt(8), summoner.getInt(9), summoner.getInt(10), summoner.getInt(11), summoner.getInt(12));

                                                            builder = new AlertDialog.Builder(HomeScreen.this);
                                                            builder.setTitle(R.string.homescreenloadingtitle);
                                                            builder.setIcon(R.mipmap.ic_launcher);
                                                            builder.setMessage(R.string.homescreenloading);
                                                            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent statTrack = new Intent(HomeScreen.this, StatTrack.class);
                                                                    statTrack.putExtra("data", ls);
                                                                    LocalMatch lm = new LocalMatch();
                                                                    statTrack.putExtra("matchlist", lm);
                                                                    statTrack.putExtra("name", input.getText().toString());
                                                                    statTrack.putExtra("exception", exceptionHandled);
                                                                    dialog.dismiss();
                                                                    startActivity(statTrack);
                                                                    finish();
                                                                }
                                                            });
                                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Toast.makeText(HomeScreen.this, "Search Cancelled.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            final AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }
                                                    } else {
                                                        builder = new AlertDialog.Builder(HomeScreen.this);
                                                        builder.setTitle(R.string.homescreenloadingtitle);
                                                        builder.setIcon(R.mipmap.ic_launcher);
                                                        builder.setMessage(R.string.homescreenloading);
                                                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                            /**
                                                             *Query the Riot API for results relevant to the summoner name inputted
                                                             */
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                ls = new LocalSummoner(input.getText().toString(), regionSet.getText().toString());
                                                                final DataSeeker dataSeeker = new DataSeeker(ls, HomeScreen.this);
                                                                final DataSeekerMatch dataSeekerMatch = new DataSeekerMatch(ls, HomeScreen.this);
                                                                Intent statTrack = new Intent(HomeScreen.this, StatTrack.class);
                                                                Thread t1 = new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Looper.prepare();
                                                                        dataSeeker.setUp();
                                                                        try {
                                                                            dataSeeker.seekSummonerData();
                                                                        } catch (APIException e) {
                                                                            e.printStackTrace();
                                                                            exceptionHandled = true;
                                                                            //errorCaught = true;
                                                                        }
                                                                    }
                                                                });
                                                                Thread t2 = new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Looper.prepare();
                                                                        dataSeekerMatch.setUp();
                                                                        try {
                                                                            dataSeekerMatch.seekMatchData();
                                                                        } catch (APIException e) {
                                                                            e.printStackTrace();
                                                                            exceptionHandled = true;
                                                                            //errorCaught = true;
                                                                        }
                                                                    }
                                                                });
                                                                t1.start();
                                                                try {
                                                                    Thread.sleep(2000);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                t2.start();
                                                                try {
                                                                    t1.join();
                                                                    t2.join();
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                if (errorCaught) {
                                                                    Toast.makeText(HomeScreen.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                                                    recreate();
                                                                } else {
                                                                    statTrack.putExtra("data", dataSeeker.returnSummonerData());
                                                                    statTrack.putExtra("matchlist", dataSeekerMatch.returnMatchData());
                                                                    statTrack.putExtra("name", input.getText().toString());
                                                                    statTrack.putExtra("exception", exceptionHandled);
                                                                    dialog.dismiss();
                                                                    startActivity(statTrack);
                                                                    finish();
                                                                }
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Toast.makeText(HomeScreen.this, "Search Cancelled.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        final AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }


                                                }
                                            }
                                        }

        );

        input = (EditText) findViewById(R.id.idinput);
        input.setOnClickListener(new View.OnClickListener()
                                 {
                                     @Override
                                     public void onClick(View v) {
                                         input.setText("", TextView.BufferType.EDITABLE);
                                     }
                                 }

        );
        input.setOnKeyListener(new View.OnKeyListener()

                               {
                                   @Override
                                   public boolean onKey(View v, int keyCode, KeyEvent event) {
                                       if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                           searchbutton.performClick();
                                           return true;
                                       }
                                       return false;
                                   }
                               }

        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setTitle(R.string.Backpresshomescreen_dialog_title)
                .setMessage(R.string.Backpresshomescreen_dialog_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
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

    /**
     * sets the regionSet button to the relevant text
     */
    public void regionSetter() {
        switch (regionCount) {
            case 0:
                regionSet.setText("BR");
                regionCount++;
                break;
            case 1:
                regionSet.setText("EUNE");
                regionCount++;
                break;
            case 2:
                regionSet.setText("EUW");
                regionCount++;
                break;
            case 3:
                regionSet.setText("JP");
                regionCount++;
                break;
            case 4:
                regionSet.setText("KR");
                regionCount++;
                break;
            case 5:
                regionSet.setText("LAN");
                regionCount++;
                break;
            case 6:
                regionSet.setText("LAS");
                regionCount++;
                break;
            case 7:
                regionSet.setText("NA");
                regionCount++;
                break;
            case 8:
                regionSet.setText("OCE");
                regionCount++;
                break;
            case 9:
                regionSet.setText("RU");
                regionCount++;
                break;
            case 10:
                regionSet.setText("TR");
                regionCount++;
                break;
            default:
                regionSet.setText("EUW");
                regionCount = 0;
                break;
        }
    }

    /**
     * Puts all the regions into an array
     * @return returns an array of regions
     */
    public String[] regionsToArray() {
        String[] tempregions = new String[LocalRegion.values().length];
        for (int i = 0; i < LocalRegion.values().length; i++) {
            tempregions[i] = LocalRegion.values()[i].name();
        }
        return tempregions;
    }

}
