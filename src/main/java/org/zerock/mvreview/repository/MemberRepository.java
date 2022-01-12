package org.zerock.mvreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mvreview.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
