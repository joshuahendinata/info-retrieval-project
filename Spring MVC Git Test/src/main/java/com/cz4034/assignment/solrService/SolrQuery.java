package com.cz4034.assignment.solrService;

import java.net.URI;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cz4034.assignment.QueryObject;
import com.cz4034.tweetJSON.Docs;
import com.cz4034.tweetJSON.TweetJSON;

public class SolrQuery {
	
	//private static QueryObject queryObject;
	private static RestTemplate restTemplate = new RestTemplate();
	
	public static String generalAndLocQuery(Model model, QueryObject curSelection){
		long startTime = System.currentTimeMillis();
		
		//queryObject = curSelection;
		
		System.out.println("query req: " + getURI(curSelection).toString()); 
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(curSelection),TweetJSON.class);
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		List<String> catNum = tweetObject.getFacet_counts().getFacet_fields().getClassCategory();
		String totalTweets = tweetObject.getResponse().getNumFound();
		
		model.addAttribute("tweetResults", tweetResults);
		model.addAttribute("catNum", catNum);
		model.addAttribute("totalTweets", totalTweets);
		model.addAttribute("queryObject", curSelection);
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time elapsed :" + estimatedTime + " ms\n");
		
		return "result";
	}
	
	public static String catQuery(Model model, QueryObject curSelection){
		long startTime = System.currentTimeMillis();
		
		System.out.println("cat req: " + getURI(curSelection).toString()); 
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(curSelection),TweetJSON.class);
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();

		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time elapsed :" + estimatedTime + " ms\n");
		
		if(tweetResults.isEmpty() && curSelection.getStartRow() == 0 ){
			System.out.println("empty");
			return "tweets :: noTweets";
		}
		else if(tweetResults.isEmpty() && curSelection.getStartRow() != 0 ){
			System.out.println("done");
			return "tweets :: scrollEnd";
		}
		
		model.addAttribute("tweetResults", tweetResults);
		
		return "tweets :: tweetsFragment";
	}
	
	public static URI getURI(QueryObject queryObject){
		URI targetUrl= UriComponentsBuilder.fromUriString("http://localhost:8983")
		    .path("/solr/gettingstarted_shard1_replica2/select")
		    .queryParam("q", queryObject.getQueryKey())
		    .queryParam("fq", "class:" + queryObject.getCategory())
		    .queryParam("start", queryObject.getStartRow())
		    .queryParam("rows", queryObject.getTweetNum())
		    .queryParam("wt", "json").queryParam("indent", "true")
		    .queryParam("facet", "true").queryParam("facet.field", "class")
		    .queryParam("spatial", "true")
		    .queryParam("fq", queryObject.getLoc())		    
		    .build()
		    .toUri();
		return targetUrl;
	}
}
