package com.example.WordList.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;

import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebActivity extends AppCompatActivity  {

    static String translation,phonetic,explains,web;
    final String key = "8888888888";    //有道翻译api获取的密钥
    EditText  e1;
    TextView  t1;
    Button    b1,B2;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        b1=(Button) findViewById(R.id.search);
        e1=(EditText) findViewById(R.id.editText);
        t1 =(TextView) findViewById(R.id.textView);
        B2=(Button)findViewById(R.id.B2);
        dbHelper = new DatabaseHelper(this,"WordList.db",null,1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = e1.getText().toString();
                String selection="queryWord='"+word+"'";
                queryWord(word);
                hideKeyboard();
                Cursor cursor=db.query("Book",null,selection,null,null,null,null);
                if(!cursor.moveToFirst()){
                    B2.setVisibility(View.VISIBLE);
                }
                cursor.close();
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String query = e1.getText().toString();
                final String ukphonetic = phonetic;
                final String usphonetic="";
                final String explain = explains;
                final String example = web;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("queryWord",query);
                values.put("usphonetic",usphonetic);
                values.put("ukphonetic",ukphonetic);
                values.put("translation ",explain);
                values.put("example",example);
                db.insert("Book",null,values);
                Toast.makeText(getApplicationContext(),"Insertrd",Toast.LENGTH_SHORT).show();
                B2.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void queryWord(String s) {
        final String url0 = "http://fanyi.youdao.com/openapi.do?keyfrom=ghyghyghy&key="+key+"&type=data&doctype=json&version=1.1&q="+s;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url0).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void parseJSONWithJSONObject(String jsonData) {
        String message=null;
        try {
            JSONArray jsonArray = new JSONArray("["+jsonData+"]");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    String errorCode = jsonObject.getString("errorCode");
                    if (errorCode.equals("20")) {
                        Toast.makeText(getApplicationContext(), "要翻译的文本过长", Toast.LENGTH_SHORT);
                    } else if (errorCode.equals("30 ")) {
                        Toast.makeText(getApplicationContext(), "无法进行有效的翻译", Toast.LENGTH_SHORT);
                    } else if (errorCode.equals("40")) {
                        Toast.makeText(getApplicationContext(), "不支持的语言类型", Toast.LENGTH_SHORT);
                    } else if (errorCode.equals("50")) {
                        Toast.makeText(getApplicationContext(), "无效的key", Toast.LENGTH_SHORT);
                    } else {
                        // 要翻译的内容
                        String query = jsonObject.getString("query");
                        message = query;
                        // 翻译内容
                        translation = jsonObject.getString("translation");
                        message += "\t" + translation;
                        // 有道词典-基本词典
                        if (jsonObject.has("basic")) {
                            JSONObject basic = jsonObject.getJSONObject("basic");
                            if (basic.has("phonetic")) {
                                phonetic = basic.getString("phonetic");
                                message += "\n\t" + phonetic;
                            }
                            if (basic.has("phonetic")) {
                                explains = basic.getString("explains");
                                message += "\n\t" + explains;
                            }
                        }
                        // 有道词典-网络释义
                        if (jsonObject.has("web")) {
                            web = jsonObject.getString("web");
                            JSONArray webString = new JSONArray("[" + web + "]");
                            message += "\n网络释义：";
                            JSONArray webArray = webString.getJSONArray(0);
                            int count = 0;
                            while (!webArray.isNull(count)) {
                                if (webArray.getJSONObject(count).has("key")) {
                                    String key = webArray.getJSONObject(count).getString("key");
                                    message += "\n\t<" + (count + 1) + ">" + key;
                                }
                                if (webArray.getJSONObject(count).has("value")) {
                                    String value = webArray.getJSONObject(count).getString("value");
                                    message += "\n\t   " + value;
                                }
                                count++;
                            }
                        }
                    }
                }
            }
            t1.setText(message);
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void hideKeyboard() {
        View viewFocus = this.getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imManager != null) {
                imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
            }
        }
    }
}
