package org.zerock.mvreview.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@ToString
public class PageResultDTO<DTO,EN> {

    /* 제네릭 클래스로 만들기
    *  - 유연성 있는 클래스가 된다
    *  - 어떤 종류의 Page<E>타입이 생성되더라도 PageResultDTO를 사용하여 처리할 수 있다.
    * */

    /* PageResultDTO
    *  - jpa처리결과 반환되는 page<엔티티>를 dto로 변환하여 초기화한다
    *  - 뷰단에서 사용될 페이지 관련 데이터들을 저장하는 상자
    * */

    private List<DTO> dtoList;//DTO리스트

    private int totalPage; //총페이지수
    private int page; //현재 페이지 번호
    private int size; //한 페이지당 게시물 갯수
    private int start, end; //화면에서 시작 페이지 번호, 끝 페이지 번호
    private boolean prev,next; //이전,다음 링크 여부
    private List<Integer> pageList; //페이지 번호 목록

    //객체가 생성될 때 엔티티를 dto타입으로 바꿔서 list에 담는다
    public PageResultDTO(Page<EN> result,Function<EN,DTO> fn) {

        //Page<Object[]> 엔티티 -> dto타입으로 변환해서 리스트에 저장
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        //페이지 관련 데이터 초기화
        this.totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    //초기화작업 메서드
    private void makePageList(Pageable pageable){

        this.size = pageable.getPageSize();
        this.page = pageable.getPageNumber()+1;//0부터 시작하기 때문에 1을 더해줘야 함
        //끝 페이지 번호 (임시)
        int tempEnd = (int)(Math.ceil(page/10.0))*10;//페이지 목록을 1~10까지 11~20..이런식으로 하고 싶으니까 10을 곱한 것 !!
        //시작 페이지 번호
        this.start = tempEnd - 9;
        //끝 페이지 번호(진짜)
        this.end = totalPage>tempEnd? tempEnd:totalPage;
        //이전,다음 표시
        this.prev = start > 1;
        this.next = totalPage>tempEnd;
        //페이지 번호 목록
        this.pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

    }

}
