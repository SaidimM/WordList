package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;

public class MainActivity extends AppCompatActivity {
    Button b1,b2,b5;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.web);
        b2 = findViewById(R.id.newWord);
        b5 = findViewById(R.id.alter);

        dbHelper = new DatabaseHelper(this, "WordList.db", null, 1);
        dbHelper.getWritableDatabase();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1 = new Intent(MainActivity.this,WebActivity.class);
                startActivity(in1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2 = new Intent(MainActivity.this,newWordActivity.class);
                startActivity(in2);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in5 = new Intent(MainActivity.this,AlterActivity.class);
                startActivity(in5);
            }
        });

    }

}
