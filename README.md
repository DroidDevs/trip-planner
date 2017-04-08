# Project  - *Name of App Here*

**Name of your app** App Description

## User Stories

The following **required** functionality is completed:

* [ ] User can log in using their Facebook account
* [ ] Display a home screen with list of trips
* [ ] Ability to create a trip
    * [ ] Trip will be nameable
    * [ ] Trip will have a start date
    * [ ] Trip can have multiple destinations, user will select them via location search from Google Map API. Destination will have geographical coordinates.
    * [ ] Trip destinations can be reordered via drag and drop
* [ ] Trip should be editable
    * [ ] Ability the change trip name/start date
    * [ ] Ability to add/remove destinations
    * [ ] Ability to change the destination(s) order 
    * [ ] Ability to change the duration in days per a single destination. Zero days are allowed.
* [ ] Trips should be selectable which would then show a trip details screen
    * [ ] Trip details would show all destinations, start times and durations, any other relevant info
    * [ ] Destination details page will be grouped by
      * [ ] Saved places
      * [ ] Food and drink
      * [ ] Things to do
      * [ ] Friends list
* [ ] App will generate place suggestions for each destination
    * [ ] Places will be generated from Google Places API 
      * [ ] Fooad and drink - places with type "bakery", "cafe", "restaurant" which are located near the target destination
      * [ ] Things to do - places with type "aquarium", "park", "spa", "stadium", "point_of_interest", etc. This could be separated into several groups(!)       
* [ ] App will generate friends list for each destination who live close to that location - Facebook API
* [ ] App will show "saved places" per each destination    
    * [ ] Ability to see a list of "saved places"
    * [ ] Ability to add/remove suggested place to/from "saved places" list.
* [ ] Adding/change/removing destination(s) to a trip and changing the trip start date should readjust total trip duration. I.e. end day of a trip and start/end dates per destinations will be calculated automatically based on trip start date and destination(s) order and duration. 
* [ ] Global map of the planned trip should be shown with a pinned destination(s)
* [ ] Detailed map per destination should be shown with pinned saved places and FB friends
* [ ] Global map with pinned all his friends who have specified their location in Facebook
* [ ] Trips should be persisted to database via Parse
    * [ ] Logging in should check Parse for any already created user information

The following **optional** features are implemented:
    
* [ ] Ability to archive a trip
* [ ] Ability to see a list of archived trips
* [ ] Destination detailed page will have additional group:
   * [ ] Friends recommendations - list of places which are liked by FB friends - Facebook API
* [ ] App will allow add current destination to a trip based on current user location - app will suggest places to check out when user is in a certain location
* [ ] User can share a whole trip with it's map to their Facebook wall
* [ ] User can share a separate trip's destination details with it's map to their Facebook wall
* [ ] "Saved places" per destination can have priorities (must see/go, like to, etc)
* [ ] Ability to save places such as restaurants, bar, coffee shop, for future stops (?)
* [ ] Ability to add friends from Facebook to the app 
* [ ] Ability to remove friends

The following **bonus** features are implemented:
* [ ] Upload trip photos to Facebook
* [ ] Friends can be sent requests to meet up at stops along a trip
    * [ ] Should also determine a meet up date & time
* [ ] Friends who have accepted a meet up should be shown real-time ETA within 1 hour of a meet up start time
* [ ] If a destination is removed, any relevant friends/meetups should be notified 

## Android Technical details
* [ ] All REST API calls have to be done in background thread(s)
* [ ] App should  run offline, persist all data in the SQLite database.
* [ ] App will use [MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp-contentproviders/) architecture.

# Activities list



# Wireframes

<img src="https://github.com/DroidDevs/trip-planner/blob/master/page1-3.png"  title="" />
<img src="https://github.com/DroidDevs/trip-planner/blob/master/pages%204-6.png"  title="" />
<img src="https://github.com/DroidDevs/trip-planner/blob/master/pages7-9.png"  title="" />



