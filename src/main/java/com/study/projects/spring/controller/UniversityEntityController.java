package com.study.projects.spring.controller;

import com.study.projects.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/entity")
public class UniversityEntityController {

    @Autowired
    private UniversityManager manager;
    private static final Logger logger = LoggerFactory.getLogger(UniversityEntityController.class);


    @GetMapping("/{id}")
    public String getUniversityObjects(@PathVariable("id") int id, ModelMap model){
        University university = manager.getById(id);
        ArrayList<Student> students  = university.getStudents();
        System.out.println(students.size());
        ArrayList<Teacher> teachers = university.getTeachers();
        ArrayList<Classroom> classrooms = university.getClassrooms();
        ArrayList<Group> groups = university.getGroups();
        model.addAttribute("university", university);
        model.addAttribute("students", students);

        for (Student student:students){
            System.out.println("this is group name in student " + student.getGroup().getName() + "and Group ID " + student.getGroup().getId() + " AND GROUP " + student.getGroup());
        }

        model.addAttribute("teachers", teachers);
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("groups", groups);
        return "University/universityEntity";
    }

    @GetMapping("/add/student/{id}")
    public String addStudentPage(@PathVariable("id") int id, ModelMap model){
        ArrayList<Group> groups = manager.getById(id).getGroups();
        model.addAttribute("id", id);
        model.addAttribute("groups", groups);
        return "University/Student/add-student";
    }

