package org.zerock.mvreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.mvreview.dto.ReviewDTO;
import org.zerock.mvreview.service.ReviewService;

import java.util.List;

@RestController //리뷰는 ajax로 처리되기 때문에 리턴 데이터가 http메세지 바디에 바로 담겨서 넘어간다
@Log4j2
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //////////////////////////////////////////////////////////////////////////// 목록 요청
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getReviewList(@PathVariable Long mno){

        log.info("[C] getReviewList()....");

        List<ReviewDTO> result = reviewService.getMovieReviewList(mno);

        return new ResponseEntity<>(result, HttpStatus.OK);//DTO는 JSON형태로 변환되어 처리
    }


    //////////////////////////////////////////////////////////////////////////// 등록 요청
    @PostMapping("/{mno}")
    public ResponseEntity<Long> reviewRegister(@RequestBody ReviewDTO dto){

        log.info("[C] reviewRegister()....");

        Long result = reviewService.register(dto);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    //////////////////////////////////////////////////////////////////////////// 수정 요청
    @PutMapping("/{mno}/{reviewNum}")
    public ResponseEntity<Long> reviewModify(@RequestBody ReviewDTO dto){

        log.info("[C] reviewModify()....");

        reviewService.modify(dto);

        return new ResponseEntity<>(dto.getReviewNum(),HttpStatus.OK);
    }


    //////////////////////////////////////////////////////////////////////////// 삭제 요청
    @DeleteMapping("/{mno}/{reviewNum}")
    public ResponseEntity<Long> reviewRemove(@PathVariable Long reviewNum){

        log.info("[C] reviewRemove()....");

        reviewService.remove(reviewNum);

        return new ResponseEntity<>(reviewNum,HttpStatus.OK);

    }



}
