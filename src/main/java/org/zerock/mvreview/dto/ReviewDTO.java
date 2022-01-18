package org.zerock.mvreview.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    //화면에 필요한 모든 정보

    private Long reviewNum;
    private String text;
    private int grade;
    private LocalDateTime regDate,modDate;

    private Long mid;//Member-pk
    private String nickName;
    private String email;

    private Long mno;//Movie-pk
}
