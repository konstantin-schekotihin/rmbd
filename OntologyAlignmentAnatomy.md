# Description of the Approach #

A more detailed description of the risk optimization approach to ontology debugging (RIO) can be downloaded [here](http://www.easychair.org/conferences/submission_download.cgi?a=1930827;track=56863;submission=1078727). As to the experiments, this draft gives a complete description of EXP-1 and EXP-2 and provides additional results. Moreover, it features a more detailed discussion of the results observed throughout all experiments. An explanation and all results of EXP-3 and EXP-4 can be found below.

# Ontology Alignment: ANATOMY Testcase #

## Experiment description ##

The experiment was performed on the set of ontologies from the ANATOMY track in OAEI 2011.5. The raw data representing the output of matching systems was downloaded from the [results page](http://web.informatik.uni-mannheim.de/oaei/anatomy11.5/results.html). The reference alignment as well as the source ontologies Mouse and Human were downloaded from the OAEI 2011 [Anatomy page](http://web.informatik.uni-mannheim.de/oaei/anatomy11/index.html).

The goal of the experiment was to test the scalability of our risk optimization debugging tool w.r.t. ontology alignments. The size of the used alignments (produced by different ontology matching systems) was between 1147 and 1461 correspondences. The ontologies, for which the alignments were calculated, include 11545 (Human) and 4838 (Mouse) axioms, respectively.

Given the ontologies ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1%7D&chs=13&.png) (Human) and ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B2%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B2%7D&chs=13&.png) (Mouse), the output ![http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D&chs=13&.png) of an ontology matcher and the correct (reference) alignment ![http://chart.apis.google.com/chart?cht=tx&chl=R_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=R_%7B12%7D&chs=13&.png), we first fixed the target diagnosis ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png) as follows: Both ontologies ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1%7D&chs=13&.png) and ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B2%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B2%7D&chs=13&.png) as well as the correctly extracted alignments ![http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Ccap%20R_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Ccap%20R_%7B12%7D&chs=13&.png) were considered as correct, i.e. placed in the background knowledge ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BB%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BB%7D&chs=13&.png). The rest of the correspondences ![http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Csetminus%20R_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Csetminus%20R_%7B12%7D&chs=13&.png), which are not in the reference alignment ![http://chart.apis.google.com/chart?cht=tx&chl=R_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=R_%7B12%7D&chs=13&.png), were analyzed by the debugger. In this way, we identified a set of diagnoses, where each diagnosis is a subset of ![http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Csetminus%20R_%7B12%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=M_%7B12%7D%20%5Csetminus%20R_%7B12%7D&chs=13&.png). From this set of diagnoses, we randomly selected one diagnosis as the target diagnosis ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png). For each selected target diagnosis, we produced a text file which describes the axioms in this diagnosis in the format which was used for the reference alignments in the [Ontology Alignment Evaluation Initiative 2006](http://web.informatik.uni-mannheim.de/ontdebug) by Christian Meilicke. These text files can be found at [target diagnoses](http://code.google.com/p/rmbd/source/browse/owlcontroller/src/test/resources/oaei11/) (.txt files).

After the target diagnosis ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BD%7D_%7Bt%7D&chs=13&.png) was fixed, we started the actual experiments, i.e. EXP-3 and EXP-4. In the following, we use the term aligned ontology for ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1M2%7D%20%3A%3D%20%5Cmathcal%7BO%7D_1%20%5Ccup%20M_%7B12%7D%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=14&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1M2%7D%20%3A%3D%20%5Cmathcal%7BO%7D_1%20%5Ccup%20M_%7B12%7D%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=14&.png). For both EXP-3 and EXP-4, one test run was performed for the aligned ontology of each matching system analyzed in the ANATOMY track. The matching systems under consideration were the following: AgrMaker, GOMMA-bk, CODI, LogMap, GOMMA-nobk, MapSSS, LogMapLt, Lily, MapEVO, Aroma, CSA and MaasMtch. In each test run a debugging session was performed, where the entire set of axioms in the aligned ontology ![http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1M2%7D&chs=13&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Cmathcal%7BO%7D_%7B1M2%7D&chs=13&.png) were analyzed for faults and the background knowledge was empty. The parameters of RIO were set as follows: the cautiousness ![http://chart.apis.google.com/chart?cht=tx&chl=c%20%3A%3D%200.25&chs=12&.png](http://chart.apis.google.com/chart?cht=tx&chl=c%20%3A%3D%200.25&chs=12&.png) and cautiousness interval ![http://chart.apis.google.com/chart?cht=tx&chl=%5B%5Cunderline%7Bc%7D%2C%5Coverline%7Bc%7D%5D%20%20%5C%3B%3A%3D%5B0%2C%7B0.5%7D%5D&chs=16&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5B%5Cunderline%7Bc%7D%2C%5Coverline%7Bc%7D%5D%20%20%5C%3B%3A%3D%5B0%2C%7B0.5%7D%5D&chs=16&.png). The applied search strategy was uniform cost search and the number of leading diagnoses ![http://chart.apis.google.com/chart?cht=tx&chl=%7C%5Cbf%7BD%7D%7C&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=%7C%5Cbf%7BD%7D%7C&chs=15&.png) considered by the debugger at a time was set to ![http://chart.apis.google.com/chart?cht=tx&chl=%7C%5Cbf%7BD%7D%7C%20%3A%3D%209&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=%7C%5Cbf%7BD%7D%7C%20%3A%3D%209&chs=15&.png). The termination threshold was set to ![http://chart.apis.google.com/chart?cht=tx&chl=%5Csigma%20%3A%3D%200.85&chs=12&.png](http://chart.apis.google.com/chart?cht=tx&chl=%5Csigma%20%3A%3D%200.85&chs=12&.png).

#### EXP-3 ####
In this experiment, in order to simulate a case where fault probabilities are specified favorably, we set ![http://chart.apis.google.com/chart?cht=tx&chl=p(ax_k)%20%3A%3D%200.001&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=p(ax_k)%20%3A%3D%200.001&chs=15&.png) for ![http://chart.apis.google.com/chart?cht=tx&chl=ax_k%20%5Cin%20%5Cmathcal%7BO%7D_1%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=ax_k%20%5Cin%20%5Cmathcal%7BO%7D_1%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=15&.png) and ![http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%3A%3D%201-v_%7Bm%7D&chs=14&.png](http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%3A%3D%201-v_%7Bm%7D&chs=14&.png) where ![http://chart.apis.google.com/chart?cht=tx&chl=v_%7Bm%7D&chs=11&.png](http://chart.apis.google.com/chart?cht=tx&chl=v_%7Bm%7D&chs=11&.png) is the confidence value of the correspondence ![http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%5Cin%20M_%7B12%7D&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%5Cin%20M_%7B12%7D&chs=15&.png) provided by the ontology matching system.

#### EXP-4 ####
In this experiment, on the contrary, we simulated unreasonable fault probabilities by defining ![http://chart.apis.google.com/chart?cht=tx&chl=p(ax_k)%20%3A%3D%200.01&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=p(ax_k)%20%3A%3D%200.01&chs=15&.png) for ![http://chart.apis.google.com/chart?cht=tx&chl=ax_k%20%5Cin%20%5Cmathcal%7BO%7D_1%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=15&.png](http://chart.apis.google.com/chart?cht=tx&chl=ax_k%20%5Cin%20%5Cmathcal%7BO%7D_1%20%5Ccup%20%5Cmathcal%7BO%7D_2&chs=15&.png) and ![http://chart.apis.google.com/chart?cht=tx&chl=p(ax_m)%20%3A%3D%200.001&chs=16&.png](http://chart.apis.google.com/chart?cht=tx&chl=p(ax_m)%20%3A%3D%200.001&chs=16&.png) for ![http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%5Cin%20M_%7B12%7D&chs=14&.png](http://chart.apis.google.com/chart?cht=tx&chl=ax_m%20%5Cin%20M_%7B12%7D&chs=14&.png).

## Results ##

The results of the experiments run on a Core-i7 (3930K) 3.2Ghz, 32GB RAM with Ubuntu Server 11.04 and Java 6 installed are given in the following Table.

| **EXP** 	| **matcher** 	| **strategy** 	| **debug** 	| **q** 		| **react** 	|
|:---------|:-------------|:--------------|:-----------|:--------|:-----------|
| 3 		| AgrMaker 	| ENT 		| 25471 	| 4 	| 4956 	|
| 3 		| AgrMaker 	| SPL 		| 65733 	| 16 	| 3817 	|
| 3 		| AgrMaker 	| RIO 		| 23209 	| 3 	| 5923 	|
| 3 		| GOMMA-bk 	| ENT 		| 16949 	| 1 	| 9920 	|
| 3 		| GOMMA-bk 	| SPL 		| 51645 	| 15 	| 3226 	|
| 3 		| GOMMA-bk 	| RIO 		| 16868 	| 1 	| 10072 	|
| 3 		| GOMMA-nobk 	| ENT 		| 16628 	| 1 	| 9704 	|
| 3 		| GOMMA-nobk 	| SPL 		| 50801 	| 15 	| 3171 	|
| 3 		| GOMMA-nobk 	| RIO 		| 16915 	| 1 	| 9995 	|
| 3 		| Lily		 	| ENT 		| 57009 	| 1 	| 31058 	|
| 3 		| Lily		 	| SPL 		| 183481 	| 17 	| 10115 	|
| 3 		| Lily			| RIO 		| 55716 	| 1 	| 31462 	|
| 3 		| LogMap	 	| ENT 		| 13997 	| 1 	| 13047 	|
| 3 		| LogMap	 	| SPL 		| 66802 	| 23 	| 2707 	|
| 3 		| LogMap	 	| RIO 		| 14278 	| 1 	| 13343 	|
| 3 		| LogMapLt	 	| ENT 		| 18658 	| 1 	| 9750 	|
| 3 		| LogMapLt	 	| SPL 		| 55503 	| 15 	| 3499 	|
| 3 		| LogMapLt	 	| RIO 		| 20318 	| 1 	| 10070 	|
| 3 		| MapSSS	 	| ENT 		| 277781 	| 32 	| 8135 	|
| 3 		| MapSSS	 	| SPL 		| 269403 	| 35 	| 6964 	|
| 3 		| MapSSS	 	| RIO 		| 288716 	| 30 	| 8912 	|
| 4 		| AgrMaker 	| ENT 		| 80417 	| 18 	| 3987 	|
| 4 		| AgrMaker 	| SPL 		| 70402 	| 16 	| 4255 	|
| 4 		| AgrMaker 	| RIO 		| 73903 	| 16 	| 4340 	|
| 4 		| GOMMA-bk 	| ENT 		| 20158 	| 8 	| 2277 	|
| 4 		| GOMMA-bk 	| SPL 		| 62802 	| 19 	| 3097 	|
| 4 		| GOMMA-bk 	| RIO 		| 32729 	| 5 	| 5635 	|
| 4 		| GOMMA-nobk 	| ENT 		| 20145 	| 8 	| 2282 	|
| 4 		| GOMMA-nobk 	| SPL 		| 63086 	| 19 	| 3112 	|
| 4 		| GOMMA-nobk 	| RIO 		| 32422 	| 5 	| 5590 	|
| 4 		| Lily		 	| ENT 		| 125924 	| 9 	| 11979 	|
| 4 		| Lily		 	| SPL 		| 221379 	| 21 	| 9861 	|
| 4 		| Lily			| RIO 		| 143814 	| 6 	| 21315 	|
| 4 		| LogMap	 	| ENT 		| 16865 	| 2 	| 8280 	|
| 4 		| LogMap	 	| SPL 		| 41290 	| 11 	| 3557 	|
| 4 		| LogMap	 	| RIO 		| 21931 	| 2 	| 9874 	|
| 4 		| LogMapLt	 	| ENT 		| 33826 	| 7 	| 4128 	|
| 4 		| LogMapLt	 	| SPL 		| 54774 	| 17 	| 3027 	|
| 4 		| LogMapLt	 	| RIO 		| 30101 	| 6 	| 4467 	|
| 4 		| MapSSS	 	| ENT 		| 223903 	| 31 	| 6470 	|
| 4 		| MapSSS	 	| SPL 		| 174793 	| 25 	| 6559 	|
| 4 		| MapSSS	 	| RIO 		| 133362 	| 17 	| 7070 	|

**matcher** name of the matching system which produced the alignment, **strategy** applied query selection strategy (ENT = entropy, SPL = split-in-half), **debug** total time (ms) of a debugging session, **q** number of queries required to determine the target diagnosis, **react** average computing time (ms) between two successive queries.

Note that five matching systems, i.e. CODI, CSA, MaasMtch, MapEVO and Aroma, which took part in the OAEI 2011.5 could not be analyzed in the experiment. This was due to a consistent output produced by CODI and the problem that the reasoner was not able to ﬁnd a model within acceptable
time (2 hours) in the case of CSA, MaasMtch, MapEVO and Aroma. Similar reasoning problems were also reported in `[2]`.


### Average times and number of queries ###

**debug** total time (ms) of a debugging session, **q** number of queries required to determine the target diagnosis, **react** average computing time (ms) between two successive queries.

#### EXP-1 ####

| **strategy** | **debug** 	| **react** 	| **q** 		|
|:-------------|:-----------|:-----------|:--------|
|ENT    | 1860 		| 262 	| 3.67 		|
|SPL    | 1427 		| 159 	| 5.70 		|
|RIO    | 1592 		| 286 	| 3.00 		|

#### EXP-2 ####

| **strategy** | **debug** 	| **react** 	| **q** 		|
|:-------------|:-----------|:-----------|:--------|
|ENT    | 1423 		| 204 	| 5.26 		|
|SPL    | 1237 		| 148 	| 5.44 		|
|RIO    | 1749 		| 245 	| 4.37 		|

#### EXP-3 ####

| **strategy** | **debug** 	| **react** 	| **q** 		|
|:-------------|:-----------|:-----------|:--------|
|ENT    | 60928		| 12367 	| 5.86 		|
|SPL    | 104910		| 4786 	| 19.43 		|
|RIO    | 62289 		| 12825 	| 5.43 		|

#### EXP-4 ####

| **strategy** | **debug** 	| **react** 	| **q** 		|
|:-------------|:-----------|:-----------|:--------|
|ENT    | 74463 		| 5629 	| 11.86 		|
|SPL    | 98647 		| 4781 	| 18.29 		|
|RIO    | 66895 		| 8327 	| 8.14 		|


## Reproduction ##

In order to run the tests and reproduce the results, have a look at [Anatomy experiment description](OntologyAlignmentAnatomyExperiment.md) for further instructions.

## Acknowledgements ##

We would like to thank to Christian Meilicke, who supported us in all ontology alignment experiments.

## References ##

`[1]` Christian Meilicke. Alignment Incoherence in Ontology Matching. University of Mannheim, Chair of Artificial Intelligence, 2011.

`[2]` Jerome Euzenat et al. Final results of the Ontology Alignment Evaluation Initiative 2011. Proceedings of the 6th International Workshop on Ontology Matching, Bonn, Germany, 2011.