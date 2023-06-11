package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.entity.Album;
import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.AlbumRepository;
import com.example.musicbackend.repository.ArtistRepository;
import com.example.musicbackend.service.ArtistService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    private final AlbumRepository albumRepository;

    private final UserService userService;

    @Override
    public List<ArtistDto> findAllArtist(){
        return artistRepository.findAll().stream()
                .map(this::getArtistDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArtistDto findById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist có id: "+ id));
        return getArtistDto(artist);
    }

    private ArtistDto getArtistDto(Artist artist) {
        ArtistDto artistDto = new ArtistDto();
        ConvertUtil.copyProIgNull(artist, artistDto);
        List<Long> listAlbumId = null;
        if(artist.getAlbums() != null){
            listAlbumId = artist.getAlbums().stream()
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
        }
        artistDto.setListAlbumId(listAlbumId);
        return artistDto;
    }

    @Override
    public byte[] getPhotoArtistById(Long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist có id: "+ id))
                .getPhotoData();
    }

    @Override
    public ArtistDto insertArtist(ArtistDto artistDto){
        User user = userService.getCurrentUser();
        Artist artist = getArtist(artistDto, user,false);
        artistRepository.save(artist);
        return getArtistDto(artist);
    }

    @Override
    public ArtistDto updateArtist(ArtistDto artistDto){
        if(!artistRepository.existsById(artistDto.getId())){
            throw new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ artistDto.getId());
        }
        User user = userService.getCurrentUser();
        Artist artist = getArtist(artistDto, user,true);
        artistRepository.save(artist);
        return getArtistDto(artist);
    }

    private Artist getArtist(ArtistDto artistDto, User user, boolean isUpdate) {
        Artist artist = new Artist();
        ConvertUtil.copyProIgNull(artistDto, artist);
        List<Album> albumList = artistDto
                .getListAlbumId()
                .stream()
                .map(id -> albumRepository.findById(id)
                        .orElseThrow(() -> new NotFoundItemException("không tìm thấy album có id: "+ id)))
                .collect(Collectors.toList());
        artist.setAlbums(albumList);
        if(isUpdate){
            DBLogicUtil.setupUpdate(artist, user);
        }else{
            DBLogicUtil.setupCreate(artist, user);
        }
        return artist;
    }

}
