package com.example.myapplication.util;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Propriete;
import com.squareup.picasso.Picasso;

import static java.lang.Math.abs;

/**
 * ViewHolder qui permet de remplir la vue associé à une liste de Propriété au sein d'un recyclerView
 */
public class ProprieteViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout foregroundView;
    private TextView titre;
    private TextView prix;
    private ImageView image;

    public ProprieteViewHolder(@NonNull View itemView) {
        super(itemView);
        titre = (TextView) itemView.findViewById(R.id.itemTitre);
        prix = (TextView) itemView.findViewById(R.id.itemPrix);
        image = (ImageView) itemView.findViewById(R.id.image);
        foregroundView = (ConstraintLayout) itemView.findViewById(R.id.view_foreground_list);
    }

    public void bind(Propriete propriete){
        Log.i("database" , ""+propriete.getImages());
        String titre_str = propriete.getTitre();
        titre.setText(propriete.getTitre());
        prix.setText(Integer.toString(propriete.getPrix())+"€");
        Picasso.get().load(propriete.getImages().get(0)).into(image);
    }

    public ConstraintLayout getForegroundView() {
        return foregroundView;
    }

    public void setForegroundView(ConstraintLayout foregroundView) {
        this.foregroundView = foregroundView;
    }

    public TextView getTitre() {
        return titre;
    }

    public void setTitre(TextView titre) {
        this.titre = titre;
    }

    public TextView getPrix() {
        return prix;
    }

    public void setPrix(TextView prix) {
        this.prix = prix;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
