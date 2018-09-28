package com.petproject.andy.bcstorage.data.database.dao


import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.petproject.andy.bcstorage.data.database.Barcode

@Dao
interface BarcodeDao {

    @get:Query("SELECT * FROM Barcode")
    val allBarcodes: LiveData<List<Barcode>>

    @Insert
    fun insert(barcode: Barcode)

    @Delete
    fun delete(barcode: Barcode)

    @Query("SELECT * FROM Barcode WHERE barcode = :specificBarcode")
    fun getSpecificBarcodes(specificBarcode: String): LiveData<List<Barcode>>

    @Update
    fun update(barcode: Barcode)

    @Query("DELETE FROM Barcode")
    fun deleteAll()

}
