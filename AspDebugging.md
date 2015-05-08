# ASP Debugging #

The current project includes three modules for ASP debugging:
  * [mdebugging-hstree](MDebugging.md) and [mdebugging-asp](MDebugging.md) modules are two prototypes for debugging non-ground monotonic answer-set programs by computing minimal diagnoses. The former is based on Hitting Set-tree computation; the latter is based on ASP itself. The publication including more information about the prototypes will be submitted in January 2015.

  * [interactive-asp](IDebugging.md) module is a prototypical implementation of an interactive ASP debugger (please see [paper](http://arxiv.org/abs/1403.5142) for details) which extends [spock framework](http://www.kr.tuwien.ac.at/research/systems/debug/index.html). Currently, we a focusing on extension of this module as a part of Ouroboros plug-in of [SeaLion IDE](http://www.sealion.at/) for ASP.

Compilation of the modules can be done as described in GettingStarted