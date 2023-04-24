package com.example.diplomproject.controller;
import com.example.diplomproject.dto.CommentDTO;
import com.example.diplomproject.dto.ResponseWrapperComment;
import com.example.diplomproject.exception.CommentNotFoundException;
import com.example.diplomproject.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Получить комментарии объявления", tags = "Комментарии")
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable("id") Integer id) {
        ResponseWrapperComment responseWrapperComment = commentService.getCommentsByAdsId(id);
        return ResponseEntity.ok(responseWrapperComment);
    }

    @Operation(
            summary = "Добавление нового комментария к объявлению", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addAdsComment(@PathVariable("id") Integer id,
                                                       @RequestBody CommentDTO commentDTO,
                                                       Authentication authentication) {
        CommentDTO comment = commentService.addComment(id, commentDTO, authentication);
        return ResponseEntity.ok(comment);
    }

    @Operation(
            summary = "Удалить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize("@commentService.getCommentById(#commentId).getAuthor().username" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteAdsComment(@PathVariable("adId") Integer adId,
                                              @PathVariable("commentId") Integer commentId) {
        commentService.deleteAdsComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize("@commentService.getCommentById(#commentId).getAuthor().username" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComments(@PathVariable("adId") Integer adId,
                                                        @PathVariable("commentId") Integer commentId,
                                                        @RequestBody CommentDTO commentDTO) {
        CommentDTO comment = commentService.updateComments(adId, commentId, commentDTO);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            throw new CommentNotFoundException();
        }
    }

}
