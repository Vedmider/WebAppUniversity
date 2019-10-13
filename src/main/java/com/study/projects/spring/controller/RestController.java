package com.study.projects.spring.controller;

import com.study.projects.domain.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class WebRestController {
    @Autowired
    private UniversityManager manager;
    private static final Logger logger = LoggerFactory.getLogger(WebRestController.class);

    @PostMapping("/api/put/lecture") // not finished
    public ResponseEntity<?> putLectureToDB(@RequestBody Map<String, String> jsonLectureBody, Errors errors) {
        String result = "";
        Object obj;
        University univ;
        Classroom classroom;
        Group group;
        Teacher teacher;
        String subject;
        Lecture lecture;
        LocalDateTime localDateTime;
        boolean isInsertOk;

        logger.info(" put Lecture to DB JSON string values: {}", jsonLectureBody.toString());

//        try {
//            obj = new JSONParser().parse(jsonLectureBody);
//        } catch (ParseException e) {
//            logger.error("Could not parse JSON in lecture add RestController.", e);
//            result += "Could not parse JSON";
//            return ResponseEntity.badRequest().body(result);
//        }

        if (errors.hasErrors()) {
            result = errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));
            result += "There were errors during sending request to server";
            return ResponseEntity.badRequest().body(result);
        }

//        JSONObject jsonObject = (JSONObject) obj;
        univ = manager.getById(Integer.parseInt(jsonLectureBody.get("universityId")));
        group = univ.getGroupById(Integer.parseInt(jsonLectureBody.get("groupName")));
        String[] prepareDate = (jsonLectureBody.get("date")).trim().split("\\D");
        int day = Integer.parseInt(prepareDate[0]);
        int month = Integer.parseInt(prepareDate[1]);
        int year = Integer.parseInt(prepareDate[2]);
        String[] prepareTime = (jsonLectureBody.get("time")).trim().split("\\D");
        int hours = Integer.parseInt(prepareTime[0]);
        int minutes = Integer.parseInt(prepareTime[1]);
        localDateTime = LocalDateTime.of(year, month, day, hours, minutes);
        teacher = univ.getTeacherById(Integer.parseInt(jsonLectureBody.get("teacher")));
        classroom = univ.getClassroomById(Integer.parseInt(jsonLectureBody.get("classroom")));
        subject = jsonLectureBody.get("subject");
        lecture = new Lecture(subject, classroom, group, teacher, localDateTime);
        isInsertOk = univ.addLecture(lecture);

        if (isInsertOk){
            result += "You successfully added lecture to university";
        } else {
            result += "Can not add lecture.\n Please check if teacher, group or classroom is vacant on this time";
        }

        return ResponseEntity.ok(result);
    }
}
