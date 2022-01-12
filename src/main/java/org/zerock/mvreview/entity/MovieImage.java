package org.zerock.mvreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "movie")
public class MovieImage extends BaseEntity{

    /*컬럼 : 번호(pk),uuid(고유번호),이미지이름,저장경로(년/월/일),영화번호(fk)*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
