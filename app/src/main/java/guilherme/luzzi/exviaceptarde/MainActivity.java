package guilherme.luzzi.exviaceptarde;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import guilherme.luzzi.exviaceptarde.model.CEP;
import guilherme.luzzi.exviaceptarde.service.APIRetrofitService;
import guilherme.luzzi.exviaceptarde.service.CEPDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etCEP;
    private Button btBuscar;
    private ProgressBar progress;
    private TextView tvRua;
    private TextView tvBairro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Refs
        etCEP = findViewById(R.id.ma_et_cep);
        btBuscar = findViewById(R.id.ma_bt_buscar);
        progress = findViewById(R.id.ma_progress);
        tvBairro = findViewById(R.id.ma_tv_bairro);
        tvRua = findViewById(R.id.ma_tv_rua);

        //deixando a progress oculta
        progress.setVisibility(View.GONE);

        Gson g =  new GsonBuilder()
                .registerTypeAdapter(CEP.class, new CEPDeserializer()).create();


        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();

        final APIRetrofitService service = retrofit.create(APIRetrofitService.class);


        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(etCEP.getText().toString().isEmpty()){
                     toast("Preencha o CEP:");
                 }else{
                     progress.setVisibility(View.VISIBLE);

                     //passando oque o usuário digitou
                     Call<CEP> callCEP =  service.getCEP(etCEP.getText().toString());
                     callCEP.enqueue(new Callback<CEP>() {
                         @Override
                         public void onResponse(Call<CEP> call, Response<CEP> response) {
                             if (response.isSuccessful()){
                                 //Chegou aqui
                                 CEP cep = response.body();
                                 toast("Cep consumido: "+cep.toString());
                                 tvRua.setText(cep.getLogradouro());
                                 tvBairro.setText(cep.getBairro());
                             }else {
                                 toast("Erro onResponse");
                             }//fecha else
                             progress.setVisibility(View.GONE);
                         }

                         @Override
                         public void onFailure(Call<CEP> call, Throwable t) {
                             toast("DEU MERDA! " +t.getMessage());
                         }
                     });
                 }//fehca else
            }//clique
        });//clique

    }//fecha onCreate

    public void  toast(String msg){
        Toast.makeText(getBaseContext(),
                msg, Toast.LENGTH_LONG).show();
    }
}//fecha classe
