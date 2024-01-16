# CDI Examples

This project uses CDI 4.0 along with Weld SE (5.1) as its implementation to demonstrate some of CDI capabilities.
The sources are separated into packages; each focusing on particular functionality.

The main class is used solely to demonstrate how CDI SE container bootstrap works and otherwise does nothing.

## Building the project

`mvn clean install` does the build along with test execution. In case you wish to skip tests, add `-DskipTests`.

## Running a test

Running a sinle test class is as simple as doing: `mvn clean verify -Dtest=NameOfTheTest`
