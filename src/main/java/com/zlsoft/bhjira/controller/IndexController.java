package com.zlsoft.bhjira.controller;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.api.domain.*;
import com.zlsoft.bhjira.services.impl.JiraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zlsoft.bhjira.services.JiraService;



@RestController
public class IndexController {

	@Autowired
	private final JiraService jiraService;

	@Autowired
	private JiraServiceImpl jiraServiceImpl;
	
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

	/**
	 * 从BH读取问题，来放在jira里面，但现在没有连接BH只有从文本里面读取
	 */
	@RequestMapping("AssIssue")
	@Scheduled(fixedRate=3600*1000)
	public void addIssue() {
		StringBuilder builder = new StringBuilder();
		try {
			//构造一个BufferedReader类来读取文件
			BufferedReader br = new BufferedReader(new FileReader("E:/bh.txt"));
			String s = null;
			//使用readLine方法，一次读一行
			while ((s = br.readLine()) != null) {
				builder.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//将BufferedReader转换成String类型，并转换成List,数组Arrays
		String result = builder.toString();
		result = result.substring(1, result.length() - 1);
		String[] strs = result.split("},");
		List<String> list = Arrays.asList(strs);
		//获取jira里面的所有项目，
		Iterable<BasicProject> projects = jiraServiceImpl.getAllProject();
		//获取项目的名称 name还有关键字 KEY
		String projectName = projects.iterator().next().getName();
		String projectKey = projects.iterator().next().getKey();
		//根据项目的关键字key来查询该项目下面的问题的key,project=DOC
		String g = jiraServiceImpl.getAllIssueKey("project=" + projectKey);
		//把的到的字符串转换为数组，方便取值，
		String[] keys = g.split(",");
		String[] summarys = new String[keys.length];
		//查询某个问题的概要，只要前36位来比较，并放入数组里面，前36位是问题的ID
		for (int k = 0; k < keys.length; k++) {
			summarys[k] = jiraServiceImpl.getIssueSummery(keys[k]).substring(0, 36);
		}
		//循环遍历 list中的数据，并在jira里面插入
		for (int i = 1; i < list.size(); i++) {
			String jsonO = list.get(i) + "}";
			//将list数组里面的每一个转成JSONObject
			JSONObject jsonObject = JSONObject.parseObject(jsonO);
			//只有传入的数据中 系统名称 为“新版移动医生工作站”才能在jira中插入
			if (jsonObject.getString("系统名称").equals(projectName)) {
				int num = 0;
				//循环遍历jira里面的问题概要的数组来判断是否会和传进来的重复
				for (int j = 0; j < summarys.length; j++) {
					//判断有相等的就加一。num相当于一个标识
					if (summarys[j].equals(jsonObject.getString("ID"))) {
						num++;
					}
				}
				//num等于0就相当于没有重复的，就可以执行插入操作了
				if (num == 0) {
					Long id = Long.parseLong(jsonObject.getString("编号"));
					BasicProject project = new BasicProject(null, null, null, jsonObject.getString("系统名称"));
					String summary = jsonObject.getString("ID") + "-" + jsonObject.getString("登记渠道名称") + "-" + jsonObject.getString("登记人姓名");
					String description = jsonObject.getString("内容描述");
					//获取报告人的信息，根据用户名
					User reporter = jiraServiceImpl.getUserByName("qbo");
					String type = "";
					if (jsonObject.getString("性质").substring(0, 1).equals("A")) {
						type = "故障";
					} else if (jsonObject.getString("性质").substring(0, 1).equals("B")) {
						type = "任务";
					} else {
						type = "故事";
					}
					//获取问题的类型信息，根据问题类型的名字
					IssueType issueType = jiraServiceImpl.getIssueType(type);
					//构造一个新问题
					Issue issue = new Issue(summary, null, null, id, project, issueType, null, description, null, null, null, reporter,
							null, null, null, null, null, null, null, null, null, null, null,
							null, null, null, null, null, null, null, null, null);
					//在Jira里面创建一个问题
					BasicIssue basicIssue = jiraServiceImpl.createIssueInJira(issue);
				} else {
					System.out.println("问题已存在，不可重复插入");
				}
			} else {
				System.out.println("jira中没有该项目，请手动去创建一个");
			}
		}

	}


	/**
	 * 配置网络钩子，问题更改则返回问题的相关信息
	 * @param key 问题的key
	 * @return 通过ResponseBody将map转换成json字符串
	 */
	@RequestMapping("/webHook")
	@ResponseBody
	public Map<Object,Object> webHook(String key){
		Issue issue=jiraService.getIssue(key);
//	System.out.println(issue.getReporter());
//	System.out.println(issue.getAssignee());
		Map<Object,Object> map = new HashMap<>();
		//问题的key
		map.put("issuekey",issue.getKey());
		//问题的id
		map.put("issueid",issue.getId());
		//报告人的信息
		map.put("reporterName",issue.getReporter());
		//经办人的信息
		map.put("assigneeName",issue.getAssignee());
		
		if(issue.getStatus().getName().equals("完成")){
			System.out.println("将状态为完成的问题返回给bh");
			return map;
		}else if (issue.getStatus().getName().equals("处理中")){
			System.out.println("将状态为处理中的问题返回给bh");
			return map;
		}else if(issue.getStatus().getName().equals("待办")){
			System.out.println("将状态为待办的问题返回给bh");
			return map;
		}else {
			//该问题没有符合的状态，返回为空
			return null;
		}



	}






}
