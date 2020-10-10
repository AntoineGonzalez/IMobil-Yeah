package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ApiProprieteListAdapter;
import com.example.myapplication.adapter.ProprieteAdapter;
import com.example.myapplication.database.AnnonceDAO;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.model.ProprieteList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Activité principale de l'application, elle est la première activité lancée au démarrage. C'est la page d'accueil.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // Conteneur des annonces
    private List<Propriete> proprieteList = new ProprieteList(); // Liste des propriétés / des annonces

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisationToolBar(); //initialise la bar de navigation
        initialisationRecyclerview(); //initialisation recyclerView
        start_okhttp_request(); //Requete http pour recupérer la MockAPI
    }

    /**
     * Function qui initialise notre bar de navigation et lui change son label.
     */
    public void initialisationToolBar(){
        Toolbar annonceToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        this.setSupportActionBar(annonceToolbar);
        this.getSupportActionBar().setTitle("Accueil");
    }

    /**
     * Fonction qui initialise notre recyclerView.
     */
    private void initialisationRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAccueil);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            case R.id.menu_action_annonces: startListAnnonceActivity(); // lance l'activiter avec toute les annonces.
                break;
            case R.id.menu_action_favoris: startListFavoriteActivity(); // lance l'activité avec tous les favoris.
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fonction qui lance la requete OkHttp vers la mockAPI.
     * @return
     */
    public Propriete start_okhttp_request(){
        if(checkConnectivity(this)){ // on check la connexion internet
            JsonHttpRequest("https://ensweb.users.info.unicaen.fr/android-estate/mock-api/liste.json"); //on démarre la requete
        }else{
            Snackbar.make(findViewById(R.id.main),"Aucune connexion disponible", Snackbar.LENGTH_LONG).show();
        }
        return null;
    }

    /**
     * Retourner un boolean qui indique si on est connecter ou non a internet.
     * @param context
     * @return
     */
    public static Boolean checkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        if (activenetwork != null && activenetwork.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * fait une requete okhttp sur l'url passer en parametre et retourne le string obtenue
     * @param url
     * @return
     */
    public void JsonHttpRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected HTTP code" + response);
                    }
                    else{
                        final String result=responseBody.string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    actGetResponse(result);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Utilise l'api MOSHI pour parser le contenue Json passer en parametre.
     * Remplit la liste d'annonce et le recyclerView avec le contenue récupérer.
     * @param content
     * @throws IOException
     */
    public void actGetResponse(String content) throws IOException {
        Type type = Types.newParameterizedType(List.class,Propriete.class);
        Moshi moshi = new Moshi.Builder().add(new ApiProprieteListAdapter()).build(); //initialisation moshi builder et ajout du ListProprieteAdapteur.
        JsonAdapter<List<Propriete>> adapter = moshi.adapter(type);
        try{
            List<Propriete> p = adapter.fromJson(content);
            for(Propriete h : p ){
                this.proprieteList.add(h);
            }
            Collections.sort(this.proprieteList); //on tri les annonces dans l'ordre chronologique pour avoir les annonces les plusrécentes
            recyclerView.setAdapter(new ProprieteAdapter(this.proprieteList.subList(0,10),this)); //On affiche seulement les dix premières dans le recyclerView.
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * lance l'activité ListedAnnonceActivity en mode non favori (affichant toutes les annonces de l'API).
     */
    public void startListAnnonceActivity(){
        Intent i = new Intent(this,ListedAnnonceActivity.class);
        i.putExtra("typeAnnonce" , "noFav"); //on precise le mode "noFav" pour non favoris
        i.putExtra("liste_annonce",(Parcelable)proprieteList); // transmet la liste d'annonce récuperer avec okHttp
        startActivity(i);
    }

    /**
     * lance l'activité ListedAnnonceActivity en mode favori (affichant toutes les annonces enregistrer en favori).
     */
    public void startListFavoriteActivity() {
        AnnonceDAO bdd = new AnnonceDAO(this);
        if(bdd.fetchAll().isEmpty()){ // si aucune annonces est mise en favoris on avertit l'utilisateur.
            Snackbar.make(findViewById(R.id.main),"Vous avez aucune annonce en favori.", Snackbar.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, ListedAnnonceActivity.class);
            i.putExtra("typeAnnonce", "Fav"); //on precise le mode "Fav" pour favoris
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
        i.putExtra("typeAnnonce","noFav"); // type d'annonce non favoris
        i.putExtra("annonce",p); // transmet l'annonce de proprité à afficher
        startActivity(i);
    }
}