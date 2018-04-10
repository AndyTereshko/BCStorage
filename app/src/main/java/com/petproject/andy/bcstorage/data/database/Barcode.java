package com.petproject.andy.bcstorage.data.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Barcode")
public class Barcode {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;



    @ColumnInfo(name = "Barcode")
    public String barcode;

    @ColumnInfo(name = "productName")
    public String productName;

    @ColumnInfo(name = "Location")
    public String location;

    @ColumnInfo(name = "Quantity")
    public int quantity;

}
