package com.br.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.resources.JSONParser;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class SearchRoteFragment extends Fragment{

	// Google Map
	private GoogleMap googleMap;
	AQuery aQuery;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnBusca, btnLista, btnCriar;
	private EditText txtEndereco1, txtEndereco2;
	private Context context;
	EnderecoApp enderecoOrigem;
	EnderecoApp enderecoDestino;
	Address ori, dest;
	List<Address> destinoLista;
	List<Address> origemLista;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.teste_mapa, container, false);
		context = getActivity();


		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("SearchRoteFragment", "onCreateView", "", e);
		}

		setAtributes(rootView);
		setBtnAction();
		return rootView;	
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


	private void setMarker(double latitude, double longitude, String title, boolean zoom) {
		//		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
		googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));

		if(zoom)
			setaZoom(latitude, longitude);
	}

	public void setaZoom(double latitude, double longitude){
		//Location myLocation = googleMap.getMyLocation();
		LatLng myLatLng = new LatLng(latitude, longitude);
		googleMap.getProjection();
		//Adiciona a latitude e longitude da minha localização a um objeto LatLng

		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

	}

	public List<Address>  getListaDeEnderecos(String endereco) throws IOException {
		// esse Geocoder aqui é quem vai traduzir o endereço de String para coordenadas double
		Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
		
		//este Adress aqui recebe um retorno do metodo geoCoder.getFromLocationName vc manipula este retorno pra pega as coordenadas
		List<Address> enderecos = null;  

		// o numero um aqui é a quantidade maxima de resultados que vc quer receber
		enderecos = geoCoder.getFromLocationName(endereco, 5);

		

		//		Address address = enderecos.get(0);

		return enderecos;
	}

	public CharSequence[] getListaConvertida(List<Address> enderecos){
		String[] strEnderecos = new String [enderecos.size()];

		for(int i = 0; i<enderecos.size(); i++){
			Address address = enderecos.get(i);
			strEnderecos[i] = address.getThoroughfare() + ", " + address.getSubThoroughfare() + ", " + address.getSubLocality() + " - " + address.getLocality();			
		}	

		return strEnderecos;

	}



	public void setAtributes(View rootView){


		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.map)).getMap();
			if (googleMap != null) {
				//				setUpMap();
			}
		}

		btnBusca = (Button) rootView.findViewById(R.id.teste_mapa_btn_buscar);
		btnLista = (Button) rootView.findViewById(R.id.teste_mapa_btn_procurar);
		btnCriar = (Button) rootView.findViewById(R.id.teste_mapa_btn_criar);

		txtEndereco1 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_origem);
		txtEndereco2 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_destino);
		aQuery = new AQuery(rootView.getContext());	

	}

	public void setBtnAction(){
		btnBusca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				String origem = txtEndereco1.getText().toString();
				String destino = txtEndereco2.getText().toString();

				try {

					origemLista = getListaDeEnderecos(origem);
					destinoLista = getListaDeEnderecos(destino);


					//Define as listas de enderecos
					final CharSequence[] enderecosOrigem = getListaConvertida(origemLista);
					final CharSequence[] enderecosDestino = getListaConvertida(destinoLista);


					if(enderecosOrigem.length > 0 && enderecosDestino.length >0){
						//Cria os popUps
						AlertDialog.Builder popupOrigem = new AlertDialog.Builder(context);
						final AlertDialog.Builder popupDestino = new AlertDialog.Builder(context);

						//Seta os titulos
						popupOrigem.setTitle("Selecione Origem");
						popupDestino.setTitle("Selecione Destino");

						//Define os itens e coloca ação no click da origem
						popupOrigem.setItems(enderecosOrigem, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Seta o endereço bonitinho no text
								//Apresenta a janela de escolha do destino
								popupDestino.show();
							}
						});					

						//Define os itens e coloca ação no click do destino
						popupDestino.setItems(enderecosDestino, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ori = origemLista.get(which);
								dest = destinoLista.get(which);

								double origemLatitude = ori.getLatitude();
								double origemLongitude = ori.getLongitude();
								double destinoLatitude = dest.getLatitude();
								double destinoLongitude = dest.getLongitude();

								connectAsyncTask task = new connectAsyncTask(makeURL(origemLatitude, origemLongitude, destinoLatitude, destinoLongitude));
								task.execute();
							}
						});

						//Mostra a popup de origem
						popupOrigem.show();
					}
					else{
						if(enderecosOrigem.length <= 0){
							txtEndereco1.setError("Seja mais especifico");
							txtEndereco1.setFocusable(true);
						}
							
						if(enderecosOrigem.length <= 0){
							txtEndereco1.setError("Seja mais especifico");
							txtEndereco1.setFocusable(true);
						}

						Utils.gerarToast(context, "Sem resultados");
					}
					
					aQuery.id(R.id.teste_mapa_btn_procurar).visible();	



				} catch (Exception e) {
					Utils.gerarToast(context, "Nenhum Endereço Encontrado");
				}
			}
		});

		btnLista.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle args = new Bundle();

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ftransaction = fragmentManager.beginTransaction();
				Fragment fragment = new ListRoteFragment();
				fragment.setArguments(args);
				ftransaction.replace(R.id.content_frame, fragment);
				ftransaction.addToBackStack(null);
				ftransaction.commit();
			}});

		btnCriar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle args = new Bundle();
				args.putParcelable("origemAddress", ori);
				args.putParcelable("destinoAddress", dest);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ftransaction = fragmentManager.beginTransaction();
				Fragment fragment = new CreateRoteFragment();
				fragment.setArguments(args);
				ftransaction.replace(R.id.content_frame, fragment);
				ftransaction.addToBackStack(null);
				ftransaction.commit();
			}});

	}

	public String makeURL (double origemLatitude, double origemLongitude, double destinoLatitude, double destinoLongitude ){
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(origemLatitude));
		urlString.append(",");
		urlString.append(Double.toString( origemLongitude));
		urlString.append("&destination=");// to
		urlString.append(Double.toString( destinoLatitude));
		urlString.append(",");
		urlString.append(Double.toString( destinoLongitude));
		urlString.append("&sensor=false&mode=driving&alternatives=true");

		googleMap.clear();

		setMarker(origemLatitude, origemLongitude, "Origem", true);
		setMarker(destinoLatitude, destinoLongitude, "Destino", false);
		return urlString.toString();
	}

	public void drawPath(String  result) {

		try {

			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			for(int z = 0; z<list.size()-1;z++){
				LatLng src= list.get(z);
				LatLng dest= list.get(z+1);
				Polyline line = googleMap.addPolyline(new PolylineOptions()
				.add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
				.width(2)
				.color(Color.BLUE).geodesic(true));
			}

		} 
		catch (JSONException e) {
			Utils.logException("SearchRoteFragment", "drawPath", "", e);
		}
	} 

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng( (((double) lat / 1E5)),
					(((double) lng / 1E5) ));
			poly.add(p);
		}

		return poly;
	}


	private class connectAsyncTask extends AsyncTask<Void, Void, String>{
		private ProgressDialog progressDialog;
		String url;
		connectAsyncTask(String urlPass){
			url = urlPass;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = Utils.setProgreesDialog(progressDialog, context, "Traçando rota", "Aguarde...");
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);   
			progressDialog.dismiss();        
			if(result!=null){
				drawPath(result);
			}

		}
	}
}
