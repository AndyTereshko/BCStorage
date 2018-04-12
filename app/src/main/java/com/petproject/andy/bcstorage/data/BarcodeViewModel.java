package com.petproject.andy.bcstorage.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.petproject.andy.bcstorage.data.BarcodeRepository;
import com.petproject.andy.bcstorage.data.database.Barcode;

import java.util.List;

public class BarcodeViewModel extends AndroidViewModel {

    private BarcodeRepository mRepository;

    private LiveData<List<Barcode>> mAllBarcodes;

    public BarcodeViewModel (Application application) {
        super(application);
        mRepository = new BarcodeRepository(application);
        mAllBarcodes = mRepository.getAllBarcodes();
    }

    public LiveData<List<Barcode>> getAllBarcodes() { return mAllBarcodes; }
    public LiveData<List<Barcode>> getSpecificBarcodes(String barcode) {
        return mRepository.getSpecificBarcodes(barcode);
    }

    public void insert(Barcode barcode) { mRepository.insert(barcode); }
    public void delete(Barcode barcode) { mRepository.delete(barcode); }
    public void update(Barcode barcode) { mRepository.update(barcode); }
}
