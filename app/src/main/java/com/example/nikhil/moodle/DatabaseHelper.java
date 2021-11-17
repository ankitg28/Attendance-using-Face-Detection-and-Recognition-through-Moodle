package com.example.nikhil.moodle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Moodle.db";
    public static final String TABLE_NAME = " Student_table";
    public static final String COL1 = "Std_id";
    public static final String COL2 = "id";
    public static final String COL3 = "id";
    public static final String COL4 = "id";
    public static final String COL5 = "id";
    public static final String COL6 = "id";

    SimpleDateFormat dateTime = new SimpleDateFormat("ddMMyyyy_HHmmss");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE" + TABLE_NAME + "(Std_id INTEGER PRIMARY KEY AUTOINCREMENT, Std_Name TEXT NOT NULL , Std_enroll TEXT NOT NULL,  Std_Course TEXT NOT NULL , Std_Teacher TEXT NOT NULL , Std_AP TEXT NOT NULL , Std_AP_Multiplier INTEGER, std_stamp TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS"  + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String enroll,  String Course, String teacherName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Std_Name", name);
        contentValues.put("Std_enroll", enroll);
        contentValues.put("Std_Course", Course);
        contentValues.put("Std_Teacher", teacherName);
        contentValues.put("Std_AP", "P");

        Log.w("INsert INTO TABLE = ", name + "\t" + enroll + "\t" + Course + "\t" + teacherName );

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result  == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean makeAttendance(String name,  String Course, String teacherName, String status, int multiplier) {
        SQLiteDatabase db = this.getWritableDatabase();

        String currentDateandTime = dateTime.format(new Date());
        ContentValues contentValues = new ContentValues();
//        contentValues.put("Std_Name", name);
//        contentValues.put("Std_enroll", enroll);
//        contentValues.put("Std_Course", Course);
        contentValues.put("Std_Teacher", teacherName);
        contentValues.put("Std_AP", status);
        contentValues.put("std_stamp", currentDateandTime);
        contentValues.put("Std_AP_Multiplier", multiplier);


        Log.w("INSERT INTO TABLE = ", name  + "\t" + Course + "\t" + teacherName + "\t" + currentDateandTime );

        long result = db.update(TABLE_NAME, contentValues, "Std_Name = \""+ name + "\" AND Std_Course = \"" + Course + "\"", null);
//        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result  == -1)
        {
            Log.w("makeAttendance", "not Update");
            return false;
        }
        else
        {
            Log.w("makeAttendance", "Update");
            return true;
        }
    }

    public Cursor getCourseList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT Std_Course FROM" + TABLE_NAME, null);
//        db.execSQL("delete from "+ TABLE_NAME);
        return res;
    }

    public Cursor getStudentList(String courseName){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT Std_Name, Std_enroll, Std_AP FROM " + TABLE_NAME + " WHERE Std_Course = \"" +courseName + "\"";
        Log.w("SQL = ", sql);
        Cursor res = db.rawQuery(sql,  null);
//        db.execSQL("delete from "+ TABLE_NAME);
        return res;
    }

    public void deleteCourse(String courseName){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+ TABLE_NAME + " WHERE Std_Course = \"" + courseName + "\"";
//        String sql = "ALTER TABLE " + TABLE_NAME + " ADD std_stamp CHAR(30)";
        Log.w("Query = ", sql);
        db.execSQL(sql);
//        db.execSQL("delete from "+ TABLE_NAME);

    }

    public Cursor GETALL(){
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT  FROM" + TABLE_NAME, null);
        String sql = "SELECT * FROM " + TABLE_NAME + " where Std_Course = \"Database Management System\"" ;
        Cursor res = db.rawQuery(sql,  null);
        return res;
    }


}
