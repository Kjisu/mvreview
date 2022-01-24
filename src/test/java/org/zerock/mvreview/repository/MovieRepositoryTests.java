package org.zerock.mvreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.MovieImage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {


    @Autowired
    private MovieRepository movieRepository;

    //영화 등록할 때, 영화 이미지는 MovieImage테이블에 저장해야 하니까
    @Autowired
    private MovieImageRepository movieImageRepository;

    //영화 더미 데이터 등록
    @Test
    void insertMovie(){

        //100개 영화 데이터 insert
        IntStream.rangeClosed(1,100).forEach(i -> {

            //1.영화 데이터를 먼저 넣는다

                //Movie엔티티 생성
                Movie movie = Movie.builder().title("Movie.." + i).build();
                //데이터 저장
                movieRepository.save(movie);


            //2.저장된 영화 번호를 사용하여 MovieImage테이블에 데이터를 넣는다

                //이미지 저장 가능 갯수 최대 5개
                int count = (int)(Math.random() * 5)+1;

                //영화 이미지 갯수(count)만큼 MovieImage 데이터 생성해서 insert
                for(int j=0;j<count;j++){
                    MovieImage movieImage = MovieImage.builder()
                            .imgName("test" + j + ".jpg")
                            .movie(movie)
                            .uuid(UUID.randomUUID().toString()).build();

                    movieImageRepository.save(movieImage);
                }
        });

    }


    //목록 데이터 가져오는 메서드 테스트
    @Test
    void getListPage(){

        //페이징 정보 저장하는 객체 생성
        Pageable pageable = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"mno"));

        //쿼리 메서드 실행
        Page<Object[]> result = movieRepository.getListPage(pageable);

        //결과 출력
        for(Object[] obj : result.getContent()){
            System.out.println(Arrays.toString(obj));
        }
    }


    //특정 영화의 모든 데이터(이미지, 평점평균, 댓글갯수) 가져오는 메서드 테스트
    @Test
    void getMovieWithAll(){

        //쿼리 메서드 실행
        List<Object[]> result = movieRepository.getMovieWithAll(100L);

        //결과 출력
        for(Object[] obj:result){
            System.out.println(Arrays.toString(obj));
        }
    }

    @Test
    void uuid(){

        movieImageRepository.deleteByUuid("a343e828-2b0a-44fd-83f1-fa26531e65b4");

    }




}
