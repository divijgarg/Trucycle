package com.example.recycleapplicationv3;

public class MainModel {
    String tutPic;
    String tutName;
    String url;

    public MainModel(String tutPic, String tutName, String url){
        this.tutName = tutName;
        this.tutPic = tutPic;
        this.url = url;
    }

    public String getTutPic() {
        return tutPic;
    }

    public String getTutName() {
        return tutName;
    }

    public String getUrl() {
        return url;
    }
}
