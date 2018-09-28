package com.petproject.andy.bcstorage

import android.Manifest
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.petproject.andy.bcstorage.data.BarcodeAdapter
import com.petproject.andy.bcstorage.data.database.Barcode
import com.petproject.andy.bcstorage.data.BarcodeViewModel
import com.petproject.andy.bcstorage.utils.DialogUtils
import com.petproject.andy.bcstorage.utils.StringUtils


class MainActivity : AppCompatActivity(), BarcodeAdapter.BarcodeAdapterOnClickHandler {
    private var mClss: Class<*>? = null
    private lateinit var mBarcodeViewModel: BarcodeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val barcodeAdapter = BarcodeAdapter(this, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = barcodeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Get a new or existing ViewModel from the ViewModelProvider.
        mBarcodeViewModel = ViewModelProviders.of(this).get(BarcodeViewModel::class.java)
        // Add an observer on the LiveData returned by getBarcodes.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mBarcodeViewModel.allBarcodes.observe(this, Observer { barcode -> barcodeAdapter.setBarcodes(barcode!!) })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_new_record_manually -> showBarcodeManuallyDialog()
            R.id.action_new_record_scan -> launchActivity(ScannerActivity::class.java)
            R.id.action_delete_all -> showAssuranceDialog()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun showAssuranceDialog() {
        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.dialog_delete_all, null)
        val barcodeManually_et = mView.findViewById<EditText>(R.id.barcode_manually_et)
        val alertOkButton = mView.findViewById<Button>(R.id.alert_ok)
        val alertCancelButton = mView.findViewById<Button>(R.id.alert_cancel)
        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        alertOkButton.setOnClickListener {
            mBarcodeViewModel.deleteAll()
            dialog.cancel()
        }
        //closing dialog
        alertCancelButton.setOnClickListener(MyButtonClickListener(StringUtils.CANCEL_ACTION, dialog, barcodeManually_et))
        dialog.show()
        DialogUtils.doKeepDialog(dialog) //keeps dialog after orientation change

    }

    override fun onClick(barcode: Barcode) {
        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.dialog_edit_record, null)
        val barcode_et = mView.findViewById<EditText>(R.id.dialog_barcode_et)
        val barcode_name_et = mView.findViewById<EditText>(R.id.dialog_name_et)
        val barcode_location_et = mView.findViewById<EditText>(R.id.dialog_location_et)
        val barcode_quantity_et = mView.findViewById<EditText>(R.id.dialog_quantity_et)
        val alertOkButton = mView.findViewById<Button>(R.id.alert_ok)
        val alertCancelButton = mView.findViewById<Button>(R.id.alert_cancel)
        val deleteButton = mView.findViewById<Button>(R.id.dialog_delete_button)
        deleteButton.visibility = View.VISIBLE //showing delete button
        mBuilder.setView(mView)
        barcode_et.setText(barcode.barcode)
        barcode_name_et.setText(barcode.productName)
        barcode_location_et.setText(barcode.location)
        barcode_quantity_et.setText(Integer.toString(barcode.quantity))
        val dialog = mBuilder.create()
        alertOkButton.setOnClickListener {
            barcode.barcode = barcode_et.text.toString()
            barcode.productName = barcode_name_et.text.toString()
            barcode.location = barcode_location_et.text.toString()
            try {
                barcode.quantity = Integer.parseInt(barcode_quantity_et.text.toString())
            } catch (e: NumberFormatException) {
                Log.d(TAG, e.toString())
            }

            mBarcodeViewModel.update(barcode)
            dialog.cancel()
        }
        alertCancelButton.setOnClickListener { dialog.cancel() }
        deleteButton.setOnClickListener {
            mBarcodeViewModel.delete(barcode)
            dialog.cancel()
        }
        dialog.show()
        DialogUtils.doKeepDialog(dialog) //keeps dialog after orientation change
    }


    private inner class MyButtonClickListener
    /**
     * Custom click listener class
     * @param action ok or cancel button
     * @param dialog reference for opened dialog
     * @param enteredManuallyBarcode_et barcode manually entered
     */
    internal constructor(private val action: String, private val dialog: AlertDialog,
                         private val enteredManuallyBarcode_et: EditText?) : View.OnClickListener {

        override fun onClick(v: View) {
            if (action == StringUtils.OK_ACTION) { //passing barcode to ResultActivity
                val i = Intent(this@MainActivity, ResultActivity::class.java)
                val enteredManuallyBarcode = enteredManuallyBarcode_et!!.text.toString()
                i.putExtra(StringUtils.PASSED_BARCODE, enteredManuallyBarcode)
                startActivity(i)
            }
            dialog.cancel() // closing dialog


        }
    }

    /**
     * Showing alert dialog to manually get barcode and then send it to ResultActivity.
     */

    private fun showBarcodeManuallyDialog() {
        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.dialog_barcode_manually, null)
        val barcodeManually_et = mView.findViewById<EditText>(R.id.barcode_manually_et)
        val alertOkButton = mView.findViewById<Button>(R.id.alert_ok)
        val alertCancelButton = mView.findViewById<Button>(R.id.alert_cancel)
        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        alertOkButton.setOnClickListener(MyButtonClickListener(StringUtils.OK_ACTION, dialog, barcodeManually_et))
        alertCancelButton.setOnClickListener(MyButtonClickListener(StringUtils.CANCEL_ACTION, dialog, null))
        dialog.show()
    }

    /**
     * Checking for camera permission.
     * @param clss activity class
     */

    fun launchActivity(clss: Class<*>) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mClss = clss
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), ZXING_CAMERA_PERMISSION)
        } else {
            val intent = Intent(this, clss)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ZXING_CAMERA_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mClss != null) {
                    val intent = Intent(this, mClss)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, getString(R.string.ask_for_camera_permission),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val ZXING_CAMERA_PERMISSION = 1
        private val TAG = "MainActivity"
    }
}
