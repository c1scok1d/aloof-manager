package com.macinternetservices.aloof.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.macinternetservices.aloof.model.GeoLoc;


@Database(entities = {GeoLoc.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GeoLocDao dataDao();
}
