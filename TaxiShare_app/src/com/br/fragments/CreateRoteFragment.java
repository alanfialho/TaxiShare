package com.br.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

	Button btnCriarRota;
	EditText textOrigem, textDestino;
	Spinner spnPessoas;
	TimePicker tpHorarioSaida;
	EnderecoApp enderecoOrigem, enderecoDestino;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_create, container, false);
		context = getActivity();

		setAtributes(rootView);
		setBtnAction();
		fillFields();

		//Checa se o usuario esta logado
		session.checkLogin();

		return rootView;
	}


	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		try {
			textOrigem = (EditText) rootView.findViewById(R.id.rote_create_txt_origem);
			textDestino = (EditText) rootView.findViewById(R.id.rote_create_txt_destino);
			spnPessoas = (Spinner) rootView.findViewById(R.id.rote_create_sp_pessoas);
			tpHorarioSaida = (TimePicker) rootView.findViewById(R.id.rote_create_tp_saida);


			//Importando botões
			btnCriarRota = (Button) rootView.findViewById(R.id.rote_create_btn_criar);

			List<String> numeroPessoas = new ArrayList<String>();
			numeroPessoas.add("0");
			numeroPessoas.add("1");
			numeroPessoas.add("2");

			ArrayAdapter<String> adapterPessoas = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, numeroPessoas);
			adapterPessoas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnPessoas.setAdapter(adapterPessoas);		

		} catch (Exception e) {
			Utils.logException("CreateRoteFragment", "setAtributes", "", e);
		}		
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

	private void fillFields(){
		try{
			Bundle args = getArguments();

			Address ori = (Address) args.getParcelable("origemAddress");
			Address dest  = (Address) args.getParcelable("destinoAddress");

			enderecoOrigem = populaEnderecoApp(ori, 'O');
			enderecoDestino = populaEnderecoApp(dest, 'D');

			String ruaOri = enderecoOrigem.getRua() + ", " + enderecoOrigem.getNumero() + " - " + enderecoOrigem.getCidade();
			String ruaDest = enderecoDestino.getRua() + ", " + enderecoDestino.getNumero() + " - " + enderecoDestino.getCidade();

			textOrigem.setText(ruaOri);
			textDestino.setText(ruaDest);

		}catch(Exception e ){
			Utils.logException("CreateRoteFragment", "fillFields", "", e);
		}
	}

	private class CreateRoteTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		RotaApp rotaApp;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Criando Rota", "Aguarde...");
			HashMap<String, String> user = session.getUserDetails();         

			//instaciando a rota e setando valores
			rotaApp = new RotaApp();
			List<EnderecoApp> lstEndereco = new ArrayList<EnderecoApp>();
			lstEndereco.add(enderecoOrigem);
			lstEndereco.add(enderecoDestino);
			rotaApp.setEnderecos(lstEndereco);
			rotaApp.setFlagAberta(true);
			rotaApp.setPassExistentes(spnPessoas.getSelectedItemPosition());

			//o usuário que está criando a rota é o administrador
			//vinculando o idUsuario(login) que provavelmente esta em session
			LoginApp adm = new LoginApp();
			adm.setId(Integer.parseInt(user.get(SessionManagement.KEY_PESSOAID)));
			rotaApp.setAdministrador(adm);
			//horario de saida
			try
			{
				//Instancia uma nova data
				Date date = new Date(); 
				Calendar calendar = GregorianCalendar.getInstance(); 
				
				//Seta a data no calendario
				calendar.setTime(date);   
				
				//Pegando a hora e minuto da rota e do celular
				int hour = calendar.get(Calendar.HOUR_OF_DAY); 
				int minute = calendar.get(Calendar.MINUTE); 
				int roteHour = tpHorarioSaida.getCurrentHour();
				int roteMinute = tpHorarioSaida.getCurrentMinute();
				
				//Parada para formarmatar a data
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				
				//Seta no calendario o ano, mes e os horarios da rota
				calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), roteHour, roteMinute);

				//Checa se a rota fica para o outro dia
				//Quando o horario da rota for menor do que o horario atual
				if(roteHour < hour || (roteHour == hour && roteMinute < minute)){
					calendar.add(Calendar.DATE, 1);
				}			

				date = calendar.getTime();
				String dataFormatada = dateFormat.format(date);
				rotaApp.setDataRota(dataFormatada);
			}
			catch(Exception e)
			{
				Utils.gerarToast(context, "Erro ao criar rota!");
				Utils.logException("CreateRoteFragment", "CreateRoteTask", "onPreExecute", e);
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
					args.putParcelable("rota", rotaApp);
					Utils.changeFragment(getFragmentManager(), new UserListRoteFragment(), args);
				}

				Utils.gerarToast(context, json.getString("descricao"));				

			} catch (JSONException e) {
				Utils.logException("CreateRoteFragment", "CreateRoteTask", "onPostExecute", e);
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
