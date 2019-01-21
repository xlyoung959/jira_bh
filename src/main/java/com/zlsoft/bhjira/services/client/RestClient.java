package com.zlsoft.bhjira.services.client;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.MetadataRestClient;

public interface RestClient extends JiraRestClient {

	/**
	 * Retrieves metadata rest client.
	 *
	 * @return metadata rest client
	 */
	MetadataRestClient getMetadataRestClient();
}