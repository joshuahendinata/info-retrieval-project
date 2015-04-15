package com.cz4034.assignment;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cz4034.classifier.Classifier;
import com.cz4034.crawler.Crawler;
import com.cz4034.tweetJSON.Docs;
import com.cz4034.tweetJSON.TweetJSON;

/**
 * Handles requests for the application home page.
 * CHANGES A
 */
@Controller
public class HomeController {
	
	private ServletContext servletContext;
	private Crawler twitterCrawler;	
	private Classifier WekaClassifier;
	private QueryObject curSelection = new QueryObject();
	private RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// Constructor Injection for modularity
	@Autowired
	public HomeController(Crawler twitterCrawler,
			Classifier wekaClassifier){
		
		this.twitterCrawler = twitterCrawler;
		this.WekaClassifier = wekaClassifier;
	}
	
	public URI getURI(){
		URI targetUrl= UriComponentsBuilder.fromUriString("http://localhost:8983")
		    .path("/solr/gettingstarted_shard1_replica2/select")
		    .queryParam("q", curSelection.getQueryKey())
		    .queryParam("fq", "class:" + curSelection.getCategory())
		    .queryParam("start", curSelection.getStartRow())
		    .queryParam("rows", curSelection.getTweetNum())
		    .queryParam("wt", "json").queryParam("indent", "true")
		    .queryParam("facet", "true").queryParam("facet.field", "class")
		    .queryParam("spatial", "true")
		    .queryParam("fq", curSelection.getLoc())		    
		    .build()
		    .toUri();
		return targetUrl;
	}
	
	// Home Rendering
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		model.addAttribute("queryObject", curSelection);
	    
		return "index";
	}
	
	// Request from search box
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String homePost(@ModelAttribute QueryObject submittedQuery, Model model) throws URISyntaxException{
		long startTime = System.currentTimeMillis();
		
		// Every new search, instantiate new query object
		curSelection = new QueryObject();
		
		// Update curSelection with the returned object
		curSelection.setQueryKey(submittedQuery.getQueryKey());
		
		System.out.println("query req: " + getURI().toString()); 
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		List<String> catNum = tweetObject.getFacet_counts().getFacet_fields().getClassCategory();
		String totalTweets = tweetObject.getResponse().getNumFound();
		
		model.addAttribute("tweetResults", tweetResults);
		model.addAttribute("catNum", catNum);
		model.addAttribute("totalTweets", totalTweets);
		
		System.out.println();
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time elapsed :" + estimatedTime + " ms");
		
		return "result";
	}
	
	// Category selection if started from search box
	@RequestMapping(value = "/query/cat/{category}", method = RequestMethod.GET)
	public String homeCatSelection(@PathVariable String category, Model model) throws URISyntaxException{
		
		// check and immediately change cat if different
		curSelection.checkStatus(category);
		
		System.out.println("cat req: " + getURI().toString()); 
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();

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

	// Location selection if started from search box
	@RequestMapping(value = "/query/loc/{loc}", method = RequestMethod.GET)
	public String homeLocSelection(@PathVariable String loc, Model model) throws URISyntaxException{
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button
		curSelection.checkStatus("all");
		
		// Set location variable on the URI
		curSelection.setLoc(loc);

		System.out.println("loc req: " + getURI().toString()); 
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
				
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		List<String> catNum = tweetObject.getFacet_counts().getFacet_fields().getClassCategory();
		String totalTweets = tweetObject.getResponse().getNumFound();
		
		model.addAttribute("tweetResults", tweetResults);
		model.addAttribute("catNum", catNum);
		model.addAttribute("totalTweets", totalTweets);
		
		// Add queryObject since we return the whole page (not partial)
		model.addAttribute("queryObject", curSelection);
		
		System.out.println();
		
		return "result";
	}
	
	// Request from URL
	@RequestMapping(value = "/query/{querykey}", method = RequestMethod.GET)
	public String homePost(@PathVariable String querykey, Model model){
		// PROCESS the query here
		System.out.println(querykey);
		
		// Update the curSelection with the input query
		curSelection.setQueryKey(querykey);
		
		System.out.println("URL req: " + getURI().toString());
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		List<String> catNum = tweetObject.getFacet_counts().getFacet_fields().getClassCategory();
		String totalTweets = tweetObject.getResponse().getNumFound();
		
		model.addAttribute("tweetResults", tweetResults);
		model.addAttribute("catNum", catNum);
		model.addAttribute("totalTweets", totalTweets);
		
		// Add queryObject since we request from URL (no object passed previously)
		model.addAttribute("queryObject", curSelection);
		
		return "result";
	}
	
	// Category selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/cat/{category}", method = RequestMethod.GET)
	public String catSelection(	@PathVariable String category, 
								@PathVariable String queryKey,
								Model model){
		// PROCESS the query here!
		System.out.println(curSelection.getLoc());
		
		// Update the current selection;
		curSelection.setQueryKey(queryKey);
		curSelection.checkStatus(category);
		
		System.out.println("URL cat req: " + getURI().toString());
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
		
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		
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

	// Location selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/loc/{loc}", method = RequestMethod.GET)
	public String locSelection(	@PathVariable String loc, 
								@PathVariable String queryKey,
								Model model){
		
		// Update the current selection;
		curSelection.setQueryKey(queryKey);
		curSelection.setLoc(loc);
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button
		curSelection.checkStatus("all");
		
		System.out.println("URL loc req: " + getURI().toString());
		
		TweetJSON tweetObject = restTemplate.getForObject(getURI(),TweetJSON.class);
		
		
		List<Docs> tweetResults = tweetObject.getResponse().getDocs();
		
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
	
	// Refresh database by re-crawling from twitter
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String refreshDatabase(Model model){
		String filePath = servletContext.getRealPath("/resources/");
		
		System.out.println(filePath + "/corpus.csv");
		

		try{
			// Crawling method: David
			
			//Crawl a = new Crawl();
			twitterCrawler.refresh(filePath);
			
			// Classify method: Ellensi + Nirmala
			WekaClassifier.classifyEntries(filePath);

			// Indexing method: Audi
			Process proc = Runtime.getRuntime().exec("java "
					+ "-Dc=gettingstarted "
					+ "-Dauto=yes -jar "
					+ "\"" + filePath + "/post.jar\" "
					+ "\"" + filePath + "/corpus-labelled.json\"");
			
			// Then retreive the process output
			InputStream in = proc.getInputStream();
			InputStream err = proc.getErrorStream();
			
			System.out.println(convertStreamToString(in));
			System.out.println(convertStreamToString(err));
			
			System.out.println("refresh test");
			model.addAttribute("errorObject", "successful!");
			
		} catch(Exception e){
			e.printStackTrace();
			model.addAttribute("errorObject", e.getMessage());
		}		
		
		return "tweets :: refreshResult";
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}	
	
	
}
