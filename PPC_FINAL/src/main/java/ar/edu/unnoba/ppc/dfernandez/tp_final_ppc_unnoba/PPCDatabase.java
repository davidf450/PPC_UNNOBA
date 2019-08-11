package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {User.class},version = 1)
public abstract class PPCDatabase extends RoomDatabase {
    private static final String DB_NAME = "ppc_db";
    private static PPCDatabase PPCDatabaseInstance;
    public static synchronized PPCDatabase getInstance(Context context){
        if (PPCDatabaseInstance ==null){
            PPCDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),PPCDatabase.class,DB_NAME).allowMainThreadQueries().build();
        }
        return PPCDatabaseInstance;
    }
    public abstract UserDao userDao();

}
