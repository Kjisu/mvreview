package org.zerock.mvreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mvreview.entity.Member;
import org.zerock.mvreview.entity.Movie;
import org.zerock.mvreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    /////////////////////////////////////////////////////////////////// 특정 영화의 모든 리뷰 조회
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    //@EntityGraph : 엔티티의 특정한 속성을 같이 로딩해주는 어노테이션, 특정 기능을 수행할 때만
    //=> 이걸 설정해주지 않으면 리뷰조회할 때 회원 데이터를 가져오지 못함(member가 LAZY타입으로 설정되어 있기 때문)
    List<Review> findByMovie(Movie movie);


    /////////////////////////////////////////////////////////////////// 특정 회원이 작성한 리뷰글 삭제 작업
    @Modifying //update나 delete를 사용하려면 반드시 필요
    @Query("delete from Review r where r.member = :member") //쿼리 안 적어도 되는데 그러면 비효율적임(delete 쿼리는 n번 실행함)
    void deleteByMember(Member member);


    /////////////////////////////////////////////////////////////////// 특정 영화번호의 댓글 삭제 쿼리
    @Query("delete from Review r where r.movie.mno =:mno")
    @Modifying
    void deleteByMno(Long mno);

}