    @PostMapping("/add/student/{id}")
    public String addStudent(@PathVariable("id") int id,
                             @RequestParam Map<String, String> params){

        University university = manager.getById(id);
        Student student = new Student(params.get("firstName").trim(), params.get("lastName").trim());
        if (params.containsKey("groupName")){
            Integer idGroup = Integer.parseInt( params.get("groupName"));
            System.out.println(params.get("groupName"));
            Group group = university.getGroupById(idGroup);
            student.setGroup(group);
        }
        manager.addStudentToUniversity(university, student);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/add/group/{id}")
    public String addGroupPage(@PathVariable("id") int id, ModelMap model){
        model.addAttribute("id", id);
        return "University/Group/add-group";
    }

    @PostMapping("/add/group/{id}")
    public String addGroup(@PathVariable("id") int id, @RequestParam("groupName") String groupName){
        Group group = new Group(groupName);
        manager.addGroupToUniversity(manager.getById(id), group);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/edit/student/{id}/{studentID}")
    public String updateStudentPage(@PathVariable("id") int id,
                                    @PathVariable("studentID") int studentID,
                                    ModelMap model){

        University univ = manager.getById(id);
        Student student = univ.getStudentById(studentID);
        String[] fullName = student.getFullName().trim().split(" ");
        ArrayList<Group> groups = univ.getGroups();
        model.addAttribute("id", id);
        model.addAttribute("studentID", studentID);
        model.addAttribute("firstName", fullName[0]);
        model.addAttribute("lastName", fullName[1]);
        model.addAttribute("groups", groups);
        model.addAttribute("defaultGroupID", student.getGroup().getId());
        return "University/Student/update-student";
    }

    @PostMapping("/edit/student/{id}/{studentID}")
    public String updateStudent(@PathVariable("id") int id,
                                @PathVariable("studentID") int studentID,
                                @RequestParam Map<String, String> params) {

        logger.info("updating Student with params: University ID {}, " +
                    "student ID {}, firstName {}, lastName {}, groupID {}",
                    id, studentID, params.get("firstName"), params.get("lastName"),
                    params.get("groupName"));
        University univ = manager.getById(id);
        Student student = new Student(params.get("firstName").trim(), params.get("lastName").trim());
        if (params.containsKey("groupName")){
            Integer idGroup = Integer.parseInt( params.get("groupName"));
            Group group = univ.getGroupById(idGroup);
            student.setGroup(group);
        }
        System.out.println("Final Student parametrs : " + student.getFullName() + " " + student.getGroup().getName());
        manager.updateStudent(univ, student);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/delete/student/{id}/{studentID}")
    public String deleteStudent(@PathVariable("id") int id,
                                @PathVariable("studentID") int studentID){

        University univ = manager.getById(id);
        Student stud = univ.getStudentById(studentID);
        manager.removeStudentFromUniversity(manager.getById(id), stud);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/edit/group/{id}/{groupID}")
    public String editGroupPage (@PathVariable("id") int id,
                                 @PathVariable("groupID") int groupID,
                                 ModelMap model) {

        University univ = manager.getById(id);
        model.addAttribute("id", id);
        model.addAttribute("groupID", groupID);
        model.addAttribute("group", univ.getGroupById(groupID));
        return "University/Group/update-group";
    }

    @PostMapping("/edit/group/{id}/{groupID}")
    public String editGroup(@PathVariable("id") int id,
                            @PathVariable("groupID") int groupID,
                            @RequestParam Map<String, String> params){

        Group group = new Group( params.get("groupName") );
        group.setId(groupID);
        manager.updateGroup(manager.getById(id), group);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/delete/group/{id}/{groupID}")
    public String deleteGroup(@PathVariable("id") int id,
                              @PathVariable("groupID") int groupID) {

        Group group = new Group("");
        group.setId(groupID);
        manager.removeGroupFromUniversity(manager.getById(id), group);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/add/teacher/{id}")
    public String addTeacherPage(@PathVariable("id") int id, ModelMap model){

        model.addAttribute("id", id);
        return "University/Teacher/add-teacher";
    }

    @PostMapping("/add/teacher/{id}")
    public String addTeacher(@PathVariable("id") int id, @RequestParam Map<String, String> params){

        Teacher teacher = new Teacher(params.get("firstName").trim(), params.get("lastName").trim());
        manager.addTeacherToUniversity(manager.getById(id), teacher);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/delete/teacher/{id}/{teacherID}")
    public String deleteTeacher(@PathVariable("id") int id, @PathVariable("teacherID") int teacherID){

        University univ = manager.getById(id);
        Teacher teacher = univ.getTeacherById(teacherID);
        manager.removeTeacherFromUniversity(univ, teacher);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/edit/teacher/{id}/{teacherID}")
    public String updateTeacherPage(@PathVariable("id") int id,
                                    @PathVariable("teacherID") int teacherID,
                                    ModelMap model){

        University univ = manager.getById(id);
        Teacher teacher = univ.getTeacherById(teacherID);
        String[] fullName = teacher.getFullName().trim().split(" ");
        model.addAttribute("firstName", fullName[0]);
        model.addAttribute("lastName", fullName[1]);
        model.addAttribute("id", id);
        model.addAttribute("teacherID", teacherID);
        return "University/Teacher/update-teacher";
    }

    @PostMapping("/edit/teacher/{id}/{teacherID}")
    public String updateTeacher(@PathVariable("id") int id,
                                @PathVariable("teacherID") int studentID,
                                @RequestParam Map<String, String> params) {

        University univ = manager.getById(id);
        Teacher teacher = new Teacher(params.get("firstName").trim(), params.get("lastName").trim());
        manager.updateTeacher(univ, teacher);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/add/classroom/{id}")
    public String addClassroomPage(@PathVariable("id") int id, ModelMap model){
        model.addAttribute("id", id);
        return "University/Classroom/add-classroom";
    }

    @PostMapping("/add/classroom/{id}")
    public String addClassroom(@PathVariable("id") int id,
                               @RequestParam Map<String, String> params) {

        University univ = manager.getById(id);
        Classroom classroom = new Classroom(Integer.parseInt(params.get("classNumber")));
        manager.addClassroomToUniversity(univ, classroom);
        return "redirect:/entity/" + id;
    }

    @GetMapping("/delete/classroom/{id}/{classID}")
    public String deleteClassroom(@PathVariable("id") int id,
                                @PathVariable("classID") int classID){

        Classroom classroom = new Classroom(0);
        classroom.setId(classID);
        manager.removeClassroomFromUniversity(manager.getById(id), classroom);
        return  "redirect:/entity/" + id;
    }

    @GetMapping("/edit/classroom/{id}/{classID}")
    public String updateClassroomPage(@PathVariable("id") int id,
                                      @PathVariable("classID") int classID,
                                      ModelMap model){

        Classroom classroom = manager.getById(id).getClassroomById(classID);
        System.out.println("Get Classroom from db: ID " + classID + " number " + classroom.getNumber() );
        model.addAttribute("id", id);
        model.addAttribute("classID", classID);
        model.addAttribute("classroom", classroom);
        return "University/Classroom/update-classroom";
    }

    @PostMapping("/edit/classroom/{id}/{classID}")
    public String updateClassroom(@PathVariable("id") int id,
                                  @PathVariable("classID") int classID,
                                  @RequestParam Map<String, String> params){

        University univ = manager.getById(id);
        Classroom classroom = new Classroom(Integer.parseInt(params.get("classNumber")));
        classroom.setId(classID);
        manager.updateClassroom(univ, classroom);
        return "redirect:/entity/" + id;
    }

    @PostMapping("/lecture/{id}")
    public String getLecturesPage(@RequestParam("datepicker") String date,
                                  @PathVariable("id") int id,
                                  ModelMap model){

        University univ = manager.getById(id);
        String[] prepareDate = date.trim().split("-");
        int day = Integer.parseInt(prepareDate[0]);
        int month = Integer.parseInt(prepareDate[1]);
        int year = Integer.parseInt(prepareDate[2]);
        LocalDate localDate = LocalDate.of(year, month, day);
        DaySchedule daySchedule = univ.getYearSchedule().get(localDate);
        model.addAttribute("daySchedule", daySchedule);
        model.addAttribute("id", id);
        model.addAttribute("date", date);

        return "University/Lecture/lectures";
    }

    @GetMapping("/lecture/add/{id}")
    public String addLecturePage(@PathVariable("id") int id,
                                 @RequestParam("date") String date,
                                 ModelMap model){

        University univ = manager.getById(id);
        model.addAttribute("classrooms", univ.getClassrooms());
        model.addAttribute("groups", univ.getGroups());
        model.addAttribute("teachers", univ.getTeachers());
        model.addAttribute("id", id);
        model.addAttribute("date", date);
        return "University/Lecture/add-lecture";
    }


}
