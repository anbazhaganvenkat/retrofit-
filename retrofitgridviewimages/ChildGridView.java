package info.camposha.retrofitgridviewimages;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ChildGridView {
    @GET("")
    Call<String> getChildImage(@Path("") String id, @Header("") String authHeader);
}
