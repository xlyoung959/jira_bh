package com.zlsoft.bhjira.services.impl;

import java.util.Iterator;
import java.util.List;

import com.atlassian.jira.rest.client.api.domain.*;

import com.atlassian.jira.rest.client.api.domain.input.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
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

    /**
     * 问题类型的ID是10000到10006
     * @param issueType the issue type
     * @return
     */
	@Override
	public IssueType getIssueType(String issueType) {
		 Iterable<IssueType> issueTypeList = jiraConnection.getMetadataClient().getIssueTypes().claim();
	        for (IssueType issueT : issueTypeList) {
	            if (issueT.getName().equalsIgnoreCase(issueType)) {
	                return issueT;
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


	/**
	 * getIssueType(issue.getIssueType().getName()).getId()
	 * @param
	 * @return
	 */
	@Override
	public BasicIssue createIssueInJira(Issue issue) {
		 IssueInputBuilder issueInputBuilder = new IssueInputBuilder(getProjectByKey(project).getKey(),issue.getIssueType().getId());
	        /*User user = getUserByName(issue.getReporter().getName());
	        // create user
	        if(user == null) {
	        	throw new RuntimeException("could not found user!");
//	            createUser(organisation, issue.getReporter());
//	            user = getUserByName(issue.getReporter().getName());
	        }*/
	        IssueInput issueInput = issueInputBuilder.setSummary(issue.getSummary())
	                        .setDescription(issue.getDescription())
	                        //.setReporter(user)
                      //.setFieldValue(getField("Request Type").getId(), "Technical Support")
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
		return null;
	}

	@Override
	public Issue updateIssueInJira(Issue issue, Iterable<FieldInput> newFieldList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Issue getIssue(String issueKey) {//根据问题的关键字得到单一问题
        Issue issue = null;
        try {
            issue = jiraConnection.getIssueClient().getIssue(issueKey).claim();

            // 得到问题后续的工作流，可对iter进行for遍历,遍历出来也就是工作流，待完成，处理中，完成的信息
            /*Iterable<Transition> iter = jiraConnection.getIssueClient()
                    .getTransitions(issue).claim();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issue;
	}

	@Override
	public void createUser( UserInput userInput) {
		   try {
               jiraConnection.getUserClient().createUser(userInput).claim();
	        }catch (Exception exception) {
//	            log.error("Exception during user creation", exception);
	        }
	}

	@Override
	public User getUserByName(String username) {
		 User user = null;
	        try {
	            user = jiraConnection.getUserClient().getUser(username).claim();
	        } catch (Exception exception) {

	        }
	        return user;
	}

	@Override
	public boolean isUserExist(String username) {
		// TODO Auto-generated method stub
		//根据用户名·判断用户是否存在，返回 true或者,false
		User user=null;
		user=jiraConnection.getUserClient().getUser(username).claim();
		if(user.getName()==username){
			return true;
		}
		else {
			return false;
		}
	}


    @Override
	public int getIssueNum(String jql){//查询某个项目下面的问题的多少、数量
		SearchResult searchResult=jiraConnection.getSearchClient().searchJql(jql).claim();
		 return searchResult.getTotal();
	}

	@Override
    public String getAllIssueKey(String jql){
		SearchResult searchResult=jiraConnection.getSearchClient().searchJql(jql).claim();
		Iterable<Issue> issues= searchResult.getIssues();
		String issueKeys="";
		for(Issue issue :issues){
			issueKeys=issueKeys+issue.getKey()+",";
		}
		return issueKeys;
    }

    @Override
	public String getIssueSummery(String issueKey){
		String summary=jiraConnection.getIssueClient().getIssue(issueKey).claim().getSummary();
		return summary;
	}

    @Override
	public void deleteIssue(String issueKey){
		jiraConnection.getIssueClient().deleteIssue(issueKey,false).claim();
	}


}
