package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PunLife on 08/05/2016.
 */
public class LocalMatch implements Parcelable {
    private String championName = null;
    private String queueType = null;
    private String gameMode = null;
    private String outcome = null;
    private String kda = null;
    private long date = 0;
    private long level = 0;
    private long cs = 0;
    private long Length = 0;

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getKda() {
        return kda;
    }

    public void setKda(String kda) {
        this.kda = kda;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getCs() {
        return cs;
    }

    public void setCs(long cs) {
        this.cs = cs;
    }

    public long getLength() {
        return Length;
    }

    public void setLength(long length) {
        Length = length;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public LocalMatch() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.championName);
        dest.writeString(this.queueType);
        dest.writeString(this.gameMode);
        dest.writeString(this.outcome);
        dest.writeString(this.kda);
        dest.writeLong(this.date);
        dest.writeLong(this.level);
        dest.writeLong(this.cs);
        dest.writeLong(this.Length);
    }

    protected LocalMatch(Parcel in) {
        this.championName = in.readString();
        this.queueType = in.readString();
        this.gameMode = in.readString();
        this.outcome = in.readString();
        this.kda = in.readString();
        this.date = in.readLong();
        this.level = in.readLong();
        this.cs = in.readLong();
        this.Length = in.readLong();
    }

    public static final Parcelable.Creator<LocalMatch> CREATOR = new Parcelable.Creator<LocalMatch>() {
        @Override
        public LocalMatch createFromParcel(Parcel source) {
            return new LocalMatch(source);
        }

        @Override
        public LocalMatch[] newArray(int size) {
            return new LocalMatch[size];
        }
    };
}

