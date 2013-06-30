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



scm.validBranch - The valid branch name (s) for this build. [master]

scm.skipGitBranchCheck - If true, do not perform the git branch check [false]

scm.skipGitCommitCheck - If true, do not perform uncommitted changes check. [false]

scm.skipGitPushedCheck - If true, do not perform unpushed change sets check. [false]

scm.skipGitOriginCheck - If true, perform origin unpulled change sets check. [false]

scm.appendRevToWarName - If true, append the current git repository revision to the war file name. [false]



[value] denotes default values.



Git client used
---------------

This plugin uses JGit as a dependency and the native git client installed in your OS.
Due to the current lack of HTTP Basic Authentication support by ~/.netrc or
credential.helper in JGit, this plugin for now uses the native git client installed in your OS for all
remote operations.








