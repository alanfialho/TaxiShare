package com.br.fragments;

import java.util.HashMap;
import com.br.activitys.R;
import com.br.adapter.RoteAdapter;
import com.br.adapter.UserRoteAdapterAdm;
import com.br.adapter.UserRoteAdapterParticipate;
import com.br.entidades.LoginApp;
import com.br.entidades.RotaApp;

import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;
import com.commonsware.cwac.merge.MergeAdapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class UserListRoteFragment extends Fragment{
	
	private static View rootView;
	private ListView roteList;
	Context context;
	RoteAdapter adapterAdm, adapterParticipate;
	private SessionManagement session;
	private int id;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rote_users_list, container, false);
		context = getActivity();
		session = new SessionManagement(rootView.getContext());
		try{
			FillRoteListTask task = new FillRoteListTask();
			task.execute();
		}
		catch (Exception e) {
			Utils.logException("UserListRoteFragment", "onCreateView", "", e);
		}
		
		return rootView;
	}
	
	
	private void fillSearchList(LoginApp login) {
		UserRoteAdapterAdm adapterAdm = new UserRoteAdapterAdm (context, login);
		UserRoteAdapterParticipate adapterParticipate = new UserRoteAdapterParticipate (context, login);
		MergeAdapter merge = new MergeAdapter();
		merge.addAdapter(adapterAdm);
		merge.addAdapter(adapterParticipate);
		
		roteList = (ListView) rootView.findViewById(R.id.rote_users_list_view);
		roteList.setAdapter(merge);
		roteList.setDivider(new ColorDrawable(0xFFfbad25));
		roteList.setDividerHeight(2);
		if(roteList.getCount() == 0){
			
			LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.rote_user_list_layout_linear);
			linearLayout.removeView(roteList);
			TextView txt1 = new TextView(context);
			txt1.setText("Voce ainda não esta em nenhuma Rota");
			txt1.setGravity(Gravity.CENTER);
			txt1.setTextSize(14);
			txt1.setTextColor(Color.GRAY);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    llp.setMargins(100, 400, 0, 0); // llp.setMargins(left, top, right, bottom);
		    txt1.setLayoutParams(llp);
			linearLayout.addView(txt1);

		}else {
			roteList.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {			
					RotaApp rotinha = (RotaApp) roteList.getAdapter().getItem(position);
					Bundle args = new Bundle();
					args.putParcelable("rota", rotinha);				
					Utils.changeFragment(getFragmentManager(), new DetailsUserListRoteFragment(), args);
				}			
			});
		}
	}
	
	public void mandaSMS(String telefone) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.putExtra("sms_body", "");
        smsIntent.putExtra("address", telefone);
        smsIntent.setType("vnd.android-dir/mms-sms");

        startActivity(smsIntent);
	}

	private void efetuaLigacao(String telefone) {
		String phoneCallUri = "tel:" + telefone;
		Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
		phoneCallIntent.setData(Uri.parse(phoneCallUri));
		startActivity(phoneCallIntent);
	}
	
	public void entraEmContato(final String telefone){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Contato");

		// set dialog message
		alertDialogBuilder
		.setMessage("Como você deseja entrar em contato?")
		.setCancelable(false)
		.setPositiveButton("Ligar",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//
				efetuaLigacao(telefone);
			}
		})
		.setNeutralButton("SMS",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//
				mandaSMS(telefone);
			}
		})
		.setNegativeButton("Cancela",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	
	
	private class FillRoteListTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		LoginApp login;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			HashMap<String, String> user = session.getUserDetails();
			id = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
			
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				login = ws.myRotes(id);
				response = "{errorCode:0, descricao:Sucesso}";

			} catch (Exception e) {
				Utils.logException("UserListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			fillSearchList(login);
			progress.dismiss();
		}		
	}

}
