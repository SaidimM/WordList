package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;

public class AlterActivity extends AppCompatActivity {
    Button b1;
    EditText e1,e2,e3,e4,e5;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);
        b1 = (Button)findViewById(R.id.sure);
        e1 = (EditText)findViewById(R.id.alterWord);
        e2 = (EditText)findViewById(R.id.ukphonetic1);
        e3 = (EditText)findViewById(R.id.usphonetic1);
        e4 = (EditText)findViewById(R.id.alterExplain);
        e5 = (EditText)findViewById(R.id.alterExample);
        dbHelper = new DatabaseHelper(this,"WordList.db",null,1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Intent intent=getIntent();
        String addedWord=intent.getStringExtra("Word");
        e1.setText(addedWord);
        System.out.println(addedWord+"**************************************************************************************************");
        final Cursor cursor = db.query("Book", null, "queryWord="+"'"+addedWord+"'",null, null, null, null);
        if(cursor.moveToNext()){
            e2.setText(cursor.getString(cursor.getColumnIndex("ukphonetic")));
            e4.setText(cursor.getString(cursor.getColumnIndex("translation")));
            e5.setText(cursor.getString(cursor.getColumnIndex("example")));
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {

                   final String query = e1.getText().toString();
                   final String ukphonetic = e2.getText().toString();
                   final String usphonetic = e3.getText().toString();
                   final String explain = e4.getText().toString();
                   final String example = e5.getText().toString();
                   ContentValues values = new ContentValues();
                   values.put("usphonetic", usphonetic);
                   values.put("ukphonetic", ukphonetic);
                   values.put("translation ", explain);
                   values.put("example", example);
                   db.update("Book", values, "queryWord=?", new String[]{query});
                   Toast.makeText(AlterActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                   cursor.close();
               }catch (Exception e){ Toast.makeText(AlterActivity.this,"修改失败",Toast.LENGTH_LONG).show();}
            }
        });
    }
}
