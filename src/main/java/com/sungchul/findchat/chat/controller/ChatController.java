package com.sungchul.findchat.chat.controller;


import com.sungchul.findchat.chat.common.ResponseAPI;
import com.sungchul.findchat.chat.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.ServerError;
import java.util.HashMap;

@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "ChatController")
public class ChatController {

    ChatService chatService;


    @RequestMapping(value="/api/sso/user/check/insideCheck" , method = RequestMethod.POST)
    public  ResponseEntity<String> createTest(HashMap<String,Object> map){
        System.out.println("### map : " + map);

        //return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/test")
    public String test(){
        return "hihi";
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = ResponseAPI.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.Forbidden.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class),
    })
    @ApiOperation(value="파일 분석", notes="사용자의 txt 파일을 분석함")
    @PostMapping(value="/chat")
    public ResponseEntity<ResponseAPI> uploadFile(MultipartFile file) throws IllegalStateException, IOException {
        ResponseAPI responseAPI = new ResponseAPI();
        String returnMessage;
        try{
            if( !file.isEmpty() ) {
                returnMessage = chatService.fileUpload(file);
                responseAPI.setData(chatService.getFindText(returnMessage));
            }else{
                returnMessage="파일이 없습니다.";
                responseAPI.setMessage(returnMessage);
            }
        }catch (Exception e){
            returnMessage="파일이 없습니다.";
            responseAPI.setMessage(returnMessage);
        }



        return new ResponseEntity(responseAPI , HttpStatus.OK);
    }
}
