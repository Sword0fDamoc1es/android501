package com.example.fandango0.model;


public class suggestions {
    String title = "";
    String type = "";
    links _links = null;

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

    public links get_links() {
        return _links;
    }

    public void set_links(links _links) {
        this._links = _links;
    }
}
