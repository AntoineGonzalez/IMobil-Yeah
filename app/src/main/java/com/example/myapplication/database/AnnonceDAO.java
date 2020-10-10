package com.example.myapplication.database;

import android.content.Context;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.model.Vendeur;
import java.util.List;

/**
 * gestionnaire BDD permettant de gérer les annonces dans leur globalité.
 */
public class AnnonceDAO extends BaseDAO {

    private ProprieteDAO pDAO;
    private VendeurDAO vDAO;
    private RepresenterDAO rDAO;
    private ImageDAO iDAO;
    private PossederDAO poDAO;
    private CaracteristiqueDAO cDAO;
    private NoteDAO nDAO;

    public AnnonceDAO(Context context) {
        super(context);
        pDAO = new ProprieteDAO(context);
        vDAO = new VendeurDAO(context);
        rDAO = new RepresenterDAO(context);
        iDAO = new ImageDAO(context);
        poDAO = new PossederDAO(context);
        cDAO = new CaracteristiqueDAO(context);
        nDAO = new NoteDAO(context);
    }

    /**
     * Ajoute une annonce en bdd ( remplit les tables propriété , vendeur , image , caractéristique ...)
     * @param p
     * @return
     */
    public boolean ajouter(Propriete p) {
        myDatabase=myManager.getWritableDatabase();
        vDAO.ajouter(p.getVendeur());
        for(String url : p.getImages()){
            iDAO.ajouter(url);
            rDAO.ajouter(p.getId(), iDAO.fetch(url));
        }
        for(String content : p.getList_caracteristic()){
            cDAO.ajouter(content);
            poDAO.ajouter(p.getId(),cDAO.fetch(content));
        }
        return pDAO.ajouter(p);
    }

    /**
     * Récupére l'annonce associée à l'id passer en parametre.
     * La retourne une instance de Propriete.
     * @param id_p
     * @return
     */
    public Propriete fetch(String id_p) {
        this.open();
        Propriete annonce = pDAO.fetchPropriete(id_p);
        if (annonce != null) {
            Vendeur vendeur = vDAO.fetchVendeur(annonce.getVendeur().getId());
            annonce.setVendeur(vendeur);
            List<Integer> list_id_Image = rDAO.fetchID(id_p);
            List<Integer> list_id_Caracteristique = poDAO.fetchID(id_p);
            for(int id_c: list_id_Caracteristique){
                annonce.addCaracteristic(cDAO.fetchId(id_c));
            }
            for(int id_i : list_id_Image) {
                annonce.addImage(iDAO.fetchId(id_i));
            }
        }
        return annonce;
    }

    /**
     * Récupére toutes les annonces présente en bdd sous forme de Liste d'instance de Propriété.
     * @return
     */
    public List<Propriete> fetchAll(){
        this.open();
        List<Propriete> proprietelist = pDAO.fetchAll();
        for(Propriete p : proprietelist){
            Vendeur v = vDAO.fetchVendeur(p.getVendeur().getId());
            p.setVendeur(v);
            List<Integer> list_id_Image = rDAO.fetchID(p.getId());
            List<Integer> list_id_Caracteristique = poDAO.fetchID(p.getId());
            for(int id_c : list_id_Caracteristique){
                p.addCaracteristic(cDAO.fetchId(id_c));
            }
            for(int id_i : list_id_Image) {
                p.addImage(iDAO.fetchId(id_i));
            }
        }
        return proprietelist;
    }

    /**
     * Supprime l'annonce d'id passer en parametre de la base de donnée.
     * @param id_p
     * @return
     */
    public boolean supprimer(String id_p){
        this.open();
        Propriete propriete = fetch(id_p);
        if(propriete != null) {
            for(String url : propriete.getImages()){
                rDAO.supprimer(id_p, iDAO.fetch(url));
            }
            for(String content : propriete.getList_caracteristic()){
                poDAO.supprimer(id_p,cDAO.fetch(content));
            }
            nDAO.supprimerNote(id_p);
            pDAO.supprimer(id_p);
            myDatabase.close();
            return true;
        }else{
            myDatabase.close();
            return false;
        }
    }
}
