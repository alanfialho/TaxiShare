package com.br.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.LoginApp;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TimePicker;


//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;


public class CreateRoteFragment extends Fragment {

	Context context;
	SessionManagement session;
	//	private GoogleMap mapa;
	//	private Marker marcador;
	private double lat, lon, minhaLat, minhaLong;

	Button btnCriarRota;
	EditText textOrigem;
	EditText textDestino;
	Spinner spnPessoas;
	TimePicker tpHorarioSaida;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_create, container, false);

		session = new SessionManagement(rootView.getContext());

		//Checa se o usuario esta logado
		session.checkLogin();

		setAtributes(rootView);
		setBtnAction();


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

	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		textOrigem = (EditText) rootView.findViewById(R.id.txtCreateOrigem);
		textDestino = (EditText) rootView.findViewById(R.id.txtCreateDestino);
		spnPessoas = (Spinner) rootView.findViewById(R.id.spinnerCreatePessoas);
		tpHorarioSaida = (TimePicker) rootView.findViewById(R.id.timePickerCreateHorarioSaida);

		//Importando botões
		btnCriarRota = (Button) rootView.findViewById(R.id.btnCreateCriar);

	}

	private void setBtnAction() {
		//Acao do botao criar rota
		btnCriarRota.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				context = view.getContext();
				CreateRoteTask task = new CreateRoteTask();
				task.execute();
			}
		});		
	}


	private class CreateRoteTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		RotaApp rotaApp;
		EnderecoApp enderecoOrigem;
		EnderecoApp enderecoDestino;
		LoginApp usuarioAdm;
		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Criando Rota", "Aguarde...");
			Log.i("onPreExecute Create Rota taxi", "onPreExecute Create Rota taxi");

			//quebrando o endereço padrão Google só foi feito isso para o teste
			String endOrigem[] = textOrigem.getText().toString().split(",");
			String endDestino[] = textDestino.getText().toString().split(",");

			//instaciando os endereços
			enderecoOrigem = new EnderecoApp();
			enderecoOrigem.setRua(endOrigem[0]);
			enderecoOrigem.setNumero(Integer.parseInt(endOrigem[1].trim()));
			enderecoOrigem.setBairro(endOrigem[2]);
			enderecoOrigem.setCidade(endOrigem[3]);
			enderecoOrigem.setEstado(endOrigem[4]);
			enderecoOrigem.setPais("Brasil");
			enderecoOrigem.setLatitude("13762782");
			enderecoOrigem.setLongitude("4938520");
			enderecoOrigem.setUf("SP");
			enderecoOrigem.setCep("08021170");
			enderecoOrigem.setTipo('O');

			enderecoDestino = new EnderecoApp();
			enderecoDestino.setRua(endOrigem[0]);
			enderecoDestino.setNumero(Integer.parseInt(endOrigem[1].trim()));
			enderecoDestino.setBairro(endOrigem[2]);
			enderecoDestino.setCidade(endOrigem[3]);
			enderecoDestino.setEstado(endOrigem[4]);
			enderecoDestino.setPais("Brasil");
			enderecoDestino.setLatitude("13762782");
			enderecoDestino.setLongitude("4938520");
			enderecoDestino.setUf("SP");
			enderecoDestino.setCep("08021170");
			enderecoDestino.setTipo('D');


			//instaciando a rota e setando valores
			rotaApp = new RotaApp();
			List<EnderecoApp> lstEndereco = new ArrayList();
			lstEndereco.add(enderecoOrigem);
			lstEndereco.add(enderecoDestino);
			rotaApp.setEnderecos(lstEndereco);
			rotaApp.setFlagAberta(true);
			rotaApp.setPassExistentes(Short.parseShort("2"));

			//o usuário que está criando a rota é o administrador
			//vinculando o idUsuario(login) que provavelmente esta em session
			LoginApp adm = new LoginApp();
			adm.setId(1);
			rotaApp.setAdministrador(adm);
			//horario de saida
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
			Date data  = new Date();
			data.setHours(tpHorarioSaida.getCurrentHour());
			data.setMinutes(tpHorarioSaida.getCurrentMinute());
			String dataFormatada = format.format(data);
			rotaApp.setDataRota(dataFormatada);
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try
			{
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.criarRota(rotaApp);

			}
			catch(Exception ex)
			{
				Utils.gerarToast( context, "Erro ao criar rota!");
				Log.i("Exception criar rota taxi", ex + "");
			}	

			return response;

		}

		@Override
		protected void onPostExecute(String strJson) {
			progress.dismiss();

			try {
				JSONObject respostaWsJSON = new JSONObject(strJson);
				Utils.gerarToast( context, respostaWsJSON.getString("descricao"));				

			} catch (JSONException e) {
				Log.i("CreateRoteTask onPostExecute Exception taxi", "Exception -> " + e + "Message -> " + e.getMessage());
				Utils.gerarToast( context, "Tente novamente mais tarde!");
			}
		}
	}}
