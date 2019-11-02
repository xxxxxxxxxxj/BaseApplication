package com.example.baseapplication.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:59
 */
public class Encyclopedias {public String source;
    private String sourceIcon;
    private String title;
    private String listCover;
    private int id;
    private int realReadNum;
    private int listCoverWeight;
    private int listCoverHeight;
    private boolean isVideo;

    public Encyclopedias() {
    }

    public Encyclopedias(String source, String sourceIcon, String title, String listCover, int id, int realReadNum, int listCoverWeight, int listCoverHeight, boolean isVideo) {
        this.source = source;
        this.sourceIcon = sourceIcon;
        this.title = title;
        this.listCover = listCover;
        this.id = id;
        this.realReadNum = realReadNum;
        this.listCoverWeight = listCoverWeight;
        this.listCoverHeight = listCoverHeight;
        this.isVideo = isVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getListCover() {
        return listCover;
    }

    public void setListCover(String listCover) {
        this.listCover = listCover;
    }

    public int getListCoverWeight() {
        return listCoverWeight;
    }

    public void setListCoverWeight(int listCoverWeight) {
        this.listCoverWeight = listCoverWeight;
    }

    public int getListCoverHeight() {
        return listCoverHeight;
    }

    public void setListCoverHeight(int listCoverHeight) {
        this.listCoverHeight = listCoverHeight;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public int getRealReadNum() {
        return realReadNum;
    }

    public void setRealReadNum(int realReadNum) {
        this.realReadNum = realReadNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }
}
