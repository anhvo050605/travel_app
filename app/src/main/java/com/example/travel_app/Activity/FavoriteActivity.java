package com.example.travel_app.Activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FavoriteAdapter;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseActivity {

    ActivityFavoriteBinding binding;
    private RecyclerView.Adapter favoriteAdapter; // Adapter for the RecyclerView
    private RecyclerView recyclerViewFavorites;
    private static List<ItemDomain> favoriteItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Make sure you set the content view!

        initView();
        initList();
    }


    private void initView() {
        recyclerViewFavorites = binding.favoriteItemsRecyclerView;
    }
    private void initList() {
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        favoriteAdapter = new FavoriteAdapter(favoriteItems);  // Create your FavoriteAdapter
        recyclerViewFavorites.setAdapter(favoriteAdapter);
    }

    // Method to add an item to the favorites list
    public static void addToFavorites(ItemDomain item) {
        if (favoriteItems == null) {
            favoriteItems = new ArrayList<>(); // Initialize if null
        }
        if (!favoriteItems.contains(item)) { // Prevent duplicates (check by some unique ID if possible)
            favoriteItems.add(item);
        }
    }

    // Method to remove an item from the favorites list
    public static void removeFromFavorites(ItemDomain item) {
        if (favoriteItems != null) {
            favoriteItems.remove(item); // Remove by object reference.
        }
    }
    // to check item is favorite or not
    public  static  boolean isFavorite(ItemDomain item){
        if (favoriteItems != null){
            for (ItemDomain favorite: favoriteItems){
                if (favorite.getId().equals(item.getId())){
                    return true;
                }
            }
        }
        return false;
    }
    public static void updateFavoriteStatus(ItemDomain item) {
        if (favoriteItems != null) {
            for (int i = 0; i < favoriteItems.size(); i++) {
                if (favoriteItems.get(i).getId().equals(item.getId())) { // Use getId() for comparison
                    favoriteItems.set(i, item); // Update the item in the list
                    break; // Exit loop once item is found and updated
                }
            }
        }
    }



    // Make sure your adapter can refresh the data.
    @Override
    protected void onResume() {
        super.onResume();
        if (favoriteAdapter != null) {
            favoriteAdapter.notifyDataSetChanged(); // Refresh the RecyclerView when activity resumes
        }
    }
}