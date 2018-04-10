package com.petproject.andy.bcstorage;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.petproject.andy.bcstorage.utils.StringUtils;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;




    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        setupToolbar();

        ViewGroup contentFrame =  findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    /**
     * Class to handle dialog buttons clicks.
     *
     */

    private class MyButtonClickListener implements View.OnClickListener {
        private String action;
        private AlertDialog dialog;
        private String resultBarcode;

        /**
         *
         * @param action ok or cancel button
         * @param dialog reference for opened dialog
         * @param resultBarcode barcode obtained from scanner
         */

        MyButtonClickListener(String action, AlertDialog dialog, String resultBarcode){
            super();
            this.action = action;
            this.dialog = dialog;
            this.resultBarcode = resultBarcode;
        }

        @Override
        public void onClick(View v) {
            if(action.equals(StringUtils.OK_ACTION)){
                Intent i = new Intent(ScannerActivity.this, ResultActivity.class);
                i.putExtra(StringUtils.PASSED_BARCODE, resultBarcode);
                startActivity(i);
            }
            dialog.cancel();


        }
    }

    /**
     * Method that handles obtained from scanner result and starts new scanner.
     * @param rawResult scanner result
     */

    @Override
    public void handleResult(Result rawResult) {
        final String resultBarcode = rawResult.getText();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_scanner_found,null);
        TextView alertDialog_tv = mView.findViewById(R.id.alert_dialog_tv);
        Button alertOkButton = mView.findViewById(R.id.alert_ok);
        Button alertCancelButton = mView.findViewById(R.id.alert_cancel);
        String stringBuilder = getString(R.string.alert_found) + "\n" +
                resultBarcode + " " +
                getString(R.string.alert_end) + " " +
                rawResult.getBarcodeFormat().toString() + "\n" +
                getString(R.string.alert_question);
        alertDialog_tv.setText(stringBuilder);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        alertOkButton.setOnClickListener(new MyButtonClickListener(StringUtils.OK_ACTION,dialog,resultBarcode));
        alertCancelButton.setOnClickListener(new MyButtonClickListener(StringUtils.CANCEL_ACTION,dialog,null));
        dialog.show();



        // Note:
        // * Wait 5 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScannerActivity.this);
            }
        }, 5000);
    }



    public void setupToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
