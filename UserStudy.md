This page presents preliminary results of a user study. The paper is in progress.

# User study #

The goal of the user study was to investigate the effects of [interactive ontology debugging](http://wiki.rmbd.googlecode.com/hg/publications/jws11.pdf) and to find answers to the following questions:
  * Can a user identify a target diagnosis from a list of given diagnoses?
  * Does automatic generation of queries allow to reduce time and effort, respectively, of a diagnosis session?
  * When do users make errors more often: When answering automatically generated queries or when specifying test cases manually?

## Before the experiment ##

The participants of the user study were the students of the Alpen-Adria University attending the "Knowledge representation and reasoning" course. The course includes a series of lectures covering Description Logic and Semantic Web technologies. During the course students learn how to formalize knowledge in OWL language as well as basic reasoning techniques. The accompanying practical course included 2 exercise sheets comprising exercises introducing the ontologies, Protege ontology editor and basic debugging techniques, which were then used in the study. In addition, we organized a one-day workshop for all participants of the study introducing advanced ontology modeling and debugging techniques.

## Experiment setup ##

Each of 20 participants got 2 ontologies:
  * O1: Model of the [Alpen-Adria University Klagenfurt](http://wiki.rmbd.googlecode.com/hg/ontologies/uni.owl)
  * O2: Model of the [Carinthian Communal IT Centre](http://wiki.rmbd.googlecode.com/hg/ontologies/gizk.owl)

Both ontologies were extensively studied by the participants during the practical course as described above. The supplementary materials included a structured natural language representation (in [English](http://wiki.rmbd.googlecode.com/hg/ontologies/uni.pdf) for O1 and in [German](http://wiki.rmbd.googlecode.com/hg/ontologies/gizk.pdf) for O2) of each ontology where all named classes, individuals and properties were marked in text. Each sentence represented some axiom in the ontology. In each ontology we introduced faults by modifying some of the axioms. The target diagnosis included all and only modified axioms. One could find the target diagnosis by simply verifying whether an axiom in the ontology represents the corresponding sentence correctly.

The ontology metrics were as follows:
|                          | **O1**  | **O2**  |
|:-------------------------|:--------|:--------|
|Number of logical axioms  | 142 | 140 |
|Number of classes         | 66  | 64  |
|Expressivity              | SROIQ | SROIQ |
|Number of conflict sets   | 6   | 7 |
|Cardinality of all (set-minimal) conflict sets | 3, 3, 3, 6, 8, 10 | 3, 3, 4, 5, 5, 6, 7 |
|Cardinality of target diagnosis | 5 | 6 |
|Number of all (set-minimal) diagnoses | 1296 | 1045 |

SROIQ - full expressivity of OWL 1.1

Each of the two debugging sessions had to be conducted by means of a different debugging method: with (_withQ_) or without queries (_withoutQ_).
In the first case, a participant was required to debug the ontology using automatic query generation/selection and manual modification of test cases was not allowed. An automatically generated query was (in this experiment) a set of explicit ontology axioms that guaranteed to both eliminate and leave valid at least one diagnosis. Among a set of query candidates that all had this property, one with optimal information entropy was automatically selected. In this vein, a query can be seen as an automatically created test case that has to be classified by the user as positive (all axioms in it should be entailed by the correct ontology) or negative (at least one axioms in it should not be entailed by the correct ontology). This should prevent the user from spending time specifying useless test cases, i.e. those that do not rule out any diagnoses.
In the second case, automatic query generation/selection was disabled and a participant had to analyze the conflict sets and diagnoses returned by the engine and add/modify test cases manually.

The task of each debugging session was to pinpoint the target diagnosis that we preliminarily fixed (see above) and that was unknown to the participants. In order for each user to solve an identical task, the participants should use the structured natural language texts of the **correct** ontologies O1 and O2 which they had been extensively studying in the practical course (see above). These ontology descriptions should serve the participants as a means to answer queries correctly (all query answers were given in the text) and to specify test cases correctly (i.e. translate a structured sentence to OWL). A debugging session was finished after a participant had committed oneself to a particular diagnosis.

For the experiment we used a within-participants design, i.e. each participant had to perform two debugging sessions, one with each of the described ontologies. In order to level out the influence of the particular ontology used and the potential carry-over effects such as increased tiredness or improved debugging skills in the second session resulting from the first session, we used a factorial design. Thereby, two factors were varied:
  * the order of ontologies, i.e. which ontology in {O1, O2} a user had to debug first;
  * the order of the debugging method to use, i.e. which method in {_withQ_, _withoutQ_} a user had to apply first.

Measurements:
  * Time of a debugging session
  * Number of mouse clicks made during a debugging session
  * Whether a participant found the correct target diagnosis, i.e. whether at least one fault was made (in specifying test cases (_withoutQ_); in answering queries (_withQ_))

## Results ##
### Time ###
For all participants (_withQ_ vs. _withoutQ_):
  * avg: 26.86 vs. 33.2 minutes
  * T-Test, paired: p-value = 0.06 (still significant)

That is, _withQ_ requires significantly less time than _withoutQ_.

Only for participants who found correct target diagnosis in both tasks (11 participants, _withQ_ vs. _withoutQ_):
  * avg: 25.36 vs. 29.93 minutes
  * T-Test, paired: p-value = 0.22 (not significant)


### Mouse clicks ###
For all participants (_withQ_ vs. _withoutQ_):
  * avg: 65 vs. 142
  * T-Test, paired: p-value = 0.0002  (very significant)

Only for participants who found correct target diagnosis in both tasks (11 participants, _withQ_ vs. _withoutQ_):
  * avg: 65 vs. 157
  * T-Test, paired: p-value = 0.001 (strongly significant)


### Found ###
Number of participants who found the target diagnosis correctly.

|  _withQ_    | _withoutQ_ |  |
|:------------|:-----------|:-|
|   not found | not found  | 1|
|   found     | not found  | 4|
|   not found | found      | 4|
|   found     | found      | 11|


These numbers show that there were 12 users (rows 1 and 4) that either faced problems in both or none of the sessions. Of these, 11 (row 4) understood (the meaning of) axioms in ontology, diagnoses, conflict sets (_withoutQ_) **and** queries (_withQ_) w.r.t. the structured natural language descriptions of the correct ontologies very well. One participant (row 1) obviously had fundamental problems and did not succeed in any setting _withQ_ or _withoutQ_. In other words, the automatic query engine did not help him/her to determine the target diagnosis.

Among those participants that made errors only when applying one of the methods in {_withQ_, _withoutQ_}, four (row 2) succeeded only using _withQ_ and equally many (row 3) succeeded only using _withoutQ_. This could either (1) suggest that none of the methods _withQ_ or _withoutQ_ is generally the better choice in terms of error rate, or (2) be due to a specific problem of understanding with the ontology O2. Namely, all of the seven participants who did not succeed in only one session, did not succeed with ontology O2, but did succeed with ontology O1, independently of the used debugging method. Also, the fact whether it was the first or second ontology to debug for a participant (i.e. potential tiredness) seems to have had no influence on the errors – four failed in the prior and four in the later session.

This is another hint that automatic test case generation (where test cases are sets of explicit ontology axioms) cannot improve the error rate in interactive debugging in a case where users have comprehension problems with logical sentences in general (row 1) or with the ontology as such (rows 2 and 3). Therefore, what it needs to reduce the error rate are probably (1) other types of queries, e.g. querying entailments of a subset of a faulty ontology that are not explicit ontology axioms may be helpful for the user, or (2) other measures of what is a good (AND easy) query or (3) specific representation methods like laconic or precise justifications or (4) transformation of axioms to natural language.


## Additional experiment ##

In this experiment the question was addressed whether a user can detect the target diagnosis as such if they are directly presented with it in the GUI. The setup was as in the experiment above, but with 29 participants. However, we considered only the _withoutQ_ sessions where users were required to identify the target diagnosis manually. In 50% of these sessions we modified the diagnosis computation procedure such that the target diagnosis appeared within the first three diagnoses that were presented to the user by the system. In this way, the target diagnosis was immediately visible to the user in the GUI (Protégé). In the other 50% of these sessions the target diagnosis was placed at a random position in the list of all diagnoses and, therefore, was often not directly visible in the GUI (i.e. only by scrolling down).

A Chi-Quadrat Test for statistical independence of variables Position (of target diagnosis) and Found (target diagnosis) given the measurements presented in the contingency table below suggests that the two factors are independent (p-value = 0.89). This can be interpreted as a sign that users cannot recognize the set of faulty axioms (target diagnosis) even if it is directly presented in the GUI.

| Position | Found (Yes) | Found (No) |
|:---------|:------------|:-----------|
| Good | 5 | 9 |
| Bad| 5 | 10 |