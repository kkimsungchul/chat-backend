package com.sungchul.findchat.chat.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


@Slf4j
@AllArgsConstructor
@Service("dateService")
public class DateService {
    /*yyyymmdd 로 현재 날짜 리턴*/
    public static String getDate() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }

    /*yyyy 로 현재 연도 리턴*/
    public static String getYear() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }
    /*HHmmss 로 현재 시간 리턴*/
    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }
    /*지정한 형식으로 출력*/
    public static String getTime(String strformat){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strformat);
        String nowString = now.format(dateTimeFormatter);
        return nowString;
    }

    /*현재 요일을 반환*/
    public static int getWeekDay(){
        Calendar cal = Calendar.getInstance();
        //1 일, 2월,3화,4수,5목,6,금,7토
        int weebDay = cal.get(Calendar.DAY_OF_WEEK)-1;
        return weebDay;

    }







    /*
     * 현재날짜에 변수로 받은 일 또는 년도를 더해서 리턴
     * @Param nType : 1 : 년, 2 : 월 , 5 : 일
     * */
    public String getTime(String strformat,int add,int nType ){
        String value = "";
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(strformat);


        Calendar today = Calendar.getInstance();
        today.add(nType, add);
        value = formatter.format(today.getTime());
        return value;
    }
}
