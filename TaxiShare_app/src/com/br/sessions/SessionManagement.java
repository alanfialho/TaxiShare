package com.br.sessions;
 
import java.util.HashMap;

import com.br.activitys.LoginActivity;
 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "TaxiSharePref";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_SEXO = "sexo";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_DATANASC = "datanasc";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_NICK = "nick";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_DDD = "ddd";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_CELULAR = "celular";
    
  
     
    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email, String sexo, String datanasc, String nick, String ddd, String celular){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        
     // Storing email in pref
        editor.putString(KEY_SEXO, sexo);
        
     // Storing email in pref
        editor.putString(KEY_DATANASC, datanasc);
        
     // Storing email in pref
        editor.putString(KEY_NICK, nick);
        
     // Storing email in pref
        editor.putString(KEY_DDD, ddd);
        
     // Storing email in pref
        editor.putString(KEY_CELULAR, celular);
         
        // commit changes
        editor.commit();
    }   
     
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
        }
         
    }
     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        
     // user email id
        user.put(KEY_SEXO, pref.getString(KEY_SEXO, null));
        
     // user email id
        user.put(KEY_DATANASC, pref.getString(KEY_DATANASC, null));
        
     // user email id
        user.put(KEY_NICK, pref.getString(KEY_NICK, null));
        
     // user email id
        user.put(KEY_DDD, pref.getString(KEY_DDD, null));
        
     // user email id
        user.put(KEY_CELULAR, pref.getString(KEY_CELULAR, null));
         
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}