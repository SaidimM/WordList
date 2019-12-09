package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;

public class ADDActivity extends AppCompatActivity {
    Button b1;
    EditText e1,e2,e3,e4,e5;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        b1 = (Button)findViewById(R.id.sure);
        e1 = (EditText)findViewById(R.id.addWord);
        e2 = (EditText)findViewById(R.id.ukphonetic);
        e3 = (EditText)findViewById(R.id.usphonetic);
        e4 = (EditText)findViewById(R.id.addExplain);
        e5 = (EditText)findViewById(R.id.addExample);

        dbHelper = new DatabaseHelper(this,"WordList.db",null,1);

       b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String query = e1.getText().toString();
                    final String ukphonetic = e2.getText().toString();
                    final String usphonetic = e3.getText().toString();
                    final String explain = e4.getText().toString();
                    final String example = e5.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("queryWord",query);
                    values.put("usphonetic",usphonetic);
                    values.put("ukphonetic",ukphonetic);
                    values.put("translation ",explain);
                    values.put("example",example);
                    db.insert("Book",null,values);
                    Toast.makeText(ADDActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(ADDActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
