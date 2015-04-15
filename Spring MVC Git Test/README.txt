=============== PROJECT CZ4034: INFORMATION RETRIEVAL - GROUP 24 ===============

System Requirement:
- At least Java SE 1.6

- Apache Solr

Either:
- Any operating system with Maven plugin installed OR

- Latest Spring Tool Suite IDE (https://spring.io/tools)

=============== Starting Solr Server ===============

1. Install the latest solr version from http://www.apache.org/dyn/closer.cgi/lucene/solr/

2. Go to /your/path/to/Solr-x.x.x/bin in command prompt

3. Type 'solr start -e cloud -noprompt' to start a SolrCloud server 

4. To pre-populate Solr, paste the provided 'corpus-labelled.json' to /your/path/to/Solr-x.x.x/example/exampledocs

5. type 'java -Dc=gettingstarted -Dauto=yes -jar post.jar corpus-labelled.json'
5.a If you decided to use another folder, make sure to use the right relative/absolute path to 'post.jar' and 'corpus-labelled.json'

6. To make sure Solr is running, visit localhost:8983/solr on your browser



=============== Starting the application via Maven Plugin ===============

1. In your console or command prompt, go to /your/path/to/Project_CZ4034_NEW

2. Type 'mvn clean install' to build the project dependencies

3. Type 'mvn tomcat7:run' to start tomcat 7 in your system

4. Go to 'localhost:8080' in your browser and enjoy the application!



=============== Starting the application via Spring Tool Suite ===============

1. Go to File->Import and select 'Existing projects to workspace'

2. Select /your/path/to/Project_CZ4034_NEW as root directory

3. Right click on the project->Run on server (choose any server available)

4. Go to 'localhost:8080/assignment/ in your browser and enjoy the application!




NOTE: In order to properly run the application, make sure you have SolrCloud running in your system
 