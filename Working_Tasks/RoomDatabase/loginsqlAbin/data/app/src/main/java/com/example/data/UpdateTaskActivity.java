package com.example.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editTextPh,address,email;
    private TextView editTextName;
    private RadioGroup radioGroup;
    TextView data;
    RadioButton radioButtonM;
    RadioButton radioButtonF;
    RadioButton radioButton;
    RadioButton radioButtonO;
    private RecyclerView recyclerView;
    private Spinner country_Spinner;
    private Spinner state_Spinner;
    private Spinner city_Spinner;

    private ArrayAdapter<Country> countryArrayAdapter;
    private ArrayAdapter<State> stateArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<Country> countries;
    private ArrayList<State> states;
    private ArrayList<City> cities;
    String sCountary="";
    String sState="";
    String sDistrict="";
    private CheckBox checkBox;
    String check="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task2);
        country_Spinner = (Spinner) findViewById(R.id.Country_spinner);
        state_Spinner = (Spinner) findViewById(R.id.state_Spinner);
        city_Spinner = (Spinner) findViewById(R.id.DistrictSpinner);
        checkBox=findViewById(R.id.checkbox);

        countries = new ArrayList<>();
        states = new ArrayList<>();
        cities = new ArrayList<>();


        editTextName = findViewById(R.id.editTextName);
        editTextPh = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.Address);
        email = findViewById(R.id.email);
        radioGroup=findViewById(R.id.radioGrp);
         radioButtonM = findViewById(R.id.radioM);
       radioButtonF = findViewById(R.id.radioF);
         radioButtonO = findViewById(R.id.radioO);
        data=findViewById(R.id.data);


        initializeUI();







        final Task task = (Task) getIntent().getSerializableExtra("task");


        try {
            loadTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }


        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(task);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(task);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }




    private void loadTask(Task task) {

        editTextName.setText(task.getPersonName());
        editTextPh.setText(task.getPhoneNumber());
        address.setText(task.getAddress());
        email.setText(task.getEmail());
        data.setText(task.getGender());
       if(task.getGender().equals("")){
           radioButtonM.setChecked(false);
           radioButtonF.setChecked(false);
           radioButtonO.setChecked(false);}
        if(task.getGender().equals("Male")){
           radioButtonM.setChecked(true);
       } if(task.getGender().equals("Female")){
           radioButtonF.setChecked(true);}
        if(task.getGender().equals("Other")){
            radioButtonO.setChecked((true));
        }
        if(!task.getCountry().equals("")){
            countryArrayAdapter = new ArrayAdapter<Country>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countries);
            countryArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            country_Spinner.setAdapter(countryArrayAdapter);
            country_Spinner.setOnItemSelectedListener(country_listener);
           int spinnerPosition = 2;
//           String  S=task.getCountry();
//           Country country=new Country(S);
//           spinnerPosition = countryArrayAdapter.getPosition();
            country_Spinner.setSelection(spinnerPosition);
        }
        if (!task.getState().equals("")){
            stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, states);
            stateArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            state_Spinner.setAdapter(stateArrayAdapter);
            state_Spinner.setOnItemSelectedListener(state_listener);
            int spinnerPosition = 2;
//           String  S=task.getCountry();
//           Country country=new Country(S);
//           spinnerPosition = countryArrayAdapter.getPosition();
            state_Spinner.setSelection(spinnerPosition);
        }
        if(!task.getDistrict().equals("")){
            cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cities);
            cityArrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            city_Spinner.setAdapter(cityArrayAdapter);
            city_Spinner.setOnItemSelectedListener(city_listener);
            int spinnerPosition = 2;
