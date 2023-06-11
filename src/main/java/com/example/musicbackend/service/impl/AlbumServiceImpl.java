package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.entity.Album;
import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.AlbumRepository;
import com.example.musicbackend.repository.ArtistRepository;
import com.example.musicbackend.service.AlbumService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    
    private final UserService userService;
    
    private final AlbumRepository albumRepository;

    private final ArtistRepository artistRepository;

    @Override
    public List<AlbumDto> findAllAlbum(){
        return albumRepository.findAll().stream()
                .map(this::getAlbumDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlbumDto findById(Long id) {
        Album Album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id));
        return getAlbumDto(Album);
    }

    private AlbumDto getAlbumDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        ConvertUtil.copyProIgNull(album, albumDto);
        Artist artist = album.getArtist();
        if(artist != null){
            albumDto.setArtistId(artist.getId());
        }
        return albumDto;
    }

    @Override
    public byte[] getPhotoAlbumById(Long id){
        return albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id))
                .getAlbumArtData();
    }

    @Override
    public AlbumDto insertAlbum(AlbumDto AlbumDto){
        User user = userService.getCurrentUser();
        Album Album = getAlbum(AlbumDto, user,false);
        albumRepository.save(Album);
        return getAlbumDto(Album);
    }

    @Override
    public AlbumDto updateAlbum(AlbumDto AlbumDto){
        if(!albumRepository.existsById(AlbumDto.getId())){
            throw new NotFoundItemException("không tìm thấy Album từ AlbumDto có id là "+ AlbumDto.getId());
        }
        User user = userService.getCurrentUser();
        Album Album = getAlbum(AlbumDto, user,true);
        albumRepository.save(Album);
        return getAlbumDto(Album);
    }

    private Album getAlbum(AlbumDto albumDto, User user, boolean isUpdate) {
        Album album = new Album();
        ConvertUtil.copyProIgNull(albumDto, album);
        if(albumDto.getArtistId() > 0) {
            Artist artist = artistRepository.findById(albumDto.getArtistId())
                    .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist trong albumDto có id là "+ albumDto.getArtistId()));
            album.setArtist(artist);
        }
        if(isUpdate){
            DBLogicUtil.setupUpdate(album, user);
        }else{
            DBLogicUtil.setupCreate(album, user);
        }
        return album;
    }
    
}
