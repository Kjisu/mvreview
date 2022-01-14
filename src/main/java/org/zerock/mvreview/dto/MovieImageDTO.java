package org.zerock.mvreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieImageDTO {

    //업로드된 영화 이미지에 관한 정보 저장하는 객체

    private String uuid;

    private String imgName;

    private String path;

    public String getImageURL(){

        try {
            //왜 인코딩 처리 해야할까..?
            return URLEncoder.encode(path+"/"+uuid+"_"+imgName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
