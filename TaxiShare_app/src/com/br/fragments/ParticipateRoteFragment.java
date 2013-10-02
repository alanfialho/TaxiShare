package com.br.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.RotaApp;
import com.br.resources.Utils;
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
	private TextView lblOrigem, lblDestino, lblPassageiros;
	private Context context;
	private RotaApp rota;
	
	
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
		setaZoom();
		return rootView;	
	}

	private void setBtnAction() {
		// TODO Auto-generated method stub
		
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


	private void adicionaMarcador(LatLng lat) {
		
		googleMap.addMarker(new MarkerOptions().position(lat).title("Marker"));
		

	}

	public void setaZoom(){
		//Location myLocation = googleMap.getMyLocation();
		
		double lat = Double.parseDouble(rota.getEnderecos().get(1).getLatitude());
		double lon = Double.parseDouble(rota.getEnderecos().get(1).getLongitude());
		LatLng myLatLng = new LatLng(lat, lon);
		googleMap.getProjection();
		//Adiciona a latitude e longitude da minha localização a um objeto LatLng

		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		adicionaMarcador(myLatLng);

	}
	
	public void setAtributes(View rootView){


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
		btnParticipa = (Button) rootView.findViewById(R.id.rote_details_btn_participar);
		lblOrigem = (TextView) rootView.findViewById(R.id.rote_details_txt_origem_info);
		lblDestino = (TextView) rootView.findViewById(R.id.rote_details_txt_destino_info);
		lblPassageiros = (TextView) rootView.findViewById(R.id.rote_details_txt_passageiros_info);
		
		lblOrigem.setText(rota.getEnderecos().get(0).getRua());
		lblDestino.setText(rota.getEnderecos().get(1).getRua());
		lblPassageiros.setText(rota.getPassExistentes() + "");

	}

}
