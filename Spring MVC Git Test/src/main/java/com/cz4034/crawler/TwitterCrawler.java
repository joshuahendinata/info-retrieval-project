package com.cz4034.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCrawler implements Crawler {
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "id,user,time,lat,lon,text";
	
	public TwitterCrawler(){
	}
	
	/* (non-Javadoc)
	 * @see com.cz4034.crawler.Crawler#refresh(java.lang.String)
	 */
	@Override
	public void refresh(String filedir){
		//Twitter Configuration
		ConfigurationBuilder cb = new ConfigurationBuilder();
		/*cb.setOAuthConsumerKey("yfnzmO6UiWtwvx3MyjK3CxC7I");
		cb.setOAuthConsumerSecret("dj0KRyS5JlHgMyKmDZr2dZ8qflTrMjzq2YtndSJk2kEUa0GyiH");
		cb.setOAuthAccessToken("3023426774-C0449WSqBW8Y2oiqnvnFFBxKXmzkHJ2CKFLSfNR");
		cb.setOAuthAccessTokenSecret("nISvYHyEGMc3xr3B0slSbxJrB39igKSG0seX2fPael4xa");*/
		
		//Alternative token if API limit exceeds
		cb.setOAuthConsumerKey("kYxDQ70cZ8PcHR2WW84gcn1YN");
		cb.setOAuthConsumerSecret("T3td8Ea3Ih3ghjFYnBU7wN2BqOVIIfSCidpfUfxHcO4RACx7Fp");
		cb.setOAuthAccessToken("3023426774-QX5jopS3IMqy9qJ3H79PU5hsqrBtGIHAcSEDB5B");
		cb.setOAuthAccessTokenSecret("rHCPo34NT3botEm8kdX44h0WoJvmeWO3NjDFyDzxebsVm");

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		Query query = new Query("#dinner OR #breakfast OR #brunch OR #lunch OR #supper");
		query.setLang("en");	//English language
		int numberOfTweets = 18000;		//maximum number of tweets can be retrieved (API limit)
		long lastID = Long.MAX_VALUE;
		ArrayList<Status> tweets = new ArrayList<Status>();
		while (tweets.size () < numberOfTweets) {
			if (numberOfTweets - tweets.size() > 100)
				query.setCount(100);
			else 
				query.setCount(numberOfTweets - tweets.size());
			try {
				QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				System.out.println("Gathered " + tweets.size() + " tweets");
				for (Status t: tweets) 
					if(t.getId() < lastID) lastID = t.getId();

			}

			catch (TwitterException te) {
				System.out.println("Couldn't connect: " + te);
			}; 
			query.setMaxId(lastID-1);
		}
		
		FileWriter fileWriter = null;
		
		try{
			fileWriter = new FileWriter(filedir + "/corpus.csv");
			
			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			for (int i = 0; i < tweets.size(); i++) {
				Status t = (Status) tweets.get(i);
				
				//skip retweet
				if (t.isRetweet() == true)
					continue;
				
				Long id = t.getId();
				String user = t.getUser().getScreenName();
				//to convert whitespace characters into space
				String text = t.getText().replaceAll("[\\t\\n\\r]+", " ");
				//to escape double quotes and comma
				String escaped_text = StringEscapeUtils.escapeCsv(text);
				Date time = t.getCreatedAt();
				Double lat= 0.0, lon=0.0;
				
				GeoLocation loc = t.getGeoLocation();
				if (loc!=null) {
					lat = t.getGeoLocation().getLatitude();
					lon = t.getGeoLocation().getLongitude();
					//System.out.println(i + " USER: " + user + " wrote: " + text + " located at " + lat + ", " + lon);
				} 
				
				fileWriter.append(String.valueOf(id));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(user);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(time.toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(lat));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(lon));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(escaped_text);
				fileWriter.append(NEW_LINE_SEPARATOR);
	
			}
			
			System.out.println("CSV file was created successfully");
		} catch (Exception e){
			System.out.println("Error in CSV File Writer");
			e.printStackTrace();
		} finally{
			try{
				fileWriter.flush();
				fileWriter.close();
			} catch(IOException e){
				System.out.println("Error while flushing/closing fileWriter!");
				e.printStackTrace();
			}
		}
	}
}
