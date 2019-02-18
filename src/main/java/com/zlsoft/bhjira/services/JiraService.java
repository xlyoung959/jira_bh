package com.zlsoft.bhjira.services;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.Field;
import com.atlassian.jira.rest.client.api.domain.input.UserInput;

public interface JiraService {
	 /**
     * Returns issue type.
     *
     * @param issueType the issue type
     * @return the issue type from jira
     */
    IssueType getIssueType(String issueType);

    /**
     * Returns all projects on jira.
     *
     * @return the list of project.
     */
    Iterable<BasicProject> getAllProject();

    /**
     * Returns all projects on jira.
     *@param key the projects key
     * @return the list of project.
     */
    Project getProjectByKey(String key);

    /**
     * Create support jira on JIRA.
     *
     * @param issue the issue to be created
     * @return the issue with the id
     */
    BasicIssue createIssueInJira(Issue issue) throws URISyntaxException;


    /**
     * Return all supported fields on jira.
     *
     * @return jira supported fields
     */
    List<Field> getAllField();

    /**
     * Return jira field.
     *
     * @param fieldName the jira field name
     * @return jira field
     */
    Field getField(String fieldName);

    /**
     * Update a issue on JIRA.
     *
     * @param issue the issue.
     * @param newFieldList the list of field to be updated.
     * @return the issue with the id
     */
    Issue updateIssueInJira(Issue issue, Iterable<FieldInput> newFieldList);

    /**
     * Returns a issue from JIRA.
     *
     * @param issueKey the issue key
     * @return the issue with the id
     */
    Issue getIssue(String issueKey);

    /**
     * create user under the provided organisation.
     *
     * @param userInput the jira user
     * @return the jira user
     */
    void createUser( UserInput userInput);

    /**
     * Returns a JIRA user.
     *
     * @param username the jira user
     * @return the user
     */
    User getUserByName(String username);

    /**
     * validate if an user exist on JIRA.
     *
     * @param username the jira user
     * @return true if exist else false
     */
    boolean isUserExist(String username);

    /**
     * 查询jira中某个项目下面问题提的数量
     * @param jql
     * @return
     */
    int getIssueNum(String jql);

    /**
     * 根据问题的关键字删除问题，
     * @param issueKey
     */
    void deleteIssue(String issueKey);

    /**
     * 通过jql语句来查询某个项目下面的所有问题的key
     * @param jql
     * @return
     */
    String getAllIssueKey(String jql);

    /**
     * 根据问题key，来查询问题的概要用来判断是否会有重复的问题
     * @param issueKey
     * @return
     */
    String getIssueSummery(String issueKey);


}
