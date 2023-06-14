package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.CommentDto;
import com.example.musicbackend.entity.Comment;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.CommentRepository;
import com.example.musicbackend.repository.SongRepository;
import com.example.musicbackend.service.CommentService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final SongRepository songRepository;

    private final UserService userService;

    @Override
    public Page<CommentDto> getAllCommentBySongId(Long songId, Integer index){
        if(songRepository.existsById(songId)){
            throw new NotFoundItemException("không tìm thấy bài hát có id là: "+songId);
        }
        Page<Comment> commentPage = commentRepository.findCommentByIdSong(songId, PageRequest.of(index,20));
        List<CommentDto> commentDtoList = commentPage.getContent()
                .stream()
                .map(this::getCommentDto)
                .collect(Collectors.toList());

        return new PageImpl<>(commentDtoList,  PageRequest.of(index, 20) , commentPage.getTotalElements());
    }

    @Override
    public CommentDto addCommentForSong(CommentDto commentDto){
        Comment comment = new Comment();
        User user = userService.getCurrentUser();
        getComment(comment, commentDto, user, false);
        comment.setIsEdited(false);
        commentRepository.save(comment);
        return getCommentDto(comment);
    }

    @Override
    public CommentDto editCommentForSong(CommentDto commentDto){
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy comment có id là: "+commentDto.getId()));
        User user = userService.getCurrentUser();
        if(comment.getIdCreateBy() != user.getId()){
            throw new BadRequestException("bạn không có quyền sửa comment này với user id là "+user.getId());
        }
        getComment(comment, commentDto, user, true);
        comment.setIsEdited(true);
        commentRepository.save(comment);
        return getCommentDto(comment);
    }

    @Override
    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy comment có id là: "+id));
        User user = userService.getCurrentUser();
        if(comment.getIdCreateBy() != user.getId()){
            throw new BadRequestException("bạn không có quyền sửa comment này với user id là "+user.getId());
        }
        DBLogicUtil.setupDelete(comment, user);
        commentRepository.save(comment);
    }

    private void getComment(Comment comment, CommentDto commentDto, User user, boolean isUpdate) {
        ConvertUtil.copyProIgNull(commentDto, comment);
        if(commentDto.getSongId() != null && !isUpdate) {
            Song song = songRepository.findById(commentDto.getSongId())
                    .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là: "+commentDto.getSongId()));
            comment.setSong(song);
        }
        if(isUpdate){
            DBLogicUtil.setupUpdate(comment, user);
        }else{
            DBLogicUtil.setupCreate(comment, user);
        }
    }

    private CommentDto getCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        ConvertUtil.copyProIgNull(comment, commentDto);
        if(comment.getSong() != null){
            commentDto.setSongId(comment.getSong().getId());
        }
        return commentDto;
    }

}
