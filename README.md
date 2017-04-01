# Project  - *Name of App Here*

**Name of your app** is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. 
The app utilizes:
 - [Facebook REST API](https://developers.facebook.com/docs/graph-api)
 - [Google Maps API] (https://developers.google.com/maps)
 - [Google Places API] (https://developers.google.com/places/)

Time spent: **X** hours spent in total

## User Stories

The following **required** functionality is completed:

* [ ] User can login with his Facebook account and is authorized to do any REST API calls
* [ ] User can login with his Google account and is authorized to do any REST API calls

The following **optional** features are implemented:

* [ ] Test

The following **bonus** features are implemented:

* [ ] Test


## Facebook REST API calls:

* /{user-id} returns a single user. https://developers.facebook.com/docs/graph-api/reference/user
* Friend List (https://developers.facebook.com/docs/graph-api/reference/friend-list/) - an object which refers to a grouping of friends 
* Place (https://developers.facebook.com/docs/graph-api/reference/place/)
* Page (https://developers.facebook.com/docs/graph-api/reference/page/)
* Searching (https://developers.facebook.com/docs/graph-api/using-graph-api) user can search in the social graph with the /search endpoint. Search types are: user, page, place with center parameter (with latitude and longitude),

# Facebook Events API Apps can respond to events on behalf of people.
* /{event-id}/attending to say the person is attending.
* /{event-id}/maybe to say the person might attend.
* /{event-id}/declined to say the person has declined to attend.

# Uploading Photos and Creating Photo Albums
Apps are able to publish and create new photo albums, and publish photos or videos via the Graph API on behalf of people or Facebook Pages.
/{user-id}/albums to create empty photo albums for people.
/{user-id}/photos to add individual photos for people.
/{album-id}/photos to add photos to an existing album for people or for Pages.


## Google Places REST API calls


## Google Maps REST API calls




