package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.Context;

import com.robrua.orianna.api.core.AsyncRiotAPI;
import com.robrua.orianna.api.core.MatchListAPI;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.api.LoadPolicy;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.common.Season;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.match.Participant;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.stats.ChampionStats;
import com.robrua.orianna.type.core.summoner.Summoner;
import com.robrua.orianna.type.dto.match.MatchDetail;
import com.robrua.orianna.type.exception.APIException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PunLife on 08/05/2016.
 */
public class DataSeekerMatch {
    private LocalSummoner ls;
    private String apikey = "ec9a7b01-42d2-402c-abf5-20403731337b";
    private Region region;
    private Context context;
    private LocalMatch lm;
    private List<LocalMatch> matchlist = new ArrayList<LocalMatch>();

    public DataSeekerMatch(LocalSummoner summoner, Context context) {
        ls = summoner;
        regionSet(summoner.getRegion());
        this.context = context;

    }

    private void regionSet(String sRegion) {
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
     * Sets up the region and api key for the API as well as the load policy
     */
    public void setUp() {
        RiotAPI.setRegion(region);
        RiotAPI.setAPIKey(apikey);
        RiotAPI.setLoadPolicy(LoadPolicy.LAZY);
    }

    /**
     * retrieves the match data from the server
     */
    public void seekMatchData() {
        long temp = 0;
        Summoner summoner = RiotAPI.getSummonerByName(ls.getName());
        ls.setId(summoner.getID());
        for (MatchReference match : MatchListAPI.getMatchList(ls.getId(), 10, 0)) {
            lm = new LocalMatch();
            lm.setChampionName(match.getChampion().getName());
            lm.setQueueType(match.getQueueType().toString());
            lm.setLength(match.getMatch(false).getDto().getMatchDuration());
            lm.setGameMode(match.getMatch(false).getDto().getMatchMode());
            lm.setDate(match.getMatch().getDto().getMatchCreation());
            //for (com.robrua.orianna.type.dto.match.Participant participant: match.getMatch().getDto().getParticipants()
            for (Participant participant : match.getMatch().getParticipants()) {
                Long id = participant.getSummonerID();
                if (id == ls.getId()) {
                    lm.setCs(participant.getStats().getMinionsKilled());
                    lm.setKda(participant.getStats().getKills() + "/" + participant.getStats().getDeaths() + "/" + participant.getStats().getAssists());
                    lm.setLevel(participant.getStats().getLevel());
                    temp = participant.getTeam().getID();
                    break;
                }
            }
            if (match.getMatch().getDto().getTeams().get(0).getTeamId() == temp && match.getMatch().getDto().getTeams().get(0).getWinner()) {
                lm.setOutcome("Victory");
            } else {
                lm.setOutcome("Defeat");
            }
            matchlist.add(lm);

        }
        ls.setMatchList(matchlist);
    }

    public LocalSummoner returnMatchData() {
        return ls;
    }
}
