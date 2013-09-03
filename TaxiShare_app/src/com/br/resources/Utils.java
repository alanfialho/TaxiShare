package com.br.resources;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class Utils {

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public final static void gerarToast(Context context, String message){
		Log.i("gerarToast", message);
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	public final static ProgressDialog setProgreesDialog(ProgressDialog progress, Context context, String title, String message){
		
		//Exibe janela de carregando
		progress = new ProgressDialog(context);
		progress.setTitle(title);
		progress.setMessage(message);
		progress.show();
		
		return progress;
	}
}
