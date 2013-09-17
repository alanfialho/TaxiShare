package com.br.fragments;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.activitys.R;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.br.resources.Utils;


public class SearchRoteFragmentAntigo extends Fragment implements OnClickListener{

	// Google Map
	private GoogleMap googleMap;
	private static View view;
	private MapView m;
	private Bundle mBundle;
	private Button btBusca;
	private EditText txtEndereco;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.teste_mapa, container, false);
		
		try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }

        m = (MapView) view.findViewById(R.id.map);
        m.onCreate(mBundle);
        setUpMapIfNeeded(view);
        criaTela();

		
		
		return view;	
		

}
		
	@Override
    public void onResume() {
        super.onResume();
        m.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        m.onPause();
    }

    @Override
    public void onDestroy() {
        m.onDestroy();
        super.onDestroy();
    }

		
	
	private void setUpMapIfNeeded(View inflatedView) {
        if (googleMap == null) {
        	googleMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
    	googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
    	setaZoom();
    }
	
	
	
	public void setaZoom(){
		//Location myLocation = googleMap.getMyLocation();
        LatLng myLatLng = new LatLng(-23.489839,
        		 -46.410520);
		
		//Adiciona a latitude e longitude da minha localização a um objeto LatLng
		
		
		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
		
	}


	
	public void procuraEndereco(String endereco) throws IOException{
		Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());// esse Geocoder aqui é quem vai traduzir o endereço de String para coordenadas double  
		
		List<Address> enderecos = null;//este Adress aqui recebe um retorno do metodo geoCoder.getFromLocationName vc manipula este retorno pra pega as coordenadas  

		enderecos = geoCoder.getFromLocationName(endereco, 1);// o numero um aqui é a quantidade maxima de resultados que vc quer receber
		
		
		double lat = enderecos.get(0).getLatitude();  
		double lon = enderecos.get(0).getLongitude();  
		String end = enderecos.get(0).getAddressLine(0);
		
		Utils.gerarToast(getActivity(), end);


	}
	
	public void criaTela(){

		btBusca = (Button) view.findViewById(R.id.btBusca);
		btBusca.setOnClickListener(this);
		txtEndereco = (EditText) view.findViewById(R.id.txtEndereco);
				
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.btBusca) {
			String e = txtEndereco.getText().toString();
			try {
				procuraEndereco(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
	
	/**
	
	else if (id == R.id.btEnderecos) {
		
		Intent intent = new Intent (TaxyShareMapa.this, MinhaRota.class);
		Bundle extras = new Bundle();
		List<Address> origem = enderecos2;
		List<Address> destino = enderecos;
		extras.putSerializable("ori", (Serializable) origem);
		extras.putSerializable("dest", (Serializable) destino);
		intent.putExtras(extras);
		startActivity(intent);
		
		
	}
	
	**/
}
