# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by TheTvdb.com, a database of series.

### Installation
* You just need Java 8 or higher for this program: https://java.com/de/download/manual.jsp
* And the `.jar`-File of the SeriesTracker: https://github.com/Kraisie/SeriesTracker/releases

### Start
Start the `.jar` via Commandline, or via double-click / right-click "Open with"
```cmd
cd path/to/jar
java -jar SeriesTracker.jar
```

### How to use the tracker
##### Basic
* Click on "Edit" > "Add a new Series" and type in the series name. If you can choose between multiple series select the one that is your desired one. It should bring you back to the MainMenu and show that series in the right table
* To start watching a series select the series in the right table and click on the button "Started series". The series will now be displayed in the left table and show you which episode you are currently at (season 1, episode 1)
* After you have watched an episode you can select the series in the left table and click on the "+" button to increment the episode of the season. If you have misclicked you can also press the "-" button do decrease the episode you are currently at. If you saw the last episode of a season the season number will increase and the episode will be at 1 again.
* If you want to see how many seasons or episodes there are and how you close you are to the end, or even how much time you invested in watching the series just select the series in the left table and click on the "Information" button.
* When there are no new episodes anymore the series will now be displayed in the middle as long as there are no new episodes. they will get back into the left table when there are new ones
* When you finished the series and there won't be any new episodes the series will not be displayed anymore in those three tables. Instead it will just be shown when you click on the "All finished series" button
* If you do not like a series and do not want to have it in your tracker just click on "Edit" > "Delete a series" and select the on you want to delete.
* "Mode" > "Close", or the OS specific close button will close the program

##### Advanced
* Click on "Mode" > "Advanced Information" and select a series to display advanced information for your series
* Click on "Mode" > "Search Series" to search for a series in your series by some parameters.
    * Derivation means that if the duration you want to search for is 20 minutes and you select a derivation of 5 minutes the search will include series with a duration per episode of 15 to 25 minutes.
    * Userstates are symbolised by the tables of the main menu
    * State means if there will be new episodes / seasons or not (series is discontinued or not)
* If you have a BackUp of your data you can import it with "Edit" > "Import BackUp" > "Yes"
* "Update" > "Update Series" updates all series except the series that are already discontinued as there won't be much change
    * You can still update the discontinued series explicitly with "Update" > "Update ended Series"
* "Settings" > "Open Settings" will open the settings to choose the file locations and BackUp frequency 
* "Help" > "How to use this program" will bring you to this site

## Known Bugs
* SearchSeries/AddSeries starts black (Linux)
* CSS errors (`java.lang.String cannot be cast to javafx.scene.paint.Color' while converting value for '-fx-background-color' from rule '*.scroll-bar:vertical' in stylesheet jar:file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jfxrt.jar!/com/sun/javafx/scene/control/skin/modena/modena.bss`)

## Think-Abouts
* API for movies
* Remove movies as it gets far too chaotic when adding every single movie (even when it is me who does not watch a lot of movies)
    * Maybe just add Movies that you want to see in the future and delete them after you saw them (keep them in json?)
    * If movies stay implement settings for movies file location
* Custom Pictures in MainMenu
* Beautify some scenes
* Pictures for ReadMe / create wiki (https://help.github.com/articles/adding-images-to-wikis/)
