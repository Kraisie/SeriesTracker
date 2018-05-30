# Series-Tracker

## ToDo-List
* Implement all functions of menu bar
    * Settings (get stored in file)
    * Sorting
        * Path for BackUp and cycle in that BackUps gets created 
    * Help
        * "How To"/Tutorial
            * Description of all Functions
            * Indication for colors of rows in the TableViews
* Show progress while updating in background (runnable, but can't update UI out of it)
* Add queries to search series that fulfill a specific dataset
    * Duration, Rating and number of Seasons
        * Duration: Set a number and adjust offset -> e.g 20 minutes duration +- 5 minutes results in all series that have a duration of 15 to 25 minutes per episode
        * Rating: Search for series with a rating above X. Option to ignore series with lower than Y Votes
            * Include SiteRaitingCount to Object MySeries
        * Number of seasons: selfexplaining

## Think-Abouts
* Contrast Color for background better than just black and white
* API for movies
* Remove manual series completely (Add)