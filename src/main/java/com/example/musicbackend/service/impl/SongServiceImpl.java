package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.constant.Constants;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.*;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.repository.*;
import com.example.musicbackend.service.SongService;
import com.example.musicbackend.service.UserService;
import com.example.musicbackend.validate.ValidateSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final UserService userService;

    private final ArtistRepository artistRepository;

    private final AlbumRepository albumRepository;

    private final GenreRepository genreRepository;

    private final ValidateSupport validateSupport;

    @Override
    public Page<SongDto> searchAll(SearchSongRequest searchSongRequest){
        if(searchSongRequest.getSize() < 1){
            searchSongRequest.setSize(Constants.DEFAULT_SIZE_RECORD);
        }
        if(searchSongRequest.getPageCurrent() < 0){
            searchSongRequest.setPageCurrent(Constants.DEFAULT_PAGE);
        }
        Page<Song> pageSong = songRepository.searchAll(searchSongRequest.getName()
                , searchSongRequest.getNameArtist()
                , searchSongRequest.getNameAlbum()
                , searchSongRequest.getListIdGenre()
                , PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()));
        List<SongDto> listSongDto = pageSong.getContent().stream().map(this::getSongDto).collect(Collectors.toList());
        return new PageImpl<>(listSongDto,  PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()), pageSong.getTotalElements());
    }

    @Override
    public Page<SongDto> searchByUser(SearchSongRequest searchSongRequest){
        if(searchSongRequest.getSize() < 1){
            searchSongRequest.setSize(Constants.DEFAULT_SIZE_RECORD);
        }
        if(searchSongRequest.getPageCurrent() < 0){
            searchSongRequest.setPageCurrent(Constants.DEFAULT_PAGE);
        }
        User user = userService.getCurrentUser();
        Page<Song> pageSong = songRepository.searchByUser(searchSongRequest.getName()
                , searchSongRequest.getNameArtist()
                , searchSongRequest.getNameAlbum()
                , user.getId()
                , searchSongRequest.getListIdGenre()
                , PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()));
        List<SongDto> songDtos = pageSong.getContent().stream().map(this::getSongDto).collect(Collectors.toList());
        return new PageImpl<>(songDtos,  PageRequest.of(searchSongRequest.getPageCurrent(), searchSongRequest.getSize()), pageSong.getTotalElements());
    }

    @Override
    public SongDto findById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là "+ id));
        return getSongDto(song);
    }

    @Override
    public byte[] getDataSongById(Long id){
        return songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là: "+ id))
                .getData();
    }

    @Override
    public SongDto insertSong(SongDto songDto, MultipartFile data, MultipartFile photo){
        validateFile(data, photo);
        User user = userService.getCurrentUser();
        Song song = new Song();
        getSong(song, songDto, user, false);
        song.setData(getData(data));
        song.setPhoto(getData(photo));
        songRepository.save(song);
        return getSongDto(song);
    }

    @Override
    public SongDto updateSong(SongDto songDto, MultipartFile data, MultipartFile photo){
        validateFile(data, photo);
        Song song = updateDataDto(songDto);
        song.setData(getData(data));
        song.setPhoto(getData(photo));
        songRepository.save(song);
        return getSongDto(song);
    }

    private void validateFile(MultipartFile data, MultipartFile photo) {
        if(validateSupport.isImageFile(photo)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(validateSupport.checkLength(photo)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
        if(validateSupport.isMusicFile(data)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(validateSupport.checkLength(data)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
    }

    @Override
    public SongDto updateSong(SongDto songDto){
        Song song = updateDataDto(songDto);
        songRepository.save(song);
        return getSongDto(song);
    }

    private Song updateDataDto(SongDto songDto) {
        Song song = songRepository.findById(songDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy song từ songDto có id là "+ songDto.getId()));
        User user = userService.getCurrentUser();
        if(user.getId().equals(song.getIdCreateBy())){
            throw new BadRequestException("bạn không có quyền truy cập vào bài hát này");
        }
        getSong(song, songDto, user, true);
        return song;
    }

    @Override
    public void deleteSong(Long id){
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy song từ songDto có id là "+ id));
        User user = userService.getCurrentUser();
        if(user.getId().equals(song.getIdCreateBy())){
            throw new BadRequestException("bạn không có quyền truy cập vào bài hát này");
        }
        DBLogicUtil.setupDelete(song, user);
        songRepository.save(song);
    }

    @Override
    public byte[] getPhotoSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là: "+ id))
                .getPhoto();
    }


    private void getSong(Song song, SongDto songDto, User user, boolean isUpdate) {
        ConvertUtil.copyProIgNull(songDto, song);

        if(songDto.getArtistId() != null){
            Artist artist = artistRepository.findById(songDto.getArtistId())
                    .orElseThrow(() -> new NotFoundItemException("không tìm thấy Artist có id là: "+ songDto.getArtistId()));
            song.setArtist(artist);
        }

        if(songDto.getAlbumId() != null){
            Album album = albumRepository.findById(songDto.getAlbumId())
                    .orElseThrow(() -> new NotFoundItemException("không tìm thấy Album có id là: "+ songDto.getAlbumId()));
            song.setAlbum(album);
        }

        if(songDto.getListGenreId() != null){
            List<Genre> listGenre = songDto.getListGenreId()
                    .stream()
                    .map(id -> genreRepository.findById(id)
                            .orElseThrow(() -> new NotFoundItemException("không tìm thấy Genre có id: "+ id)))
                    .collect(Collectors.toList());
            song.setGenres(listGenre);
        }

        if(isUpdate){
            DBLogicUtil.setupUpdate(song, user);
        }else{
            DBLogicUtil.setupCreate(song, user);
        }
    }

    @Override
    public SongDto getSongDto(Song song) {
        SongDto songDto = new SongDto();
        ConvertUtil.copyProIgNull(song, songDto);
        songDto.setArtistId(song.getArtist() != null ? song.getArtist().getId(): null);
        songDto.setAlbumId(song.getAlbum() != null ? song.getAlbum().getId() : null);
        if(song.getGenres() != null){
            List<Long> listGenreId = song.getGenres().stream().map(BaseEntity::getId).collect(Collectors.toList());
            songDto.setListGenreId(listGenreId);
        }
        songDto.setCountLike(songRepository.countLike(songDto.getId()));
        return songDto;
    }

    @Override
    public SongDto clickLikeSong(Long songId){
        Song song = songRepository.findById(songId).orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id: "+ songId) );
        User user = userService.getCurrentUser();
        Boolean isLiked = songRepository.checkLiked(songId, user.getId());
        List<User> listUserLike;
        if(isLiked){
            listUserLike = song.getLikedUsers();
            listUserLike.add(user);
        }else{
            listUserLike = song.getLikedUsers().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .collect(Collectors.toList());
        }
        song.setLikedUsers(listUserLike);
        // không update người thay đổi vì sẽ có rất nhiều người lại nên thông tin bài thay đổi người lại không cần quan tâm
        Song songNew = songRepository.save(song);
        return getSongDto(songNew);
    }

    private byte[] getData(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileWrongException("dữ liệu file sai không lấy ra được");
        }
    }


}
