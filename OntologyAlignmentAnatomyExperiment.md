# Download & install software #

First you need to download [Maven](http://maven.apache.org/download.html) and [Mercurial](http://mercurial.selenic.com/downloads/).

After you installed the software you can check out the code with

`hg clone https://code.google.com/p/rmbd/`

You can do `mvn install` in the root directory of the project to download all necessary libs and also compile the software.

# Run Tests #

You can execute the tests in the `<project root>\owlcontroller` directory.

Running `mvn -Dtest=UnsolvableTests\#doTestsOAEIAnatomyTrack test` you can run the tests.

A directory logs is created in the root directory of the project or in the owlcontroller directory (differs because of operating system ...). In this directory you find a file called owlcontroller.log where you can see the results appended at the end of the file.