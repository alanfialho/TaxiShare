package com.br.fragments;

import java.util.HashMap;
import java.util.List;

import com.br.activitys.R;
import com.br.adapter.RoteAdapter;
import com.br.adapter.UserRoteAdapterAdm;
import com.br.entidades.LoginApp;
import com.br.entidades.RotaApp;

import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class UserListRoteFragment extends Fragment{
	
	private static View rootView;
	private ListView roteListAdm, roteListParticipate;
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
		UserRoteAdapterAdm roteAdapter = new UserRoteAdapterAdm (context, login);
		
		roteListAdm = (ListView) rootView.findViewById(R.id.rote_users_list_view);
		roteListAdm.setAdapter(roteAdapter);
		
		
		

			
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
