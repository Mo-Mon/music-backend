package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.PlaylistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.*;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.PlaylistRepository;
import com.example.musicbackend.repository.SongRepository;
import com.example.musicbackend.service.PlaylistService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final UserService userService;

    private final SongRepository songRepository;

    public List<SongDto> getSongsInPlaylist(){
        // todo
        return null;
    }

    public PlaylistDto addSongToPlaylist(){
        // todo
        return null;
    }

    public PlaylistDto deleteSongToPlaylist(){
        // todo
        return null;
    }



    @Override
    public PlaylistDto findById(Long id){
        Playlist playlist =  playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy playlist có id: "+ id));
        User user = userService.getCurrentUser();
        if(playlist.getIdCreateBy() != user.getId()){
            throw new BadRequestException("bạn không có quyền truy cập và playlist này");
        }
        return getPlaylistDto(playlist);
    }

    @Override
    public PlaylistDto insertPlayList(PlaylistDto playlistDto){
        User user = userService.getCurrentUser();
        Playlist playlist = new Playlist();
        getPlaylist(playlist, playlistDto, user,false);
        playlistRepository.save(playlist);
        return getPlaylistDto(playlist);
    }

    @Override
    public PlaylistDto updatePlayList(PlaylistDto playlistDto){
        Playlist playlist = playlistRepository.findById(playlistDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy playlist có id là "+ playlistDto.getId()));
        User user = userService.getCurrentUser();
        if(playlist.getIdCreateBy() != user.getId()){
            throw new BadRequestException("bạn không có quyền truy cập và playlist này");
        }
        getPlaylist(playlist, playlistDto, user,false);
        playlistRepository.save(playlist);
        return getPlaylistDto(playlist);
    }

    @Override
    public void deletePlaylist(Long id){
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy playlist có id là "+ id));
        User user = userService.getCurrentUser();
        if(playlist.getIdCreateBy() != user.getId()){
            throw new BadRequestException("bạn không có quyền truy cập và playlist này");
        }
        DBLogicUtil.setupDelete(playlist, user);
        playlistRepository.save(playlist);
    }

    private void getPlaylist(Playlist playlist, PlaylistDto playlistDto, User user, boolean isUpdate) {
        ConvertUtil.copyProIgNull(playlistDto, playlist);
        List<Song> songs = playlistDto
                .getSongs()
                .stream()
                .map(id -> songRepository.findById(id)
                        .orElseThrow(() -> new NotFoundItemException("không tìm thấy song có id: "+ id)))
                .collect(Collectors.toList());
        playlist.setSongs(songs);
        if(isUpdate){
            DBLogicUtil.setupUpdate(playlist, user);
        }else{
            DBLogicUtil.setupCreate(playlist, user);
        }
    }

    private PlaylistDto getPlaylistDto(Playlist playlist) {
        PlaylistDto playlistDto = new PlaylistDto();
        ConvertUtil.copyProIgNull(playlist, playlistDto);
        if(playlist.getSongs() != null){
            List<Long> listSongId = playlist.getSongs().stream()
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
            playlistDto.setSongs(listSongId);
        }
        return playlistDto;
    }

}
