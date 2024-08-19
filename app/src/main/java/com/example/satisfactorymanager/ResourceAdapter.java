package com.example.satisfactorymanager;

import static com.example.satisfactorymanager.utils.PartType.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satisfactorymanager.utils.Inventory;
import com.example.satisfactorymanager.utils.Part;
import com.example.satisfactorymanager.utils.PartType;
import com.example.satisfactorymanager.utils.Resource;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>{

    private static Inventory inventory = null;
    private static final PartType[] parts={IronPlate,
            IronRod,
            Wire,
            Cable,
            Concrete,
            Screw,
            SteelBeam,
            SteelPipe,
            CopperSheet};

    ResourceAdapter(Inventory inventory){
        ResourceAdapter.inventory =inventory;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item,parent,false);
        return new ResourceViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Resource resource=inventory.getRawMaterials().get(position);
        holder.resourceName.setText(resource.getResourceType().toString());
        holder.resourceDetails.setText(String.format("Production Rate: %.2f\nUtilization Rate: %.2f",resource.getProductionRate(),resource.getUtilizationRate()));
        switch (resource.getResourceType()){
            case Coal:
                holder.resourceAvatar.setImageResource(R.drawable.coal);
                break;
            case Iron:
                holder.resourceAvatar.setImageResource(R.drawable.iron_ore);
                break;
            case Water:
                holder.resourceAvatar.setImageResource(R.drawable.water);
                break;
            case Copper:
                holder.resourceAvatar.setImageResource(R.drawable.copper_ore);
                break;
            case Limestone:
                holder.resourceAvatar.setImageResource(R.drawable.limestone);
                break;
            case SteelIngot:
                holder.resourceAvatar.setImageResource(R.drawable.steel_ingot);
                break;
            case Wood:
            case Leaves:
                holder.resourceAvatar.setImageIcon(Icon.createWithFilePath(null));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return inventory.getRawMaterials().size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder{

        public ImageView resourceAvatar;
        public TextView resourceName;
        public TextView resourceDetails;
        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceAvatar=itemView.findViewById(R.id.resourceAvatar);
            resourceName=itemView.findViewById(R.id.resourceName);
            resourceDetails=itemView.findViewById(R.id.resourceDetails);

            // add click listener to each item in the view to display a floating card which shows detailed info about the resource
            itemView.setOnClickListener(v -> {
                int indexClicked=getAdapterPosition();
                Resource resource=inventory.getRawMaterials().get(indexClicked);

                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(v.getContext());
                View view=LayoutInflater.from(v.getContext()).inflate(R.layout.resource_card,null);

                builder.setView(view);
                builder.setBackground(new ColorDrawable(Color.TRANSPARENT));

                ImageView imageView=view.findViewById(R.id.image_view);
                TextView resourceTitleTextView=view.findViewById(R.id.resource_title);
                TextView productionRateTextView=view.findViewById(R.id.production_rate_text_field);
                TextView utilizationRateTextView=view.findViewById(R.id.utilization_rate_text_field);
                TextView utilizationRatioTextView=view.findViewById(R.id.utilization_ratio_text_field);
                MaterialButton addSourceMaterialButton=view.findViewById(R.id.add_source_button);
                MaterialButton addUsageMaterialButton=view.findViewById(R.id.add_usage_button);

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

                builder.show();

                //add click listener to 'add source button'
                addSourceMaterialButton.setOnClickListener(v1 -> {
                    MaterialAlertDialogBuilder rateInputDialogBuilder = new MaterialAlertDialogBuilder(v1.getContext());
                    rateInputDialogBuilder.setTitle("Enter Production Rate");
                    View dialogView= LayoutInflater.from(v1.getContext()).inflate(R.layout.rate_input_layout,null);
                    rateInputDialogBuilder.setView(dialogView);

                    rateInputDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        Editable prod_rate=((TextInputEditText)(dialogView.findViewById(R.id.prod_rate_input_box))).getText();
                        assert prod_rate != null;
                        float productionRate=Float.parseFloat(prod_rate.toString());

                        inventory.addRawMaterial(new Resource(resource.getResourceType(),productionRate));
                        ((Activity)v.getContext()).recreate();    //refresh the recyclerView on adding a raw material
                    });
                    rateInputDialogBuilder.show();
                });

                //add click listener to 'add usage button'
                addUsageMaterialButton.setOnClickListener(v12 -> {
                    PartType[] selectedPartType=new PartType[1] ;
                    List<String> partTypeStringList = Arrays.stream(parts).map(Enum::toString).collect(Collectors.toList());
                    MaterialAlertDialogBuilder partSelectorDialogBuilder=new MaterialAlertDialogBuilder(v12.getContext());

                    partSelectorDialogBuilder.setTitle("Select Part");
                    partSelectorDialogBuilder.setItems((String[]) partTypeStringList.toArray(partTypeStringList.toArray(new String[0])), (dialog, which) -> {
                        selectedPartType[0]=parts[which];
                        MaterialAlertDialogBuilder builder1=new MaterialAlertDialogBuilder(v12.getContext());

                        LinearLayout rateInputView= (LinearLayout) LayoutInflater.from(v12.getContext()).inflate(R.layout.rate_input_layout,null);


                        ((TextInputEditText)(rateInputView.findViewById(R.id.prod_rate_input_box))).setHint("Enter Raw Material Input Rate");

                        TextInputEditText partProductionRateInputText=new TextInputEditText(v12.getContext());
                        partProductionRateInputText.setHint("Enter Production Rate");
                        rateInputView.addView(partProductionRateInputText);

                        builder1.setPositiveButton("OK", (dialogu, whichu) -> {
                            Editable rawMaterialRate = ((TextInputEditText)(rateInputView.findViewById(R.id.prod_rate_input_box))).getText();
                            Editable partProductionRate= partProductionRateInputText.getText();

                            if(rawMaterialRate!=null && partProductionRate!=null){
                                if(inventory.getRawMaterials().get(getAdapterPosition()).addUsage(new Part(Float.parseFloat(rawMaterialRate.toString()),inventory.getRawMaterials().get(getAdapterPosition()).getResourceType(),Float.parseFloat(partProductionRate.toString()),selectedPartType[0]))){
                                    Toast.makeText(v12.getContext(),"Usage added successfully",Toast.LENGTH_SHORT).show();
                                    ((Activity)v12.getContext()).recreate();     //refresh the recyclerView on adding a part
                                }
                                else {
                                    Toast.makeText(v12.getContext(),"Error while adding part",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder1.setView(rateInputView);
                        builder1.show();
                    });
                    partSelectorDialogBuilder.show();
                });
            });

        }
    }
}
