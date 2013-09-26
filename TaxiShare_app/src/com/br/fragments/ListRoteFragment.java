package com.br.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.br.resources.Utils;
import com.google.gson.Gson;

public class ListRoteFragment extends Fragment {

	private static View view;
	private ListView RoteList;
	public TextView txtEndereco1, txtEndereco2;




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rote_list_item, container, false);

		fillSearchList(view);

		return view;
	}


	//Classe que vai segurar os atributos da lista


	private void fillSearchList(View view){


	};	














	public void setEnderecos(String endereco1, String endereco2){
	}


	private void setRotes(String resposta){

		/*

		try {
			JSONObject json = new JSONObject(resposta);

			Log.i("resposta taxi", resposta);

			if (json.getInt("errorCode") == 0) {
				Gson gson = new Gson();
				ArrayList<RotaApp> listaRotas = new ArrayList<RotaApp>(); 

				json = json.getJSONObject("data");
				JSONArray array = json.getJSONArray("listaRotas");

				for (int i = 0; i < array.length(); i++) {
					listaRotas.add(gson.fromJson(array.get(i), RotaApp.class));
				}




			}


		}
		catch (JSONException e) {
			Log.i("Exeception onPostExecute fillQuestionSpinner taxi", "Exception -> " + e + "  || Message: -> " + e.getMessage());			

		}*/
	}
}

