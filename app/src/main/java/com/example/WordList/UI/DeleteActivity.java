package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;

public class DeleteActivity extends AppCompatActivity {
    Button b1;
    EditText e1;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        b1 = (Button)findViewById(R.id.sure);
        e1 = (EditText)findViewById(R.id.DeleteWord);

        dbHelper = new DatabaseHelper(this,"WordList.db",null,1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String query = e1.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Book", "queryWord=?", new String[]{query});
                    Toast.makeText(DeleteActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                }catch (Exception e){ Toast.makeText(DeleteActivity.this,"删除失败",Toast.LENGTH_LONG).show();  }
            }
        });

    }

}
