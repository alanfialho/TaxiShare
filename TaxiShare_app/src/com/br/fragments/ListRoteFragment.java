package com.br.fragments;

import java.io.Serializable;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.br.activitys.R;

import com.br.adapter.MenuAdapter;
import com.br.adapter.RoteAdapter;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;


public class ListRoteFragment extends Fragment {

	private static View rootView;
	private ListView roteList;
	Context context;
	RoteAdapter adapter;
	//public static List<RotaApp> rotasBuscadas;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rote_list, container, false);
		context = getActivity();
		
		try{
			FillRoteListTask task = new FillRoteListTask();
			task.execute();
		}
		catch (Exception e) {
			Utils.logException("ListRoteFragment", "onCreateView", "", e);
		}
		setListAction();
		return rootView;
	}

	private void fillSearchList(List<RotaApp> rotas) {
		RoteAdapter roteAdapter = new RoteAdapter(context, rotas);		
		roteList = (ListView) rootView.findViewById(R.id.rote_list_list_view);
		roteList.setAdapter(roteAdapter);
		roteList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {			
				
				RotaApp rotinha = (RotaApp) roteList.getAdapter().getItem(position);
				
				//Passando a rota selecionada para tela de detalhes.			
				Bundle args = new Bundle();
				args.putParcelable("rota", rotinha);				
				Utils.changeFragment(getFragmentManager(), new ParticipateRoteFragment(), args);				
			}			
		});
	}
	

	private class FillRoteListTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		List<RotaApp> rotas;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				rotas = ws.getRotas();
				response = "{errorCode:0, descricao:Sucesso}";

			} catch (Exception e) {
				Utils.logException("ListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			fillSearchList(rotas);
			progress.dismiss();
		}		
	}
	
	private void setListAction() {

	}
	
}