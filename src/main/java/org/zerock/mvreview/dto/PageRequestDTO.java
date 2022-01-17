package org.zerock.mvreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@ToString
//@Builder
//@AllArgsConstructor
public class PageRequestDTO {

    /*PageRequestDTO의 역할
    * 1.페이징 처리할 때 필요한 데이터(파라미터로 넘어옴)를 저장 상자
    * 2.JPA에서 사용될 Pageable객체 생성
    *   */

    private int page;//현재 페이지 번호
    private int size;//한 페이지당 출력할 게시글 갯수
    private String type;//검색유형
    private String keyword;//검색 키워드


    //리스트 페이지 처음 들어갈 때는 페이지 정보가 없는 상태니까 초기화 작업 반드시 필요
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    //Pageable 객체 반환
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1,size,sort);
    }
}
