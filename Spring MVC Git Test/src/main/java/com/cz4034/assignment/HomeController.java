package com.cz4034.assignment;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;

import com.cz4034.assignment.solrService.SolrQuery;
import com.cz4034.classifier.Classifier;
import com.cz4034.crawler.Crawler;

/**
 * Handles requests for the application home page.
 * CHANGES A
 */
@Controller
public class HomeController {
	
	private ServletContext servletContext;
	private Crawler twitterCrawler;	
	private Classifier WekaClassifier;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private Map<String, QueryObject> queryObjList = new HashMap<String, QueryObject>();
	
	// Constructor Injection for modularity
	@Autowired
	public HomeController(Crawler twitterCrawler, Classifier wekaClassifier){
		
		this.twitterCrawler = twitterCrawler;
		this.WekaClassifier = wekaClassifier;
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
		
		logger.info("Welcome home! The client locale is {}.", locale);
		
		QueryObject curSelection = new QueryObject();
		
		queryObjList.put(curSelection.getUID(), curSelection);
		
		model.addAttribute("queryObject", curSelection);
	    
		return "index";
	}
	
	// Request from search box
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String homePost(@ModelAttribute QueryObject submittedQuery, Model model){
		
		queryObjList.put(submittedQuery.getUID(), submittedQuery);
		
		return SolrQuery.generalAndLocQuery(model, submittedQuery);
		
	}
	
	// Category selection if started from search box
	@RequestMapping(value = "/query/cat/{category}/{uid}", method = RequestMethod.GET)
	public String homeCatSelection( @PathVariable String category, 
									@PathVariable String uid, 
									Model model){
		
		// check and immediately change cat if different
		queryObjList.get(uid).checkStatus(category);
		
		return SolrQuery.catQuery(model, queryObjList.get(uid));
	}

	// Location selection if started from search box
	@RequestMapping(value = "/query/loc/{loc}/{uid}", method = RequestMethod.GET)
	public String homeLocSelection( @PathVariable String loc, 
									@PathVariable String uid, 
									Model model){
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button-group
		queryObjList.get(uid).checkStatus("all");
		
		// Set location variable on the URI
		queryObjList.get(uid).setLoc(loc);
		
		return SolrQuery.generalAndLocQuery(model, queryObjList.get(uid));
	}
	
	// Request from URL
	@RequestMapping(value = "/query/{querykey}", method = RequestMethod.GET)
	public String homePost( @PathVariable String querykey,	
							Model model){
		// PROCESS the query here
		System.out.println(querykey);
		
		QueryObject curSelection = new QueryObject();
		
		// Update the curSelection with the input query
		curSelection.setQueryKey(querykey);
		queryObjList.put(curSelection.getUID(), curSelection);
		
		model.addAttribute("queryObject", curSelection);
		
		return SolrQuery.generalAndLocQuery(model, curSelection);
	}
	
	// Category selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/cat/{category}/{uid}", method = RequestMethod.GET)
	public String catSelection(	@PathVariable String category, 
								@PathVariable String queryKey, 
								@PathVariable String uid, 
								Model model){
		
		// Update the current selection;
		queryObjList.get(uid).setQueryKey(queryKey);
		queryObjList.get(uid).checkStatus(category);

		return SolrQuery.catQuery(model, queryObjList.get(uid));
	}

	// Location selection if started from URL
	@RequestMapping(value = "/query/{queryKey}/loc/{loc}/{uid}", method = RequestMethod.GET)
	public String locSelection(	@PathVariable String loc, 
								@PathVariable String queryKey,
								@PathVariable String uid,
								Model model){
		
		// To show every result on the cat-button group when location button is clicked 
		// i.e refresh the button-group
		queryObjList.get(uid).checkStatus("all");
		
		// Update the current selection;
		queryObjList.get(uid).setQueryKey(queryKey);
		queryObjList.get(uid).setLoc(loc);
		
		return SolrQuery.generalAndLocQuery(model, queryObjList.get(uid));
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
