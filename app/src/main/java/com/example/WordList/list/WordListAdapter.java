package com.example.WordList.list;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.WordList.Database.DatabaseHelper;
import com.example.WordList.R;
import com.example.WordList.UI.AlterActivity;

import java.util.List;


public class WordListAdapter extends ArrayAdapter<Word> {
    private int resourceId;
    private DatabaseHelper dbHelper;
    private List<Word>  list;
    private Context mContext;
    public WordListAdapter(Context context, int textViewResourceId, List<Word> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        list=objects;
        mContext=context;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Word word = getItem(position); //获取当前项的Word实例
        dbHelper = new DatabaseHelper(getContext(),"WordList.db",null,1);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.word_item, parent, false);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ViewHolder holder;

        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder=new ViewHolder();
            view.setTag(holder);
        }
        else{
            view=convertView;
            holder=(ViewHolder)view.getTag();
        }
        holder.Delete=(Button)view.findViewById(R.id.Delete);
        holder.Alter=(Button)view.findViewById(R.id.Alter);
        holder.wordName = (TextView) view.findViewById(R.id.word_name);
        holder.word_d=(TextView)view.findViewById(R.id.word_detail);
        if(word!=null){
            holder.wordName.setText(word.getQuery());
            holder.word_d.setText(word.getTranslation());
            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position,word,db);
                }
            });
            holder.Alter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, AlterActivity.class);
                    intent.putExtra("Word",word.getQuery());
                    mContext.startActivity(intent);
                }
            });
        }
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.Delete.setVisibility(View.VISIBLE);
                holder.Alter.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return view;
    }
    private void deleteItem(int pos, Word word, SQLiteDatabase db) {
        list.remove(pos);
        db.delete("Book","queryWord='"+word.getQuery()+"'",null);
        this.notifyDataSetChanged();
    }
    private static class ViewHolder{
        TextView wordName;
        TextView word_d;
        Button Delete,Alter;
    }
}

