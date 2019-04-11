package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.os.Parcel;
import android.os.Parcelable;

import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.stats.ChampionStats;

/**
 * Created by PunLife on 06/05/2016.
 */
public class LocalChampion implements Parcelable {
    private String championName;
    private String kda;
    private String winrate;
    private String gamesplayed;
    private String averagecs;
    private int totalGamesPlayed;

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public LocalChampion(Champion champion, ChampionStats championStats) {
        this.championName = champion.getName();
        totalGamesPlayed = championStats.getStats().getTotalGamesPlayed();
        int kills = 0;
        int deaths = 0;
        int assists = 0;
        if (championStats.getStats().getTotalKills() == 0) {
            kills = 0;
        } else {
            kills = (championStats.getStats().getTotalKills() / totalGamesPlayed);
        }
        if (championStats.getStats().getTotalDeaths() == 0) {
            deaths = 0;
        } else {
            deaths = (championStats.getStats().getTotalDeaths() / totalGamesPlayed);
        }
        if (championStats.getStats().getTotalAssists() == 0) {
            assists = 0;
        } else {
            assists = (championStats.getStats().getTotalAssists() / totalGamesPlayed);
        }
        kda = "" + kills + "/" + deaths + "/" + assists;
        winrate = (int) (((double) championStats.getStats().getTotalWins() / (double) championStats.getStats().getTotalGamesPlayed()) * 100) + "%";
        gamesplayed = "W" + championStats.getStats().getTotalWins() + "/L" + championStats.getStats().getTotalLosses();
        averagecs = "" + (championStats.getStats().getTotalMinionKills() / championStats.getStats().getTotalGamesPlayed());


    }

    public String getChampionName() {
        return championName;
    }

    public String getKda() {
        return kda;
    }

    public String getWinrate() {
        return winrate;
    }

    public String getGamesplayed() {
        return gamesplayed;
    }

    public String getAveragecs() {
        return averagecs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.championName);
        dest.writeString(this.kda);
        dest.writeString(this.winrate);
        dest.writeString(this.gamesplayed);
        dest.writeString(this.averagecs);
        dest.writeInt(this.totalGamesPlayed);
    }

    protected LocalChampion(Parcel in) {
        this.championName = in.readString();
        this.kda = in.readString();
        this.winrate = in.readString();
        this.gamesplayed = in.readString();
        this.averagecs = in.readString();
        this.totalGamesPlayed = in.readInt();
    }

    public static final Parcelable.Creator<LocalChampion> CREATOR = new Parcelable.Creator<LocalChampion>() {
        @Override
        public LocalChampion createFromParcel(Parcel source) {
            return new LocalChampion(source);
        }

        @Override
        public LocalChampion[] newArray(int size) {
            return new LocalChampion[size];
        }
    };
}
