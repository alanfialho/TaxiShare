
package com.br.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.activitys.R;
import com.br.entidades.PessoaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;


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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;


public class EditRegisterFragment extends Fragment {
	Context context;
	
	//botoes
	Button btnSalvar;

	//dados pessoa
	EditText textNome;
	EditText textEmail;
	EditText textCelular;
	EditText textDDD;	
	Spinner spinnerSexo;
	EditText textNick;
	DatePicker dateDataNascimento;
	SessionManagement session;


	private static String pessoaID;
	private static String sessionedLogin;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_register, container, false);


		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		textNome = (EditText) rootView.findViewById(R.id.editNome);
		textNick = (EditText) rootView.findViewById(R.id.editNick);
		textEmail = (EditText) rootView.findViewById(R.id.editEmail);
		textDDD = (EditText) rootView.findViewById(R.id.editDDD);
		spinnerSexo = (Spinner) rootView.findViewById(R.id.editSexo);
		textCelular = (EditText) rootView.findViewById(R.id.editCelular);
		dateDataNascimento = (DatePicker) rootView.findViewById(R.id.editDateDataNascimemto);

		//Importando botões
		btnSalvar = (Button) rootView.findViewById(R.id.btnEditSalvar);

		//Checa se o usuario esta logado
		session.checkLogin();

		//Recupera os dados do usuario na sessão
		HashMap<String, String> user = session.getUserDetails();         

		//Pega as variaveis do usuario
		String nome = user.get(SessionManagement.KEY_NAME);
		String email = user.get(SessionManagement.KEY_EMAIL);
		String nick = user.get(SessionManagement.KEY_NICK);
		String sexo = user.get(SessionManagement.KEY_SEXO);
		String ddd = user.get(SessionManagement.KEY_DDD);
		String celular = user.get(SessionManagement.KEY_CELULAR);
		String datanasc = user.get(SessionManagement.KEY_DATANASC);

		//Setando id
		pessoaID = user.get(SessionManagement.KEY_PESSOAID);
		sessionedLogin = user.get(SessionManagement.KEY_LOGIN);

		//Try para preencher o combo de sexo
		try {

			//Definindo Lista de sexos
			List<String> sexos = new ArrayList<String>();
			sexos.add("Masculino");
			sexos.add("Feminino");			

			//Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, sexos);
			adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSexo.setAdapter(adapterSexo);

			//Retorna a posição do sexo no array e seta no spinner
			int spinnerPosition = adapterSexo.getPosition(sexo);
			spinnerSexo.setSelection(spinnerPosition);

			Log.i("Spinners preenchidos taxi", "");	


		}catch (Exception e1) {
			Log.i("Preenchendo Sppiners Exception taxi", e1 + "");	
		}

		// displaying user data
		textNome.setText(nome);
		textDDD.setText(ddd);
		textCelular.setText(celular);
		textEmail.setText(email);
		textNick.setText(nick);		

		try{
			//convertendo a data para int
			String year =datanasc.substring(6, 10);
			String month = datanasc.substring(3, 5);
			String day = datanasc.substring(0, 2);

			//convertendo a data para int
			int yearInt = Integer.parseInt(year);
			int monthInt = Integer.parseInt(month);
			int dayInt = Integer.parseInt(day);

			dateDataNascimento.updateDate(yearInt, monthInt - 1, dayInt);
		}
		catch(Exception e)
		{
			Log.i("Exception ao montar a data taxi", e + " -->" + e.getMessage());

		}


		new WSTaxiShare();		

		//Acao do botao cadastrar
		btnSalvar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				context = view.getContext();

				EditRegisterTask task = new EditRegisterTask();
				task.fillContext = view.getContext();
				task.execute();
			}
		});

		
		return rootView;
	}

	private class EditRegisterTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		Context fillContext;
		PessoaApp pessoaApp;

		protected void onPreExecute() {
			try{
				//Inica a popup de load
				progress = new ProgressDialog(fillContext);
				progress.setTitle("Salvando Alterações");
				progress.setMessage("Aguarde...");
				progress.show();
				
				//Pegando as informações de pessoas
				String nome = textNome.getText().toString();
				String nick = textNick.getText().toString();
				String email = textEmail.getText().toString();
				String ddd = textDDD.getText().toString();
				String celular = textCelular.getText().toString();

				//Montando data de nascimento
				int year = dateDataNascimento.getYear();
				int month = dateDataNascimento.getMonth();
				int day = dateDataNascimento.getDayOfMonth();
				String dataNascimento = day + "/" + month+ "/" +year;

				String sexo = spinnerSexo.getSelectedItem().toString();				

				//Criando objeto pessoa e objeto login
				pessoaApp = new PessoaApp();

				//Definindo as paradas em pessoa				
				pessoaApp.setId(Long.parseLong(pessoaID));
				pessoaApp.setNome(nome);
				pessoaApp.setNick(nick);
				pessoaApp.setCelular(celular);
				pessoaApp.setDataNascimento(dataNascimento);
				pessoaApp.setDdd(ddd);
				pessoaApp.setEmail(email);
				pessoaApp.setSexo(sexo);			
			}catch(Exception e){
				Log.i("Exception onPreExecute EditRegisterTask ", "Exection -> " +  e + " || Message -> " + e.getMessage());
			}
			
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try 
			{
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.editarCadastro(pessoaApp);

			} 
			catch (Exception e) {
				Utils.gerarToast( context, "Erro ao alterar!");
				Log.i("Exception alterar taxi", e + "");
			}


			return response;

		}

		@Override
		protected void onPostExecute(String strJson) {
			

			//Cria um obj JSON com a resposta
			try {
				JSONObject respostaWsJSON = new JSONObject(strJson);
				if(respostaWsJSON.getInt("errorCode") == 0){

					session.createLoginSession(pessoaID, pessoaApp.getNome(),  pessoaApp.getEmail(), pessoaApp.getSexo(),  pessoaApp.getDataNascimento(),  pessoaApp.getNick(), pessoaApp.getDdd(),  pessoaApp.getCelular(), sessionedLogin);
					Fragment fragment = new DashboardFragment();
					Bundle args = new Bundle();
					fragment.setArguments(args);

					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
				}

				Utils.gerarToast( context, respostaWsJSON.getString("descricao"));


			} catch (JSONException e) {
				Log.i("onPostExecute Exception taxi", "Exception -> " + e + "Message -> " + e.getMessage());
			}
			
			progress.dismiss();

		}
	}
}