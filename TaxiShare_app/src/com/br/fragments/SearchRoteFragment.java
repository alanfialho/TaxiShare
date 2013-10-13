package com.br.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;


public class SearchRoteFragment extends Fragment {

	// Google Map
	private GoogleMap googleMap;
	AQuery aQuery;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnBusca, btnLista, btnCriar, btnMinhasRotas;
	private EditText txtEndereco1;
	private EditText txtEndereco2;
	private Context context;
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

		//		Address address = enderecos.get(0);

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

		mapView = (MapView) rootView.findViewById(R.id.rote_details_map);
		mapView.onCreate(mBundle);

		if (googleMap == null) {
			googleMap = ((MapView) rootView.findViewById(R.id.rote_details_map)).getMap();
			if (googleMap != null) {
				//				setUpMap();
			}
		}

		googleMap.setTrafficEnabled(true);
		btnBusca = (Button) rootView.findViewById(R.id.teste_mapa_btn_buscar);
		btnLista = (Button) rootView.findViewById(R.id.teste_mapa_btn_procurar);
		btnMinhasRotas = (Button) rootView.findViewById(R.id.teste_mapa_minhas_rotas);
		btnCriar = (Button) rootView.findViewById(R.id.teste_mapa_btn_criar);

		txtEndereco1 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_origem);
		txtEndereco2 = (EditText) rootView.findViewById(R.id.teste_mapa_txt_destino);
		aQuery = new AQuery(rootView.getContext());	

		mapUtils = new MapUtils(context, googleMap);

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

								mapUtils.execute(destinoLatitude, destinoLongitude, origemLatitude, origemLongitude);
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
				Utils.changeFragment(getFragmentManager(),  new ListRoteFragment(), null);
			}});
		
		btnMinhasRotas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Utils.changeFragment(getFragmentManager(),  new UserListRoteFragment(), null);
			}});

		btnCriar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle args = new Bundle();
				args.putParcelable("origemAddress", ori);
				args.putParcelable("destinoAddress", dest);
				Utils.changeFragment(getFragmentManager(),  new CreateRoteFragment(), args);
			}});
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
