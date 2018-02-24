## Serien-Tracker

### Track your series.

#### ToDo-List

* Average time of a episode to get an amount of wasted time (Info-Table)
* Description of series for Info-Table
    * Program that changes old `Series.json` to a `.json` that has space for a description
* Sort series after percentage of completion
* Menubar on top to switch to movies (not implemented yet), or open the settings (not implemented)
    * Settings with path where to save the `.json`-file and maybe own background-pics
* Implement Series-API of TVDB to make everything easier and get most information automatically
* Check every period of time if there are new episodes/seasons of a series
    * Period changeable in Settings
    * Change interval of back-ups in settings  
* When a series is discontinued (TVDB) it shall switch the `userState` to discontinued too, when the user finished watching it.

#### Think-Abouts

* Server for Webapp (change whole programm to a Spring Webapp) and run it on server for access from everywhere on series
* BackUp of `.json` to Server, not just external drive (for me)
* Linux-Support regarding `Paths` etc.

#### Done!

* Add Series
* Edit Series
* Delete Series
* TableView for starting, watching and waiting
* Added "Started"-Button to switch status of Series to "watching"
* Added + and - buttons to increase or decrease the episode number (switches to next season episode 1 when highest episode reached of current season)
* 
* Added "All finished Series"-button to see a list of all series that got discontinued and that you watched all episodes of
* Added Information-button to get informations like number of episodes, completion rate etc.
* Added "Discontinued"-button to change a series with no more new episoded from "waiting" to "ended"
* Added "New Episodes"-Button to add a new season to a series that had no more new episodes to watch
* Added background pictures that change randomly when opening/going back to the MainMenu


* Function to get high contrast for labels regarding the background pic (not thaaat good on some pictures)
* BackUp every 24 hours for example on an external drive (Path at the moment not changeable)