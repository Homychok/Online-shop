package com.example.diplomproject.controller;

import com.example.diplomproject.dto.*;
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
                                    schema = @Schema(implementation = AdsDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDTO> addAds(@RequestPart("image") MultipartFile imageFile,
                                         @Valid
                                         @RequestPart("properties") CreateAds createAds,
                                         Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adsService.addAds(imageFile, createAds, authentication));
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
    public ResponseEntity<?> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getAdsById(id));
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
                                    schema = @Schema(implementation = AdsDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getAdsById(#id).getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id,
                                       @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(adsService.updateAds(id,createAds));
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
    public ResponseEntity<?> getAdsAuthentication(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAdsAuthentication(authentication));
    }

    @Operation(hidden = true)

    @GetMapping("/search")
    public ResponseEntity<?> getAdsByTitle(
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(adsService.getAdsByTitle(title));
    }

    @Operation(
            summary = "Обновить картинку объявления",tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize("@adsService.getAdsById(#id).getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdsImage(@PathVariable Integer id,
                                            @RequestPart("image") MultipartFile imageFile) throws IOException {
        adsService.updateImage(id, imageFile);
        return ResponseEntity.ok().build();
    }

    @Operation(hidden = true)
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("id") Integer id) {
        return adsService.getImage(id);
    }

}
