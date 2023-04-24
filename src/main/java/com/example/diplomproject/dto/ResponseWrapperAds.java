package com.example.diplomproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Data
//@Getter
//@Setter
public class ResponseWrapperAds {
    @MyAnnotation(name = "общее количество объявлений")
    private Integer count;
    private List<AdsDTO> result;
    public static  ResponseWrapperAds fromAdsDTO(List<AdsDTO> results) {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        if (results == null) {
            return responseWrapperAds;
        }
        responseWrapperAds.setResult(results);
        responseWrapperAds.setCount(results.size());
        return responseWrapperAds;
    }
}
