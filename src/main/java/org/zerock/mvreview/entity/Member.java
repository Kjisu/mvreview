package org.zerock.mvreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mv_member")
public class Member extends BaseEntity{

    /*컬럼 : 회원아이디(pk), 이메일, 비번, 닉네임*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    private String email;

    private String pw;

    private String nickname;

}
