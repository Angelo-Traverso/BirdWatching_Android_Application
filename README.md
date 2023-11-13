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

## References
- Al-Fanatsah, A. M. (2017). [android - i want to change the position of request focus error icon](https://stackoverflow.com/questions/46526260/android-i-want-to-change-the-position-of-request-focus-error-icon-so-that-it-does) - Stack Overflow.
- [ChatGPT](https://chat.openai.com).
- Mapbox. (2023). [Add a complete turn-by-turn experience](https://docs.mapbox.com/android/navigation/examples/turn-by-turn-experience/).
- murli (2012). [how to rotate a bitmap 90 degrees](https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees) - Stack Overflow.
