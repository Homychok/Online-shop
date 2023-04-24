package com.example.diplomproject.controller;

import com.example.diplomproject.dto.*;
import com.example.diplomproject.exception.AdsNotFoundException;
import com.example.diplomproject.exception.IncorrectArgumentException;
import com.example.diplomproject.service.AdsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService adsService;

    @Operation(
            summary = "Получить все объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapperAds.class))})
            }
    )
    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds responseWrapperAds = adsService.getAllAds();
        return ResponseEntity.ok(responseWrapperAds);
    }

    @Operation(
            summary = "Добавить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateAds.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDTO> addAds(@RequestPart("properties") CreateAds createAds,
                                         @Valid
                                         @RequestPart("image") MultipartFile imageFile,
                                         Authentication authentication) throws IOException {
        AdsDTO adsDTO = adsService.addAds(createAds, imageFile, authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adsDTO);
    }

    @Operation(
            summary = "Получить информацию об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FullAds.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getFullAds(@PathVariable("id") Integer id) {
        FullAds fullAds = adsService.getAdsById(id);
        if (fullAds == null) {
            throw new AdsNotFoundException();
        }
        return ResponseEntity.ok(fullAds);
    }

    @Operation(
            summary = "Удалить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getAdsById(#id).getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAds(@PathVariable("id") Integer id,
                                       Authentication authentication) {
        adsService.removeAdsById(id);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "Обновить информацию об объявлении",tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateAds.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getAdsById(#id).getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDTO> updateAds(@PathVariable("id") Integer id,
                                            @RequestBody CreateAds createAds) {
        AdsDTO adsDTO = adsService.updateAds(id, createAds);
        if (adsDTO == null) {
            throw new AdsNotFoundException();
        }
        return ResponseEntity.ok(adsDTO);
    }

    @Operation(
            summary = "Получить объявления авторизованного пользователя", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapperAds.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsAuthentication(Authentication authentication) {
        ResponseWrapperAds responseWrapperAds = adsService.getAdsAuthentication(authentication);
        return ResponseEntity.ok(responseWrapperAds);
    }
    @GetMapping("/source")
    @Operation(
            summary = "Поиск объявлений по названию", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapperAds.class))})
            }
    )
    public ResponseEntity<ResponseWrapperAds> getAdsByTitle(
            @RequestParam() String text) {
        if (text == null || text.isBlank()) {
            throw new IncorrectArgumentException();
        }
        return ResponseEntity.ok(adsService.getAdsByTitle(text));
    }

    @Operation(
            summary = "Обновить картинку объявления",tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getAdsById(#id).getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdsImage(@PathVariable("id") Integer id,
                                            @RequestPart("image") MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(adsService.updateImage(id, imageFile).getData());
    }

    @Operation(hidden = true)
    @GetMapping(value = "/image/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public byte[] getImage(@PathVariable("id") Integer id) {
        return adsService.getImage(id);
    }

}
