package org.zerock.mvreview.service;

import org.zerock.mvreview.dto.MovieDTO;
import org.zerock.mvreview.dto.MovieImageDTO;
import org.zerock.mvreview.dto.PageRequestDTO;
import org.zerock.mvreview.dto.PageResultDTO;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.MovieImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {


    /////////////////////////////////////////////////////////////////////// 등록 작업 : 등록된 게시글 번호를 반환
    Long register(MovieDTO movieDTO);

    /////////////////////////////////////////////////////////////////////// 목록 작업 : PageResultDTO 리턴
    PageResultDTO<MovieDTO,Object[]> getList(PageRequestDTO dto);

    /////////////////////////////////////////////////////////////////////// 조회 작업
    MovieDTO getMovie(Long mno);

    /////////////////////////////////////////////////////////////////////// 수정
    Long update(MovieDTO movieDTO);

    /////////////////////////////////////////////////////////////////////// 삭제
    void remove(Long mno);

    /////////////////////////////////////////////////////////////////////// 영화 이미지 삭제 작업
    boolean removeImg(String uuid);

    /////////////////////////////////////////////////////////////////////// dto -> entity로 변환
    default Map<String,Object> dtoToEntity(MovieDTO dto){

        //저장공간 map 생성
        //한번에 두가지 종류의 객체를 반환해야 하기에 map을 사용해야함
        Map<String,Object> hashMap = new HashMap<>();

        //MovieDTO -> movie엔티티로 옮기기
        Movie movie = Movie.builder()
                .mno(dto.getMno())
                .title(dto.getTitle())
                .build();

        //변환한 엔티티를 map에 담기
        hashMap.put("movie",movie);

        //MovieImageDTO리스트 -> MovieImage에 담기
        //null이면 pass
        List<MovieImageDTO> imageDTOList = dto.getImageDTOList();

        //MovieImage 엔티티 담을 리스트 생성
        List<MovieImage> movieImageList = new ArrayList<>();

        //dto에 있던 데이터 -> 엔티티에 하나씩 옮겨담기
        if(imageDTOList != null && imageDTOList.size()>0){

            for(MovieImageDTO movieImageDTO:imageDTOList){

                if(movieImageDTO.getImgName() != null){

                    MovieImage movieImage = MovieImage.builder()
                            .uuid(movieImageDTO.getUuid())
                            .imgName(movieImageDTO.getImgName())
                            .path(movieImageDTO.getPath())
                            .movie(movie)
                            .build();

                    movieImageList.add(movieImage);
                }

            }
        }

        //이미지 리스트를 map에 추가
        hashMap.put("imgList",movieImageList);
        System.out.println("movieImageList??>>"+movieImageList);

        return hashMap;

    }

    /////////////////////////////////////////////////////////////////////// entity -> dto 변환
    //JPA에서 나오는 엔티티 객체들과 Double, Long 등의 데이터를 MovieDTO로 변환
    default MovieDTO entityToDTO(Movie movie, List<MovieImage> movieImages,Double avg,Long reviewCnt){

        //Movie엔티티
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        //이미지 데이터 movieImageDTO에 담기
        List<MovieImageDTO> movieImageDTOS = movieImages.stream().map(movieImage -> {

            MovieImageDTO movieImageDTO = MovieImageDTO.builder()
                    .path(movieImage.getPath())
                    .imgName(movieImage.getImgName())
                    .uuid(movieImage.getUuid())
                    .build();
            return movieImageDTO;

        }).collect(Collectors.toList());

        System.out.println("???????????"+movieImageDTOS);

        //movieImageDTO 리스트, 평균평점, 리뷰갯수 담기
        movieDTO.setImageDTOList(movieImageDTOS);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt.intValue());

        System.out.println("???????"+movieDTO);

        return movieDTO;
    }

}
