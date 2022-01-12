package org.zerock.mvreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mvreview.entity.Member;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;


    //리뷰 200개 insert
    @Test
    void insertReviews(){

        IntStream.rangeClosed(1,200).forEach(i->{

            //영화번호 랜덤(1~100) 생성
            Long mno = (long)(Math.random()*100)+1;
            Movie movie = Movie.builder().mno(mno).build();

            //리뷰 남긴 회원 번호(1~100) 생성
            Long mid = (long)(Math.random()*100)+1;
            Member member = Member.builder().mid(mid).build();

            //Review 엔티티 생성
            Review review = Review.builder()
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화는 말이야 .. "+i)
                    .movie(movie)
                    .member(member).build();

            //insert 쿼리 실행
            reviewRepository.save(review);


        });
    }

    //특정 영화에 달린 리뷰, 회원 데이터 조회 메서드 테스트
    @Test
    void findByMovie(){

        //Movie엔티티 생성
        Movie movie = Movie.builder().mno(98L).build();

        //쿼리 실행
        List<Review> result = reviewRepository.findByMovie(movie);

        //결과 출력
        //만약 findByMovie()에
        //@EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)이걸 설정해주지 않으면 두 번째 출력코드에서 오류가 발생한다
        //member는 기본적으로 lazy타입으로 설정되어 있기 때문에
        for(Review r:result){
            System.out.println(r);
            System.out.println(r.getMember().getNickname());
        }

    }


}
