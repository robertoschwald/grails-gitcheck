class GitcheckGrailsPlugin {
    def version = "0.3.0-SNAPSHOT"
    def grailsVersion = "2.0 > *"
    def title = "Gitcheck Plugin"
    def author = "Robert Oschwald"
    def authorEmail = "roos@symentis.com"
    def description = 'Provides the ability to check Git respository status, e.g. uncommited files, missing updates, etc. while creating a war file.'
    def documentation = "http://grails.org/plugin/gitcheck"

    def license = "APACHE"
    def organization = [ name: "symentis", url: "http://www.symentis.com/" ]
    def issueManagement = [ system: "Github", url: "https://github.com/robertoschwald/grails-gitcheck/issues" ]
    def scm = [ url: "https://github.com/robertoschwald/grails-gitcheck" ]
}
