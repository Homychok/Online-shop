//package com.example.diplomproject.service.impl;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.tuple.Pair;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import ru.skypro.project.marketpkace.exception.ImageNotFoundException;
//import ru.skypro.project.marketpkace.model.Avatar;
//import ru.skypro.project.marketpkace.repository.AvatarRepository;
//import ru.skypro.project.marketpkace.service.ImageService;
//
//import java.io.IOException;
//@Slf4j
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AvatarServiceImpl implements ImageService<Avatar> {
//
//    private final AvatarRepository avatarRepository;
//
//
//    @Override
//    public void remove(Avatar avatar) {
//        log.debug("Removing avatar with id {}", avatar.getId());
//        avatarRepository.delete(avatar);
//        log.info("Avatar removed successfully");
//    }
//
//    @Override
//    public Avatar uploadImage(MultipartFile avatarFile) throws IOException {
//        log.debug("Uploading avatar file: {}", avatarFile.getOriginalFilename());
//        Avatar avatar = new Avatar();
//        avatar.setMediaType(avatarFile.getContentType());
//        avatar.setFileSize(avatarFile.getSize());
//        avatar.setData(avatarFile.getBytes());
//        Avatar savedAvatar = avatarRepository.save(avatar);
//        log.info("Avatar successfully uploaded with id {}", savedAvatar.getId());
//        return savedAvatar;
//    }
//
//    @Override
//    public Avatar getImageById(Integer id) {
//        log.debug("Getting avatar with id: {}", id);
//        return avatarRepository.findById(id).orElseThrow(ImageNotFoundException::new);
//    }
//
//    @Override
//    public Pair<String, byte[]> getImage(Integer id) {
//        Avatar avatar = getImageById(id);
//        return Pair.of(avatar.getMediaType(), avatar.getData());
//    }
//
//}