package com.petproject.andy.bcstorage;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petproject.andy.bcstorage.data.database.Barcode;
import com.petproject.andy.bcstorage.data.BarcodeAdapter;
import com.petproject.andy.bcstorage.data.BarcodeViewModel;
import com.petproject.andy.bcstorage.utils.StringUtils;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private BarcodeViewModel mBarcodeViewModel;
    String specificBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BarcodeAdapter barcodeAdapter = new BarcodeAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(barcodeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mBarcodeViewModel = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)       {
            specificBarcode =(String) bundle.get(StringUtils.PASSED_BARCODE);
            // Add an observer on the LiveData returned by getBarcodes.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            mBarcodeViewModel.getSpecificBarcodes(specificBarcode).observe(this, new Observer<List<Barcode>>() {
                @Override
                public void onChanged(@Nullable final List<Barcode> barcode) {

                    barcodeAdapter.setBarcodes(barcode);
                }
            });
        }





        /*Barcode barcode = new Barcode();

        barcode.productName="eqweq";
        barcode.location="rwerw";
        barcode.quantity=1;
        mBarcodeViewModel.insert(barcode);*/
    }

    public void onAddClick(View view){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_edit_record,null);
        final EditText barcode_et = mView.findViewById(R.id.dialog_barcode_et);
        final EditText barcode_name_et = mView.findViewById(R.id.dialog_name_et);
        final EditText barcode_location_et = mView.findViewById(R.id.dialog_location_et);
        final EditText barcode_quantity_et = mView.findViewById(R.id.dialog_quantity_et);
        Button alertOkButton = mView.findViewById(R.id.alert_ok);
        Button alertCancelButton = mView.findViewById(R.id.alert_cancel);
        barcode_name_et.setText(specificBarcode);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        alertOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Barcode barcodeToInsert = new Barcode();
                barcodeToInsert.barcode = barcode_et.getText().toString();
                barcodeToInsert.productName = barcode_name_et.getText().toString();
                barcodeToInsert.location = barcode_location_et.getText().toString();
                //TODO: check for exception
                barcodeToInsert.quantity = Integer.parseInt(barcode_quantity_et.getText().toString());
                mBarcodeViewModel.insert(barcodeToInsert);
                dialog.cancel();
            }
        });
        alertCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }
}
