package com.thcreation.projectphi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.thcreation.projectphi.Intent.DialupCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Snackbar snackbar;

    Dialog dialog;

    DrawerLayout drawerLayout;

    AutoCompleteTextView ectLaocation,ectGrade,ectCategory;

    Map<String, String> location = new HashMap<>();
    Map<String, String> grade = new HashMap<>();
    Map<String, String> category = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dialog = new Dialog(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ectLaocation = findViewById(R.id.actLocation);
        ectGrade = findViewById(R.id.actGrade);
        ectCategory = findViewById(R.id.actCategory);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocation();
        loadGrade();
        loadCategory();
    }

    private void loadCategory() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/category/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setCategory(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error = "+error.getMessage());
//                Toast.makeText(MainActivity.this,"Error="+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void setCategory(JSONArray response){
        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++){

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("category"));
                category.put(obj.getString("category"),obj.getString("id"));

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        ectCategory.setAdapter(adapter);
    }

    private void loadGrade() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/grade/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setGrade(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error = "+error.getMessage());
//                Toast.makeText(MainActivity.this,"Error="+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void setGrade(JSONArray response){
        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++){

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("grade"));
                grade.put(obj.getString("grade"),obj.getString("id"));

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        ectGrade.setAdapter(adapter);
    }

    private void loadLocation() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/location/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setLocation(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error = "+error.getMessage());
//                Toast.makeText(MainActivity.this,"Error="+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void setLocation(JSONArray response){
        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++){

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("location"));
                location.put(obj.getString("location"),obj.getString("id"));

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        ectLaocation.setAdapter(adapter);
    }

    public void openDialog(){

        String  loc = ectLaocation.getText().toString();
        String lid = location.get(loc);

        if(lid == null){
            snackbar.make(drawerLayout,"Location Should be Select",3000).show();
        }
        else {
            dialog.setContentView(R.layout.dialog_contact);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }



    public void contact(View view) {
        openDialog();

    }

    public void dialogClose(View view) {
        dialog.dismiss();
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
        recreate();
    }

    public void warningDialog(View view){
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void dismiss(View view){
        dialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void callNow(View view) {

        String  loc = ectLaocation.getText().toString();
        String lid = location.get(loc);

        if(lid == null){
            snackbar.make(drawerLayout,"Location Should be Select",3000).show();
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://beezzserver.com/slthadi/projectPHI/contactNow/index.php?Lid=" + lid + "";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            setContact(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error = " + error.getMessage());
                        }
                    });
            queue.add(jsonArrayRequest);

        }
//        DialupCall dialupCall = new DialupCall();
//        startActivity(dialupCall.Call());

    }

    public void setContact(JSONArray response){
        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++){

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("phoneNum"));

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+obj.getString("phoneNum")));
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void goList(View view) {

        String loc = ectLaocation.getText().toString();
        String grd = ectGrade.getText().toString();
        String cat = ectCategory.getText().toString();

        String lid = location.get(loc);
        String gid = grade.get(grd);
        String cid = category.get(cat);

        if(loc.equals("")){
            Snackbar.make(drawerLayout,"Location can not be empty",3000).show();
        }else if(grd.equals("")){
            Snackbar.make(drawerLayout,"Grade can not be empty",3000).show();
        }else if(cat.equals("")){
            Snackbar.make(drawerLayout,"Category can not be empty",3000).show();
        }else {
            Intent in = new Intent(this, ListActivity.class);
            in.putExtra("lid", lid);
            in.putExtra("gid", gid);
            in.putExtra("cid", cid);
            startActivity(in);
        }

    }

}