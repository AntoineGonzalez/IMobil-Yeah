package com.example.myapplication.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NoteAdapter;
import com.example.myapplication.database.NoteDAO;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.util.NoteViewHolder;
import com.example.myapplication.util.RecyclerItemTouchHelper;
import com.example.myapplication.util.RecyclerItemTouchHelperListner;

import java.util.ArrayList;
import java.util.List;

/**
 * Activité qui permet d'ajouter des notes/commentaires sur une annonce.
 */
public class NoteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    private NoteDAO noteDAO= new NoteDAO(this); // gestionnaire bdd table Note
    private Propriete propriete; // annonce que l'on commente
    private Button addnote; // bouton de validation du commentaire
    private List<String> listeNote = new ArrayList<>(); // liste des commentaires
    private EditText saisie; // champ de saisie du commentaire
    private RecyclerView recyclerView; // conteneur de la vue qui affiche les commentaires
    private ConstraintLayout rootLayout;
    private NoteAdapter adapteur; // adapteur du recyclerView, transforme le String en item pour le recyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        rootLayout= (ConstraintLayout) findViewById(R.id.noteLayout);
        addnote= findViewById(R.id.addNoteButton);
        saisie=findViewById(R.id.Saisietext);

        Intent intent = getIntent();
        if(intent != null){
            propriete=intent.getParcelableExtra("propriete");
        }

        // on recupére tout les commentaires de la bdd associés à l'annonce
        listeNote = noteDAO.fetchAll(propriete);

        // on les ajoute au recyclerView via l'adapteur.
        adapteur = new NoteAdapter(listeNote,this);

        // initialisation recyclerView et mise en place animation Swap sur les items
        recyclerView = findViewById(R.id.note_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapteur);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack
                =new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        //initialisation bat de navigation.
        initialisationToolBar();

    }

    /**
     * Function qui initialise notre bar de navigation et lui change son label.
     */
    public void initialisationToolBar(){
        Toolbar annonceToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        this.setSupportActionBar(annonceToolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Commentaires:");
    }


    /**
     * Fonction qui ajoute une note/un commentaire en bdd
     * @param v
     */
    public void addNote(View v){
        String content = saisie.getText().toString(); // recupere le String du commentaire
        noteDAO.ajouterNote(content, propriete.getId()); // gestionnaire bdd
        saisie.setText(""); // on vide le champ de saisie
        listeNote = noteDAO.fetchAll(propriete); // on recupére la nouvelle liste
        adapteur  = new NoteAdapter(listeNote,this);
        recyclerView.setAdapter(adapteur);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack
                =new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
    }

    /**
     * Fonction appeler lorsque l'on swap un item du recyclerView, elle supprime le commentaire, met a jour la bdd et la vue.
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof NoteViewHolder)
        {
            final String deletedItem = listeNote.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            adapteur.removeItem(viewHolder.getAdapterPosition());
            noteDAO.supprimerNote(propriete, deletedItem);
            Snackbar snackbar = Snackbar.make(rootLayout," Suppression en cour ! ",
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("Annuler", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapteur.restoreItem(deletedItem,deleteIndex);
                    noteDAO.ajouterNote(deletedItem,propriete.getId());
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}