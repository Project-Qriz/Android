pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "qriz"
include(":app")

include(":core:navigation")
include(":core:datastore")
include(":core:designsystem")
include(":core:testing")

include(":core:network:core")
include(":core:network:common")
include(":core:network:user")
include(":core:network:onboard")

include(":core:data:user:user-api")
include(":core:data:user:user")
include(":core:data:onboard:onboard-api")
include(":core:data:onboard:onboard")
include(":core:data:token:token")
include(":core:data:token:token-api")
include(":core:data:test:test-api")
include(":core:data:conceptbook:conceptbook")
include(":core:data:conceptbook:conceptbook-api")
include(":core:data:application:application")
include(":core:data:application:application-api")
include("core:data:daily-study:daily-study-api")

include(":core:ui:test")
include(":core:ui:common")

include(":feature:base")
include(":feature:main")
include(":feature:splash")
include(":feature:sign")
include(":feature:onboard")
include(":feature:home")
include(":feature:concept-book")
include(":feature:incorrect-answers-note")
include(":feature:mypage")
include(":core:model")
include(":core:network:application")
