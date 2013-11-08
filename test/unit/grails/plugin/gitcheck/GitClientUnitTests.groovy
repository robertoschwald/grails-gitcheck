package grails.plugin.gitcheck

import static grails.test.MockUtils.*
import grails.test.GrailsUnitTestCase

/**
 * @author roos
 */
class GitClientUnitTests extends GrailsUnitTestCase {

  protected void setUp() {
    super.setUp()
    mockLogging(getClass(), true)
  }

  void testCurrentBranchName() {
    log.debug("testCurrentBranchName()")
    assert GitClient.currentBranchName(".") == 'master'
  }

  void testUncommittedChanges(){
    log.debug("testUncommittedChanges()")
    def testFileName = 'testUncommittedChanges.testfile'
    File testFile = new File(testFileName)
    if (!testFile.exists()) testFile.createNewFile()
    def uncommitted = GitClient.uncommittedChanges()
    assert uncommitted.contains(testFileName), "Current repo does not contain expected uncomitted testFile"
    testFile.delete()
  }

  void testBranchIsAheadOfRemote(){
    log.debug("testBranchIsAheadOfRemote()")
    def testFileName = "testBranchAheadOfRemote.testfile"
    File testFile = new File(testFileName)
    if (!testFile.exists()) testFile.createNewFile()
    // add to index
    GitClient.addFile(testFileName)
    // commit
    GitClient.commit("testBranchIsAheadOfRemote test commit")
    def isAhead = GitClient.branchIsAheadOfRemote()
    assert isAhead, "Repo must be ahead of origin, but result is $isAhead"
    // delete commit
    GitClient.deleteLastCommit()
    GitClient.removeFromIndex(testFileName)
    testFile.delete()
  }

 /* can only be tested when pushing a change in another git client.
    void testRemoteIsAheadOfBranch(){
    log.debug("testBranchIsAheadOfRemote()")
    assert GitClient.remoteIsAheadOfBranch(GitClient.currentBranchName()), "Origin is ahead of branch. Please perform pull"
  }*/
}
