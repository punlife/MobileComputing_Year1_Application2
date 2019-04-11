package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.os.Parcel;
import android.os.Parcelable;

import com.robrua.orianna.type.dto.champion.Champion;
import com.robrua.orianna.type.dto.currentgame.Mastery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by PunLife on 04/05/2016.
 */
public class LocalSummoner implements Parcelable {

    private long id = 0;
    private String profileicon = null;
    private String name = null;
    private String region = null;
    private String rank = null;
    private String tier = null;
    private long level = 0;
    private int masteryLevel = 0;
    private int lp = 0;
    private int wins = 0;
    private int loss = 0;
    private int gamesplayed = 0;
    private String winrate = null;
    private List<LocalChampion> championList = new ArrayList<LocalChampion>();
    private List<LocalMatch> matchList = new ArrayList<LocalMatch>();

    public LocalSummoner() {

    }

    public LocalSummoner(String name, String region) {
        this.name = name;
        this.region = region;
    }

    public LocalSummoner(long id, String profileicon, String name, String region, String rank, String tier, long level, int masteryLevel, int lp, int wins, int loss, int gamesplayed) {
        this.id = id;
        this.profileicon = profileicon;
        this.name = name;
        this.region = region;
        this.rank = rank;
        this.tier = tier;
        this.level = level;
        this.masteryLevel = masteryLevel;
        this.lp = lp;
        this.wins = wins;
        this.loss = loss;
        this.gamesplayed = gamesplayed;
        setWinrate(wins, (wins + loss));
    }


    public void addChampionToList(LocalChampion lc) {
        if (lc == null) {
            //Do Nothing
        } else {
            championList.add(lc);
        }
    }

    public void sortChampionListAlphabetically() {
        Collections.sort(championList, new Comparator<LocalChampion>() {
            @Override
            public int compare(final LocalChampion object1, final LocalChampion object2) {
                return object1.getChampionName().compareTo(object2.getChampionName());
            }
        });
    }

    public void sortChampionListGamesPlayed() {
        Collections.sort(championList, new Comparator<LocalChampion>() {
            @Override
            public int compare(final LocalChampion object1, final LocalChampion object2) {
                if (object1.getTotalGamesPlayed() == object2.getTotalGamesPlayed()) {
                    return 0;
                } else if (object2.getTotalGamesPlayed() < object1.getTotalGamesPlayed()) {
                    return -1;
                }
                return 1;
            }
        });
    }

    public void setWinrate(int wins, int gamesplayed) {
        double wrate = 0;
        wrate = ((double) wins / (double) gamesplayed);
        double finalwrate = (int) (wrate * 100);
        winrate = "" + finalwrate + "%";
    }

    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public int getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(int masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getWinrate() {
        return winrate;
    }

    public List<LocalChampion> getChampionList() {
        return championList;
    }

    public void setChampionList(List<LocalChampion> championList) {
        this.championList = championList;
    }

    public int getGamesplayed() {
        return gamesplayed;
    }

    public void setGamesplayed(int gamesplayed) {
        this.gamesplayed = gamesplayed;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getProfileicon() {
        return profileicon;
    }

    public void setProfileicon(String profileicon) {
        this.profileicon = profileicon;
    }

    public List<LocalMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<LocalMatch> matchList) {
        this.matchList = matchList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.profileicon);
        dest.writeString(this.name);
        dest.writeString(this.region);
        dest.writeString(this.rank);
        dest.writeString(this.tier);
        dest.writeLong(this.level);
        dest.writeInt(this.masteryLevel);
        dest.writeInt(this.lp);
        dest.writeInt(this.wins);
        dest.writeInt(this.loss);
        dest.writeInt(this.gamesplayed);
        dest.writeString(this.winrate);
        dest.writeTypedList(championList);
        dest.writeTypedList(matchList);
    }

    protected LocalSummoner(Parcel in) {
        this.id = in.readLong();
        this.profileicon = in.readString();
        this.name = in.readString();
        this.region = in.readString();
        this.rank = in.readString();
        this.tier = in.readString();
        this.level = in.readLong();
        this.masteryLevel = in.readInt();
        this.lp = in.readInt();
        this.wins = in.readInt();
        this.loss = in.readInt();
        this.gamesplayed = in.readInt();
        this.winrate = in.readString();
        this.championList = in.createTypedArrayList(LocalChampion.CREATOR);
        this.matchList = in.createTypedArrayList(LocalMatch.CREATOR);
    }

    public static final Creator<LocalSummoner> CREATOR = new Creator<LocalSummoner>() {
        @Override
        public LocalSummoner createFromParcel(Parcel source) {
            return new LocalSummoner(source);
        }

        @Override
        public LocalSummoner[] newArray(int size) {
            return new LocalSummoner[size];
        }
    };
}


