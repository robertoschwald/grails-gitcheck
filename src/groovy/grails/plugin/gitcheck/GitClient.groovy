package grails.plugin.gitcheck

import grails.util.BuildSettingsHolder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.RepositoryCache
import org.eclipse.jgit.storage.file.FileRepository
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
  private static final GIT_DIR = "${BuildSettingsHolder.settings.baseDir}/.git"

  static Repository getRepo = {
    RepositoryCache.open(RepositoryCache.FileKey.lenient(new File(GIT_DIR), FS.DETECTED), true)
  }

  /* get current revision */
  public static def currentRevision() {
    assert checkGitRepo(), "This project doesn't seem to be backed by a Git repository."
    def repository = getRepo
    ObjectId objId = repository.resolve(Constants.HEAD);
    return "${objId.getName()}"
  }

  /* get current branch name */
  public static def currentBranchName() {
    def repository = new FileRepository(GIT_DIR)
    return repository.getFullBranch().substring(Constants.R_HEADS.length())
  }

  /* get untracked changes */
  public static def uncommittedChanges() {
    Git git = new Git(getRepo(GIT_DIR))
    def uncommitted = []
    def status = git.status().call()
    uncommitted << status.getModified()
    uncommitted << status.getChanged()
    uncommitted << status.getAdded()
    uncommitted << status.getConflicting()
    uncommitted << status.getRemoved()
    uncommitted << status.getUntracked()
    uncommitted
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
    return new File(GIT_DIR).exists()
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
  private static def gitExec(List gitArgs, boolean removeNewLines = false) {
    def stdout = new ByteArrayOutputStream()
    def proc = 'git $gitArgs'.execute()
    def stdErr =  ${proc.err.text}
    def stdOut = ${proc.in.text}
    if (stdout.toByteArray().length > 0) {
      def result = stdout.toString()
      println "#" * 10
      println "removeNewLines: " + removeNewLines
      if (removeNewLines) {
        result = result.replaceAll("\\n", "")
        result = result.replaceAll("\\r", "")
        result = result.replaceAll("\\f", "")
      }
      println " - ${gitArgs}:  >${result}<"
      println "#" * 10
      result
    } else {
      null
    }
  }
}
