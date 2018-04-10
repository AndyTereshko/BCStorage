package com.petproject.andy.bcstorage.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.petproject.andy.bcstorage.R;
import com.petproject.andy.bcstorage.data.database.Barcode;

import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder> {



    class BarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView mBarcodeId_tv;
        private TextView mBarcode_tv;
        private TextView mBarcodeName_tv;
        private TextView mBarcodeLocation_tv;
        private TextView mBarcodeQuantity_tv;

        BarcodeViewHolder(View itemView){
            super(itemView);
            mBarcodeId_tv = itemView.findViewById(R.id.barcode_id_tv);
            mBarcode_tv = itemView.findViewById(R.id.barcode_tv);
            mBarcodeName_tv = itemView.findViewById(R.id.barcode_name_tv);
            mBarcodeLocation_tv = itemView.findViewById(R.id.barcode_location_tv);
            mBarcodeQuantity_tv = itemView.findViewById(R.id.barcode_quantity_tv);
        }

    }

    private final LayoutInflater mLayoutInflater;
    private List<Barcode> mBarcodes;

    public BarcodeAdapter(Context context)
    {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new BarcodeViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder holder, int position) {
        Barcode currentBarcode = mBarcodes.get(position);
        holder.mBarcodeId_tv.setText (Long.toString(currentBarcode.id));
        holder.mBarcode_tv.setText(currentBarcode.barcode);
        holder.mBarcodeName_tv.setText(currentBarcode.productName);
        holder.mBarcodeLocation_tv.setText(currentBarcode.location);
        holder.mBarcodeQuantity_tv.setText(Integer.toString(currentBarcode.quantity));


    }

    public void setBarcodes(List<Barcode> barcodes){
        mBarcodes = barcodes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBarcodes!=null)
            return mBarcodes.size();
        else
            return 0;
    }
}
