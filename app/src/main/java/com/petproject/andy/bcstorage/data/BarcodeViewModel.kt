package com.petproject.andy.bcstorage.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.petproject.andy.bcstorage.data.BarcodeRepository
import com.petproject.andy.bcstorage.data.database.Barcode

class BarcodeViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: BarcodeRepository

    val allBarcodes: LiveData<List<Barcode>>

    init {
        mRepository = BarcodeRepository(application)
        allBarcodes = mRepository.allBarcodes
    }

    fun getSpecificBarcodes(barcode: String): LiveData<List<Barcode>> {
        return mRepository.getSpecificBarcodes(barcode)
    }

    fun insert(barcode: Barcode) {
        mRepository.insert(barcode)
    }

    fun delete(barcode: Barcode) {
        mRepository.delete(barcode)
    }

    fun deleteAll() {
        mRepository.deleteAll()
    }

    fun update(barcode: Barcode) {
        mRepository.update(barcode)
    }
}
