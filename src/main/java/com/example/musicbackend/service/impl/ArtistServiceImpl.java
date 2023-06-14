package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.constant.Constants;
import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.Album;
import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.payload.request.SearchArtistRepuest;
import com.example.musicbackend.payload.response.ArtistResponse;
import com.example.musicbackend.repository.AlbumRepository;
import com.example.musicbackend.repository.ArtistRepository;
import com.example.musicbackend.repository.SongRepository;
import com.example.musicbackend.service.ArtistService;
import com.example.musicbackend.service.SongService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    private final AlbumRepository albumRepository;

    private final UserService userService;

    private final SongRepository songRepository;

    private final SongService songService;

    @Override
    public List<SongDto> getSongsInArtist(Long id){
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        return artist.getSongs().stream().filter(song -> !song.getDeleteFlag()).map(songService::getSongDto).collect(Collectors.toList());
    }

    @Override
    public ArtistDto addSongToArtist(Long id, Long songId){
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        List<Song> songList = artist.getSongs();
        Song song = songRepository.findById(songId).orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id: "+ songId) );
        songList.add(song);
        artist.setSongs(songList);
        User user = userService.getCurrentUser();
        DBLogicUtil.setupUpdate(artist,user);
        Artist artistNew = artistRepository.save(artist);
        return this.getArtistDto(artistNew);
    }

    @Override
    public ArtistDto deleteSongToArtist(Long id, Long songId){
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        List<Song> songList = artist.getSongs().stream().filter(song -> !song.getDeleteFlag() && !song.getId().equals(songId)).collect(Collectors.toList());
        songRepository.findById(songId)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id: "+ songId) );
        artist.setSongs(songList);
        User user = userService.getCurrentUser();
        DBLogicUtil.setupUpdate(artist,user);
        Artist artistNew = artistRepository.save(artist);
        return this.getArtistDto(artistNew);
    }

    @Override
    public Page<ArtistResponse> search(SearchArtistRepuest searchArtistRepuest){
        if(searchArtistRepuest.getSize() < 1){
            searchArtistRepuest.setSize(Constants.DEFAULT_SIZE_RECORD);
        }
        if(searchArtistRepuest.getPageCurrent() < 0){
            searchArtistRepuest.setPageCurrent(Constants.DEFAULT_PAGE);
        }
        return artistRepository.search(searchArtistRepuest.getName()
                , searchArtistRepuest.getInfo()
                , PageRequest.of(searchArtistRepuest.getPageCurrent()
                        ,searchArtistRepuest.getSize()));
    }

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
    public ArtistDto insertArtist(ArtistDto artistDto, MultipartFile file){
        User user = userService.getCurrentUser();
        Artist artist = new Artist();
        getArtist(artist, artistDto, user,false);
        artist.setPhotoData(getData(file));
        artistRepository.save(artist);
        return getArtistDto(artist);
    }

    @Override
    public ArtistDto updateArtist(ArtistDto artistDto, MultipartFile file){
        Artist artist = artistRepository.findById(artistDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ artistDto.getId()));
        User user = userService.getCurrentUser();
        getArtist(artist ,artistDto , user,true);
        artist.setPhotoData(getData(file));
        artistRepository.save(artist);
        return getArtistDto(artist);
    }

    @Override
    public void deleteArtist(Long id){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist có id là "+ id));
        User user = userService.getCurrentUser();
        DBLogicUtil.setupDelete(artist, user);
        artistRepository.save(artist);
    }

    @Override
    public ArtistDto updateArtist(ArtistDto artistDto) {
        Artist artist = artistRepository.findById(artistDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ artistDto.getId()));
        User user = userService.getCurrentUser();
        getArtist(artist ,artistDto , user,true);
        artistRepository.save(artist);
        return getArtistDto(artist);
    }

    private void getArtist(Artist artist, ArtistDto artistDto, User user, boolean isUpdate) {
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
    }

    private byte[] getData(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileWrongException("dữ liệu file sai không lấy ra được");
        }
    }

}
