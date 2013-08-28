package com.br.sessions;

import java.util.HashMap;

import com.br.activitys.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManagement {
	SharedPreferences pref;
	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "TaxiSharePref";

	// Variavel que informa se usuario esta logado
	private static final String IS_LOGIN = "IsLoggedIn";

	// Dados gravados (Variaveis publicas para ter acesso externo)
	public static final String KEY_PESSOAID = "pessoaId";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_SEXO = "sexo";
	public static final String KEY_DATANASC = "datanasc";
	public static final String KEY_NICK = "nick";
	public static final String KEY_DDD = "ddd";
	public static final String KEY_CELULAR = "celular";
	public static final String KEY_LOGIN = "login";

	// Construtor
	public SessionManagement(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	//Cria um ogin na sessao
	public void createLoginSession(String id, String name, String email, String sexo, String datanasc, String nick, String ddd, String celular, String login){
		//Inicia a variavel is_login como true
		editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_PESSOAID, id);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_SEXO, sexo);
		editor.putString(KEY_DATANASC, datanasc);
		editor.putString(KEY_NICK, nick);
		editor.putString(KEY_DDD, ddd);
		editor.putString(KEY_CELULAR, celular);
		editor.putString(KEY_LOGIN, login);

		// Comita as alterações
		editor.commit();
	}   

	//Chegca se o usuario esta logado
	public void checkLogin(){
		if(!this.isLoggedIn()){
			//Caso usuario nao esteja logado, direciona para tela de login
			Log.i("Usuario não esta logado taxi" , "" );
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}

	}

	//Retorna as informações da sessao
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		//Seta a chave e depois pega o valor, caso o valor nao exista, ficara como null
		user.put(KEY_PESSOAID, pref.getString(KEY_PESSOAID, null));
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_SEXO, pref.getString(KEY_SEXO, null));
		user.put(KEY_DATANASC, pref.getString(KEY_DATANASC, null));
		user.put(KEY_NICK, pref.getString(KEY_NICK, null));
		user.put(KEY_DDD, pref.getString(KEY_DDD, null));
		user.put(KEY_CELULAR, pref.getString(KEY_CELULAR, null));
		user.put(KEY_LOGIN, pref.getString(KEY_LOGIN, null));

		return user;
	}

	public void logoutUser(){
		//Limpa o shared preferences
		editor.clear();
		editor.commit();

		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	// Verifica se o usuario esta logado
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}