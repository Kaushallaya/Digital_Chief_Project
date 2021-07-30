package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity implements Response.Listener<String>,Response.ErrorListener {

    DrawerLayout drawerLayout;

    Dialog dialog;

    RatingBar ratingBar,fbratebar;
    TextView tvRate;
    EditText etname,etDiscription;

    String pid="0";
    String rate = "0";

    ListView lv;

    int[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        dialog = new Dialog(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        lv = findViewById(R.id.listView);
        ratingBar = findViewById(R.id.fdratingBar);
        tvRate = findViewById(R.id.txvRate);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        pid=bundle.getString("pid");
        rate=bundle.getString("rate");

        tvRate.setText(rate);
        ratingBar.setRating(Float.parseFloat(rate));

        array = new int[]{R.drawable.h4,R.drawable.pic2, R.drawable.h2, R.drawable.pic6, R.drawable.h1, R.drawable.pic4,R.drawable.h4,R.drawable.pic2, R.drawable.h2, R.drawable.pic6, R.drawable.h1, R.drawable.pic4,R.drawable.h4,R.drawable.pic2, R.drawable.h2, R.drawable.pic6, R.drawable.h1, R.drawable.pic4};
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadList();
    }

    public void LoadList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/feedback/index.php?pid="+pid+"";
        System.out.println("!!!!!!!!!!!!!!!url="+url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setFeedback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);
    }

    public void  setFeedback(JSONArray response){

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            for(int i=0; i<response.length(); i++){

                JSONObject object = response.getJSONObject(i);


                HashMap<String, String> map = new HashMap<>();
                map.put("id", object.getString("id"));
                map.put("name", object.getString("name"));
                map.put("feedback", object.getString("feedback"));
                map.put("reating", object.getString("reating"));
                map.put("img", Integer.toString(array[i]));

               // Toast.makeText(this, "reating="+object.getString("reating"), Toast.LENGTH_SHORT).show();

                if(Float.parseFloat(object.getString("reating"))< 2.5) {
                    map.put("icon", Integer.toString(R.drawable.ic_dislike));
                }else{
                    map.put("icon", Integer.toString(R.drawable.ic_like));}

                list.add(map);

            }

            int layout = R.layout.item_feedback;

            int[] views = {R.id.fid,R.id.tvName,R.id.tvfeedback,R.id.img,R.id.iconLike};

            String[] colums = {"id","name","feedback","img","icon"};

            SimpleAdapter adapter = new SimpleAdapter(this,list,layout,colums,views);
            lv.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }






    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        redirectActivity(this,HomeActivity.class);
    }

    public void warningDialog(View view){
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void dismiss(View view){
        dialog.dismiss();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void openDialog(View view){
        dialog.setContentView(R.layout.dialog_feedback);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addnow(View view) {
        etname = dialog.findViewById(R.id.fbName);
        etDiscription = dialog.findViewById(R.id.fbfeedback);
        fbratebar = dialog.findViewById(R.id.fbratingBar);

        final String fbkname = etname.getText().toString();
        final String fbdescription = etDiscription.getText().toString();
        final float fbrate = fbratebar.getRating();

        if(fbkname.equals("")){
            Snackbar.make(drawerLayout,"Name can not be empty",3000).show();
        }else if(fbdescription.equals("")){
            Snackbar.make(drawerLayout,"Description can not be empty",3000).show();
        }else {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://beezzserver.com/slthadi/projectPHI/feedback/insert.php";
            System.out.println("url = "+url);
            StringRequest request = new StringRequest(Request.Method.POST,url,this,this){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("pid",pid);
                    params.put("name", fbkname);
                    params.put("description",fbdescription);
                    params.put("rate", String.valueOf(fbrate));

                    return params;
                }
            };
            queue.add(request);
        }

//        Toast.makeText(this, "name:"+fbrate, Toast.LENGTH_SHORT).show();
//        dialog.dismiss();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(this, "error = "+error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        Snackbar.make(drawerLayout,"Thank you for your feedback",3000).show();
        dialog.dismiss();
        Intent in = new Intent(this,FeedbackActivity.class);
        in.putExtra("pid",pid);
        in.putExtra("rate",rate);
        startActivity(in);
        finish();
    }
}