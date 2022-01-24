package org.zerock.mvreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mvreview.dto.MovieDTO;
import org.zerock.mvreview.dto.PageRequestDTO;
import org.zerock.mvreview.dto.PageResultDTO;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.MovieImage;
import org.zerock.mvreview.repository.MovieImageRepository;
import org.zerock.mvreview.repository.MovieRepository;
import org.zerock.mvreview.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final ReviewRepository reviewRepository;

    //////////////////////////////////////////////////////////////////////////////// 등록 메서드
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

    /////////////////////////////////////////////////////////////////////////////////// 목록 getList(PageRequestDTO dto)
    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO dto) {

        log.info("========== [S] getList() ==============");

        //쿼리 실행
        Page<Object[]> result = movieRepository.getListPage(dto.getPageable(Sort.by("mno").descending()));

        //entity->dto변환 작업 담기
        Function<Object[],MovieDTO> fn = (entity ->
                entityToDTO(
                (Movie)entity[0],(List<MovieImage>)(Arrays.asList((MovieImage)entity[1])),
                (Double)entity[2],(Long)entity[3]));

        return new PageResultDTO<>(result,fn);
    }

    /////////////////////////////////////////////////////////////////////////////////// 조회 getList(PageRequestDTO dto)
    @Override
    public MovieDTO getMovie(Long mno) {

        log.info(">>>>>>>>>>>>> [S] getMovie()");

        //영화 이미지가 여러 개니까
        //Object[] : (Movie객체 | MovieImage객체 | 평점 | 리뷰 갯수) × 이미지 갯수만큼 생성됨
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        //1.Movie엔티티는 가장 앞에 존재함 - 즉, 모든 row가 동일한 값을 가짐
        Movie movieEntity = (Movie)result.get(0)[0];

        //2.MovieImage -> list로 만들기
        List<MovieImage> movieImageList = new ArrayList<>();
        result.forEach(obj -> {
            MovieImage movieImage = (MovieImage)obj[1];
            movieImageList.add(movieImage);
        });

        //3.평점평균
        Double avg = (double)result.get(0)[2];

        //4. 리뷰갯수
        Long reviewCnt = (Long)result.get(0)[3];

        //엔티티 -> dto 변환
        return entityToDTO(movieEntity,movieImageList,avg,reviewCnt);
    }

    /////////////////////////////////////////////////////////////////////////////////// 삭제
    @Override
    @Transactional
    public void remove(Long mno) {

        log.info(">>>>>>>>>>>>> [S] remove()");

        //fk데이터부터 삭제 (리뷰,영화이미지)
        reviewRepository.deleteByMno(mno);
        movieImageRepository.deleteByMno(mno);

        //영화 게시글 삭제
        movieRepository.deleteById(mno);

    }

    /////////////////////////////////////////////////////////////////////////////////// 영화 이미지 삭제작업
    @Override
    public boolean removeImg(String uuid) {

        log.info(">>>>>>>>>>>>> [S] remove()");

        //uuid가 같은 영화 이미지 제거
        movieImageRepository.deleteByUuid(uuid);

        return true;
    }


}
