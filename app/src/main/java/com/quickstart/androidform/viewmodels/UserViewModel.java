package com.quickstart.androidform.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quickstart.androidform.models.User;
import com.quickstart.androidform.repositories.room.AppDatabase;
import com.quickstart.androidform.repositories.room.AppDatabaseDao;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private AppDatabaseDao mDatabaseDao;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mDatabaseDao = AppDatabase.getDatabase(application).getDatabaseDao();
    }

    public LiveData<List<User>> getAll() {
        return mDatabaseDao.getAll();
    }

    @SuppressLint("StaticFieldLeak")
    public long insert(User user) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabaseDao.insert(user);
            }
        }.execute();
        return 1;
    }
}
