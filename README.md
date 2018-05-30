# Series-Tracker

### ToDo-List
#### Code
* Implement all functions of menu bar
    * Settings (get stored in file)
    * Sorting
        * Path for BackUp and cycle in that BackUps gets created 
    * Help
        * "How To"/Tutorial
            * Description of all Functions
            * Indication for colors of rows in the TableViews
* Show progress while updating in background (runnable, but can't update UI out of it)
* AirDate to only change UserState when AirDate is before or equal to current Date
* Add queries to search series that fulfill a specific dataset
    * Duration, Rating and number of Seasons
        * Duration: Set a number and adjust offset -> e.g 20 minutes duration +- 5 minutes results in all series that have a duration of 15 to 25 minutes per episode
        * Rating: Search for series with a rating above X. Option to ignore series with lower than Y Votes
            * Include SiteRaitingCount to Object MySeries
        * Number of seasons: selfexplaining

#### Asthetics
* Show series in the tables by different color based on rating or status
    * if there are no ratings the rating is 0.0

### Think-Abouts
* Contrast Color for background better than just black and white
* API for movies
* Remove manual series completely (Add)

### Done!
* Add Series
* Edit Series
* Delete Series
* TableView for starting, watching and waiting
* Added "Started"-Button to switch status of Series to "watching"
* Added + and - buttons to increase or decrease the episode number (switches to next season episode 1 when highest episode reached of current season)
* Added "All finished Series"-button to see a list of all series that got discontinued and that you watched all episodes of
* Added Information-button to get informations like number of episodes, completion rate etc.
* Added "Discontinued"-button to change a series with no more new episodes from "waiting" to "ended"
* Added "New Episodes"-Button to add a new season to a series that had no more new episodes to watch
* **FIRST KINDA RELEASE**
* Added background pictures that change randomly when opening/going back to the MainMenu
* Function to get high contrast for labels regarding the background pic (not thaaat good on some pictures)
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