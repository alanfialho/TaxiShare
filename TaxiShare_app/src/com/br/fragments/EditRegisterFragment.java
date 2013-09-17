
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
import com.br.validation.Rule;
import com.br.validation.Validator;
import com.br.validation.Validator.ValidationListener;
import com.br.validation.annotation.Email;
import com.br.validation.annotation.Regex;
import com.br.validation.annotation.Required;
import com.br.validation.annotation.TextRule;


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
	Validator validator;


	//botoes
	Button btnSalvar;

	//dados pessoa

	@Required(order = 1, message="Campo obrigatorio")
	@Regex(order=2, pattern="[a-z A-Z]+", message="Deve conter apenas letras")
	EditText textNome;

	@Required(order = 3, message="Campo obrigatorio")
	@Email(order =4, message="E-mail Inválido")
	EditText textEmail;


	@Required(order = 5, message="Campo obrigatorio")
	@TextRule(order=6, minLength=8, message="Deve conter no minimo 8 digitos")
	EditText textCelular;


	@Required(order = 7, message="Campo obrigatorio")
	@TextRule(order=8, minLength=2, message="Deve conter 2 digitos")
	EditText textDDD;

	Spinner spinnerSexo;
	DatePicker dateDataNascimento;
	SessionManagement session;


	private static String pessoaID;
	private static String sessionedLogin;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_register, container, false);
		setAtributes(rootView);
		fillFields(rootView);
		setBtnAction();

		//Criando listner
		ValidationListner validationListner = new ValidationListner();
		validationListner.validationContext = getActivity();

		//Instanciando Validation
		validator = new Validator(this);
		validator.setValidationListener(validationListner);

		//Checa se o usuario esta logado
		session.checkLogin();

		return rootView;
	}

	private void setBtnAction() {
		//Acao do botao cadastrar
		btnSalvar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				context = view.getContext();

				validator.validate();



			}
		});		
	}

	private void fillFields(View rootView) {
		//Recupera os dados do usuario na sessão
		HashMap<String, String> user = session.getUserDetails();         

		//Pega as variaveis do usuario
		String nome = user.get(SessionManagement.KEY_NAME);
		String email = user.get(SessionManagement.KEY_EMAIL);
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

	}

	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		textNome = (EditText) rootView.findViewById(R.id.editNome);
		textEmail = (EditText) rootView.findViewById(R.id.editEmail);
		textDDD = (EditText) rootView.findViewById(R.id.editDDD);
		spinnerSexo = (Spinner) rootView.findViewById(R.id.editSexo);
		textCelular = (EditText) rootView.findViewById(R.id.editCelular);
		dateDataNascimento = (DatePicker) rootView.findViewById(R.id.editDateDataNascimemto);

		//Importando botões
		btnSalvar = (Button) rootView.findViewById(R.id.btnEditSalvar);

	}

	private class ValidationListner implements ValidationListener {
		Context validationContext;

		public void onValidationSucceeded() {

			EditRegisterTask task = new EditRegisterTask();
			task.fillContext = validationContext;
			task.execute();
		}

		public void onValidationFailed(View failedView, Rule<?> failedRule) {

			String message = failedRule.getFailureMessage();

			if (failedView instanceof EditText) {
				failedView.requestFocus();
				((EditText) failedView).setError(message);
			} else {
				Utils.gerarToast(failedView.getContext(), message);
			}
		}
	}

	private class EditRegisterTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;
		Context fillContext;
		PessoaApp pessoaApp;

		protected void onPreExecute() {
			try{
				//Inica a popup de load				
				progress = Utils.setProgreesDialog(progress, fillContext, "Salvando Alterações", "Aguarde...");

				//Pegando as informações de pessoas
				String nome = textNome.getText().toString();
				String email = textEmail.getText().toString();
				String ddd = textDDD.getText().toString();
				String celular = textCelular.getText().toString();

				//Montando data de nascimento
				int year = dateDataNascimento.getYear();
				int month = dateDataNascimento.getMonth() + 1;
				int day = dateDataNascimento.getDayOfMonth();
				String niceMonth = month > 9 ? "" + month : "0"+ month;
				String dataNascimento = year + "-" +niceMonth+ "-" +day;

				Log.i("DATAAAAAAAAAAAAAAAAAA taxi", dataNascimento);

				String sexo = spinnerSexo.getSelectedItem().toString();				

				//Criando objeto pessoa e objeto login
				pessoaApp = new PessoaApp();

				//Definindo as paradas em pessoa				
				pessoaApp.setId(Integer.parseInt(pessoaID));
				pessoaApp.setNome(nome);
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

					session.createLoginSession(pessoaID, pessoaApp.getNome(),  pessoaApp.getEmail(), pessoaApp.getSexo(),  pessoaApp.getDataNascimento(), pessoaApp.getDdd(),  pessoaApp.getCelular(), sessionedLogin);
					Fragment fragment = new SearchRoteFragmentAntigo();
					Bundle args = new Bundle();
					fragment.setArguments(args);

					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
				}

				Utils.gerarToast( context, respostaWsJSON.getString("descricao"));


			} catch (JSONException e) {
				Log.i(" EditRegisterTask onPostExecute Exception taxi", "Exception -> " + e + "Message -> " + e.getMessage());
				Utils.gerarToast( context, "Tente novamente mais tarde!");
			}

			progress.dismiss();

		}
	}
}