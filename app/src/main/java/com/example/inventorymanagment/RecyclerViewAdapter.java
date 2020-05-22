package com.example.inventorymanagment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    private Context mcontext;
    private List<ListItems> mData;
    private List<ListItems> mDataFiltered;

    RecyclerViewAdapter(Context mcontext, List<ListItems> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflator = LayoutInflater.from(mcontext);
        View view = inflator.inflate(R.layout.row, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.card.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_transition));
        holder.idt.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_translate));

        holder.idt.setText(String.valueOf(mDataFiltered.get(position).getId()));
        holder.itemname.setText(mDataFiltered.get(position).getItemName());
        holder.sps.setText("sac/hsn : " + mDataFiltered.get(position).getSachsn() + "  "
                + "Price : " + mDataFiltered.get(position).getPrice() + "  "
                + "Stock : " + mDataFiltered.get(position).getStock());

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, UpdateItems.class);
                i.putExtra("Id", mDataFiltered.get(position).getId());
                // Log.i("is clicked" , String.valueOf(mDataFiltered.get(position).getId()));
                mcontext.startActivity(i);
            }
        });

        holder.main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(mcontext).setMessage("Do you want to delete ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerView.sql.execSQL("DELETE FROM item_list where ID = " + mDataFiltered.get(position).getId() + "");
                                //      Log.i("deleted" , String.valueOf(mDataFiltered.get(position).getId()));
                                show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });

    }

    public void show() {

        recyclerView.mData.clear();

        Cursor c = recyclerView.sql.rawQuery("SELECT * FROM item_list", null);

        if (c.moveToFirst()) {
            do {
                //   int id, int price, int stock, String itemName, String sachsn

                mData.add(new ListItems(c.getInt(0),
                        c.getInt(3),
                        c.getInt(4),
                        c.getString(1),
                        c.getString(2)));

            } while (c.moveToNext());
        }
        recyclerView.rv.setAdapter(recyclerView.itemAdapter);
        recyclerView.rv.setLayoutManager(new LinearLayoutManager(mcontext));
        c.close();
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();

                if (key.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<ListItems> isFiltered = new ArrayList<>();
                    for (ListItems row : mData) {
                        if (row.getItemName().toLowerCase().contains(key.toLowerCase())) {
                            isFiltered.add(row);
                        }
                    }
                    mDataFiltered = isFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDataFiltered = (List<ListItems>) results.values;
                notifyDataSetChanged();

            }
        };
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView idt;
        private TextView itemname;
        private TextView sps;
        private ConstraintLayout main;
        private LinearLayout card;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idt = itemView.findViewById(R.id.idTv);
            itemname = itemView.findViewById(R.id.itemNameRow);
            sps = itemView.findViewById(R.id.spsRow);
            main = itemView.findViewById(R.id.rowLayout);
            card = itemView.findViewById(R.id.linearcard);

        }
    }
}
