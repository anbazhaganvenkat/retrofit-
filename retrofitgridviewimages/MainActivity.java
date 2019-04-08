package info.camposha.retrofitgridviewimages;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class MainActivity extends AppCompatActivity {


    class Sticker {
        private String name;
        private String id;
        private String photo_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String setId(String id) {
            this.id = id;
            return id;
        }

        public String getphoto_url(){
            return photo_url;
        }

        public void setphoto_url(String photo_url){
            this.photo_url = photo_url;
        }
    }

    interface MyAPIService {
        @GET("/children")
        Call<String> getChildImage( @Header("x-auth-token") String authHeader);
    }

    static class RetrofitClientInstance {

        private static Retrofit retrofit;
        private static final String BASE_URL = "https://api.littlenuggetco.com";

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    class GridViewAdapter extends BaseAdapter{

        public List<Sticker> spacecrafts;
        private MainActivity context;


        public GridViewAdapter(MainActivity context, List<Sticker> spacecrafts){
            this.context = context;
            this.spacecrafts = spacecrafts;
        }



        @Override
        public int getViewTypeCount() {
            return getCount();
        }
        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public int getCount() {
            return spacecrafts.size();
        }

        @Override
        public Object getItem(int position) {
            return spacecrafts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;


            if(convertView==null)
            {
                holder = new ViewHolder();
                LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_gridviewmodel, null, true);
                holder.nuggetImageView = convertView.findViewById(R.id.nuggetImageView);
                convertView.setTag(holder);

            }
            else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder)convertView.getTag();
            }


            Picasso.get().load(spacecrafts.get(position).getphoto_url()).into(holder.nuggetImageView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            return convertView;
        }
        private class ViewHolder {
            protected ImageView nuggetImageView;
        }
    }

    private GridViewAdapter adapter;
    private GridView mGridView;
    ProgressBar myProgressBar;

    private void populateGridView(List<Sticker> StickerList) {
        mGridView = findViewById(R.id.mGridView);
        adapter = new GridViewAdapter(this,StickerList);
        mGridView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar myProgressBar= findViewById(R.id.myProgressBar);
        myProgressBar.setIndeterminate(true);
        myProgressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        Call<List<Sticker>> call = myAPIService.getChildImage("710c421892018373803fa06d74b6d8e8e2ad0c46")
        call.enqueue(new Callback<List<Sticker>>() {

            @Override
            public void onResponse(Call<List<Sticker>> call, Response<List<Sticker>> response) {
                myProgressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Log.i("onSuccess", response.body().toString());
                    populateGridView(response.body());
                    String jsonresponse = response.body().toString();
                    populateGridView(jsonresponse);

                }
            }
            @Override
            public void onFailure(Call<List<Sticker>> call, Throwable throwable) {
                myProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateGridView(String jsonresponse) {

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(jsonresponse);
            ArrayList<GridView> spacecraftList = new ArrayList<>();
            JSONArray dataArray  = obj.getJSONArray( "svg");
            Log.d("sssssawass", new Gson().toJson(dataArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
//end
