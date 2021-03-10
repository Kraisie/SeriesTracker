# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. 
This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by [TheTvdb.com](https://www.thetvdb.com/), a database of TV series.

### Usage
##### Needed stuff 
* You need [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) for this program
* The `.jar`-File of the [SeriesTracker](https://github.com/Kraisie/SeriesTracker/releases)
* And furthermore an account and an API-Key from [TheTvdb.com](https://www.thetvdb.com/member/api) 

##### Start
Start the `.jar` via Commandline, via double-click or via right-click "Open with...".
```cmd
cd path/to/jar
java -jar SeriesTracker-release-X.jar
```
If you want to clone or download and extract the repository yourself you can start the program like this:
* Open a CMD/Terminal in the folder where the files are located
    * On Linux or Windows with gradle installed type: `gradle run`
    * On Linux without gradle installed type: `./gradlew run`
    * On Windows without gradle installed type: `gradlew run`

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
* You can find all awaited series in a list when clicking on the button titled "All awaited series"
* You can find all finished series in a list when clicking on the button titled "All finished series"
* Right click on a series and click on "Information..." to get some information about that series
* To delete a series right-click on a series in a tables and click "Delete"
* Instead of scrolling through a long list of series you can focus a table and press a key on your keyboard to directly select the first series with that initial letter
* Click on "Mode" > "Search Series" to search for a series in your data by some parameters
* Click on "Mode" > "Restore default resolution" to set the size of the window back to default
* "Settings" > "Open Settings" will open the settings to choose the file locations, BackUp frequency and to change your API-Key

##### BackUps
* You can always create a BackUp of your data with "Edit" > "Create BackUp" > "Yes"
* If you have a BackUp of your data you can import it with "Edit" > "Import BackUp" > "Yes"

##### Update
* "Update" > "Update Series" updates all series except the ones that are already discontinued as there won't be much change
    * You can still update the discontinued series explicitly with "Update" > "Update ended Series"

##### Customization
* You can use custom backgrounds in the main menu. Just navigate to your Home folder, open the `SERIESTRACKER` folder and place your `png`, `jpg`, or `jpeg` in `/Backgrounds/`.