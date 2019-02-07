package com.bit.geha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.geha.criteria.SearchCriteria;
import com.bit.geha.dao.SearchDao;
import com.bit.geha.dto.SearchDto;

@Controller
public class SearchController {

	@Autowired
	private SearchDao dao;
	
	@RequestMapping("/search")
	public String search() {
		return "search";
	}
	
	@GetMapping("/allgehainfo")
	@ResponseBody
	public List<SearchDto> searchapi(SearchCriteria sc){
		return dao.listGeha();
	}
	
	@GetMapping("/searchgehainfo")
	@ResponseBody
	public List<SearchDto> searchGehaInfo(SearchCriteria sc){
		System.out.println("aaaaa");
		
		System.out.println(sc.getGender());
		System.out.println(sc.getGender().size());
		
		System.out.println(sc.getFacilities());
		System.out.println(sc.getFacilities().size());
		
		return dao.searchGeha(sc);
	}
	
}
