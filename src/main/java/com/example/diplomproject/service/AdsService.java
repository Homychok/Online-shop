package com.example.diplomproject.service;

import com.example.diplomproject.dto.AdsDTO;
import com.example.diplomproject.dto.CreateAds;
import com.example.diplomproject.dto.FullAds;
import com.example.diplomproject.dto.ResponseWrapperAds;
import com.example.diplomproject.exception.AdsNotFoundException;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.Image;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.ImageRepository;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdsService {
    private final UserRepository userRepository;
    private final AdsRepository adsRepository;
    private final ImageRepository imageRepository;

    public AdsService(UserRepository userRepository, AdsRepository adsRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.adsRepository = adsRepository;
        this.imageRepository = imageRepository;
    }

    public ResponseWrapperAds getAllAds() {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setResult(adsRepository.findAll()
                .stream().map(AdsDTO::fromAdsDTO)
                .collect(Collectors.toList()));
        return responseWrapperAds;
    }

    public AdsDTO addAds( CreateAds createAds, MultipartFile imageFile,Authentication authentication) throws IOException {

        Ads ads = createAds.toAds();
        ads.setAuthor(userRepository
                .findByUsernameIgnoreCase(authentication.getName()));
        ads.setImage(updateImage(createAds.toAds().getId(),imageFile));
        adsRepository.save(ads);
        return AdsDTO.fromAdsDTO(ads);
    }

    public FullAds getAdsById(Integer id) {
        return FullAds.fromFullAds(
                adsRepository.findById(id)
                        .orElseThrow(AdsNotFoundException::new));
    }
    public AdsDTO getAdsDTOById(Integer id) {
        return AdsDTO.fromAdsDTO(
                adsRepository.findById(id)
                        .orElseThrow(AdsNotFoundException::new));
    }
    public void removeAdsById(Integer id) {
        adsRepository.deleteById(id);
    }

    public AdsDTO updateAds(Integer id, CreateAds createAds) {
        Ads ads = adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        adsRepository.save(ads);
        return AdsDTO.fromAdsDTO(ads);
    }

    public Image updateImage(Integer id, MultipartFile imageFile) throws IOException {
        try {
            Image image = new Image();
            image.setData(imageFile.getBytes());
            imageRepository.save(image);
            adsRepository.updateAdsImage(id, image.getId());
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] getImage(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new).getData();
    }
    public ResponseWrapperAds getAdsAuthentication(Authentication authentication) {
            List<AdsDTO> adsDTOList = adsRepository.findAllByAuthor_Username(authentication.getName())
                    .stream().map(AdsDTO::fromAdsDTO)
                    .collect(Collectors.toList());
            return ResponseWrapperAds.fromAdsDTO(adsDTOList);
    }
    public ResponseWrapperAds getAdsByTitle(String text) {
        List<AdsDTO> adsDTOList = adsRepository.findByTitleContainingIgnoreCase(text)
                .stream().map(AdsDTO::fromAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.fromAdsDTO(adsDTOList);
    }
}


