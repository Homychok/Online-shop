package com.example.diplomproject.dto;
import com.example.diplomproject.annotations.MyAnnotation;
import com.example.diplomproject.exception.ImageNotFoundException;
import com.example.diplomproject.model.Ads;
import lombok.Data;
@Data
public class AdsDTO {
    @MyAnnotation(name = "id автора объявления")
    private Integer author;
@MyAnnotation(name = "ссылка на картинку объявления")
private String image;
@MyAnnotation(name = "id объявления")
private Integer pk;
    @MyAnnotation(name = "цена объявления")
    private int price;
    @MyAnnotation(name = "заголовок объявления")
    private String title;
    @MyAnnotation(name = "описание объявления")
    private String description;
    public static AdsDTO fromAdsDTO(Ads ads) {
        AdsDTO adsDTO = new AdsDTO();

        adsDTO.setAuthor(ads.getAuthor().getId());
        adsDTO.setPk(ads.getId());
        adsDTO.setPrice(ads.getPrice());
        adsDTO.setTitle(ads.getTitle());
        if (ads.getImage() == null) {
            adsDTO.setImage("No image");
            throw new ImageNotFoundException();
        } else {
            adsDTO.setImage("/ads/me/image/"
                    + ads.getImage().getId());
        }
        return adsDTO;
    }
}
