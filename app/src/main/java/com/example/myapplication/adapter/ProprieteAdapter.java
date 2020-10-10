package com.example.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.util.ProprieteViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.model.Propriete;

import java.util.List;

/**
 * Class qui permet d'adapter une liste instance de propriété en Item/vue pour le recyclerView.
 */
public class ProprieteAdapter extends RecyclerView.Adapter<ProprieteViewHolder> {
    List<Propriete> list; //liste adapter
    Context context;

    public ProprieteAdapter(List<Propriete> list,Context context){
        this.context = context;
        this.list = list;
    }

    /**
     * Recupére la vue associé à une propriété
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public ProprieteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_propriete, viewGroup,false);
        return new ProprieteViewHolder(view);
    }

    /**
     * Associe l'instance de Propriété à la vue associé
     * @param proprieteViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(ProprieteViewHolder proprieteViewHolder, int i) {
        Propriete propriete = list.get(i);
        proprieteViewHolder.bind(propriete);
    }

    /**
     * Supprime l'item du recyclerView à la position passer en parametre
     * @param position
     */
    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Restore l'item du recyclerView à la position passer en parametre
     * @param position
     */
    public void restoreItem(Propriete p, int position){
        list.add(position,p);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
