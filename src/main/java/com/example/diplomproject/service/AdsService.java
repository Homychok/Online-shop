package com.example.diplomproject.service;

import com.example.diplomproject.dto.AdsDTO;
import com.example.diplomproject.dto.CreateAds;
import com.example.diplomproject.dto.FullAds;
import com.example.diplomproject.dto.ResponseWrapperAds;
import com.example.diplomproject.exception.AdsNotFoundException;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.AdsMapper;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class AdsService {
    private final UserRepository userRepository;
    private final AdsRepository adsRepository;

    public AdsService(UserRepository userRepository, AdsRepository adsRepository) {
        this.userRepository = userRepository;
        this.adsRepository = adsRepository;
    }

    public ResponseWrapperAds getAllAds() {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setResult(adsRepository.findAll()
                .stream().map(AdsMapper::toDTO)
                .collect(Collectors.toList()));
        responseWrapperAds.setCount(responseWrapperAds.getResult().size());
        return responseWrapperAds;
    }

    public AdsDTO addAds(  MultipartFile imageFile, CreateAds createAds,Authentication authentication) {
ChecksMethods.checkForChangeParameter(imageFile);
        Ads ads = AdsMapper.fromCreateAds(ChecksMethods.checkForChangeParameter(createAds));
        try {
            byte[] bytes = imageFile.getBytes();
            ads.setImage(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        ads.setAuthor(user);
        return AdsMapper.toDTO(adsRepository.saveAndFlush(ads));
    }

    public FullAds getAdsById(Integer id) {
        return AdsMapper.toFullAds(adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new));
    }
    @Transactional
        public void removeAdsById(Integer id) {
        adsRepository.deleteById(id);
    }
    @Transactional
    public AdsDTO updateAds(Integer id, CreateAds createAds) {
        ChecksMethods.checkForChangeParameter(createAds);
        Ads ads = adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        adsRepository.save(ads);
        return AdsMapper.toDTO(adsRepository.save(ads));
    }
@Transactional
    public byte[] updateImage(Integer id, MultipartFile avatar) {
        ChecksMethods.checkForChangeParameter(avatar);
        Ads ads = adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new);
        try {
            byte[] bytes = avatar.getBytes();
            ads.setImage(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return adsRepository.saveAndFlush(ads).getImage();
    }
    public byte[] getImage(Integer id) {
        return adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new).getImage();
    }
    public ResponseWrapperAds getAdsAuthentication(Authentication authentication) {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        responseWrapperAds.setResult(adsRepository.findAdsByAuthor_Id(user.getId())
                .stream()
                .map(AdsMapper::toDTO)
                .collect(Collectors.toList()));
        responseWrapperAds.setCount(responseWrapperAds.getResult().size());
        return responseWrapperAds;
    }
    public ResponseWrapperAds getAdsByTitle(String title) {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setResult(adsRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(AdsMapper::toDTO)
                .collect(Collectors.toList()));
        responseWrapperAds.setCount(responseWrapperAds.getResult().size());
        return responseWrapperAds;
    }
    }


