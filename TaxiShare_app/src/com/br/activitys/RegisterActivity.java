/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import com.br.entidades.LoginApp;
import com.br.entidades.NovaPessoaApp;
import com.br.entidades.PerguntaApp;
import com.br.entidades.WSTaxiShare;
import com.br.resources.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	// botoes
	Button btnCadastrar;
	Button btnLinkToLogin;

	// dados pessoa
	EditText textNome;
	EditText textEmail;
	EditText textSenha;
	EditText textSenha2;
	EditText textCelular;
	EditText textDDD;
	Spinner spinnerSexo;
	EditText textNick;
	DatePicker dateDataNascimento;

	// Dados login
	EditText textResposta;
	EditText textLogin;
	Spinner spinnerPergunta;

	// Erro
	TextView registerErrorMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		// Importando os campos da pessoa
		textNome = (EditText) findViewById(R.id.textNome);
		textNick = (EditText) findViewById(R.id.textNick);
		textEmail = (EditText) findViewById(R.id.textEmail);
		textSenha = (EditText) findViewById(R.id.textSenha);
		textSenha2 = (EditText) findViewById(R.id.textSenha2);
		textDDD = (EditText) findViewById(R.id.textDDD);
		spinnerSexo = (Spinner) findViewById(R.id.textSexo);
		textCelular = (EditText) findViewById(R.id.textCelular);
		dateDataNascimento = (DatePicker) findViewById(R.id.dateDataNascimemto);

		// Importando os campos do login
		spinnerPergunta = (Spinner) findViewById(R.id.textPergunta);
		textLogin = (EditText) findViewById(R.id.textLogin);
		textResposta = (EditText) findViewById(R.id.textResposta);

		// Importando botões e caixa de erro
		btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

		// Instanciando WS
		WSTaxiShare ws = new WSTaxiShare();

		try {

			// Definindo Lista de sexos
			List<String> sexos = new ArrayList<String>();
			sexos.add("Masculino");
			sexos.add("Feminino");

			// Recebendo lista de perguntas
			List<PerguntaApp> jsonPerguntas = ws.getPerguntas();
			// Criando uma lista para colocar apenas as strings das perguntas
			List<String> perguntas = new ArrayList<String>();

			// Populando lista de Strings de Pertuntas
			for (int i = 0; i < 4; i++) {
				String opcao = jsonPerguntas.get(i).getId() + " - "
						+ jsonPerguntas.get(i).getPergunta();
				perguntas.add(opcao);
			}

			// Colocando lista de perguntas no spinner
			ArrayAdapter<String> adapterPerguntas = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, perguntas);
			adapterPerguntas
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Log.i("MarcaoViado", perguntas.toString());
			spinnerPergunta.setAdapter(adapterPerguntas);

			// Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, sexos);
			adapterSexo
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Log.i("MarcaoViado", sexos.toString());
			spinnerSexo.setAdapter(adapterSexo);

		} catch (Exception e1) {
			Log.i("Preenchendo Sppiners Exception", e1.getMessage());
		}

		// Acao do botao cadastrar
		btnCadastrar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
								
				new Utils();

				// Pegando as informações de pessoas
				String nome = textNome.getText().toString();
				String nick = textNick.getText().toString();
				String email = textEmail.getText().toString();
				String ddd = textDDD.getText().toString();
				String celular = textCelular.getText().toString();

				// Montando data de nascimento
				int year = dateDataNascimento.getYear();
				int month = dateDataNascimento.getMonth();
				int day = dateDataNascimento.getDayOfMonth();
				String dataNascimento = day + "/" + month + "/" + year;
				String sexo = spinnerSexo.getSelectedItem().toString();

				// Pegando as informações do login
				int pergunta = spinnerPergunta.getSelectedItemPosition();
				String login = textLogin.getText().toString();
				String resposta = textResposta.getText().toString();
				String senha = textSenha.getText().toString();
				String senha2 = textSenha2.getText().toString();
				
				boolean checkSenha = false;
				boolean checkEmpty = false;
				boolean checkEmail = false;
				boolean validate = false;
				String message = "";

				//Checa se as senhas são iguais
				if (!senha.equals(senha2) || senha.equals("") || senha2.equals(""))
					checkSenha = false;
				 else 
					checkSenha = true;
				
				//Checa se não há campos vazios
				if (nome.equals("") || nick.equals("") || email.equals("") || ddd.equals("") || celular.equals("") || resposta.equals("") || login.equals("") || senha.equals("")) 
					checkEmpty = false;
				 else 
					checkEmpty = true;
				
				//checa se o email é válido
				if(!Utils.isValidEmail(email))
					checkEmail = false;				
				else
					checkEmail= true;
				
				//Checa se tudo esta ok
				if(checkSenha && checkEmpty && checkEmail){
					validate = true;
				}
					
				else{
					
					Log.i("Erro Validacao", checkSenha + " " + checkEmpty + " " + checkEmail);
					
					if(!checkSenha){
						Log.i("Erro Validacao", "Check-Senha");
						message+= "Senhas precisam ser iguais! \n";
					}
					
					if(!checkEmpty){
						Log.i("Erro Validacao", "Check-Vazio");
						message+= "Preencha os campos vazios! \n";
					}
							
					if(!checkEmail){
						Log.i("Erro Validacaop", "Check-Email");
						message+= "Email inválido!";
					}

					validate = false;

				}
				

				if (validate) {
					// Criando objeto pessoa e objeto login
					NovaPessoaApp pessoaApp = new NovaPessoaApp();
					LoginApp loginApp = new LoginApp();
					PerguntaApp perguntaApp = new PerguntaApp();

					// Definindo as paradas em pessoa
					pessoaApp.setNome(nome);
					pessoaApp.setNick(nick);
					pessoaApp.setCelular(celular);
					pessoaApp.setDataNascimento(dataNascimento);
					pessoaApp.setDdd(ddd);
					pessoaApp.setEmail(email);
					pessoaApp.setSexo(sexo);

					// Definindo as paradas em pergunta
					perguntaApp.setId(new Long(pergunta));

					// Definindo as paradas em login
					loginApp.setLogin(login);
					loginApp.setSenha(senha);
					loginApp.setResposta(resposta);

					// Setando a pergunta no login
					loginApp.setPergunta(perguntaApp);

					// Setando pessoa no login
					loginApp.setPessoa(pessoaApp);

					// Inicia o Web Service
					WSTaxiShare ws = new WSTaxiShare();

					try {
						// String com a resposta do WS
						String checkLogin = ws.checkLogin(login);
						// Transforma a string em um objeto JSON
						JSONObject checkLoginJSON = new JSONObject(checkLogin);

						// Checa se o login esta disponivel
						if (checkLoginJSON.getInt("errorCode") == 0) {

							// String com a resposta do WS
							String cadastroLogin = ws.cadastrarLogin(loginApp);

							// Transforma a string em um objeto JSON
							JSONObject cadastroLoginJSON = new JSONObject(
									cadastroLogin);

							// Checa se o cadastro deu certo
							if (cadastroLoginJSON.getInt("errorCode") == 0) {
								gerarToast("Cadastro efetuado!");

								// Retorna para tela de login
								Intent i = new Intent(getApplicationContext(),
										LoginActivity.class);
								startActivity(i);
								finish();
							} else
								gerarToast(cadastroLoginJSON
										.getString("descricao"));

						} else
							gerarToast(checkLoginJSON.getString("descricao"));

					} catch (Exception e) {
						gerarToast("Erro ao cadastrar " + e.getMessage());
						Log.i("Erro cadastro",
								"Algo deu errado --> " + e.getMessage()
										+ " --- " + e.toString());
					}
				} else {
					gerarToast(message);
				}

			}
		});

		// Link para Login
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
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
