package org.zerock.mvreview.service;

import org.zerock.mvreview.dto.MovieDTO;
import org.zerock.mvreview.dto.MovieImageDTO;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.MovieImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {


    /////////////////////////////////////////////////////////////////////// 등록 처리 작업 : 등록된 게시글 번호를 반환
    Long register(MovieDTO movieDTO);


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
        List<MovieImageDTO> imageDTOList = dto.getImageDTOList();

        //MovieImage 엔티티 담을 리스트 생성
        List<MovieImage> movieImageList = new ArrayList<>();

        //dto에 있던 데이터 -> 엔티티에 하나씩 옮겨담기
        if(imageDTOList != null && imageDTOList.size()>0){

            imageDTOList.stream().map(movieImageDTO -> {

                //dto -> entity로 옮겨담기
                MovieImage movieImage = MovieImage.builder()
                        .uuid(movieImageDTO.getUuid())
                        .imgName(movieImageDTO.getImgName())
                        .path(movieImageDTO.getPath())
                        .movie(movie)
                        .build();

                //리스트에 추가
                movieImageList.add(movieImage);

                return movieImage;

            }).collect(Collectors.toList());
        }

        //이미지 리스트를 map에 추가
        hashMap.put("imgList",movieImageList);

        return hashMap;

    }


}
