import grails.plugin.gitcheck.GitClient
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.storage.file.FileRepository

eventCreateWarStart = { warName, stagingDir ->

  // load config
  createConfig()

  if (new File("${grailsSettings.baseDir}/.git").exists()){
    def gitRevision =  GitClient.currentRevision()
    println "Git Revision: ${gitRevision}"
  }

  // branchCheck
  if (!config.scm.skipGitBranchCheck) {
    def currentBranchName =  GitClient.currentBranchName()
    assert currentBranchName in config.scm.validBranch, "\n\nCurrent branch >${GitClient.gitBranchName()}< is invalid for package, please checkout one of >${config.scm.validBranch}< \n\n"
    println "SCM branch ${currentBranchName} is valid."
  } else {
    println "Skipping gitBranchCheck as scm.skipGitBranchCheck is set in Config.groovy"
  }

  // commitCheck
  if (!config.scm.skipGitCommitCheck){
    def uncommittedChanges = GitClient.uncommittedChanges()
    assert !uncommittedChanges, "\n\nYou have uncommitted changes!\n$uncommittedChanges"
  }

  // PushedChanges check


  // origin un-pulled changes
  if (!config.scm.skipGitOriginCheck){
     assert !GitClient.branchIsAheadOfRemote(), "Branch is ahead of origin!"
  }

  if (!config.scm.skipGitPushedCheck) {
    assert !GitClient.remoteIsAheadOfBranch(GitClient.currentBranchName()), "Origin is ahead of branch. Please perform pull"
  }

}