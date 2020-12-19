package com.example.tokoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    int custom_list_item;
    SQLiteDatabase mDatabase;
    private Context context;
    private List<Item> itemList;

    public ItemAdapter(Context context, int custom_list_item, List<Item> itemList, SQLiteDatabase mDatabase) {
        this.context = context;
        this.custom_list_item = custom_list_item;
        this.mDatabase = mDatabase;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = itemList.get(position);

        holder.txtNama.setText(item.getNama());
        holder.txtJumlah.setText(item.getJumlah() + " buah");
        holder.txtHarga.setText("Rp." + item.getHarga() + "/" + item.getSatuan());
        holder.txtHari.setText("Expired : " + item.getHari());
        holder.txtBulan.setText(item.getBulan());
        holder.txtTahun.setText(item.getTahun());

        holder.imgEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateData(item);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteData(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNama, txtHarga, txtJumlah, txtHari, txtBulan, txtTahun;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.txt_nama);
            txtHarga = itemView.findViewById(R.id.txt_harga);
            txtJumlah = itemView.findViewById(R.id.txt_jumlah);
            txtHari = itemView.findViewById(R.id.txt_hari);
            txtBulan = itemView.findViewById(R.id.txt_bulan);
            txtTahun = itemView.findViewById(R.id.txt_tahun);

            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }

    public void deleteData(int id){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Yakin Menghapus Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = "DELETE FROM Item WHERE id="+id;
                mDatabase.execSQL(data);
                reloadProductFromDatabase();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void updateData(final Item item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(view);
        builder.setCancelable(true);

        final EditText editNama     = view.findViewById(R.id.editNama);
        final EditText editJumlah   = view.findViewById(R.id.editJumlah);
        final EditText editHarga    = view.findViewById(R.id.editHarga);
        final EditText editHari     = view.findViewById(R.id.editHari);
        final EditText editBulan    = view.findViewById(R.id.editBulan);
        final EditText editTahun    = view.findViewById(R.id.editTahun);

        editNama.setText(item.getNama());
        editJumlah.setText(item.getJumlah());
        editHarga.setText(item.getHarga());
        editHari.setText(item.getHari());
        editBulan.setText(item.getBulan());
        editTahun.setText(item.getTahun());

        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nama     = editNama.getText().toString().trim();
                String jumlah   = editJumlah.getText().toString().trim();
                String harga    = editHarga.getText().toString().trim();
                String hari     = editHari.getText().toString().trim();
                String bulan    = editBulan.getText().toString().trim();
                String tahun    = editTahun.getText().toString().trim();

                if (!nama.isEmpty() && !jumlah.isEmpty() && !harga.isEmpty() && !hari.isEmpty() && !bulan.isEmpty() && !tahun.isEmpty()) {
                    String data = "UPDATE Item SET nama = ?, jumlah = ?, harga = ?, hari = ?, bulan = ?, tahun = ? WHERE id = ? ";

                    mDatabase.execSQL(data, new String[]{nama, jumlah, harga , hari, bulan, tahun, String.valueOf(item.getId())});
                    Toast.makeText(context, "Barang telah diupdate!", Toast.LENGTH_SHORT).show();
                    reloadProductFromDatabase();
                }
                else{
                    Toast.makeText(context, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void reloadProductFromDatabase() {
        Cursor item = mDatabase.rawQuery("SELECT * FROM Item", null);
        if (item.moveToFirst()) {
            itemList.clear();
            do {
                itemList.add(new Item(
                        item.getInt(0),
                        item.getString(1),
                        item.getString(2),
                        item.getString(3),
                        item.getString(4),
                        item.getString(5),
                        item.getString(6),
                        item.getString(7),
                        item.getString(8)
                ));
            } while (item.moveToNext());
        }
        item.close();
        notifyDataSetChanged();
    }
}
