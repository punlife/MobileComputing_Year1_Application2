package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.robrua.orianna.api.core.AsyncRiotAPI;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.api.Action;
import com.robrua.orianna.type.core.common.QueueType;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.common.Season;
import com.robrua.orianna.type.core.league.League;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.stats.ChampionStats;
import com.robrua.orianna.type.core.summoner.Summoner;
import com.robrua.orianna.type.exception.APIException;

/**
 * Created by PunLife on 04/05/2016.
 */

public class DataSeeker {
    private LocalSummoner ls;
    private String apikey = "ec9a7b01-42d2-402c-abf5-20403731337b";
    private Region region;
    private Context context;
    private LocalChampion lc;

    public DataSeeker(LocalSummoner summoner, Context context) {
        ls = summoner;
        regionSet(summoner.getRegion());
        this.context = context;


    }

    /**
     * Sets up the region and api key for the API
     */
    public void setUp() {
        RiotAPI.setRegion(region);
        RiotAPI.setAPIKey(apikey);
    }

    public void regionSet(String sRegion) {
        switch (sRegion) {
            case "BR":
                region = Region.BR;
                break;
            case "EUW":
                region = Region.EUW;
                break;
            case "EUNE":
                region = Region.EUNE;
                break;
            case "LAN":
                region = Region.LAN;
                break;
            case "LAS":
                region = Region.LAS;
                break;
            case "NA":
                region = Region.NA;
                break;
            case "OCE":
                region = Region.OCE;
                break;
            case "RU":
                region = Region.RU;
                break;
            case "TR":
                region = Region.TR;
                break;
            case "JP":
                region = Region.JP;
                break;
            case "KR":
                region = Region.KR;
                break;

        }
    }

    /**
     * Retrieves data from the API
     */
    public void seekSummonerData() {
        Summoner summoner = RiotAPI.getSummonerByName(ls.getName());
        ls.setId(summoner.getID());
        ls.setProfileicon("http://ddragon.leagueoflegends.com/cdn/6.9.1/img/profileicon/" + summoner.getProfileIconID() + ".png");
        ls.setName(summoner.getName());
        ls.setLevel(summoner.getLevel());
        ls.setMasteryLevel(summoner.getTotalMasteryLevel());
        ls.setRank(summoner.getLeagueEntries().get(0).getTier() + " " + summoner.getLeagueEntries().get(0).getDto().getEntries().get(0).getDivision());
        ls.setTier(summoner.getLeagueEntries().get(0).getTier().toString());
        ls.setLp(summoner.getLeagueEntries().get(0).getEntries().get(0).getLeaguePoints());
        Map<Champion, ChampionStats> rankedstats = summoner.getRankedStats(Season.SEASON2016);
        for (Map.Entry<Champion, ChampionStats> entry : rankedstats.entrySet()) {
            if (entry.getKey() == null) {
                ls.setGamesplayed(entry.getValue().getStats().getTotalGamesPlayed());
                ls.setWins(entry.getValue().getStats().getTotalWins());
                ls.setLoss(entry.getValue().getStats().getTotalLosses());
                ls.setWinrate(ls.getWins(), ls.getGamesplayed());
            } else {
                lc = new LocalChampion(entry.getValue().getChampion(), entry.getValue());
                ls.addChampionToList(lc);
            }
        }
        ls.sortChampionListGamesPlayed();
        List<String> championNameList = new ArrayList<String>();
        for (LocalChampion champ : ls.getChampionList()) {
            championNameList.add(champ.getChampionName());
        }

    }

    public LocalSummoner returnSummonerData() {
        return ls;
    }

}
