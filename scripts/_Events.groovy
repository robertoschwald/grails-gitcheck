

eventCreateWarStart = { warName, stagingDir ->

  classLoader.loadClass('grails.plugin.gitcheck.GitClient')
  classLoader.loadClass('grails.plugin.gitcheck.GitException')

  // load config
  createConfig()

  def gitPath = "${grailsSettings.baseDir}/.git"
  if (new File(gitPath).exists()){
    def gitRevision =  grails.plugin.gitcheck.GitClient.currentRevision()
    println "Git Revision: ${gitRevision}"
  }

  // check if current branch is a valid one for this env. war creation.
  if (!config.scm.skipGitBranchCheck) {
    def currentBranchName =  grails.plugin.gitcheck.GitClient.currentBranchName()
    assert currentBranchName in config.scm.validBranch, "\n\nCurrent branch >${currentBranchName}< is invalid for package, please checkout one of >${config.scm.validBranch}< \n\n"
    println "SCM branch >${currentBranchName}< is valid."
  } else {
    println "Skipping gitBranchCheck as scm.skipGitBranchCheck is set in Config.groovy"
  }

  // check that all files are committed
  if (!config.scm.skipGitCommitCheck){
    def uncommittedChanges = grails.plugin.gitcheck.GitClient.uncommittedChanges()
    assert !uncommittedChanges, "\n\nYou have uncommitted changes!\n$uncommittedChanges"
  } else {
    println "Skipping git commit check as scm.skipGitBranchCheck is set in Config.groovy"
  }

  // check unpushed changes
  if (!config.scm.skipGitPushedCheck){
     assert !grails.plugin.gitcheck.GitClient.branchIsAheadOfRemote(), "Local branch is ahead of origin!"
  } else {
    println "Skipping git push check as scm.skipGitPushedCheck is set in Config.groovy"
  }

  // check origin un-pulled changes
  if (!config.scm.skipGitOriginCheck) {
    println "Checking if origin is ahead"
    assert !grails.plugin.gitcheck.GitClient.remoteIsAheadOfBranch(grails.plugin.gitcheck.GitClient.currentBranchName()), "Origin is ahead of local branch. Please perform pull"
  } else {
    println "Skipping git origin check as scm.skipGitOriginCheck is set in Config.groovy"
  }

}