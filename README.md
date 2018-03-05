# Serien-Tracker

## Track your series.

You can convert your old `.json` to the new `.json` here: https://github.com/leonfrisch/SeriesJSONtoNew

### ToDo-List
#### Code
* When performing a backup on Linux (external drive that is not mounted) it will obviously fail
    * Let the user enter a path (Settings (also see **FXML/Code**)), since otherwise you have to get the name of the drive :(
* AddController: Show all options that TVDB has, since the first is not always the right (HoC, Defenders etc.)
* Sorting for Tables (for now) as long as session goes
    * Store Settings and Sorting in file (later)
* Implement all functions of menu bar
* Show progress while updating in background
    
#### FXML/Code
* `Information` not just for `continue`-table, would be a waste of the collected data
    * Maybe one that says "Advanced Information" to also see description of episodes when you can not remember what happend (this one for all Series)
* Let User add or edit a series manually with the same options
    * **Check all given Data if they are valid**
* Label that shows that series that get found by TVDB will lose their edit when updating (in Edit Series)
* "How To" for Help Window
    * indication for colors of rows in the TableViews

#### Asthetics
* Change MenuBar Color to something dark greyish
* Show series in the tables by different color based on rating
    * if there are no ratings the rating is 0.0

### Think-Abouts
* Contrast Color for background better than just black and white
* Server for Webapp (change whole programm to a Spring Webapp) and run it on server for access from everywhere on series
* BackUp of `.json` to Server, not just external drive (for me)

### Done!
* Add Series
* Edit Series
* Delete Series
* TableView for starting, watching and waiting
* Added "Started"-Button to switch status of Series to "watching"
* Added + and - buttons to increase or decrease the episode number (switches to next season episode 1 when highest episode reached of current season)
* Added "All finished Series"-button to see a list of all series that got discontinued and that you watched all episodes of
* Added Information-button to get informations like number of episodes, completion rate etc.
* Added "Discontinued"-button to change a series with no more new episoded from "waiting" to "ended"
* Added "New Episodes"-Button to add a new season to a series that had no more new episodes to watch
* Added background pictures that change randomly when opening/going back to the MainMenu
* Function to get high contrast for labels regarding the background pic (not thaaat good on some pictures)
* Linux-Support regarding `Paths` etc.
* BackUp every 24 hours for example on an external drive (Path at the moment not changeable)
* Added average Time of an episode to get wasted time on a series
* Added Description of a series
* Implemented TVDB-API
* Program that changes old `Series.json` to a `.json`
* Get every page of episodes that a series has and add them all to the list
* Set `current` when translating old to new `Series.json`, otherwise set it on 1.1
* Order episodes regarding their Season and Episode
* Filter every season or episode that has 0/null in its epNumber/seasonNumber
* Sort series after percentage of completion
* Fixed `inc` and `dec`
* Add amount of wasted time (Info-Table)
* Description of series to Info-Table
* Added MenuBar
