package com.example.myapplication.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * ViewHolder qui permet de remplir la vue associé à une liste de Sring/Commentaires au sein d'un recyclerView
 */
public class NoteViewHolder extends RecyclerView.ViewHolder {
    private TextView content;
    private RelativeLayout foregroundView;


    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        content=(TextView) itemView.findViewById(R.id.content);
        foregroundView=(RelativeLayout) itemView.findViewById(R.id.view_foreground);
    }

    public void bind(String contentString){
        content.setText(contentString);
    }

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }

    public RelativeLayout getForegroundView() {
        return foregroundView;
    }

    public void setForegroundView(RelativeLayout foregroundView) {
        this.foregroundView = foregroundView;
    }
}
