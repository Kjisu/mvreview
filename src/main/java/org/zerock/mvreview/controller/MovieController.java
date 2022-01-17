package org.zerock.mvreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public void getRead(Long mno,@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,Model model){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] getRead() ");

        //서비스 실행
        MovieDTO dto = movieService.getMovie(mno);

        //반환데이터 model에 저장
        model.addAttribute("dto",dto);

    }
}
