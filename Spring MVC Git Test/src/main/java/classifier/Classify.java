package classifier;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class Classify {
	
	/**
	 * Object that stores the instance.
	 */
	private static Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	private static FilteredClassifier classifier;

	public static void classifyEntries(String filedir) {
		loadModel(filedir + "/J48.model");
		
		String id = "";
		String user = "";
		String time = "";
		String lat = "";
		String lon = "";
		String text = "";
		String classname = "";
		
		try {
			
			JSONArray jsonArray = new JSONArray();
			BufferedReader br = new BufferedReader(new FileReader(filedir + "/corpus.csv"));
			String line;
			
			br.readLine(); //skip the header
			
			while ((line = br.readLine()) != null) {
				String[] entry = line.split(",", 6);
				
				id = entry[0];
				user = entry[1];
				time = entry[2];
				lat = entry[3];
				lon = entry[4];
				text = entry[5];
				System.out.println(text);
				classname = classifyEntry(text);
				
				JSONObject x = new JSONObject();
				x.put("id", id);
				x.put("user", user);
				x.put("time", time);
				x.put("lat", lat);
				x.put("lng",  lon);
				x.put("text", text);
				x.put("class", classname);
				x.put("pos_p", lat + ", " + lon);
				
				jsonArray.put(x);
 
			}
			
			FileWriter out = new FileWriter(filedir + "/corpus-labelled.json");
			out.write(jsonArray.toString(2));
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(id + ", " + user + ", " + text + ", " + classname);
		}

	}
	
	private static String classifyEntry(String text) {
		// Create the attributes, class and text
		FastVector fvNominalVal = new FastVector(5);
		fvNominalVal.addElement("breakfast");
		fvNominalVal.addElement("lunch");
		fvNominalVal.addElement("dinner");
		fvNominalVal.addElement("supper");
		fvNominalVal.addElement("brunch");
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("text",(FastVector) null);
		
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		instances = new Instances("Test relation", fvWekaAttributes, 1);
		
		// Set class index
		instances.setClassIndex(0);
		
		// Create and add the instance
		Instance instance = new Instance(2);
		instance.setValue(attribute2, text);
		instances.add(instance);
// 		System.out.println("===== Instance created with reference dataset =====");
//		System.out.println(instances);
		
		//classify the new instance
		double prediction = 0;
		try {
			prediction = classifier.classifyInstance(instances.instance(0));
//			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + instances.classAttribute().value((int) prediction));
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
		}
		return instances.classAttribute().value((int) prediction);
	}
	
	private static void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + fileName + " =====");
       } 
		catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + fileName);
		}
	}
}
