package com.petproject.andy.bcstorage.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petproject.andy.bcstorage.R
import com.petproject.andy.bcstorage.data.database.Barcode

class BarcodeAdapter(context: Context, private val mClickHandler: BarcodeAdapterOnClickHandler) : RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder>() {


    private val mLayoutInflater: LayoutInflater
    private var mBarcodes: List<Barcode>? = null


    /**
     * interface to handle clicks on recyclerview rows
     */

    interface BarcodeAdapterOnClickHandler {
        fun onClick(barcode: Barcode)
    }


    inner class BarcodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mBarcodeId_tv: TextView
        val mBarcode_tv: TextView
        val mBarcodeName_tv: TextView
        val mBarcodeLocation_tv: TextView
        val mBarcodeQuantity_tv: TextView

        init {
            mBarcodeId_tv = itemView.findViewById(R.id.barcode_id_tv)
            mBarcode_tv = itemView.findViewById(R.id.barcode_tv)
            mBarcodeName_tv = itemView.findViewById(R.id.barcode_name_tv)
            mBarcodeLocation_tv = itemView.findViewById(R.id.barcode_location_tv)
            mBarcodeQuantity_tv = itemView.findViewById(R.id.barcode_quantity_tv)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val clickedBarcode = mBarcodes!![adapterPosition]
            mClickHandler.onClick(clickedBarcode)


        }


    }


    init {

        mLayoutInflater = LayoutInflater.from(context)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeViewHolder {
        val itemView = mLayoutInflater.inflate(R.layout.recyclerview_item, parent, false)
        return BarcodeViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: BarcodeViewHolder, position: Int) {
        val currentBarcode = mBarcodes!![position]
        holder.mBarcodeId_tv.text = java.lang.Long.toString(currentBarcode.id)
        holder.mBarcode_tv.text = currentBarcode.barcode
        holder.mBarcodeName_tv.text = currentBarcode.productName
        holder.mBarcodeLocation_tv.text = currentBarcode.location
        holder.mBarcodeQuantity_tv.text = Integer.toString(currentBarcode.quantity)

    }

    fun setBarcodes(barcodes: List<Barcode>) {
        mBarcodes = barcodes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mBarcodes != null)
            mBarcodes!!.size
        else
            0
    }
}
