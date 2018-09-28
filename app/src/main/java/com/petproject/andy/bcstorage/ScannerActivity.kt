package com.petproject.andy.bcstorage

import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.google.zxing.Result
import com.petproject.andy.bcstorage.utils.DialogUtils
import com.petproject.andy.bcstorage.utils.StringUtils

import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null


    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_scanner)
        setupToolbar()

        val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
        mScannerView = ZXingScannerView(this)
        contentFrame.addView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()

    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    /**
     * Class to handle dialog buttons clicks.
     *
     */

    private inner class MyButtonClickListener
    /**
     *
     * @param action ok or cancel button
     * @param dialog reference for opened dialog
     * @param resultBarcode barcode obtained from scanner
     */
    internal constructor(private val action: String, private val dialog: AlertDialog, private val resultBarcode: String) : View.OnClickListener {

        override fun onClick(v: View) {
            if (action == StringUtils.OK_ACTION) {
                val i = Intent(this@ScannerActivity, ResultActivity::class.java)
                i.putExtra(StringUtils.PASSED_BARCODE, resultBarcode)
                startActivity(i)
            }
            dialog.cancel()


        }
    }

    /**
     * Method that handles obtained from scanner result and starts new scanner.
     * @param rawResult scanner result
     */

    override fun handleResult(rawResult: Result) {
        val resultBarcode = rawResult.text
        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.dialog_scanner_found, null)
        val alertDialog_tv = mView.findViewById<TextView>(R.id.alert_dialog_tv)
        val alertOkButton = mView.findViewById<Button>(R.id.alert_ok)
        val alertCancelButton = mView.findViewById<Button>(R.id.alert_cancel)
        val stringBuilder = getString(R.string.alert_found) + "\n" +
                resultBarcode + " " +
                getString(R.string.alert_end) + " " +
                rawResult.barcodeFormat.toString() + "\n" +
                getString(R.string.alert_question)
        alertDialog_tv.text = stringBuilder
        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        alertOkButton.setOnClickListener(MyButtonClickListener(StringUtils.OK_ACTION, dialog, resultBarcode))
        alertCancelButton.setOnClickListener(MyButtonClickListener(StringUtils.CANCEL_ACTION, dialog, resultBarcode))
        dialog.show()
        DialogUtils.doKeepDialog(dialog) //keeps dialog after orientation change


        // Note:
        // * Wait 5 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        val handler = Handler()
        handler.postDelayed({ mScannerView!!.resumeCameraPreview(this@ScannerActivity) }, 5000)
    }


    fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
