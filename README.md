# Gradle TeamCity plugin

Gradle plugin to support the development of TeamCity plugins

[![Build Status](https://travis-ci.org/rodm/gradle-teamcity-plugin.svg?branch=master)](https://travis-ci.org/rodm/gradle-teamcity-plugin)

The plugin applies the Java Plugin, adds the JetBrains Maven repository, adds the TeamCity server-api dependency
to the compile configuration, adds the tests-support dependency to the testCompile configuration and adds a number of
tasks to package the plugin into a zip, deploy the plugin and start and stop both the server and build agent.

## Usage

The plugin is published on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.github.rodm.teamcity), follow
the instructions on the page to add the plugin to your build script. 

### Extension Properties

The following properties are defined in the `teamcity` configuration block. The version property controls the version of
the TeamCity server-api added to the `compile` configuration and the version downloaded and installed. The plugin
descriptor can be specified as a path to a file or by a configuration block within the build script.

* `version` : The version of the TeamCity API to build against. Defaults to '9.0'.
* `descriptor` : The plugin descriptor, the descriptor can be defined within the build script or reference an external file.  
* `homeDir` : The path to a TeamCity install.
* `dataDir` : The path to the TeamCity Data directory.
* `javaHome` : The path to the version of Java used to run the server and build agent.
* `downloadBaseUrl` : The base URL used to download the TeamCity installer. Default 'http://download.jetbrains.com/teamcity'.

The plugin descriptor properties are shown in the examples below and described in the TeamCity documentation for [Packaging Plugins](https://confluence.jetbrains.com/display/TCD9/Plugins+Packaging#PluginsPackaging-PluginDescriptor)  

### Tasks

* `packagePlugin` : Builds and packages a TeamCity plugin
* `deployPlugin` : Deploys the plugin archive to a local TeamCity server, requires the `dataDir` property to be defined.
* `undeployPlugin` : Undeploys the plugin archive from a local TeamCity server, requires the `dataDir` property to be defined.
* `startSever` : Starts the TeamCity Server, requires the `homeDir` and `dataDir` properties to be defined.
* `stopServer` : Stops the TeamCity Server, requires the `homeDir` property to be defined.
* `startAgent` : Starts the default TeamCity Build Agent, requires the `homeDir` property to be defined.
* `stopAgent` : Stops the default TeamCity Build Agent, requires the `homeDir` property to be defined.
* `installTeamCity` : Downloads and installs TeamCity, this tasks uses the `downloadBaseUrl` and the `homeDir` properties.

### Examples

Plugin descriptor defined in the build script.
```
teamcity {
    // Use TeamCity 8.1 API 
    version = '8.1.5'

    // Plugin descriptor
    descriptor {
        // required properties
        name = project.name
        displayName = 'TeamCity Plugin'        
        version = project.version
        vendorName = 'vendor name'
        
        // optional properties
        description = 'Example TeamCity plugin'
        downloadUrl = 'download url'
        email = 'me@example.com'
        vendorUrl = 'vendor url'
        vendorLogo = 'vendor logo'
        useSeparateClassloader = 'true'

        parameters {
            parameter 'name1', 'value1'
            parameter 'name2', 'value2'
        }
    }
            
    // local TeamCity instance properties        
    homeDir = file("/opt/TeamCity")
    dataDir = file("$rootDir/data")
    javaHome = file("/opt/jdk1.7.0_80")
    
    // local web server for downloading TeamCity distributions 
    downloadBaseUrl = "http://repository/"
}
```

Plugin descriptor defined in an external file at the root of the project.
```
teamcity {
    // Use TeamCity 8.1 API 
    version = '8.1.5'
    // Locate the plugin descriptor in the project root
    descriptor = file('teamcity-plugin.xml')
}
```
