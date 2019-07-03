package com.study.projects.spring.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.study.projects.domain.UniversityManager;
import com.study.projects.domain.University;

@Controller
public class UniversityController {
	
	private UniversityManager manager = new UniversityManager();
	
	@RequestMapping(value="/", method=RequestMethod.GET)
    public String getOrderPage(Model model) {
		
		ArrayList<University> universityList = manager.getAll();
		model.addAttribute("universities", universityList);
        return "manager/manager-list";
    }

}
