package com.covid.perpustakaanyusril;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView TvStatus;
    Button btnProses;
    EditText txID, txNamaAnggota, txJudul, txPinjam, txKembali, txStatus;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        txID = (EditText) findViewById(R.id.txID);
        txNamaAnggota = (EditText) findViewById(R.id.txNamaAnggota);
        txJudul = (EditText) findViewById(R.id.txJudul);
        txPinjam = (EditText) findViewById(R.id.txPinjam);
        txKembali = (EditText) findViewById(R.id.txKembali);
        txStatus = (EditText) findViewById(R.id.txStatus);

        TvStatus = (TextView) findViewById(R.id.TvStatus);
        btnProses = (Button) findViewById(R.id.btnProses);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        getData();

        txKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesKembali();
            }
        });

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
    }


    private void prosesKembali() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setMessage("Proses ke pengembalian buku");
        builder.setCancelable(true);
        builder.setPositiveButton("Proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idpinjam = txID.getText().toString().trim();
                String kembali = "Di kembalikan";

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_status, kembali);
                dbHelper.updateData(values, id);
                Toast.makeText(AddActivity.this, "Proses Pengambalian Buku Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                txKembali.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdfl = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String tglPinjam = sdfl.format(cl.getTime());
        txPinjam.setText(tglPinjam);

        Cursor cur = dbHelper.oneData(id);
        if (cur.moveToFirst()) {
            String idpinjam = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            String nama = cur.getString(cur.getColumnIndex(DBHelper.row_nama));
            String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            String pinjam = cur.getString(cur.getColumnIndex(DBHelper.row_pinjam));
            String kembali = cur.getString(cur.getColumnIndex(DBHelper.row_kembali));
            String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            txID.setText(idpinjam);
            txNamaAnggota.setText(nama);
            txJudul.setText(judul);
            txPinjam.setText(pinjam);
            txKembali.setText(kembali);
            txStatus.setText(status);

            if (txID.equals("")) {
                TvStatus.setVisibility(View.GONE);
                txStatus.setVisibility(View.GONE);
                btnProses.setVisibility(View.GONE);
            } else {
                TvStatus.setVisibility(View.VISIBLE);
                txStatus.setVisibility(View.VISIBLE);
                btnProses.setVisibility(View.VISIBLE);
            }

            if (status.equals("DiPinjam")) {
                btnProses.setVisibility(View.VISIBLE);
            } else {
                btnProses.setVisibility(View.GONE);
                txNamaAnggota.setEnabled(false);
                txJudul.setEnabled(false);
                txKembali.setEnabled(false);
                txStatus.setEnabled(false);

            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);

        String idpinjam = txID.getText().toString().trim();
        String status = txStatus.getText().toString().trim();


        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);

        if (idpinjam.equals("")) {
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }else {
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }

        if (status.equals("Di Kembalikan")) {
            itemSave.setVisible(false);
            itemDelete.setVisible(false);
            itemClear.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                insertAndUpdate();
        }
        switch (item.getItemId()) {
            case R.id.action_clear:
                txNamaAnggota.setText("");
                txJudul.setText("");
                txKembali.setText("");
        }
        switch (item.getItemId()) {
            case R.id.action_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage("data ini akan di hapus");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteData(id);
                        Toast.makeText(AddActivity.this, "Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertAndUpdate() {
        String idPinjam = txID.getText().toString().trim();
        String nama = txNamaAnggota.getText().toString().trim();
        String judul = txJudul.getText().toString().trim();
        String tglPinjam = txPinjam.getText().toString().trim();
        String tglkembali = txKembali.getText().toString().trim();
        String status = "Dipinjam";

        ContentValues values = new ContentValues();

        values.put(DBHelper.row_nama, nama);
        values.put(DBHelper.row_judul, judul);
        values.put(DBHelper.row_kembali, tglkembali);
        values.put(DBHelper.row_status, status);

        if (nama.equals("") || judul.equals("") || tglkembali.equals("")) {
            Toast.makeText(AddActivity.this, "Isi data dengan lengkap", Toast.LENGTH_SHORT).show();
        } else {
            if (idPinjam.equals("")) {
                values.put(DBHelper.row_pinjam, tglPinjam);
                dbHelper.inserData(values);
            } else {
                dbHelper.updateData(values, id);
            }
            Toast.makeText(AddActivity.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

