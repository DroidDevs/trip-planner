# Project  - *Name of App Here*

**Name of your app** App Description

## User Stories

The following **required** functionality is completed:

* [ ] User can log in using their Facebook account
* [ ] Display a home screen with list of upcoming trips
    * [ ] Also the ability to look at past trips
* [ ] Ability to create a trip
    * [ ] Trip will be nameable
    * [ ] Trip can have multiple destinations, use will select them via location search suggestions from Google Map API. Destination will have geographical coordinates.
    * [ ] Trip will have a start date
    * [ ] Trip destinations can be reordered via drag and drop
* [ ] Trip should be editable
    * [ ] Ability to add/remove destinations
    * [ ] Ability the change trip name/start date
    * [ ] Ability to change the destination(s) order 
    * [ ] Ability to change the duration in days per a single destination. Zero days are allowed.
* [ ] Trips should be selectable which would then show a trip details screen
    * [ ] Details would show all destinations, start times and durations, any other relevant info
* [ ] App will generate place suggestions for each destination
    * [ ] Places will be generated from Google Places API 
      * [ ] Fooad and drink - places with type "bar", "bakery", "cafe", "restaurant" which are located near teh target destination
      * [ ] Things to do - places with type "aquarium", "park", "spa", "stadium", "point_of_interest", etc      
    * [ ] Places will be generated from Facebook API
      * [ ] List of places liked by user's friends in FB  
* [ ] App will show "saved places" per each destination    
    * [ ] Ability to see a list of "saved places"
    * [ ] Ability to add/remove suggested place to/from "saved places" list.
* [ ] Stops can also be selected from a list of friends “Likes” in a particular destination 
    * [ ] This will be generated from the Facebook API
* [ ] Adding/change/removing destination(s) to a trip and changing the trip start date should readjust total trip duration. I.e. end day of a trip and start/end dates per destinations will be calculated automatically based on trip start date and destination(s) order and duration. 
* [ ] Global map of the planned trip should be shown with a pinned destination(s)
* [ ] Detailed map per destination should be shown with pinned saved places
* [ ] Trips should be persisted to database via Parse
    * [ ] Logging in should check Parse for any already created user information

The following **optional** features are implemented:

* [ ] "Saved places" per destination can have priorities (must see/go, like to, etc)
* [ ] Ability to save places such as restaurants, bar, coffee shop, for future stops (?)
* [ ] Ability to add friends from Facebook to the app 
* [ ] Ability to remove friends
* [ ] For each destination app will show a list of friends who live close to that location
* [ ] Friends can be sent requests to meet up at stops along a trip
    * [ ] Should also determine a meet up date & time
* [ ] Friends who have accepted a meet up should be shown real-time ETA within 1 hour of a meet up start time
* [ ] If a destination is removed, any relevant friends/meetups should be notified 
* [ ] Ability to see location of friends on a map
* [ ] User can share a trip and map to their Facebook wall

The following **bonus** features are implemented:

* [ ] Upload trip photos to Facebook 
* [ ] Suggest places to check out when user is in a certain location
* [ ] Use Messenger API to chat with friends via Facebook

## Android Technical details
* [ ] All REST API calls have to be done in background thread(s)
* [ ] App should  run offline, persist all data in the SQLite database.
* [ ] App will use [MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp-contentproviders/) architecture.

## Facebook REST API calls:

### How to connect a user to some location:
User object contains 2 fields where we can take his location:
* hometown - the person's hometown. The type of [Page](https://developers.facebook.com/docs/graph-api/reference/page/)
* location - the person's current location as entered by them on their profile. This field is not related to check-ins. The type of [Page](https://developers.facebook.com/docs/graph-api/reference/page/)

### General
* /{user-id} returns a [single user](https://developers.facebook.com/docs/graph-api/reference/user)
* /{user-id}/friendlists - an object which refers to a grouping of friends [Friend List](https://developers.facebook.com/docs/graph-api/reference/friend-list/)
* /{place-id} return a [Place](https://developers.facebook.com/docs/graph-api/reference/place/)
* /{page-id} returns a [Page](https://developers.facebook.com/docs/graph-api/reference/page/)
* /search?q={your-query}&type={object-type} user can search in the social graph for these types: user, page, place with center parameter (with latitude and longitude), etc. [Searching](https://developers.facebook.com/docs/graph-api/using-graph-api)
* /{user-id}/likes - list of pages this person has liked. [Likes](https://developers.facebook.com/docs/graph-api/reference/user/likes/). 

### Uploading Photos and Creating Photo Albums
Apps are able to publish and create new photo albums, and publish photos via the Graph API.
* /{user-id}/albums to create empty photo albums for people.
* /{user-id}/photos to add individual photos for people.
* /{album-id}/photos to add photos to an existing album for people or for Pages.

## Google Places REST API calls

## Google Maps REST API calls




