package com.petproject.andy.bcstorage

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.petproject.andy.bcstorage.data.database.Barcode
import com.petproject.andy.bcstorage.data.BarcodeAdapter
import com.petproject.andy.bcstorage.data.BarcodeViewModel
import com.petproject.andy.bcstorage.utils.DialogUtils
import com.petproject.andy.bcstorage.utils.StringUtils

class ResultActivity : AppCompatActivity(), BarcodeAdapter.BarcodeAdapterOnClickHandler {

    private var mBarcodeViewModel: BarcodeViewModel? = null
    internal var specificBarcode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val barcodeAdapter = BarcodeAdapter(this, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = barcodeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Get a new or existing ViewModel from the ViewModelProvider.
        mBarcodeViewModel = ViewModelProviders.of(this).get(BarcodeViewModel::class.java)
        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            specificBarcode = bundle.get(StringUtils.PASSED_BARCODE) as String
            supportActionBar!!.title = "Result for $specificBarcode"
            // Add an observer on the LiveData returned by getBarcodes.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            mBarcodeViewModel!!.getSpecificBarcodes(specificBarcode).observe(this, Observer { barcode -> barcodeAdapter.setBarcodes(barcode!!) })
        }

    }

    /**
     * Showing dialog to add new record.
     * @param view that was pressed
     */

    fun onAddClick(view: View) {
        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.dialog_edit_record, null)
        val barcode_et = mView.findViewById<EditText>(R.id.dialog_barcode_et)
        val barcode_name_et = mView.findViewById<EditText>(R.id.dialog_name_et)
        val barcode_location_et = mView.findViewById<EditText>(R.id.dialog_location_et)
        val barcode_quantity_et = mView.findViewById<EditText>(R.id.dialog_quantity_et)
        val alertOkButton = mView.findViewById<Button>(R.id.alert_ok)
        val alertCancelButton = mView.findViewById<Button>(R.id.alert_cancel)
        val deleteButton = mView.findViewById<Button>(R.id.dialog_delete_button)
        deleteButton.visibility = View.GONE //hiding delete button
        barcode_et.setText(specificBarcode) //getting barcode passed into this activity
        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        alertOkButton.setOnClickListener {
            val barcodeToInsert = Barcode()
            barcodeToInsert.barcode = barcode_et.text.toString()
            barcodeToInsert.productName = barcode_name_et.text.toString()
            barcodeToInsert.location = barcode_location_et.text.toString()
            try {
                barcodeToInsert.quantity = Integer.parseInt(barcode_quantity_et.text.toString())
            } catch (e: NumberFormatException) {
                Log.d(TAG, e.toString())
            }

            mBarcodeViewModel!!.insert(barcodeToInsert)
            dialog.cancel()
        }
        alertCancelButton.setOnClickListener { dialog.cancel() }
        dialog.show()
        DialogUtils.doKeepDialog(dialog) //keeps dialog after orientation change

    }

    /**
     * on recycler view item click show dialog to edit barcode
     * @param barcode which is being updated
     */

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

            mBarcodeViewModel!!.update(barcode)
            dialog.cancel()
        }
        alertCancelButton.setOnClickListener { dialog.cancel() }
        deleteButton.setOnClickListener {
            mBarcodeViewModel!!.delete(barcode)
            dialog.cancel()
        }
        dialog.show()
        DialogUtils.doKeepDialog(dialog) //keeps dialog after orientation change
    }

    companion object {
        private val TAG = "ResultActivity"
    }


}
