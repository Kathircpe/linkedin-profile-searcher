package com.finder.core.dto;
import java.util.List;

public class SearchResponseBody {
    public String status;
    public List<SearchResponse> data;
    public SearchResponseBody(String status,List<SearchResponse> data){
        this.status=status;
        this.data=data;
    }

}
