package com.example.baseapplication.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-21 18:59
 */
public class BannerBean {
    private String img;
    private int display;
    private String destination;
    private String title;
    private int point;
    private String backup;

    public BannerBean(String img, int display, String destination, String title) {
        this.img = img;
        this.display = display;
        this.destination = destination;
        this.title = title;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "img='" + img + '\'' +
                ", display=" + display +
                ", destination='" + destination + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
