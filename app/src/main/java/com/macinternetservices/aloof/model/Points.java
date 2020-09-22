package com.macinternetservices.aloof.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Points implements Parcelable {
    private String lastTransactionEndTime, stillStartTime, deviceId;


    //@SerializedName("Points")
    //@Expose
    private List<Points> points = null;

    public Points(String lastTransactionEndTime, String stillStartTime, String deviceId) {
        this.lastTransactionEndTime = lastTransactionEndTime;
        this.stillStartTime = stillStartTime;
        this.deviceId = deviceId;

    }

    protected Points(Parcel in) {
        lastTransactionEndTime = in.readString();
        stillStartTime = in.readString();
        deviceId = in.readString();
        points = in.createTypedArrayList(Points.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stillStartTime);
        dest.writeString(lastTransactionEndTime);
        dest.writeString(deviceId);
        dest.writeTypedList(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Points> CREATOR = new Creator<Points>() {
        @Override
        public Points createFromParcel(Parcel in) {
            return new Points(in);
        }

        @Override
        public Points[] newArray(int size) {
            return new Points[size];
        }
    };

    public List<Points> getPoints() {
        return points;
    }

    public void setPoints(List<Points> points) {
        this.points = points;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLastTransactionEndTime() {
        return lastTransactionEndTime;
    }

    public void setLastTransactionEndTime(String lastTransactionEndTime) {
        this.lastTransactionEndTime = lastTransactionEndTime;
    }

    public String getStillStartTime() {
        return stillStartTime;
    }

    public void setStillStartTime(String stillStartTime) {
        this.stillStartTime = stillStartTime;
    }
}