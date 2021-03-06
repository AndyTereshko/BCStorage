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
    private static final String DELETE_ALL_COMMAND = "delete-all";
    private static final String UPDATE_COMMAND = "update";
    private BarcodeDAO mBarcodeDao;
    private LiveData<List<Barcode>> mAllBarcodes;


    BarcodeRepository(Application application){
        BarcodeRoomDatabase db = BarcodeRoomDatabase.getDatabase(application);
        mBarcodeDao = db.barcodeDAO();
        mAllBarcodes = mBarcodeDao.getAllBarcodes();


    }
    //TODO: Make async
    public LiveData<List<Barcode>> getAllBarcodes(){
        return mAllBarcodes;
    }
    public LiveData<List<Barcode>> getSpecificBarcodes(String barcode){
        return mBarcodeDao.getSpecificBarcodes(barcode);
    }

    public void insert (Barcode barcode) {
        new DBAsyncTask(mBarcodeDao, INSERT_COMMAND).execute(barcode);
    }

    public void deleteAll(){
        new DBAsyncTask(mBarcodeDao, DELETE_ALL_COMMAND).execute();
    }

    public void delete (Barcode barcode) {
        new DBAsyncTask(mBarcodeDao, DELETE_COMMAND).execute(barcode);
    }

    public void update (Barcode barcode) {
        new DBAsyncTask(mBarcodeDao, UPDATE_COMMAND).execute(barcode);
    }

    private static class DBAsyncTask extends AsyncTask<Barcode, Void, Void> {

        private BarcodeDAO mAsyncTaskDao;
        private String command;

        DBAsyncTask(BarcodeDAO dao, String command) {
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
                case UPDATE_COMMAND:
                    mAsyncTaskDao.update(params[0]);
                    break;
                case DELETE_ALL_COMMAND:
                    mAsyncTaskDao.deleteAll();
            }

            return null;
        }
    }
}
