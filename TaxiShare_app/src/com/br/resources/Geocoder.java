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

public class Geocoder {

	private Context context;
	private Locale locale;

	public Geocoder(Context context, Locale locale) {
		this.context = context;
		this.locale = locale;
	}

	public List<Address> getAddresses(String address) {

		List<Address> addresses = new ArrayList<Address>();

		address = address.replace(" ", "+");
		String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true";

		AQuery aq = new AQuery(context);

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					try {
						JSONArray results = json.getJSONArray("results");
						
						Utils.gerarToast(context, "RESULTS " + results.toString() );
						
						for (int i = 0; i < results.length(); i++) {
							JSONObject result = results.getJSONObject(i);
//							Utils.gerarToast(context, "result " + result.toString() );
							JSONArray addressComponent = result.getJSONArray("address_components");
							JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
							
							Address obj = new Address(Locale.getDefault());
							
							obj.setLatitude(location.getDouble("lat"));
							obj.setLongitude(location.getDouble("lng"));
							
							Utils.gerarToast(context, "Location " + location.getDouble("lat") );
							
							for (int j = 0; j < addressComponent.length(); j++) {
								
								JSONObject component = addressComponent.getJSONObject(i);
								
								String longValue = component.getString("long_name");
								
								String type = component.getJSONArray("types").getString(0).toUpperCase();

								Utils.gerarToast(context, "ENUM " + type + " --  " + Types.valueOf(type).getStatusCode());
								switch (Types.valueOf(type).getStatusCode()) {

								case 1:
									// endereço
									obj.setThoroughfare(longValue);
									break;

								case 2:
									// endereço
									obj.setSubLocality(longValue);
									break;

								case 3:
									// endereço
									obj.setLocality(longValue);
									break;

								case 4:
									// endereço
									obj.setAdminArea(longValue);
									break;

								case 5:
									// endereço
									obj.setCountryName(longValue);
									break;

								case 6:
									// endereço
									obj.setPostalCode(longValue);
									break;

								default:
									break;
								}								
							}							
							
							addresses.add(obj);
						}

					} catch (JSONException e) {
						Utils.gerarToast(context, e.getMessage());
					}

				} else {

					Utils.gerarToast(context, "erro no aquery");

				}
			}
		});

		return addresses;
	}
}
