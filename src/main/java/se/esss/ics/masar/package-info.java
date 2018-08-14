/**
 * This project implements the MASAR (MAchine Save And Restore) service as a Spring Boot application.
 * 
 * <h3>Key concepts</h3>
 * 
 * <p>
 * The MASAR service implemented in this project manages two key entities: <b>configurations</b> and <b>snapshots</b>. 
 * </p>
 * <p>
 * A <b>configuration</b> lists the PVs that make up the data set (aka save set) 
 * subject for backup or restore. A PV - identified by it's unique name - can be listed in 
 * multiple configurations. A configuration is identified by its name.
 * </p>
 * <p>
 * A <b>snapshot</b> consists of the data associated by the list of PVs in a configuration, and that is read at the time the snapshot was requested by the user. 
 * An arbitrary number of time stamped snapshots can be created for each configuration.</p>
 * 
 * <p>
 * Configurations and snapshots are persisted to a database using a tree model. Each node in the tree is either a "folder" or a configuration. Folders may contain sub-folders
 * and configurations. A snapshot is however <i>not</i> treated as a node in the tree. Consequently a configuration node cannot have child nodes. The service always contains a (folder) root node
 * that cannot be deleted.
 * </p>
 * 
 * <p>
 * Nodes can be moved in the same manner as objects in a file system. A folder can be moved to another parent folder, or a configuration (together with its snapshots) can be moved
 * to a different folder. 
 * Since snapshots are not treated as nodes, they cannot be moved to a different configuration. That would not make sense anyway as the data in the snapshot is linked to the list of PVs in the
 * associated configuration.
 * </p>
 * 
 * <p>
 * A configurations can be updated by changing its name or by modifying its list of PVs. When PVs are deleted from the configuration, the saved data for those PVs in the snapshots will
 * also be deleted.
 * </p>
 * 
 * <p>
 * When a configuration is deleted, all associated snapshots are also deleted. When a folder is deleted, the entire sub-tree of that folder is also deleted. Clients
 * should hence caution the user of the consequences of deleting configurations and folders.
 * </p>
 * 
 * <h3>The service</h3>
 * <p>
 * All operations needed to manage the MASAR data are provided as HTTP end-points in a RESTful service. The list of operations includes creating, updating and deleting nodes in the
 * tree, as well as perform save (i.e. take snapshot). Restore operations are not explicitly provided and should instead be executed by a client application based on the saved data in a snapshot.
 * </p>
 * 
 * <h3>Database implementation</h3>
 * <p>
 * A tree as described above can be modeled in various ways in a relational database. This implementation uses a closure table to hold relations between the
 * nodes in the tree.
 * See <a href="https://www.slideshare.net/billkarwin/models-for-hierarchical-data" target="_blank">this presentation</a> for a discussion of various models (including the closure table approach).
 * </p>
 * 
 * <p>
 * The service has been tested with Postgres 9.6.
 * The in-memory database H2 (ver 1.4) is used for unit testing.
 * </p>
 */
package se.esss.ics.masar;