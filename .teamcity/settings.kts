
import com.github.rodm.teamcity.gradle.switchGradleBuildStep
import com.github.rodm.teamcity.project.githubIssueTracker

import jetbrains.buildServer.configs.kotlin.v2019_2.version
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.CheckoutMode
import jetbrains.buildServer.configs.kotlin.v2019_2.Template
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

version = "2019.2"

project {

    val vcsId = "GradleTeamcityPlugin"
    val vcs = GitVcsRoot {
        id(vcsId)
        name = "gradle-teamcity-plugin"
        url = "https://github.com/rodm/gradle-teamcity-plugin.git"
        branchSpec = """
            +:refs/heads/(master)
            +:refs/tags/(*)
        """.trimIndent()
        useTagsAsBranches = true
        useMirrors = false
    }
    vcsRoot(vcs)

    features {
        githubIssueTracker {
            displayName = "GradleTeamCityPlugin"
            repository = "https://github.com/rodm/gradle-teamcity-plugin"
            pattern = """#(\d+)"""
        }
    }

    params {
        param("teamcity.ui.settings.readOnly", "true")
    }

    val buildTemplate = Template {
        id("Build")
        name = "build"

        params {
            param("gradle.opts", "")
            param("gradle.tasks", "clean build")
            param("java.home", "%java8.home%")
        }

        vcs {
            root(vcs)
            checkoutMode = CheckoutMode.ON_SERVER
            cleanCheckout = true
        }

        steps {
            gradle {
                id = "GRADLE_BUILD"
                tasks = "%gradle.tasks%"
                buildFile = ""
                gradleParams = "%gradle.opts%"
                useGradleWrapper = true
                enableStacktrace = true
                jdkHome = "%java.home%"
            }
        }

        triggers {
            vcs {
                id = "vcsTrigger"
                quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
                branchFilter = "+:*"
            }
        }

        failureConditions {
            executionTimeoutMin = 10
        }

        features {
            feature {
                id = "perfmon"
                type = "perfmon"
            }
        }
    }
    template(buildTemplate)

    val buildJava = BuildType {
        templates(buildTemplate)
        id("BuildJava7")
        name = "Build - Java 8"
    }
    buildType(buildJava)

    val buildJava11 = buildType {
        templates(buildTemplate)
        id("BuildJava11")
        name = "Build - Java 11"

        params {
            param("java.home", "%java11.home%")
        }
    }

    val buildFunctionalTestJava8 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava8")
        name = "Build - Functional Test - Java 8"

        params {
            param("gradle.tasks", "clean functionalTest")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava8)

    val buildFunctionalTestJava9 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava9")
        name = "Build - Functional Test - Java 9"

        params {
            param("gradle.tasks", "clean functionalTest")
            param("java.home", "%java9.home%")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava9)

    val buildFunctionalTestJava10 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava10")
        name = "Build - Functional Test - Java 10"

        params {
            param("gradle.tasks", "clean functionalTest")
            param("java.home", "%java10.home%")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava10)

    val buildFunctionalTestJava11 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava11")
        name = "Build - Functional Test - Java 11"

        params {
            param("gradle.tasks", "clean functionalTest")
            param("java.home", "%java11.home%")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava11)

    val buildFunctionalTestJava12 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava12")
        name = "Build - Functional Test - Java 12"

        params {
            param("gradle.tasks", "clean functionalTest")
            param("gradle.version", "5.4")
            param("java.home", "%java12.home%")
        }

        steps {
            switchGradleBuildStep()
            stepsOrder = arrayListOf("SWITCH_GRADLE", "GRADLE_BUILD")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava12)

    val buildFunctionalTestJava13 = BuildType {
        templates(buildTemplate)
        id("BuildFunctionalTestJava13")
        name = "Build - Functional Test - Java 13"

        params {
            param("gradle.tasks", "clean functionalTest")
            param("gradle.version", "6.0")
            param("java.home", "%java13.home%")
        }

        steps {
            switchGradleBuildStep()
            stepsOrder = arrayListOf("SWITCH_GRADLE", "GRADLE_BUILD")
        }

        failureConditions {
            executionTimeoutMin = 20
        }
    }
    buildType(buildFunctionalTestJava13)

    val buildSamplesTest = BuildType {
        templates(buildTemplate)
        id("BuildSamplesTestJava7")
        name = "Build - Samples Test - Java 8"

        artifactRules = "samples/**/build/distributions/*.zip"

        params {
            param("gradle.tasks", "clean samplesTest")
        }

        failureConditions {
            executionTimeoutMin = 15
        }
    }
    buildType(buildSamplesTest)

    val reportCodeQuality = BuildType {
        templates(buildTemplate)
        id("ReportCodeQuality")
        name = "Report - Code Quality"

        params {
            param("gradle.opts", "%sonar.opts%")
            param("gradle.tasks", "clean build sonarqube")
        }
    }
    buildType(reportCodeQuality)

    buildTypesOrder = arrayListOf(
        buildJava,
        buildJava11,
        buildFunctionalTestJava8,
        buildFunctionalTestJava9,
        buildFunctionalTestJava10,
        buildFunctionalTestJava11,
        buildFunctionalTestJava12,
        buildFunctionalTestJava13,
        buildSamplesTest,
        reportCodeQuality
    )
}
