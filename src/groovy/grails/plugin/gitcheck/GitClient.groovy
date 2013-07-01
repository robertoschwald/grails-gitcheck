package grails.plugin.gitcheck

import grails.util.BuildSettingsHolder
import org.eclipse.jgit.api.Git
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
      baseDir = "${BuildSettingsHolder?.settings?.baseDir}/.git"
    } else if (!baseDir) {
      baseDir = '.'  // fallback
    }
    RepositoryCache.open(RepositoryCache.FileKey.lenient(new File(baseDir), FS.DETECTED), true)
  }

  /* get current revision */

  public static def currentRevision() {
    assert checkGitRepo(), "This project doesn't seem to be backed by a Git repository."
    def repository = getRepo()
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

  /* get any origin pending change set updates */
  /* Note: Due to the lack of HTTP Authentication support in
     JGit, we use the installed local native git client.
   */

  static def fetchPendingOriginChanges(path, progressMonitor) {
    try {
      /* JGIT once we know how to authenticate http urls the same way as native git.
       def repository = getRepo
       Git git = new Git(repository)
       FetchResult result = git.fetch()
           .setRemoveDeletedRefs(true)
           .call()
       return result
       */
    } catch (Exception e) {
      throw new Exception(e.cause.message)
    }
  }

  static def checkGitRepo(String repoPath) {
    return new File('${repoPath}/.git').exists()
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