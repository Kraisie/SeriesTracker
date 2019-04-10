# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. 
This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by [TheTvdb.com](https://www.thetvdb.com/), a database of series.

### Usage
##### Needed stuff 
* You need [Java 8](https://java.com/de/download/manual.jsp) for this program
* The `.jar`-File of the [SeriesTracker](https://github.com/Kraisie/SeriesTracker/releases)
* And furthermore an account and an API-Key from [TheTvdb.com](https://www.thetvdb.com/member/api) 

##### Start
Start the `.jar` via Commandline, via double-click or via right-click "Open with...".
```cmd
cd path/to/jar
java -jar SeriesTracker-release-X.jar
```

### How to use the tracker

##### How to get an API Key
* First [create an account](https://www.thetvdb.com/register) on theTVDB.com
* Now navigate to the [API access page](https://www.thetvdb.com/member/api)
* Generate a new Key; you can leave the fields blank
* Copy the `Username`, `Unique ID` and the `API Key` on that page
* On first start of the program provide that data to the three text fields

##### Basic operations
* Click on "Edit" > "Add a new Series" and type in the name of the series. If you can choose between multiple series select the one that is your desired one
* To start watching a series select the series in the right table and click on the button "Started series"
* You can select the series in the left table and click on the "+" button to increment the episode of the season; decrease with the "-" button
* Select a series and click on the "Information" button to get some information about that series
* You can find all finished series in a list when clicking on the button titled "All finished series"
* To delete a series click on "Edit" > "Delete a series" and select the one you want to delete
* Instead of scrolling through a long list of series you can focus a table and press a key on your keyboard to directly select the first series with that initial letter
* Click on "Mode" > "Search Series" to search for a series in your data by some parameters.
    * Userstates are symbolised by the tables of the main menu
* "Settings" > "Open Settings" will open the settings to choose the file locations, BackUp frequency and to change your API-Key

##### BackUps
* You can always create a BackUp of your data with "Edit" > "Create BackUp" > "Yes"
* If you have a BackUp of your data you can import it with "Edit" > "Import BackUp" > "Yes"

##### Update
* "Update" > "Update Series" updates all series except the ones that are already discontinued as there won't be much change
    * You can still update the discontinued series explicitly with "Update" > "Update ended Series"

##### Customization
* You can use custom backgrounds in the main menu. Just navigate to your Home folder, open the `SERIESTRACKER` folder and place your `png`, `jpg`, or `jpeg` in `/Backgrounds/`. 
    * The minimum size is 1280x720px due to the fixed window size. Larger sizes work too as long as they have an aspect ratio of 16:9

## Bugs with no solution yet
* SearchSeries/AddSeries starts black sometimes (Linux)
* `Gtk-Message: Failed to load module "pantheon-filechooser-module"` (Linux/pantheon)
* NPE when switching scenes on Linux caused by the menu bar and gtk (Main menu -> Information)
    * only removing the menu bar fixes it at the moment
    
## Why is there no Java 11 support?
Unfortunately there are a lot of problems regarding Java 11 and OpenJFX. [Creating a .jar-file via gradle](http://openjdk.java.net/projects/jigsaw/spec/issues/#MultiModuleExecutableJARs), bugged windows (like on a Raspberry Pi), [gtk3-errors](https://bugs.openjdk.java.net/browse/JDK-8215104) and [warnings](https://bugs.openjdk.java.net/browse/JDK-8211305) and so on and so on. 
Because of those problems there is currently no point in supporting Java 11 which would be needed to fix the menu bar bug. 
Java 9 would also fix that bug but as a non LTS version it is already deprecated. Currently it seems that OpenJFX 12 fixes some of those problems so later this program may support Java 12.
Until then only Java 8 is supported at a loss of a correctly working menu bar on a second screen on Windows.