//           String  S=task.getCountry();
//           Country country=new Country(S);
//           spinnerPosition = countryArrayAdapter.getPosition();
            city_Spinner.setSelection(spinnerPosition);
        }



    }

    private void updateTask(final Task task) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
         radioButton = (RadioButton) findViewById(selectedId);
         if(checkBox.isChecked()){
             check="yes";
         }
        final String sTask = editTextName.getText().toString().trim();
        final String sDesc = editTextPh.getText().toString().trim();
        final String sAddress = address.getText().toString().trim();
        final String sEmail = email.getText().toString().trim();
       final String sGender= radioButton.getText().toString();
       final String sCountarys=sCountary;
       final String sStates=sState;
       final  String sDistricts=sDistrict;
       final String sCheck=check;


        if (sTask.isEmpty() && sDesc.isEmpty()  ) {
            Toast.makeText(getApplicationContext(), "Insert Proper details", Toast.LENGTH_SHORT).show();
        }


        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Overrider
            protected Void doInBackground(Void... voids) {
                task.setPersonName(sTask);
                task.setPhoneNumber(sDesc);
                task.setAddress(sAddress);
                task.setGender(sGender);
                task.setEmail(sEmail);
                task.setCountry(sCountarys);
                task.setState(sStates);
                task.setDistrict(sDistricts);


                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
    private void initializeUI() {


        createLists();

        countryArrayAdapter = new ArrayAdapter<Country>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countries);
        countryArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        country_Spinner.setAdapter(countryArrayAdapter);

        stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, states);
        stateArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        state_Spinner.setAdapter(stateArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cities);
        cityArrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        city_Spinner.setAdapter(cityArrayAdapter);

        country_Spinner.setOnItemSelectedListener(country_listener);
        state_Spinner.setOnItemSelectedListener(state_listener);
        city_Spinner.setOnItemSelectedListener(city_listener);

    }

     AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Country country = (Country) country_Spinner.getItemAtPosition(position);
                sCountary=country.toString();
                Log.d("SpinnerCountry", "onItemSelected: country: "+country.getCountryID());
                ArrayList<State> tempStates = new ArrayList<>();

                tempStates.add(new State(0, new Country(0, "Choose a Country"), "Choose a State"));

                for (State singleState : states) {
                    if (singleState.getCountry().getCountryID() == country.getCountryID()) {
                        tempStates.add(singleState);
                    }
                }

                stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, tempStates);
                stateArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                state_Spinner.setAdapter(stateArrayAdapter);
            }

            cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new ArrayList<City>());
            cityArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            city_Spinner.setAdapter(cityArrayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

     AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final State state = (State) state_Spinner.getItemAtPosition(position);
                sState=state.toString();

                Log.d("SpinnerCountry", "onItemSelected: state: "+state.getStateID());
                ArrayList<City> tempCities = new ArrayList<>();

                Country country = new Country(0, "Choose a Country");
                State firstState = new State(0, country, "Choose a State");
                tempCities.add(new City(0, country, firstState, "Choose a City"));

                for (City singleCity : cities) {
                    if (singleCity.getState().getStateID() == state.getStateID()) {
                        tempCities.add(singleCity);
                    }
                }

                cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tempCities);
                cityArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                city_Spinner.setAdapter(cityArrayAdapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

     AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final City city= (City) city_Spinner.getItemAtPosition(position);
            sDistrict=city.toString();


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void createLists() {
        Country country0 = new Country(0, "Choose a Country");
        Country country1 = new Country(1, "INDIA");
        Country country2 = new Country(2, "USA");

        countries.add(new Country(0, "Choose a Country"));
        countries.add(new Country(1, "INDIA"));
        countries.add(new Country(2, "USA"));

        State state0 = new State(0, country0, "Choose a Country");
        State state1 = new State(1, country1, "KERALA");
        State state2 = new State(2, country1, "TAMIL");
        State state3 = new State(3, country2, "CANNADA");
        State state4 = new State(4, country2, "BRACO");

        states.add(state0);
        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);

        cities.add(new City(0, country0, state0, "Choose a City"));
        cities.add(new City(1, country1, state1, "KOLlAM"));
        cities.add(new City(2, country1, state1, "ALAPPUZHA"));
        cities.add(new City(3, country1, state2, "ERAnakulam"));
        cities.add(new City(4, country2, state2, "THRISSUR"));
        cities.add(new City(5, country2, state3, "NAYAGRA"));
        cities.add(new City(6, country2, state3, "MONOSCO"));
        cities.add(new City(7, country2, state4, "PARADIZE"));
        cities.add(new City(8, country1, state4, "DAERLINO"));
    }

    private class Country implements Comparable<Country> {

        private int countryID;
        private String countryName;



        public Country(int countryID, String countryName) {
            this.countryID = countryID;
            this.countryName = countryName;
        }

        public Country(String s) {
        }


        public int getCountryID() {
            return countryID;
        }

        public String getCountryName() {
            return countryName;
        }

        @Override
        public String toString() {
            return countryName;
        }


        @Override
        public int compareTo(Country another) {
            return this.getCountryID() - another.getCountryID();
        }
    }

    private class State implements Comparable<State> {

        private int stateID;
        private Country country;
        private String stateName;

        public State(int stateID, Country country, String stateName) {
            this.stateID = stateID;
            this.country = country;
            this.stateName = stateName;
        }

        public int getStateID() {
            return stateID;
        }

        public Country getCountry() {
            return country;
        }

        public String getStateName() {
            return stateName;
        }

        @Override
        public String toString() {
            return stateName;
        }

        @Override
        public int compareTo(State another) {
            return this.getStateID() - another.getStateID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
        }
    }

    private class City implements Comparable<City> {

        private int cityID;
        private Country country;
        private State state;
        private String cityName;

        public City(int cityID, Country country, State state, String cityName) {
            this.cityID = cityID;
            this.country = country;
            this.state = state;
            this.cityName = cityName;
        }

        public int getCityID() {
            return cityID;
        }

        public Country getCountry() {
            return country;
        }

        public State getState() {
            return state;
        }

        public String getCityName() {
            return cityName;
        }

        @Override
        public String toString() {
            return cityName;
        }

        @Override
        public int compareTo(City another) {
            return this.cityID - another.getCityID();//ascending order
//            return another.getCityID() - this.cityID;//descending order
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


