# Overview of code modules #

The code is organised as a [Maven](http://maven.apache.org/) multi-module project.

## Main module: rmbd ##

The main module **rmbd** is splitted up in submodules:


### diagnosis ###

This module is responsible for calculating diagnoses und conflicts using our methods. It also contains the code for generating queries.

This Module is abstract. You need to derive a module with the implementation for the logic one want to use.


### choco2, owlapi-3, sat4j ###

These modules are concrete implementations of **diagnosis** for OWL(owlapi-3), SAT(sat4j) and Constraints(choco2).

### protegeview ###

This module uses **diagnosis** and **owlapi-3** to implement a debugging plugin for the Protege OWL Editor.

### logging, usrstudlogger ###

These modules are used for logging and for special logging purposes in experiments. You don't need to look at this modules to understand our approach.


# Download & install software #

  1. You need to download [Maven](http://maven.apache.org/download.html) and [Mercurial](http://mercurial.selenic.com/downloads/).

> 2 After you installed the software you can check out the code with
`hg clone https://code.google.com/p/rmbd/`

> 3 You can do `mvn install` in the root directory of the project to download all necessary libs and also compile the software.


# Using the library #

As explained before you only need to look at the diagnosis module and at the module which implements the type of knowledge base you want to debug.

So if you e.g. want to use constraints you only have to look at **diagnosis** and **choco2** module.

In this wiki most of the examples are using **diagnosis** and **owlapi-3**.

To understand what you need to do and how it works it is the best to look at some of the testcase.

# Running Testcases #

In the **owlapi-3** module in **src/test/java/at/ainf/owlapi-3/test** are JUnit Tests which we use to test our software. If you look at these files you get an idea how the software works.

There is a JUnit test called **searchKoalaTest()** in [WikiExamplesTest.java](http://code.google.com/p/rmbd/source/browse/owlapi-3/src/test/java/at/ainf/owlapi3/test/WikiExamplesTest.java#44) which shows the steps do a diagnosis search.