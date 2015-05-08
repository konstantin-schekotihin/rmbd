## Prototypes for debugging non-ground monotonic ASP ##

Examples for inconsistent monotonic ASP programs: [house.lp](http://wiki.rmbd.googlecode.com/hg/other/house_p02t002.lp), [ppm.lp](http://wiki.rmbd.googlecode.com/hg/other/gen-t05p03.lp), [pup.lp](http://wiki.rmbd.googlecode.com/hg/other/pup-24.asp)

Benchmark problem instances: [house.zip](http://wiki.rmbd.googlecode.com/hg/other/instances-house.zip), [ppm.zip](http://wiki.rmbd.googlecode.com/hg/other/instances-ppm.zip), [pup.zip](http://wiki.rmbd.googlecode.com/hg/other/instances-pup.zip)

### Hitting Set-tree approach ###

Download: [monotonic-asp-hstree-1.0.jar](http://wiki.rmbd.googlecode.com/hg/other/monotonic-asp-hstree-1.0.jar) (1.9 MB)

Source Code: [Module mdebugging-hstree](https://code.google.com/p/rmbd/source/browse/#hg%2Fmdebugging-hstree)

Requirements: [Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html) or higher, [Clingo 4.\*](http://sourceforge.net/projects/potassco/files/clingo/)

Make sure that _clingo_ is in your path (for UNIX) or in the current directory (for WINDOWS).
Otherwise it is possible to set the path manually (see below).

To debug a monotone answer set program use the following parameter: [-file] or [-f] followed by the file to debug.

To show more information about the debugging process use the following parameters: [-enableInfo] or [-i] followed by [-file] or [-f] followed by the file to debug.

There is the possibility to set the path of the program: [-clingo] or [-c] followed by the clingo path.

For example:
  * `java -jar monotonic-asp-hstree-1.0.jar -file example.lp` or

  * `java -jar monotonic-asp-hstree-1.0.jar -enableInfo -f example.lp` or

  * `java -jar monotonic-asp-hstree-1.0.jar -clingo /usr/bin/clingo -file example.lp` or

  * `java -jar monotonic-asp-hstree-1.0.jar -i -c /usr/bin/clingo -f example.lp`


### ASP approach ###

Download: [monotonic-asp-asp-1.0.jar](http://wiki.rmbd.googlecode.com/hg/other/monotonic-asp-asp-1.0.jar) (1.7 MB)

Source Code: [Module mdebugging-asp](https://code.google.com/p/rmbd/source/browse/#hg%2Fmdebugging-asp)

Requirements: [Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html) or higher, [Clingo 4.\*](http://sourceforge.net/projects/potassco/files/clingo/)

Make sure that _clingo_ is in your path (for UNIX) or in the current directory (for WINDOWS).
Otherwise it is possible to set the path manually (see below).

To debug a monotone answer set program use the following parameter: [-file] or [-f] followed by the file to debug.

There is the possibility to set the path of clingo: [-clingo] or [-c] followed by the clingo path.

For example:
  * `java -jar monotonic-asp-asp-1.0.jar -file example.lp` or

  * `java -jar monotonic-asp-asp-1.0.jar -clingo /usr/bin/clingo -f example.lp`