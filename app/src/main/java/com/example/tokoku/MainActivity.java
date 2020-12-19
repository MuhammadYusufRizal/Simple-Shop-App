package com.example.tokoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "DatabaseTokoku1.db";
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTokoTable();
    }

    public void StockBarang(View view) {
        Intent intent = new Intent(MainActivity.this, ListItem.class);
        startActivity(intent);
    }

    public void Tentang(View view) {
        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        startActivity(intent);
    }

    public void Exit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Apakah anda yakin ingin keluar?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void EntryBarang(View view) {
        Intent intent = new Intent(MainActivity.this, InsertItem.class);
        startActivity(intent);
    }

    private void createTokoTable() {
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS Item " +
                "(\n" +
                "    id INTEGER NOT NULL CONSTRAINT item_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    kode varchar(200) NOT NULL,\n" +
                "    nama varchar(200) NOT NULL,\n" +
                "    satuan varchar(200) NOT NULL,\n" +
                "    jumlah varchar(200) NOT NULL,\n" +
                "    harga varchar(200) NOT NULL,\n" +
                "    hari varchar(200) NOT NULL,\n" +
                "    bulan varchar(200) NOT NULL,\n" +
                "    tahun varchar(200) NOT NULL\n" +
                ");"

        );
    }
}