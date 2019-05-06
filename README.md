# java-web

`java-web` provides a set of classes for working seamlessly within web
environments: servlets, portlets.

It provides helpers for managing sessions, templates, views, etc.

It is provided as a standalone package as it is used by both `collab-wiki`
and `collab-forum`.

## Dependencies

This repository is part of a multi-project `gradle` build.

It has the following dependencies:

- [java-common](https://github.com/ugorji/java-common)
- [java-markup](https://github.com/ugorji/java-markup)

Before building:

- clone the dependencies into adjacent folders directly under same parent folder
- download [`settings.gradle`](https://gist.githubusercontent.com/ugorji/2a338462e63680d117016793989847fa/raw/settings.gradle) into the parent folder

## Building

```sh
gradle clean
gradle build
```

