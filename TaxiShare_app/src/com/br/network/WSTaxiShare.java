package com.br.network;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonParser;

import com.br.entidades.*;
import com.br.resources.Utils;


public class WSTaxiShare {

	private static final String URL_WS = "http://192.168.56.1:8080/WS_TaxiShare/";

	public String login(String email, String password) {


		String resposta = new WSClient().get(URL_WS + "login/login/?login="+ email +"&password="+ URLEncoder.encode(password));
		return resposta;
	}

	public ArrayList<String> getPerguntas() throws Exception {

		String resposta = new WSClient().get(URL_WS + "pergunta/findAll");		
		ArrayList<String> perguntas = new ArrayList<String>(); 

		JSONObject jsonResposta = new JSONObject(resposta);

		if (jsonResposta.getInt("errorCode") == 0) {
			Gson gson = new Gson();

			jsonResposta = jsonResposta.getJSONObject("data");
			JSONArray array = jsonResposta.getJSONArray("perguntas");

			for (int i = 0; i < array.length(); i++) {
				PerguntaApp pergunta = gson.fromJson(array.get(i).toString(), PerguntaApp.class);				
				String opcao = pergunta.getId() + " - " + pergunta.getPergunta();
				perguntas.add(opcao);
			}
		}		
		return perguntas;
	}


	public List<RotaApp> getRotas() throws Exception {

		String resposta = new WSClient().get(URL_WS + "rota/findAll");		
		JSONObject jsonResposta = new JSONObject(resposta);
		ArrayList<RotaApp> rotas = new ArrayList<RotaApp>();
		JsonParser parser = new JsonParser();

		if (jsonResposta.getInt("errorCode") == 0) {
			
			Gson gson = new Gson();
			JsonArray array = parser.parse(jsonResposta.getJSONArray("data").toString()).getAsJsonArray();
			
			for (int i = 0; i < array.size(); i++) {
				rotas.add(gson.fromJson(array.get(i), RotaApp.class));
			}
		}		
		return rotas;

	}
	
	public RotaApp detailRota(int id) throws Exception {

		String resposta = new WSClient().get(URL_WS + "rota/findById/" + id);		
		JSONObject jsonResposta = new JSONObject(resposta);
		RotaApp rota = new RotaApp();
		
		if (jsonResposta.getInt("errorCode") == 0) {
			
			Gson gson = new Gson();
			rota = gson.fromJson(jsonResposta.getJSONObject("data").toString(),RotaApp.class);
			
		}		
		return rota;

	}
	
	public LoginApp myRotes(int id) throws Exception {

		String resposta = new WSClient().get(URL_WS + "login/myRotes/" + id);		
		JSONObject jsonResposta = new JSONObject(resposta);
		LoginApp login = new LoginApp();
		
		if (jsonResposta.getInt("errorCode") == 0) {
			
			Gson gson = new Gson();
			login = gson.fromJson(jsonResposta.getJSONObject("data").toString(),LoginApp.class);
			
		}		
		return login;

	}


	public List<PessoaApp> getListaPessoa() throws Exception {

		String resposta = new WSClient().get(URL_WS + "pessoa/findAll");


		Gson gson = new Gson();
		ArrayList<PessoaApp> listaPessoa = new ArrayList<PessoaApp>();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(resposta).getAsJsonArray();

		for (int i = 0; i < array.size(); i++) {
			listaPessoa.add(gson.fromJson(array.get(i), PessoaApp.class));
		}
		return listaPessoa;

	}

	public String cadastrarLogin(LoginApp login) throws Exception {
		String resposta = "";
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(login);
			resposta = new WSClient().post(URL_WS + "login/create", loginJSON);
			Log.i("WSTaxishare cadastrarLogin taxi", "URL -> " + URL_WS + " || Resposta -> " + resposta );

		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "cadastrarLogin", "Exception", e);
		}

		return resposta;
	}

	public String editarCadastro(PessoaApp novaPessoa) throws Exception {
		String resposta = "";
		try
		{
			Gson gson = new Gson();
			String pessoaJSON = gson.toJson(novaPessoa);
			resposta = new WSClient().post(URL_WS + "pessoa/edit", pessoaJSON);
		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "editarCadastro", "Exception", e);
		}

		return resposta;
	}

	public String editarSenha(LoginApp loginApp) throws Exception {
		String resposta = "";
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(loginApp);
			resposta = new WSClient().post(URL_WS + "login/editPassword", loginJSON);
		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "editarSenha", "Exception", e);
		}

		return resposta;
	}

	public String checkLogin(String login) {

		String resposta = new WSClient().get(URL_WS + "login/checkLogin?login="+ login);
		return resposta;
	}

	public String criarRota(RotaApp rota) throws Exception {
		String resposta = "";
		try
		{
			Gson gson = new Gson();
			String rotaJson = gson.toJson(rota) ;
			resposta = new WSClient().post(URL_WS + "rota/create", rotaJson);
		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "criarRota", "Exception", e);
		}

		return resposta;
	}

	public String participarRota(int idRota, int idUsuario, EnderecoApp endereco) throws Exception {

		String resposta = "";

		try
		{
			Gson gson = new Gson();
			String enderecoJson = gson.toJson(endereco);
			resposta = new WSClient().put(URL_WS + "rota/joinIn/" + idRota + "/" + idUsuario, enderecoJson);
		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "participarRota", "Exception", e);
		}

		return resposta;
	}
	
	public ArrayList<RotaApp> getRotasPerimetro(List<PerimetroApp> perimetros) throws Exception {

		Gson gson = new Gson();
		String json = gson.toJson(perimetros);
		String resposta = new WSClient().post(URL_WS + "rota/findByPerimeter", json);		
		
		JSONObject jsonResposta = new JSONObject(resposta);
		ArrayList<RotaApp> rotas = new ArrayList<RotaApp>();
		JsonParser parser = new JsonParser();

		if (jsonResposta.getInt("errorCode") == 0) {
			
			JsonArray array = parser.parse(jsonResposta.getJSONArray("data").toString()).getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				rotas.add(gson.fromJson(array.get(i), RotaApp.class));
			}
		}		
		return rotas;

	}
	
	public String sairRota(int idRota, int idUsuario, EnderecoApp endereco) throws Exception {

		String resposta = "";

		try
		{
			Gson gson = new Gson();
			String enderecoJson = gson.toJson(endereco);
			resposta = new WSClient().put(URL_WS + "rota/exitRote/" + idRota + "/" + idUsuario, enderecoJson);
		}
		catch(Exception e)
		{
			Utils.logException("WSTaxishare", "sairrRota", "Exception", e);
		}

		return resposta;
	}


}

