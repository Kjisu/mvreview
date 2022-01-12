package org.zerock.mvreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mvreview.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    //회원 더미 데이터 100개 insert
    @Test
    void insertMember(){

        IntStream.rangeClosed(1,100).forEach(i->{

            //Member엔티티 생성
            Member member = Member.builder()
                    .email("m" + i + "@zero.com")
                    .pw("1111")
                    .nickname("m" + i).build();

            //데이터 insert 쿼리 실행
            memberRepository.save(member);

        });
    }


    //회원 삭제 (순서주의!! 회원 데이터를 삭제하기 전에 회원을 fk하고 있는 것들 모두 제거한 후에 회원 데이터를 삭제해야한다)
    @Test
    @Transactional //2가지 삭제작업이 하나의 트랜잭션 내에서 이루어지게 위해 사용
    @Commit //dml이라서 commit해줘야 변경사항이 반영된다
    void deleteMember(){


        //특정 회원pk를 fk하고 있는 리뷰 게시글을 삭제한다.
        reviewRepository.deleteByMember(Member.builder().mid(100L).build());

        //회원을 삭제한다.
        memberRepository.deleteById(100L);



    }
}
