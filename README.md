

[![Build Status](https://travis-ci.org/skjolber/extend.svg?branch=master)](https://travis-ci.org/skjolber/extend)

# extend
Simple project for manipulation of compiled libraries so that classes can accessed and/or extended (i.e. subclassed).

The project uses [ASM] to do the following edits to bytecode:

 * update access-modifier for fields, methods and classes (from private to public), and
 * correspondingly update method invocations, and
 * remove final modifier on methods, classes and some fields

Bugs, feature suggestions and help requests can be filed with the issue-tracker.
## License
[Apache 2.0]

# Obtain
The project is based on [Maven]. Add coordinates

```xml
<dependency>
    <groupId>com.github.skjolber.extend</groupId>
    <artifactId>core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

for [Maven] for or 


```groovy
ext {
    extendVersion = '1.0.0-SNAPSHOT'
}
```

with

```groovy
implementation("com.github.skjolber.extend:core:${extendVersion}")
```

for Gradle. Java 11+ projects please use module `com.github.skjolber.extend.core`.

# Usage
Run the main class `com.github.skjolber.extend.core.Extender` while passing parameters `input` and `output`, where input typically is the path to `classes.jar` and output the path to `classes.ext.jar`.

# History

 - 1.0.0: Initial version

[Apache 2.0]: http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]: https://github.com/skjolber/extend/issues
[Maven]: https://maven.apache.org/
[ASM]: https://asm.ow2.io/
