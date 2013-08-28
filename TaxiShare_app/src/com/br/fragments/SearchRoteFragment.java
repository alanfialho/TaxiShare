package com.br.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.activitys.R;
import com.br.sessions.SessionManagement;

public class SearchRoteFragment extends Fragment {

	Context context;
	SessionManagement session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rote_search, container, false);

		session = new SessionManagement(rootView.getContext());

		//Checa se o usuario esta logado
		session.checkLogin();

		return rootView;
	}
}
