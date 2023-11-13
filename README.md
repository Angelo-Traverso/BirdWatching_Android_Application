# OPSC7312 POE

## Group Members
- Jonathan Polakow (ST10081881)
- Angelo Traverso (ST10081927)

## GitHub Repository
[OPSC7312_POE_BirdWatching](https://github.com/Angelo-Traverso/OPSC7312_POE_BirdWatching)

## Device Requirements
The device must be able to use Google Play Store services. We recommend the basic Pixel 4 with Google Play running Android version 13 (Tiramisu) with API level 33.

## Files/Folders Included in the Submission
1. `README.pdf`
2. `Google_Play_Store_Preparations.pdf`
3. Video Walkthrough
4. Android Studio Application Folder
5. `Release_Notes.pdf`
6. `Part_1_Planning_and_Design.pdf`
7. Raw Images Folder

### File Descriptions
- `README.pdf`: This document.
- `Google_Play_Store_Preparations.pdf`: Outlines information for the Google Play Store page and preparation for publishing.
- Video Walkthrough: Demonstrates the mobile application.
- Android Studio Application Folder: Contains the application.
- `Release_Notes.pdf`: Lists all changes made between part two and part 3.
- `Part_1_Planning_and_Design.pdf`: Initial planning and design submission.
- Raw Images Folder: Images used in the Google Play Store preparations PDF.

## Important Information
- On the first launch after cloning the repository, sync your project with Gradle files, build the project, and then run.
- If pulling from GitHub, there might be Gradle sync issues. Sync up to three times if errors occur.
- Android Studio apps should be located in the folder created by Android Studio.
- Android Studio emulators may have incorrect dates; cold boot if facing challenges.
- The demonstration video uses navigation simulation; the final app will not.

## Default User Information
- **User 1:**
  - Email: aa@aa.aa
  - Password: Password123!
- New users can sign up on the sign-in page.

## Extra Features
- Voiceover for navigation.
- Imperial or metric units for voiceover.
- Map style customization.
- Navigation displays distance and time remaining.
- Duck hunt game.
- Daily challenges.
- Nearby hotspots show other Feather-Find users' observations.

## Cloud Services
- Firebase Authentication for login and signup.
- Firebase Firestore to store user settings and observations.
- eBird for retrieving hotspot and sightings data.

## Features Implemented from Feedback
- Navigate to past observations from the "My Observations" menu.
- Navigate to a sighting at a hotspot.
- Add an observation to a hotspot.

## Additional Information
- Ignore "E/SurfaceSyncer" error on game entry; it won't affect the app.
- Ignore "E/BufferQueueProducer" error on game exit; it won't affect the app.
- If SDK location not found error, copy "local.properties" from another project.
- Google Maps and MapBox are used for different strengths in the app.


## Release Notes
# Version 1.1.2 (Release - 14 November 2023)

## Summary

After a thorough reassessment of our initial submission, it became apparent that certain key features were lacking, particularly in terms of usability. Following extensive hours dedicated to this latest release, we are pleased to unveil a comprehensively enhanced birdwatching application that reflects our commitment to delivering a more robust and user-friendly experience.

## New Features

### User Observations
- Users of Feather-Find can now find bird observations made by other users.

### Add Sighting
- Users now have the ability to click on a nearby hotspot and add a bird sighting for that location. Bird observations made at a nearby hotspot will be added to that hotspot’s sightings.

### My Observation Navigation
- Users can now click on one of their observations, which will then begin navigation to that bird species.

### Bottom Sheet Bird Navigation
- Users can now click on a bird in a nearby hotspot, which will begin navigation to that species location.

### Authentication Service
- Users are now authenticated upon login, and their email and password are being authenticated using Firebase's built-in authentication system.

## Enhancements

### Data Storage
- Data is now stored in a hosted cloud platform, so all data is persisted and can be accessed at any time, anywhere, as long as users have an internet connection.

### Bottom Sheet
- Users can now easily see and use both the navigate and add sighting functions in the bottom sheet displayed when a user clicks on a nearby hotspot or user location.

## Bug Fixes

### Scroll View not allowing users to scroll up
- There was a known issue of users not being able to scroll "up" in the bottom sheet. This has been fixed, and users can now use it as intended without any bugs.

## Changes

### Online Hosted Database
- Data is no longer being used session-by-session. Data is now rather being stored in an online hosted database - Firebase.

## Known Issue

Once the menu button is clicked in hotspots, users have to click the button again to close it; you are not able to open it and click away from it, it will stay open.

## Security Updates

Users are now authenticated upon login, using Firebase authentication.

## Contributors

Development and Documentation were concurrently completed by both Angelo Traverso (ST10081927) and Jonathan Polakow (ST10081881).
