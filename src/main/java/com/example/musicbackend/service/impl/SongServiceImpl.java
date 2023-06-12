package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.*;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.*;
import com.example.musicbackend.service.SongService;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
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

    private final PlaylistRepository playlistRepository;

    @Override
    public SongDto findById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là "+ id));
        SongDto songDto = getSongDto(song);
        return songDto;
    }

    @Override
    public byte[] getDataSongById(Long id){
        return songRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy bài hát có id là: "+ id))
                .getData();
    }

    @Override
    public SongDto insertSong(SongDto songDto, MultipartFile file){
        User user = userService.getCurrentUser();
        Song song = new Song();
        getSong(song, songDto, user, false);
        song.setData(getData(file));
        songRepository.save(song);
        return getSongDto(song);
    }

    @Override
    public SongDto updateSong(SongDto songDto, MultipartFile file){
        Song song = songRepository.findById(songDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy song từ songDto có id là "+ songDto.getId()));
        User user = userService.getCurrentUser();
        getSong(song, songDto, user, true);
        song.setData(getData(file));
        songRepository.save(song);
        return getSongDto(song);
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

    private SongDto getSongDto(Song song) {
        SongDto songDto = new SongDto();
        ConvertUtil.copyProIgNull(song, songDto);
        songDto.setArtistId(song.getArtist() != null ? song.getArtist().getId(): null);
        songDto.setAlbumId(song.getAlbum() != null ? song.getAlbum().getId() : null);
        if(song.getGenres() != null){
            List<Long> listGenreId = song.getGenres().stream().map(BaseEntity::getId).collect(Collectors.toList());
            songDto.setListGenreId(listGenreId);
        }
        return songDto;
    }

    private byte[] getData(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileWrongException("dữ liệu file sai không lấy ra được");
        }
    }

}
