package info.camposha.retrofitgridviewimages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ChildphotosGridview {

    private MainActivity.GridViewAdapter adapter;
    private android.widget.GridView GridView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.activity_gridview, container, false);
        GridView =  view.findViewById(R.id.GridView);
        getJSONResponse();
        return view;

    }
    private void getJSONResponse() {

        /*Create handle for the RetrofitInstance interface*/
        ChildGridView myAPIService = MainActivity.RetrofitClientInstance.getRetrofitInstance().create(ChildGridView.class);

        Call<String> call = myAPIService.getChildImage("","");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Log.i("onSuccess", response.body().toString());
                    String jsonresponse = response.body().toString();
                    populateGridView(jsonresponse);

                }
                else if (response.code() == 400)  {
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
            }
        });
    }

    private void populateGridView(String response) {
        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            ArrayList<GridView> spacecraftList = new ArrayList<>();
            JSONArray dataArray  = obj.getJSONArray( "");
            for (int i = 0; i < dataArray.length(); i++) {
                GridView modelListView = new GridView();
                JSONObject dataobj = dataArray.getJSONObject(i);
                modelListView.setthumbnail_url(dataobj.getString(""));
                spacecraftList.add(modelListView);
            }
            adapter = new MainActivity.GridViewAdapter(this, spacecraftList);
            GridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
