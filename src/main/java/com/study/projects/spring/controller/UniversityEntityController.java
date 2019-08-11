package com.study.projects.spring.controller;

import com.study.projects.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/entity")
public class UniversityEntityController {

    @Autowired
    private UniversityManager manager;

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
    public String addStudent(@PathVariable("id") int id, @RequestParam Map<String, String> params){
        University university = manager.getById(id);
        Student student = new Student(params.get("firstName"), params.get("lastName"));
        if (params.containsKey("groupName")){
            Integer idGroup = Integer.parseInt( params.get("groupName"));
            //System.out.println(params.get("groupName"));
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
}
