package org.zerock.mvreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mvreview.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage,Long> {

    //특정 영화 번호에 해당되는 영화 이미지 모두 삭제 쿼리
    @Query("delete from MovieImage mi where mi.movie.mno =:mno")
    @Modifying
    void deleteByMno(Long mno);

    //uuid가 같은 영화 이미지 삭제 쿼리
    @Query("delete from MovieImage mi where mi.uuid =:uuid")
    @Modifying
    @Transactional
    void deleteByUuid(String uuid);


}
