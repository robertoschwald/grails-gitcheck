grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  repositories {
    mavenRepo "http://192.168.121.100:8081/artifactory/libs-releases"
    grailsCentral()
    //mavenLocal()
    mavenCentral()
  }
  dependencies {
    compile "org.eclipse.jgit:org.eclipse.jgit:2.3.1.201302201838-r"
    test("org.gmock:gmock:0.8.1") {
      export = false
    }
  }

  plugins {
    build(":release:2.2.0") {
      export = false
    }
  }
}
