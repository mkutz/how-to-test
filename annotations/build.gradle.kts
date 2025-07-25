@file:Suppress("UnstableApiUsage")

plugins {
  java
  `java-test-fixtures`
  `jvm-test-suite`
  id("org.springframework.boot") version "3.5.3"
  id("io.spring.dependency-management") version "1.1.7"
}

java { sourceCompatibility = JavaVersion.VERSION_21 }

repositories { mavenCentral() }

extra["testcontainers.version"] = "1.19.7"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  runtimeOnly("org.postgresql:postgresql")

  testFixturesImplementation("org.apache.commons:commons-lang3:3.18.0")
  testFixturesImplementation("org.apache.commons:commons-rng-simple:1.6")
}

testing {
  suites {
    withType(JvmTestSuite::class).configureEach {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api")
        implementation("org.assertj:assertj-core")
        runtimeOnly("org.junit.platform:junit-platform-launcher")
        runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
      }
    }
    val test by
      getting(JvmTestSuite::class) {
        dependencies { implementation("org.junit.jupiter:junit-jupiter-params") }
      }

    val integrationTest by
      register<JvmTestSuite>("integrationTest") {
        testType.set(TestSuiteType.INTEGRATION_TEST)
        dependencies {
          implementation(testFixtures(project()))
          implementation("org.springframework.boot:spring-boot-starter-test")
          implementation("org.springframework.boot:spring-boot-starter-webflux")
          implementation("org.springframework.boot:spring-boot-starter-data-jpa")
          implementation("org.springframework.boot:spring-boot-testcontainers")
          implementation("org.testcontainers:postgresql")
          implementation("org.testcontainers:junit-jupiter")
          runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.107.Final:osx-aarch_64")
        }
        targets { all { testTask.configure { shouldRunAfter(test) } } }
      }
  }
}

tasks.check { dependsOn(testing.suites.named("integrationTest")) }
