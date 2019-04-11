The app uses a Orianna a Riot API adaptaion for Java,
It uses an inputted player(summoner) name in order to retrieve data from the Riot servers. After retrieving the data the app then displays
it to the user in the following activity in form of three different tabs. The summoner tab uses an async task to retrieve an image from the server 
and display it in the imageview present on the page. The information is retrieved via two dataseeker classes which run on separate threads.
The StatTrack activity is a tab activity which uses fragments to display the different sections. The last section contains the map and relevant UI 
controls such as the text views. Upon clicking on the markers present on the map the user can choose to calculate the distance between the user location
and the server location. There is database and service implemented in the background along with notifications. Every 10 minutes the service clears the 
database with the relevant table name and displays a notification that the same user can now be searched for (refreshed) again.