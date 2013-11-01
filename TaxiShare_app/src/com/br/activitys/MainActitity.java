package com.br.activitys;

import com.br.adapter.MenuAdapter;
import com.br.fragments.CreateRoteFragment;
import com.br.fragments.EditPasswordFragment;
import com.br.fragments.EditRegisterFragment;
import com.br.fragments.SearchRoteFragment;
import com.br.fragments.UserListRoteFragment;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActitity extends Activity {
	private DrawerLayout menuLateral;
	private ListView listaMenuLateral;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence tituloMenuLateral;
	private CharSequence titulo;
	private String[] opcoes;
	private SessionManagement session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		opcoes = getResources().getStringArray(R.array.menu_options) ;

		session = new SessionManagement(getApplicationContext());

		MenuAdapter mAdapter = new MenuAdapter(getApplicationContext(), opcoes);

		titulo = tituloMenuLateral = "TaxiShare";
		menuLateral = (DrawerLayout) findViewById(R.id.drawer_layout);
		listaMenuLateral = (ListView) findViewById(R.id.left_drawer);

		//Seta a sombra do menu (Copiei assim, nao vi isso funcionando)
		menuLateral.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		//Define as opções dentro do listview do menu lateral
		listaMenuLateral.setAdapter(mAdapter);

		//Define as ações dos items no menu lateral instanciando uma subclasse
		listaMenuLateral.setOnItemClickListener(new DrawerItemClickListener());

		//Pelo que eu entendi, ele permite que o botao da actionbar permita o togle do menu lateral
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//Define umas paradas do togle do menu lateral
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* Activity que contem o menu*/
				menuLateral,         /* objeto DrawerLayout */
				R.drawable.ic_drawer,  /* botaozinho dos pauzinhos do actionBar*/
				R.string.drawer_open,  /* Descricao de abrir - Para acessibilidade */
				R.string.drawer_close  /* "Descricao de fechar - Para acessibilidade*/
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(titulo);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(tituloMenuLateral);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		//Seta comportamento de toggle no menu lateral
		menuLateral.setDrawerListener(mDrawerToggle);

		//Nao saquei o que é isso
		if (savedInstanceState == null) {
			selectItem(0);
		}


		//Seta fragmento home no content frame HENRIQUE
		Utils.changeFragment(getFragmentManager(), new SearchRoteFragment(), null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = menuLateral.isDrawerOpen(listaMenuLateral);
		menu.findItem(R.id.action_rote_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		Bundle args = new Bundle();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ftransaction = fragmentManager.beginTransaction();

		// Handle action buttons
		//HENRIQUE aqui esta definindo a ação do botão da lupa no action bar
		switch(item.getItemId()) {

		case R.id.action_rote_search:

			Fragment SearchRoteFragmentAntigo = new SearchRoteFragment();
			SearchRoteFragmentAntigo.setArguments(args);
			ftransaction.replace(R.id.content_frame, SearchRoteFragmentAntigo);

			break;
		default:
			return super.onOptionsItemSelected(item);
		}


		ftransaction.addToBackStack(null);
		ftransaction.commit();

		return true;

	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		//HENRIQUE
		//É aqui que a parada esta trocando de tela, são essas 4 linhas, mas a linha de case que define o framegment, mais as 2 la em baixo.

		Fragment fragment = new Fragment();		
		switch(position) {

		case 0:
			fragment = new SearchRoteFragment();			
			break;
		case 1:
			fragment = new EditRegisterFragment();
			break;
		case 2:
			fragment = new EditPasswordFragment();
			break;
		case 3:
			fragment = new UserListRoteFragment	();
			break;
		case 4:
			questionaLogout();
		}

		if (position != 4)
		{
			Utils.changeFragment(getFragmentManager(), fragment, null);

			// update selected item and title, then close the drawer
			listaMenuLateral.setItemChecked(position, true);
			setTitle(opcoes[position]);
			menuLateral.closeDrawer(listaMenuLateral);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		titulo = title;
		getActionBar().setTitle(titulo);
	}

	/**
	 * Quando usar  ActionBarDrawerToggle, precisamos chamar
	 * onPostCreate() e onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	//Metodo que chama chama popup questionando se user deseja sair do app
	public void questionaLogout(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// Titulo
		alertDialogBuilder.setTitle("Logout");

		// Adiciona a mensagem na caixa
		alertDialogBuilder
		.setMessage("Deseja efetuar logoff do aplicativo?")
		.setCancelable(false)
		.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
			//Ação se o usuario clicar em "sim"
			public void onClick(DialogInterface dialog,int id) {
				session.logoutUser();
				Intent i = new Intent(MainActitity.this, LoginActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();

			}
		}) //Ação se o usuario clicar em "sim"
		.setNegativeButton("Não",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				dialog.cancel();
			}
		});

		// Cria a caixa de dialogo
		AlertDialog alertDialog = alertDialogBuilder.create();

		// mostra
		alertDialog.show();
	}

}