package com.br.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import android.util.Log;

public class WSClient {

	public final String get(String url) {

		String resultCode = "";
		String result = "";

		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		try {
			Log.i("WSClient Get taxi", "Url -> " + url);
			response = HttpClientSingleton.getHttpClientInstace().execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				//Código da respostas
				resultCode = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				//Resposta
				result = toString(instream);
				instream.close();
				Log.i("WSClient if entity not null taxi", "Result Code " + resultCode);
				Log.i("WSClient if entity not null taxi", "Result: " + result);

				if(!resultCode.equals("200"))					
					result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
			}
		} catch (Exception e) {
			Log.i("Exception WSClient get taxi", "Exception ->" + e);
			result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
		}
		return result;
	}

	public final String post(String url, String json) {
		String resultCode = "";
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setHeader("Content-type", "application/json");
			StringEntity sEntity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(sEntity);

			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstace().execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				resultCode = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result = toString(instream);
				instream.close();
				
				if(!resultCode.equals("200"))					
					result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
			}

		} catch (Exception e) {
			result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
		}
		return result;
	}

	public final String put(String url, String json) {

		String resultCode = "";
		String result = "";
		
		try {
			
			HttpPut httput = new HttpPut(url);
			httput.setHeader("Content-type", "application/json");
			StringEntity sEntity = new StringEntity(json, "UTF-8");
			httput.setEntity(sEntity);
			
			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstace().execute(httput);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				resultCode = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result = toString(instream);
				instream.close();
				
				if(!resultCode.equals("200"))					
					result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
			}
		} catch (Exception e) {
			result = "{\"errorCode\": 1, \"descricao\": \"Falha de rede!\"}";
		}
		return result;
	}

	private String toString(InputStream is) throws IOException {

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}
}