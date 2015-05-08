# Quick Start Guide for Query Debugger Plugin #

## Installation ##

Download [Protege+Debugger](http://rmbd.googlecode.com/files/protegeAndDebugger.zip) and install it. You need Java 1.7 for the Tool to work. This is Protege 4.2 beta 278 with a nightly build newer 20120918 from Hermit Plugin. The Query Debugger Plugin is alpha from 20121031. We provide this package because of a bug in HermiT provided with Protege beta 278.

## Usage ##

In the menu above is a new item called debug. Open the debug menu and choose "Open Ontology Debug Tab" or "Open Interactive Debug Tab".

![http://wiki.rmbd.googlecode.com/hg/images/debugMenu.png](http://wiki.rmbd.googlecode.com/hg/images/debugMenu.png)

### Using Ontology Debug Tab ###

You can click on **Calculate Diagnoses** in the menu to get diagnoses and hitting sets calculated. If you want to get all diags you have to mark the _calc all diagnoses_ checkbox  in **Options**

After calculating diagnoses you can see the diagnoses and conflicts which were found. On default, only 9 diagnoses are calculated because this is normally enough to discriminate using our interactive approach.

![http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabDiagsCalculated.png](http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabDiagsCalculated.png)

Now on the left side you can specifiy some Testcase by clicking on the plus sign right from **Entailed Testcases** and **Not Entailed Testcases**.

When looking at the axioms in conflicts and diagnoses one see some intuitively correct axioms. In my opinion, marsupials are disjoint from person and a koala is some sort of marsupials. So I clicked on the plus sign right from **Entailed Testcases** and specified a testcase containing **Marsupials DisjointWith Person**. Then I did the same for **Koala SubClassOf Marsupials**.

After clicking **Calculate Diagnoses** in the menu you get a screenshot like the following.

![http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabSomeTestCases.png](http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabSomeTestCases.png)

Now you can see that the number of possible diagnoses is smaller but there are still many to choose from. Now I decide that also animals could be hard working individuals so I added a **Not Entailed Testcase** containing **isHardWorking Domain Person**. After recalculating diagnoses, there are only two diagnoses to choose from.

![http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabAllTestCases.png](http://wiki.rmbd.googlecode.com/hg/images/ontoDebugTabAllTestCases.png)

At this point one can decide that the concept KoalaWithPhD is not well choosen and therefore identify the first diagnoses as the correct one.

## Using Interactive Debug Tab ##

When you click on **Get Query** a new query is displayed. You can choose the axioms you want to have entailed or not entailed with the plus and minus buttons. To confirm the choices click on **Commit**. The axioms are added to the corresponding entailments section and a new query is calculated and displayed.

Here you see a query session:

**Answering first query:**

![http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFirstQuery.png](http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFirstQuery.png)

**Answering second query:**

![http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabSecondQuery.png](http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabSecondQuery.png)

**Answering third query:**

![http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabThirdQuery.png](http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabThirdQuery.png)

**Answering fourth query:**

![http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFourthQuery.png](http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFourthQuery.png)

**Session finished**

![http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFinish.png](http://wiki.rmbd.googlecode.com/hg/images/interactiveDebugTabFinish.png)

With the answering of only 4 queries we have found the target diagnosis. Also it is enough to answer parts of queries which one really understand.