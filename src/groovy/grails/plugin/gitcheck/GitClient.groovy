package grails.plugin.gitcheck

import grails.util.BuildSettingsHolder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.RepositoryCache
import org.eclipse.jgit.util.FS

/**
 *  Simple Git Client.
 *
 * User: roos
 * Date: 27.06.13
 * Time: 14:15
 *
 */
class GitClient {

  private static Repository getRepo(String repoBasePath) {
    def baseDir = repoBasePath;
    if (!baseDir && BuildSettingsHolder?.settings?.baseDir) {
      baseDir = "${BuildSettingsHolder?.settings?.baseDir}"
      println "Basedir $baseDir"
    } else if (!baseDir) {
      baseDir = '.'  // fallback
      println "using . as repodir"
    }
    RepositoryCache.open(RepositoryCache.FileKey.lenient(new File(baseDir), FS.DETECTED), true)
  }

  /* get current revision */

  public static def currentRevision() {
    Repository repository = getRepo()
    ObjectId objId = repository.resolve(Constants.HEAD);
    return "${objId.getName()}"
  }

  /* get current branch name */

  public static def currentBranchName(String _baseDir) {
    def repository = getRepo(_baseDir)
    return repository.getFullBranch().substring(Constants.R_HEADS.length())
  }

  /* get untracked changes */

  public static def uncommittedChanges() {
    Git git = new Git(getRepo())
    def uncommitted = []
    def status = git.status().call()
    uncommitted << status.getModified()
    uncommitted << status.getChanged()
    uncommitted << status.getAdded()
    uncommitted << status.getConflicting()
    uncommitted << status.getRemoved()
    uncommitted << status.getUntracked()
    uncommitted.flatten()
  }

  public static def addFile(String fileName){
    Git git = new Git(getRepo())
    git.add().addFilepattern(fileName).call()
  }

  public static def removeFromIndex(String fileName){
    Git git = new Git(getRepo())
    def result = git.rm().addFilepattern(fileName).call()
    println "removed $fileName from index."
  }

  public static def commit(String message){
    Git git = new Git(getRepo())
    git.commit().setMessage(message).call()
  }

  public static def push(){
    Git git = new Git(getRepo())
    git.push().call()
  }

  /* Pull from origin */
  public static def pull(){
    // Note: cannot use Jgit yet due to HTTP auth impl problem
    // Git git = new Git(getRepo())
    // git.pull().call()
    gitExec(['pull'])
  }

  public static def deleteLastCommit(){
    Git git = new Git(getRepo())
    git.reset().setMode(ResetCommand.ResetType.SOFT).setRef('HEAD^').call()
    println "removed last commit."
  }

  static boolean checkGitRepo() {
    return new File("./.git").exists()
  }

  static boolean branchIsAheadOfRemote() {
    return gitExec(['status']).contains('Your branch is ahead of')
  }

  static boolean remoteIsAheadOfBranch(String branchname) {
    return hasRemoteModifications(branchname)
  }

  static def hasRemoteModifications(String branchname) {
    getRemoteModifications(branchname) == null ? false : true
  }

  static def getRemoteModifications(String branchname) {
    String arg = branchname + "..origin/" + branchname
    def remoteModifications = gitExec(['diff', arg])
    return remoteModifications
  }

  // Helper for external git.
  private static String gitExec(List gitArgs, boolean removeNewLines = false) {
    def proc = ['git', gitArgs].flatten().execute()
    def stdErr = proc.err.text
    if (!stdErr.isEmpty()) {
      throw new GitException(stdErr)
    }
    def stdOut = proc.in.text
    if (!stdOut.isEmpty()) {
      def result = stdOut
      def logStr = "#" * 10
      if (removeNewLines) {
        result = result.replaceAll("\\n", "")
        result = result.replaceAll("\\r", "")
        result = result.replaceAll("\\f", "")
      }
      // println " $logStr - ${gitArgs}:  >${result}< \n ${"#" * 10}"
      result
    } else {
      null
    }
  }
}
