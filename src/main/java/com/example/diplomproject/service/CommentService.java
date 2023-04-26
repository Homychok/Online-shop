package com.example.diplomproject.service;

import com.example.diplomproject.dto.CommentDTO;
import com.example.diplomproject.dto.ResponseWrapperComment;
import com.example.diplomproject.exception.CommentNotFoundException;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.CommentMapper;
import com.example.diplomproject.model.Comment;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.CommentRepository;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
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

    public CommentDTO addComment(Integer id, CommentDTO commentDTO, Authentication authentication){
        Comment comment = CommentMapper.fromDTO(ChecksMethods.checkForChangeParameter(commentDTO));
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName()).orElseThrow(UserNotFoundException::new);
        comment.setAuthor(user);
        comment.setAds(adsRepository.findById(id).orElseThrow(CommentNotFoundException::new));
        commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    public void deleteAdsComment(Integer commentId, Integer adId){
            commentRepository.deleteByIdAndAdsId(ChecksMethods.checkForChangeParameter(commentId),adId);
        }

    public CommentDTO updateComments(Integer commentId, Integer adId, CommentDTO commentDTO){
        Comment comment = commentRepository.findByIdAndAdsId(commentId, adId)
                .orElseThrow(CommentNotFoundException::new);
        comment.setText(commentDTO.getText());
        return CommentMapper.toDTO(commentRepository.save(comment));
    }
    public ResponseWrapperComment getCommentsByAdsId(Integer adsId) {
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setResult(commentRepository.findAllByAdsId(adsId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList()));
        responseWrapperComment.setCount(responseWrapperComment.getResult().size());
        return responseWrapperComment;
        }
    public Comment getCommentById(Integer id){
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

}
