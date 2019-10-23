package com.example.baseapplication.mvp.model.entity;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-21 19:07
 */
public class PostBean {
    private int visitors;
    private String carPrice;
    private int comments;
    private String carName;
    private String carIcon;
    private String icon;
    private String title;
    private String uuid;
    private int praises;
    private int carId;
    private String userName;
    private String createTime;
    private String headImg;
    private String batteryLife;
    private String content;
    private List<String> media;
    private ShareMapBean shareMap;

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public ShareMapBean getShareMap() {
        return shareMap;
    }

    public void setShareMap(ShareMapBean shareMap) {
        this.shareMap = shareMap;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPraises() {
        return praises;
    }

    public void setPraises(int praises) {
        this.praises = praises;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getContent() {
        return content;
    }
}
