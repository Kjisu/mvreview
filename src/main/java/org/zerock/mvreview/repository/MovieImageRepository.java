package org.zerock.mvreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mvreview.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage,Long> {
}
