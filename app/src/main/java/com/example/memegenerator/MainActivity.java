package com.example.memegenerator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public String url1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadMeme();

    }

    public void LoadMeme(){

        //Getting JSON Object Using Volley Library -->>

        ProgressBar progress = (ProgressBar) findViewById(R.id.progresso);
        progress.setVisibility(View.VISIBLE);
        String url = "https://meme-api.herokuapp.com/gimme"; //link

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    MainActivity main = new MainActivity();

                    //Getting only url from API -->>

                    try {
                        main.url1= response.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ImageView imageView = (ImageView) findViewById(R.id.meme);

                    //Downloading and displaying image from url using Glide Library

                    Glide.with(this).load(main.url1).listener(new RequestListener<Drawable>() {

                        //on Failing of load-->>

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progress.setVisibility(View.INVISIBLE);
                            imageView.setImageResource(R.drawable.internet123);

                            return false;
                        }

                        //On Successful Load -->>

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    }).into(imageView);

                },

                //On API Call Error -->>
                        error -> {
                    ImageView imageView = (ImageView) findViewById(R.id.meme);
                    progress.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.internet123);
                });

        // Access the RequestQueue through your singleton class.

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }


    public void NextFun(View view) {

        //On Clicking Next -->>

        final AlphaAnimation button = new AlphaAnimation(1F,0.7F);
        view.startAnimation(button);
        LoadMeme();
    }

    public void SendFun(View view) {

        //On Clicking Send -->>

        final AlphaAnimation button = new AlphaAnimation(1F,0.7F);
        view.startAnimation(button);

        MainActivity main = new MainActivity();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,("Hey, Check This cool meme I found in Redit : "+main.url1));
        Intent chooser = Intent.createChooser(intent,"Share using");
        startActivity(chooser);
    }
}