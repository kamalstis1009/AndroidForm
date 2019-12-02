package com.quickstart.androidform.repositories.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.quickstart.androidform.models.User;
import com.quickstart.androidform.utils.ConstantKey;

@Database(entities = {User.class}, version = ConstantKey.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    private volatile static AppDatabase mInstance;

    public static AppDatabase getDatabase(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context, AppDatabase.class, ConstantKey.DATABASE_NAME).build();
                }
            }
        }
        return mInstance;
    }

    public abstract AppDatabaseDao getDatabaseDao();
}
