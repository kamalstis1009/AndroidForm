package com.quickstart.androidform.repositories.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.quickstart.androidform.models.User;

import java.util.List;

@Dao
public interface AppDatabaseDao {

    @Query("select * from users")
    LiveData<List<User>> getAll();

    @Query("select * from users where id=:userId")
    LiveData<User> getById(String userId);

    @Insert
    long insert(User user);

    @Insert
    long[] insertAll(User... user);

    @Update
    int update(User user);

    @Delete
    int delete(User user);
}
