package com.petproject.andy.bcstorage.data.database.DAO;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.petproject.andy.bcstorage.data.database.Barcode;

import java.util.List;

@Dao
public interface BarcodeDAO {

    @Insert
    void insert(Barcode barcode);

    @Delete
    void delete(Barcode barcode);

    @Query("SELECT * FROM Barcode")
    LiveData<List<Barcode>> getAllBarcodes();

    @Query("SELECT * FROM Barcode WHERE barcode = :specificBarcode")
    LiveData<List<Barcode>> getSpecificBarcodes(String specificBarcode);


}
