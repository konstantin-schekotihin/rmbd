# Description of the approach #

A draft version of the description can be downloaded [here](http://wiki.rmbd.googlecode.com/hg/publications/drafts/iswc2012_submission_184.pdf).

# Reproduction of results #

## Download & install software ##

Compilation requires Java 6 (or higher version) to be installed on the computer. We tested the software with Sun (Oracle) JDK 1.6.0\_24, but it should also run normally if compiled with other JDKs.

Download and install [Maven](http://maven.apache.org/download.html).

Download the source code [snapshot](http://rmbd.googlecode.com/files/snapshotDirectDiagnosis.zip) of the repository from the time we run the tests. The code that was used to create a snapshot can be retrieved from the  repository using the following command:

```
hg clone https://code.google.com/p/rmbd/ -r 0e663ac7e898
```

(To clone the repository you have to install [Mercurial](http://mercurial.selenic.com/downloads/))

Note that due to a bug in [surefire plug-in 2.12](http://jira.codehaus.org/browse/SUREFIRE-827) individual test cases are not found on Windows. Therefore, we recommend to use the snapshot that includes a workaround.

## Run Tests ##

Execute `mvn install` in the root directory of the project to download all necessary dependencies and to compile the software.

You can execute the tests in the `<project_root>\owlcontroller` directory. All the ontologies and mappings used in the experiments are stored in the repository and the tests are run automatically. The ontologies can also be downloaded as a separate [package](http://rmbd.googlecode.com/files/ontologyDirectDiagnosis.zip).

The tests in the _snapshot_, by default, will be executed for all ontologies listed in Figure 4 (first test) and Table 2 (second test). The list of ontologies considered in each test can be modified in the following property files `30Diagnoses.txt` and `unsolvable.txt` located in    `<project_root>\owlcontroller\src\test\resources\oaei11conference\matchings`.

Running
```
mvn -Dtest=RDFMatchingFileReaderTester#searchOneDiagTime test
```
you search up to 30 diagnoses with both of HS-Tree and Inv-HS-Tree.

Running
```
mvn -Dtest=UnsolvableTests#doTestsOAEIConference test
```
you run query sessions for ontologies only solvable with Inv-HS-Tree.

A directory logs is created in the root directory of the project or in the owlcontroller directory depending on your operating system. In this directory you find `owlcontroller.log` where you can see the results appended at the end of the file.


# Time to calculate Diagnoses #

In this experiment we took a set of ontologies and measured how much time (in seconds) HS-Tree and Inv-HS-Tree need to calculate diagnoses. We stopped after calculation  of 1, 9 and 30 diagnoses and recorded the time.

On this table only ontologies are listed which were solvable by both approaches. Note however that Inv-HS-Tree is able to solve more of the tested ontologies.

## Results ##

(time in seconds)

| **ID** | **matcher** | **Ontology 1** | **Ontology 2** | **HS-Tree 1 Diagnosis** | **HS-Tree 9 Diagnoses** | **HS-Tree 30 Diagnoses** | **Inv-HS-Tree 1 Diagnosis** | **Inv-HS-Tree 9 Diagnoses** | **Inv-HS-Tree 30 Diagnoses** |
|:-------|:------------|:---------------|:---------------|:------------------------|:------------------------|:-------------------------|:----------------------------|:----------------------------|:-----------------------------|
| 0 | agrmaker | edas | iasted | 15.037 | 19.191 | 31.595 | 10.634 | 29.482 | 68.903 |
| 1 | aroma | cmt | iasted | 6.236 | 22.004 | 38.767 | 2.634 | 9.139 | 21.01 |
| 2 | aroma | confof | iasted | 10.721 | 29.485 | 121.252 | 9.78 | 29.021 | 73.403 |
| 3 | aroma | ekaw | iasted | 29.056 | 35.816 | 62.648 | 21.643 | 72.247 | 150.079 |
| 4 | csa | ekaw | iasted | 44.366 | 57.391 | 103.808 | 26.663 | 111.151 | 263.522 |
| 5 | ldoa | cmt | iasted | 23.842 | 29.908 | 36.115 | 17.845 | 49.932 | 115.291 |
| 6 | ldoa | confof | iasted | 44.6 | 65.89 | 93.46 | 20.403 | 91.811 | 192.797 |
| 7 | ldoa | edas | sigkdd | 101.571 | 104.912 | 108.952 | 17.277 | 67.43 | 138.243 |
| 8 | lily | confof | iasted | 3.285 | 6.824 | 10.514 | 2.04 | 7.266 | 18.044 |
| 9 | lily | edas | iasted | 16.208 | 22.976 | 34.07 | 10.926 | 29.17 | 73.47 |
| 10 | mapevo | edas | sigkdd | 0.562 | 2.911 | 2.964 | 0.387 | 1.272 | 1.313 |
| 11 | mappso | cmt | iasted | 24.344 | 28.855 | 41.897 | 16.439 | 47.45 | 104.927 |
| 12 | mappso | confof | iasted | 17.548 | 21.82 | 27.943 | 13.083 | 24.286 | 60.845 |
| 13 | mappso | ekaw | iasted | 23.816 | 49.536 | 67.486 | 17.255 | 76.251 | 172.32 |
| 14 | mapsss | edas | iasted | 21.183 | 23.04 | 34.895 | 11.133 | 21.129 | 36.914 |
| 15 | mapsss | ekaw | iasted | 20.275 | 34.735 | 61.227 | 15.196 | 44.401 | 96.542 |
| 16 | optima | conference | iasted | 4.838 | 8.812 | 9.414 | 2.778 | 9.855 | 12.056 |
| 17 | optima | confof | iasted | 15.038 | 18.67 | 21.185 | 12.348 | 24.115 | 37.967 |
| 18 | optima | ekaw | iasted | 33.278 | 84.185 | 89.053 | 9.087 | 36.18 | 88.959 |
| 19 | yam | edas | iasted | 13.424 | 23.993 | 33.705 | 4.992 | 27.813 | 68.185 |
| 20 | agrmaker | cmt | conference | 43.849 | 46.379 | 56.917 | 20.263 | 52.296 | 63.722 |
| 21 | agrmaker | cmt | confof | 10.506 | 20.16 | 22.525 | 6.611 | 33.096 | 34.569 |
| 22 | agrmaker | conference | confof | 33.647 | 48.86 | 60.661 | 7.381 | 41.389 | 55.252 |
| 23 | agrmaker | conference | ekaw | 73.219 | 78.206 | 80.94 | 35.621 | 61.098 | 82.227 |
| 24 | agrmaker | confof | ekaw | 54.778 | 56.968 | 60.609 | 35.811 | 60.282 | 80.865 |
| 25 | agrmaker | edas | ekaw | 40.242 | 46.738 | 52.783 | 32.389 | 42.617 | 43.316 |
| 26 | agrmaker | edas | sigkdd | 17.091 | 20.616 | 25.935 | 21.439 | 29.762 | 31.648 |
| 27 | aroma | cmt | conference | 49.29 | 68.004 | 79.534 | 30.228 | 56.851 | 76.301 |
| 28 | aroma | cmt | confof | 45.299 | 47.835 | 53.806 | 20.965 | 57.655 | 74.842 |
| 29 | aroma | cmt | edas | 28.883 | 36.357 | 42.625 | 26.326 | 32.574 | 34.053 |
| 30 | aroma | cmt | ekaw | 58.322 | 63.605 | 69.909 | 46.531 | 74.711 | 101.891 |
| 31 | aroma | cmt | sigkdd | 34.968 | 48.944 | 55.007 | 13.487 | 47.995 | 63.305 |
| 32 | aroma | conference | confof | 59.637 | 61.808 | 78.003 | 39.487 | 66.678 | 92.977 |
| 33 | aroma | conference | edas | 46.589 | 65.776 | 70.474 | 28.273 | 63.506 | 64.056 |
| 34 | aroma | conference | ekaw | 40.939 | 48.417 | 55.474 | 32.034 | 56.247 | 82.203 |
| 35 | aroma | confof | edas | 24.24 | 29.656 | 31.656 | 11.311 | 19.499 | 21.039 |
| 36 | aroma | confof | ekaw | 63.745 | 78.044 | 90.147 | 34.087 | 61.477 | 88.308 |
| 37 | aroma | edas | ekaw | 42.315 | 48.684 | 54.666 | 23.034 | 41.086 | 42.868 |
| 38 | aroma | edas | iasted | 74.909 | 81.431 | 87.957 | 41.238 | 51.576 | 51.984 |
| 39 | aroma | edas | sigkdd | 27.342 | 30.352 | 33.427 | 25.606 | 29.277 | 30.355 |
| 40 | aroma | ekaw | sigkdd | 33.687 | 49.176 | 51.298 | 12.26 | 47.587 | 62.77 |
| 41 | cider | cmt | edas | 25.801 | 34.454 | 40.391 | 16.518 | 30.012 | 31.221 |
| 42 | cider | cmt | ekaw | 52.347 | 64.082 | 80.785 | 21.718 | 52.148 | 69.084 |
| 43 | cider | cmt | iasted | 6.681 | 9.003 | 12.174 | 3.844 | 8.781 | 10.186 |
| 44 | cider | cmt | sigkdd | 41.14 | 55.931 | 57.875 | 15.517 | 48.825 | 60.039 |
| 45 | cider | conference | edas | 11.931 | 18.347 | 21.903 | 8.283 | 19.683 | 22.776 |
| 46 | cider | conference | ekaw | 48.208 | 50.434 | 54.551 | 24.681 | 56.358 | 74.995 |
| 47 | cider | confof | edas | 31.43 | 55.551 | 65.873 | 13.71 | 44.194 | 45.67 |
| 48 | cider | confof | ekaw | 45.921 | 57.366 | 61.718 | 37.788 | 63.497 | 88.137 |
| 49 | cider | confof | iasted | 7.221 | 10.319 | 11.857 | 2.993 | 5.016 | 5.873 |
| 50 | cider | edas | ekaw | 49.746 | 67.868 | 72.677 | 40.973 | 58.667 | 59.156 |
| 51 | cider | edas | iasted | 17.861 | 20.605 | 23.78 | 8.816 | 12.438 | 13.577 |
| 52 | cider | ekaw | iasted | 9.803 | 10.9 | 11.461 | 9.075 | 9.501 | 9.653 |
| 53 | cider | ekaw | sigkdd | 25.91 | 28.284 | 30.131 | 8.832 | 25.627 | 27.838 |
| 54 | csa | cmt | conference | 71.022 | 78.712 | 92.371 | 37.385 | 66.725 | 91.832 |
| 55 | csa | cmt | confof | 57.386 | 67.871 | 70.712 | 34.527 | 66.294 | 91.019 |
| 56 | csa | cmt | edas | 74.237 | 82.78 | 87.781 | 42.37 | 66.337 | 67.049 |
| 57 | csa | cmt | ekaw | 56.703 | 61.398 | 67.111 | 31.826 | 59.396 | 81.169 |
| 58 | csa | cmt | iasted | 15.265 | 16.593 | 18.161 | 11.927 | 13.953 | 14.099 |
| 59 | csa | cmt | sigkdd | 45.254 | 49.528 | 53.654 | 27.033 | 58.589 | 73.211 |
| 60 | csa | conference | confof | 231.831 | 234.512 | 277.245 | 44.937 | 78.577 | 117.264 |
| 61 | csa | conference | sigkdd | 38.636 | 47.496 | 48.185 | 20.42 | 45.759 | 47.378 |
| 62 | csa | confof | edas | 85.765 | 102.294 | 119.094 | 53.937 | 119.852 | 143.334 |
| 63 | csa | confof | ekaw | 139.958 | 140.973 | 143.009 | 55.261 | 95.819 | 140.25 |
| 64 | csa | confof | iasted | 8.538 | 10.513 | 12.602 | 4.876 | 9.693 | 10.239 |
| 65 | csa | confof | sigkdd | 77.666 | 89.618 | 105.828 | 50.736 | 79.219 | 117.309 |
| 66 | csa | edas | ekaw | 131.423 | 137.885 | 145.24 | 76.37 | 127.071 | 127.282 |
| 67 | csa | edas | sigkdd | 99.657 | 124.119 | 130.474 | 68.681 | 137.264 | 146.667 |
| 68 | csa | ekaw | sigkdd | 102.834 | 117.79 | 144.566 | 53.865 | 98.02 | 143.944 |
| 69 | csa | iasted | sigkdd | 3.648 | 4.49 | 4.98 | 2.926 | 4.02 | 4.086 |
| 70 | ldoa | cmt | conference | 53.626 | 61.725 | 66.595 | 23.826 | 53.506 | 72.536 |
| 71 | ldoa | cmt | confof | 66.333 | 83.988 | 98.691 | 44.88 | 71.135 | 101.599 |
| 72 | ldoa | cmt | sigkdd | 45.154 | 48.329 | 55.573 | 16.897 | 50.344 | 61.884 |
| 73 | ldoa | conference | edas | 166.345 | 170.274 | 174.994 | 70.524 | 138.149 | 198.218 |
| 74 | ldoa | conference | sigkdd | 46.435 | 48.703 | 50.589 | 28.809 | 51.393 | 62.13 |
| 75 | ldoa | confof | edas | 80.745 | 89.717 | 98.958 | 45.067 | 102.814 | 129.386 |
| 76 | ldoa | confof | ekaw | 92.026 | 108.693 | 217.074 | 47.66 | 79.605 | 121.372 |
| 77 | ldoa | confof | sigkdd | 79.632 | 85.225 | 90.427 | 44.168 | 77.007 | 106.963 |
| 78 | ldoa | edas | ekaw | 141.151 | 150.596 | 159.027 | 84.479 | 161.288 | 209.439 |
| 79 | ldoa | ekaw | sigkdd | 79.729 | 107.256 | 112.616 | 45.679 | 76.033 | 113.152 |
| 80 | lily | cmt | edas | 21.284 | 24.375 | 27.581 | 23.417 | 28.313 | 30.045 |
| 81 | lily | cmt | ekaw | 37.062 | 61.484 | 68.922 | 18.419 | 54.005 | 65.069 |
| 82 | lily | conference | confof | 48.364 | 55.189 | 67.842 | 23.647 | 56.132 | 73.303 |
| 83 | lily | conference | edas | 56.942 | 66.37 | 72.313 | 35.614 | 49.624 | 50.496 |
| 84 | lily | conference | ekaw | 20.836 | 47.211 | 47.676 | 11.177 | 46.751 | 48.358 |
| 85 | lily | edas | ekaw | 23.292 | 28.945 | 35.394 | 19.074 | 28.905 | 29.888 |
| 86 | lily | edas | sigkdd | 8.538 | 13.061 | 17.635 | 10.304 | 16.363 | 19.126 |
| 87 | lily | ekaw | iasted | 14.638 | 15.615 | 17.096 | 12.589 | 14.485 | 14.76 |
| 88 | lily | ekaw | sigkdd | 34.719 | 39.745 | 49.744 | 9.612 | 40.033 | 55.04 |
| 89 | logmap | confof | ekaw | 22.362 | 28.593 | 31.072 | 5.512 | 36.692 | 39.056 |
| 90 | mapevo | cmt | ekaw | 20.613 | 29.829 | 35.466 | 4.662 | 32.071 | 53.456 |
| 91 | mapevo | conference | confof | 37.991 | 49.835 | 58.598 | 10.532 | 45.802 | 56.299 |
| 92 | mapevo | conference | ekaw | 33.825 | 44.702 | 47.028 | 25.269 | 50.456 | 63.562 |
| 93 | mapevo | confof | iasted | 19.533 | 23.765 | 26.128 | 11.794 | 24.225 | 24.544 |
| 94 | mapevo | edas | ekaw | 71.268 | 87.995 | 100.089 | 33.988 | 93.714 | 136.283 |
| 95 | mapevo | ekaw | sigkdd | 37.43 | 46.851 | 53.35 | 15.425 | 47.758 | 59.84 |
| 96 | mappso | cmt | conference | 61.615 | 81.149 | 87.979 | 38.455 | 68.139 | 99.209 |
| 97 | mappso | cmt | confof | 94.161 | 106.792 | 142.98 | 42.402 | 72.38 | 106.515 |
| 98 | mappso | cmt | edas | 78.384 | 84.986 | 89.568 | 46.873 | 73.991 | 74.563 |
| 99 | mappso | cmt | ekaw | 97.196 | 100.209 | 134.054 | 50.243 | 77.297 | 108.88 |
| 100 | mappso | cmt | sigkdd | 40.331 | 50.597 | 54.092 | 22.439 | 54.159 | 69.454 |
| 101 | mappso | conference | confof | 87.656 | 97.764 | 100.733 | 36.552 | 63.444 | 93.764 |
| 102 | mappso | conference | edas | 121.491 | 126.928 | 131.586 | 83.017 | 112.808 | 113.047 |
| 103 | mappso | confof | edas | 91.93 | 129.944 | 158.23 | 48.699 | 122.884 | 138.444 |
| 104 | mappso | confof | sigkdd | 79.198 | 88.759 | 103.53 | 47.38 | 80.666 | 118.465 |
| 105 | mappso | edas | ekaw | 159.596 | 217.567 | 908.275 | 80.789 | 156.603 | 250.065 |
| 106 | mappso | edas | sigkdd | 100.304 | 164.173 | 506.545 | 72.406 | 137.043 | 187.251 |
| 107 | mappso | ekaw | sigkdd | 97.982 | 109.665 | 191.253 | 54.459 | 96.357 | 143.063 |
| 108 | mapsss | conference | confof | 18.855 | 29.553 | 34.713 | 3.103 | 34.314 | 38.097 |
| 109 | mapsss | conference | edas | 19.007 | 23.577 | 28.331 | 16.405 | 24.29 | 25.292 |
| 110 | mapsss | conference | ekaw | 34.519 | 75.247 | 103.849 | 17.033 | 56.327 | 69.845 |
| 111 | mapsss | confof | edas | 17.684 | 21.013 | 24.274 | 18.099 | 25.873 | 27.165 |
| 112 | mapsss | confof | ekaw | 10.965 | 24.735 | 28.868 | 2.404 | 24.382 | 26.723 |
| 113 | mapsss | edas | ekaw | 16.784 | 20.664 | 24.002 | 13.655 | 18.814 | 19.863 |
| 114 | optima | cmt | conference | 56.1 | 64.702 | 79.607 | 35.287 | 61.69 | 86.456 |
| 115 | optima | cmt | confof | 27.481 | 39.743 | 42.643 | 9.668 | 46.2 | 47.141 |
| 116 | optima | cmt | edas | 57.577 | 63.432 | 69.952 | 36.541 | 48.673 | 49.002 |
| 117 | optima | cmt | ekaw | 231.401 | 450.232 | 580.546 | 34.15 | 63.674 | 85.667 |
| 118 | optima | cmt | iasted | 20.124 | 21.523 | 23.447 | 16.128 | 18.433 | 18.571 |
| 119 | optima | cmt | sigkdd | 54.042 | 71.703 | 87.233 | 33.198 | 58.608 | 78.191 |
| 120 | optima | conference | confof | 75.413 | 79.597 | 92.088 | 46.27 | 76.363 | 110.998 |
| 121 | optima | conference | edas | 67.262 | 75.663 | 79.48 | 43.126 | 54.248 | 54.375 |
| 122 | optima | confof | edas | 69.297 | 78.962 | 84.794 | 48.001 | 62.969 | 63.21 |
| 123 | optima | confof | sigkdd | 55.224 | 65.273 | 70.801 | 33.83 | 59.936 | 82.033 |
| 124 | optima | edas | ekaw | 88.424 | 98.359 | 104.513 | 54.277 | 103.153 | 103.659 |
| 125 | optima | edas | iasted | 50.291 | 55.612 | 62.658 | 23.271 | 32.307 | 33.294 |
| 126 | optima | edas | sigkdd | 49.978 | 56.125 | 60.439 | 33.905 | 47.578 | 48.594 |
| 127 | optima | ekaw | sigkdd | 73.028 | 79.037 | 115.641 | 33.34 | 64.448 | 88.229 |
| 128 | optima | iasted | sigkdd | 15.955 | 17.429 | 18.458 | 18.744 | 20.557 | 20.897 |
| 129 | yam | cmt | iasted | 6.859 | 13.591 | 15.309 | 5.254 | 14.357 | 15.195 |
| 130 | yam | conference | edas | 19.107 | 29.001 | 36.18 | 13.202 | 27.643 | 28.255 |
| 131 | yam | confof | edas | 16.428 | 21.69 | 24.501 | 11.316 | 20.961 | 21.953 |
| 132 | yam | edas | ekaw | 15.407 | 20.465 | 24.874 | 15.737 | 24.277 | 25.975 |

# Debugging Sessions Comparison #


Comparison of HS-Tree and Inv-HS-Tree on ontologies used in [[3](Publications.md)]  including small number of minimal conflicts that result in computation of large number of diagnoses.


Running
```
mvn -Dtest=UnsolvableTests#testNormalCasesDual test
```
you run query sessions using HS-Tree and Inv-HS-Tree with University, Economy and Transportation Ontology.

| **Ontology** | **Tree** | **error probabilites** | **diagnosis position** | **iteration** | **time (ms)** | **number of queries** | **reaction time** |
|:-------------|:---------|:-----------------------|:-----------------------|:--------------|:--------------|:----------------------|:------------------|
| University | HS-Tree | EXTREME | GOOD | 0 | 914 | 3 | 261 |
| University | HS-Tree | EXTREME | GOOD | 1 | 1362 | 3 | 356 |
| University | HS-Tree | EXTREME | GOOD | 2 | 591 | 2 | 200 |
| University | HS-Tree | EXTREME | GOOD | 3 | 525 | 2 | 177 |
| University | HS-Tree | EXTREME | GOOD | 4 | 924 | 3 | 237 |
| University | HS-Tree | EXTREME | GOOD | 5 | 348 | 1 | 185 |
| University | HS-Tree | EXTREME | GOOD | 6 | 312 | 1 | 201 |
| University | HS-Tree | EXTREME | GOOD | 7 | 535 | 3 | 139 |
| University | HS-Tree | EXTREME | GOOD | 8 | 648 | 3 | 188 |
| University | HS-Tree | EXTREME | GOOD | 9 | 444 | 2 | 174 |
| University | HS-Tree | EXTREME | GOOD | 10 | 416 | 2 | 160 |
| University | HS-Tree | EXTREME | GOOD | 11 | 688 | 3 | 154 |
| University | HS-Tree | EXTREME | GOOD | 12 | 547 | 3 | 156 |
| University | HS-Tree | EXTREME | GOOD | 13 | 699 | 3 | 198 |
| University | HS-Tree | EXTREME | GOOD | 14 | 690 | 3 | 190 |
| University | HS-Tree | EXTREME | AVERAGE | 0 | 546 | 2 | 170 |
| University | HS-Tree | EXTREME | AVERAGE | 1 | 929 | 4 | 161 |
| University | HS-Tree | EXTREME | AVERAGE | 2 | 545 | 2 | 187 |
| University | HS-Tree | EXTREME | AVERAGE | 3 | 432 | 2 | 123 |
| University | HS-Tree | EXTREME | AVERAGE | 4 | 843 | 4 | 150 |
| University | HS-Tree | EXTREME | AVERAGE | 5 | 275 | 1 | 162 |
| University | HS-Tree | EXTREME | AVERAGE | 6 | 594 | 3 | 125 |
| University | HS-Tree | EXTREME | AVERAGE | 7 | 609 | 3 | 142 |
| University | HS-Tree | EXTREME | AVERAGE | 8 | 676 | 3 | 165 |
| University | HS-Tree | EXTREME | AVERAGE | 9 | 895 | 4 | 154 |
| University | HS-Tree | EXTREME | AVERAGE | 10 | 642 | 2 | 184 |
| University | HS-Tree | EXTREME | AVERAGE | 11 | 852 | 4 | 178 |
| University | HS-Tree | EXTREME | AVERAGE | 12 | 635 | 3 | 146 |
| University | HS-Tree | EXTREME | AVERAGE | 13 | 678 | 3 | 148 |
| University | HS-Tree | EXTREME | AVERAGE | 14 | 735 | 3 | 184 |
| University | HS-Tree | EXTREME | BAD | 0 | 1522 | 9 | 149 |
| University | HS-Tree | EXTREME | BAD | 1 | 10340 | 21 | 469 |
| University | HS-Tree | EXTREME | BAD | 2 | 882 | 7 | 109 |
| University | HS-Tree | EXTREME | BAD | 3 | 2651 | 9 | 221 |
| University | HS-Tree | EXTREME | BAD | 4 | 785 | 5 | 138 |
| University | HS-Tree | EXTREME | BAD | 5 | 1441 | 8 | 153 |
| University | HS-Tree | EXTREME | BAD | 6 | 2434 | 10 | 187 |
| University | HS-Tree | EXTREME | BAD | 7 | 4166 | 13 | 247 |
| University | HS-Tree | EXTREME | BAD | 8 | 4189 | 13 | 287 |
| University | HS-Tree | EXTREME | BAD | 9 | 1609 | 8 | 180 |
| University | HS-Tree | EXTREME | BAD | 10 | 3660 | 11 | 272 |
| University | HS-Tree | EXTREME | BAD | 11 | 1365 | 7 | 167 |
| University | HS-Tree | EXTREME | BAD | 12 | 776 | 5 | 124 |
| University | HS-Tree | EXTREME | BAD | 13 | 852 | 6 | 102 |
| University | HS-Tree | EXTREME | BAD | 14 | 11564 | 17 | 652 |
| University | HS-Tree | MODERATE | GOOD | 0 | 479 | 4 | 101 |
| University | HS-Tree | MODERATE | GOOD | 1 | 666 | 3 | 183 |
| University | HS-Tree | MODERATE | GOOD | 2 | 579 | 3 | 136 |
| University | HS-Tree | MODERATE | GOOD | 3 | 591 | 3 | 140 |
| University | HS-Tree | MODERATE | GOOD | 4 | 851 | 4 | 168 |
| University | HS-Tree | MODERATE | GOOD | 5 | 712 | 4 | 127 |
| University | HS-Tree | MODERATE | GOOD | 6 | 495 | 4 | 106 |
| University | HS-Tree | MODERATE | GOOD | 7 | 1374 | 6 | 206 |
| University | HS-Tree | MODERATE | GOOD | 8 | 416 | 3 | 116 |
| University | HS-Tree | MODERATE | GOOD | 9 | 556 | 3 | 143 |
| University | HS-Tree | MODERATE | GOOD | 10 | 546 | 3 | 130 |
| University | HS-Tree | MODERATE | GOOD | 11 | 444 | 3 | 118 |
| University | HS-Tree | MODERATE | GOOD | 12 | 910 | 4 | 155 |
| University | HS-Tree | MODERATE | GOOD | 13 | 1376 | 7 | 173 |
| University | HS-Tree | MODERATE | GOOD | 14 | 562 | 3 | 159 |
| University | HS-Tree | MODERATE | AVERAGE | 0 | 1409 | 7 | 180 |
| University | HS-Tree | MODERATE | AVERAGE | 1 | 2435 | 8 | 244 |
| University | HS-Tree | MODERATE | AVERAGE | 2 | 3377 | 10 | 264 |
| University | HS-Tree | MODERATE | AVERAGE | 3 | 1195 | 5 | 170 |
| University | HS-Tree | MODERATE | AVERAGE | 4 | 1527 | 7 | 156 |
| University | HS-Tree | MODERATE | AVERAGE | 5 | 1200 | 6 | 164 |
| University | HS-Tree | MODERATE | AVERAGE | 6 | 1361 | 6 | 198 |
| University | HS-Tree | MODERATE | AVERAGE | 7 | 1485 | 7 | 189 |
| University | HS-Tree | MODERATE | AVERAGE | 8 | 4336 | 12 | 288 |
| University | HS-Tree | MODERATE | AVERAGE | 9 | 1690 | 7 | 185 |
| University | HS-Tree | MODERATE | AVERAGE | 10 | 1862 | 8 | 207 |
| University | HS-Tree | MODERATE | AVERAGE | 11 | 3314 | 11 | 279 |
| University | HS-Tree | MODERATE | AVERAGE | 12 | 2990 | 9 | 265 |
| University | HS-Tree | MODERATE | AVERAGE | 13 | 1629 | 6 | 199 |
| University | HS-Tree | MODERATE | AVERAGE | 14 | 2266 | 9 | 228 |
| University | HS-Tree | MODERATE | BAD | 0 | 1358 | 8 | 149 |
| University | HS-Tree | MODERATE | BAD | 1 | 2180 | 11 | 176 |
| University | HS-Tree | MODERATE | BAD | 2 | 1734 | 8 | 197 |
| University | HS-Tree | MODERATE | BAD | 3 | 1517 | 8 | 164 |
| University | HS-Tree | MODERATE | BAD | 4 | 1919 | 8 | 183 |
| University | HS-Tree | MODERATE | BAD | 5 | 2213 | 10 | 196 |
| University | HS-Tree | MODERATE | BAD | 6 | 1952 | 9 | 193 |
| University | HS-Tree | MODERATE | BAD | 7 | 1730 | 8 | 186 |
| University | HS-Tree | MODERATE | BAD | 8 | 3447 | 12 | 262 |
| University | HS-Tree | MODERATE | BAD | 9 | 577 | 5 | 100 |
| University | HS-Tree | MODERATE | BAD | 10 | 4099 | 14 | 268 |
| University | HS-Tree | MODERATE | BAD | 11 | 1470 | 9 | 143 |
| University | HS-Tree | MODERATE | BAD | 12 | 7574 | 17 | 399 |
| University | HS-Tree | MODERATE | BAD | 13 | 2353 | 11 | 194 |
| University | HS-Tree | MODERATE | BAD | 14 | 1270 | 7 | 159 |
| University | HS-Tree | UNIFORM | GOOD | 0 | 737 | 4 | 134 |
| University | HS-Tree | UNIFORM | GOOD | 1 | 564 | 3 | 166 |
| University | HS-Tree | UNIFORM | GOOD | 2 | 1725 | 8 | 188 |
| University | HS-Tree | UNIFORM | GOOD | 3 | 602 | 3 | 168 |
| University | HS-Tree | UNIFORM | GOOD | 4 | 1724 | 8 | 188 |
| University | HS-Tree | UNIFORM | GOOD | 5 | 772 | 3 | 191 |
| University | HS-Tree | UNIFORM | GOOD | 6 | 1343 | 6 | 169 |
| University | HS-Tree | UNIFORM | GOOD | 7 | 1653 | 8 | 180 |
| University | HS-Tree | UNIFORM | GOOD | 8 | 749 | 3 | 190 |
| University | HS-Tree | UNIFORM | GOOD | 9 | 1342 | 6 | 168 |
| University | HS-Tree | UNIFORM | GOOD | 10 | 751 | 3 | 191 |
| University | HS-Tree | UNIFORM | GOOD | 11 | 489 | 3 | 143 |
| University | HS-Tree | UNIFORM | GOOD | 12 | 1971 | 9 | 191 |
| University | HS-Tree | UNIFORM | GOOD | 13 | 617 | 4 | 133 |
| University | HS-Tree | UNIFORM | GOOD | 14 | 557 | 3 | 164 |
| University | HS-Tree | UNIFORM | AVERAGE | 0 | 1286 | 6 | 167 |
| University | HS-Tree | UNIFORM | AVERAGE | 1 | 1319 | 6 | 166 |
| University | HS-Tree | UNIFORM | AVERAGE | 2 | 1714 | 8 | 173 |
| University | HS-Tree | UNIFORM | AVERAGE | 3 | 1718 | 8 | 173 |
| University | HS-Tree | UNIFORM | AVERAGE | 4 | 1261 | 6 | 173 |
| University | HS-Tree | UNIFORM | AVERAGE | 5 | 2003 | 8 | 200 |
| University | HS-Tree | UNIFORM | AVERAGE | 6 | 1477 | 6 | 193 |
| University | HS-Tree | UNIFORM | AVERAGE | 7 | 1414 | 8 | 149 |
| University | HS-Tree | UNIFORM | AVERAGE | 8 | 1847 | 8 | 205 |
| University | HS-Tree | UNIFORM | AVERAGE | 9 | 1686 | 8 | 180 |
| University | HS-Tree | UNIFORM | AVERAGE | 10 | 1294 | 6 | 169 |
| University | HS-Tree | UNIFORM | AVERAGE | 11 | 1543 | 7 | 170 |
| University | HS-Tree | UNIFORM | AVERAGE | 12 | 1161 | 7 | 148 |
| University | HS-Tree | UNIFORM | AVERAGE | 13 | 2128 | 8 | 205 |
| University | HS-Tree | UNIFORM | AVERAGE | 14 | 1476 | 7 | 174 |
| University | HS-Tree | UNIFORM | BAD | 0 | 1493 | 8 | 166 |
| University | HS-Tree | UNIFORM | BAD | 1 | 2119 | 10 | 187 |
| University | HS-Tree | UNIFORM | BAD | 2 | 5606 | 16 | 316 |
| University | HS-Tree | UNIFORM | BAD | 3 | 13924 | 25 | 507 |
| University | HS-Tree | UNIFORM | BAD | 4 | 2448 | 9 | 213 |
| University | HS-Tree | UNIFORM | BAD | 5 | 2133 | 9 | 213 |
| University | HS-Tree | UNIFORM | BAD | 6 | 1785 | 8 | 175 |
| University | HS-Tree | UNIFORM | BAD | 7 | 4113 | 13 | 288 |
| University | HS-Tree | UNIFORM | BAD | 8 | 1696 | 8 | 186 |
| University | HS-Tree | UNIFORM | BAD | 9 | 1841 | 9 | 183 |
| University | HS-Tree | UNIFORM | BAD | 10 | 5982 | 16 | 315 |
| University | HS-Tree | UNIFORM | BAD | 11 | 2447 | 9 | 213 |
| University | HS-Tree | UNIFORM | BAD | 12 | 1240 | 7 | 155 |
| University | HS-Tree | UNIFORM | BAD | 13 | 1843 | 11 | 149 |
| University | HS-Tree | UNIFORM | BAD | 14 | 3419 | 13 | 222 |
| University | Inv-HS-Tree | EXTREME | GOOD | 0 | 649 | 2 | 221 |
| University | Inv-HS-Tree | EXTREME | GOOD | 1 | 835 | 2 | 245 |
| University | Inv-HS-Tree | EXTREME | GOOD | 2 | 490 | 2 | 163 |
| University | Inv-HS-Tree | EXTREME | GOOD | 3 | 484 | 2 | 182 |
| University | Inv-HS-Tree | EXTREME | GOOD | 4 | 1154 | 2 | 484 |
| University | Inv-HS-Tree | EXTREME | GOOD | 5 | 575 | 2 | 235 |
| University | Inv-HS-Tree | EXTREME | GOOD | 6 | 763 | 2 | 316 |
| University | Inv-HS-Tree | EXTREME | GOOD | 7 | 818 | 3 | 240 |
| University | Inv-HS-Tree | EXTREME | GOOD | 8 | 1247 | 5 | 189 |
| University | Inv-HS-Tree | EXTREME | GOOD | 9 | 822 | 2 | 253 |
| University | Inv-HS-Tree | EXTREME | GOOD | 10 | 839 | 2 | 267 |
| University | Inv-HS-Tree | EXTREME | GOOD | 11 | 1718 | 6 | 231 |
| University | Inv-HS-Tree | EXTREME | GOOD | 12 | 674 | 2 | 238 |
| University | Inv-HS-Tree | EXTREME | GOOD | 13 | 890 | 3 | 268 |
| University | Inv-HS-Tree | EXTREME | GOOD | 14 | 7247 | 18 | 377 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 0 | 2349 | 7 | 315 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 1 | 599 | 2 | 258 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 2 | 1009 | 4 | 199 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 3 | 1795 | 6 | 236 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 4 | 646 | 2 | 258 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 5 | 1348 | 7 | 172 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 6 | 670 | 2 | 255 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 7 | 723 | 3 | 188 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 8 | 2294 | 6 | 317 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 9 | 1159 | 5 | 210 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 10 | 986 | 5 | 170 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 11 | 767 | 4 | 171 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 12 | 1248 | 5 | 229 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 13 | 1816 | 7 | 195 |
| University | Inv-HS-Tree | EXTREME | AVERAGE | 14 | 1071 | 5 | 193 |
| University | Inv-HS-Tree | EXTREME | BAD | 0 | 943 | 4 | 217 |
| University | Inv-HS-Tree | EXTREME | BAD | 1 | 3397 | 12 | 235 |
| University | Inv-HS-Tree | EXTREME | BAD | 2 | 11102 | 26 | 378 |
| University | Inv-HS-Tree | EXTREME | BAD | 3 | 750 | 4 | 169 |
| University | Inv-HS-Tree | EXTREME | BAD | 4 | 8814 | 20 | 410 |
| University | Inv-HS-Tree | EXTREME | BAD | 5 | 2755 | 9 | 244 |
| University | Inv-HS-Tree | EXTREME | BAD | 6 | 11243 | 23 | 460 |
| University | Inv-HS-Tree | EXTREME | BAD | 7 | 3364 | 11 | 253 |
| University | Inv-HS-Tree | EXTREME | BAD | 8 | 2646 | 8 | 287 |
| University | Inv-HS-Tree | EXTREME | BAD | 9 | 1846 | 7 | 242 |
| University | Inv-HS-Tree | EXTREME | BAD | 10 | 1562 | 6 | 237 |
| University | Inv-HS-Tree | EXTREME | BAD | 11 | 1824 | 8 | 198 |
| University | Inv-HS-Tree | EXTREME | BAD | 12 | 672 | 5 | 119 |
| University | Inv-HS-Tree | EXTREME | BAD | 13 | 1781 | 6 | 224 |
| University | Inv-HS-Tree | EXTREME | BAD | 14 | 850 | 3 | 222 |
| University | Inv-HS-Tree | MODERATE | GOOD | 0 | 1871 | 8 | 213 |
| University | Inv-HS-Tree | MODERATE | GOOD | 1 | 1156 | 3 | 330 |
| University | Inv-HS-Tree | MODERATE | GOOD | 2 | 10742 | 19 | 476 |
| University | Inv-HS-Tree | MODERATE | GOOD | 3 | 2088 | 8 | 237 |
| University | Inv-HS-Tree | MODERATE | GOOD | 4 | 983 | 4 | 196 |
| University | Inv-HS-Tree | MODERATE | GOOD | 5 | 1185 | 3 | 292 |
| University | Inv-HS-Tree | MODERATE | GOOD | 6 | 1706 | 8 | 194 |
| University | Inv-HS-Tree | MODERATE | GOOD | 7 | 1004 | 3 | 262 |
| University | Inv-HS-Tree | MODERATE | GOOD | 8 | 594 | 2 | 217 |
| University | Inv-HS-Tree | MODERATE | GOOD | 9 | 1753 | 6 | 197 |
| University | Inv-HS-Tree | MODERATE | GOOD | 10 | 1099 | 5 | 184 |
| University | Inv-HS-Tree | MODERATE | GOOD | 11 | 4019 | 12 | 312 |
| University | Inv-HS-Tree | MODERATE | GOOD | 12 | 1255 | 6 | 170 |
| University | Inv-HS-Tree | MODERATE | GOOD | 13 | 1518 | 7 | 188 |
| University | Inv-HS-Tree | MODERATE | GOOD | 14 | 1174 | 3 | 319 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 0 | 1783 | 7 | 228 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 1 | 1695 | 4 | 311 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 2 | 893 | 3 | 251 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 3 | 1227 | 6 | 174 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 4 | 1904 | 7 | 244 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 5 | 1361 | 6 | 208 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 6 | 2923 | 10 | 273 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 7 | 1500 | 7 | 194 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 8 | 927 | 4 | 187 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 9 | 1283 | 4 | 277 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 10 | 8387 | 19 | 376 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 11 | 1091 | 3 | 248 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 12 | 1691 | 7 | 220 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 13 | 2015 | 8 | 233 |
| University | Inv-HS-Tree | MODERATE | AVERAGE | 14 | 2039 | 6 | 225 |
| University | Inv-HS-Tree | MODERATE | BAD | 0 | 3856 | 11 | 296 |
| University | Inv-HS-Tree | MODERATE | BAD | 1 | 5642 | 16 | 328 |
| University | Inv-HS-Tree | MODERATE | BAD | 2 | 9660 | 21 | 404 |
| University | Inv-HS-Tree | MODERATE | BAD | 3 | 5163 | 15 | 285 |
| University | Inv-HS-Tree | MODERATE | BAD | 4 | 2028 | 8 | 228 |
| University | Inv-HS-Tree | MODERATE | BAD | 5 | 3112 | 12 | 239 |
| University | Inv-HS-Tree | MODERATE | BAD | 6 | 1165 | 4 | 244 |
| University | Inv-HS-Tree | MODERATE | BAD | 7 | 2107 | 9 | 191 |
| University | Inv-HS-Tree | MODERATE | BAD | 8 | 2251 | 9 | 220 |
| University | Inv-HS-Tree | MODERATE | BAD | 9 | 1187 | 4 | 210 |
| University | Inv-HS-Tree | MODERATE | BAD | 10 | 2103 | 8 | 213 |
| University | Inv-HS-Tree | MODERATE | BAD | 11 | 874 | 5 | 153 |
| University | Inv-HS-Tree | MODERATE | BAD | 12 | 2423 | 8 | 281 |
| University | Inv-HS-Tree | MODERATE | BAD | 13 | 1002 | 6 | 139 |
| University | Inv-HS-Tree | MODERATE | BAD | 14 | 2564 | 9 | 259 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 0 | 928 | 5 | 151 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 1 | 3049 | 9 | 314 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 2 | 829 | 5 | 148 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 3 | 4026 | 13 | 280 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 4 | 1595 | 6 | 241 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 5 | 1061 | 6 | 157 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 6 | 6712 | 19 | 325 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 7 | 1018 | 6 | 151 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 8 | 958 | 5 | 155 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 9 | 6624 | 19 | 321 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 10 | 1655 | 6 | 250 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 11 | 754 | 5 | 134 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 12 | 710 | 4 | 157 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 13 | 705 | 4 | 157 |
| University | Inv-HS-Tree | UNIFORM | GOOD | 14 | 2468 | 7 | 230 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 0 | 775 | 3 | 198 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 1 | 1035 | 4 | 227 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 2 | 2097 | 8 | 226 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 3 | 1841 | 6 | 248 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 4 | 2013 | 8 | 226 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 5 | 1815 | 6 | 245 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 6 | 4419 | 13 | 287 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 7 | 3064 | 12 | 231 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 8 | 2067 | 7 | 241 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 9 | 821 | 3 | 196 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 10 | 2271 | 7 | 249 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 11 | 867 | 3 | 235 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 12 | 1830 | 6 | 247 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 13 | 765 | 3 | 194 |
| University | Inv-HS-Tree | UNIFORM | AVERAGE | 14 | 1097 | 4 | 242 |
| University | Inv-HS-Tree | UNIFORM | BAD | 0 | 3995 | 12 | 280 |
| University | Inv-HS-Tree | UNIFORM | BAD | 1 | 3537 | 13 | 246 |
| University | Inv-HS-Tree | UNIFORM | BAD | 2 | 1049 | 6 | 156 |
| University | Inv-HS-Tree | UNIFORM | BAD | 3 | 2052 | 9 | 205 |
| University | Inv-HS-Tree | UNIFORM | BAD | 4 | 2941 | 10 | 271 |
| University | Inv-HS-Tree | UNIFORM | BAD | 5 | 1022 | 6 | 152 |
| University | Inv-HS-Tree | UNIFORM | BAD | 6 | 1893 | 8 | 194 |
| University | Inv-HS-Tree | UNIFORM | BAD | 7 | 2881 | 12 | 216 |
| University | Inv-HS-Tree | UNIFORM | BAD | 8 | 2105 | 8 | 213 |
| University | Inv-HS-Tree | UNIFORM | BAD | 9 | 1914 | 7 | 225 |
| University | Inv-HS-Tree | UNIFORM | BAD | 10 | 1965 | 10 | 177 |
| University | Inv-HS-Tree | UNIFORM | BAD | 11 | 2903 | 9 | 269 |
| University | Inv-HS-Tree | UNIFORM | BAD | 12 | 2057 | 10 | 185 |
| University | Inv-HS-Tree | UNIFORM | BAD | 13 | 1461 | 8 | 164 |
| University | Inv-HS-Tree | UNIFORM | BAD | 14 | 1066 | 6 | 158 |
| Transportation | HS-Tree | EXTREME | GOOD | 0 | 4810 | 4 | 943 |
| Transportation | HS-Tree | EXTREME | GOOD | 1 | 15346 | 11 | 1157 |
| Transportation | HS-Tree | EXTREME | GOOD | 2 | 5941 | 4 | 1037 |
| Transportation | HS-Tree | EXTREME | GOOD | 3 | 10562 | 7 | 1233 |
| Transportation | HS-Tree | EXTREME | GOOD | 4 | 5394 | 4 | 1012 |
| Transportation | HS-Tree | EXTREME | GOOD | 5 | 2746 | 1 | 1838 |
| Transportation | HS-Tree | EXTREME | GOOD | 6 | 3099 | 1 | 1775 |
| Transportation | HS-Tree | EXTREME | GOOD | 7 | 10931 | 8 | 1056 |
| Transportation | HS-Tree | EXTREME | GOOD | 8 | 7056 | 6 | 905 |
| Transportation | HS-Tree | EXTREME | GOOD | 9 | 3027 | 1 | 1898 |
| Transportation | HS-Tree | EXTREME | GOOD | 10 | 8854 | 7 | 1144 |
| Transportation | HS-Tree | EXTREME | GOOD | 11 | 2461 | 1 | 1684 |
| Transportation | HS-Tree | EXTREME | GOOD | 12 | 2894 | 1 | 1663 |
| Transportation | HS-Tree | EXTREME | GOOD | 13 | 7926 | 5 | 1231 |
| Transportation | HS-Tree | EXTREME | GOOD | 14 | 5156 | 3 | 1359 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 0 | 5644 | 4 | 1286 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 1 | 20653 | 12 | 1465 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 2 | 20141 | 13 | 1299 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 3 | 5786 | 4 | 1084 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 4 | 19868 | 14 | 1300 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 5 | 2504 | 1 | 1688 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 6 | 8471 | 5 | 1308 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 7 | 14631 | 11 | 1094 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 8 | 3147 | 1 | 1793 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 9 | 2752 | 1 | 1976 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 10 | 3027 | 1 | 1756 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 11 | 7218 | 5 | 1226 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 12 | 18477 | 12 | 1324 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 13 | 6045 | 5 | 994 |
| Transportation | HS-Tree | EXTREME | AVERAGE | 14 | 9315 | 7 | 1079 |
| Transportation | HS-Tree | EXTREME | BAD | 0 | 31145 | 20 | 1431 |
| Transportation | HS-Tree | EXTREME | BAD | 1 | 27200 | 16 | 1485 |
| Transportation | HS-Tree | EXTREME | BAD | 2 | 30377 | 19 | 1415 |
| Transportation | HS-Tree | EXTREME | BAD | 3 | 8777 | 10 | 781 |
| Transportation | HS-Tree | EXTREME | BAD | 4 | 142701 | 52 | 2640 |
| Transportation | HS-Tree | EXTREME | BAD | 5 | 22967 | 15 | 1300 |
| Transportation | HS-Tree | EXTREME | BAD | 6 | 37298 | 20 | 1754 |
| Transportation | HS-Tree | EXTREME | BAD | 7 | 9403 | 7 | 1130 |
| Transportation | HS-Tree | EXTREME | BAD | 8 | 9183 | 8 | 1025 |
| Transportation | HS-Tree | EXTREME | BAD | 9 | 26508 | 16 | 1522 |
| Transportation | HS-Tree | EXTREME | BAD | 10 | 22957 | 16 | 1257 |
| Transportation | HS-Tree | EXTREME | BAD | 11 | 27025 | 19 | 1308 |
| Transportation | HS-Tree | EXTREME | BAD | 12 | 27274 | 16 | 1533 |
| Transportation | HS-Tree | EXTREME | BAD | 13 | 28943 | 19 | 1397 |
| Transportation | HS-Tree | EXTREME | BAD | 14 | 31491 | 20 | 1453 |
| Transportation | HS-Tree | MODERATE | GOOD | 0 | 8557 | 6 | 1297 |
| Transportation | HS-Tree | MODERATE | GOOD | 1 | 3155 | 1 | 1868 |
| Transportation | HS-Tree | MODERATE | GOOD | 2 | 11241 | 8 | 1108 |
| Transportation | HS-Tree | MODERATE | GOOD | 3 | 6163 | 4 | 1144 |
| Transportation | HS-Tree | MODERATE | GOOD | 4 | 8129 | 6 | 1075 |
| Transportation | HS-Tree | MODERATE | GOOD | 5 | 4021 | 3 | 1097 |
| Transportation | HS-Tree | MODERATE | GOOD | 6 | 8234 | 7 | 1061 |
| Transportation | HS-Tree | MODERATE | GOOD | 7 | 4490 | 3 | 1014 |
| Transportation | HS-Tree | MODERATE | GOOD | 8 | 5290 | 4 | 1031 |
| Transportation | HS-Tree | MODERATE | GOOD | 9 | 9066 | 6 | 1293 |
| Transportation | HS-Tree | MODERATE | GOOD | 10 | 12398 | 9 | 1073 |
| Transportation | HS-Tree | MODERATE | GOOD | 11 | 12015 | 9 | 1211 |
| Transportation | HS-Tree | MODERATE | GOOD | 12 | 5004 | 3 | 1284 |
| Transportation | HS-Tree | MODERATE | GOOD | 13 | 9121 | 7 | 1180 |
| Transportation | HS-Tree | MODERATE | GOOD | 14 | 19217 | 12 | 1350 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 0 | 18081 | 12 | 1385 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 1 | 11606 | 7 | 1323 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 2 | 3188 | 1 | 1977 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 3 | 3230 | 1 | 1897 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 4 | 12194 | 9 | 1123 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 5 | 15049 | 10 | 1221 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 6 | 12001 | 9 | 1116 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 7 | 16387 | 12 | 1174 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 8 | 17381 | 10 | 1510 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 9 | 11580 | 8 | 1275 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 10 | 11611 | 8 | 1137 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 11 | 19883 | 13 | 1411 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 12 | 3148 | 1 | 1889 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 13 | 7573 | 5 | 1192 |
| Transportation | HS-Tree | MODERATE | AVERAGE | 14 | 17903 | 11 | 1371 |
| Transportation | HS-Tree | MODERATE | BAD | 0 | 14964 | 11 | 1123 |
| Transportation | HS-Tree | MODERATE | BAD | 1 | 102840 | 39 | 2523 |
| Transportation | HS-Tree | MODERATE | BAD | 2 | 35674 | 21 | 1589 |
| Transportation | HS-Tree | MODERATE | BAD | 3 | 51772 | 26 | 1792 |
| Transportation | HS-Tree | MODERATE | BAD | 4 | 44463 | 27 | 1541 |
| Transportation | HS-Tree | MODERATE | BAD | 5 | 15054 | 13 | 1051 |
| Transportation | HS-Tree | MODERATE | BAD | 6 | 41742 | 25 | 1567 |
| Transportation | HS-Tree | MODERATE | BAD | 7 | 16199 | 12 | 1063 |
| Transportation | HS-Tree | MODERATE | BAD | 8 | 33563 | 22 | 1422 |
| Transportation | HS-Tree | MODERATE | BAD | 9 | 35063 | 23 | 1416 |
| Transportation | HS-Tree | MODERATE | BAD | 10 | 34586 | 23 | 1384 |
| Transportation | HS-Tree | MODERATE | BAD | 11 | 39251 | 25 | 1469 |
| Transportation | HS-Tree | MODERATE | BAD | 12 | 32359 | 19 | 1585 |
| Transportation | HS-Tree | MODERATE | BAD | 13 | 35308 | 22 | 1491 |
| Transportation | HS-Tree | MODERATE | BAD | 14 | 58160 | 30 | 1819 |
| Transportation | HS-Tree | UNIFORM | GOOD | 0 | 10748 | 8 | 1073 |
| Transportation | HS-Tree | UNIFORM | GOOD | 1 | 11926 | 8 | 1262 |
| Transportation | HS-Tree | UNIFORM | GOOD | 2 | 16789 | 10 | 1447 |
| Transportation | HS-Tree | UNIFORM | GOOD | 3 | 12041 | 8 | 1275 |
| Transportation | HS-Tree | UNIFORM | GOOD | 4 | 11985 | 8 | 1271 |
| Transportation | HS-Tree | UNIFORM | GOOD | 5 | 14751 | 12 | 1108 |
| Transportation | HS-Tree | UNIFORM | GOOD | 6 | 10102 | 7 | 1110 |
| Transportation | HS-Tree | UNIFORM | GOOD | 7 | 18093 | 11 | 1373 |
| Transportation | HS-Tree | UNIFORM | GOOD | 8 | 6800 | 5 | 967 |
| Transportation | HS-Tree | UNIFORM | GOOD | 9 | 9660 | 8 | 936 |
| Transportation | HS-Tree | UNIFORM | GOOD | 10 | 36732 | 20 | 1615 |
| Transportation | HS-Tree | UNIFORM | GOOD | 11 | 15880 | 10 | 1368 |
| Transportation | HS-Tree | UNIFORM | GOOD | 12 | 12013 | 9 | 1215 |
| Transportation | HS-Tree | UNIFORM | GOOD | 13 | 9123 | 7 | 1138 |
| Transportation | HS-Tree | UNIFORM | GOOD | 14 | 8754 | 6 | 994 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 0 | 9325 | 7 | 1055 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 1 | 8593 | 8 | 948 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 2 | 10779 | 9 | 1003 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 3 | 25815 | 16 | 1372 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 4 | 13481 | 12 | 1006 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 5 | 7338 | 7 | 921 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 6 | 15366 | 12 | 1067 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 7 | 12631 | 10 | 1137 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 8 | 12898 | 8 | 1378 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 9 | 9621 | 7 | 1104 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 10 | 22890 | 15 | 1288 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 11 | 14572 | 12 | 1096 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 12 | 6282 | 6 | 663 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 13 | 15620 | 10 | 1357 |
| Transportation | HS-Tree | UNIFORM | AVERAGE | 14 | 18862 | 12 | 1326 |
| Transportation | HS-Tree | UNIFORM | BAD | 0 | 64117 | 31 | 1961 |
| Transportation | HS-Tree | UNIFORM | BAD | 1 | 48080 | 27 | 1578 |
| Transportation | HS-Tree | UNIFORM | BAD | 2 | 33916 | 21 | 1398 |
| Transportation | HS-Tree | UNIFORM | BAD | 3 | 26242 | 16 | 1454 |
| Transportation | HS-Tree | UNIFORM | BAD | 4 | 5867 | 6 | 841 |
| Transportation | HS-Tree | UNIFORM | BAD | 5 | 9901 | 9 | 969 |
| Transportation | HS-Tree | UNIFORM | BAD | 6 | 55015 | 26 | 1918 |
| Transportation | HS-Tree | UNIFORM | BAD | 7 | 47300 | 25 | 1693 |
| Transportation | HS-Tree | UNIFORM | BAD | 8 | 40576 | 24 | 1554 |
| Transportation | HS-Tree | UNIFORM | BAD | 9 | 13859 | 14 | 872 |
| Transportation | HS-Tree | UNIFORM | BAD | 10 | 24747 | 16 | 1430 |
| Transportation | HS-Tree | UNIFORM | BAD | 11 | 31333 | 21 | 1371 |
| Transportation | HS-Tree | UNIFORM | BAD | 12 | 27865 | 17 | 1472 |
| Transportation | HS-Tree | UNIFORM | BAD | 13 | 16090 | 13 | 1104 |
| Transportation | HS-Tree | UNIFORM | BAD | 14 | 80161 | 36 | 2121 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 0 | 20178 | 3 | 5285 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 1 | 49050 | 15 | 3172 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 2 | 17054 | 3 | 3579 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 3 | 15754 | 4 | 3833 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 4 | 82297 | 21 | 3696 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 5 | 11589 | 1 | 6299 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 6 | 7304 | 1 | 6163 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 7 | 63981 | 19 | 2938 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 8 | 12981 | 1 | 6843 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 9 | 12452 | 1 | 6760 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 10 | 17151 | 7 | 2329 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 11 | 20935 | 7 | 2847 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 12 | 43137 | 13 | 3208 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 13 | 17751 | 5 | 3419 |
| Transportation | Inv-HS-Tree | EXTREME | GOOD | 14 | 75085 | 19 | 3849 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 0 | 25635 | 7 | 2785 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 1 | 25516 | 8 | 3059 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 2 | 32863 | 9 | 3388 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 3 | 68787 | 17 | 3943 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 4 | 13287 | 1 | 7112 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 5 | 87891 | 25 | 3412 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 6 | 15910 | 3 | 3369 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 7 | 15969 | 4 | 3668 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 8 | 183097 | 45 | 3858 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 9 | 73912 | 24 | 2868 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 10 | 69583 | 24 | 2697 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 11 | 32700 | 7 | 4159 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 12 | 33123 | 10 | 3120 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 13 | 14676 | 3 | 4218 |
| Transportation | Inv-HS-Tree | EXTREME | AVERAGE | 14 | 19613 | 4 | 4563 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 0 | 129803 | 39 | 3149 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 1 | 43715 | 17 | 2345 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 2 | 51360 | 18 | 2740 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 3 | 52667 | 21 | 2402 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 4 | 94604 | 25 | 3678 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 5 | 48861 | 16 | 2843 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 6 | 399427 | 87 | 4491 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 7 | 172031 | 48 | 3398 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 8 | 131818 | 40 | 3189 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 9 | 62959 | 18 | 3267 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 10 | 54780 | 16 | 3326 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 11 | 69090 | 19 | 3529 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 12 | 251579 | 58 | 4137 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 13 | 175779 | 46 | 3618 |
| Transportation | Inv-HS-Tree | EXTREME | BAD | 14 | 46897 | 16 | 2832 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 0 | 18690 | 5 | 2925 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 1 | 176224 | 46 | 3626 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 2 | 41920 | 13 | 2982 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 3 | 39426 | 14 | 2647 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 4 | 15434 | 3 | 4731 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 5 | 67094 | 22 | 2939 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 6 | 17298 | 4 | 4226 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 7 | 42799 | 15 | 2756 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 8 | 591123 | 103 | 5547 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 9 | 35694 | 12 | 2858 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 10 | 16179 | 3 | 4532 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 11 | 29050 | 11 | 2451 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 12 | 39002 | 13 | 2896 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 13 | 10841 | 1 | 6157 |
| Transportation | Inv-HS-Tree | MODERATE | GOOD | 14 | 38977 | 10 | 3320 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 0 | 25886 | 11 | 2238 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 1 | 63280 | 25 | 2424 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 2 | 52827 | 16 | 3051 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 3 | 36499 | 10 | 3536 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 4 | 46255 | 17 | 2622 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 5 | 64913 | 22 | 2840 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 6 | 38645 | 12 | 3104 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 7 | 20326 | 9 | 2022 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 8 | 66475 | 21 | 2960 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 9 | 27529 | 7 | 3573 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 10 | 18531 | 6 | 2860 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 11 | 18786 | 7 | 2347 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 12 | 31988 | 11 | 2468 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 13 | 34835 | 13 | 2378 |
| Transportation | Inv-HS-Tree | MODERATE | AVERAGE | 14 | 29181 | 7 | 4049 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 0 | 449806 | 90 | 4897 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 1 | 170520 | 43 | 3861 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 2 | 903723 | 132 | 6669 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 3 | 26958 | 11 | 2199 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 4 | 86928 | 29 | 2884 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 5 | 131631 | 37 | 3326 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 6 | 74667 | 26 | 2768 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 7 | 150611 | 44 | 3318 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 8 | 400949 | 77 | 5104 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 9 | 28161 | 10 | 2728 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 10 | 122520 | 39 | 2964 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 11 | 51738 | 20 | 2484 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 12 | 387719 | 84 | 4436 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 13 | 106406 | 32 | 2931 |
| Transportation | Inv-HS-Tree | MODERATE | BAD | 14 | 747669 | 122 | 6033 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 0 | 37992 | 14 | 2614 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 1 | 43671 | 14 | 2964 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 2 | 65949 | 24 | 2644 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 3 | 28270 | 11 | 2475 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 4 | 37691 | 14 | 2528 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 5 | 32314 | 10 | 2994 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 6 | 83924 | 26 | 2990 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 7 | 38065 | 10 | 3565 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 8 | 44157 | 15 | 2847 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 9 | 34976 | 14 | 2396 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 10 | 45455 | 18 | 2426 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 11 | 36412 | 14 | 2444 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 12 | 43053 | 17 | 2419 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 13 | 42588 | 15 | 2744 |
| Transportation | Inv-HS-Tree | UNIFORM | GOOD | 14 | 37517 | 16 | 2247 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 0 | 46639 | 15 | 2958 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 1 | 41015 | 13 | 3056 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 2 | 49587 | 19 | 2500 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 3 | 38235 | 13 | 2843 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 4 | 27581 | 11 | 2407 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 5 | 31008 | 13 | 2284 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 6 | 62114 | 24 | 2390 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 7 | 52740 | 19 | 2675 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 8 | 31331 | 11 | 2730 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 9 | 21217 | 8 | 2550 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 10 | 42900 | 18 | 2285 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 11 | 44011 | 19 | 2228 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 12 | 25385 | 10 | 2363 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 13 | 33197 | 10 | 3226 |
| Transportation | Inv-HS-Tree | UNIFORM | AVERAGE | 14 | 36128 | 17 | 2037 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 0 | 67226 | 24 | 2716 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 1 | 90287 | 32 | 2712 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 2 | 34335 | 10 | 3220 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 3 | 29190 | 11 | 2579 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 4 | 63092 | 22 | 2664 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 5 | 138183 | 46 | 2918 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 6 | 33414 | 13 | 2474 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 7 | 81103 | 31 | 2534 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 8 | 36448 | 13 | 2526 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 9 | 372563 | 87 | 4200 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 10 | 49229 | 19 | 2501 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 11 | 46499 | 17 | 2644 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 12 | 31234 | 12 | 2531 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 13 | 47640 | 19 | 2410 |
| Transportation | Inv-HS-Tree | UNIFORM | BAD | 14 | 85853 | 29 | 2794 |
| Economy | HS-Tree | EXTREME | GOOD | 0 | 3885 | 2 | 1485 |
| Economy | HS-Tree | EXTREME | GOOD | 1 | 4875 | 3 | 1344 |
| Economy | HS-Tree | EXTREME | GOOD | 2 | 4100 | 2 | 1560 |
| Economy | HS-Tree | EXTREME | GOOD | 3 | 5692 | 3 | 1437 |
| Economy | HS-Tree | EXTREME | GOOD | 4 | 4329 | 2 | 1454 |
| Economy | HS-Tree | EXTREME | GOOD | 5 | 2666 | 1 | 1699 |
| Economy | HS-Tree | EXTREME | GOOD | 6 | 2646 | 1 | 1980 |
| Economy | HS-Tree | EXTREME | GOOD | 7 | 5335 | 3 | 1493 |
| Economy | HS-Tree | EXTREME | GOOD | 8 | 5454 | 3 | 1519 |
| Economy | HS-Tree | EXTREME | GOOD | 9 | 3212 | 2 | 1149 |
| Economy | HS-Tree | EXTREME | GOOD | 10 | 5566 | 3 | 1308 |
| Economy | HS-Tree | EXTREME | GOOD | 11 | 2644 | 1 | 1658 |
| Economy | HS-Tree | EXTREME | GOOD | 12 | 3950 | 2 | 1682 |
| Economy | HS-Tree | EXTREME | GOOD | 13 | 4529 | 3 | 1157 |
| Economy | HS-Tree | EXTREME | GOOD | 14 | 5033 | 3 | 1348 |
| Economy | HS-Tree | EXTREME | AVERAGE | 0 | 4907 | 3 | 1220 |
| Economy | HS-Tree | EXTREME | AVERAGE | 1 | 9329 | 7 | 1191 |
| Economy | HS-Tree | EXTREME | AVERAGE | 2 | 9427 | 7 | 1197 |
| Economy | HS-Tree | EXTREME | AVERAGE | 3 | 3558 | 2 | 1208 |
| Economy | HS-Tree | EXTREME | AVERAGE | 4 | 12202 | 8 | 1380 |
| Economy | HS-Tree | EXTREME | AVERAGE | 5 | 2617 | 1 | 1745 |
| Economy | HS-Tree | EXTREME | AVERAGE | 6 | 3671 | 2 | 1283 |
| Economy | HS-Tree | EXTREME | AVERAGE | 7 | 22269 | 15 | 1288 |
| Economy | HS-Tree | EXTREME | AVERAGE | 8 | 2811 | 1 | 2039 |
| Economy | HS-Tree | EXTREME | AVERAGE | 9 | 3972 | 3 | 1175 |
| Economy | HS-Tree | EXTREME | AVERAGE | 10 | 2637 | 1 | 1535 |
| Economy | HS-Tree | EXTREME | AVERAGE | 11 | 3222 | 2 | 1108 |
| Economy | HS-Tree | EXTREME | AVERAGE | 12 | 5016 | 3 | 1314 |
| Economy | HS-Tree | EXTREME | AVERAGE | 13 | 11508 | 7 | 1379 |
| Economy | HS-Tree | EXTREME | AVERAGE | 14 | 4178 | 3 | 1236 |
| Economy | HS-Tree | EXTREME | BAD | 0 | 31555 | 21 | 1399 |
| Economy | HS-Tree | EXTREME | BAD | 1 | 34808 | 18 | 1714 |
| Economy | HS-Tree | EXTREME | BAD | 2 | 27551 | 18 | 1408 |
| Economy | HS-Tree | EXTREME | BAD | 3 | 87096 | 35 | 2340 |
| Economy | HS-Tree | EXTREME | BAD | 4 | 41633 | 24 | 1612 |
| Economy | HS-Tree | EXTREME | BAD | 5 | 41306 | 23 | 1621 |
| Economy | HS-Tree | EXTREME | BAD | 6 | 19505 | 16 | 1100 |
| Economy | HS-Tree | EXTREME | BAD | 7 | 17203 | 12 | 1315 |
| Economy | HS-Tree | EXTREME | BAD | 8 | 25525 | 17 | 1378 |
| Economy | HS-Tree | EXTREME | BAD | 9 | 16410 | 14 | 1006 |
| Economy | HS-Tree | EXTREME | BAD | 10 | 35681 | 21 | 1584 |
| Economy | HS-Tree | EXTREME | BAD | 11 | 47729 | 27 | 1659 |
| Economy | HS-Tree | EXTREME | BAD | 12 | 75269 | 34 | 2012 |
| Economy | HS-Tree | EXTREME | BAD | 13 | 84672 | 37 | 2179 |
| Economy | HS-Tree | EXTREME | BAD | 14 | 23468 | 15 | 1351 |
| Economy | HS-Tree | MODERATE | GOOD | 0 | 4742 | 3 | 1116 |
| Economy | HS-Tree | MODERATE | GOOD | 1 | 3708 | 2 | 1289 |
| Economy | HS-Tree | MODERATE | GOOD | 2 | 9182 | 6 | 1273 |
| Economy | HS-Tree | MODERATE | GOOD | 3 | 4087 | 3 | 1231 |
| Economy | HS-Tree | MODERATE | GOOD | 4 | 4414 | 3 | 1071 |
| Economy | HS-Tree | MODERATE | GOOD | 5 | 5214 | 3 | 1320 |
| Economy | HS-Tree | MODERATE | GOOD | 6 | 5316 | 3 | 1461 |
| Economy | HS-Tree | MODERATE | GOOD | 7 | 4881 | 3 | 1311 |
| Economy | HS-Tree | MODERATE | GOOD | 8 | 7698 | 3 | 2019 |
| Economy | HS-Tree | MODERATE | GOOD | 9 | 9044 | 6 | 1353 |
| Economy | HS-Tree | MODERATE | GOOD | 10 | 5134 | 3 | 1255 |
| Economy | HS-Tree | MODERATE | GOOD | 11 | 9223 | 6 | 1293 |
| Economy | HS-Tree | MODERATE | GOOD | 12 | 4087 | 2 | 1471 |
| Economy | HS-Tree | MODERATE | GOOD | 13 | 4179 | 3 | 1267 |
| Economy | HS-Tree | MODERATE | GOOD | 14 | 4687 | 3 | 1068 |
| Economy | HS-Tree | MODERATE | AVERAGE | 0 | 12494 | 7 | 1452 |
| Economy | HS-Tree | MODERATE | AVERAGE | 1 | 6310 | 4 | 1273 |
| Economy | HS-Tree | MODERATE | AVERAGE | 2 | 5572 | 3 | 1389 |
| Economy | HS-Tree | MODERATE | AVERAGE | 3 | 4256 | 2 | 1589 |
| Economy | HS-Tree | MODERATE | AVERAGE | 4 | 8224 | 6 | 1234 |
| Economy | HS-Tree | MODERATE | AVERAGE | 5 | 5431 | 3 | 1278 |
| Economy | HS-Tree | MODERATE | AVERAGE | 6 | 5280 | 3 | 1324 |
| Economy | HS-Tree | MODERATE | AVERAGE | 7 | 10869 | 7 | 1322 |
| Economy | HS-Tree | MODERATE | AVERAGE | 8 | 11388 | 8 | 1193 |
| Economy | HS-Tree | MODERATE | AVERAGE | 9 | 5836 | 3 | 1422 |
| Economy | HS-Tree | MODERATE | AVERAGE | 10 | 6841 | 5 | 1224 |
| Economy | HS-Tree | MODERATE | AVERAGE | 11 | 6175 | 4 | 1188 |
| Economy | HS-Tree | MODERATE | AVERAGE | 12 | 4195 | 2 | 1411 |
| Economy | HS-Tree | MODERATE | AVERAGE | 13 | 4655 | 3 | 1390 |
| Economy | HS-Tree | MODERATE | AVERAGE | 14 | 8179 | 6 | 1098 |
| Economy | HS-Tree | MODERATE | BAD | 0 | 24906 | 17 | 1337 |
| Economy | HS-Tree | MODERATE | BAD | 1 | 13020 | 11 | 1038 |
| Economy | HS-Tree | MODERATE | BAD | 2 | 31989 | 20 | 1491 |
| Economy | HS-Tree | MODERATE | BAD | 3 | 15079 | 12 | 1140 |
| Economy | HS-Tree | MODERATE | BAD | 4 | 44385 | 24 | 1740 |
| Economy | HS-Tree | MODERATE | BAD | 5 | 10344 | 9 | 1007 |
| Economy | HS-Tree | MODERATE | BAD | 6 | 53481 | 28 | 1794 |
| Economy | HS-Tree | MODERATE | BAD | 7 | 53432 | 27 | 1799 |
| Economy | HS-Tree | MODERATE | BAD | 8 | 12964 | 12 | 915 |
| Economy | HS-Tree | MODERATE | BAD | 9 | 8412 | 7 | 1057 |
| Economy | HS-Tree | MODERATE | BAD | 10 | 57771 | 29 | 1784 |
| Economy | HS-Tree | MODERATE | BAD | 11 | 33864 | 21 | 1499 |
| Economy | HS-Tree | MODERATE | BAD | 12 | 17553 | 13 | 1246 |
| Economy | HS-Tree | MODERATE | BAD | 13 | 15466 | 12 | 1172 |
| Economy | HS-Tree | MODERATE | BAD | 14 | 33644 | 21 | 1492 |
| Economy | HS-Tree | UNIFORM | GOOD | 0 | 4352 | 3 | 912 |
| Economy | HS-Tree | UNIFORM | GOOD | 1 | 4689 | 4 | 953 |
| Economy | HS-Tree | UNIFORM | GOOD | 2 | 4748 | 4 | 963 |
| Economy | HS-Tree | UNIFORM | GOOD | 3 | 4103 | 3 | 920 |
| Economy | HS-Tree | UNIFORM | GOOD | 4 | 3450 | 3 | 971 |
| Economy | HS-Tree | UNIFORM | GOOD | 5 | 6401 | 5 | 961 |
| Economy | HS-Tree | UNIFORM | GOOD | 6 | 4643 | 3 | 985 |
| Economy | HS-Tree | UNIFORM | GOOD | 7 | 3901 | 3 | 1044 |
| Economy | HS-Tree | UNIFORM | GOOD | 8 | 4733 | 4 | 950 |
| Economy | HS-Tree | UNIFORM | GOOD | 9 | 6983 | 6 | 1033 |
| Economy | HS-Tree | UNIFORM | GOOD | 10 | 4058 | 3 | 904 |
| Economy | HS-Tree | UNIFORM | GOOD | 11 | 4002 | 3 | 1026 |
| Economy | HS-Tree | UNIFORM | GOOD | 12 | 4515 | 3 | 960 |
| Economy | HS-Tree | UNIFORM | GOOD | 13 | 6956 | 6 | 1028 |
| Economy | HS-Tree | UNIFORM | GOOD | 14 | 4424 | 3 | 927 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 0 | 11131 | 8 | 1122 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 1 | 9119 | 7 | 1064 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 2 | 8500 | 7 | 1074 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 3 | 6113 | 7 | 764 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 4 | 13233 | 9 | 1181 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 5 | 5740 | 7 | 709 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 6 | 9809 | 8 | 1099 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 7 | 7928 | 6 | 1030 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 8 | 9574 | 8 | 1067 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 9 | 6839 | 5 | 1217 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 10 | 9571 | 7 | 1075 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 11 | 9570 | 7 | 1073 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 12 | 11104 | 8 | 1121 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 13 | 9037 | 7 | 1046 |
| Economy | HS-Tree | UNIFORM | AVERAGE | 14 | 9659 | 7 | 1084 |
| Economy | HS-Tree | UNIFORM | BAD | 0 | 13859 | 12 | 961 |
| Economy | HS-Tree | UNIFORM | BAD | 1 | 17181 | 14 | 1105 |
| Economy | HS-Tree | UNIFORM | BAD | 2 | 16585 | 15 | 992 |
| Economy | HS-Tree | UNIFORM | BAD | 3 | 8764 | 8 | 874 |
| Economy | HS-Tree | UNIFORM | BAD | 4 | 18614 | 14 | 1206 |
| Economy | HS-Tree | UNIFORM | BAD | 5 | 93836 | 40 | 2233 |
| Economy | HS-Tree | UNIFORM | BAD | 6 | 15310 | 12 | 1047 |
| Economy | HS-Tree | UNIFORM | BAD | 7 | 88531 | 39 | 2108 |
| Economy | HS-Tree | UNIFORM | BAD | 8 | 102635 | 41 | 2311 |
| Economy | HS-Tree | UNIFORM | BAD | 9 | 10468 | 11 | 807 |
| Economy | HS-Tree | UNIFORM | BAD | 10 | 22269 | 16 | 1158 |
| Economy | HS-Tree | UNIFORM | BAD | 11 | 20750 | 17 | 1016 |
| Economy | HS-Tree | UNIFORM | BAD | 12 | 31167 | 23 | 1241 |
| Economy | HS-Tree | UNIFORM | BAD | 13 | 9789 | 11 | 780 |
| Economy | HS-Tree | UNIFORM | BAD | 14 | 44464 | 25 | 1667 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 0 | 12075 | 3 | 3727 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 1 | 14396 | 5 | 2655 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 2 | 5740 | 1 | 4955 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 3 | 10775 | 2 | 2618 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 4 | 63129 | 15 | 4090 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 5 | 7183 | 1 | 4427 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 6 | 6457 | 1 | 5514 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 7 | 16127 | 6 | 2324 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 8 | 9695 | 1 | 4755 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 9 | 8033 | 1 | 4839 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 10 | 9506 | 1 | 5315 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 11 | 10927 | 3 | 3504 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 12 | 14269 | 5 | 2569 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 13 | 11089 | 3 | 3071 |
| Economy | Inv-HS-Tree | EXTREME | GOOD | 14 | 22178 | 6 | 3350 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 0 | 12695 | 4 | 3072 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 1 | 33344 | 16 | 1870 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 2 | 20420 | 4 | 4691 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 3 | 23107 | 8 | 2783 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 4 | 6059 | 1 | 4511 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 5 | 64677 | 23 | 2703 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 6 | 8926 | 2 | 4099 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 7 | 6267 | 2 | 2647 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 8 | 36818 | 13 | 2731 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 9 | 35928 | 11 | 3136 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 10 | 19122 | 6 | 3053 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 11 | 17914 | 6 | 2868 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 12 | 19285 | 6 | 2923 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 13 | 8790 | 2 | 3848 |
| Economy | Inv-HS-Tree | EXTREME | AVERAGE | 14 | 12532 | 2 | 5683 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 0 | 83618 | 23 | 3428 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 1 | 62463 | 30 | 1976 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 2 | 19273 | 7 | 2643 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 3 | 229295 | 64 | 3396 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 4 | 94058 | 28 | 3037 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 5 | 89749 | 33 | 2523 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 6 | 47297 | 20 | 2116 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 7 | 23824 | 8 | 2654 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 8 | 27801 | 11 | 2397 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 9 | 692055 | 110 | 6168 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 10 | 204374 | 57 | 3401 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 11 | 66406 | 19 | 3253 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 12 | 100394 | 36 | 2689 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 13 | 138061 | 48 | 2779 |
| Economy | Inv-HS-Tree | EXTREME | BAD | 14 | 78226 | 29 | 2496 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 0 | 41129 | 20 | 1850 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 1 | 25598 | 11 | 2193 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 2 | 19920 | 6 | 3213 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 3 | 26096 | 6 | 4007 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 4 | 11725 | 3 | 3447 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 5 | 28088 | 9 | 2877 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 6 | 16179 | 5 | 3121 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 7 | 46409 | 13 | 3467 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 8 | 10260 | 2 | 3743 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 9 | 16249 | 6 | 2363 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 10 | 16287 | 3 | 4186 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 11 | 34535 | 16 | 2052 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 12 | 21680 | 6 | 3491 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 13 | 9712 | 1 | 6330 |
| Economy | Inv-HS-Tree | MODERATE | GOOD | 14 | 15370 | 4 | 3694 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 0 | 29298 | 13 | 2012 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 1 | 89604 | 28 | 3071 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 2 | 16806 | 6 | 2527 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 3 | 15435 | 4 | 3477 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 4 | 16664 | 7 | 2181 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 5 | 27478 | 10 | 2647 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 6 | 17111 | 5 | 3306 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 7 | 15022 | 5 | 2851 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 8 | 22210 | 9 | 2358 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 9 | 23786 | 8 | 2700 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 10 | 12390 | 3 | 3645 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 11 | 12740 | 6 | 2039 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 12 | 11791 | 5 | 2256 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 13 | 17001 | 6 | 2738 |
| Economy | Inv-HS-Tree | MODERATE | AVERAGE | 14 | 20623 | 6 | 3165 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 0 | 55852 | 20 | 2685 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 1 | 34602 | 16 | 1953 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 2 | 102800 | 37 | 2669 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 3 | 47408 | 22 | 1947 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 4 | 357783 | 80 | 4366 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 5 | 63677 | 22 | 2742 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 6 | 17514 | 10 | 1659 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 7 | 82383 | 34 | 2334 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 8 | 80076 | 25 | 2972 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 9 | 74598 | 27 | 2562 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 10 | 34159 | 11 | 2984 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 11 | 93434 | 33 | 2643 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 12 | 26406 | 12 | 1955 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 13 | 105694 | 30 | 3386 |
| Economy | Inv-HS-Tree | MODERATE | BAD | 14 | 40204 | 17 | 2254 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 0 | 27400 | 12 | 2167 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 1 | 15491 | 4 | 3497 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 2 | 25943 | 8 | 3121 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 3 | 19007 | 4 | 4384 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 4 | 25943 | 8 | 3121 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 5 | 27678 | 12 | 2189 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 6 | 19011 | 4 | 4392 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 7 | 19476 | 5 | 3784 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 8 | 18653 | 4 | 4308 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 9 | 17858 | 6 | 2739 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 10 | 18971 | 4 | 4385 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 11 | 25995 | 8 | 3127 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 12 | 18196 | 6 | 2910 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 13 | 37981 | 11 | 3335 |
| Economy | Inv-HS-Tree | UNIFORM | GOOD | 14 | 15777 | 4 | 3559 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 0 | 14691 | 5 | 2663 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 1 | 15609 | 7 | 2124 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 2 | 36646 | 15 | 2337 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 3 | 26172 | 9 | 2792 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 4 | 29886 | 8 | 3615 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 5 | 26379 | 9 | 2812 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 6 | 29212 | 9 | 2981 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 7 | 44480 | 15 | 2848 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 8 | 25994 | 9 | 2775 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 9 | 26157 | 9 | 2794 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 10 | 15574 | 7 | 2119 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 11 | 29411 | 9 | 3001 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 12 | 33079 | 9 | 3406 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 13 | 32578 | 9 | 3351 |
| Economy | Inv-HS-Tree | UNIFORM | AVERAGE | 14 | 16944 | 4 | 3885 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 0 | 122495 | 35 | 3394 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 1 | 85836 | 23 | 3528 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 2 | 157378 | 47 | 3184 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 3 | 71455 | 21 | 3185 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 4 | 56915 | 18 | 2956 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 5 | 63501 | 27 | 2156 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 6 | 49206 | 18 | 2626 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 7 | 104994 | 33 | 2989 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 8 | 33653 | 11 | 2818 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 9 | 408322 | 88 | 4456 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 10 | 142217 | 40 | 3449 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 11 | 15186 | 3 | 4450 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 12 | 102329 | 30 | 3305 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 13 | 128739 | 41 | 2881 |
| Economy | Inv-HS-Tree | UNIFORM | BAD | 14 | 170211 | 44 | 3680 |

<a href='Hidden comment: 
http://wiki.rmbd.googlecode.com/hg/images/normalCase.jpg
'></a>