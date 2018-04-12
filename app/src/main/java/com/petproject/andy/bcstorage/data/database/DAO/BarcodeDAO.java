package com.petproject.andy.bcstorage.data.database.DAO;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Update
    void update(Barcode barcode);

  /*  @Query("UPDATE Barcode SET Barcode=:barcode.barcode  WHERE id=:barcode.id")
    void update(long id, String );*/


}
