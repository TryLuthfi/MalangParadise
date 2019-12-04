package malang.paradise.com.malangparadise.inteface;

import malang.paradise.com.malangparadise.json.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateProfileApi {
    @FormUrlEncoded
    @POST("/editprofile.php")
    Call<Value> editprofile(@Field("username") String username,
                       @Field("password") String password,
                       @Field("nama") String nama,
                       @Field("id_user") String id);
}
