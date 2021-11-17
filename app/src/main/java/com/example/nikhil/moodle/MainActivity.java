package com.example.nikhil.moodle;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.os.AsyncTask;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter adapter;
    DatabaseHelper MYdb;

    String subjectSelected = "";
    String dataMode= "online";
    int currentMultiplier = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MYdb = new DatabaseHelper(this);
//        MYdb.deleteCourse("ddf");
        boolean status = MYdb.insertData("nikhil", "yadav", "science", "jitendra");

        if (status==true)
        {
            Log.w("DB write", "DaTA INSERTED");
        }
        else
        {
            Log.w("DB write", "DaTA INSERTED FAILD");
        }

        Cursor res = MYdb.getCourseList();
        if(res.getCount()==0)
        {
            Log.w("DB RESPONC","NO DATA FOUND");
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext())
        {
            buffer.append("Sno :" + res.getString(0) + "\n");
//            buffer.append("name :" + res.getString(1) + "\n");
//            buffer.append("enroll :" + res.getString(2) + "\n");
//            buffer.append("course :" + res.getString(3) + "\n");
//            buffer.append("teacher:" + res.getString(4) + "\n");
//            buffer.append("A/P :" + res.getString(5) + "\n");
        }
        Log.w("DB DATA", buffer.toString() );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(subjectSelected.equals(""))
                {
                    Log.w("UPLOAD", "No Sbuject selected");
                }
                Log.w("onclick", subjectSelected);
            }
        });

//        final Button setting = findViewById(R.id.action_settings);
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.w("Setting button press", "1" );
//            }
//        });

    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
        Log.w("Menu", item.toString());

        if(item.toString().equals("Settings"))
        {
            Cursor res =  MYdb.GETALL();
            if(res.getCount()==0)
            {
                Log.w("DB GETALL() RESPONCE","NO DATA FOUND");
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext())
            {
                 buffer.append("Sno :" + res.getString(0) + "\t");
            buffer.append("name :" + res.getString(1) + "\t");
            buffer.append("enroll :" + res.getString(2) + "\t");
            buffer.append("course :" + res.getString(3) + "\t");
            buffer.append("teacher:" + res.getString(4) + "\t");
            buffer.append("A/P :" + res.getString(5) + "\t");
            buffer.append("Multi :" + res.getString(6) + "\t");
            buffer.append("Multi :" + res.getString(7) + "\n");
            }
            Log.w( "Table data", buffer.toString());
        }
        if(item.toString().equals("Help"))
        {
            Cursor res = MYdb.getCourseList();
            if(res.getCount()==0)
            {
                Log.w("DB getCorlist RESPONCE","NO DATA FOUND");
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext())
            {
                buffer.append("Sno :" + res.getString(0) + "\n");
//            buffer.append("name :" + res.getString(1) + "\n");
//            buffer.append("enroll :" + res.getString(2) + "\n");
//            buffer.append("course :" + res.getString(3) + "\n");
//            buffer.append("teacher:" + res.getString(4) + "\n");
//            buffer.append("A/P :" + res.getString(5) + "\n");
            }
            Log.w("DB DATA", buffer.toString() );
            showInputDialog();
        }
        if(item.toString().equals("Course(offline)"))
        {
            dataMode= "offline";
            Log.w("Menu", "Course(offline)");
            List<String> items = new ArrayList<>();

            Cursor res = MYdb.getCourseList();
            if(res.getCount()==0)
            {
                Log.w("DB RESPONC","NO DATA FOUND");
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext())
            {
                buffer.append("Sno :" + res.getString(0) + "\n");

                items.add(res.getString(0));
//            buffer.append("name :" + res.getString(1) + "\n");
//            buffer.append("enroll :" + res.getString(2) + "\n");
//            buffer.append("course :" + res.getString(3) + "\n");
//            buffer.append("teacher:" + res.getString(4) + "\n");
//            buffer.append("A/P :" + res.getString(5) + "\n");
            }

            ListView listView = (ListView) findViewById(R.id.mobile_list);
            ArrayAdapter< String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, items);
            listView.setAdapter(adapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setBackgroundColor(Color.blue(255));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    ListView lv = (ListView) parent;

//                        dataModel.checked = !dataModel.checked;
                    Log.w("IN MENU CheckBox", String.valueOf(position) + " is " + lv.isItemChecked(position));
                    Log.w("IN MENU CheckBox name", parent.getItemAtPosition(position).toString());
                    subjectSelected = parent.getItemAtPosition(position).toString();
                    Log.w("Selected Subjecct ", subjectSelected);
                    List<String> items = new ArrayList<>();
                    Log.w("while", "====================" + parent.getItemAtPosition(position).toString()+"=====================");
                    Cursor res = MYdb.getStudentList(parent.getItemAtPosition(position).toString());

                    if(res.getCount()==0)
                    {
                        Log.w("DB RESPONC","NO DATA FOUND");
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext())
                    {
                        buffer.append("Sno :" + res.getString(0) + "\n");
                        items.add(res.getString(0));
//            buffer.append("name :" + res.getString(1) + "\n");
//            buffer.append("enroll :" + res.getString(2) + "\n");
//            buffer.append("course :" + res.getString(3) + "\n");
//            buffer.append("teacher:" + res.getString(4) + "\n");
//            buffer.append("A/P :" + res.getString(5) + "\n");
                    }

                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    ArrayAdapter< String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, items);
                    listView.setAdapter(adapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setBackgroundColor(Color.blue(255));

                    int i = 0;
                    Log.w("while", "====================" + subjectSelected +"=====================");
                    Cursor res1 = MYdb.getStudentList(subjectSelected);

                    if(res1.getCount()==0)
                    {
                        Log.w("DB RESPONC","NO DATA FOUND");
                    }
                    else {
                        while (res1.moveToNext()) {

                            Log.w("while", res1.getString(2).toString());
                            if(res1.getString(2).toString().equals("true")){
                                listView.setItemChecked(i, true);
                            }
                            i++;
                        }
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                            ListView lv = (ListView) parent;

//                        dataModel.checked = !dataModel.checked;
                            Log.w("IN MENU CheckBox", String.valueOf(position) + " is " + lv.isItemChecked(position));
                            Log.w("IN MENU CheckBox name", parent.getItemAtPosition(position).toString());
//                        adapter.notifyDataSetChanged();
                        }
                    });
