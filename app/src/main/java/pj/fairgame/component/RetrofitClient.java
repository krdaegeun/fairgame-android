package pj.fairgame.component;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //.baseUrl("http://localhost:8080")
    //for virtual
    //.baseUrl("http://10.0.2.2:3000/")

    private static final String BASE_URL="http://10.0.2.2:3000/";

    private RetrofitClient(){}

    private static class RetrofitClientHelper{

        private static final Retrofit INSTANCE =
                new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

    }

    public static Retrofit getInstance() {
        return RetrofitClientHelper.INSTANCE;
    }

}
