package com.br.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.activitys.R;
import com.br.sessions.SessionManagement;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;


import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class SearchRoteFragment extends Fragment {

	Context context;
	SessionManagement session;
	private GoogleMap mapa;
	private Marker marcador;
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
