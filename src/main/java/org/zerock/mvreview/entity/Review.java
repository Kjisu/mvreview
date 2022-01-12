package org.zerock.mvreview.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"movie","member"}) //호출 시 다른 엔티티를 사용하지 않도록
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends Serializers.Base {

    /*컬럼 : 리뷰번호(pk), 회원(fk), 영화(fk), 평점, 내용 */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY) //Member 테이블을 참조하기 때문에
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) //Movie 테이블을 참조하기 때문에
    private Movie movie;

    private int grade;

    private String text;

}
