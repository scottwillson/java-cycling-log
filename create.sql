drop database cycling_log_test;
create database cycling_log_test;
use cycling_log_test;

CREATE TABLE   equipment   (
    id   int(11) NOT NULL auto_increment,
    name   varchar(100) NOT NULL default '',
    userId   int(11) NOT NULL default '0',
  PRIMARY KEY  (  id  )
) TYPE=MyISAM;

CREATE TABLE   users   (
    email   varchar(100) default '',
    lastName   varchar(100) default '',
    firstName   varchar(100) default '',
    username   varchar(255) default NULL,
    password   varchar(255) default NULL,
    id   int(11) NOT NULL auto_increment,
    name   varchar(64) default NULL,
  PRIMARY KEY  (  id  ),
  KEY   id   (  id  )
) TYPE=MyISAM;

CREATE TABLE   users_administrators   (
    userId   int(11) NOT NULL,
    administratorId   int(11) NOT NULL
) TYPE=MyISAM;

CREATE TABLE   weeks   (
    startDate   date NOT NULL default '0000-00-00',
    endDate   date NOT NULL default '0000-00-00',
    notes   varchar(255) NOT NULL default '',
    focus   varchar(16) NOT NULL default '',
    intensity   varchar(16) NOT NULL default '',
    work   float NOT NULL default '0',
    duration   int(11) NOT NULL default '0',
    distance   float NOT NULL default '0',
    morale   float NOT NULL default '0',
    life   float NOT NULL default '0',
    weight   float NOT NULL default '0',
    weather   float NOT NULL default '0',
    id   int(11) NOT NULL auto_increment,
    public_notes   blob,
    userId   int(11) NOT NULL default '0',
  PRIMARY KEY  (  id  )
) TYPE=MyISAM;

create unique index ix_date on weeks(userId, startDate);

CREATE TABLE   workouts   (
    id   int(11) NOT NULL auto_increment,
    notes   blob,
    date   date NOT NULL default '0000-00-00',
    duration   int(11) NOT NULL default '0',
    distance   double NOT NULL default '0',
    speed   double NOT NULL default '0',
    intensity   int(11) NOT NULL default '0',
    morale   int(11) NOT NULL default '0',
    life   int(11) NOT NULL default '0',
    weather   int(11) NOT NULL default '0',
    weight   float NOT NULL default '0',
    work   float NOT NULL default '0',
    equipmentId   int(11) NOT NULL default '0',
    activity   varchar(16) NOT NULL default '',
    race   varchar(16) NOT NULL default '',
    focus   varchar(16) NOT NULL default '',
    weekId   int(11) NOT NULL default '0',
    public_notes   blob,
    userId   int(11) NOT NULL default '0',
  PRIMARY KEY  (  id  ),
  KEY   date   (  date  )
) TYPE=MyISAM;

grant all on cycling_log_test.* TO 'cycling_log'@'localhost';
