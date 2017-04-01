# Project  - *Name of App Here*

**Name of your app** is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. 
The app utilizes:
 - [Facebook REST API](https://developers.facebook.com/docs/graph-api)
 - [Google Places API](https://developers.google.com/places/)
 - [Google Maps API](https://developers.google.com/maps)

Time spent: **X** hours spent in total

## User Stories

The following **required** functionality is completed:

* [ ] User can login with his Facebook account and is authorized to do any REST API calls
* [ ] User can login with his Google account and is authorized to do any REST API calls

The following **optional** features are implemented:

* [ ] Test

The following **bonus** features are implemented:

* [ ] Test

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

### Uploading Photos and Creating Photo Albums
Apps are able to publish and create new photo albums, and publish photos via the Graph API.
* /{user-id}/albums to create empty photo albums for people.
* /{user-id}/photos to add individual photos for people.
* /{album-id}/photos to add photos to an existing album for people or for Pages.

## Google Places REST API calls


## Google Maps REST API calls




