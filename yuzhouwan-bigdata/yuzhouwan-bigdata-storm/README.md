# Storm examples

Storm 3 requires JDK 25. Set `JAVA_HOME` to a JDK 25 installation before
building this module or the complete Maven reactor:

```sh
mvn -pl yuzhouwan-bigdata/yuzhouwan-bigdata-storm -am clean verify
```

The test workflow, Java CodeQL build, and Docker builder also use JDK 25.
Other modules retain their Java 21 compilation target.
