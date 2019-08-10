package com.lfq.picacg.data;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {

    public List<SearchBean> title;
    public List<SearchBean> keyword;
    public List<SearchBean> user;

    @Data
    public static class SearchBean {
        public int id;
        public String value;
        public String icon;
    }
}
