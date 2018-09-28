package com.petproject.andy.bcstorage.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

import com.petproject.andy.bcstorage.data.database.Barcode
import com.petproject.andy.bcstorage.data.database.BarcodeRoomDatabase
import com.petproject.andy.bcstorage.data.database.dao.BarcodeDao

class BarcodeRepository internal constructor(application: Application) {
    private val mBarcodeDao: BarcodeDao
    //TODO: Make async
    val allBarcodes: LiveData<List<Barcode>>


    init {
        val db = BarcodeRoomDatabase.getDatabase(application)
        mBarcodeDao = db!!.barcodeDAO()
        allBarcodes = mBarcodeDao.allBarcodes


    }

    fun getSpecificBarcodes(barcode: String): LiveData<List<Barcode>> {
        return mBarcodeDao.getSpecificBarcodes(barcode)
    }

    fun insert(barcode: Barcode) {
        DBAsyncTask(mBarcodeDao, INSERT_COMMAND).execute(barcode)
    }

    fun deleteAll() {
        DBAsyncTask(mBarcodeDao, DELETE_ALL_COMMAND).execute()
    }

    fun delete(barcode: Barcode) {
        DBAsyncTask(mBarcodeDao, DELETE_COMMAND).execute(barcode)
    }

    fun update(barcode: Barcode) {
        DBAsyncTask(mBarcodeDao, UPDATE_COMMAND).execute(barcode)
    }

    private class DBAsyncTask internal constructor(private val mAsyncTaskDao: BarcodeDao, private val command: String) : AsyncTask<Barcode, Void, Void>() {

        override fun doInBackground(vararg params: Barcode): Void? {
            when (command) {
                INSERT_COMMAND -> mAsyncTaskDao.insert(params[0])
                DELETE_COMMAND -> mAsyncTaskDao.delete(params[0])
                UPDATE_COMMAND -> mAsyncTaskDao.update(params[0])
                DELETE_ALL_COMMAND -> mAsyncTaskDao.deleteAll()
            }
            return null
        }
    }

    companion object {

        private val INSERT_COMMAND = "insert"
        private val DELETE_COMMAND = "delete"
        private val DELETE_ALL_COMMAND = "delete-all"
        private val UPDATE_COMMAND = "update"
    }
}
