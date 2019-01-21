package com.zlsoft.bhjira.services.impl;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Field;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;


import com.zlsoft.bhjira.services.JiraService;

@Service
public class JiraServiceImpl implements JiraService{

	@Value("${jira.project}")
    protected String project;
	
	
	@Autowired
    private JiraRestClient jiraConnection;
	
	@Override
	public IssueType getIssueType(String issueType) {
		 List<IssueType> issueTypeList = (List<IssueType>) jiraConnection.getMetadataClient().getIssueTypes();
//	        log.debug("List of available issue type [ {} ]", issueTypeList);
	        for (IssueType issue : issueTypeList) {
//	            log.info("Issue type [ {} ]" + issue.getName());
	            if (issue.getName().equalsIgnoreCase(issueType)) {
	                return issue;
	            }
	        }
	        return null;
	}

	@Override
	public Iterable<BasicProject> getAllProject() {
		 return  jiraConnection.getProjectClient().getAllProjects().claim();
	}

	@Override
	public Project getProjectByKey(String key) {
        return jiraConnection.getProjectClient().getProject(key).claim();
	}

	@Override
	public BasicIssue createIssueInJira(Issue issue) {
		 IssueInputBuilder issueInputBuilder = new IssueInputBuilder(getProjectByKey(project).getKey(), getIssueType(issue.getIssueType().getName()).getId());
	        User user = getUserByName(issue.getReporter().getName());

	        // create user
	        if(user == null) {
	        	throw new RuntimeException("could not found user!");
//	            createUser(organisation, issue.getReporter());
//	            user = getUserByName(issue.getReporter().getName());
	        }

	        IssueInput issueInput = issueInputBuilder.setSummary(issue.getSummary())
	                        .setDescription(issue.getDescription())
	                        .setReporter(user)
//	                      .setFieldValue(getField("Request Type").getId(), "Technical Support")
	                        .build();
	        BasicIssue issueCreated = jiraConnection.getIssueClient().createIssue(issueInput).claim();
//	        log.info("New issue has been created with key [ {} ]", issueCreated);
	        return issueCreated;
	}

	@Override
	public List<Field> getAllField() {
        return (List<Field>) jiraConnection.getMetadataClient().getFields();
	}

	@Override
	public Field getField(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Issue updateIssueInJira(Issue issue, Iterable<FieldInput> newFieldList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Issue getIssue(String issueKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUser(String organisation, String user) {
		   try {
//	            invokePostMethod(host + USER_URI_PREFIX + "?name=" + organisation, "{\"emails\":[\""+ user +"\"]}");
	        }catch (Exception exception) {
//	            log.error("Exception during user creation", exception);
	        }
	}

	@Override
	public User getUserByName(String username) {
		 User user = null;
	        try {
	            user = jiraConnection.getUserClient().getUser(username).claim();
//	            log.info("Jira User [ {} ]", user);
	        } catch (Exception exception) {
//	            log.error("Unable to retrieve user from jira [ {} ]", exception.getMessage());
	        }
	        return user;
	}

	@Override
	public boolean isUserExist(String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
