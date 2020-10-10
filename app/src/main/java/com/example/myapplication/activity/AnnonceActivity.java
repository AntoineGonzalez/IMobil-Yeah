package com.example.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.database.AnnonceDAO;
import com.example.myapplication.model.Propriete;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activité qui affiche le détails d'une annonce.
 */
public class AnnonceActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ViewPagerAdapter viewPagerAdapter; // adpateur du carrousel(ViewPager)
    private List<String> listeImagesPath = new ArrayList<>(); // liste contenant les urls de toutes les images de l'annonce (images en ligne + photos)
    private String currentPhotoPath; // chemin absolue du fichier de la derniere photo prise
    private String typeAnnonce; //type annonce "fav" ou  "noFav".

    //vues associées aux attributs de Propriété:
    private ViewPager image_v;
    private TextView date_v;
    private TextView titre_v;
    private TextView prix_v;
    private TextView adresse_v;
    private TextView description_v;
    private TextView caracteristique_v;
    private TextView vendeur_v;
    private TextView phone_v;
    private TextView email_v;
    private Button bouttonFav;

    private Propriete propriete; // instance de propriété que l'on affiche.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);

        initialisationToolBar();

        //recuperation element et widget de la vue
        image_v = (ViewPager) findViewById(R.id.ViewPagerAnnonce);
        date_v = (TextView) findViewById(R.id.date_textView);
        titre_v = (TextView) findViewById(R.id.title_textView);
        prix_v = (TextView) findViewById(R.id.prix_textView);
        adresse_v = (TextView) findViewById(R.id.adresse_textView);
        description_v = (TextView) findViewById(R.id.description_textView);
        caracteristique_v = (TextView) findViewById(R.id.caracteristique_textView);
        vendeur_v = (TextView) findViewById(R.id.vendeur_textView);
        email_v = (TextView) findViewById(R.id.mail_textView);
        phone_v = (TextView) findViewById(R.id.phone_textView);
        bouttonFav =(Button) findViewById(R.id.Addbutton);

        // recuperation de l'annonce
        Intent intent = getIntent();
        if (intent != null) {
            propriete = intent.getParcelableExtra("annonce");
            this.typeAnnonce=getIntent().getStringExtra("typeAnnonce");
            if (propriete != null) {
                listeImagesPath=propriete.getImages(); // ajout des images de l'annonce à la liste d'url.
                if(typeAnnonce.matches("Fav")){
                    getLocalPhoto(); // ajoute les chemins des photos à liste d'url.
                }
                viewPagerAdapter = new ViewPagerAdapter(this,listeImagesPath); // adapte la liste d'image au carrousel/viewPager pourqu'il puisse les afficher.
                afficher_annonce(propriete); // affichage de l'annonce
            }
        }

        if(typeAnnonce.matches("Fav")){
            bouttonFav.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Function qui initialise notre bar de navigation et lui change son label.
     */
    public void initialisationToolBar(){
        Toolbar annonceToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        this.setSupportActionBar(annonceToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Details de l'annonce");
    }

    /**
     * Fonction qui associe notre menu de navigation au menu xml définit en paramétre.
     * Mise en place des items de la bar de navigation.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(typeAnnonce.matches("Fav")) {
            getMenuInflater().inflate(R.menu.annonce_menu, menu);
        }else{
            getMenuInflater().inflate(R.menu.liste_annonce_menu,menu);
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
            case R.id.menu_action_note: start_note_activity();
            break;
            case R.id.menu_action_camera: start_camera_activity();
            break;
            case R.id.menu_action_favoris:startFavorisActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * fonction qui initialise et lance l'activité Camera.
     * elle permet la prise de photo pour une annonce.
     */
    private void start_camera_activity() {
        Intent intent_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // intent pour lance la camera
        if (intent_picture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); //creation du fichier photo
            } catch (IOException ex) {
                System.out.println("file creation bug");
            }
            // continue seulement si le fichier est bien créer.
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileProvider",
                        photoFile);

                //Démarre l'activité caméra et lui donne le fichier où elle doit enregistrer la photo.
                intent_picture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent_picture, REQUEST_IMAGE_CAPTURE);
            }
            listeImagesPath.add(listeImagesPath.size(),currentPhotoPath); // ajoute le chemin de la photo créer à la liste global.
            viewPagerAdapter.notifyDataSetChanged(); //on notify le carrousel via son adapteur qu'une nouvelle photo est à afficher.
        }
    }

    /**
     * Créer le fichier où sauvegarder la photo.
     * Le nom du fichier contient l'id de la propriété pour savoir qu'il lui appartient.
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+propriete.getId()+"_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //dossier où placer le fichier .jpg (dossier Pictures de l'App)
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* dossier */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Récupére les fichiers photos associer à l'annonce et ajoute leur chemein à listeImagesPath
     */
    public void getLocalPhoto(){
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] listfile = storageDir.listFiles();
        for(File f : listfile){
            if(f.getAbsolutePath().contains(propriete.getId())){ //si le fichier contient l'id d'une annonce alors il lui appartient.
                listeImagesPath.add(f.getAbsolutePath());
            }
        }
    }

    /**
     * Démarre l'activité NoteActivity qui permet de commenter une annonce.
     */
    private void start_note_activity() {
        Intent i = new Intent(this,NoteActivity.class);
        i.putExtra("propriete",this.propriete);
        startActivity(i);
    }

    /**
     * Remplit la vue avec les attributs de la proprite passé en parametre.
     * @param p
     */
    public void afficher_annonce(Propriete p){
        String carac_text=Integer.toString(p.getNb_principal_piece())+ " pièces\n";
        image_v.setAdapter(viewPagerAdapter);
        titre_v.setText(p.getTitre());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date_text=dateFormat.format(new Date(p.getDate() * 1000L));
        date_v.setText("mise en ligne le : "+date_text);
        prix_v.setText(Integer.toString(p.getPrix())+"€");
        adresse_v.setText(p.getVille()+"\n"+p.getCode_postal());
        description_v.setText(p.getDescription());
        for(String c : p.getList_caracteristic()){
            String s=c+"\n";
            carac_text+=s;
        }
        caracteristique_v.setText(carac_text);
        vendeur_v.setText(p.getVendeur().getPrenom()+" "+p.getVendeur().getNom());
        phone_v.setText(p.getVendeur().getTelephone());
        email_v.setText(p.getVendeur().getMail());
    }

    /**
     * fonction appeler lorsque l'on clique sur le bouton ajouter.
     * elle ajoute l'annonce au annonce favori.
     * @param view
     */
    public void addFavorite(View view){
        AnnonceDAO annonceDAO = new AnnonceDAO(this); //gestionnaire Bdd
        if(!annonceDAO.ajouter(propriete)){ // ajout en bdd de l'annonce
            Snackbar.make(findViewById(R.id.annonce),"Annonce deja en favorie", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * lance l'activité ListedAnnonceActivity en mode favori (affichant toutes les annonces enregistrer en favori).
     */
    private void startFavorisActivity() {
        AnnonceDAO bdd = new AnnonceDAO(this);
        if(bdd.fetchAll().isEmpty()){
            Snackbar.make(findViewById(R.id.annonce),"Vous avez aucune annonce en favori.", Snackbar.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, ListedAnnonceActivity.class);
            i.putExtra("typeAnnonce", "Fav");
            startActivity(i);
        }
    }

}

