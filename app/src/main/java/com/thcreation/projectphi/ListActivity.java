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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    String lid = "0";
    String gid = "0";
    String cid = "0";

    ListView lv;

    int[] array;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        dialog = new Dialog(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        lid=bundle.getString("lid");
        gid=bundle.getString("gid");
        cid=bundle.getString("cid");

        lv=findViewById(R.id.place_list);

        array = new int[]{R.drawable.pic1,R.drawable.pic2, R.drawable.pic5, R.drawable.pic6, R.drawable.pic3, R.drawable.pic4};
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadList();
    }

    public void LoadList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/place/index.php?Lid="+lid+"&gid="+gid+"&cid="+cid+"";
        System.out.println("!!!!!!!!!!!!!!!url="+url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){openDialog();}
                        else{setPlace(response);}
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

    public void  setPlace(JSONArray response){

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            for(int i=0; i<response.length(); i++){

                JSONObject object = response.getJSONObject(i);

                Toast.makeText(ListActivity.this, "id", Toast.LENGTH_SHORT).show();

                HashMap<String, String> map = new HashMap<>();
                map.put("id", object.getString("id"));
                map.put("name", object.getString("name"));
                map.put("food", object.getString("food"));
                map.put("description", object.getString("description"));
                map.put("price", object.getString("price"));
                map.put("imgUri", object.getString("imgUri"));
                map.put("rating", object.getString("reating"));
                map.put("img", Integer.toString(array[i]));


                if((object.getString("id")).equals("")){openDialog();}
                else{list.add(map);}

            }

                int layout = R.layout.item_placelist;
                int[] views = {R.id.pid,R.id.tvName,R.id.tvFood,R.id.tvPrice,R.id.tvDes,R.id.img,R.id.tvRate};
                String[] colums = {"id","name","food","price","description","img","rating"};
                SimpleAdapter adapter = new SimpleAdapter(this,list,layout,colums,views);
                lv.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
            openDialog();
        }
    }

    public void openDialog(){
        dialog.setContentView(R.layout.dialog_error);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void  goDescription(View view){

        TextView id = view.findViewById(R.id.pid);
        TextView name = view.findViewById(R.id.tvName);
        TextView desc = view.findViewById(R.id.tvDes);
        TextView price = view.findViewById(R.id.tvPrice);
        TextView rate = view.findViewById(R.id.tvRate);

        Intent in = new Intent(this,DescriptionActivity.class);
        in.putExtra("pid",id.getText().toString());
        in.putExtra("name",name.getText().toString());
        in.putExtra("desc",desc.getText().toString());
        in.putExtra("price",price.getText().toString());
        in.putExtra("rate",rate.getText().toString());

        //Toast.makeText(this, id.getText().toString()+"des: "+desc.getText().toString(), Toast.LENGTH_SHORT).show();
        startActivity(in);

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

    public void goDesc(View view) {
        Intent in = new Intent(this,DescriptionActivity.class);
        startActivity(in);
    }
}