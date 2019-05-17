package guilherme.luzzi.exviaceptarde.service;

import guilherme.luzzi.exviaceptarde.model.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIRetrofitService {

    //https://viacep.com.br/ws/91240090/json
    @GET("{CEP}/json")
    Call<CEP> getCEP(@Path("CEP") String CEP);

}//fecha classe
