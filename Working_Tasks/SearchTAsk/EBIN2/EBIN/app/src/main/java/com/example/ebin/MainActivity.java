package com.example.ebin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextWatcher{
   List<DataModel> dataModels;
   List<DataModel> dataModels1;
   ListView listView;
   CustomAdapter adapter;
EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView=findViewById(R.id.inputSearch);
        listView=  findViewById(R.id.recycler);

        dataModels= new ArrayList<>();
        dataModels1= new ArrayList<>();
        adapter= new CustomAdapter(dataModels,getApplicationContext(),R.layout.list_data, dataModels1);

        listView.setAdapter(adapter);
        searchView.addTextChangedListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layout_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add:
                AlertDialog.Builder builder
                        = new AlertDialog.Builder(this);
                builder.setTitle("Name");

                // set the custom layout
                final View customLayout
                        = getLayoutInflater()
                        .inflate(
                                R.layout.custom,
                                null);
                builder.setView(customLayout);

                // add a button
                builder
                        .setPositiveButton(
                                "Save",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which)
                                    {

                                        // send data from the
                                        // AlertDialog to the Activity
                                        EditText editText
                                                = customLayout.findViewById(R.id.et1);


                                        EditText editText2
                                                = customLayout
                                                .findViewById(
                                                        R.id.et2);
                                        String name=editText
                                                .getText()
                                                .toString();
                                        String dob= editText2
                                                .getText()
                                                .toString();
                                        sendDialogDataToActivity(name,dob);
                                    }
                                });

                // create and show
                // the alert dialog
                AlertDialog dialog
                        = builder.create();
                dialog.show();}
        return true;

    }


    private void sendDialogDataToActivity(String toString, String toString1) {
       DataModel dataModel=new DataModel(toString,toString1);
       dataModels.add(dataModel);
       dataModels1.add(dataModel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       adapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

