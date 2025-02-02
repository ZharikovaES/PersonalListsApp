package com.ZharikovaES.PersonalListsApp.models;

public class RequestParams {
    private String searchText;
    private Long tagId;

    private OrderingType orderingType;
    private int pageNum;
    private int pageSize;


    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public OrderingType getOrderingType() {
        return orderingType;
    }

    public void setOrderingType(OrderingType orderingType) {
        this.orderingType = orderingType;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
