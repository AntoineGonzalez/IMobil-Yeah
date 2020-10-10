package com.example.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapteur qui transforme une String url en ImageView pour le carrousel.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls; // liste d'url adapter

    public ViewPagerAdapter(Context context, List<String> imageUrls){
        this.context=context;
        this.imageUrls=imageUrls;
    }

    /**
     * récupére la position de l'item dans le recyclerView
     * @param object
     * @return
     */
    public int getItemPosition(Object object) {
        int index = imageUrls.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * Instancie l'imageView de l'item qui se trouve à la position passer en argument.
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        ImageView imageView = new ImageView((context));
        Log.i("testpager",imageUrls.toString());
        if(imageUrls.get(position).contains("https://")){
            Picasso.get().load(imageUrls.get(position)).fit().centerCrop().placeholder(R.drawable.error).into(imageView);

        }else{
            Picasso.get().load(new File(imageUrls.get(position))).fit().centerCrop().placeholder(R.drawable.error).into(imageView);
        }
        container.addView((imageView));
        return imageView;
    }

    /**
     * Ajoute l'url/une image au carrousel
     * @param url
     * @return
     */
    public int addUrl (String url)
    {
        return addUrl(url, imageUrls.size());
    }

    /**
     * notifie le carrousel d'unchangement
     */
    @Override
    public void notifyDataSetChanged() {
        System.out.println("notify");
        super.notifyDataSetChanged();
    }

    /**
     * Ajoute l'url/une image au carrousel à la position spécifier en parametre
     * @param url
     * @return
     */
    public int addUrl (String url, int position)
    {
        imageUrls.add(position,url);
        this.notifyDataSetChanged();
        return position;
    }

    /**
     * Supprime une url/image du carrousel
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView(((View) object));
    }
}
