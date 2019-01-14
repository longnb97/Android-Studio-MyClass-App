package longnb.xda.edu.btlandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Email extends AppCompatActivity {
    EditText content, title;
    TextView titlee, contentt;
    Button sendMail, back;
    Boolean cooldown;
    Boolean sendingRequest;
    String classname;
    String emailToSend, parentNameToSend, studentNameToSend, avatarToDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        try{
            if(getIntent().hasExtra("email") && getIntent().hasExtra("parentName") && getIntent().hasExtra("studentName") && getIntent().hasExtra("avatar")){
                emailToSend = getIntent().getStringExtra("email");
                parentNameToSend = getIntent().getStringExtra("parentName");
                studentNameToSend = getIntent().getStringExtra("studentName");
                avatarToDisplay = getIntent().getStringExtra("avatar");

                init();
            }
        }
        catch (Exception e){
            Toast.makeText(Email.this, "trycatch exception!", Toast.LENGTH_LONG).show();
        }


    }


    private static Email context;
    public static Email getInstance(){
        return context;
    }
    public void init(){
        context = this;

        cooldown= false;
        sendingRequest = false;

        sendMail = findViewById(R.id.sendMail);
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);

        back = findViewById(R.id.goBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StudentDetails.this, AllStudents.class);
//                context.startActivity(intent);
                finish();
            }
        });
        UserDetails userDetails = UserDetails.getInstance();

        classname = userDetails.getClassname();

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cooldown){
                    Toast.makeText(Email.this, "Mail vừa được gửi, xin đợi 1 chút rồi thử lại!", Toast.LENGTH_LONG).show();
                }
                cooldown = true;
                postData();
                //set timeout cooldown
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                cooldown = false;
                            }
                        }, 5000);
            }
        });
    }

    public void postData(){
        String url = "https://btlandroid.herokuapp.com/api/mails/send";

        final JSONObject jsonObj = new JSONObject();

        String tit = title.getText().toString();
        String cont = content.getText().toString();

        if(cont.equals("")){
            Toast.makeText(Email.this, "nội dung không được để trống!", Toast.LENGTH_LONG).show();
            cooldown= false;
        }
        else {
            if(tit.equals("")){
                tit = "Thông báo từ lớp " + classname;
            }
            try{
                jsonObj.put("to", emailToSend);
                jsonObj.put("subject", tit);
                jsonObj.put("text", cont);
                jsonObj.put("parentName", parentNameToSend);
                jsonObj.put("studentName", studentNameToSend);

                requestPost(url, jsonObj);

            }catch (JSONException e){
                Toast.makeText(Email.this, "trycatch exception!", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void requestPost (String url, JSONObject jsonObj){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sendingRequest = false;
                        Toast.makeText(Email.this, "mail đã gửi!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendingRequest = false;
                        Toast.makeText(Email.this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsObjRequest);
    }
}
