package com.sungchul.findchat.chat.service;


import com.sungchul.findchat.chat.common.DateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Service("ChatService")
public class ChatService {

    @Value("${spring.servlet.multipart.location}")
    private String fileUploadPath;
    /**
     * fildUpload , 파일 업로드
     * @param file
     * @return String
     * */
    public String fileUpload(MultipartFile file){
        String saveFileName="";
        String retunrMessage ="";
        String fileName = file.getOriginalFilename();
        saveFileName = renameFile(file.getOriginalFilename());
        if(!extCheck(fileName)){
            retunrMessage = "파일은 txt 확장자만 업로드 가능합니다.";
            return retunrMessage;
        }
        //파일 저장
        //파일명 변경

        retunrMessage =  "파일 업로드 성공";
        try{
            file.transferTo(new File(saveFileName));
        }catch (IOException e){
            retunrMessage =  "파일 업로드 실패";
            return retunrMessage;
        }
        return saveFileName;
    }

    /**
     * getFindText , 선택한 텍스트 파일을 분석하여 정보를 리턴함
     * @param saveFileName
     * @return HashMap<String,Object>
     * */
    public HashMap<String,Object> getFindText(String saveFileName){
        HashMap<String,Object> resultMap = new HashMap<>();
        File file = new File(fileUploadPath , saveFileName);
        BufferedReader br = null;
        int count = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        String killUser=null;
        //채팅에 참여한 사용자 목록
        String memberName;
        ArrayList<String> memberList = new ArrayList<String>();
        //사용자명,채팅횟수 기록
        HashMap<String,Integer> talkCountMap = new HashMap<String, Integer>();
        HashMap<String,Integer> killCountMap = new HashMap<String, Integer>();
        HashMap<String,String> killUserListMap = new HashMap<String, String>();
        List<Map.Entry<String, Integer>> tokeList =null;
        List<Map.Entry<String, Integer>> killList =null;
        int totalLine=0;

        int memberCount=0;
        try {
            //사용자 목록 추출
            br = new BufferedReader(new FileReader(file));
            while((line=br.readLine()) != null) {
                int t=line.indexOf("]");
                if(t>0){
                    memberName = line.substring(0,t+1);

                    for(String name : memberList){
                        if(name.equals(memberName)){
                            memberCount++;
                        }
                    }
                    if(memberCount==0){
                        memberList.add(line.substring(0,t+1));
                    }
                    memberCount=0;
                }
            }

            //사용자세팅
            for(int i=0;i<memberList.size();i++){
                talkCountMap.put(memberList.get(i),0);
                killCountMap.put(memberList.get(i),0);
                killUserListMap.put(memberList.get(i),"");
            }
            br = new BufferedReader(new FileReader(file));


            //대화횟수저장
            while((line=br.readLine()) != null) {

                if(line.indexOf("나갔습니다")>=0){
                    killCountMap.put(killUser,killCountMap.get(killUser)+1);
                    int a=line.indexOf("님이");
                    if(killUserListMap.get(killUser).length()>0){
                        killUserListMap.put(killUser, killUserListMap.get(killUser) +" ,"  +  line.substring(0,a+1));
                    }else{
                        killUserListMap.put(killUser,killUserListMap.get(killUser) + line.substring(0,a+1));
                    }

                }

                for(int i=0;i<memberList.size();i++){
                    if(line.indexOf(memberList.get(i))>=0){
                        talkCountMap.put(memberList.get(i),talkCountMap.get(memberList.get(i))+1);
                        totalLine++;
                        killUser = memberList.get(i);
                    }
                }
            }

            //가나다순으로 memberList 정렬
            memberList.sort(Comparator.naturalOrder());

            //대화내용 순으로 talkCount 정렬
            tokeList = new LinkedList<>(talkCountMap.entrySet());
            tokeList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            //사용자를 쫓아낸 순으로 killCount 정렬
            killList = new LinkedList<>(killCountMap.entrySet());
            killList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(br != null) {
                    br.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resultMap.put("tokeList" , tokeList);
        resultMap.put("killList" , killList);
        resultMap.put("memberList" , memberList);
        resultMap.put("memberCount" , memberList.size());
        resultMap.put("totalLine" , totalLine);
        return resultMap;
    }


    /**
     * extCheck , 파일의 확장자 체크
     * @param fileName
     * @return boolean
     * */
    private boolean extCheck(String fileName){

        String ext = fileName.substring(fileName.lastIndexOf("."));
        if("txt".equals(ext) || "TXT".equals(ext)){
            return false;
        }else{
            return true;
        }
    }

    public double getPercent(double totalLine , double talkCount){
        return Math.round(talkCount/totalLine*1000000)/10000.0;
    }

    /**
     * renameFile , 파일명 변경
     * @param fileName
     * @return String
     * */
    public String renameFile(String fileName){
        //파일명 변경
        String saveFileName = makeFileName(fileName);
        try{
            File uploadFile =  new File(fileUploadPath , fileName);
            File saveFile = new File(fileUploadPath , saveFileName);
            uploadFile.renameTo(saveFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return saveFileName;
    }

    /**
     * makeFileName , 파일명앞에 날짜를 붙여서 리턴
     * @param fileName
     * @return String
     * */
    public String makeFileName(String fileName){

        String newFileName = DateService.getDate()+"_"+DateService.getTime()+"_"+fileName;

        System.out.println("### newFileName : " + newFileName);
        return newFileName;
    }
}
