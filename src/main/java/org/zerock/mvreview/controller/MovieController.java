package org.zerock.mvreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mvreview.dto.MovieDTO;
import org.zerock.mvreview.dto.PageRequestDTO;
import org.zerock.mvreview.dto.PageResultDTO;
import org.zerock.mvreview.service.MovieService;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //////////////////////////////////////////////// 영화 등록 페이지 요청
    @GetMapping("/register")
    public void registerGet(){
        log.info(">>>>>>>>>>>>>>>>> [C] registerGet()...");
    }



    //////////////////////////////////////////////// 영화 등록 작업 요청
    @PostMapping("/register")
    public String registerPost(MovieDTO movieDTO, RedirectAttributes redirectAttributes){

        log.info(">>>>>>>>>>>>>>>>> [C] registerPost()...");

        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg",mno);

        return "redirect:/movie/list";
    }


    //////////////////////////////////////////////// 목록 페이지 요청
    @GetMapping("/list")
    public void getList(PageRequestDTO dto, Model model){

        log.info(">>>>>>>>>>>>>>>>>> [C] getList() ... ");

        PageResultDTO<MovieDTO, Object[]> result = movieService.getList(dto);

        log.info("result = "+result);

        model.addAttribute("result",result);
    }



    //////////////////////////////////////////////// 조회 페이지 요청
    @GetMapping({"/read","/modify"})
    public void getRead(Long mno,@ModelAttribute PageRequestDTO pageRequestDTO,Model model){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] getRead() ");

        //서비스 실행
        MovieDTO dto = movieService.getMovie(mno);

        //반환데이터 model에 저장
        model.addAttribute("dto",dto);

    }


    //////////////////////////////////////////////// 수정 작업 요청
    @PostMapping("/modify")
    public String modify(MovieDTO dto, @ModelAttribute PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] modify() ");

        return "redirect:/movie/read";
    }


    //////////////////////////////////////////////// 삭제 작업 요청
    @PostMapping("/remove")
    public String remove(@RequestParam Long mno, RedirectAttributes redirectAttributes){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] remove() ");

        //삭제 서비스 메서드 호출
        movieService.remove(mno);

        //삭제한 게시글 번호 저장
        redirectAttributes.addFlashAttribute("msg",mno);

        return "redirect:/movie/list";
    }

    //////////////////////////////////////////////// 이미지 데이터 삭제 작업 요청
    @PostMapping("/removeImg")
    @ResponseBody
    public ResponseEntity<Boolean> removeImg(String uuid){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] removeImg() ");
        log.info("uuid is ..... >>>> "+uuid);

        //넘어온 uuid는 uuid_이미지이름.jpg로 이루어져 있기 때문에 uuid만 잘라주기
        String[] arr = uuid.split("_");
        String nuuid = arr[0];
        log.info(">>>>>>>>>>>>>>>>>>>>> arr[0] = "+nuuid);

        //이미지 테이블에서 같은 uuid를 삭제하는 서비스 메서드 호출
        boolean result = movieService.removeImg(nuuid);

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] removeImg() -- complete img data delete !!!  ");

        return new ResponseEntity<>(true, HttpStatus.OK);

    }


}
