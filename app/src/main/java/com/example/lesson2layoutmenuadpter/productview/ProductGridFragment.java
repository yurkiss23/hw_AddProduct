package com.example.lesson2layoutmenuadpter.productview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.lesson2layoutmenuadpter.NavigationHost;
import com.example.lesson2layoutmenuadpter.R;
import com.example.lesson2layoutmenuadpter.addproduct.AddProductFragment;
import com.example.lesson2layoutmenuadpter.network.ProductEntry;
import com.example.lesson2layoutmenuadpter.productview.network.ProductDTO;
import com.example.lesson2layoutmenuadpter.productview.network.ProductDTOService;
import com.example.lesson2layoutmenuadpter.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductGridFragment extends Fragment {

    private static final String TAG = ProductGridFragment.class.getSimpleName();
    private Button btnAdd, btnRemove;
    private RecyclerView recyclerView;
    private ProductEntry nProduct = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_grid, container, false);

        btnAdd = view.findViewById(R.id.btn_add);

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

//        List<ProductEntry> list = ProductEntry.initProductEntryList(getResources());
//        ProductCardRecyclerViewAdapter adapter = new ProductCardRecyclerViewAdapter(list);
//
//        recyclerView.setAdapter(adapter);
//        Log.d(TAG, "----------!!!!!!!!!!!----------");
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        CommonUtils.showLoading(getActivity());
        ProductDTOService.getInstance()
                .getJSONApi()
                .getAllProducts()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                        if(response.isSuccessful()) {
                            Log.d(TAG, "----------Response good----------");
                            List<ProductDTO> list = response.body();
                            List<ProductEntry> newlist = new ArrayList<ProductEntry>();//ProductEntry.initProductEntryList(getResources());
                            for (ProductDTO item : list) {
                                ProductEntry pe = new ProductEntry(item.getTitle(), item.getUrl(), item.getUrl(), item.getPrice(), "sdfasd");
                                newlist.add(pe);
                            }
                            if (nProduct != null){
                                newlist.add(nProduct);
                            }
                            ProductCardRecyclerViewAdapter newAdapter = new ProductCardRecyclerViewAdapter(newlist);
                            recyclerView.swapAdapter(newAdapter, false);
                        }
                        else {
                          //  Toast.makeText(getContext(), "Проблема при отримані даних",Toast.LENGTH_LONG).show();
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                        Log.e(TAG, "----------Bad Request----------");
                        CommonUtils.hideLoading();
                    }
                });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFragment fragment = new AddProductFragment();
                fragment.setTargetFragment(ProductGridFragment.this, 120);
                ((NavigationHost)getActivity()).navigateTo(fragment, true);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120){
            if (resultCode == Activity.RESULT_OK){
                String nTitle = data.getStringExtra("title");
                String nPrice = data.getStringExtra("price");
                String nUrl = data.getStringExtra("url");
                nProduct = new ProductEntry(nTitle, nUrl, nUrl, nPrice, "Description");
            }else {

            }
        }
    }
}
