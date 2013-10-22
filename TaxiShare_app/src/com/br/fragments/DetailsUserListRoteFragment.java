package com.br.fragments;

import com.br.activitys.R;
import com.br.entidades.RotaApp;

import com.br.network.WSTaxiShare;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class DetailsUserListRoteFragment extends Fragment{
	private GoogleMap googleMap;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnSairRota;
	private TextView lblOrigem, lblDestino, lblPassageiros, lblAdm, lblHora;
	private Context context;
	private RotaApp rota, rotaDetalhe;
	private MapUtils mapUtils;
	private int rotaId;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_users_details, container, false);
		context = getActivity();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("DetailsUserListRoteFragment", "onCreateView", "", e);
		}
		mapUtils = new MapUtils(context, googleMap);
		setAtributes(rootView);
		
		return rootView;	
	}
	
	public void setAtributes(View rootView){
		mapView = (MapView) rootView.findViewById(R.id.rote_users_details_map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.rote_users_details_map)).getMap();
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
			Utils.logException("DetailsUserListRoteFragment", "onCreateView", "", e);
		}
		
		lblOrigem = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_origem_info);
		lblDestino = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_destino_info);
		lblPassageiros = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_passageiros_info);
		lblAdm = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_adm_nome);
		lblHora = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_hora_info);
		btnSairRota = (Button) rootView.findViewById(R.id.rote_users_details_btn_sair);
		
		lblOrigem.setText(rota.getEnderecos().get(0).getRua() + ", " + rota.getEnderecos().get(0).getNumero() + " - " + rota.getEnderecos().get(0).getCidade());
		lblDestino.setText(rota.getEnderecos().get(1).getRua() + ", " + rota.getEnderecos().get(1).getNumero() + " - " + rota.getEnderecos().get(1).getCidade());
		int passageiro =  4 - rota.getPassExistentes();
		lblPassageiros.setText(passageiro + "");
		lblHora.setText(rota.getDataRota().toString());
	}

	public void setMarcadores(){
		int participantes = 0;
		participantes += rotaDetalhe.getUsuarios().size();
		double[] latitudes = new double[participantes + 1];
		double[] longitudes = new double[participantes + 1];

		//seta o marcador do ADM e da Zoom
		latitudes[0] = Double.parseDouble(rotaDetalhe.getEnderecos().get(1).getLatitude());
		longitudes[0] = Double.parseDouble(rotaDetalhe.getEnderecos().get(1).getLongitude());
		String adm = rotaDetalhe.getAdministrador().getLogin();
		String end = rotaDetalhe.getEnderecos().get(1).getRua() + ", " + rotaDetalhe.getEnderecos().get(1).getNumero() + " - " + rotaDetalhe.getEnderecos().get(1).getBairro();
		setMarker(latitudes[0], longitudes[0], adm, end, true);

		if (participantes > 0){
			//Interao numero de participantes na rota e seta marcadores para cada um deles.
			for (int i = 1; i <= participantes; i++){
				latitudes[i] = Double.parseDouble(rotaDetalhe.getEnderecos().get(i + 1).getLatitude());
				longitudes[i] = Double.parseDouble(rotaDetalhe.getEnderecos().get(i + 1).getLongitude());
				String titulo = rotaDetalhe.getUsuarios().get(i - 1).getLogin();
				String rua = rotaDetalhe.getEnderecos().get(i + 1).getRua() + ", " + rotaDetalhe.getEnderecos().get(i + 1).getNumero() + " - " + rotaDetalhe.getEnderecos().get(i + 1).getBairro();
				setMarker(latitudes[i], longitudes[i], titulo, rua, false);
			}
		}
	}

	
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
				Utils.logException("DetailsUserListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro pegar detalhes rota!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
//			setMarcadores();
			progress.dismiss();
		}		
	}
	
	
	public Marker setMarker(double latitude, double longitude, String title, String snippet, boolean zoom) {
		//		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
		Marker mark = googleMap.addMarker(new MarkerOptions()
		.position(new LatLng(latitude, longitude))
		.title(title)
		.snippet(snippet));
		mark.showInfoWindow();

		if(zoom)
			setaZoom(latitude, longitude);

		return mark;
	}
	
	private void setaZoom(double latitude, double longitude){
		//Location myLocation = googleMap.getMyLocation();
		LatLng myLatLng = new LatLng(latitude, longitude);
		googleMap.getProjection();
		//Adiciona a latitude e longitude da minha localização a um objeto LatLng

		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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