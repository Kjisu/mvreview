package org.zerock.mvreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mvreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController //파일 업로드 결과를 화면이 아닌 JSON타입으로 전달할꺼니까
@Log4j2
public class UploadController {

    //업로드된 파일이 저장되는 곳
    @Value("${org.zerock.upload.path}") //application.properties의 변수 값 가져옴
    private String uploadPath;

    ////////////////////////////////// 이미지 업로드 작업
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){ //배열로 받아야 여러개 업로드되는 이미지도 처리할 수 있다

        log.info("======== Here is UploadController.java");

        //결과 데이터 담을 객체 list 생성
        List<UploadResultDTO> dtoList = new ArrayList<>();


        for(MultipartFile multipartFile:uploadFiles){

        //업로드된 파일을 저장할 때 고려해야 하는 것

            //1.파일확장자가 '이미지'인지 체크
                if(multipartFile.getContentType().startsWith("image") == false){
                    log.warn("이미지 파일만 업로드 가능합니다.");
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }

            //2.실제 파일 이름만 분리
            //IE,Edge에서 업로드 되는 경우의 파일 이름 = 전체 경로
            //Chrome  "   = 단순히 파일 이름

                //원래 파일 이름
                String originalName = multipartFile.getOriginalFilename();
                //파일 이름만 추출
                String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);
                log.info("fileName = "+ fileName);



            //3.날짜(년/월/일) 폴더 생성
                String folderPath = makeFolder();

            //4.고유한 파일 이름 만들기(동일한 파일이름을 가진 파일 업로드 시 삭제 되는걸 방지하기 위함)

                //uuid 생성
                String uuid = UUID.randomUUID().toString();

                //저장할 파일 이름 중간에 "_"를 이용하여 구분
                String uqFileName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
                log.info(">>>>>>>>>>>>> uqFileName = "+uqFileName);


            //5.업로드 된 파일 저장 (+썸네일 이미지 파일 생성해서 저장하는 작업까지)
                Path savePath = Paths.get(uqFileName);
                try {
                    //넘어온 이미지 파일을 이 경로에 저장
                    multipartFile.transferTo(savePath);

                    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> 이미지 파일 저장 완료 !! ");

                    //섬네일 이미지 만들기 - 1. 섬네일 이미지 제목 생성
                    String thumbNailImageName = uploadPath+File.separator+folderPath+File.separator+"s_"+uuid+"_"+fileName;

                    //2.섬네일 파일 객체 생성
                    File thumbNailFile = new File(thumbNailImageName);

                    //3.섬네일 파일 생성
                    //Thumbnailator.createThumbnail(파일 저장위치,섬네일파일,100,100);
                    Thumbnailator.createThumbnail(savePath.toFile(),thumbNailFile,100,100);

                    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> 썸네일 이미지 파일 저장 완료 !! ");

                    //리턴할 데이터
                    dtoList.add(new UploadResultDTO(fileName,uuid,folderPath.replace("\\","/")));

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }




    ///////////////////////// URL 인코딩된 파일이름을 파라미터로 받아서, 해당 파일을 byte[]로 만들어 브라우저로 전송
    @GetMapping("/display")
    public ResponseEntity<byte[]> getImageFile(String fileName){

        ResponseEntity<byte[]> result = null;

        try {
            //인코딩된 URL을 String타입으로 변환
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");
            log.info("fileName = "+srcFileName); //2022/01/13/58195f7a-532d-42ba-9aef-15bf7656ff95_������������.JPG

            //File 객체 생성 , 이미지 꺼내기
            File file = new File(uploadPath+File.separator+srcFileName.replace("/","\\"));
            log.info("file is >>>>> "+fileName);

            HttpHeaders headers = new HttpHeaders();

            //MIME타입 처리
            //파일 확장자에 따라 브라우저에 전송하는 MIME타입이 달라져야 하는 문제는 java.nio.file 패키지의 Files.probeContentType()을 이용하여 처리
            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            //파일 데이터 처리, 스프링에서 제공하는 FileCopyUtils 이용
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;

    }


    ///////////////////////////////////////////////////////// 이미지 삭제 작업
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){

        String srcFileName = null;

        try {
            //1.원본파일제거
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath+File.separator+srcFileName);
            boolean result = file.delete();

            //2.섬네일 파일 제거
            File thumbFile = new File(file.getParent(),"s_"+file.getName());
            log.info(" =============== file.getParanet()>>" + file.getParent()); //D:/upload/2022/01/13 (실제로는 /가 \로 되어있음, 에러떠서 변경함)
            log.info("======= file.getName() >> "+file.getName()); //f4de1ec8-488c-4d99-a4cf-3297141ac3c1_����Ȩ.JPG
            result = thumbFile.delete();

            return new ResponseEntity<>(result,HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //날짜 폴더를 생성하고 (년/월/일) 폴더 경로 리턴
    private String makeFolder(){

        //오늘 날짜 생성
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        log.info(">>>>>>>>>>>>>>>>>>>>> folderPath is = "+str);

        //"/"를 "\"로 변경
        String folderPath = str.replace("/", File.separator);
        log.info(">>>>>>>>>>>>>>>>>>>>> ModifiedfolderPath is = "+folderPath);

        //폴더 만들기
        File file = new File(uploadPath,folderPath);
        if(file.exists() == false){
            file.mkdirs();
        }
        return folderPath;
    }

}
