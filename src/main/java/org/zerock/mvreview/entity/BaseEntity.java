package org.zerock.mvreview.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //이 추상클래스는 엔티티로 만들어지지 않고 이 클래스를 상속한 엔티티에서 컬럼으로 만들어진다
@Getter //엔티티클래스는 함부로 수정되면 안 되기 때문에 getter만 만든다
@EntityListeners(value = {AuditingEntityListener.class}) //애플리케이션 클래스 단에 @EnableJpaAuditing을 달아줘야 감지 리스너가 작동함
abstract class BaseEntity {

    /*모든 엔티티 클래스에 공통적으로 사용되는 컬럼 작성*/


    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate //가장 최근에 수정된 값을 DB에 저장
    private LocalDateTime modDate;



}
