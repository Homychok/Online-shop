package com.example.diplomproject.dto;
import com.example.diplomproject.exception.ImageNotFoundException;
import com.example.diplomproject.model.Ads;
import lombok.Data;
@Data
public class FullAds {
    @MyAnnotation(name = "id объявления")
    private Integer pk;
    @MyAnnotation(name = "фамилия автора объявления")
    private String authorLastName;
    @MyAnnotation(name = "имя автора объявления")
    private String authorFirstName;
    @MyAnnotation(name = "описание объявления")
    private String description;
    @MyAnnotation(name = "логин автора объявления")
    private String email;
    @MyAnnotation(name = "ссылка на картинку объявления")
    private String image;
    @MyAnnotation(name = "телефон автора объявления")
    private String phone;
    @MyAnnotation(name = "цена объявления")
    private int price;
    @MyAnnotation(name = "заголовок объявления")
    private String title;
    public static FullAds fromFullAds(Ads ads) {
        FullAds fullAds = new FullAds();

        fullAds.setPk(ads.getId());
        fullAds.setDescription(ads.getDescription());
        fullAds.setPrice(ads.getPrice());
        fullAds.setTitle(ads.getTitle());
        if (ads.getImage() == null) {
            fullAds.setImage("No image");
            throw new ImageNotFoundException();
        } else {
            fullAds.setImage("/ads/me/image/"
                    + ads.getImage().getId());
        }
        fullAds.setEmail(ads.getAuthor().getUsername());
        fullAds.setAuthorFirstName(ads.getAuthor().getFirstName());
        fullAds.setAuthorLastName(ads.getAuthor().getLastName());
        fullAds.setPhone(ads.getAuthor().getPhone());
        return fullAds;
    }
}
