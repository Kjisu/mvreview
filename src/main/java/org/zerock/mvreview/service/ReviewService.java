package org.zerock.mvreview.service;

import org.zerock.mvreview.dto.ReviewDTO;
import org.zerock.mvreview.entity.Member;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.Review;

import java.util.List;

public interface ReviewService {

    //모든 리뷰 가져오기, 리뷰 등록 삭제 수정 기능
    //entity -> dto , dto -> entity 변환 기능 (default 메서드)

    ///////////////////////////////////////////////////////////////// 모든 리뷰 가져오기
    List<ReviewDTO> getMovieReviewList(Long mno);

    ///////////////////////////////////////////////////////////////// 등록
    Long register(ReviewDTO dto);

    ///////////////////////////////////////////////////////////////// 수정
    void modify(ReviewDTO dto);

    ///////////////////////////////////////////////////////////////// 삭제
    void remove(Long reviewNum);


    ///////////////////////////////////////////////////////////////// dto -> entity 변환
    default Review dtoToEntity(ReviewDTO dto){

        Review review = Review.builder()
                .reviewnum(dto.getReviewNum())
                .text(dto.getText())
                .grade(dto.getGrade())
                .member(Member.builder().mid(dto.getMid()).build())
                .movie(Movie.builder().mno(dto.getMno()).build())
                .build();

        return review;
    }


    ///////////////////////////////////////////////////////////////// entity -> dto 변환
    default ReviewDTO entityToDTO(Review re){

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewNum(re.getReviewnum())
                .email(re.getMember().getEmail())
                .nickName(re.getMember().getNickname())
                .regDate(re.getRegDate())
                .modDate(re.getModDate())
                .mid(re.getMember().getMid())
                .mno(re.getMovie().getMno())
                .text(re.getText())
                .grade(re.getGrade())
                .build();

        return reviewDTO;
    }


}
