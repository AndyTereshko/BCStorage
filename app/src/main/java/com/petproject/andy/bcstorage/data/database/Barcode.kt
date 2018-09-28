package com.petproject.andy.bcstorage.data.database


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Barcode")
class Barcode {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0


    @ColumnInfo(name = "Barcode")
    var barcode: String? = null

    @ColumnInfo(name = "productName")
    var productName: String? = null

    @ColumnInfo(name = "Location")
    var location: String? = null

    @ColumnInfo(name = "Quantity")
    var quantity: Int = 0

}
