package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.adapter.ProprieteAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AnnonceDAO;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.util.ProprieteViewHolder;
import com.example.myapplication.util.RecyclerItemTouchHelperAnnonce;
import com.example.myapplication.util.RecyclerItemTouchHelperListner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Actitivité qui affiche une liste d'annonce dans un recyclerView.
 * En mode Fav elle affiche les annonces enregistrer en favori.
 * Sinon elle affiche toutes les annonces de la mockAPI.
 */
public class ListedAnnonceActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {
    private String typeAnnonce; // mode de l'annonce "Fav" ou "nonFav"
    private RecyclerView recyclerView; //Vue conteneur des annonces
    private List<Propriete> proprieteList = new ArrayList<>(); //Liste contenant les instaneces de propriete/annonce.
    private ProprieteAdapter adapter; //adapteur qui transforme une instance de propriete en vue pour le recyclerView.
    private ConstraintLayout rootLayout;
    private boolean restoreflag=false; //flag qui vaut true si on annule la supression d'une annonce.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_annonce);

        Intent intent = getIntent(); //recupération intent
        if (intent != null) {
            proprieteList = intent.getParcelableExtra("liste_annonce");
            typeAnnonce = intent.getStringExtra("typeAnnonce");
        }

        rootLayout= (ConstraintLayout) findViewById(R.id.annonce_layout);
        initialisationToolBar();
        initialisationRecyclerview();

        //mise en place de l'adapteur pour le recyclerView
        if(typeAnnonce.matches("noFav")){
            // mode nonFav donc on lui fourni toutes les annonces de l'api
            adapter=new ProprieteAdapter(this.proprieteList,this);
        }else{
            //mode Fav donc on questionne la base de donnée SQLite pour récupérer les annonces enregistrer et on lui fourni.
            AnnonceDAO bdd = new AnnonceDAO(this);
            proprieteList = bdd.fetchAll();
            //code permettant la mise en place de l'animation Swap d'un item du recyclerView
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            adapter=new ProprieteAdapter(this.proprieteList,this);
            ItemTouchHelper.SimpleCallback itemTouchHelperCallBack
                    =new RecyclerItemTouchHelperAnnonce(0,ItemTouchHelper.LEFT,this);
            new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        }
        recyclerView.setAdapter(adapter);
    }

    /**
     * Function qui initialise notre bar de navigation et lui change son label.
     */
    public void initialisationToolBar(){
        Toolbar annonceToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        this.setSupportActionBar(annonceToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(typeAnnonce.matches("noFav")){
            this.getSupportActionBar().setTitle("Toutes nos annonces");
        }else{
            this.getSupportActionBar().setTitle("Annonces favorites");
        }
    }

    /**
     * Fonction qui initialise notre recyclerView.
     */
    private void initialisationRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.listed_announce_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Fonction qui associe notre menu de navigation au menu xml définit en paramétre.
     * Mise en place des items de la bar de navigation.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(typeAnnonce.matches("noFav")) {
            getMenuInflater().inflate(R.menu.liste_annonce_menu, menu);
        }
        return true;
    }

    /**
     * Fonction qui définit les actions à réaliser lorsque l'on clique sur un item du menu de navigation.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_action_favoris: startFavorisActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * lance l'activité ListedAnnonceActivity en mode favori (affichant toutes les annonces enregistrer en favori).
     */
    private void startFavorisActivity() {
        AnnonceDAO bdd = new AnnonceDAO(this);
        if(bdd.fetchAll().isEmpty()){
            Snackbar.make(findViewById(R.id.annonce_layout),"Vous avez aucune annonce en favori.", Snackbar.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, ListedAnnonceActivity.class);
            i.putExtra("typeAnnonce", "Fav");
            startActivity(i);
        }
    }

    /**
     * fonction appelé lorsque l'on clique sur un item du recyclerView (une annonce).
     * Elle prend l'instance de propriété correspondant à l'annonce et lance l'activité de son détail.
     * @param view
     */
    public void click (View view) {
        int id = recyclerView.getChildAdapterPosition((View) view.getParent()); // je recupere la position de l'annonce dans la liste des annonce
        Propriete p = proprieteList.get(id); // ici mon instance de propriete
        Intent i =new Intent(this, AnnonceActivity.class);
        i.putExtra("typeAnnonce",this.typeAnnonce);
        i.putExtra("annonce",p);
        i.putExtra("parent","ListedAnnonceActivity.Class");
        startActivity(i);
    }

    /**
     * Fonction appeler lorsque l'on swap un item du recylerView (une annonce).
     * Elle lance alors le processus de supression d'une annonce.
     * Fonction disponible que en mode "Fav".
     *
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ProprieteViewHolder)
        {
            final Propriete deletedItem = proprieteList.get(viewHolder.getAdapterPosition()); //récupérer l'instance de propriete à supprimer
            final int deleteIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(viewHolder.getAdapterPosition()); // on utlise l'adapteur pour notifier le recyclerView qu'un item est supprimer.
            final AnnonceDAO annonceDAO = new AnnonceDAO(this); //gestionnaire de base de donnée
            annonceDAO.supprimer(deletedItem.getId()); //on supprime l'annonce de la bdd.

            //mise en place du snackbar qui alerte une supression.
            Snackbar snackbar = Snackbar.make(rootLayout, "Suppression en cour!", Snackbar.LENGTH_LONG);
            //mise en place d'une action pour annulez la supression
            snackbar.setAction("Annuler", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restoreflag=true;
                    adapter.restoreItem(deletedItem,deleteIndex); // on utilise l'adapteur pour restorer l'item supprimer.
                    annonceDAO.ajouter(deletedItem); // on le rajoute en bdd
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(!restoreflag){
                        supprimer_photos(deletedItem); // on supprime les photos prise par l'utlisateur (avec la camera;
                    }
                    restoreflag=false;
                }
            });
            snackbar.show();

        }
    }

    /**
     * Fonction qui supprime les photos d'une annonce prises par un utilisateur
     * @param propriete
     */
    public void supprimer_photos(Propriete propriete){
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // acces au dossier local picture
        File[] listfile = storageDir.listFiles(); // on recupere le contenue du dossier
        for(File file : listfile){
            if(file.getAbsolutePath().contains(propriete.getId())){ // on regarde les fichiers contenant l'id de l'annonce
                file.delete(); // on supprime le fichier
            }
        }
    }
}
