package malang.paradise.com.malangparadise.inteface;

import malang.paradise.com.malangparadise.json.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterApi {
    @FormUrlEncoded
    @POST("/register.php")
    Call<Value> daftar        (@Field("username") String username,
                               @Field("password") String password,
                               @Field("nama") String nama);
}
