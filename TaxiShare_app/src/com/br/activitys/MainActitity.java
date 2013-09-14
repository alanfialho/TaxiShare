package com.br.activitys;

import com.br.adapter.MenuAdapter;
import com.br.fragments.CreateRoteFragment;
import com.br.fragments.EditPasswordFragment;
import com.br.fragments.EditRegisterFragment;
import com.br.fragments.SearchRoteFragment;
import com.br.sessions.SessionManagement;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDashboardOptions;
	private SessionManagement session;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		mDashboardOptions = getResources().getStringArray(R.array.menu_options) ;

		session = new SessionManagement(getApplicationContext());

		MenuAdapter mAdapter = new MenuAdapter(getApplicationContext(), mDashboardOptions);

		mTitle = mDrawerTitle = "TaxiShare";
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		//Seta a sombra do menu (Copiei assim, nao vi isso funcionando)
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		//Define as opções dentro do listview do menu lateral
		mDrawerList.setAdapter(mAdapter);

		//Define as ações dos items no menu lateral instanciando uma subclasse
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		//Pelo que eu entendi, ele permite que o botao da actionbar permita o togle do menu lateral
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//Define umas paradas do togle do menu lateral
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* Activity que contem o menu*/
				mDrawerLayout,         /* objeto DrawerLayout */
				R.drawable.ic_drawer,  /* botaozinho dos pauzinhos do actionBar*/
				R.string.drawer_open,  /* Descricao de abrir - Para acessibilidade */
				R.string.drawer_close  /* "Descricao de fechar - Para acessibilidade*/
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		//Seta comportamento de toggle no menu lateral
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//Nao saquei o que é isso
		if (savedInstanceState == null) {
			selectItem(0);
		}

		//Seta fragmento de dashboard no content frame
		Fragment fragment = new CreateRoteFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
		switch(item.getItemId()) {

		case R.id.action_rote_search:

			Fragment searchRoteFragment = new SearchRoteFragment();
			searchRoteFragment.setArguments(args);
			ftransaction.replace(R.id.content_frame, searchRoteFragment);

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

		//		<item>Buscar Rota</item>
		//		<item>Editar Perfil</item>
		//		<item>Alterar Senha</item>
		//		<item>Logout</item>

		Bundle args = new Bundle();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ftransaction = fragmentManager.beginTransaction();
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
			fragment = new CreateRoteFragment();
			break;
		case 4:
			session.logoutUser();
		}


		fragment.setArguments(args);
		ftransaction.replace(R.id.content_frame, fragment);
		ftransaction.addToBackStack(null);
		ftransaction.commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mDashboardOptions[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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

}