package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityDeltaiBinding;

public class DeltaiActivity extends BaseActivity {
    ActivityDeltaiBinding binding;
    private ItemDomain object;
    private int defaultFavIconColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeltaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVisible();
        initListeners();

    }

    private void setVisible() {
        double price = object.getPrice();
        binding.titleTxt.setText(object.getTitle());
        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormatVN.format(price);
        formattedPrice = formattedPrice.replace("₫", "").trim();
        binding.priceTxt.setText(formattedPrice + "₫");
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.bedTxt.setText(""+object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore()+" Rating");
        binding.ratingBar2.setRating((float)object.getScore());

        Glide.with(DeltaiActivity.this).load(object.getPic()).into(binding.pic);

        defaultFavIconColor = binding.imageView8.getColorFilter() != null ?
                ContextCompat.getColor(this, R.color.gray) : // Nếu có color filter, giả định màu xám
                0; // Nếu không có color filter, mặc định là 0 (không màu)

        // **Cập nhật màu icon yêu thích ban đầu dựa trên trạng thái isFavorite**
        updateFavIconColor();

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                CartActivity.addItemToCart(object);
                Intent intent = new Intent(DeltaiActivity.this, OrderActivity.class);
                intent.putExtra("object", object);
                startActivity(intent);
            }
        });
    }
    private void initListeners() {
        binding.imageView8.setOnClickListener(new View.OnClickListener() { // Set OnClickListener cho imageView8
            @Override
            public void onClick(View view) {
                object.setFavorite(!object.isFavorite()); // Đảo ngược trạng thái yêu thích
                updateFavIconColor(); // Cập nhật màu icon
                if (object.isFavorite()){
                    Toast.makeText(DeltaiActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    FavoriteActivity.addToFavorites(object);
                }else {
                    Toast.makeText(DeltaiActivity.this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    FavoriteActivity.removeFromFavorites(object);
                }
            }
        });
    }

    private void updateFavIconColor() {
        if (object.isFavorite()) {
            binding.imageView8.setColorFilter(ContextCompat.getColor(this, R.color.red)); // Set màu đỏ khi yêu thích
        } else {
            binding.imageView8.setColorFilter(defaultFavIconColor);
//            binding.imageView8.clearColorFilter(); // Xóa color filter để trở về màu gốc
//            // Hoặc set lại màu gốc nếu bạn biết trước màu gốc:
//            // binding.imageView8.setColorFilter(defaultFavIconColor);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(object != null){ // add this check
            updateFavIconColor(); //update the icon when return this page

        }
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }
}
