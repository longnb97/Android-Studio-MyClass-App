package longnb.xda.edu.btlandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class StudentProfile extends AppCompatActivity {
    ImageView ava;
    TextView name;
    TextView address;
    TextView conduct;
    TextView parentname;
    TextView phonenumber;
    TextView classname;
    Button back;
    TextView averagesc;
    TextView email;
    TextView dob;
    ScrollView scroll;
    FloatingActionButton mail, callFloatBtn;
    Boolean isSecondTimeDown, isSecondTimeRight;
    Boolean call;

    String sendEmail, sendParentName, sendStudentName, sendAva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_wrap);
        init();
        initSwipe();
    }

    private static StudentProfile context;

    public void init() {
        context = this;

        ava = findViewById(R.id.ava);
        name = findViewById(R.id.stName);
        address = findViewById(R.id.address);
        conduct = findViewById(R.id.conduct);
        parentname = findViewById(R.id.parentname);
        averagesc = findViewById(R.id.averagesc);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        scroll = findViewById(R.id.scroll);
        phonenumber = findViewById(R.id.phonenumber);
        classname = findViewById(R.id.classname);

        back = findViewById(R.id.back);
        callFloatBtn = findViewById(R.id.calll);
        callFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Bạn muốn gọi chứ ?", Snackbar.LENGTH_LONG)
                        .setAction("Có", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String num = phonenumber.getText().toString();
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + num));
                                startActivity(callIntent);
                                call = false;
                            }
                        }).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mail = findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Bạn muốn gửi mail chứ ?", Snackbar.LENGTH_LONG)
                        .setAction("Có", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startMailActivity();
                            }
                        }).show();
            }
        });

        isSecondTimeDown = isSecondTimeRight = call = false;

        if (getIntent().hasExtra("classname") && getIntent().hasExtra("image") && getIntent().hasExtra("address") && getIntent().hasExtra("name") && getIntent().hasExtra("averageScore") && getIntent().hasExtra("address") && getIntent().hasExtra("dob") && getIntent().hasExtra("parentName") && getIntent().hasExtra("phoneNumber") && getIntent().hasExtra("email") && getIntent().hasExtra("conduct")) {
            String Classname = getIntent().getStringExtra("classname");
            String Photo = getIntent().getStringExtra("image");
            String Name = getIntent().getStringExtra("name");
            String AverageScore = getIntent().getStringExtra("averageScore");
            String Address = getIntent().getStringExtra("address");
            String Dob = getIntent().getStringExtra("dob");
            String ParentName = getIntent().getStringExtra("parentName");
            String PhoneNumber = getIntent().getStringExtra("phoneNumber");
            String Email = getIntent().getStringExtra("email");
            String Conduct = getIntent().getStringExtra("conduct");
            sendEmail = Email;
            sendStudentName = Name;
            sendParentName = ParentName;
            sendAva = Photo;
            Picasso.get()
                    .load(Photo)
                    .into(ava);
            name.setText(Name);
            classname.setText(Classname);
            address.setText(Address);
            conduct.setText(Conduct);
            parentname.setText(ParentName);
            phonenumber.setText(PhoneNumber);
            averagesc.setText(AverageScore);
            email.setText(Email);
            dob.setText(Dob);

        } else {
            Log.d("nohas", "SDSDSD");
        }

    }


    public void initSwipe() {
        scroll.setOnTouchListener(new OnSwipeTouchListener(StudentProfile.this) {
            public void onSwipeTop() {
//                Toast.makeText(StudentDetails.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Intent intent = new Intent(StudentProfile.this, AllStudents.class);
                context.startActivity(intent);
            }

            public void onSwipeLeft() {
                Intent intent = new Intent(StudentProfile.this, AllStudents.class);
                context.startActivity(intent);
            }

            public void onSwipeBottom() {

            }

        });
    }

    public void startMailActivity() {
        Intent intent = new Intent(StudentProfile.this, Email.class);
        intent.putExtra("email", sendEmail);
        intent.putExtra("parentName", sendParentName);
        intent.putExtra("studentName", sendStudentName);
        intent.putExtra("avatar", sendAva);
        context.startActivity(intent);
    }
}
