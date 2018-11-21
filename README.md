The jmasar-service implements the MAchine Save And Restore service as a collection
of REST endpoints. These can be used by clients to create and manage configurations and
snapshots. 

NOTE: the R(estore) portion of the name JMasar is a bit misleading. The service does not provide
a way to restore/write PV values. However, it provides an API to retrieve PV values
that have been persisted in a snapshot at some point in time. A client application may
then use these values to perform the restore operation.

The service depends on the jmasar-model artifact (separate git repository). 
Java-based clients should also make use of jmasar-model to facilitate 
marshalling/unmarshalling of data.

Features:

* The service supports a tree structure of objects, much like a file system tree. 
Tree node objects are either "folders" or configurations. 

* Nodes in the tree can be renamed, moved or deleted. 

* Snapshots are associated with configurations and are not treated as nodes in
the tree.

* The service is built upon Spring Boot and depends on some persistence 
implementation. In its current version, persistence is implemented against
a RDB engine, using Spring's JdbcTemplate to manage SQL queries.

* The service has been verified on Postgres 9.6 and Mysql 8.0, on Mac OS. Database 
connection parameters are found in src/main/resources. To select a database engine, add
-Ddbengine=postgresql or -Ddbengine=mysql to the command line when launching
the service.

* Flyway scripts for Postgres and Mysql are provided to set up the database. 
Flyway is run as part of the application startup, i.e. there is no need to 
run Flyway scripts manually.

* Unit tests rely on the H2 in-memory database and are hence independent of any
external database engine. Note that Flyway scripts for the H2 database are found
in src/test/resources/db/migration. Running the unit tests will create the H2
"database file" (h2.db.mv.db) in a folder named db relative to the current directory.

* A Swagger UI is available if -Dspring.profiles.active=[development | test] is
provided on the command line.
When running the service on any other host than the local development box, also
add command line parameter -Dhostname=[IP address | FQDN]. On a production 
environment, use -Dspring.profiles.active=production in order to completely
hide the Swagger UI.

Missing features:

* Security in terms of authentication and authorization.

* Search for configurations or snapshots. This will be added next.

* JPA as persistence framework, which would make transition to database engines
other than Postgresql or Mysql easier, and also hide some of the issues with
differences in SQL dialects.

Build and run:

The project was developed using Spring STS 3.9.5 (an Eclipse IDE clone).

Being a Spring Boot application, one may build from Maven command line and 
then launch the jar artifact assembled by the build system. The following 
parameters (environment variables) must be specified on the command line:

* -Ddbengine=[postgresql | mysql]. 

* -Dspring.datasource.username=<DB user name>

* -Dspring.datasource.password=<DB password>

