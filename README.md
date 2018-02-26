## Serien-Tracker

### Track your series.

You can convert your old `.json` to the new `.json` here: https://github.com/leonfrisch/SeriesJSONtoNew

#### ToDo-List

* Filter every season or episode that has 0/null in its epNumber/seasonNumber
* Get all episodes not just 10 episodes of every Season (but idk how --> Solution: episodes have pages)
    * pages: 
    `{
        "links": {
        "first": 1,
        "last": 2,
        "next": null,
        "prev": 1
    },[...]`
        * first: first page of all
        * last: last page (maybe iterate from 1 to last to get all)
        * next and prev are irrelevant
    * pages-link: https://api.thetvdb.com/series/<id>/episodes?page=<page>
    * --> Add all pages to List of episodes
* Maybe switch to another API that is not shit
* Set `current` when translating old to new `Series.json`, otherwise set it on 1.1
    * Should have the side-effect that `getCurrent` works to fill Tables
    
* FIX FUCKING EVERYTHING FXML

* Add amount of wasted time (Info-Table)
* Description of series to Info-Table
* Program that changes old `Series.json` to a `.json` that has space for a description
* Sort series after percentage of completion
* Menubar on top to switch to movies (not implemented yet), or open the settings (not implemented)
    * Settings with path where to save the `.json`-file and maybe own background-pics
* Check every period of time if there are new episodes/seasons of a series or an `Update All Series`-Button
    * Period changeable in Settings
    * Change interval of back-ups in settings  
* When a series is discontinued (TVDB) it shall switch the `userState` to discontinued too, when the user finished watching it.
* Let User add a series manually with the same options everywhere, fix the current ones
    * Check all given Data if they are valid
* Order episodes regarding their Season and Episode, since they are currently not and it would destroy `incEpisode` and `decEpisode`.

#### Think-Abouts

* Server for Webapp (change whole programm to a Spring Webapp) and run it on server for access from everywhere on series
* BackUp of `.json` to Server, not just external drive (for me)

#### Done!

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