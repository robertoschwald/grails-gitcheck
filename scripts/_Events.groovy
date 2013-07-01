import grails.plugin.gitcheck.GitClient

eventCreateWarStart = { warName, stagingDir ->

  // load config
  createConfig()

  if (new File("${grailsSettings.baseDir}/.git").exists()){
    def gitRevision =  GitClient.currentRevision()
    println "Git Revision: ${gitRevision}"
  }

  // check if current branch is a valid one for this env. war creation.
  if (!config.scm.skipGitBranchCheck) {
    def currentBranchName =  GitClient.currentBranchName()
    assert currentBranchName in config.scm.validBranch, "\n\nCurrent branch >${GitClient.gitBranchName()}< is invalid for package, please checkout one of >${config.scm.validBranch}< \n\n"
    println "SCM branch ${currentBranchName} is valid."
  } else {
    println "Skipping gitBranchCheck as scm.skipGitBranchCheck is set in Config.groovy"
  }

  // check that all files are committed
  if (!config.scm.skipGitCommitCheck){
    def uncommittedChanges = GitClient.uncommittedChanges()
    assert !uncommittedChanges, "\n\nYou have uncommitted changes!\n$uncommittedChanges"
  }

  // check unpushed changes
  if (!config.scm.skipGitPushedCheck){
     assert !GitClient.branchIsAheadOfRemote(), "Branch is ahead of origin!"
  }

  // check origin un-pulled changes
  if (!config.scm.skipGitOriginCheck) {
    assert !GitClient.remoteIsAheadOfBranch(GitClient.currentBranchName()), "Origin is ahead of branch. Please perform pull"
  }

}