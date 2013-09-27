package com.br.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.br.activitys.R;

import com.br.adapter.RoteAdapter;
import com.br.entidades.PerguntaApp;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.google.gson.Gson;

public class ListRoteFragment extends Fragment {

	private static View view;
	private ListView roteList;
	Context context;
	RoteAdapter adapter;
	//public static List<RotaApp> rotasBuscadas;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rote_list, container, false);
		
		
		adapter = null;
		try{
			FillList task = new FillList();
			task.execute();
		}
		catch (Exception e) {
			Log.i("Preenchendo lista Exception taxi", "Exception -> " + e + " || Message -> " + e.getMessage());
		}

		
		

		return view;
	}




	//Método que adicicona a lista buscada no adapter
	private void fillSearchList(View view, List<RotaApp> rotasBuscadas){
		int tamanho = rotasBuscadas.size();
		RotaApp[] rotasLista = new RotaApp[tamanho];
		
		for (int i = 0; i < tamanho; i++){
			rotasLista[i] = rotasBuscadas.get(i);
		}
		
		
		
		 // Pass the folderData to our ListView adapter
        adapter = new RoteAdapter (getActivity(),
                R.layout.rote_list_item, rotasLista);

        // Set the adapter to our ListView
        roteList = (ListView) view.findViewById(R.id.roteList);
        roteList.setAdapter(adapter);

	}	

	




	private class FillList extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		List<RotaApp>rotas = null;
		
		
		
		
		

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
			
			//chamando o método
			fillSearchList(view, rotas);
			Utils.gerarToast(getActivity(), "Rodou");
				
			progress.dismiss();
		}
	}












}

