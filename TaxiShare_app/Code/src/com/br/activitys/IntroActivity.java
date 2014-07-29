package com.br.activitys;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finishscreen();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 3500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro, menu);
		return true;
	}

	private void finishscreen() {
        this.finish();
    }
}
