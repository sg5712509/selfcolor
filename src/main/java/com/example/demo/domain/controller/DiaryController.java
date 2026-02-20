/*package com.example.demo.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiaryController {

    @GetMapping("/diary")
    public String diary(@RequestParam(required = false) String date, Model model) {
        model.addAttribute("date", date); // 없으면 null
        return "diary"; // diary.html
    }
}*/

package com.example.demo.domain.controller;

import com.example.demo.domain.diary.Diary;
import com.example.demo.domain.diary.DiaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryMapper diaryMapper;

    // ✅ (A) "일기쓰러 가기" 페이지: 날짜 선택 + 내용 표시/작성
    @GetMapping("/write")
    public String write(@RequestParam(value = "date", required = false) LocalDate date,
                        Model model) {

        LocalDate target = (date != null) ? date : LocalDate.now();
        Diary diary = diaryMapper.findByDate(target);

        // diary 없으면 빈 객체로 폼 띄우기
        if (diary == null) {
            diary = new Diary();
            diary.setDiaryDate(target);
            diary.setTitle("");
            diary.setContent("");
            model.addAttribute("isNew", true);
        } else {
            model.addAttribute("isNew", false);
        }

        model.addAttribute("targetDate", target);
        model.addAttribute("diary", diary);
        return "diary/write"; // templates/write.html
    }

    // ✅ 저장 (없으면 INSERT, 있으면 UPDATE)
    @PostMapping("/write")
    public String save(@RequestParam("diaryDate") LocalDate diaryDate,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content) {

        Diary exist = diaryMapper.findByDate(diaryDate);
        if (exist == null) {
            diaryMapper.insert(diaryDate, title, content);
        } else {
            diaryMapper.update(diaryDate, title, content);
        }
        return "redirect:/diary/write?date=" + diaryDate;
    }

    // ✅ (B) "다이어리 쓰기" 달력 페이지
    @GetMapping("/calendar")
    public String calendar(@RequestParam(value = "year", required = false) Integer year,
                           @RequestParam(value = "month", required = false) Integer month,
                           Model model) {

        LocalDate now = LocalDate.now();
        int y = (year != null) ? year : now.getYear();
        int m = (month != null) ? month : now.getMonthValue();

        YearMonth ym = YearMonth.of(y, m);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Diary> diaries = diaryMapper.findByMonth(start, end);

        // 날짜별 빠른 조회용 Map
        Map<LocalDate, Diary> diaryMap = new HashMap<>();
        for (Diary d : diaries) diaryMap.put(d.getDiaryDate(), d);

        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("yearMonth", ym);
        model.addAttribute("startDow", start.getDayOfWeek().getValue() % 7); // 일=0
        model.addAttribute("daysInMonth", ym.lengthOfMonth());
        model.addAttribute("diaryMap", diaryMap);

        return "diary/calendar"; // templates/calendar.html
    }
}