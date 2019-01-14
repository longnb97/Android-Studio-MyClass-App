package longnb.xda.edu.btlandroid;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllStudents extends AppCompatActivity  {
    FloatingActionButton goToTop;
    ImageView profileTab, allStudentTab;
    RecyclerView recyclerView;
    private ArrayList<StudentJSON> arr;
    private StudentAdapter studentAdapter;
    SwipeRefreshLayout refreshLayout;
    Boolean  isSecondTimeRight;
    RelativeLayout load;
    CoordinatorLayout tab;
    String className;
    SearchView searchh;
    LinearLayout container, searchField;
    Snackbar snack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students);
        init();

    }
    public static AllStudents context;

    public void init(){
        context = this;
        isSecondTimeRight = false;
        tab = findViewById(R.id.tab);

        UserDetails userDetails = UserDetails.getInstance();
        className = userDetails.getClassname();

        goToTop = findViewById(R.id.goToTop);
        profileTab = findViewById(R.id.profileTab);

//        allStudentTab = findViewById(R.id.allStudentTab);

        goToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack = Snackbar.make(v, "Đi đến đầu trang ?", Snackbar.LENGTH_LONG)
                        .setAction("Đồng ý", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (recyclerView != null) {
                                    recyclerView.smoothScrollToPosition(0);
                                }
                            }
                        });
                snack.show();
            }
        });

        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, UserProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                AllStudents.getInstance().startActivity(intent);
            }
        });

//        allStudentTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AllStudents.this, "error1", Toast.LENGTH_SHORT).show();
//            }
//        });

        searchh = findViewById(R.id.search);
        searchh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                studentAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                studentAdapter.getFilter().filter(s);
                return false;
            }
        });

        load = findViewById(R.id.loadingPanel);
        load.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycleView);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipe to refresh data
                loadDataFromApi();
            }
        });
        //load data by defaults
        loadDataFromApi();
    }
    @Override
    public void onBackPressed() {

    }
    public void loadDataFromApi(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://btlandroid.herokuapp.com/api/students/"+className;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        load.setVisibility(View.GONE);// turn off progressBar

                        //turn off refresh icon
                        if (refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        show(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                load.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                Toast.makeText(AllStudents.this, "error1", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static AllStudents getInstance(){
        return context;
    }

    public void show(JSONObject data){
        if(!data.equals("")){
            try {
                arr = new ArrayList<>();
                JSONArray jsonArray = data.getJSONArray("students");
                for (int i = 0; i < jsonArray.length(); i++){
                    // All data
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String gender = jsonObject.getString("gender");
                    String conduct = jsonObject.getString("conduct");
                    String id = jsonObject.getString("_id");
                    String dateOfBirth = jsonObject.getString("dateOfBirth");
                    String studentName = jsonObject.getString("fullname");
                    String className = jsonObject.getString("class");
                    String address = jsonObject.getString("address");
                    String photo = jsonObject.getString("photoUrl");
                    String averageScore = jsonObject.getString("averageScore");

                    // "parents" key
                    JSONObject parentObj = jsonObject.getJSONObject("parents");
                    String phoneNumber = parentObj.getString("phoneNumber");
                    String email = parentObj.getString("email");
                    String parentName = parentObj.getString("name");

                    StudentJSON stJSON = new StudentJSON(conduct, studentName, id, dateOfBirth, className, address, phoneNumber, email, parentName, photo, gender, averageScore);
                    arr.add(stJSON);


                    studentAdapter = new StudentAdapter(arr, getApplicationContext());
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(studentAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(AllStudents.this, "error2", Toast.LENGTH_LONG).show();
        }
    }



}
