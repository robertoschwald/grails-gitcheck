package grails.plugin.gitcheck.GitClientUnitTests

import grails.plugin.gitcheck.GitClient
import grails.test.GrailsUnitTestCase
import static grails.test.MockUtils.*

/**
 *
 * User: roos
 * Date: 01.07.13
 * Time: 09:38
 *
 */
class GitClientUnitTests extends GrailsUnitTestCase {

  protected void setUp() {
    super.setUp()
    mockLogging(this.class, true)
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
    def testFileName = "testBrancAheadOfRemote.testfile"
    File testFile = new File(testFileName)
    if (!testFile.exists()) testFile.createNewFile()
    def isAhead = GitClient.branchIsAheadOfRemote()
    assert isAhead, 'Repo must be ahead of origin, but result is $isAhead'
    testFile.delete()
  }

  void testRemoteIsAheadOfBranch(){
    log.debug("testBranchIsAheadOfRemote()")
    assert GitClient.remoteIsAheadOfBranch(GitClient.currentBranchName()), "Origin is ahead of branch. Please perform pull"
  }
}
