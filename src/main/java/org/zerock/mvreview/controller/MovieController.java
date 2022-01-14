package org.zerock.mvreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mvreview.dto.MovieDTO;
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
}
