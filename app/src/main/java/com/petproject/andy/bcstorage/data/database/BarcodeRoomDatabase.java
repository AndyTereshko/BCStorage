package com.petproject.andy.bcstorage.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.petproject.andy.bcstorage.data.database.DAO.BarcodeDAO;

@Database(entities = {Barcode.class}, version = 1)
public abstract class BarcodeRoomDatabase extends RoomDatabase {

    public abstract BarcodeDAO barcodeDAO();

    private static BarcodeRoomDatabase INSTANCE;

    public static BarcodeRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (BarcodeRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            BarcodeRoomDatabase.class,
                            "BarcodeDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }




}
