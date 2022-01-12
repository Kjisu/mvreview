package org.zerock.mvreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mvreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {

    //////////////////////////////////////////////////////////////////////////// 페이지 처리
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) " +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review r on r.movie = m " +
            " group by m ")
    Page<Object[]> getListPage(Pageable pageable);

    //영화 이미지 중 가장 나중에 추가된 이미지를 가져오려면,,서브 쿼리 이용
    /*  @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) " +
              " from Movie m left outer join MovieImage mi on mi.movie = m " +
              " and mi.inum = (select max(mi2.inum) from MovieImage mi2 where mi2.movie = m ) " +
              " left outer join Review r on r.movie = m " +
              " group by m ")
      Page<Object[]> getListPage(Pageable pageable);
    */


    /////////////////////////////////////////////////////////////////////////////// 특정 영화 조회
    //영화 이미지를 중심으로 그룹핑 안하면 맨 처음 저장된 영화이미지만 출력된다
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) " +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review r on r.movie = m " +
            " where m.mno = :mno " +
            " group by mi")
    List<Object[]> getMovieWithAll(Long mno);


}
