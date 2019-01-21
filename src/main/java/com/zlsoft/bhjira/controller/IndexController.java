package com.zlsoft.bhjira.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.zlsoft.bhjira.services.JiraService;



@RestController
public class IndexController {

	private final JiraService jiraService;
	
	public IndexController(JiraService jiraService) {
		this.jiraService = jiraService;
	}

	@RequestMapping(value = "/jira")
	public List<Project> index() {
//    	JiraRestClient s;
//    	s.getMetadataClient().getIssueType(uri);
		System.out.println();
		Iterable<BasicProject> list = jiraService.getAllProject();
		List<Project> listProject = new ArrayList<Project>();
		for (BasicProject basicProject : list) {
			listProject.add(jiraService.getProjectByKey(basicProject.getKey()));
		}
		
		
//		IndexDto id = new IndexDto("张三", "主页");
		return listProject;
	}
}
