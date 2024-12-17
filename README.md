# AVIV
In this test assignment I have taken an MVI approach using the following libraries:
- Ktor for networking
- Room for database
- Koin for dependency injection
- Coroutines for async operations
- Coil for image loading
- JUnit and Mockito for testing

The app structure follows the schema recommended by Google and is divided into 3 main packages:
- data: Contains the repositories' implementation, the database, and network classes
- domain: Contains the use cases and the repositories interfaces
- presentation: Contains the view models and UI classes

The app has 2 screens:
- The main screen shows a list of flats
- The detail screen shows the details of a flat

Important note:
Even though the API response for the list of flats and the details of a flat is similar, 
I have created 2 different models for them. Usually the response for a list of items contains 
limited information compared to a more extended response of the whole details of an item. 
And having two separate sets of models makes the app ready for such an approach in the future. 