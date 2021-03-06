<details><summary>Older updates</summary><p>

* Add Series
* Edit Series
* Delete Series
* TableView for starting, watching and waiting
* Added "Started"-Button to switch status of Series to "watching"
* Added + and - buttons to increase or decrease the episode number (switches to next season episode 1 when highest episode reached of current season)
* Added "All finished Series"-button to see a list of all series that got discontinued and that you watched all episodes of
* Added Information-button to get information like number of episodes, completion rate etc.
* Added "Discontinued"-button to change a series with no more new episodes from "waiting" to "ended"
* Added "New Episodes"-Button to add a new season to a series that had no more new episodes to watch
* **FIRST KINDA RELEASE**
* Added background pictures that change randomly when opening/going back to the MainMenu
* Function to get high contrast for labels regarding the background pic (not that good on some pictures)
* Linux-Support regarding `Paths` etc.
* BackUp every 24 hours for example on an external drive (Path at the moment not changeable)
* Added average Time of an episode to get wasted time on a series
* Added Description of a series
* Implemented TVDB-API
* Program that changes old `Series.json` to a new `Series.json`
* Get every page of episodes that a series has and add them all to the list
* Set `current` when translating old to new `Series.json`, otherwise set it on 1.1
* Order episodes regarding their Season and Episode
* Filter every season or episode that has 0/null in its epNumber/seasonNumber
* Sort series after percentage of completion
* Fixed `inc` and `dec`
* Add amount of wasted time (Info-Table)
* Description of series to Info-Table
* Added MenuBar
* Add a series with TVDB
* Get to choose between 5 possible series of TVDB to add
* Added Advanced Information
* Added banner to Advance Information and Selection
* Added basic movie support
* Added support for waiting series when there are empty seasons (now require an air date to get back into continue table and if not given they get from watching to waiting)
* Ended series do not get updated, you have to update them explicitly now
* BackUp can now get imported
* Added search for series (in already added series)
* Added Settings
* **VERSION 1.0**
    * Added option to force a BackUp
    * Series that get continued after they already got ended before are now automatically added back to the continuing series
* **VERSION 1.1**
    * Removed old Information screen in favor of advanced information screen
    * Custom main menu background picture support
* **VERSION 1.1.2**
    * Checking air dates after an update
    * PopUp informing you about (a) newly aired episode/s you can watch
* **VERSION 1.2 - Bugfixes and Enhancements**
    * Handling series with no episodes as a faulty series of TVDB
    * Enhancement of the selection screen
    * Fixed crash when no background picture is available
    * Main menu now uses a fallback image if there are none in the folder
    * Series not available on TVDB are now not failing the update anymore
* **VERSION 2.0 - RECODE**
    * Tidied up the code
    * Removed movie support
    * Added Gradle support
    * Changed background size aspects to 16:9 to support more resolutions
    * Background image folder for easier selection of own background pictures
    * Added option to set API-Key
    * Added a better PopUp
    * Added option to sort by name or by completion
    * Added selecting series via button press in main menu and deletion screen
    * Information button now works for all tables again
* **VERSION 2.0.1 - Minor fixes and changes**
    * Updated some FXML files
    * Fixed Windows charset (CP1252 to UTF-8)
    * Fixed invalid keystrokes to scroll
    * Removed unnecessary logins to TVDB
* **VERSION 2.0.2 - Fixed Windows related issues and a few bugs**
    * Fixed small bug when searching for series
    * Removed double PopUp when APIKey was missing
    * Fixed [Windows resizeable window bug](https://bugs.openjdk.java.net/browse/JDK-8089008)
* **VERSION 2.1**
    * Improved stability and code quality
    * Added time related order option
    * Added `About` screen
* **VERSION 2.1.1**
    * Added icon to PopUps
    * Changed main menu sizes
    * Changed date related and search related behaviour
* **VERSION 2.1.2**
    * Upgraded graphics for every scene
    * Improved file management
* **Version 2.1.3**
    * Added option to select a preferred language for the TVDB results
    * Improved way to delete series
    * Improved searching options
    * Minor layout/graphics changes and improvements
    * Added JavaDoc
    * New scenes now open centered to the old one
    * Program requests API Key via form if not set on start
    * Added validity check for API Key
    * Improved behaviour and design on first start
    
</p>
</details>

* **VERSION 2.2**
    * Added Java12 support
    * Removed PopUp calls from non-UI classes
    * PopUps are now centered to the stage
    * Improved selection of a series to add
    * Improved layouts for DPI awareness
* **VERSION 2.2.1**
    * Made all scenes responsive to size changes
    * Moved "Waiting" table to new scene
    * Fixed problem with series that are not complete and do not provide a continuous list of episodes
    * Moved option to delete a series from scene to context menu
    * Improved duplicate check
    * Added option to restore default window size of the main scene
    * Added tooltips for unclear options/parameter