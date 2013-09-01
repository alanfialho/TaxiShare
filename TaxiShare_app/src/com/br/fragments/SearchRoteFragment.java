package com.br.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.br.activitys.R;
import com.br.sessions.SessionManagement;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;


public class SearchRoteFragment extends Fragment {

	Context context;
	SessionManagement session;
//	private GoogleMap mapa;
//	private Marker marcador;
	private double lat, lon, minhaLat, minhaLong;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_search, container, false);

		session = new SessionManagement(rootView.getContext());

		//Checa se o usuario esta logado
		session.checkLogin();
		
		
		//setUpMapSePreciso()


		return rootView;
	}
	
	/**
	private void setUpMapSePreciso() {
		
		//Verifica se o mapa está nulo ou não para saber se é preciso cria-lo
		if(mapa == null) {
			
			//Tenta obter um mapa do SupportMapFragment
			mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			//Verifica se teve sucesso em obter o mapa
			if(mapa != null){
				setUpMap();
			}
			
			
		}
		
	}
	
**/
	
	

}
