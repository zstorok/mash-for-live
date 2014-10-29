Mash for Live
=============

There is an abundance of free music available online that just begs to be remixed and mashed up. However, the process of downloading tracks, slicing them into usable pieces and laying them out in Ableton Live is tedious and time consuming. Why not let a computer do the boring part so us humans can concentrate on being creative?

The goal of this project is to create an online tool for finding freely downloadable tracks from SoundCloud and compiling them into an Ableton Live set with pre-sliced clips. The slicing is based on the analysis results retrieved from the Echo Nest API.

This project started out as a quick and dirty hack for Music Hack Day Berlin 2014, but since several people expressed their interest, I decided to keep polishing it a bit further. 

Live demo
---------
http://mashforlive.com

Requirements
------------
* Java 8

Building from source
--------------------
Mash for Live uses Maven for its build system. In order to create a standalone JAR, simply run `mvn clean package` in the hu.zstorok.mashforlive directory. The resulting `mashforlive-x.y.z-SNAPSHOT.jar` will be placed in the `target` subdirectory, where the `x.y.z` part depends on the current version of the application.

Running
-------
Once you have built the application from source, you can run it by typing e.g. `java -jar target/mashforlive-0.0.1-SNAPSHOT.jar`. 

Go to [http://localhost:8080](http://localhost:8080) to start using Mash for Live.
