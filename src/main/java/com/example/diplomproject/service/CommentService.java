package com.example.diplomproject.service;

import com.example.diplomproject.dto.CommentDTO;
import com.example.diplomproject.dto.ResponseWrapperComment;
import com.example.diplomproject.exception.AdsNotFoundException;
import com.example.diplomproject.exception.CommentNotFoundException;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.Comment;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.CommentRepository;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, AdsRepository adsRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    public CommentDTO addComment(Integer adId, CommentDTO commentDTO, Authentication authentication){
        Ads ads = adsRepository.findById(adId)
                .orElseThrow(AdsNotFoundException::new);
        Comment comment = commentDTO.toComment();
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(userRepository.findByUsernameIgnoreCase(
                authentication.getName()));
        commentRepository.save(comment);
        return CommentDTO.fromCommentDTO(comment);

    }

    public void deleteAdsComment(Integer adId, Integer commentId){
            Comment comment = commentRepository.findByIdAndAdsId(commentId, adId).orElseThrow(CommentNotFoundException::new);
            commentRepository.delete(comment);
        }

    public CommentDTO updateComments(Integer adId, Integer commentId, CommentDTO commentDTO){
        Comment comment = commentRepository.findByIdAndAdsId(commentId, adId)
                .orElseThrow(CommentNotFoundException::new);
        comment.setText(commentDTO.getText());
        commentDTO = CommentDTO.fromCommentDTO(comment);
        commentRepository.save(comment);
        return commentDTO;
    }
    public ResponseWrapperComment getCommentsByAdsId(Integer id) {
            List<CommentDTO> commentDTOList = commentRepository.findAllByAdsId(id)
                    .stream().map(CommentDTO::fromCommentDTO)
                    .collect(Collectors.toList());
            return ResponseWrapperComment.fromCommentDTO(commentDTOList);
        }
    public Comment getAdsComment(Integer commentId, Integer adId) {
        return commentRepository.findByIdAndAdsId(commentId, adId).orElseThrow(CommentNotFoundException::new);
    }
    public Comment getCommentById(Integer id){
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

}
