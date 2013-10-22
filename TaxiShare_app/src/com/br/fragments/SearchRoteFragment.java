package com.br.fragments;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.LoginApp;
import com.br.entidades.PerimetroApp;
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


public class SearchRoteFragment extends Fragment {

	// Google Map
	private GoogleMap googleMap;
	AQuery aQuery;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnBusca, btnLista, btnCriar, btnMinhasRotas;
	private EditText txtEndereco1;
	private EditText txtEndereco2;
	public Context context;
	EnderecoApp enderecoOrigem;
	EnderecoApp enderecoDestino;
	Address ori, dest;
	List<Address> destinoLista;
	List<Address> origemLista;
	MapUtils mapUtils;

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
		centerMapOnMyLocation();
		setBtnAction();
		return rootView;	
	}

	public List<Address>  getListaDeEnderecos(String endereco) throws IOException {
		// esse Geocoder aqui é quem vai traduzir o endereço de String para coordenadas double
		Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());

		//este Adress aqui recebe um retorno do metodo geoCoder.getFromLocationName vc manipula este retorno pra pega as coordenadas
		List<Address> enderecos = null;  

		// o numero um aqui é a quantidade maxima de resultados que vc quer receber
		enderecos = geoCoder.getFromLocationName(endereco, 5);

		return enderecos;
	}

	public CharSequence[] getListaConvertida(List<Address> enderecos){
		String[] strEnderecos = new String [enderecos.size()];

		for(int i = 0; i<enderecos.size(); i++){
			Address address = enderecos.get(i);

			String endereco = address.getThoroughfare();

			String numero = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "Sem numero" ;
			String bairro = address.getSubLocality() != null ? address.getSubLocality() : "Sem bairro" ;
			String cidade = address.getLocality() != null ? address.getLocality() : "Sem cidade" ;
			String estado = address.getAdminArea() != null ? address.getAdminArea() : "Sem estado";

			strEnderecos[i] = endereco + ", " + numero + ", " + bairro + " - " + cidade + " / " + estado ;		
		}	

		return strEnderecos;

	}

	public void setAtributes(View rootView){

		mapView = (MapView) rootView.findViewById(R.id.rote_search_map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.rote_search_map)).getMap();
			if (googleMap != null) {
				//				setUpMap();
			}
		}

		googleMap.setTrafficEnabled(true);
		btnBusca = (Button) rootView.findViewById(R.id.teste_mapa_btn_buscar);


		txtEndereco1 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_origem);
		txtEndereco2 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_destino);
		aQuery = new AQuery(rootView.getContext());	

		mapUtils = new MapUtils(context, googleMap);

	}

	public void setBtnAction(){
		//acao do botao buscar
		btnBusca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				//pega o texto dos campos
				String origem = txtEndereco1.getText().toString();
				String destino = txtEndereco2.getText().toString();

				try {
					//recebe uma lista de endereços com objetos ADDRESS
					origemLista = getListaDeEnderecos(origem);
					destinoLista = getListaDeEnderecos(destino);

					//Converte para uma lista de strings formatadas
					final CharSequence[] enderecosOrigem = getListaConvertida(origemLista);
					final CharSequence[] enderecosDestino = getListaConvertida(destinoLista);

					//Checa se houve retorno para os dois endereços
					if(enderecosOrigem.length > 0 && enderecosDestino.length >0){
						//Cria os 2 popUps
						AlertDialog.Builder popupOrigem = new AlertDialog.Builder(context);
						final AlertDialog.Builder popupDestino = new AlertDialog.Builder(context);

						//Seta os titulos
						popupOrigem.setTitle("Selecione Origem");
						popupDestino.setTitle("Selecione Destino");

						//Define os itens da lista e coloca ação no click da origem
						popupOrigem.setItems(enderecosOrigem, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Define o objeto origem de acordo com a escolha na lista
								ori = origemLista.get(which);
								//Passa a bola para janela de escolha do destino
								popupDestino.show();
							}	
						});					

						//Define os itens da lista e coloca ação no click do destino
						popupDestino.setItems(enderecosDestino, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								//Define o objeto destino de acordo com a escolha na lista
								dest = destinoLista.get(which);

								//Agora definimos as longitudes e latitudes da origem e destino
								String textoOrigem = ori.getThoroughfare() + ", " + ori.getSubThoroughfare() + " - " + ori.getSubLocality();
								String textoDestino = dest.getThoroughfare() + ", " + dest.getSubThoroughfare() + " - " + dest.getSubLocality();
								txtEndereco1.setText(textoOrigem);
								txtEndereco2.setText(textoDestino);
								double origemLatitude = ori.getLatitude();
								double origemLongitude = ori.getLongitude();

								double destinoLatitude = dest.getLatitude();
								double destinoLongitude = dest.getLongitude();		

								//Executa uma async task que ira no ws pegar a lista de rotas
								RouteListTask task = new RouteListTask(origemLatitude, origemLongitude, destinoLatitude, destinoLongitude);
								//FindAll task = new FindAll();
								task.execute();								
							}
						});

						//Mostra a popup de origem primeiro
						popupOrigem.show();
					}
					//Se um endereço não deu retorno
					else{
						//seta o erro aonde a busca não deu retorno
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

				} catch (Exception e) {
					Utils.logException("SerachRoteFragment", "setBtnActions", "", e);
					Utils.gerarToast(context, "Nenhum Endereço Encontrado");
				}
			}
		});

	


		
	}	

	//Só estamos usando esse metodo, ele retorna os 4 pontos para montar o perimetro.
	private double[] getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

		final double[] boundingBox = new double[4];

		final double latRadian = Math.toRadians(pLatitude);

		final double degLatKm = 110.574235;
		final double degLongKm = 110.572833 * Math.cos(latRadian);
		final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
		final double deltaLong = pDistanceInMeters / 1000.0 /
				degLongKm;

		final double minLat = pLatitude - deltaLat;
		final double minLong = pLongitude - deltaLong;
		final double maxLat = pLatitude + deltaLat;
		final double maxLong = pLongitude + deltaLong;

		boundingBox[0] = minLat;
		boundingBox[1] = minLong;
		boundingBox[2] = maxLat;
		boundingBox[3] = maxLong;

		return boundingBox;
	}

	private class RouteListTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;

		double latitudeOrigem, longitudeOrigem;
		double latitudeDestino, longitudeDestino;

		double[]  pontosOrigem, pontosDestino;
		PerimetroApp perimetroOrigem, perimetroDestino;

		//Lista com os 2 objetos de perimetros que iremos montar
		List<PerimetroApp> perimetros;

		//ArrayList que vai receber a lista de rotas do WS
		ArrayList<RotaApp> rotas;

		//Construtor da task, que recebe a lat/long de origem e destino
		public RouteListTask(double latitudeOrigem, double longitudeOrigem, double latitudeDestino, double longitudeDestino){
			this.latitudeOrigem = latitudeOrigem;
			this.longitudeOrigem = longitudeOrigem;
			this.latitudeDestino = latitudeDestino;
			this.longitudeDestino = longitudeDestino;
		}

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Buscando listas", "Aguarde...");
			perimetros = new ArrayList<PerimetroApp>();

			//Recebe os 4 pontos base para criar o perimetro (defini 1000 e 2000 depois temos que ver como vai ficar)
			pontosOrigem = getBoundingBox(latitudeOrigem, longitudeOrigem, 1000);
			pontosDestino = getBoundingBox(latitudeDestino, longitudeDestino, 2000);

			//Instancia o objeto perimetro com os dados certinhos, que foram retornados acima.
			perimetroOrigem = new PerimetroApp(pontosOrigem[2], pontosOrigem[0], pontosOrigem[1], pontosOrigem[3]); 
			perimetroDestino = new PerimetroApp(pontosDestino[2], pontosDestino[0], pontosDestino[1], pontosDestino[3]);

			//Adicionamos na lista de permitros os dois itens que são usados no WS posteriormente
			perimetros.add(perimetroOrigem);
			perimetros.add(perimetroDestino);
		}

		@Override
		protected String doInBackground(String... urls) {

			String response = "";

			WSTaxiShare ws = new WSTaxiShare();
			try {
				//Aqui passamos a lista de perimetros para o WS fazer a busca e retornar a lista de rotas
				rotas = ws.getRotasPerimetro(perimetros);
				response = "{errorCode:0, descricao:Sucesso}";

			} catch (Exception e) {
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
				Utils.logException("SearchRoteFragment", "RouteListTask", "doInBackground", e);
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			try {
				JSONObject jsonResposta = new JSONObject(response);

				//Checamos se a resposta teve sucesso e se retornou uma lista de rotas
				if(jsonResposta.getInt("errorCode")==0 && rotas.size() > 0){
					//Caso tenha retornado, passamos a lista para o proximo fragment
					Bundle args = new Bundle();
					args.putParcelableArrayList("rotas",rotas);								
					Utils.changeFragment(getFragmentManager(), new ListRoteFragment(), args);
				}
				else{
					//Caso contrario, informaos que nenhuma rota foi encontrada.
					//Utils.gerarToast(context, "Nenhuma rota encontrada!");
					questionaCriaRota();


				}
			} catch (Exception e) {
				Utils.logException("SearchRoteFragment", "RouteListTask", "onPostExecute", e);
			}

			//ISSO AQUI É O TESTE QUE MARCA OS PONTOS NO MAPA, VOU DEIXAR PARA TESTARMOS QUALQUER COISA.
			//			LatLng latlng1 = new LatLng(perimetroOrigem.getCima(), perimetroOrigem.getEsquerda());
			//			LatLng latlng2 = new LatLng(perimetroOrigem.getCima(), perimetroOrigem.getDireita());
			//			LatLng latlng3 = new LatLng(perimetroOrigem.getBaixo(), perimetroOrigem.getEsquerda());
			//			LatLng latlng4 = new LatLng(perimetroOrigem.getBaixo(), perimetroOrigem.getDireita());
			//			
			//			LatLng latlng10 = new LatLng(perimetroDestino.getCima(), perimetroDestino.getEsquerda());
			//			LatLng latlng20 = new LatLng(perimetroDestino.getCima(), perimetroDestino.getDireita());
			//			LatLng latlng30 = new LatLng(perimetroDestino.getBaixo(), perimetroDestino.getEsquerda());
			//			LatLng latlng40 = new LatLng(perimetroDestino.getBaixo(), perimetroDestino.getDireita());
			//			
			//			
			//			googleMap.addMarker(new MarkerOptions().position(latlng1).title("Origem 1"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng2).title("Origem 2"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng3).title("Origem 3"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng4).title("Origem 4"));
			//			
			//			googleMap.addMarker(new MarkerOptions().position(latlng10).title("Destino 1"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng20).title("Destino 2"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng30).title("Destino 3"));
			//			googleMap.addMarker(new MarkerOptions().position(latlng40).title("Destino 4"));

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

	public void questionaCriaRota(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Rota não encontrada");

		// set dialog message
		alertDialogBuilder
		.setMessage("Não foram encontradas rotas, deseja criar uma com os endereços da busca?")
		.setCancelable(false)
		.setPositiveButton("Criar",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Bundle args = new Bundle();
				//Leva os 2 Address para a fragment de criação de rotas
				args.putParcelable("origemAddress", ori);
				args.putParcelable("destinoAddress", dest);
				Utils.changeFragment(getFragmentManager(),  new CreateRoteFragment(), args);

			}
		})
		.setNegativeButton("Não",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	private void centerMapOnMyLocation() {
		String contexto = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) context.getSystemService(contexto);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
	    
	   
	    LatLng myLocation = null;
	  
	    
	    if (location != null) {
	        myLocation = new LatLng(location.getLatitude(),
	                location.getLongitude());
	        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
		            16));
	    }
	    
	}

	private class FindAll extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		List<RotaApp> listaRota;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Criando Rota", "Aguarde...");
			
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try
			{
				WSTaxiShare ws = new WSTaxiShare();
				listaRota = ws.getRotas();
				response = "{errorCode:0, descricao:Sucesso}";
			}
			catch(Exception e)
			{
				Utils.gerarToast(context, "Erro ao criar rota!");
				Utils.logException("CreateRoteFragment", "CreateRoteTask", "doInBackground", e);
			}	

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			progress.dismiss();

			try {
				JSONObject json = new JSONObject(response);
				if(json.getInt("errorCode") == 0){

					//Passando a rota selecionada para tela de detalhes.
					Bundle args = new Bundle();
					args.putSerializable("rotas", (Serializable) listaRota);
					args.putParcelable("destinoAddress", dest);
					Utils.changeFragment(getFragmentManager(), new ListRoteFragment(), args);
				}

				Utils.gerarToast(context, json.getString("descricao"));				

			} catch (JSONException e) {
				Utils.logException("SearchRoteFragment", "FindAll", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
			}
		}
	}




}


