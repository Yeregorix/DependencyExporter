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
    id 'net.smoofyuniverse.dependency-exporter' version '1.0.6'
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
        "name": "org.apache.logging.log4j:log4j-slf4j-impl:2.17.1",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-slf4j-impl/2.17.1/log4j-slf4j-impl-2.17.1.jar",
        "size": 24279,
        "digest": "84692d456bcce689355d33d68167875e486954dd"
    },
    {
        "name": "org.apache.logging.log4j:log4j-core:2.17.1",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-core/2.17.1/log4j-core-2.17.1.jar",
        "size": 1790452,
        "digest": "779f60f3844dadc3ef597976fcb1e5127b1f343d"
    },
    {
        "name": "org.apache.logging.log4j:log4j-api:2.17.1",
        "url": "https://repo.maven.apache.org/maven2/org/apache/logging/log4j/log4j-api/2.17.1/log4j-api-2.17.1.jar",
        "size": 301872,
        "digest": "d771af8e336e372fb5399c99edabe0919aeaf5b2"
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
        "digest": "76f2c504904f96208026fde914701001a668716e",
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
        "digest": "e8d960421a991229e373a417a1202da19b1782d8",
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
        "digest": "a2fdc6292a7727a389dc41d42dd1a86040ee7625",
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
        "digest": "d9e58df4eb0c61e90165665c404d2f51ea39408b",
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
        "digest": "0adb6ee2aa7dff77286a0673dd0c001e318e7224",
        "systems": [
            "windows"
        ],
        "archs": [
            "x64"
        ]
    }
]
```
