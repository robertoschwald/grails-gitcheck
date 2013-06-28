GitCheck Grails Plugin
======================

This plugin provides the ability to check git status of a local repository on
war file generation.

It can be used to ensure your changes are committed and pushed as well as your
project is current while generating a war file.



The following tests are performed:



-   Check for valid branch name

-   Check for uncommitted changes

-   Check for unpushed change sets

-   Check for unpulled origin change sets



Configuration Options
---------------------



scm.validBranch - The valid branch name (s) for this build [master]

scm.failOnGitBranchCheck - If true, do not fail if current branch is not the
configured validBranch [false]

scm.failOnGitCommitCheck - If true, d<o not fail if uncommitted changes are
detected [false]>

scm.failOnGitPushedCheck - If true, do not fail if unpushed change sets
detected. <[false]>

scm.failOnGitOriginCheck - If true, do not fail if origin unpulled change
sets detected. <[false]>



[value] denotes default values.



Git client used
---------------

Due to the current lack of HTTP Basic Authentication support by ~/.netrc or
<credential.helper> in JGit, this plugin for now uses the native git client installed in your OS.








