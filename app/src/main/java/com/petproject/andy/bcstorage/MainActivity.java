package com.petproject.andy.bcstorage;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.petproject.andy.bcstorage.data.BarcodeAdapter;
import com.petproject.andy.bcstorage.data.database.Barcode;
import com.petproject.andy.bcstorage.data.BarcodeViewModel;
import com.petproject.andy.bcstorage.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {


    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BarcodeAdapter barcodeAdapter = new BarcodeAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(barcodeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        BarcodeViewModel mBarcodeViewModel = ViewModelProviders.of(this).get(BarcodeViewModel.class);

        // Add an observer on the LiveData returned by getBarcodes.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mBarcodeViewModel.getAllBarcodes().observe(this, new Observer<List<Barcode>>() {
            @Override
            public void onChanged(@Nullable final List<Barcode> barcode) {

                barcodeAdapter.setBarcodes(barcode);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new_record_manually:
                showBarcodeManuallyDialog();
                break;
            case R.id.action_new_record_scan:
                launchActivity(ScannerActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyButtonClickListener implements View.OnClickListener {
        private String action;
        private AlertDialog dialog;
        private EditText enteredManuallyBarcode_et;

        /**
         *
         * @param action ok or cancel button
         * @param dialog reference for opened dialog
         * @param enteredManuallyBarcode_et barcode manually entered
         */

        MyButtonClickListener(String action, AlertDialog dialog, EditText enteredManuallyBarcode_et){
            super();
            this.action = action;
            this.dialog = dialog;
            this.enteredManuallyBarcode_et = enteredManuallyBarcode_et;
        }

        @Override
        public void onClick(View v) {
            if(action.equals(StringUtils.OK_ACTION)){
                Intent i = new Intent(MainActivity.this, ResultActivity.class);
                String enteredManuallyBarcode = enteredManuallyBarcode_et.getText().toString();
                i.putExtra(StringUtils.PASSED_BARCODE, enteredManuallyBarcode);
                startActivity(i);
            }
            dialog.cancel();


        }
    }

    private void showBarcodeManuallyDialog(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_barcode_manually,null);
        EditText barcodeManually_et = mView.findViewById(R.id.barcode_manually_et);
        Button alertOkButton = mView.findViewById(R.id.alert_ok);
        Button alertCancelButton = mView.findViewById(R.id.alert_cancel);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        alertOkButton.setOnClickListener(new MyButtonClickListener(StringUtils.OK_ACTION,dialog,barcodeManually_et));
        alertCancelButton.setOnClickListener(new MyButtonClickListener(StringUtils.CANCEL_ACTION,dialog,null));
        dialog.show();
    }

    /**
     * Checking for camera permission.
     * @param clss
     */

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.ask_for_camera_permission),
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
