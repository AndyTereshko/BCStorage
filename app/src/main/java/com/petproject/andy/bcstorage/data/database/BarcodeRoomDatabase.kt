package com.petproject.andy.bcstorage.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.petproject.andy.bcstorage.data.database.dao.BarcodeDao

@Database(entities = arrayOf(Barcode::class), version = 1)
abstract class BarcodeRoomDatabase : RoomDatabase() {

    abstract fun barcodeDAO(): BarcodeDao

    companion object {

        private var INSTANCE: BarcodeRoomDatabase? = null

        fun getDatabase(context: Context): BarcodeRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(BarcodeRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<BarcodeRoomDatabase>(context.applicationContext,
                                BarcodeRoomDatabase::class.java,
                                "BarcodeDatabase")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }


}
