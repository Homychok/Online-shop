package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
import java.util.List;
@Data
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
