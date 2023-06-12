package com.my.memorizeapp.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.memorizeapp.DB.DBHelper;
import com.my.memorizeapp.R;
import com.my.memorizeapp.home.MainActivity;
import com.my.memorizeapp.viewQuestion.ViewNotesActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    DBHelper dbHelper;
    SQLiteDatabase db;
    private ArrayList<String> Data = null;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    String folderName;


    public RecyclerViewAdapter(ArrayList<String> list, Context context) {
        inflater = LayoutInflater.from(context);
        Data = list;
        dbHelper = DBHelper.getInstance(context, "notes", null, 1);
        db = dbHelper.getReadableDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = Data.get(position);
        holder.recyclerText.setText(text);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recyclerText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            recyclerText = itemView.findViewById(R.id.recyclerItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                String folderName = Data.get(getAdapterPosition());
                clickListener.onItemClick(v, folderName);
            }

        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, String folderName);
    }

    public void deleteItem(int position) {
        String folderName = Data.get(position);

        db.execSQL("DELETE FROM notes WHERE folder_id IN (SELECT _id FROM folders WHERE folder = '" + folderName + "')");
        db.execSQL("DELETE FROM folders WHERE folder = '" + folderName + "'");

        Data.remove(position);
        notifyItemRemoved(position);


    }

}
