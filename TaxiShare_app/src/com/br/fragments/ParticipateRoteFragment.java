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
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.LoginApp;
import com.br.entidades.RotaApp;

import com.br.network.WSTaxiShare;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParticipateRoteFragment extends Fragment{
	private GoogleMap googleMap;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnParticipa;
	private TextView lblOrigem, lblDestino, lblPassageiros, lblAdm, lblHora;
	private Context context;
	private RotaApp rota;
	private SessionManagement session;
	private int id, rotaId;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_details, container, false);
		context = getActivity();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
		}

		setAtributes(rootView);
		setBtnAction();
		MapUtils mapUtils = new MapUtils(context, googleMap);
		
		double destinoLatitude = Double.parseDouble(rota.getEnderecos().get(1).getLatitude());
		double destinoLongitude = Double.parseDouble(rota.getEnderecos().get(1).getLongitude());

		double origemLatitude = Double.parseDouble(rota.getEnderecos().get(0).getLatitude());
		double origemLongitude = Double.parseDouble(rota.getEnderecos().get(0).getLongitude());
			
		mapUtils.execute(destinoLatitude, destinoLongitude, origemLatitude, origemLongitude);
		
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



	private class PartcipaRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;


		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			HashMap<String, String> user = session.getUserDetails();
			EnderecoApp endereco = rota.getEnderecos().get(0);
			id = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
			rotaId = rota.getId();
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.participarRota(rotaId, id, endereco);
				
				

			} catch (Exception e) {
				Utils.logException("UserListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			Utils.gerarToast(context, "Entrou na rota");
			progress.dismiss();
		}		
	}
	
	
	

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
