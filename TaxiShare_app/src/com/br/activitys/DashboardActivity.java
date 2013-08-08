/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import com.br.sessions.SessionManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends Activity {
	Button btnLogout;
	Button btnEditar;
	SessionManagement session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		session = new SessionManagement(getApplicationContext());
		btnLogout = (Button) findViewById(R.id.btnLogout);	
		btnEditar = (Button) findViewById(R.id.btnEditar);

		
		btnLogout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				session.logoutUser();
			};

		});

		btnEditar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),
						EditRegisterActivity.class);
				startActivity(intent);
				finish();
			};

		});
	}
}