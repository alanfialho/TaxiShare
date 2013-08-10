//package com.br.activitys;
//
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//import android.view.View;
//import com.br.activitys.R;
//import com.br.entidades.*;
//import android.widget.*;
//
//
//public class MainActivity extends Activity {
//	
//	//controles da tela
//    EditText etId,etCel,etData,etDDD,etEmail,etNome,etSexo;
//	Button btCadastrar,btConsultar,btAlterar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        
//        etId = (EditText) findViewById(R.id.etId);
//        etCel = (EditText) findViewById(R.id.etCel);
//        etData = (EditText) findViewById(R.id.etData);
//        etDDD = (EditText) findViewById(R.id.etDDD);
//        etEmail = (EditText) findViewById(R.id.etEmail);
//        etNome = (EditText) findViewById(R.id.etNome);
//        etSexo = (EditText) findViewById(R.id.etSexo);
//        btCadastrar = (Button) findViewById(R.id.btCadastro);
//        btConsultar = (Button) findViewById(R.id.btConsultar);
//        btAlterar = (Button) findViewById(R.id.btAlterar);
//        
//       
//    	
//    	//listener dos botões
//    	btCadastrar.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				NovaPessoaApp pessoa = new NovaPessoaApp();
//				pessoa.setCelular(etCel.getText().toString());
//				pessoa.setDataNascimento(etData.getText().toString());
//				pessoa.setEmail(etEmail.getText().toString());
//				pessoa.setNome(etNome.getText().toString());
//				
//	            WSTaxiShare ws = new WSTaxiShare();
//	            try 
//	            {
////	                 String resposta = ws.cadastrarPessoa(pessoa);
////	                 gerarToast(resposta);
//	            } 
//	            catch (Exception e) {
//	                 e.printStackTrace();
//	                 gerarToast(e.getMessage());
//	            }
//			}
//		});
//    	
//    	btConsultar.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				WSTaxiShare ws = new WSTaxiShare();
//	            try 
//	            {
////	                 Pessoa pessoa = ws.getPessoa(Integer.parseInt(etId.getText().toString()));
////	                 etCel.setText(pessoa.getCelular());
////	                 etData.setText(pessoa.getDataNascimento());
////	                 etDDD.setText(pessoa.getDddCelular());
////	                 etEmail.setText(pessoa.getEmail());
////	                 etNome.setText(pessoa.getNome());
////	                 etSexo.setText(pessoa.getSexo());
//	            } 
//	            catch (Exception e) {
//	                 e.printStackTrace();
//	                 gerarToast(e.getMessage());
//	            } 
//			}
//		});
//    }
//    
//    private void gerarToast(CharSequence message) {
//        int duration = Toast.LENGTH_LONG;
//        Toast toast = Toast
//                .makeText(getApplicationContext(), message, duration);
//        toast.show();
//       }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    
//}
