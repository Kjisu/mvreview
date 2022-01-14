package org.zerock.mvreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mvreview.dto.MovieDTO;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.MovieImage;
import org.zerock.mvreview.repository.MovieImageRepository;
import org.zerock.mvreview.repository.MovieRepository;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;

    //////////////////////////////////////////////////////// 등록 처리 메서드
    @Override
    public Long register(MovieDTO movieDTO) {

        log.info(">>>>>>>>>>>>>>>> [S] register()..... ");

        //dto -> entity로 변환
        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        //맵에서 entity들 꺼내서
        Movie movie = (Movie)entityMap.get("movie");
        List<MovieImage> imgList = (List)entityMap.get("imgList");
        log.info(">?????????????"+imgList);

        //디비에 등록1 (영화 데이터 - Movie 엔티티)
        movieRepository.save(movie);

        //디비에 등록2(영화 이미지 데이터)
        for(MovieImage movieImage:imgList){
            movieImageRepository.save(movieImage);
        }

        //등록된 영화 번호 리턴
        return movie.getMno();
    }
}
