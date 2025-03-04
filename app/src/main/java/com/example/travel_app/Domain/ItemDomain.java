package com.example.travel_app.Domain;

import java.io.Serializable;

public class ItemDomain implements Serializable {
    public ItemDomain() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTimeTour() {
        return timeTour;
    }

    public void setTimeTour(String timeTour) {
        this.timeTour = timeTour;
    }

    public String getDateTour() {
        return dateTour;
    }

    public void setDateTour(String dateTour) {
        this.dateTour = dateTour;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public String getTourGuidePhone() {
        return tourGuidePhone;
    }

    public void setTourGuidePhone(String tourGuidePhone) {
        this.tourGuidePhone = tourGuidePhone;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private String title;
    private int quantity;
    private String address;
    private String description;
    private String pic;
    private String duration;
    private String id;

    private boolean isFavorite = false;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String timeTour;
    private String dateTour;
    private String distance;
    private String tourGuideName;
    private String tourGuidePhone;
    private String tourGuidePic;
    private int price;
    private int bed;
    private int score;

}
