package com.example.myapplication.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.util.NoteViewHolder;
import com.example.myapplication.util.ProprieteViewHolder;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    Context context;
    List<String> list;

    public NoteAdapter(List<String> listeNote,Context context) {
        this.list = listeNote;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_note, viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        String content = list.get(i);
        noteViewHolder.bind(content);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String content, int position){
        list.add(position,content);
        notifyItemInserted(position);
    }
}
