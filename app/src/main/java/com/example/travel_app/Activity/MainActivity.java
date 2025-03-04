package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler; // Import Handler
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2; // Import ViewPager2

import com.example.travel_app.Adapter.CategoryAdapter;
import com.example.travel_app.Adapter.PopularAdapter;
import com.example.travel_app.Adapter.RecommendedAdapter;
import com.example.travel_app.Adapter.SliderAdapter;
import com.example.travel_app.Domain.Category;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.Location;
import com.example.travel_app.Domain.SliderItems;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    ChipNavigationBar bottomNavBar;
    private List<ItemDomain> originalRecommendedList = new ArrayList<>();
    private List<ItemDomain> originalPopularList = new ArrayList<>();
    private List<Category> originalCategoryList = new ArrayList<>();
    private RecommendedAdapter recommendedAdapter;
    private PopularAdapter popularAdapter;
    private CategoryAdapter categoryAdapter;

    // Auto-Slider variables
    private Handler sliderHandler = new Handler(); // Handler để xử lý auto-scroll
    private Runnable sliderRunnable; // Runnable để thực hiện auto-scroll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavBar = findViewById(R.id.chipNavigationBar);

        bottomNavBar.setItemSelected(R.id.explorer, true);


        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {

                } else if (id == R.id.favorites) {

                } else if (id == R.id.cart) {
                    Intent intentToCart = new Intent(MainActivity.this, CartActivity.class);
                    intentToCart.putExtra("cart_item_list", (ArrayList<ItemDomain>) CartActivity.cartItemList); // **THÊM DÒNG NÀY: Truyền cartItemList qua Intent**
                    startActivity(intentToCart);
                } else if (id == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });

        // Thêm TextWatcher vào EditText tìm kiếm
        binding.editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().toLowerCase(Locale.getDefault()).trim(); // Lấy query và chuẩn hóa
                filterLists(query); // Gọi hàm filter với query
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        initLocation();
        initBanner();
        initCategory();
        initRecommended();
        initPopular();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoSlider(); // Bắt đầu auto-slider khi Activity resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoSlider(); // Dừng auto-slider khi Activity pause
    }

    private void startAutoSlider() {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPageSlider.getCurrentItem();
                int totalItems = binding.viewPageSlider.getAdapter().getItemCount();
                int nextItem = (currentItem + 1) % totalItems; // Chuyển sang item kế tiếp, lặp lại khi hết danh sách

                binding.viewPageSlider.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000); // Lặp lại sau 3 giây
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000); // Bắt đầu auto-slider sau 3 giây
    }

    private void stopAutoSlider() {
        if (sliderHandler != null && sliderRunnable != null) {
            sliderHandler.removeCallbacks(sliderRunnable); // Dừng auto-slider
        }
    }


    // Hàm filter các danh sách
    private void filterLists(String query) {
        Log.d("MainActivity", "filterLists: Query = '" + query + "'"); // Thêm Logcat
        filterRecommended(query);
        filterPopular(query);
        filterCategory(query);
    }

    // Hàm filter danh sách Recommended
    private void filterRecommended(String query) {
        List<ItemDomain> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalRecommendedList);
        } else {
            for (ItemDomain item : originalRecommendedList) {
                boolean matches = item.getTitle().toLowerCase(Locale.getDefault()).startsWith(query) || // **ĐẢM BẢO: startsWith**
                        item.getAddress().toLowerCase(Locale.getDefault()).startsWith(query) || // **ĐẢM BẢO: startsWith**
                        item.getDescription().toLowerCase(Locale.getDefault()).startsWith(query); // **ĐẢM BẢO: startsWith**
                Log.d("MainActivity", "filterRecommended: Checking item: " + item.getTitle() + ", matches = " + matches);
                if (matches) {
                    filteredList.add(item);
                }
            }
        }
        recommendedAdapter.updateData(filteredList);
        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
    }

    // Hàm filter danh sách Popular
    private void filterPopular(String query) {
        List<ItemDomain> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalPopularList);
        } else {
            for (ItemDomain item : originalPopularList) {
                boolean matches = item.getTitle().toLowerCase(Locale.getDefault()).startsWith(query) || // **SỬA: startsWith thay vì contains**
                        item.getAddress().toLowerCase(Locale.getDefault()).startsWith(query) || // **SỬA: startsWith thay vì contains**
                        item.getDescription().toLowerCase(Locale.getDefault()).startsWith(query); // **SỬA: startsWith thay vì contains**
                Log.d("MainActivity", "filterPopular: Checking item: " + item.getTitle() + ", matches = " + matches);
                if (matches) {
                    filteredList.add(item);
                }
            }
        }
        popularAdapter.updateData(filteredList);
        binding.recyclerViewPopular.setAdapter(popularAdapter);
    }

    // Hàm filter danh sách Category
    private void filterCategory(String query) {
        List<Category> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalCategoryList);
        } else {
            for (Category category : originalCategoryList) {
                boolean matches = category.getName().toLowerCase(Locale.getDefault()).contains(query);
                Log.d("MainActivity", "filterCategory: Checking category: " + category.getName() + ", matches = " + matches); // Thêm Logcat
                if (matches) {
                    filteredList.add(category);
                }
            }
        }
        categoryAdapter.updateData(filteredList);
        binding.recyclerViewCategory.setAdapter(categoryAdapter);
    }


    private void initPopular() {
        DatabaseReference myRef = database.getReference("Popular");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);

        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    originalPopularList.addAll(list);
                    if (!list.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewRecommended.setItemViewCacheSize(0);
                        binding.recyclerViewRecommended.setClipToPadding(false);
                        binding.recyclerViewRecommended.setClipChildren(false);
                        binding.recyclerViewRecommended.setNestedScrollingEnabled(false);
                        popularAdapter = new PopularAdapter(list);
                        binding.recyclerViewPopular.setAdapter(popularAdapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecommended() {
        DatabaseReference myRef = database.getReference("Item");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);

        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    originalRecommendedList.addAll(list); // Lưu danh sách gốc
                    if (!list.isEmpty()){
                        binding.recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewRecommended.setItemViewCacheSize(0); // **Thêm dòng này**
                        binding.recyclerViewRecommended.setClipToPadding(false); // **Thêm dòng này**
                        binding.recyclerViewRecommended.setClipChildren(false); // **Thêm dòng này**
                        binding.recyclerViewRecommended.setNestedScrollingEnabled(false);
                        recommendedAdapter = new RecommendedAdapter(list); // Khởi tạo adapter và lưu lại
                        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
                    }
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(Category.class));
                    }
                    originalCategoryList.addAll(list); // Lưu danh sách gốc
                    if (!list.isEmpty()){
                        binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewCategory.setItemViewCacheSize(0); // **Thêm dòng này**
                        binding.recyclerViewCategory.setClipToPadding(false); // **Thêm dòng này**
                        binding.recyclerViewCategory.setClipChildren(false); // **Thêm dòng này**
                        binding.recyclerViewCategory.setNestedScrollingEnabled(false);
                        categoryAdapter = new CategoryAdapter(list); // Khởi tạo adapter và lưu lại
                        binding.recyclerViewCategory.setAdapter(categoryAdapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void banners(ArrayList<SliderItems> items){
        binding.viewPageSlider.setAdapter(new SliderAdapter(items,binding.viewPageSlider));
        binding.viewPageSlider.setClipToPadding(false);
        binding.viewPageSlider.setClipChildren(false);
        binding.viewPageSlider.setOffscreenPageLimit(3);
        binding.viewPageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPageSlider.setPageTransformer(compositePageTransformer);
    }
    private void initBanner(){
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}