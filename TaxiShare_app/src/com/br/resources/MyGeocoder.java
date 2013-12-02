package com.br.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class MyGeocoder {

	private Context context;
	private List<Address> addresses;
	

	public MyGeocoder(Context context) {
		this.context = context;
		addresses = new ArrayList<Address>();

	}

	public List<Address> getAddresses(String address) {		

		address = address.replace(" ", "+");
		String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true";

		AQuery aq = new AQuery(context);

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

			
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					try {						
						//Pega o results
						JSONArray results = json.getJSONArray("results");

						//Itera dentro dos results
						for (int i = 0; i < results.length(); i++) {
							//Pega o result
							JSONObject result = results.getJSONObject(i);
							//Pega o addrees component dentro do result							
							JSONArray addressComponent = result.getJSONArray("address_components");
							//Pega o location dentro do geometry
							JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
							//Cria um Address
							Address obj = new Address(Locale.getDefault());
							//Seta a latitude
							obj.setLatitude(location.getDouble("lat"));
							//Seta a longitude
							obj.setLongitude(location.getDouble("lng"));
							
							//Agora eu monto os dados do address iterando nos componetes
							for (int j = 0; j < addressComponent.length(); j++) {

								//pego o componete
								JSONObject component = addressComponent.getJSONObject(j);
								//pego o nome do componente
								String longValue = component.getString("long_name");
								//Pego o tipo do componente
								String type = component.getJSONArray("types").getString(0).toUpperCase();
								
								//Verifico onde vai o componente
								switch (Types.valueOf(type)) {

								//Rua
								case ROUTE :
									obj.setThoroughfare(longValue);
									break;

									//Numero
								case  STREET_NUMBER:
									obj.setSubThoroughfare(longValue);
									break;

									//Bairro
								case  SUBLOCALITY:
									obj.setSubLocality(longValue);
									break;

									//Cidade
								case  LOCALITY:
									obj.setLocality(longValue);
									break;

									//Estado
								case  ADMINISTRATIVE_AREA_LEVEL_1:
									obj.setLocality(longValue);
									break;									

									//Pais
								case  COUNTRY:
									obj.setCountryName(longValue);
									break;

									//CEP
								case  POSTAL_CODE:
									obj.setPostalCode(longValue);
									break;
									
								default:
									break;
								} //fim do switch								
							}//fim do for dos componentes		'	
							
							//Adiciono o Address no array
							addresses.add(obj);
						}//Fim do for dos results						
						

					} catch (JSONException e) {
						Utils.gerarToast(context, e.getMessage());
					}
					
				} else {
					//Se o json vier vazio
					Utils.gerarToast(context, "json null =>  " + json);
				}
			}//Fim do callback
		});//Fim do AQUERY
		
	
		return addresses;
		
	}	
}



