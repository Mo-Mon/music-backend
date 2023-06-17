package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.ConvertUtil;
import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.constant.Constants;
import com.example.musicbackend.dto.GenreDto;
import com.example.musicbackend.entity.Genre;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.entity.base.BaseEntity;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.payload.request.SearchGenreRequest;
import com.example.musicbackend.payload.response.GenreResponse;
import com.example.musicbackend.repository.GenreRepository;
import com.example.musicbackend.repository.SongRepository;
import com.example.musicbackend.service.GenreService;
import com.example.musicbackend.service.UserService;
import com.example.musicbackend.validate.ValidateSupport;
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
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final UserService userService;

    private final SongRepository songRepository;

    @Override
    public Page<GenreResponse> search(SearchGenreRequest searchGenreRequest){

        if(searchGenreRequest.getSize() < 1){
            searchGenreRequest.setSize(Constants.DEFAULT_SIZE_RECORD);
        }
        if(searchGenreRequest.getPageCurrent() < 0){
            searchGenreRequest.setPageCurrent(Constants.DEFAULT_PAGE);
        }

        return genreRepository.search(searchGenreRequest.getName()
        , PageRequest.of(searchGenreRequest.getPageCurrent(),searchGenreRequest.getSize()));
    }

    @Override
    public List<GenreDto> findAllGenre(){
        return genreRepository.findAll().stream()
                .map(this::getGenreDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findById(Long id){
        Genre genre = genreRepository.findById(id).orElseThrow(() ->new NotFoundItemException("không tìm thấy genre có id: "+ id));
        return getGenreDto(genre);
    }

    @Override
    public byte[] getPhotoGenreById(Long id){
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy Genre có id: "+ id))
                .getPhotoData();
    }

    @Override
    public GenreDto insertGenre(GenreDto genreDto, MultipartFile file){
        if(ValidateSupport.isImageFile(file)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(ValidateSupport.checkLength(file)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
        User user = userService.getCurrentUser();
        Genre genre = new Genre();
        getGenre(genre, genreDto, user, false);
        genre.setPhotoData(getData(file));
        Genre newGenre =genreRepository.save(genre);
        return getGenreDto(newGenre);
    }

    @Override
    public GenreDto updateGenre(GenreDto genreDto, MultipartFile file){
        if(ValidateSupport.isImageFile(file)){
            throw new BadRequestException("data request file này phải có đuôi dạng file ảnh (\"png\",\"jpg\",\"jpeg\", \"bmp\")");
        }
        if(ValidateSupport.checkLength(file)){
            throw new BadRequestException("data request file phải có độ dài dung lượng dưới 2mb");
        }
        Genre genre = genreRepository.findById(genreDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy genre từ genreDto có id là "+ genreDto.getId()));
        User user = userService.getCurrentUser();
        getGenre(genre, genreDto, user, false);
        genre.setPhotoData(getData(file));
        Genre newGenre = genreRepository.save(genre);
        return getGenreDto(newGenre);
    }

    @Override
    public void deleteGenre(Long id){
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy genre có id là "+ id));
        User user = userService.getCurrentUser();
        DBLogicUtil.setupDelete(genre, user);
        genreRepository.save(genre);
    }

    @Override
    public GenreDto updateGenre(GenreDto genreDto) {
        Genre genre = genreRepository.findById(genreDto.getId())
                .orElseThrow(() -> new NotFoundItemException("không tìm thấy genre từ genreDto có id là "+ genreDto.getId()));
        User user = userService.getCurrentUser();
        getGenre(genre, genreDto, user, false);
        Genre newGenre = genreRepository.save(genre);
        return getGenreDto(newGenre);
    }

    private void getGenre(Genre genre, GenreDto genreDto, User user, boolean isUpdate) {
        ConvertUtil.copyProIgNull(genreDto, genre);
        List<Song> songs = genreDto
                .getListSongId()
                .stream()
                .map(id -> songRepository.findById(id)
                        .orElseThrow(() -> new NotFoundItemException("không tìm thấy song có id: "+ id)))
                .collect(Collectors.toList());
        genre.setSongs(songs);
        if(isUpdate){
            DBLogicUtil.setupUpdate(genre, user);
        }else{
            DBLogicUtil.setupCreate(genre, user);
        }
    }

    private GenreDto getGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        ConvertUtil.copyProIgNull(genre, genreDto);
        if(genre.getSongs() != null){
            List<Long> listSongId = genre.getSongs().stream().map(BaseEntity::getId).collect(Collectors.toList());
            genreDto.setListSongId(listSongId);
        }
        return genreDto;
    }

    private byte[] getData(MultipartFile file){
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileWrongException("dữ liệu file sai không lấy ra được");
        }
    }
}
