package com.vanberlo.blake.newname_android;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.Models.Name;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmService {

    // Realm object for this thread
    private Realm realm;

    public RealmService(){
        realm = Realm.getDefaultInstance(); // Get a realm instance for the thread
    }

    /***
     * Insert a new name into the Realm DB.
     * @param name The string for the name
     * @param gender The gender of the name
     */
    public void insertName(String name, Gender gender){
        Number maxId = realm.where(Name.class).max("id"); // Get the current max id in the Name table
        int id = (maxId == null) ? 1 : maxId.intValue() + 1; // Generate a unique id for the new name
        Name newName = new Name(id, name, gender);

        realm.beginTransaction();
        realm.insert(newName); // Insert new name into the DB
        realm.commitTransaction();
        return;
    }

    /***
     * Delete a name from the Realm DB.
     * @param id The ID of the name object
     */
    public void deleteNameWithId(long id){
        Name row = realm.where(Name.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        row.deleteFromRealm();
        realm.commitTransaction();
        return;
    }

    /***
     * Delete all saved names in the Realm DB.
     */
    public void deleteAllNames() {
        RealmResults<Name> rows = realm.where(Name.class).findAll();
        realm.beginTransaction();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
        return;
    }

    /***
     * Get all the names in the Realm DB
     * @return a list of Name objects. Note that RealmResults<Name> implements List<Name>.
     */
    public RealmResults<Name> getAllNames(){
        return realm.where(Name.class).findAll().sort("name");
    }


    public RealmResults<Name> getNamesWithText(String searchTerm){
        if(searchTerm.length() > 0) {
            //searchTerm = searchTerm.substring(0, 1).toUpperCase() + searchTerm.substring(1); // Ensure first character is uppercase
            return realm.where(Name.class).contains("name", searchTerm, Case.INSENSITIVE).findAll().sort("name");
        }
        else {
            return getAllNames();
        }
    }

    public RealmResults<Name> getNamesWithTextAndGender(String searchTerm, int gender){
        if(searchTerm.length() > 0){
            return realm.where(Name.class).equalTo("gender", gender).findAll().sort("name");
        }
        else {
            return realm.where(Name.class).equalTo("gender", gender).contains("name", searchTerm, Case.INSENSITIVE).findAll().sort("name");
        }
    }


}
