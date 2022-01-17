package org.zerock.mvreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    //클라로부터 넘어오는 영화 데이터 담는 클래스

    private Long mno; //영화 번호

    private String title; //영화제목

    @Builder.Default//초기화해주지 않으면 인스턴스 생성시 null이 들어가는데 이렇게 초기화해주면 ArrayList가 들어간다
    private List<MovieImageDTO> imageDTOList = new ArrayList<>(); //영화이미지dto list

    private double avg; //평균평점

    private int reviewCnt; //리뷰갯수

    private LocalDateTime regDate; //등록일자

    private LocalDateTime modDate; //수정일자


}
