package com.example.meet4sho.model;

public class APIresponse {
    int pageSize;
    int pageNumber;
    int count;
    embedded _embedded = null;
    links _links = null;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(embedded _embedded) {
        this._embedded = _embedded;
    }

    public links get_links() {
        return _links;
    }

    public void set_links(links _links) {
        this._links = _links;
    }
}
