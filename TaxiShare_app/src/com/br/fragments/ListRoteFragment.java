package com.br.fragments;

import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.br.activitys.R;
import com.br.adapter.RoteAdapter;
import com.br.entidades.EnderecoApp;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;


public class ListRoteFragment extends Fragment {

	private static View rootView;
	private ListView roteList;
	Context context;
	RoteAdapter adapter;
	public static Address destination, origin;
	private Button btnCriar;
	private int idUser;
	private SessionManagement session;
	//public static List<RotaApp> rotasBuscadas;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rote_list, container, false);
		context = getActivity();
		Bundle args = getArguments();
		List<RotaApp> rotas = (List<RotaApp>) args.getSerializable("rotas");
		destination = args.getParcelable("destinoAddress");
		origin = args.getParcelable("origemAddress");
		session = new SessionManagement(rootView.getContext());

		fillSearchList(rotas);	
		setBtnAction();

		return rootView;
	}

	private void fillSearchList(List<RotaApp> rotas) {
		HashMap<String, String> user = session.getUserDetails();
		idUser = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
		
		btnCriar = (Button) rootView.findViewById(R.id.rote_list_btn_criar);

		
		
		
		
		
		RoteAdapter roteAdapter = new RoteAdapter(context, rotas);		
		roteList = (ListView) rootView.findViewById(R.id.rote_list_list_view);		
		roteList.setAdapter(roteAdapter);
		
		
		
		
/*		for (int i = 0; i < roteList.getCount(); i++){
			RotaApp rotinha = (RotaApp) roteList.getAdapter().getItem(i);
			int id = rotinha.getAdministrador().getId();
			if (idUser == id){
				roteList.removeViewAt(i);
				
			}
		}*/
		
		
		
		
		roteList.setDivider(new ColorDrawable(0xFFfbad25));
		roteList.setDividerHeight(2);
		roteList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {			
				
				RotaApp rotinha = (RotaApp) roteList.getAdapter().getItem(position);
				
				//Passando a rota selecionada para tela de detalhes.			
				Bundle args = new Bundle();
				args.putParcelable("rota", rotinha);
				args.putParcelable("destinoAddress", destination);
				Utils.changeFragment(getFragmentManager(), new ParticipateRoteFragment(), args);				
			}			
		});
	}
	
	private void setBtnAction() {
		btnCriar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				Bundle args = new Bundle();
				args.putParcelable("origemAddress", origin);
				args.putParcelable("destinoAddress", destination);
				Utils.changeFragment(getFragmentManager(), new CreateRoteFragment(), args);
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
	

	
}