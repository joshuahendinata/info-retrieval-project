package com.cz4034.assignment;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cz4034.assignment.solrService.SolrQuery;
import com.cz4034.classifier.Classifier;
import com.cz4034.crawler.Crawler;
import com.cz4034.tweetJSON.Docs;
import com.cz4034.tweetJSON.TweetJSON;

/**
 * Handles requests for the application home page.
 * CHANGES A
 */
@Controller
//@SessionAttributes("queryObject")
public class HomeController {
	
	private ServletContext servletContext;
	private Crawler twitterCrawler;	
	private Classifier WekaClassifier;
	private QueryObject curSelection; // = new QueryObject();
	private RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// Constructor Injection for modularity
	@Autowired
	public HomeController(Crawler twitterCrawler, Classifier wekaClassifier ,QueryObject curSelection
			){
		
		this.twitterCrawler = twitterCrawler;
		this.WekaClassifier = wekaClassifier;
		this.curSelection = curSelection;
	}

	/*
	@ModelAttribute("submittedQuery")
	public QueryObject addStuffToRequestScope() {
		System.out.println("Inside of addStuffToRequestScope");
		QueryObject bean = new QueryObject();
		return bean;
	} */

	// Home Rendering
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, SessionStatus status, Model model) {
		//status.setComplete();
		logger.info("Welcome home! The client locale is {}.", locale);
		
		model.addAttribute("queryObject", new QueryObject());
	    
		return "index";
	}
	
	// Request from search box
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String homePost(@ModelAttribute QueryObject submittedQuery, Model model) throws URISyntaxException{
		// Every new search, instantiate new query object
		curSelection = new QueryObject();
		
		// Update curSelection with the returned object
		curSelection.setQueryKey(submittedQuery.getQueryKey());

		// Renew the session Query, but retained the query key
		// No new object created
		//String queryKey = submittedQuery.getQueryKey();
		//submittedQuery = new QueryObject();
		//submittedQuery.setQueryKey(queryKey);
		
		return SolrQuery.generalAndLocQuery(model, curSelection);
		
	}
	
	// Category selection if started from search box
	@RequestMapping(value = "/query/cat/{category}", method = RequestMethod.GET)
	public String homeCatSelection(@PathVariable String category, Model model) throws URISyntaxException{
		
		curSelection.toString();
		// check and immediately change cat if different
		curSelection.checkStatus(category);
		
		curSelection.toString();
		
		return SolrQuery.catQuery(model, curSelection);
	}

	// Location selection if started from search box
	@RequestMapping(value = "/query/loc/{loc}", method = RequestMethod.GET)
	public String homeLocSelection(@PathVariable String loc, Model model) throws URISyntaxException{
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button-group
		curSelection.checkStatus("all");
		
		// Set location variable on the URI
		curSelection.setLoc(loc);
		
		return SolrQuery.generalAndLocQuery(model, curSelection);
	}
	
	// Request from URL
	@RequestMapping(value = "/query/{querykey}", method = RequestMethod.GET)
	public String homePost(@PathVariable String querykey, Model model){
		// PROCESS the query here
		System.out.println(querykey);
		curSelection = new QueryObject();
		// Update the curSelection with the input query
		curSelection.setQueryKey(querykey);
		
		return SolrQuery.generalAndLocQuery(model, curSelection);
	}
	
	// Category selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/cat/{category}", method = RequestMethod.GET)
	public String catSelection(	@PathVariable String category, @PathVariable String queryKey, Model model){
		// PROCESS the query here!
		System.out.println(curSelection.getLoc());
		
		// Update the current selection;
		curSelection.setQueryKey(queryKey);
		curSelection.checkStatus(category);

		return SolrQuery.catQuery(model, curSelection);
	}

	// Location selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/loc/{loc}", method = RequestMethod.GET)
	public String locSelection(	@PathVariable String loc, 
								@PathVariable String queryKey,
								Model model){
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button-group
		curSelection.checkStatus("all");
		
		// Update the current selection;
		curSelection.setQueryKey(queryKey);
		curSelection.setLoc(loc);
		
		return SolrQuery.generalAndLocQuery(model, curSelection);
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
