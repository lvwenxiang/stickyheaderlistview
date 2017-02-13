package com.example.bean;


//mainActivity ��ҳչʾ����
public class TravelingEntity {
	private String type; // �羰�����ֲ�����
    private String title; // ����
    private String from; // ��Դ
    private int rank; // ����
    private String image_url; // ͼƬ��ַ

    // ������������
    private boolean isNoData = false;
    private int height;

    public TravelingEntity() {
    }

    public TravelingEntity(String type, String title, String from, int rank, String image_url) {
        this.type = type;
        this.title = title;
        this.from = from;
        this.rank = rank;
        this.image_url = image_url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isNoData() {
        return isNoData;
    }

    public void setNoData(boolean noData) {
        isNoData = noData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
