# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. 
This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by [TheTvdb.com](https://www.thetvdb.com/), a database of series.

### Usage
##### Needed stuff 
* You need [Java 12](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html) for this program
* ~~(The `.jar`-File of the [SeriesTracker](https://github.com/Kraisie/SeriesTracker/releases))~~ **Not available for Java12 at the moment**
* And furthermore an account and an API-Key from [TheTvdb.com](https://www.thetvdb.com/member/api) 

##### Start
<details><summary>Old starting method with Java8</summary><p>

Start the `.jar` via Commandline, via double-click or via right-click "Open with...".
```cmd
cd path/to/jar
java -jar SeriesTracker-release-X.jar
```

</p>
</details>

* Clone or download and extract the repository
* Open a CMD/Terminal in the folder where the files are located
    * On Linux or Windows with gradle installed type: `gradle run`
    * On Linux without gradle installed type: `./gradlew run`
    * On Windows without gradle installed type: `gradlew.bat run`

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
    
### Why is there no jar to use?
At the moment there is a module called `java.base` that needs the `javafx.graphics` module on the start of the application.
That JavaFX module needs to be present by name, because if not the application will stop starting. 
Thus you can not use the JavaFX libraries as jars on the classpath as it is simply not allowed.
Furthermore it would result in lots of duplicate filenames etc if it would be.
It would be needed to install the JavaFX SDK on every system that wants to run this program or use alternatives that create Images (`jlink` is implemented in `build.gradle`) or bundle your other jars etc.
Another workaround may be to create a new `Main`-class which calls the `Main` that extends `Application` but I could not get that to work either.
Because of that there is currently no `.jar`-file you can use.

**TL;DR**: Problems of removing JavaFX from the JDK are not yet completely solved in a useful manner.