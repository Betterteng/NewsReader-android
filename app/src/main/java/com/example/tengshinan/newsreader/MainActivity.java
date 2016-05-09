package com.example.tengshinan.newsreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String newsURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20feed%20where%20url=%27www.abc.net.au%2Fnews%2Ffeed%2F51120%2Frss.xml%27&format=json";
    private ArrayList<News> newsAl;
    private OkHttpClient client;
    private NewsAdapter adapter;
    private ListView partyLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup partyListView to the ListView in MainActivity
        partyLv = (ListView) findViewById(R.id.partyLv);

        // Setup OkHttpClient plugin
        client = new OkHttpClient();
        newsAl = new ArrayList<>();

        // Create adapter and associate with ArrayList
        adapter = new NewsAdapter(MainActivity.this, newsAl);
        partyLv.setAdapter(adapter);

        // Setup on item click listener for partyLv
        partyLv.setOnItemClickListener(this);

        // Display news
        new GetNews().execute();
    }

    /**
     * Call this method when any item in the ListView has been selected.
     * This method will pass the URL of selected object to the NewsDetailActivity.
     **/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = newsAl.get(position).getLink();
        Intent i = new Intent(this, NewsDetailActivity.class);
        i.putExtra("url", url);
        startActivityForResult(i, 0);
    }

    /**
     * Call this method when MainActivity is being loaded.
     * This method will call another method to download the news
     * and when it cannot get correct response, system will
     * popup a dialog to require users to try again and stop this method.
     * If everything goes well, refresh the ListView.
     **/
    private class GetNews extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return downloadNews();
        }

        @Override
        protected void onPostExecute(String result) {
            // If we cannot get anything at the onPostExecute status
            if (result == null) {
                // If network connection lost and we get nothing
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Sorry");
                alertDialog.setMessage("We may lost network connection now," +
                        "\ntry again later please.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }

            // If doesn't parse results to ArrayList successfully
            if (!parseResultToArrayList(result)) {
                return;
            }

            // If everything goes well
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Call this method when we need pass the results to the ArrayList.
     * There are two conditions may occur.
     * Unsuccessfully, system will popup a dialog to inform users.
     * Successfully, system will put all the objects we get in the ArrayList.
     **/
    public Boolean parseResultToArrayList(String result) {
        try {
            JSONObject jObjectNews = new JSONObject(result);
            // It there aren't any news we can get, system will
            // popup a dialog to inform users to try again later.
            if (jObjectNews.getJSONObject("query").getInt("count") == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Sorry");
                alertDialog.setMessage("We may lost network connection now," +
                        "\ntry again later please.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return false;
            }

            // If there's no issue, we grab the JSONObjects and open them step by step
            // until we get the objects we want (item).
            // Then, put all the objects in the ArrayList.
            JSONArray jArrayNews = jObjectNews.getJSONObject("query").getJSONObject("results").getJSONArray("item");
            for (int i = 0; i < jArrayNews.length(); i++) {
                News news = new News(jArrayNews.getJSONObject(i));
                newsAl.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Call this method when we need to download the JSONObjects.
     * newsURL is predefined above in the field.
     **/
    public String downloadNews() {
        Request request = new Request.Builder()
                .url(newsURL)
                .build();
        Response response;
        String result = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
