package com.example.lesson2layoutmenuadpter.addproduct;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lesson2layoutmenuadpter.R;
import com.example.lesson2layoutmenuadpter.network.ProductEntry;
import com.google.android.material.textfield.TextInputEditText;

public class AddProductFragment extends DialogFragment implements OnClickListener {

    private ImageButton btnNewProductImage;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private Button btnOk, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        btnOk = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnNewProductImage = view.findViewById(R.id.btn_image);

        TextInputEditText newProductTitle = view.findViewById(R.id.new_product_title_edit);
        TextInputEditText newProductPrice = view.findViewById(R.id.new_product_price_edit);

        ProductEntry nProduct = new ProductEntry(newProductTitle.getText().toString(), "selectedImagePath",
                selectedImagePath, newProductPrice.getText().toString(), "");

        btnNewProductImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//                Log.d("TAG", "---------!!!!!!!!!!--------");
            }
        });

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", newProductTitle.getText().toString());
                intent.putExtra("price", newProductPrice.getText().toString());
                intent.putExtra("url", "selectedImagePath");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == getActivity().RESULT_OK){
            if (requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                btnNewProductImage.setImageURI(selectedImageUri);
                selectedImagePath = selectedImageUri.getPath();
//                Log.d("TAG", selectedImageUri.getPath());
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//

}
