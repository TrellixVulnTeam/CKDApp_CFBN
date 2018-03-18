package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Diet extends AppCompatActivity {

    // Request Codes for different meals
    public static final int REQUEST_CODE_Breakfast = 1111;
    public static final int REQUEST_CODE_Lunch = 2222;
    public static final int REQUEST_CODE_Dinner = 3333;
    public static final int REQUEST_CODE_Snacks = 4444;

    private Button btnAddBreakfast;
    private Button btnAddLunch;
    private Button btnAddDinner;
    private Button btnAddSnacks;
    private String thisFoodName;
    private int thisFoodNbdNo;

    // Hashmaps for every meal type (will probably not use)
    Map<String,foodItem> breakfast = new HashMap<>();
    Map<String,foodItem> lunch = new HashMap<>();
    Map<String,foodItem> dinner = new HashMap<>();
    Map<String,foodItem> snacks = new HashMap<>();

    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        btnAddBreakfast = (Button) findViewById(R.id.addBreakfast_btn);
        btnAddLunch = (Button) findViewById(R.id.addLunch_btn);
        btnAddDinner = (Button) findViewById(R.id.addDinner_btn);
        btnAddSnacks = (Button) findViewById(R.id.addSnacks_btn);

        btnAddBreakfast.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Breakfast);
            }
        });

        btnAddLunch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Lunch Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Lunch);
            }
        });

        btnAddDinner.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Dinner);
            }
        });

        btnAddSnacks.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Snacks);
            }
        });

    }

    // Listener for result back from SearchFood
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String intrequestcode = Integer.toString(requestCode);
        Log.i("request code was: ", intrequestcode);

        if(resultCode == Activity.RESULT_OK)
        {
            thisFoodName = data.getStringExtra("FoodName");
            thisFoodNbdNo = data.getIntExtra("NdbNo", 0);
            String intfoodNdbNo = Integer.toString(thisFoodNbdNo);
            Log.i("Logging foodName: " ,thisFoodName);
            Log.i("Logging NdbNo: " ,intfoodNdbNo);

            // Put values back into foodItem class
            foodItem newFood = new foodItem(thisFoodName, thisFoodNbdNo);

            String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());

            // Creates a Diet Child Node under Data
            // Creates a UID Child Node under Diet
            DatabaseReference Data_node = db.getReference("Data");
            DatabaseReference Diet_node = db.getReference("Diet");
            DatabaseReference UID_node = Diet_node.child(UID);

            switch(requestCode)
            {
                case 1111: // Breakfast
                    DatabaseReference Breakfast_node = db.getReference("Breakfast");
                    UID_node.child(date).child("Breakfast").setValue(newFood);
                    return;

                case 2222: // Lunch
                    DatabaseReference Lunch_node = db.getReference("Lunch");
                    UID_node.child(date).child("Lunch").setValue(newFood);
                    return;

                case 3333: // Dinner
                    DatabaseReference Dinner_node = db.getReference("Dinner");
                    UID_node.child(date).child("Dinner").setValue(newFood);
                    return;

                case 4444: // Snacks
                    DatabaseReference Snacks_node = db.getReference("Snacks");
                    UID_node.child(date).child("Snacks").setValue(newFood);
                    return;

                default:
                    Breakfast_node = db.getReference("Breakfast");
                    UID_node.child(date).child("Breakfast").setValue(newFood);
                    return;
            }


        }

        else
        {
            // Handle bad result from SearchFood.java
        }
    }

}

