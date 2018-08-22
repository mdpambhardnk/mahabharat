package com.jorjoto.mahabharat.model;

import java.util.ArrayList;

public class ResponseModel {
    String status, message,totalItems,totalPages,currentPage,appShareCount,showAdd;
    ArrayList<CategoryModel> mainData,relatedVideos,SuggestedApps,view_ad,adsData;
    CategoryModel videoData,appShare;

    public ArrayList<CategoryModel> getSuggestedApps() {
        return SuggestedApps;
    }

    public void setSuggestedApps(ArrayList<CategoryModel> suggestedApps) {
        SuggestedApps = suggestedApps;
    }

    public String getAppShareCount() {
        return appShareCount;
    }

    public void setAppShareCount(String appShareCount) {
        this.appShareCount = appShareCount;
    }

    public CategoryModel getAppShare() {
        return appShare;
    }

    public void setAppShare(CategoryModel appShare) {
        this.appShare = appShare;
    }

    public ArrayList<CategoryModel> getRelatedVideos() {
        return relatedVideos;
    }

    public void setRelatedVideos(ArrayList<CategoryModel> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }

    public CategoryModel getVideoData() {
        return videoData;
    }

    public void setVideoData(CategoryModel videoData) {
        this.videoData = videoData;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<CategoryModel> getMainData() {
        return mainData;
    }

    public void setMainData(ArrayList<CategoryModel> mainData) {
        this.mainData = mainData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShowAdd() {
        return showAdd;
    }

    public void setShowAdd(String showAdd) {
        this.showAdd = showAdd;
    }

    public ArrayList<CategoryModel> getView_ad() {
        return view_ad;
    }

    public void setView_ad(ArrayList<CategoryModel> view_ad) {
        this.view_ad = view_ad;
    }

    public ArrayList<CategoryModel> getAdsData() {
        return adsData;
    }

    public void setAdsData(ArrayList<CategoryModel> adsData) {
        this.adsData = adsData;
    }
}
