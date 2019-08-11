package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE :user AND " +
            "password LIKE :pass LIMIT 1")
    User findByName(String user, String pass);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
