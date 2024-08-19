/*
    ...........
    ...........
    ...........
    NOT BEING USED.
    ...........
    ...........
    ...........
 */


package com.example.satisfactorymanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.satisfactorymanager.utils.Inventory;
import com.example.satisfactorymanager.utils.Resource;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ResourceDetailsActivity extends AppCompatActivity {

    private Inventory inventory;
    private int index;
    float productionRate=0;
    private Resource resource;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inventory=(Inventory) getIntent().getSerializableExtra("inventory");
        index=getIntent().getIntExtra("index",0);
        resource=inventory.getRawMaterials().get(index);
        setContentView(R.layout.resource_details_activity);


        ImageView imageView=findViewById(R.id.image_view);
        TextView resourceTitleTextView=findViewById(R.id.resource_title);
        TextView productionRateTextView=findViewById(R.id.production_rate_text_field);
        TextView utilizationRateTextView=findViewById(R.id.utilization_rate_text_field);
        TextView utilizationRatioTextView=findViewById(R.id.utilization_ratio_text_field);
        MaterialButton addSourceMaterialButton=findViewById(R.id.add_source_button);
        MaterialButton addUsageMaterialButton=findViewById(R.id.add_usage_button);

        switch (resource.getResourceType()){
            case Coal:
                imageView.setImageResource(R.drawable.coal);
                break;
            case Iron:
                imageView.setImageResource(R.drawable.iron_ore);
                break;
            case Water:
                imageView.setImageResource(R.drawable.water);
                break;
            case Copper:
                imageView.setImageResource(R.drawable.copper_ore);
                break;
            case Limestone:
                imageView.setImageResource(R.drawable.limestone);
                break;
            case SteelIngot:
                imageView.setImageResource(R.drawable.steel_ingot);
                break;
            case Wood:
            case Leaves:
                imageView.setImageIcon(Icon.createWithFilePath(null));
                break;
        }

        resourceTitleTextView.setText(resource.getResourceType().toString());
        productionRateTextView.setText(String.format("Production Rate : %.2f",resource.getProductionRate()));
        utilizationRateTextView.setText(String.format("Utilization Rate : %.2f",resource.getUtilizationRate()));
        utilizationRatioTextView.setText(String.format("Utilization Percentage : %.2f%%",resource.getUtilizationRatio()*100));

        addSourceMaterialButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder rateInputDialogBuilder = new MaterialAlertDialogBuilder(ResourceDetailsActivity.this);
            rateInputDialogBuilder.setTitle("Enter Production Rate");
            View dialogView= LayoutInflater.from(ResourceDetailsActivity.this).inflate(R.layout.rate_input_layout,null);
            rateInputDialogBuilder.setView(dialogView);

            rateInputDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                Editable prod_rate=((TextInputEditText)(dialogView.findViewById(R.id.prod_rate_input_box))).getText();
                assert prod_rate != null;
                productionRate=Float.parseFloat(prod_rate.toString());

                inventory.addRawMaterial(new Resource(resource.getResourceType(),productionRate));

                Log.d("Nimish",String.valueOf(inventory.getRawMaterials().get(index).getProductionRate()));
                productionRateTextView.setText(String.format("Production Rate : %.2f",resource.getProductionRate()));
                utilizationRateTextView.setText(String.format("Utilization Rate : %.2f",resource.getUtilizationRate()));
                utilizationRatioTextView.setText(String.format("Utilization Percentage : %.2f%%",resource.getUtilizationRatio()*100));
                if(writeInventoryToFile())
                    Toast.makeText(ResourceDetailsActivity.this,"Inventory saved to file",Toast.LENGTH_SHORT).show();
            });

            rateInputDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
               dialog.dismiss();
            });

            rateInputDialogBuilder.show();
        });
    }

    private boolean writeInventoryToFile(){
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
}
