package com.gaeasoft.project.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class AddressApiService {
	
	private static final String confmKey = "devU01TX0FVVEgyMDI0MDYxMjE3MDc0ODExNDgzODI=";
	
	public ResponseEntity<String> getAddressApi(HttpServletRequest req, ModelMap model) {
		String currentPage = req.getParameter("currentPage");
		String countPerPage = req.getParameter("countPerPage");
		String resultType = req.getParameter("resultType");
		String keyword = req.getParameter("keyword");
		String callback = req.getParameter("callback");
		String apiUrl = null;
        
		try {
			apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage
				             +"&countPerPage="+countPerPage
				             +"&keyword="+URLEncoder.encode(keyword,"UTF-8")
				             +"&confmKey="+ confmKey
				             +"&resultType="+resultType;
		  
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(callback  + "{URL 인코딩 실패: " + e.getMessage() + "}");
		}
          
        BufferedReader br = null;
        try {
        	URL url = new URL(apiUrl);
         	 
          	br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
          	StringBuilder sb = new StringBuilder();
            String tempStr;
             
            while ((tempStr = br.readLine()) != null) {
            	sb.append(tempStr); // 응답결과 JSON 저장
            }
            
     		return ResponseEntity.ok()
     						.header("Content-Type", "application/json")
     						.body(callback + "(" + sb.toString() + ");"); // 응답결과 반환
     		 
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        					.body(callback + "{response 데이터 읽기 실패: " + e.getMessage() + "}");
          
        } finally {
        	if (br != null) {
        		try {
        			br.close();
                  
                } catch (Exception e) {
                	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                					.body(callback +"{BufferedReader 닫기 실패: " + e.getMessage() + "}");
                }
         	}
        }
    }

}