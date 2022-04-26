package com.example.meet4sho.api;

public class TMEventClassification {
    private String segment;
    private String genre;
    private String subgenre;

    public TMEventClassification(String segment, String genre, String subgenre) {
        this.segment = segment;
        this.genre = genre;
        this.subgenre = subgenre;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSubgenre() {
        return subgenre;
    }

    public void setSubgenre(String subgenre) {
        this.subgenre = subgenre;
    }
}
