# AxosoftTeamCity
Axosoft Issue Tracking Plugin for TeamCity 9+

***
# Build
Follow instructions for building Plugins as described on Jetbrains' page [here] (https://confluence.jetbrains.com/display/TCD10/Getting+Started+with+Plugin+Development).

***
# Configuration
1.) Drag and drop axosoftPlugin.zip file into <TeamCity Data Directory>/Plugins
2.) Restart TeamCity service
3.) Generate non-expiring token in Axosoft (Tools/System Options/API Settings/Manage Tokens/Create non-expiring token)
4.) Create a connection to your issue tracker on the Project Settings > Issue Trackers page
5.) Enter in link to Axosoft account, and non-expiring token from step 3.

***
# Usage
Any changes tagged with the commit message syntax for Axosoft source control plugins as described in our documentation [here](https://support.axosoft.com/integrations/commits), will be available for view in TeamCity.

When viewing the change log, TeamCity will load the item name, workflow step, and if the issue is resolved.  It will also create a link back to any Axosoft items that can be clicked and view and edit the item directly in the Axosoft website.

When viewing the issue log, the plugin will display detailed information for all of the items in Axosoft that were referenced in the recent changes.
