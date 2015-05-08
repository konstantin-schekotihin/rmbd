The project can be applied to debugging (diagnosis) of SAT (SAT4J), Constraints (Choco, JSolver) and OWL (any OWLAPI compatible reasoner) knowledge bases. At the moment the project team focuses on development on the OWL debugging module (see [Publications](http://code.google.com/p/rmbd/wiki/Publications) for details). Therefore, CP and SAT modules might be slightly outdated.

Given user _requirements_ such as consistency, coherency of ontology and test cases and a _background knowledge_, which are formulas assumed to be correct, the library computes:
  * a set of _conflicts_, which are irreducible subsets of KB formulas causing violation of requirements/test cases;
  * a set of _diagnoses_, which are minimal subsets of KB that should be changed in order to satisfy all the requirements/test cases.
In the standard implementation conflicts are computed using Junker's QuickXPlain algorithm  whereas diagnoses by a corrected version of the HS-Tree algorithm presented in Reiter's "A Theory of Diagnosis from First Principles".

Note that the test cases can be specified as logical formulas that must be entailed/not entailed by the KB or some other functions/metrics that allow to decide whether KB corresponds to the target one that the user intended to formulate.

In general case the library can return many diagnoses as there may exists multiple ways to repair a KB. Therefore we developed an _interactive debugging algorithm_ that allows a user to reduce the number of diagnoses by answering a number of queries, i.e. whether some set of formulas should be entailed by the target KB or not.