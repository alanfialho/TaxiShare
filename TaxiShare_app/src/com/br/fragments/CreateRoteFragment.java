package com.br.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
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
import android.widget.TimePicker;

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
	Address ori, dest;
	EnderecoApp enderecoOrigem, enderecoDestino;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_create, container, false);
		context = getActivity();

		session = new SessionManagement(rootView.getContext());

		//Checa se o usuario esta logado
		session.checkLogin();

		setAtributes(rootView);
		setBtnAction();


		//setUpMapSePreciso()

		return rootView;
	}


	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		textOrigem = (EditText) rootView.findViewById(R.id.rote_create_txt_origem);
		textDestino = (EditText) rootView.findViewById(R.id.rote_create_txt_destino);
		spnPessoas = (Spinner) rootView.findViewById(R.id.rote_create_sp_pessoas);
		tpHorarioSaida = (TimePicker) rootView.findViewById(R.id.rote_create_tp_saida);

		Bundle args = getArguments();
		ori = (Address) args.getParcelable("origemAddress");
		dest  = (Address) args.getParcelable("destinoAddress");
		enderecoOrigem = populaEnderecoApp(ori, 'O');
		enderecoDestino = populaEnderecoApp(dest, 'D');
		String ruaOri = enderecoOrigem.getRua() + ", " + enderecoOrigem.getNumero() + " - " + enderecoOrigem.getCidade();
		String ruaDest = enderecoDestino.getRua() + ", " + enderecoDestino.getNumero() + " - " + enderecoDestino.getCidade();

		textOrigem.setText(ruaOri);
		textDestino.setText(ruaDest);


		try {

			// Definindo Lista de sexos
			List<String> numeroPessoas = new ArrayList<String>();
			numeroPessoas.add("0");
			numeroPessoas.add("1");
			numeroPessoas.add("2");


			// Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numeroPessoas);
			adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnPessoas.setAdapter(adapterSexo);		


		} catch (Exception e) {
			Log.i("Preenchendo Sppiners Exception taxi", "Exceptiom -> " + e + " || Message -> " + e.getMessage());
		}





		//Importando botões
		btnCriarRota = (Button) rootView.findViewById(R.id.rote_create_btn_criar);

	}

	private void setBtnAction() {
		//Acao do botao criar rota
		btnCriarRota.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
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
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), tpHorarioSaida.getCurrentHour(), tpHorarioSaida.getCurrentMinute());
				Date data = cal.getTime();
				String dataFormatada = dateFormat.format(data);
				rotaApp.setDataRota(dataFormatada);
			}
			catch(Exception ex)
			{
				Utils.gerarToast( context, "Erro ao criar rota!");
				Log.i("Exception criar rota taxi", ex.getMessage() + "");

			}
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
		protected void onPostExecute(String response) {
			progress.dismiss();

			try {
				JSONObject respostaWsJSON = new JSONObject(response);
				Utils.gerarToast( context, respostaWsJSON.getString("descricao"));				

			} catch (JSONException e) {
				Log.i("CreateRoteTask onPostExecute Exception taxi", "Exception -> " + e + "Message -> " + e.getMessage());
				Utils.gerarToast( context, "Tente novamente mais tarde!");
			}
		}
	}

	public EnderecoApp populaEnderecoApp(Address address, Character tipo){
		EnderecoApp endereco = new EnderecoApp();
		switch (tipo) {
		case 'O':
			enderecoOrigem = new EnderecoApp();
			enderecoOrigem.setRua(address.getThoroughfare());
			enderecoOrigem.setNumero(Integer.parseInt(address.getSubThoroughfare()));
			enderecoOrigem.setBairro(address.getSubLocality());
			enderecoOrigem.setCidade(address.getLocality());
			enderecoOrigem.setEstado(address.getAdminArea());
			enderecoOrigem.setPais(address.getCountryName());
			enderecoOrigem.setLatitude(Double.toString(address.getLatitude()));
			enderecoOrigem.setLongitude(Double.toString(address.getLongitude()));
			enderecoOrigem.setUf("SP");
			enderecoOrigem.setCep(address.getPostalCode());
			enderecoOrigem.setTipo(tipo);
			endereco = enderecoOrigem;
		case 'D':
			enderecoDestino = new EnderecoApp();
			enderecoDestino.setRua(address.getThoroughfare());
			enderecoDestino.setNumero(Integer.parseInt(address.getSubThoroughfare()));
			enderecoDestino.setBairro(address.getSubLocality());
			enderecoDestino.setCidade(address.getLocality());
			enderecoDestino.setEstado(address.getAdminArea());
			enderecoDestino.setPais(address.getCountryName());
			enderecoDestino.setLatitude(Double.toString(address.getLatitude()));
			enderecoDestino.setLongitude(Double.toString(address.getLongitude()));
			enderecoDestino.setUf("SP");
			enderecoDestino.setCep(address.getPostalCode());
			enderecoDestino.setTipo(tipo);
			endereco = enderecoDestino;
		default:
			break;
		}
		return endereco;


	}

}
