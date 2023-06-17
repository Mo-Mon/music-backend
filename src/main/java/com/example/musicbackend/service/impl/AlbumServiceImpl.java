package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.Album;
import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.repository.AlbumRepository;
import com.example.musicbackend.repository.ArtistRepository;
import com.example.musicbackend.repository.SongRepository;
import com.example.musicbackend.service.AlbumService;
import com.example.musicbackend.service.SongService;
import com.example.musicbackend.service.UserService;
import com.example.musicbackend.validate.ValidateSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    
    private final UserService userService;

    private final AlbumRepository albumRepository;

    private final ArtistRepository artistRepository;

    private final SongRepository songRepository;

    private final SongService songService;

    @Override
    public Page<AlbumDto> search(String name, Pageable pageable){
        return albumRepository.search(name, pageable);
    }

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
            albumDto.setArtistName(artist.getName());
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
    public AlbumDto insertAlbum(AlbumDto albumDto, MultipartFile file){
        if(ValidateSupport.isImageFile(file)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(ValidateSupport.checkLength(file)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
        User user = userService.getCurrentUser();
        Album album = new Album();
        getAlbum(album, albumDto, user,false);
        album.setAlbumArtData(getData(file));
        albumRepository.save(album);
        return getAlbumDto(album);
    }

    @Override
    public AlbumDto updateAlbum(AlbumDto albumDto, MultipartFile file){
        if(ValidateSupport.isImageFile(file)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(ValidateSupport.checkLength(file)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
        Album album = albumRepository.findById(albumDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ albumDto.getId()));
        User user = userService.getCurrentUser();
        getAlbum(album, albumDto, user,true);
        album.setAlbumArtData(getData(file));
        albumRepository.save(album);
        return getAlbumDto(album);
    }

    @Override
    public AlbumDto updateAlbum(AlbumDto albumDto){
        Album album = albumRepository.findById(albumDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ albumDto.getId()));
        User user = userService.getCurrentUser();
        getAlbum(album, albumDto, user,true);
        albumRepository.save(album);
        return getAlbumDto(album);
    }

    @Override
    public void deleteAlbum(Long id){
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy artist từ artistDto có id là "+ id));
        User user = userService.getCurrentUser();
        DBLogicUtil.setupDelete(album, user);
        albumRepository.save(album);
    }

    private void getAlbum(Album album, AlbumDto albumDto, User user, boolean isUpdate) {
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
    }

    private byte[] getData(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileWrongException("dữ liệu file sai không lấy ra được");
        }
    }

    @Override
    public Page<SongDto> getSongsInAlbum(Long id, SearchSongRequest searchSongRequest){
        albumRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        Page<Song> pageSong = albumRepository.searchSongByAlbum(searchSongRequest.getName(),
                searchSongRequest.getNameArtist(),
                id,
                searchSongRequest.getListIdGenre(),
                PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()));
        List<SongDto> songDtoList = pageSong.getContent().stream().map(songService::getSongDto).collect(Collectors.toList());
        return new PageImpl<>(songDtoList,
                PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()),
                pageSong.getTotalPages());
    }

    @Override
    public AlbumDto addSongToAlbum(Long id, Long songId){
        Album album = albumRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        Song song = songRepository.findById(songId).orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id: "+ songId) );
        List<Song> songList = album.getSongs();
        songList.add(song);
        album.setSongs(songList);
        User user = userService.getCurrentUser();
        DBLogicUtil.setupUpdate(album,user);
        Album albumNew = albumRepository.save(album);
        return this.getAlbumDto(albumNew);
    }

    @Override
    public AlbumDto deleteSongToAlbum(Long id, Long songId){
        Album album = albumRepository.findById(id).orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id: "+ id) );
        songRepository.findById(songId).orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id: "+ songId) );
        List<Song> songList = album.getSongs().stream().filter(s -> !s.getDeleteFlag() && !s.getId().equals(songId)).collect(Collectors.toList());
        album.setSongs(songList);
        User user = userService.getCurrentUser();
        DBLogicUtil.setupUpdate(album,user);
        Album albumNew = albumRepository.save(album);
        return this.getAlbumDto(albumNew);
    }
}
