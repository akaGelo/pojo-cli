Super simple library for parsing command line arguments to POJO Object.


[![Build Status](https://travis-ci.org/akaGelo/pojo-cli.svg?branch=master)](https://travis-ci.org/akaGelo/pojo-cli)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.vyukov/pojo-cli/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.vyukov/pojo-cli)


```xml
<dependency>
    <groupId>ru.vyukov</groupId>
    <artifactId>pojo-cli</artifactId>
    <version>0.1</version>
</dependency>

```

Usage

```java
public class MyPojo {

	@CliParam(name = "-port")
	private int port;
	
	... get/set
}

```

```java

public class App {

	public static void main(String[] args) {
			CliParser cliParser = new CliParser(args);
			
			if (cliParser.hasArgument("-a")){
					...
			}
			
			MyPojo pojo = cliParser.parse(MyPojo.class);
	}
}
	
```