package com.jorjoto.mahabharat.model;

import java.util.ArrayList;

public class ResponseModel {
    String status, message,totalItems,totalPages,currentPage,appShareCount;
    ArrayList<CategoryModel> mainData,relatedVideos;
    CategoryModel videoData,appShare;

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
}
