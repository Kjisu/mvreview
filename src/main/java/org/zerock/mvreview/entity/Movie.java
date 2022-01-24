package org.zerock.mvreview.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseEntity{

    /*컬럼 : 영화번호, 제목*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;


    //제목 수정
    public void changeTitle(String title){
        this.title = title;
    }

}