//                        adapter.notifyDataSetChanged();
                }
            });
        }
        if(item.toString().equals("Course(online)"))
        {
            dataMode= "online";
            Log.w("Menu", "Course(online)");
            new SendPostRequest().execute("course");
        }

        else if (item.toString().equals("MarkAll"))
        {
            Log.w("Menu", "in MarkAll");
            ListView listView = (ListView) findViewById(R.id.mobile_list);

            Log.w("count", String.valueOf(listView.getCount()));
            for ( int i=0; i < listView.getCount(); i++) {
                listView.setItemChecked(i, true);
            }
        }
        else if (item.toString().equals("UnMarkAll"))
        {
            Log.w("Menu", "in MarkAll");
            ListView listView = (ListView) findViewById(R.id.mobile_list);
//            listView.clearChoices();

            Log.w("Count in unmark", String.valueOf(listView.getCount()));
            for ( int i=0; i < listView.getCount(); i++) {
                listView.setItemChecked(i, false);
            }
        }

//        else if (item.toString().equals("UnMarkAll"))
//        {
//            Log.w("Menu", "in MarkAll");
//            ListView listView = (ListView) findViewById(R.id.mobile_list);
//            listView.clearChoices();
//            for ( int i=0; i < listView.getCount(); i++) {
//                listView.setItemChecked(i, false);
//            }
//        }

        if(item.toString().equals("Save"))
        {
            ListView listView = (ListView) findViewById(R.id.mobile_list);
            Log.w("count", String.valueOf(listView.getCount()));
            for ( int i=0; i < listView.getCount(); i++) {
//                Log.w("list data", listView.getItemAtPosition(i).toString() + " = " + String.valueOf(listView.isItemChecked(i)));
//                (String name,  String Course, String teacherName, String status, int multiplier)
                MYdb.makeAttendance(listView.getItemAtPosition(i).toString(), subjectSelected, "abc",String.valueOf(listView.isItemChecked(i)) , currentMultiplier);
                listView.isItemChecked(i);

            }
        }
        Log.w("Menu", "task complete");
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.logindialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        final NumberPicker numberpicker = (NumberPicker) promptView.findViewById(R.id.numberPicker);
        String[] nums = new String[20];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i);

        numberpicker.setMinValue(1);
        numberpicker.setMaxValue(20);
        numberpicker.setWrapSelectorWheel(false);
        numberpicker.setDisplayedValues(nums);
        numberpicker.setValue(1);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        resultText.setText("Hello, " + editText.getText());
                        int val = numberpicker.getValue();
                        currentMultiplier = val;
                        Log.w("Dialog Input", String.valueOf(val));
                    }
                })
                .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        private CustomAdapter adapter;
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {
                Log.w("Post resqest type", arg0[0]);
                URL url = new    URL("http://192.168.1.5/postttest.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                if(arg0[0].equals("student"))
                {
                    postDataParams.put("requestType", arg0[0]);
                    postDataParams.put("courseName", arg0[1]);
                    subjectSelected = arg0[1];
                }
                else {
                    postDataParams.put("requestType", arg0[0]);
                    postDataParams.put("email", "abc@gmail.com");
                }
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            try {
                Log.d("row data", result);
//                JSONObject obj = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(result);

//                Log.d("name", obj.getString("name"));
//                Log.d("email", obj.getString("email"));

//                JSONArray array= obj.getJSONArray();

                List<String> items = new ArrayList<>();

                JSONObject typeObject= jsonArray.getJSONObject(0);

                Log.w("Post Respoce Type", typeObject.getString("type") );
                if(typeObject.getString("type").equals("course")) {
                    Log.w("list if ", "course");
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//                    items.add("name" + i);
                        items.add(object.getString("fullname"));
                    }

                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    ArrayAdapter< String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, items);
                    listView.setAdapter(adapter);

                    listView.setAdapter(adapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setBackgroundColor(Color.blue(255));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                            ListView lv = (ListView) parent;

//                        dataModel.checked = !dataModel.checked;
                            Log.w("CheckBox", String.valueOf(position) + " is " + lv.isItemChecked(position));
                            Log.w("CheckBox name", parent.getItemAtPosition(position).toString());
                            new SendPostRequest().execute("student", parent.getItemAtPosition(position).toString());
//                        adapter.notifyDataSetChanged();
                        }
                    });
                }
                if(typeObject.getString("type").equals("student")) {
                    Log.w("list if ", "student");

                    MYdb.deleteCourse(subjectSelected);
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//                    items.add("name" + i);
                        items.add(object.getString("username") + "_" + object.getString("firstname"));
                        boolean status = MYdb.insertData( object.getString("username") + "_" + object.getString("firstname"), object.getString("username"), subjectSelected, "jitendra");
//
//                        if (status==true)
//                        {
//                            Log.w("DB write", "DaTA INSERTED");
//                        }
//                        else
//                        {
//                            Log.w("DB write", "DaTA INSERTED FAILD");
//                        }
                    }
                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    ArrayAdapter< String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, items);
                    listView.setAdapter(adapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    ListView lv = (ListView) parent;

//                        dataModel.checked = !dataModel.checked;
                            Log.w("CheckBox", String.valueOf(position) + " is " + lv.isItemChecked(position));
//                        adapter.notifyDataSetChanged();
                        }
                    });
                }
//                adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, items);


            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"" + t);
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            Log.w("Post Respoce", result.toString());
        }
        return result.toString();
    }
}


