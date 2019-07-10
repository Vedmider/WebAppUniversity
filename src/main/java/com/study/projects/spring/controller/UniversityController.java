package com.study.projects.spring.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.study.projects.domain.UniversityManager;
import com.study.projects.domain.University;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UniversityController {

	@Autowired
	private UniversityManager manager;

	@Bean
	UniversityManager manager(){
		return new UniversityManager();
	}
	
	@GetMapping("/manager")
    public String getUniversitiesListPage(Model model) {
		
		ArrayList<University> universityList = manager.getAll();
		model.addAttribute("universities", universityList);
        return "manager-list";
    }

	@GetMapping("/")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

}
