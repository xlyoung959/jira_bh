package com.zlsoft.bhjira.services.client.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.AuditRestClient;
import com.atlassian.jira.rest.client.api.ComponentRestClient;
import com.atlassian.jira.rest.client.api.GroupRestClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.MetadataRestClient;

import com.atlassian.jira.rest.client.api.MyPermissionsRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.ProjectRolesRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.SessionRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.VersionRestClient;

import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

@Component
public class MyJIRARestClient implements InitializingBean, JiraRestClient{

    private IssueRestClient issueRestClient;

    private SessionRestClient sessionRestClient;

    private UserRestClient userRestClient;

    private ProjectRestClient projectRestClient;

    private ComponentRestClient componentRestClient;

//    private MetadataRestClient metadataClient;

    private MetadataRestClient metadataRestClient;

    private SearchRestClient searchRestClient;

    private VersionRestClient versionRestClient;

    private ProjectRolesRestClient projectRolesRestClient;
    
    private GroupRestClient groupRestClient;
    
    private JiraRestClient restClient;
    
    
    @Value("${jira.host}")
    protected String host;

    @Value("${jira.user}")
    protected String username;

    @Value("${jira.password}")
    protected String password;

    private URI baseUri;

    private URI serverUri;
	
	
	@Override
	public IssueRestClient getIssueClient() {
		return issueRestClient;
	}

	@Override
	public SessionRestClient getSessionClient() {
		// TODO Auto-generated method stub
		return sessionRestClient;
	}

	@Override
	public UserRestClient getUserClient() {
		// TODO Auto-generated method stub
		return userRestClient;
	}

	@Override
	public GroupRestClient getGroupClient() {
		// TODO Auto-generated method stub
		return groupRestClient;
	}

	@Override
	public ProjectRestClient getProjectClient() {
		// TODO Auto-generated method stub
		return projectRestClient;
	}

	@Override
	public ComponentRestClient getComponentClient() {
		// TODO Auto-generated method stub
		return componentRestClient;
	}


	@Override
	public SearchRestClient getSearchClient() {
		// TODO Auto-generated method stub
		return searchRestClient;
	}

	@Override
	public VersionRestClient getVersionRestClient() {
		// TODO Auto-generated method stub
		return versionRestClient;
	}

	@Override
	public ProjectRolesRestClient getProjectRolesRestClient() {
		// TODO Auto-generated method stub
		return projectRolesRestClient;
	}

	@Override
	public AuditRestClient getAuditRestClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyPermissionsRestClient getMyPermissionsRestClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public MetadataRestClient getMetadataClient() {
		// TODO Auto-generated method stub
		return metadataRestClient;
	}

//	@Override
//	public MetadataRestClient getMetadataRestClient() {
//		// TODO Auto-generated method stub
//		return metadataRestClient;
//	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		 init();
	}
	
	
	public void init() throws URISyntaxException {
//        log.info("Initialize JIRA REST clients");

        this.serverUri = new URI(host);
        this.baseUri = UriBuilder.fromUri(serverUri).build();
        
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        
		
        restClient = factory.createWithBasicHttpAuthentication(this.baseUri, username, password);
        
		metadataRestClient = restClient.getMetadataClient();
		sessionRestClient = restClient.getSessionClient();
		issueRestClient = restClient.getIssueClient();
		userRestClient = restClient.getUserClient();
		projectRestClient = restClient.getProjectClient();
		componentRestClient = restClient.getComponentClient();
		searchRestClient = restClient.getSearchClient();
		versionRestClient = restClient.getVersionRestClient();
		projectRolesRestClient = restClient.getProjectRolesRestClient();

//        AuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(username, password);
//        DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
//        authenticationHandler.configure(config);
//        httpClient = initHttpClient(config, authenticationHandler);
//        metadataClient = new MetadataClient(baseUri, httpClient);
//        metadataRestClient = new JerseyMetadataRestClient(baseUri, httpClient);
//        sessionRestClient = new JerseySessionRestClient(httpClient, serverUri);
//        issueRestClient = new JerseyIssueRestClient(baseUri, httpClient, sessionRestClient, metadataRestClient);
//        userRestClient = new JerseyUserRestClient(baseUri, httpClient);
//        projectRestClient = new JerseyProjectRestClient(baseUri, httpClient);
//        componentRestClient = new JerseyComponentRestClient(baseUri, httpClient);
//        searchRestClient = new JerseySearchRestClient(baseUri, httpClient);
//        versionRestClient = new JerseyVersionRestClient(baseUri, httpClient);
//        projectRolesRestClient = new JerseyProjectRolesRestClient(baseUri, httpClient, serverUri);
    }
	
//	   @Bean
//	    public ProgressMonitor progressMonitor() {
//	        return new NullProgressMonitor();
//	    }

}
