# DependencyExporter

DependencyExporter is a Gradle plugin that enables automatic creation of JSON files containing data about your project
dependencies, such as download URL, size and SHA-1 digest. This is useful for applications that want to download
dependencies at runtime.

## Configuration

In the `dependencyExport` section you can select a configuration to export and the file that will be written and
automatically shaded in your jar. You can do this multiple times. You can also add constraints so that some dependency
classifier are automatically recognised, and data about system and architecture are addded.

## Example

#### Configuration

```groovy
plugins {
    id 'net.smoofyuniverse.dependency-exporter' version '1.0.2'
}

dependencyExport {
    loggerImpl {
        path = "dep/logger.json"
        config = configurations.loggerImpl
        skipWhenEmpty = true
    }

    javafx {
        path = "dep/javafx.json"
        config = configurations.javaFXRuntime
        constraint("win", ["windows"], ["x64"])
        constraint("win-x86", ["windows"], ["x86"])
        constraint("mac", ["macos"], ["x64"])
        constraint("mac-aarch64", ["macos"], ["arm64"])
        constraint("linux", ["linux"], ["x64"])
        constraint("linux-aarch64", ["linux"], ["arm64"])
        constraint("linux-arm32-monocle", ["linux"], ["arm32"])
    }
}
```

#### Output

##### dep/logger.json

```json
[
  {
    "name": "org.apache.logging.log4j:log4j-slf4j-impl:2.14.1",
    "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-slf4j-impl/2.14.1/log4j-slf4j-impl-2.14.1.jar",
    "size": 23625,
    "digest": "9a40554b8dab7ac9606089c87ae8a5ba914ec932"
  },
  {
    "name": "org.apache.logging.log4j:log4j-core:2.14.1",
    "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-core/2.14.1/log4j-core-2.14.1.jar",
    "size": 1745700,
    "digest": "9141212b8507ab50a45525b545b39d224614528b"
  },
  {
    "name": "org.apache.logging.log4j:log4j-api:2.14.1",
    "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-api/2.14.1/log4j-api-2.14.1.jar",
    "size": 300365,
    "digest": "cd8858fbbde69f46bce8db1152c18a43328aae78"
  }
]
```

##### dep/javafx.json

```json
[
  {
    "name": "org.openjfx:javafx-base:17:linux",
    "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/17/javafx-base-17-linux.jar",
    "size": 745750,
    "digest": "476c7640c2320244d3156edbbc19a7eda275c1ed",
    "systems": [
      "linux"
    ],
    "archs": [
      "x64"
    ]
  },
  {
    "name": "org.openjfx:javafx-base:17:linux-aarch64",
    "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/17/javafx-base-17-linux-aarch64.jar",
    "size": 745749,
    "digest": "b62b6cae95847fda1dce4a8a29edcf8a31b85333",
    "systems": [
      "linux"
    ],
    "archs": [
      "arm64"
    ]
  },
  {
    "name": "org.openjfx:javafx-base:17:mac",
    "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/17/javafx-base-17-mac.jar",
    "size": 745746,
    "digest": "61d52392b063be78f7cc1b0fbfc04dda7a835a15",
    "systems": [
      "macos"
    ],
    "archs": [
      "x64"
    ]
  },
  {
    "name": "org.openjfx:javafx-base:17:win",
    "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/17/javafx-base-17-win.jar",
    "size": 745751,
    "digest": "b63b9696f73009ca5002f49ec4db73576e2de168",
    "systems": [
      "windows"
    ],
    "archs": [
      "x64"
    ]
  }
]
```