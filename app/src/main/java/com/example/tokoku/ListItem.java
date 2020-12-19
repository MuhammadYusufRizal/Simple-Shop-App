package com.example.tokoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends AppCompatActivity {
    private Cursor item;
    RecyclerView recyclerView;
    SQLiteDatabase mDatabase;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        showItem();
    }

    private void showItem() {
        item = mDatabase.rawQuery("select * from Item", null);

        List<Item> itemList = new ArrayList<>();

        if (item.moveToFirst()) {
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
        if (itemList.isEmpty()) {
            Toast.makeText(this, "Tidak ada barang", Toast.LENGTH_SHORT).show();
        }
        item.close();
        adapter = new ItemAdapter(this, R.layout.list_item, itemList, mDatabase);
        recyclerView.setAdapter(adapter);
        adapter.reloadProductFromDatabase();
    }
}