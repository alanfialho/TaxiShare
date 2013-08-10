package com.br.entidades;


import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.br.entidades.NovaPessoaApp;; 


public class WSTaxiShare {

	private static final String URL_WS = "http://10.0.2.2:8080/WS_TaxiShare/";

	public NovaPessoaApp getPessoa(int id) throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pessoa/findById/" + id);

		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			NovaPessoaApp pessoa = gson.fromJson(resposta[1], NovaPessoaApp.class);
			return pessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}	
	public String login(String email, String password) throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "login/login/?login="+ email +"&password="+ password);

		String saida = resposta[1];
		if (resposta[0].equals("200")) {
			return saida;
		} else {
			return saida;
		}
	}


	public List<PerguntaApp> getPerguntas() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pergunta/findAll");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<PerguntaApp> listaPessoa = new ArrayList<PerguntaApp>();
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(resposta[1]).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				listaPessoa.add(gson.fromJson(array.get(i), PerguntaApp.class));
			}

			return listaPessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public List<NovaPessoaApp> getListaPessoa() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pessoa/findAll");

		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<NovaPessoaApp> listaPessoa = new ArrayList<NovaPessoaApp>();
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(resposta[1]).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				listaPessoa.add(gson.fromJson(array.get(i), NovaPessoaApp.class));
			}
			return listaPessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public String cadastrarPessoa(NovaPessoaApp pessoa) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String clienteJSON = gson.toJson(pessoa);
			resposta = new WSClient().post(URL_WS + "novapessoa/create", clienteJSON);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	} 

	public String cadastrarLogin(LoginApp login) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(login);
			resposta = new WSClient().post(URL_WS + "login/create", loginJSON);
		}
		catch(Exception ex)
		{
			Log.i("Erronocadastro", "Erro no cadastro login " + ex.toString());
			Log.i("Erronocadastro2", "Erro no cadastro login " + ex.getMessage());
		}
		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public String editarCadastro(NovaPessoaApp novaPessoa) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String pessoaJSON = gson.toJson(novaPessoa);
			resposta = new WSClient().post(URL_WS + "novapessoa/edit", pessoaJSON);
			Log.i("Edittar bruno", resposta[0] + " ----- " +resposta[1]);
		}
		catch(Exception ex)
		{
			
		}
		
		if (resposta[0].equals("200")) {
			Log.i("Edittar bruno if", resposta[0] + " ----- " +resposta[1]);

			return resposta[1];
		} else {
			Log.i("Edittar bruno else", resposta[0] + " ----- " +resposta[1]);

			return resposta[0];
		}
	}

	public String checkLogin(String login) throws Exception {
		String[] resposta = {};
		try
		{
			resposta = new WSClient().get(URL_WS + "login/checkLogin?login="+ login);
		}
		catch(Exception ex)
		{
			Log.i("ErroBruno", "Erro no checar login " + ex.toString() + " -- " + ex);
		}
		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	}

}

