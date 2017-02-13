GitCheck Plugin for Grails 2.x
==============================

This plugin provides the ability to check git status of a local repository on
Grails 2.x war file generation.

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

scm.disablePlugin - If true, the plugin is disabled. [false] 

[value] denotes default values.

Git client used
---------------

This plugin uses JGit as a dependency and the native Git client installed in your OS.

Due to the current lack of HTTP Basic Authentication support using .netrc or
credential.helper authentication in JGit, this plugin for now uses the native Git client installed in your OS for all
remote operations to support Git, SSH and HTTP(s) protocols.

Please ensure your native Git client is available on the PATH environment variable.


If you need such a funtionality for Grails 3.x, use a Gradle Git plugin.

