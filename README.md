# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by [TheTvdb.com](https://www.thetvdb.com/), a database of series.

### Installation
* You need [Java 8](https://java.com/de/download/manual.jsp) for this program
* The `.jar`-File of the [SeriesTracker](https://github.com/Kraisie/SeriesTracker/releases)
* And furthermore an account and an API-Key from [TheTvdb.com](https://www.thetvdb.com/member/api) 

### Start
Start the `.jar` via Commandline, via double-click or via right-click "Open with...".
```cmd
cd path/to/jar
java -jar SeriesTracker-release-X.jar
```

### How to use the tracker
##### Basic
* After the first start please open the setting (HowTo below) and insert your API-Key as you won't be able to use this program otherwise
* Click on "Edit" > "Add a new Series" and type in the name of the series. If you can choose between multiple series select the one that is your desired one. It should bring you back to the MainMenu and show that series in the right table
* To start watching a series select the series in the right table and click on the button "Started series". The series will now be displayed in the left table and has the information which episode you are currently at (season 1, episode 1)
* After you've watched an episode you can select the series in the left table and click on the "+" button to increment the episode of the season. If you misclicked you can also press the "-" button to decrease the episode. If you saw the last episode of a season the season will increase and the episode will start at 1 again
* If you want to see how many seasons or episodes there are and how close you are to the end, or even how much time you invested in watching the series just select a series and click on the "Information" button.
* When there are no new episodes anymore the series will now be displayed in the middle as long as there are no new episodes. They will get back into the left table when there are new ones. If a series should get discontinued they will get displayed in the table of all finished series
* When you finished the series and there won't be any new episodes the series will not be displayed anymore in those three tables. Instead it will just be shown when you click on the "All finished series" button
* You can find all finished series in a list when clicking on the button titled "All finished series"
* If you do not like a series and do not want to have it in your tracker just click on "Edit" > "Delete a series" and select the one you want to delete
* Instead of scrolling through a long list of series you can focus a table and press a key on your keyboard to directly select the first series with that initial letter. On a second keypress it will continue to scroll down the list with those series that got the same initial letter. If you click it again on the last one with that letter it will go back to the first hit
* "Mode" > "Close", or the OS specific close button will close the program

##### Advanced
* Click on "Mode" > "Sort by ..." and select `Name` to sort by name. Select `Completion rate` to sort by the amount episodes needed to finish the series. This only takes effect in the main menu and only for the `Continue watching` table as the other tables are at either 0% or 100%. Select `Less needed Time` to sort by the amount of time needed to finish the series. This one does affect `Continue watching` and `Start watching`. When you order by `Completion rate` the series with the highest completion percentage gets shown at the top of the table. `Less needed Time` also shows the series with the least needed time at the top of the table.
* Click on "Mode" > "Search Series" to search for a series in your data by some parameters.
    * Derivation means that if the duration you want to search for is 20 minutes and you select a derivation of 5 minutes the search will include series with a duration per episode of 15 to 25 minutes.
    * Userstates are symbolised by the tables of the main menu
    * State means if there will be new episodes / seasons or not (series is discontinued or not)
* You can always create a BackUp of your data with "Edit" > "Create BackUp" > "Yes"
* If you have a BackUp of your data you can import it with "Edit" > "Import BackUp" > "Yes"
* "Update" > "Update Series" updates all series except the ones that are already discontinued as there won't be much change
    * You can still update the discontinued series explicitly with "Update" > "Update ended Series"
* "Settings" > "Open Settings" will open the settings to choose the file locations, BackUp frequency and to set your API-Key
* "Help" > "How to use this program" will bring you to this site
* You can use custom backgrounds in the main menu. Just navigate to your Home folder, open the `SERIESTRACKER` folder and place your `png`, `jpg`, or `jpeg` in `/Backgrounds/`. The minimum size is 1124x632px due to the fixed window size. Larger sizes work too as long as they have an aspect ratio of 16:9

## Think-Abouts
* Beautify some scenes
* Center new scenes in the middle of the main menu 
* Change the numeric value of the `userState` to something more obvious
    * That will require support for a change of the json file

## Bugs with no solution yet
* SearchSeries/AddSeries starts black sometimes (Linux)
* `Gtk-Message: Failed to load module "pantheon-filechooser-module"` (Linux/pantheon)
* NPE when switching scenes on Linux caused by the menu bar and gtk (Main menu -> Information)
    * only removing the menu bar fixes it at the moment
    
## Why is there no Java 11 support?
Unfortunately there are a lot of problems regarding Java 11 and OpenJFX. [Creating a .jar-file via gradle](http://openjdk.java.net/projects/jigsaw/spec/issues/#MultiModuleExecutableJARs), bugged windows (like on a Raspberry Pi), [gtk3-errors](https://bugs.openjdk.java.net/browse/JDK-8215104) and [warnings](https://bugs.openjdk.java.net/browse/JDK-8211305) and so on and so on. Because of those problems there is currently no point in supporting Java 11 which would be needed to fix the menu bar bug. Java 9 would also fix that bug but as a non LTS version it is already deprecated. Currently it seems that OpenJFX 12 fixes some of those problems so later this program may support Java 12.
Until then only Java 8 is supported at a loss of a correctly working menu bar on a second screen on Windows.