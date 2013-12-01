package com.br.fragments;

import java.util.HashMap;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParticipateRoteFragment extends Fragment{
	private GoogleMap googleMap;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnParticipa;
	private TextView lblOrigem, lblOrigem2, lblDestino, lblDestino2, lblPassageiros, lblAdm, lblHora;
	private Context context;
	private RotaApp rota, rotaDetalhe;
	private SessionManagement session;
	private Address destino;
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
		

		return rootView;	
	}

	public void setAtributes(View rootView){
		session = new SessionManagement(rootView.getContext());

		mapView = (MapView) rootView.findViewById(R.id.rote_detail_map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.rote_detail_map)).getMap();
			if (googleMap != null) {
				//				setUpMap();
			}
		}
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		Bundle args = getArguments();
		rota = args.getParcelable("rota");
		destino = args.getParcelable("destinoAddress");

		try{
			DetalhesRotaTask task = new DetalhesRotaTask();
			task.execute();
		}
		catch (Exception e) {
			Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
		}

		lblOrigem = (TextView) rootView.findViewById(R.id.rote_details_lbl_origem_info);
		lblOrigem2 = (TextView) rootView.findViewById(R.id.rote_details_lbl_origem_info2);
		lblDestino = (TextView) rootView.findViewById(R.id.rote_details_lbl_destino_info);
		lblDestino2 = (TextView) rootView.findViewById(R.id.rote_details_lbl_destino_info2);
		lblPassageiros = (TextView) rootView.findViewById(R.id.rote_details_lbl_passageiros_info);
		lblAdm = (TextView) rootView.findViewById(R.id.rote_details_lbl_adm_nome);
		lblHora = (TextView) rootView.findViewById(R.id.rote_details_lbl_hora_info);
		btnParticipa = (Button) rootView.findViewById(R.id.rote_details_btn_participar);

		lblOrigem.setText(rota.getEnderecos().get(0).getRua() + ", " + rota.getEnderecos().get(0).getNumero());
		lblOrigem2.setText(rota.getEnderecos().get(0).getBairro() + " - " + rota.getEnderecos().get(0).getCidade());
		lblDestino.setText(rota.getEnderecos().get(1).getRua() + ", " + rota.getEnderecos().get(1).getNumero());
		lblDestino2.setText(rota.getEnderecos().get(1).getBairro() + " - " + rota.getEnderecos().get(1).getCidade());
		int passageiro =  4 - rota.getPassExistentes();
		lblPassageiros.setText(passageiro + "");
		lblHora.setText(rota.getDataRota().toString());
	}

	//Seta os marcadores no mapa
	public void setMarcadores(RotaApp r){
		int participantes = 0;
		participantes += r.getUsuarios().size();
		double[] latitudes = new double[participantes + 1];
		double[] longitudes = new double[participantes + 1];

		//seta o marcador do ADM e da Zoom
		latitudes[0] = Double.parseDouble(r.getEnderecos().get(1).getLatitude());
		longitudes[0] = Double.parseDouble(r.getEnderecos().get(1).getLongitude());
		String adm = r.getAdministrador().getLogin();
		lblAdm.setText(adm);
		String end = r.getEnderecos().get(1).getRua();
		setMarker(latitudes[0], longitudes[0], adm, end, true);

		if (participantes > 0){
			//Interao numero de participantes na rota e seta marcadores para cada um deles.
			for (int i = 1; i <= participantes; i++){
				latitudes[i] = Double.parseDouble(r.getEnderecos().get(i + 1).getLatitude());
				longitudes[i] = Double.parseDouble(r.getEnderecos().get(i + 1).getLongitude());
				String titulo = r.getUsuarios().get(i - 1).getLogin();
				String rua = r.getEnderecos().get(i + 1).getRua() + ", " + r.getEnderecos().get(i + 1).getNumero() + " - " + r.getEnderecos().get(i + 1).getBairro();
				setMarker(latitudes[i], longitudes[i], titulo, rua, false).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destino_verde));
			}
		}
	}
	
	public Marker setMarker(double latitude, double longitude, String title, String snippet, boolean zoom) {
		//		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
		Marker mark = googleMap.addMarker(new MarkerOptions()
		.position(new LatLng(latitude, longitude))
		.title(title)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destino_azul))
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
		CameraPosition cp = new CameraPosition.Builder()
		   .target(new LatLng(myLatLng.latitude, myLatLng.longitude))// centro do mapa para uma lat e long
		   .zoom(11)          // muda a orientação da camera para leste
		   .tilt(34)             // ângulo de visão da câmera para 45 graus
		   .build();             // cria um CameraPosition a partir do builder   

		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
	}

	public void setBtnAction(){
		btnParticipa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//Utils.gerarToast(context, "teste");
				try{
					ParticipaRotaTask task = new ParticipaRotaTask();
					task.execute();
				}
				catch (Exception e) {
					Utils.logException("ParticipateRoteFragment", "onCreateView", "", e);
				}
			}});
	}

	private class ParticipaRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;


		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			HashMap<String, String> user = session.getUserDetails();
			EnderecoApp endereco = criaEnderecoDestino(destino);
			id = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
			rotaId = rota.getId();
			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.participarRota(rotaId, id, endereco);

			} catch (Exception e) {
				Utils.logException("ParticipateRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao participar rota!}";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			Utils.gerarToast(context, "Entrou na rota");
			Bundle args = new Bundle();
			Utils.changeFragment(getFragmentManager(), new UserListRoteFragment(), args);
			progress.dismiss();
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
				Utils.logException("ParticipateRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro pegar detalhes rota!}";
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			setMarcadores(rotaDetalhe);
			progress.dismiss();
		}		
	}

	public EnderecoApp criaEnderecoDestino(Address address){
		EnderecoApp endereco = new EnderecoApp();

		endereco.setRua(address.getThoroughfare());
		endereco.setNumero(Integer.parseInt(address.getSubThoroughfare()));
		endereco.setBairro(address.getSubLocality());
		endereco.setCidade(address.getLocality());
		endereco.setEstado(address.getAdminArea());
		endereco.setPais(address.getCountryName());
		endereco.setLatitude(Double.toString(address.getLatitude()));
		endereco.setLongitude(Double.toString(address.getLongitude()));
		endereco.setUf("SP");
		endereco.setCep(address.getPostalCode());
		endereco.setTipo('D');

		return endereco;
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
