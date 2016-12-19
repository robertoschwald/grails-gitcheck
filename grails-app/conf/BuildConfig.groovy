grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual'

grails.project.dependency.resolution = {

  inherits 'global'
  log "warn"

  repositories {
    grailsCentral()
    mavenLocal()
    mavenCentral()
  }

  dependencies {
    compile "commons-io:commons-io:2.5"
    compile "org.eclipse.jgit:org.eclipse.jgit:2.3.1.201302201838-r"
    test("org.gmock:gmock:0.8.1") {
      export = false
    }
  }

  plugins {
    build ':release:2.2.1', ':rest-client-builder:1.0.3', {
      export = false
    }
  }
}
