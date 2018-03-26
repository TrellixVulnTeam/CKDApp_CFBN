package CKD.Android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    // Arraylists for foodNames and Ndbnos for each meal
    ArrayList<String> breakfastNames, breakfastNdbnos,
                        lunchNames, lunchNdbnos,
                        dinnerNames, dinnerNdbnos,
                        snackNames, snackNdbnos;

    Map <String,ArrayList<String>> mealNameLists = new HashMap<>();
    Map <String,ArrayList<String>> mealNdbnoLists = new HashMap<>();
    Map <String, LinearLayout> llList = new HashMap<>();

    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        //TODO remove once searchclass is good
        AppData.updateDailyChecklist("Diet");

       // mDatabse = FirebaseDatabase.getInstance().getReference();

        initializeButtons();
        initializeAndStoreMealNameLists();
        initializeAndStoreMealLayoutLists();
        setOnClickListeners();

        grabAllUsersDietSubmissions();

    }

    private void initializeAndStoreMealLayoutLists()
    {
        LinearLayout breakfastLL = findViewById(R.id.Diet_LL_Breakfast);
        LinearLayout lunchLL = findViewById(R.id.Diet_LL_Lunch);
        LinearLayout dinnerll = findViewById(R.id.Diet_LL_Dinner);
        LinearLayout snacksll = findViewById(R.id.Diet_LL_Snacks);

        llList.put("Breakfast", breakfastLL);
        llList.put("Lunch",lunchLL);
        llList.put("Dinner",dinnerll);
        llList.put("Snacks",snacksll);
    }

    private void initializeAndStoreMealNameLists()
    {
        // Arraylists for foodNames and Ndbnos for each meal
        breakfastNames = new ArrayList<>();
        mealNameLists.put("Breakfast",breakfastNames);

        breakfastNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Breakfast", breakfastNdbnos);

        lunchNames = new ArrayList<>();
        mealNameLists.put("Lunch",lunchNames);

        lunchNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Lunch",lunchNdbnos);

        dinnerNames = new ArrayList<>();
        mealNameLists.put("Dinner",dinnerNames);

        dinnerNdbnos = new ArrayList<>();

        mealNdbnoLists.put("Dinner", dinnerNdbnos);

        snackNames = new ArrayList<>();
        mealNameLists.put("Snacks",snackNames);

        snackNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Snacks",snackNdbnos);

    }

    private void setOnClickListeners()
    {
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

    private void initializeButtons()
    {
        btnAddBreakfast = findViewById(R.id.addBreakfast_btn);
        btnAddLunch = findViewById(R.id.addLunch_btn);
        btnAddDinner = findViewById(R.id.addDinner_btn);
        btnAddSnacks = findViewById(R.id.addSnacks_btn);
    }

    private void grabAllUsersDietSubmissions()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Date_node = db.getReference()
                                        .child("Data")
                                        .child("Diet")
                                        .child(UID)
                                        .child(AppData.getTodaysDate());

        Date_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Collects all of the users previously submitted data
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    String meal = d.getKey();
                    grabMealSubmitted(meal,d);
                }
            }

            private void grabMealSubmitted(String meal, DataSnapshot d)
            {
                String foodID = (String) d.child("Food NDBs").getValue();
                String foodNames =(String) d.child("Food Names").getValue();

                assert foodNames != null;
                foodNames = foodNames.replace("[" ,"");
                foodNames = foodNames.replace("]","");

                assert foodID != null;
                foodID = foodID.replace("[","");
                foodID = foodID.replace("]","");

                // Commas within the Name of each food item are currently replaced with "_"
                ArrayList<String> ndbnoList = new ArrayList<>(Arrays.asList(foodID.split(",")));
                ArrayList<String> nameList  = new ArrayList<>(Arrays.asList(foodNames.split(",")));

                // Stores the data into the HashMap for corresponding meal
                mealNameLists.get(meal).addAll(nameList);
                mealNdbnoLists.get(meal).addAll(ndbnoList);

                addFoodItems(meal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void addFoodItems( String meal)
     {
        LinearLayout linear = llList.get(meal);
        ArrayList<String> nameList = mealNameLists.get(meal);
        ArrayList<String> ndbnoList = mealNdbnoLists.get(meal);

        for (int j = 0; j < nameList.size() ; j++)
        {
            LinearLayout childLayout = new LinearLayout(this);

            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            childLayout.setOrientation(LinearLayout.VERTICAL);

            childLayout.setLayoutParams(linearParams);

            TextView fName = new TextView(this);
            TextView fID = new TextView(this);

            fName.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fID.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fName.setTextSize(17);
            fName.setPadding(5, 3, 0, 3);
            fName.setTypeface(Typeface.DEFAULT_BOLD);
            fName.setGravity(Gravity.START | Gravity.CENTER);

            fID.setTextSize(16);
            fID.setPadding(5, 3, 0, 3);
            fID.setTypeface(null, Typeface.ITALIC);
            fID.setGravity(Gravity.START | Gravity.CENTER);

            fName.setText(nameList.get(j).replaceAll("_",","));
            fID.setText(ndbnoList.get(j));

            childLayout.addView(fName);
            childLayout.addView(fID);

            linear.addView(childLayout);
        }
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
            String intFoodNdbNo = Integer.toString(thisFoodNbdNo);

            Log.i("Logging foodName: " ,thisFoodName);
            Log.i("Logging NdbNo: " ,intFoodNdbNo);

            enterSelectedFoodItemIntoDatabase(requestCode, thisFoodName, thisFoodNbdNo );

        }

        else
        {
            // Handle bad result from SearchFood.java
        }
    }

    private void enterSelectedFoodItemIntoDatabase(int requestCode, String thisFoodName, int thisFoodNbdNo)
    {
        String meal = getMeal(requestCode);
        String date = AppData.getTodaysDate();

        DatabaseReference Diet_node = db.getReference().child("Data").child("Diet");
        DatabaseReference Date_node = Diet_node
                .child(UID)
                .child(date);

        mealNameLists.get(meal).add(thisFoodName);
        mealNdbnoLists.get(meal).add(String.valueOf(thisFoodNbdNo));

        String bnames = mealNameLists.get(meal).toString();
        String bndbs = mealNdbnoLists.get(meal).toString();

        Date_node.child(meal).child("Food Names").setValue(bnames);
        Date_node.child(meal).child("Food NDBs").setValue(bndbs);
    }

    private String getMeal(int requestCode)
    {
        switch(requestCode)
        {
            case 1111: // Breakfast
                return "Breakfast";

            case 2222: // Lunch
                return "Lunch";

            case 3333: // Dinner
                return "Dinner";

            case 4444: // Snacks
                return "Snacks";

                    // Incorrect requestCode
            default:
                return "Breakfast";
        }
    }

}

