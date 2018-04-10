package com.petproject.andy.bcstorage.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.petproject.andy.bcstorage.data.database.Barcode;
import com.petproject.andy.bcstorage.data.database.BarcodeRoomDatabase;
import com.petproject.andy.bcstorage.data.database.DAO.BarcodeDAO;

import java.util.List;

public class BarcodeRepository
{

    private static final String INSERT_COMMAND = "insert";
    private static final String DELETE_COMMAND = "delete";
    private BarcodeDAO mBarcodeDao;
    private LiveData<List<Barcode>> mAllBarcodes;


    public BarcodeRepository(Application application){
        BarcodeRoomDatabase db = BarcodeRoomDatabase.getDatabase(application);
        mBarcodeDao = db.barcodeDAO();
        mAllBarcodes = mBarcodeDao.getAllBarcodes();


    }

    public LiveData<List<Barcode>> getAllBarcodes(){
        return mAllBarcodes;
    }
    public LiveData<List<Barcode>> getSpecificBarcodes(String barcode){
        return mBarcodeDao.getSpecificBarcodes(barcode);
    }

    public void insert (Barcode barcode) {
        new asyncTask(mBarcodeDao, INSERT_COMMAND).execute(barcode);
    }

    public void delete (Barcode barcode) {
        new asyncTask(mBarcodeDao, DELETE_COMMAND).execute(barcode);
    }

    private static class asyncTask extends AsyncTask<Barcode, Void, Void> {

        private BarcodeDAO mAsyncTaskDao;
        private String command;

        asyncTask(BarcodeDAO dao, String command) {
            mAsyncTaskDao = dao;
            this.command = command;
        }

        @Override
        protected Void doInBackground(final Barcode... params) {
            switch(command){
                case INSERT_COMMAND:
                    mAsyncTaskDao.insert(params[0]);
                    break;
                case DELETE_COMMAND:
                    mAsyncTaskDao.delete(params[0]);
                    break;
            }

            return null;
        }
    }
}
