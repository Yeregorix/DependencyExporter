# DependencyExporter

DependencyExporter is a Gradle plugin that enables automatic creation of JSON files containing data about your project
dependencies, such as download URL, size and SHA-256 digest. This is useful for applications that want to download
dependencies at runtime.

## Configuration

In the `dependencyExport` section you can select a configuration to export and the file that will be written and
automatically shaded in your jar. You can do this multiple times. You can also add constraints so that some dependency
classifier are automatically recognised, and data about system and architecture are addded.

## Example

#### Configuration

```groovy
plugins {
    id 'net.smoofyuniverse.dependency-exporter' version '1.0.7'
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
        
        // shortcut: presetOpenJFX()
        constraint("win", "windows", "x64")
        constraint("mac", "macos", "x64")
        constraint("mac-aarch64", "macos", "arm64")
        constraint("linux", "linux", "x64")
        constraint("linux-aarch64", "linux", "arm64")
    }
}
```

#### Output

##### dep/logger.json

```json
[
    {
        "name": "org.apache.logging.log4j:log4j-slf4j-impl:2.17.2",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-slf4j-impl/2.17.2/log4j-slf4j-impl-2.17.2.jar",
        "size": 24248,
        "digest": "77912d47190a5d25d583728e048496a92a2cb32308b71d3439931d7719996637"
    },
    {
        "name": "org.apache.logging.log4j:log4j-core:2.17.2",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-core/2.17.2/log4j-core-2.17.2.jar",
        "size": 1811089,
        "digest": "5adb34ff4197cd16a8d24f63035856a933cb59562a6888dde86e9450fcfef646"
    },
    {
        "name": "org.apache.logging.log4j:log4j-api:2.17.2",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar",
        "size": 302511,
        "digest": "09351b5a03828f369cdcff76f4ed39e6a6fc20f24f046935d0b28ef5152f8ce4"
    }
]
```

##### dep/javafx.json

```json
[
    {
        "name": "org.openjfx:javafx-base:21.0.1:linux",
        "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-linux.jar",
        "size": 754325,
        "digest": "88a57b4eb65c7da430b44a3a1c5409e37ee87db429539cfe79251aaa0422f666",
        "systems": [
            "linux"
        ],
        "archs": [
            "x64"
        ]
    },
    {
        "name": "org.openjfx:javafx-base:21.0.1:linux-aarch64",
        "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-linux-aarch64.jar",
        "size": 754326,
        "digest": "16f01b399c84cb439ca3a7f2f24a2e0bc768de49f94e45862eb8e8578315ccfe",
        "systems": [
            "linux"
        ],
        "archs": [
            "arm64"
        ]
    },
    {
        "name": "org.openjfx:javafx-base:21.0.1:mac",
        "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-mac.jar",
        "size": 754326,
        "digest": "830b7882d3d8d46270e1a1ddc94d464f2b63a2cefa2e92d968433f590f6d0c9e",
        "systems": [
            "macos"
        ],
        "archs": [
            "x64"
        ]
    },
    {
        "name": "org.openjfx:javafx-base:21.0.1:mac-aarch64",
        "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-mac-aarch64.jar",
        "size": 754326,
        "digest": "fede71174732edae1fb3f794a05e856a13b63be097a1f354e54f1a302ead810f",
        "systems": [
            "macos"
        ],
        "archs": [
            "arm64"
        ]
    },
    {
        "name": "org.openjfx:javafx-base:21.0.1:win",
        "url": "https://repo.maven.apache.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-win.jar",
        "size": 754326,
        "digest": "0a46d1c7c69e177b937612574647eb54a31c6498cba6ee5ea20a0e8c2d4c2444",
        "systems": [
            "windows"
        ],
        "archs": [
            "x64"
        ]
    }
]
```
