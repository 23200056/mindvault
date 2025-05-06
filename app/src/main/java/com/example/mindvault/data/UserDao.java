package com.example.mindvault.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    User getUser(String email, String password);

    @Query("UPDATE users SET password = :newPass WHERE email = :email")
    void updatePassword(String email, String newPass);
}
