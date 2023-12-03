package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeItem {


    @SerializedName("Story")
    public List<StoryItem> storyItemList;

    @SerializedName("Festival")
    public List<FestivalItem> festivalItemList;

    @SerializedName("Feature")
    public List<FeatureItem> featureItemList;

    @SerializedName("AnimatedCategory")
    public List<CategoryItem> animatedCategoryList;

    @SerializedName("BusinessCategory")
    public List<BusinessCategoryItem> businessCategoryList;

    @SerializedName("Category")
    public List<CategoryItem> categoryList;

    public HomeItem() {
    }



}
