# Project  - *Name of App Here*

**Name of your app** App Description

## User Stories

The following **required** functionality is completed:

* [x] User can log in using their Facebook account
* [x] Display a home screen with list of trips
* [x] Ability to create a trip
    * [x] Trip will be nameable
    * [x] Trip will have a start date
    * [x] Trip can have multiple destinations, user will select them via location search from Google Map API. Destination will have geographical coordinates.
    * [ ] Trip destinations can be reordered via drag and drop
* [x] Trip should be editable
    * [x] Ability the change trip name/start date
    * [x] Ability to add/remove destinations
    * [ ] Ability to change the destination(s) order (drag and drop)
    * [x] Ability to change the duration in days per a single destination. Zero days are allowed.
* [x] Trips should be selectable which would then show a trip details screen
    * [x] Trip details would show all destinations, start times and durations, any other relevant info
    * [x] Destination details page will be grouped by
      * [x] Saved places
      * [x] Restaurant
      * [x] Cafe
* [ ] App will generate place suggestions for each destination
    * [x] Places will be generated from Google Places API 
      * [x] Restaurant - Food, Bar, etc
      * [ ] Things to do - places with type "aquarium", "park", "spa", "stadium", "point_of_interest", etc. This could be separated into several groups(!)       
* [x] App will show "saved places" per each destination    
    * [x] Ability to see a list of "saved places"
    * [x] Ability to add/remove suggested place to/from "saved places" list.
* [x] Adding/change/removing destination(s) to a trip and changing the trip start date should readjust total trip duration. I.e. end day of a trip and start/end dates per destinations will be calculated automatically based on trip start date and destination(s) order and duration. 
* [x] Global map of the planned trip should be shown with a pinned destination(s)
* [x] Detailed map per destination should be shown with pinned saved places
* [x] Trips should be persisted to database via Parse
    * [x] Logging in should check Parse for any already created user information

The following **optional** features are implemented:
* [ ] App will generate friends list for each destination who live close to that location - Facebook API
* [ ] Global map with pinned all his friends who have specified their location in Facebook
* [ ] Ability to archive a trip
* [x] Ability to see a list of archived trips
* [ ] Destination detailed page will have additional group:
   * [ ] Friends recommendations - list of places which are liked by FB friends - Facebook API
* [ ] App will allow add current destination to a trip based on current user location - app will suggest places to check out when user is in a certain location
* [ ] User can share a whole trip with it's map to their Facebook wall
* [ ] User can share a separate trip's destination details with it's map to their Facebook wall
* [ ] "Saved places" per destination can have priorities (must see/go, like to, etc)
* [ ] Ability to add friends from Facebook to the app 
* [ ] Ability to remove friends

The following **bonus** features are implemented:
* [ ] Upload trip photos to Facebook
* [ ] Friends can be sent requests to meet up at stops along a trip
    * [ ] Should also determine a meet up date & time
* [ ] Friends who have accepted a meet up should be shown real-time ETA within 1 hour of a meet up start time
* [ ] If a destination is removed, any relevant friends/meetups should be notified 

## Android Technical details
* [x] All REST API calls have to be done in background thread(s)
* [x] App should  run offline, persist all data in the SQLite database.
* [x] App will use [MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp-contentproviders/) architecture.

# Activities list

| Activity name | Fragment name | Comments | Developer | Status |
| ------------- | ------------- | ---------- | ---------- | ----- | 
| AddEditTrip | AddEditTrip | ability to add/edit trip | Elmira | Functionally Complete |
| Trips | Trips | list of open trips | Jared | Functionally Complete |
| FacebookLogin |  | login with facebook account | Jared | Functionally Complete |
| TripDetails| | map + destinations tabs | Elmira | Functionally Complete |
| ... | TripMap | a map of connected trip destinations | Elmira | Functionally Complete |
| ... | TripDestination | only trip start/edn dates + places group names | Jared | Functionally Complete |
| SavedPlaces |  | List with saved places: user can switch to list or map view | Elmira | Functionally Complete |
| ... | SavedPlacesList | list | Elmira | Functionally Complete |
| ... | SavedPlacesMap | map with saved places | Elmira | Functionally Complete |
| SuggestedPlaces |  | List with suggested places: user can switch to list or map view. Suggested places are egnerated at runtime from Google Places API | Jared | Functionally Complete |
| ... | SuggestedPlacesList |  | Jared | Functionally Complete |
| ... | SuggestedPlacesMap | map with saved places | Elmira | In Progress |
| PlaceDetails | PlaceDetails | details per place: all data we can take from Google Places | Jared | Functionally Complete |
| ... | PlaceDetailsMap | a map per a place | Elmira | In Progress |

|  |  |  |  |  |

# Wireframes

<img src="https://github.com/DroidDevs/trip-planner/blob/master/page1-3.png"  title="" />
<img src="https://github.com/DroidDevs/trip-planner/blob/master/pages%204-6.png"  title="" />
<img src="https://github.com/DroidDevs/trip-planner/blob/master/pages7-9.png"  title="" />



