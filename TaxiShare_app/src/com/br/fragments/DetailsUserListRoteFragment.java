package com.br.fragments;

import com.br.activitys.R;
import com.br.entidades.RotaApp;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import android.app.Fragment;
import android.content.Context;
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
		MapUtils mapUtils = new MapUtils(context, googleMap);
		
		double destinoLatitude = Double.parseDouble(rota.getEnderecos().get(1).getLatitude());
		double destinoLongitude = Double.parseDouble(rota.getEnderecos().get(1).getLongitude());

		double origemLatitude = Double.parseDouble(rota.getEnderecos().get(0).getLatitude());
		double origemLongitude = Double.parseDouble(rota.getEnderecos().get(0).getLongitude());

			
		mapUtils.execute(destinoLatitude, destinoLongitude, origemLatitude, origemLongitude);
		
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

	

