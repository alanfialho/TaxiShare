package com.br.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.RotaApp;



import com.br.network.WSTaxiShare;
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

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
	private Button btnSairRota, btnSms, btnLigar;
	private TextView lblOrigem, lblDestino, lblPassageiros, lblAdm, lblHora;
	private Context context;
	private RotaApp rota, rotaDetalhe;
	private List<String> logins, telefones;
	private int selecionado;
	private SessionManagement session;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_users_details, container, false);
		context = getActivity();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("DetailsUserListRoteFragment", "onCreateView", "", e);
		}
		setAtributes(rootView);
		setBtnAction();

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

		session = new SessionManagement(rootView.getContext());

		lblOrigem = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_origem_info);
		lblDestino = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_destino_info);
		lblPassageiros = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_passageiros_info);
		lblAdm = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_adm_nome);
		lblHora = (TextView) rootView.findViewById(R.id.rote_users_details_lbl_hora_info);
		btnSairRota = (Button) rootView.findViewById(R.id.rote_users_details_btn_sair);
		btnSms = (Button) rootView.findViewById(R.id.rote_users_details_btn_sms);
		btnLigar = (Button) rootView.findViewById(R.id.rote_users_details_btn_ligar);

		lblOrigem.setText(rota.getEnderecos().get(0).getRua() + ", " + rota.getEnderecos().get(0).getNumero() + " - " + rota.getEnderecos().get(0).getCidade());
		lblDestino.setText(rota.getEnderecos().get(1).getRua() + ", " + rota.getEnderecos().get(1).getNumero() + " - " + rota.getEnderecos().get(1).getCidade());
		int passageiro =  4 - rota.getPassExistentes();
		lblPassageiros.setText(passageiro + "");
		lblHora.setText(rota.getDataRota().toString());
	}

	public void setBtnAction(){
		btnSms.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				popupSms();
			}});
		btnLigar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				popupLigar();
			}});
		btnSairRota.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				try{
					SairRotaTask task = new SairRotaTask();
					task.execute();
				}
				catch (Exception e) {
					Utils.logException("DetailsUserListRoteFragment", "onCreateView", "", e);
				}
			}});

	}

	public void setMarcadores(){
		int participantes = 0;
		logins = new ArrayList<String>();
		telefones = new ArrayList<String>();
		participantes += rotaDetalhe.getUsuarios().size();
		double[] latitudes = new double[participantes + 1];
		double[] longitudes = new double[participantes + 1];

		//seta o marcador do ADM e da Zoom
		latitudes[0] = Double.parseDouble(rotaDetalhe.getEnderecos().get(1).getLatitude());
		longitudes[0] = Double.parseDouble(rotaDetalhe.getEnderecos().get(1).getLongitude());
		String adm = rotaDetalhe.getAdministrador().getLogin();

		lblAdm.setText(adm);
		logins.add(adm);
		String telefoneAdm = rotaDetalhe.getAdministrador().getPessoa().getCelular();
		telefones.add(telefoneAdm);
		String end = rotaDetalhe.getEnderecos().get(1).getRua() + ", " + rotaDetalhe.getEnderecos().get(1).getNumero() + " - " + rotaDetalhe.getEnderecos().get(1).getBairro();
		setMarker(latitudes[0], longitudes[0], adm, end, true);

		if (participantes > 0){
			//Interao numero de participantes na rota e seta marcadores para cada um deles.
			for (int i = 1; i <= participantes; i++){
				latitudes[i] = Double.parseDouble(rotaDetalhe.getEnderecos().get(i + 1).getLatitude());
				longitudes[i] = Double.parseDouble(rotaDetalhe.getEnderecos().get(i + 1).getLongitude());
				String titulo = rotaDetalhe.getUsuarios().get(i - 1).getLogin();
				logins.add(titulo);
				String telefoneParticipante = rotaDetalhe.getUsuarios().get(i - 1).getPessoa().getCelular();
				telefones.add(telefoneParticipante);
				String rua = rotaDetalhe.getEnderecos().get(i + 1).getRua() + ", " + rotaDetalhe.getEnderecos().get(i + 1).getNumero() + " - " + rotaDetalhe.getEnderecos().get(i + 1).getBairro();
				setMarker(latitudes[i], longitudes[i], titulo, rua, false).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destino_verde));
			}
		}
	}


	private class DetalhesRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		int rotaId;


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
			setMarcadores();
			progress.dismiss();
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

	private void popupSms(){
		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final CharSequence[] items = logins.toArray(new CharSequence[logins.size()]); 

		// Set the dialog title
		builder.setTitle("Mandar SMS para...")
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected
		.setMultiChoiceItems(items, null,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked) {
					// If the user checked the item, add it to the selected items
					mSelectedItems.add(which);
				} else if (mSelectedItems.contains(which)) {
					// Else, if the item is already in the array, remove it 
					mSelectedItems.remove(Integer.valueOf(which));
				}
			}
		})
		// Set the action buttons
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK, so save the mSelectedItems results somewhere
				// or return them to the component that opened the dialog
				enviaSms(mSelectedItems);
			}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

	private void popupLigar(){
		final CharSequence[] items = logins.toArray(new CharSequence[logins.size()]);


		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Ligar para...");
		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selecionado = which;
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				efetuarLigacao(selecionado);
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

	private void enviaSms(ArrayList<Integer> c){
		String separator = "; ";
		if(android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")){
			separator = ", ";
		}
		String tels = "";

		for (int i = 0; i < c.size(); i++){
			for (int j = 0; j < logins.size(); j++){
				if(Integer.parseInt(c.get(i).toString()) == j){
					if(tels.isEmpty()){
						tels += telefones.get(j);
					}
					else{
						tels += separator + telefones.get(j);
					}

				}
			}
		}

		Intent smsIntent = new Intent(Intent.ACTION_VIEW);

		smsIntent.putExtra("sms_body", "");
		smsIntent.putExtra("address", tels);
		smsIntent.setType("vnd.android-dir/mms-sms");

		startActivity(smsIntent);

	}

	private void efetuarLigacao(int l){
		String tel = "";
		for (int i = 0; i < logins.size(); i++){
			if (i == l){
				tel = telefones.get(i);
			}
		}

		String phoneCallUri = "tel:" + tel;
		Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
		phoneCallIntent.setData(Uri.parse(phoneCallUri));
		startActivity(phoneCallIntent);
	}

	private class SairRotaTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		String userName;
		int rotaId, id;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");
			HashMap<String, String> user = session.getUserDetails();
			userName = user.get(SessionManagement.KEY_LOGIN);
			rotaId = rotaDetalhe.getId();
			id = Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID));
		}

		@Override
		protected String doInBackground(String... urls) {

			String response = "";
			EnderecoApp endereco = null;
			for(int i = 0; i < logins.size(); i++){
				if (userName.equals(logins.get(i))){
					endereco = rotaDetalhe.getEnderecos().get(i + 1);
				}
			}
			try {
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.sairRota(rotaId, id, endereco);

			} catch (Exception e) {
				Utils.logException("DetailsUserListRoteFragment", "FillList", "onPostExecute", e);
				response = "{errorCode:1, descricao:Erro ao sair da rota!}";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			Utils.gerarToast(context, response);
			Bundle args = new Bundle();
			Utils.changeFragment(getFragmentManager(), new UserListRoteFragment(), args);
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
}