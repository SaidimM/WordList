package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;
import com.example.WordList.list.Word;
import com.example.WordList.list.WordListAdapter;

import java.util.ArrayList;
import java.util.List;

public class newWordActivity extends AppCompatActivity {
    Button B1;
    ListView l1;
    TextView t1;
    EditText e1;
    Cursor cursor;
    private List<Word> wordList = new ArrayList<>();

    //    Word word;
    private DatabaseHelper dbHelper;
//    private List<Word>  wordList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        B1 = (Button)findViewById(R.id.searchNew);
        e1 = (EditText) findViewById(R.id.editText);
        l1 = (ListView)findViewById(R.id.list_itemm);
        dbHelper = new DatabaseHelper(this,"WordList.db",null,1);

        initWords();
        WordListAdapter adapter =new WordListAdapter(newWordActivity.this,R.layout.word_item,wordList);
        ListView listView = (ListView)findViewById(R.id.itemm2) ;
        listView.setAdapter(adapter);


        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    l1.setVisibility(View.GONE);
                }else {
                    showListView();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                    e1 = (EditText) findViewById(R.id.editText);
                    t1 = (TextView) findViewById(R.id.textView);
                    String message;
                    String s1 = e1.getText().toString();
                    Cursor cursor = db1.query("Book", null, "queryWord=?", new String[]{s1}, null, null, null);
                    cursor.moveToFirst();
                    String word = cursor.getString(cursor.getColumnIndex("queryWord"));
                    String translation = cursor.getString(cursor.getColumnIndex("translation"));
                    String uk = cursor.getString(cursor.getColumnIndex("ukphonetic"));
                    String example = cursor.getString(cursor.getColumnIndex("example"));
                    message = word+"\n\t"+"发音："+uk+"\n\t"+"解释："+translation+"\n\t"+"例子："+example;
                    t1.setText(message);
                    Toast.makeText(newWordActivity.this,"查找成功",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(newWordActivity.this,"查找失败",Toast.LENGTH_LONG).show();}

            }
        });


        }
    private void initWords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("select * from Book",null);

        if (cursor.moveToFirst()) {
            do {
                Word data = new Word(cursor.getString(cursor.getColumnIndex("queryWord")),cursor.getString(cursor.getColumnIndex("translation")));
                wordList.add(data);
//                i++;
            }
            while (cursor.moveToNext());


        }
    }

    //模糊查询
     private void showListView(){
        int i=0;
         l1.setVisibility(View.VISIBLE);
         //获得输入的内容
         String str = e1.getText().toString().trim();
         //获取数据库对象
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         cursor= db.rawQuery("select queryWord from Book where queryWord like '%" + str + "%'", null);
         if(cursor.moveToFirst()){
             do{
                 i=i+1;
             }while(cursor.moveToNext());
         }
         if(cursor.moveToFirst()){
             String [] data=new String[i];
             i=0;
             do{
                 data[i]=cursor.getString(cursor.getColumnIndex("queryWord"));
                 i++;
             }
             while(cursor.moveToNext());
             ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(newWordActivity.this,android.R.layout.simple_list_item_1,data);
             l1.setAdapter(arrayAdapter);
         }
         l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 cursor.moveToPosition(position);
                 String word=cursor.getString(cursor.getColumnIndex("queryWord"));
                 e1.setText(word);
                 l1.setVisibility(View.GONE);
             }
         });

     }
}
