# Band Tracker

#### Advanced Databases Independent Project for Epicodus, 05/13/2016

#### By **Kevin Deganos**

## Description

Website for tracking bands and venues. A user can add bands and venues as well as associate venues with bands. Bands and venues are searchable.

## Setup/Installation Requirements

* This website was created with Java, Spark Framework, Apache Velocity, Bootstrap, Animate.css, and PostgreSQL
* In PSQL:
  * CREATE DATABASE band\_tracker;
  * \\c band\_tracker;
  * CREATE TABLE bands (id serial PRIMARY KEY, name varchar);
  * CREATE TABLE venues (id serial PRIMARY KEY, name varchar);
  * CREATE TABLE bands_venues (id serial PRIMARY KEY, band_id int, venue_id int);
  * CREATE DATABASE band\_tracker\_test WITH TEMPLATE band\_tracker;

### License

*Code released under the MIT license.*

Copyright (c) 2016 **Kevin Deganos**
