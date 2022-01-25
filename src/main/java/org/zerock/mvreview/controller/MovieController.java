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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

        log.info("update ing .... data = "+dto);
        //MovieDTO(mno=154, title=11111, imageDTOList=[MovieImageDTO(uuid=null, imgName=null, path=null), MovieImageDTO(uuid=9d9c1f7b-5aba-4e95-b3e5-68f8e368b633, img

        Long result = movieService.update(dto);

        //상세 페이지 정보 추가
        redirectAttributes.addAttribute("mno",dto.getMno());
        redirectAttributes.addAttribute("page",pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type",pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword",pageRequestDTO.getKeyword());

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
    public ResponseEntity<Boolean> removeImg(String targetFile){

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] removeImg() ");
        log.info("넘어온 데이터 is ..... >>>> "+targetFile);

        //넘어온 파일 이름 인코딩
        try {
            String encodedName = URLDecoder.decode(targetFile,"UTF-8");

            //이미지 이름만 잘라내기
            String[] arr = encodedName.split("_");
            String imgName = arr[1];

            //서비스 호출
            movieService.removeImg(imgName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>> [C] removeImg() -- complete img data delete !!!  ");

        return new ResponseEntity<>(true, HttpStatus.OK);

    }


}
