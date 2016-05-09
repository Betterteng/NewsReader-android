package com.example.tengshinan.newsreader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TengShinan on 22/04/2016.
 */
public class News {
    private String title;
    private String desc;
    private String link;
    private String thumbnail;

    public News(String title, String desc, String link, String thumbnail) {
        this.title = title;
        this.desc = desc;
        this.link = link;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public News(JSONObject joNews) {
        thumbnail = "";
        try {
            title = joNews.getString("title");
            desc = joNews.getString("description").replaceAll("<p>", "").replaceAll("</p>","").trim();
            link = joNews.getString("link");
            thumbnail = joNews.getJSONObject("group").getJSONObject("thumbnail").getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
