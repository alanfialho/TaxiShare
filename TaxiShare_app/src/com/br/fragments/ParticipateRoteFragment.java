package com.br.fragments;

import java.util.HashMap;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.RotaApp;

import com.br.network.WSTaxiShare;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class ParticipateRoteFragment extends Fragment{
	private GoogleMap googleMap;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnParticipa;
	private TextView lblOrigem, lblDestino, lblPassageiros, lblAdm, lblHora;
	private Context context;
	private RotaApp rota, rotaDetalhe;
	private SessionManagement session;
	private int id, rotaId;
	MapUtils mapUtils;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_details, container, false);
		context = getActivity();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
		}
		
		mapUtils = new MapUtils(context, googleMap);
		setAtributes(rootView);
		setBtnAction();
		setMarcadores(rotaDetalhe);
		
		
		
		
		return rootView;	
	}

	public void setAtributes(View rootView){
		session = new SessionManagement(rootView.getContext());

		mapView = (MapView) rootView.findViewById(R.id.rote_details_map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.rote_details_map)).getMap();
			if (googleMap != null) {
				//				setUpMap();
			}
		}
		Bundle args = getArguments();
		rota = args.getParcelable("rota");
		
		try{
			DetalhesRotaTask task = new DetalhesRotaTask();
			task.execute();
		}
		catch (Exception e) {
			Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
		}
		
		lblOrigem = (TextView) rootView.findViewById(R.id.rote_details_lbl_origem_info);
		lblDestino = (TextView) rootView.findViewById(R.id.rote_details_lbl_destino_info);
		lblPassageiros = (TextView) rootView.findViewById(R.id.rote_details_lbl_passageiros_info);
		lblAdm = (TextView) rootView.findViewById(R.id.rote_details_lbl_adm_nome);
		lblHora = (TextView) rootView.findViewById(R.id.rote_details_lbl_hora_info);
		btnParticipa = (Button) rootView.findViewById(R.id.rote_details_btn_participar);
		
		lblOrigem.setText(rota.getEnderecos().get(0).getRua() + ", " + rota.getEnderecos().get(0).getNumero() + " - " + rota.getEnderecos().get(0).getCidade());
		lblDestino.setText(rota.getEnderecos().get(1).getRua() + ", " + rota.getEnderecos().get(1).getNumero() + " - " + rota.getEnderecos().get(1).getCidade());
		int passageiro =  4 - rota.getPassExistentes();
		lblPassageiros.setText(passageiro + "");
		lblHora.setText(rota.getDataRota().toString());

	}
	
	//Seta os marcadores no mapa
	public void setMarcadores(RotaApp r){
		int participantes = r.getUsuarios().size();
		double[] latitudes = new double[participantes + 1];
		double[] longitudes = new double[participantes + 1];
		
		//seta o marcador do ADM e da Zoom
		latitudes[0] = Double.parseDouble(r.getEnderecos().get(1).getLatitude());
		longitudes[0] = Double.parseDouble(r.getEnderecos().get(1).getLongitude());
		String adm = r.getAdministrador().getLogin();
		String end = r.getEnderecos().get(1).getRua();
		mapUtils.setMarker(latitudes[0], longitudes[0], adm, end, true);
		
		
		//Interao numero de participantes na rota e seta marcadores para cada um deles.
		for (int i = 1; i <= participantes; i++){
			latitudes[i] = Double.parseDouble(r.getEnderecos().get(i + 2).getLatitude());
			longitudes[i] = Double.parseDouble(r.getEnderecos().get(i + 2).getLongitude());
			String titulo = r.getUsuarios().get(i - 1).getLogin();
			String rua = r.getEnderecos().get(i + 2).getRua() + ", " + r.getEnderecos().get(i + 2).getNumero() + " - " + r.getEnderecos().get(i + 2).getBairro();
			mapUtils.setMarker(latitudes[i], longitudes[i], titulo, rua, false);
		}
		
	}

	public void setBtnAction(){
		btnParticipa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//Utils.gerarToast(context, "teste");
				try{
					PartcipaRotaTask task = new PartcipaRotaTask();
					task.execute();
				}
				catch (Exception e) {
					Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
				}
			}});
	}

<<<<<<< HEAD
	public void setaMarcadores(){
		
		
	}


=======
>>>>>>> 6ac7cfe4a6eb309539fc714806e3cd95f4fc8bbf
	private class PartcipaRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;


		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			HashMap<String, String> user = session.getUserDetails();
			EnderecoApp endereco = rota.getEnderecos().get(1);
			id = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
			rotaId = rota.getId();
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.participarRota(rotaId, id, endereco);
<<<<<<< HEAD
				
				

			} catch (Exception e) {
				Utils.logException("ParticipateRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao participar rota!}";
=======
							} catch (Exception e) {
				Utils.logException("UserListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
>>>>>>> 6ac7cfe4a6eb309539fc714806e3cd95f4fc8bbf
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			Utils.gerarToast(context, "Entrou na rota");
			progress.dismiss();
		}		
	}
	
<<<<<<< HEAD
	
	private class DetalhesRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;


		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			
			rotaId = rota.getId();
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				rotaDetalhe = ws.detailRota(rotaId);
				response = "{errorCode:0, descricao:Sucesso}";
				
				

			} catch (Exception e) {
				Utils.logException("ParticipateRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro pegar detalhes rota!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			
			progress.dismiss();
		}		
	}
	
	
	

=======
>>>>>>> 6ac7cfe4a6eb309539fc714806e3cd95f4fc8bbf
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}
}
