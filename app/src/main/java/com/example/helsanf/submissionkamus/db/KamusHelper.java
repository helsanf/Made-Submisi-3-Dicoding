package com.example.helsanf.submissionkamus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.helsanf.submissionkamus.R;
import com.example.helsanf.submissionkamus.model.KamusModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.helsanf.submissionkamus.db.DatabaseContract.KamusColumns.ARTI;
import static com.example.helsanf.submissionkamus.db.DatabaseContract.KamusColumns.KATA;
import static com.example.helsanf.submissionkamus.db.DatabaseContract.TABLE_ENG_IND;
import static com.example.helsanf.submissionkamus.db.DatabaseContract.TABLE_IND_ENG;

public class KamusHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public ArrayList<KamusModel> getDatabyKata(String kata, String languange) {
        String kategory = null;
        Cursor cursor = null;
        if (languange == "Eng") {
            cursor = database.query(TABLE_ENG_IND, null, KATA + " LIKE ?", new String[]{kata.trim() + "%"}, null, null, _ID + " ASC", null);
            kategory = context.getResources().getString(R.string.eng_in);
        } else if (languange == "Ind") {
            cursor = database.query(TABLE_IND_ENG, null, KATA + " LIKE ?", new String[]{kata.trim() + "%"}, null, null, _ID + " ASC", null);
            kategory = context.getResources().getString(R.string.in_eng);
        }
        cursor.moveToFirst();

        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount() > 0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKosakata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));
                kamusModel.setKategori(kategory);

                arrayList.add(kamusModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<KamusModel> getAllData(String languange) {
        Cursor cursor;
        String category = null;
        if (languange == "Eng") {
            cursor = database.query(TABLE_ENG_IND, null, null, null, null, null, _ID + " ASC", null);
            category = context.getResources().getString(R.string.eng_in);
        } else {
            cursor = database.query(TABLE_IND_ENG, null, null, null, null, null, _ID + " ASC", null);
            category = context.getResources().getString(R.string.in_eng);
        }
        cursor.moveToFirst();

        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount() > 0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKosakata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));
                kamusModel.setKategori(category);

                arrayList.add(kamusModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(KamusModel kamusModel, String languange) {
        String table = null;
        if (languange == "Eng") {
            table = TABLE_ENG_IND;
        } else {
            table = TABLE_IND_ENG;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KATA, kamusModel.getKosakata());
        initialValues.put(ARTI, kamusModel.getDeskripsi());
        return database.insert(table, null, initialValues);
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public void insertTransaction(KamusModel kamusModel, String languange){
        String table = null;
        if(languange == "Eng"){
            table = TABLE_ENG_IND;
        }else{
            table = TABLE_IND_ENG;
        }

        String sql = "INSERT INTO "+table+" ("+KATA+", "+ARTI
                +") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, kamusModel.getKosakata());
        stmt.bindString(2, kamusModel.getDeskripsi());
        stmt.execute();
        stmt.clearBindings();
    }

    public int update(KamusModel kamusModel, String languange){
        String table = null;
        if(languange == "Eng"){
            table = TABLE_ENG_IND;
        }else{
            table = TABLE_IND_ENG;
        }

        ContentValues args = new ContentValues();
        args.put(KATA, kamusModel.getKosakata());
        args.put(ARTI, kamusModel.getDeskripsi());
        return database.update(table, args, _ID + "= '" + kamusModel.getId() + "'", null);
    }
    public int delete(int id, String languange){
        String table = null;
        if(languange == "Eng"){
            table =TABLE_ENG_IND;
        }else{
            table = TABLE_IND_ENG;
        }
        return database.delete(table, _ID + " = '"+id+"'", null);
    }

}
