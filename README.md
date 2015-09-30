# BeakerRubyMinePlugin
An IntelliJ (RubyMine or Idea) Plugin for executing Beaker Tests against PuppetLabs.

# Installation
Install from https://plugins.jetbrains.com/
In your IntelliJ IDE:
 - Select Preferences 
 - Click Browse repositories button
 - Select 'Beaker Test Runner' and install
 - Restart your IDE

# Features
 - Adds a new Beaker configuration type with simple support for some of the common parameters
 - Ability to run/debug Beaker tests from context (right click on a beaker test and either run or debug)
 - Ability to re-use the most recent saved hosts.yml for a configuration file
 - Ability to run/debug Beaker with or without Bundler
 - Ability to specify any additional command line arguments not included in the base configuration

# Future capabilities
 - Additional code completion support for Beaker classes
 - Code generation - create new Beaker test case (possibly different templates)

# Configuration
 - Set the default values for the Beaker configuration to match what you would typically run on command line
 - If your workflow generally consists of running a command to have Beaker spin up a node(s) and then executing tests against that existing node as a separate command, you can create a specific configuration(s) to spin up the nodes, and then use the default configuration to execute tests.

# Usage
After configuring the default Beaker Configuration, right click on a file in the Project window and select run or debug to execute it as a Beaker test.
