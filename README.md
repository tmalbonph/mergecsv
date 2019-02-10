# MergeCSV

MergeCSV project

## Overview

I have a Java project and it was a necessity to merge two CSV file into one with the help of another CSV that shall serve as a template for the resulting merged CSV file.

## Prerequisite

[Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

[Maven 3](https://maven.apache.org/download.cgi)

## Building & Running

This application is written in Java and built using Maven. The code has been tested on Linux. To build the code:

```
cd source
mvn clean install
```

## Testing

- Create output.csv

```
cd source
java -cp target/mergecsv-1.0.7.jar com.albon.util.MergeCSV \
  --a test_files/5f780f0c.csv --b test_files/b44fb40f.csv \
  --t test_files/template.csv --o output.csv
```

- The results:
```
# Should display result as follows:
# Template 'test_files/template.csv' contains 4 columns
# Input 1 'test_files/5f780f0c.csv' contains 6 columns
# Input 2 'test_files/b44fb40f.csv' contains 6 columns
# Creating output 'output.csv'
# Merged 50 CSV records, Ignored 0 CSV record(s).
```

## Verification

```
cd source
diff -cads output.csv test_files/results.csv

# Should display as follows:
# Files output.csv and test_files/results.csv are identical
```

## License

This project is released under the MIT License.

## Contact Information

This project is developed and maintained by [Teddy Albon Sr](mailto:tmalbonph@yahoo.com)
