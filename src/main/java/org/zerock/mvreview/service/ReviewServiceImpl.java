package org.zerock.mvreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mvreview.dto.ReviewDTO;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.Review;
import org.zerock.mvreview.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository repository;

    /////////////////////////////////////////////////////////////////////////////// 모든 리뷰 가져오기
    @Override
    public List<ReviewDTO> getMovieReviewList(Long mno) {

        log.info(">>>>>>>>>> [C] getMovieReviewList() ..........");

        List<Review> reviewList = repository.findByMovie(Movie.builder().mno(mno).build());

        List<ReviewDTO> result = reviewList.stream().map(review -> {

            return entityToDTO(review);

        }).collect(Collectors.toList());

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////// 등록
    @Override
    public Long register(ReviewDTO dto) {

        log.info(">>>>>>>>>> [C] register() ..........");

        //dto -> entity
        Review review = dtoToEntity(dto);
        //쿼리실행
        Review result = repository.save(review);

        return result.getReviewnum();
    }

    /////////////////////////////////////////////////////////////////////////////// 수정
    @Override
    public void modify(ReviewDTO dto) {

        log.info(">>>>>>>>>> [C] modify() ..........");

        //수정할 엔티티 가져오기
        Optional<Review> review = repository.findById(dto.getReviewNum());
        //수정할 데이터 변경
        if(review.isPresent()) {
            Review review1 = review.get();
            review1.changeGrade(dto.getGrade());
            review1.changeText(dto.getText());
            //수정된 내용 저장
            repository.save(review1);
        }
    }

    /////////////////////////////////////////////////////////////////////////////// 삭제
    @Override
    public void remove(Long reviewNum) {

        log.info(">>>>>>>>>> [C] remove() ..........");

        repository.deleteById(reviewNum);
    }
}
