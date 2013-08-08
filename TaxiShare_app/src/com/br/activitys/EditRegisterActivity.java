/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.entidades.LoginApp;
import com.br.entidades.NovaPessoaApp;
import com.br.entidades.PerguntaApp;
import com.br.entidades.WSClient;
import com.br.entidades.WSTaxiShare;
import com.br.sessions.SessionManagement;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;


public class EditRegisterActivity extends Activity {
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


	
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_register);
		
		session = new SessionManagement(getApplicationContext());

		//Importando os campos da pessoa
		textNome = (EditText) findViewById(R.id.editNome);
		textNick = (EditText) findViewById(R.id.editNick);
		textEmail = (EditText) findViewById(R.id.editEmail);
		textDDD = (EditText) findViewById(R.id.editDDD);
		spinnerSexo = (Spinner) findViewById(R.id.editSexo);
		textCelular = (EditText) findViewById(R.id.editCelular);
		dateDataNascimento = (DatePicker) findViewById(R.id.editDateDataNascimemto);
		
		try {

			//Definindo Lista de sexos
			List<String> sexos = new ArrayList<String>();
			sexos.add("Masculino");
			sexos.add("Feminino");			

			//Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexos);
			adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSexo.setAdapter(adapterSexo);
			
			Log.i("Preenchendo Sppiners", "Tudo Certo");	


		}catch (Exception e1) {
			Log.i("Preenchendo Sppiners Exception", e1.getMessage());	
		}
		
		
		
		session.checkLogin();
        
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
         
        // name
        String nome = user.get(SessionManagement.KEY_NAME);
         
        // email
        String email = user.get(SessionManagement.KEY_EMAIL);
        
        // name
        String nick = user.get(SessionManagement.KEY_NICK);
         
        // email
        String sexo = user.get(SessionManagement.KEY_SEXO);
        Log.i("Sexo", sexo);
        
        // name
        String ddd = user.get(SessionManagement.KEY_DDD);
         
        // email
        String celular = user.get(SessionManagement.KEY_CELULAR);
        
        // email
        String datanasc = user.get(SessionManagement.KEY_DATANASC);
        Log.i("BRUNO datanasc", datanasc);
         
        // displaying user data
        textNome.setText(nome);
        textDDD.setText(ddd);
        textCelular.setText(celular);
        textEmail.setText(email);
        textNick.setText(nick);	
        
		


		//Importando botões e caixa de erro
		btnSalvar = (Button) findViewById(R.id.btnEditSalvar);
			


		//Instanciando WS
		WSTaxiShare ws = new WSTaxiShare();		

		//Acao do botao cadastrar
		btnSalvar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
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
				NovaPessoaApp pessoaApp = new NovaPessoaApp();
				LoginApp loginApp = new LoginApp();
				PerguntaApp perguntaApp = new PerguntaApp();

				//Definindo as paradas em pessoa
				pessoaApp.setNome(nome);
				pessoaApp.setNick(nick);
				pessoaApp.setCelular(celular);
				pessoaApp.setDataNascimento(dataNascimento);
				pessoaApp.setDdd(ddd);
				pessoaApp.setEmail(email);
				pessoaApp.setSexo(sexo);
				
				
				//Setando a pergunta no login
				loginApp.setPergunta(perguntaApp);

				//Setando pessoa no login
				loginApp.setPessoa(pessoaApp);

				WSTaxiShare ws = new WSTaxiShare();
				try 
				{
					String respostaWs = ws.cadastrarLogin(loginApp);
					Log.i("Dados do cadastro", respostaWs);
					gerarToast("Cadastro efetuado!");
					Log.i("Feito cadastro", "Beleza, deu certo");
					
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(i);
					// Close Registration View
					finish();
					
				} 
				catch (Exception e) {
					gerarToast("Erro ao cadastrar " + e.getMessage());
					Log.i("Erro cadastro", "Beleza, deu errado");
				}
			}
		});

		
	}

	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
}


