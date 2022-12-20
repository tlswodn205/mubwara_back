package site.metacoding.finals.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import site.metacoding.finals.dto.reservation.ReservationReqDto.AnalysisDateReqDto;
import site.metacoding.finals.dto.reservation.ReservationRespDto.AnalysisWeekRespDto;

@Slf4j
public class DateTimeTest {

    @Test
    public void LocaldateTest() {
        String todayStr = "20221205";
        AnalysisDateReqDto dto = new AnalysisDateReqDto();
        dto.setDate(todayStr);

        LocalDate date = dto.toLocalDate();
        log.debug("디버그 파싱 : " + date);

        String todayWeek = date.getDayOfWeek().toString();
        log.debug("디버그 요일 : " + todayWeek);

        // LocalDate testdate = date.plusDays(1);
        log.debug("디버그 일 :" + date.plusDays(1).toString());
        log.debug("더한 요일 : " + date.getDayOfWeek().toString());
        // log.debug("테스트 : " + testdate);

        // ------------------------------------------------
        int i = 1;
        int num = 0;
        while (i < 5) {
            i++;
            num++;

            log.debug("잘 더해지는가" + num);

            log.debug(date.plusDays(num).getDayOfWeek().toString());

            if (date.plusDays(num).getDayOfWeek().toString().equals("SUNDAY")) {
                break;
            }
        }

    }

    @Test
    public void datetimeTest() throws ParseException {
        String todays = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDate.now());
        int today = Integer.parseInt(todays);

        log.debug("디버그 : " + today);

    }

    @Test
    public void CalendarTest() throws Exception {
        // 캘린더 현재
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        System.out.println("current: " + df.format(new Date()));

        // 캘린더 연산
        cal.add(Calendar.MONTH, 2);
        cal.add(Calendar.DATE, -3);
        System.out.println("after: " + df.format(cal.getTime()));

        // 문자열을 캘린더로 활용
        String endDate = "20221128";
        Date endResult = df.parse(endDate);
        String formattend = df.format(endResult);
        System.out.println("문자열을 데이트로 " + formattend);
    }

    public void 데이트리스트날짜전체출력로직() {
        // log.debug("디버그 : " + " 서비스 진입");

        // DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        // Shop shopPS = ShopRespository.findById(dto.getShopId())
        // .orElseThrow(() -> new RuntimeException("잘못된 가게 요청입니다"));

        // // 필요 데이터 목록
        // // 오늘 날짜
        // Calendar today = Calendar.getInstance();
        // today.setTime(new Date());

        // // 가져온 엔드 날짜
        // Calendar endDay = Calendar.getInstance();
        // Date endDatePS = formatter.parse(shopPS.getSetReservationDate());
        // endDay.setTime(endDatePS);

        // // 데이트 리스트 생성
        // DateFormat df = new SimpleDateFormat("yyyyMMdd");

        // List<String> ableDate = new ArrayList<>();
        // while (true) {
        // today.add(Calendar.DATE, 1);
        // ableDate.add(df.format(today.getTime()));

        // if (today.equals(endDay)) {
        // break;
        // }
        // }
    }

}
