package com.example.tokoku;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InsertItem extends AppCompatActivity{
    public EditText edtKode, edtNama, edtJumlah, edtHarga, edtHari, edtBulan, edtTahun;
    public TextView edtSatuan;
    public Button btnSubmit, btnReset;
    public Spinner edtSatuanSpinner;
    SQLiteDatabase mDatabase;

    String record;
    String satuanBarang[] = {"kilo", "pcs", "box"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_item);

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        edtKode     = findViewById(R.id.edtKode);
        edtNama     = findViewById(R.id.edtNama);
        edtSatuan   = findViewById(R.id.edtSatuan);
        edtJumlah   = findViewById(R.id.edtJumlah);
        edtHarga    = findViewById(R.id.edtHarga);
        edtHari     = findViewById(R.id.edtHari);
        edtBulan    = findViewById(R.id.edtBulan);
        edtTahun    = findViewById(R.id.edtTahun);
        edtSatuanSpinner = findViewById(R.id.edtSatuanSpinner);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, satuanBarang);
        edtSatuanSpinner.setAdapter(adapter);

        edtSatuanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        record = "kilo";
                        break;
                    case 1:
                        record = "pcs";
                        break;
                    case 2:
                        record = "box";
                        break;
                }
                displayResult(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama     = edtNama.getText().toString();
                String kode     = edtKode.getText().toString();
                String satuan   = edtSatuan.getText().toString();
                String jumlah   = edtJumlah.getText().toString();
                String harga    = edtHarga.getText().toString();
                String hari     = edtHari.getText().toString();
                String bulan    = edtBulan.getText().toString();
                String tahun    = edtTahun.getText().toString();


                if (nama.isEmpty() || kode.isEmpty() || satuan.isEmpty() || jumlah.isEmpty() || harga.isEmpty() || hari.isEmpty() || bulan.isEmpty() || tahun.isEmpty()) {
                    Toast.makeText(InsertItem.this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String insertSQL = "INSERT INTO Item \n" +
                            "(kode, nama, satuan, harga, jumlah, hari, bulan, tahun)\n" +
                            "VALUES \n" +
                            "(?,?,?,?,?,?,?,?);" +
                            "";
                    mDatabase.execSQL(insertSQL, new String[]{kode, nama, satuan, harga, jumlah, hari, bulan, tahun});
                    resetField();
                    Toast.makeText(InsertItem.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetField();
            }
        });
    }

    public void displayResult(View view){
        edtSatuan.setText(record);
    }

    public void resetField(){
        edtKode.setText("");
        edtNama.setText("");
        edtJumlah.setText("");
        edtHarga.setText("");
        edtHari.setText("");
        edtBulan.setText("");
        edtTahun.setText("");
    }
}