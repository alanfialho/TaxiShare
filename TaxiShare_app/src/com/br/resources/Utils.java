package com.br.resources;

import com.br.activitys.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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

	public final static void logException(String clazz, String method, String extra, Exception e){
		Log.i(clazz + " " + method + " " + extra + " taxi", "ex -> " + e + "message -> " + e.getMessage());
	}

	public final static ProgressDialog setProgreesDialog(ProgressDialog progress, Context context, String title, String message){

		//Exibe janela de carregando
		progress = new ProgressDialog(context);
		progress.setTitle(title);
		progress.setMessage(message);
		progress.show();

		return progress;
	}
	
	public final static void changeFragment(FragmentManager manager, Fragment fragment, Bundle args){
		if(args == null)
			args = new Bundle();
		
		FragmentManager fragmentManager = manager;
		FragmentTransaction ftransaction = fragmentManager.beginTransaction();
		fragment.setArguments(args);
		ftransaction.replace(R.id.content_frame, fragment);
		ftransaction.addToBackStack(null);
		ftransaction.commit();
	}
}
