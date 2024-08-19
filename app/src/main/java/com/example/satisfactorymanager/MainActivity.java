package com.example.satisfactorymanager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satisfactorymanager.utils.Coal;
import com.example.satisfactorymanager.utils.Copper;
import com.example.satisfactorymanager.utils.Inventory;
import com.example.satisfactorymanager.utils.Iron;
import com.example.satisfactorymanager.utils.Limestone;
import com.example.satisfactorymanager.utils.SteelIngot;
import com.example.satisfactorymanager.utils.Water;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private Inventory inventory;
    private final String[] resourceType = {"Raw Material", "Part"};
    private final String[] rawMaterials={"Coal","Copper","Iron","Limestone","Water","Steel Ingot"};
    private final String[] parts={"Concrete","Iron Plate","Iron Rod","Screws", "Wire"};
    private final String[] chosenResourceType = new String[1];
    private final String[] chosenRawMaterial =new String[1];
    private final String[] chosenPart=new String[1];
    private final float[] productionRate=new float[1];
    private ResourceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if(readInventoryFromFile()){
            Toast.makeText(MainActivity.this,"Inventory read from file.",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(MainActivity.this,"Something went wrong while reading inventory from file.",Toast.LENGTH_SHORT).show();

        if(inventory==null)
            inventory=new Inventory();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialDividerItemDecoration dividerItemDecoration=new MaterialDividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setLastItemDecorated(false);
        dividerItemDecoration.setDividerColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_secondary30));
        dividerItemDecoration.setDividerInsetStart(20);
        dividerItemDecoration.setDividerInsetEnd(20);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ResourceAdapter(inventory);
        recyclerView.setAdapter(adapter);
        
        FloatingActionButton addFloatingActionButton=findViewById(R.id.add_fab);

        addFloatingActionButton.setOnClickListener(v -> {
            addResource();
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void addResource() {

        //AlertDialog Box to select resource type(Raw Material,Part)
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("Select Resource Type");

        builder.setItems(resourceType, (dialog, which) -> {
            chosenResourceType[0] =resourceType[which];
            dialog.dismiss();

            //next dialog box which asks for specific resource
            //for e.g, if user selects Raw Materials, then a dialog box with list of raw materials is displayed

            MaterialAlertDialogBuilder builder1=new MaterialAlertDialogBuilder(MainActivity.this);

            if(chosenResourceType[0].equals(resourceType[0])){          // if user chooses to add Raw Material
                builder1=new MaterialAlertDialogBuilder(MainActivity.this);
                builder1.setTitle("Select Raw Material");

                builder1.setItems(rawMaterials, (dialogu, whichu) -> {
                    chosenRawMaterial[0]=rawMaterials[whichu];
                    dialogu.dismiss();

                    generateRateInputAlertDialogBuilder().show();
                });
            }
            else if (chosenResourceType[0].equals(resourceType[1])) {   // if user chooses to add Part
                builder1=new MaterialAlertDialogBuilder(MainActivity.this);
                builder1.setTitle("Select Part");

                builder1.setItems(parts, (dialogu, whichu) -> {
                    chosenPart[0]=parts[whichu];
                    dialogu.dismiss();
                });
            }
            builder1.show();
        });
        builder.show();
    }

    //save inventory to file on app destroy
    @Override
    protected void onDestroy() {
        writeInventoryToFile();
        super.onDestroy();
    }

    //save inventory to file on app pause
    @Override
    protected void onPause() {
        super.onPause();
        writeInventoryToFile();
    }

    //save inventory to file on app stop
    @Override
    protected void onStop() {
        super.onStop();
        writeInventoryToFile();
    }

    //inflate the save inventory button on the ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        Objects.requireNonNull(menu.getItem(0).getIcon()).setColorFilter(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_primary60), PorterDuff.Mode.SRC_IN);

        return true;
    }

    // save inventory on button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.save_button){
            if(writeInventoryToFile()){
                Toast.makeText(MainActivity.this,"Inventory saved to file.",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MainActivity.this,"Something went wrong.",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean readInventoryFromFile(){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput("inventory.dat"));
            inventory= (Inventory) objectInputStream.readObject();
            Log.d("Nimish","Done reading file.");
            objectInputStream.close();
            return true;
        } catch (IOException | ClassNotFoundException e){
            Log.d("ReadFileError",e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeInventoryToFile(){
        try{
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(openFileOutput("inventory.dat", Context.MODE_PRIVATE));
            objectOutputStream.writeObject(inventory);
            Log.d("Nimish","Done writing file.");
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (IOException e) {
            Log.d("WriteFileError",e.toString());
            e.printStackTrace();
            return false;
        }
    }

    // helper function to create an alert dialog box which asks for input rate to user
    private MaterialAlertDialogBuilder generateRateInputAlertDialogBuilder(){
        MaterialAlertDialogBuilder rateInputDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
        rateInputDialogBuilder.setTitle("Enter Production Rate");
        View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.rate_input_layout,null);
        rateInputDialogBuilder.setView(dialogView);

        rateInputDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            Editable prod_rate=((TextInputEditText)(dialogView.findViewById(R.id.prod_rate_input_box))).getText();
            if(prod_rate!=null){
                productionRate[0]=Float.parseFloat(prod_rate.toString());
                if(chosenResourceType[0].equals(resourceType[0])){         //raw material is to be added.
                    switch (chosenRawMaterial[0]){
                        case "Coal":
                            inventory.addRawMaterial(new Coal(productionRate[0]));
                            break;
                        case "Copper":
                            inventory.addRawMaterial(new Copper(productionRate[0]));
                            break;
                        case "Iron":
                            inventory.addRawMaterial(new Iron(productionRate[0]));
                            break;
                        case "Limestone":
                            inventory.addRawMaterial(new Limestone(productionRate[0]));
                            break;
                        case "Water":
                            inventory.addRawMaterial(new Water(productionRate[0]));
                            break;
                        case "Steel Ingot":
                            inventory.addRawMaterial(new SteelIngot(productionRate[0]));
                            break;
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,"Resource Added Successfully.",Toast.LENGTH_SHORT).show();
                    writeInventoryToFile();
                }
            }

        });

        rateInputDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return rateInputDialogBuilder;
    }
}