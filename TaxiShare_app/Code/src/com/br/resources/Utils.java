package com.br.resources;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.br.activitys.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateFormat;
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
	
	public final static void geraDialogInformacao(String titulo, String mensagem, Context context){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		
		builder1.setTitle(titulo);
		builder1.setMessage(mensagem);

		builder1.setNeutralButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
			}
		});
		builder1.show();

	}
	
	public static String convertTo24HoursFormat(String twelveHourTime) throws java.text.ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		java.util.Date date = formatter.parse(twelveHourTime);
		twelveHourTime = date.toString();
		return twelveHourTime;
	  
	}

	public final static void gerarToast(Context context, String message){
		Log.i("gerarToast", message);
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, message, duration);
		//toast.setGravity(gravity, xOffset, yOffset)
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
