# Series-Tracker

A program to keep track of all your series you are currently watching, you plan to watch in the future and all those you already finished. 
This program helps you to never watch an episode again because you forgot if you already saw it, or reminds you of the series you started, but never finished as you forgot that there was a new season.
All data is provided by [TheTvdb.com](https://www.thetvdb.com/), a database of TV series.

### Usage

##### Needed stuff

* You need [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) or higher
  for this program.
* The `.jar`-File of the [SeriesTracker](https://github.com/Kraisie/SeriesTracker/releases).
  * You can download the specific version for your platform (Linux, Mac, Windows) or the one that runs on all
    platforms (Master).
* And furthermore an account and an API-Key from [TheTvdb.com](https://www.thetvdb.com/).

##### Start

Start the `.jar` via Commandline, via double-click or via right-click "Open with...".

```
cd path/to/jar
java -jar SeriesTracker-release-X.jar
```

If you want to clone or download and extract the repository yourself you can start the program like this:

* Open the CMD/Terminal in the folder where the files are located.
  * On Linux or Windows with gradle installed type: `gradle run`.
  * On Linux without gradle installed type: `./gradlew run`.
  * On Windows without gradle installed type: `gradlew run`.

### How to use the tracker

##### How to get an API Key

* First [create an account](https://www.thetvdb.com/register) on theTVDB.com.
* Generate a new API Key [here](https://thetvdb.com/dashboard/account/apikey/create).
* Afterwards copy the `Username` (in the @ field) and `Unique User Key`
  from [here](https://thetvdb.com/dashboard/account/editinfo) and the `API Key`
  from [here](https://thetvdb.com/dashboard/account/apikey).
* On first start of the program provide that data to the three text fields.

##### Basic operations

* You can open the menu with a click on the hamburger icon at the top left
* To add a series open the menu and click on "Add a new series"
  * Type the name of the series in the text field and press enter/click on "Add series"
  * If there are multiple matches you may need to select the desired series in the following screen
* Search a series in your local series by opening the menu and clicking on "Search for a series"

##### Update

* "Update" > "Update Series" updates all series except the ones that are already discontinued as there won't be much
  change.
  * You can still update the discontinued series explicitly with "Update" > "Update ended Series".

##### Customization

* You can use custom backgrounds in the main menu. Just navigate to your Home folder, open the `SERIESTRACKER` folder
  and place your `png`, `jpg`, or `jpeg` in `/Backgrounds/`.

### Legal Notice

TV information and images are provided by [TheTvdb.com](https://www.thetvdb.com/), but I am not endorsed or certified
by [TheTvdb.com](https://www.thetvdb.com/) or its affiliates.