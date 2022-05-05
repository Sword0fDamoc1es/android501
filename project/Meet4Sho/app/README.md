# meet4sho Package

-   **AppAcitivity.java**: The main activity where all the other fragments are contained within
-   **ConversationsAdapter.java**: A Recyclerview to display all the conversations/chat-rooms that the user has
-   **EventInfoFragment.java**: This class displays all the info pertaining to a specific event/movie when a user clicks on it from the RecyclerView in the SearchFragment class
-   **Firebase_playground.java**: Firebase playground that allows for user creation and manipulation
-   **loginActivity.java**: This class deals with the login page
-   **MessageChatFragment.java**: Fragment the displays a chat-room for the user and from where the user can message other people
-   **MessagesListFragment.java**: Fragment that displays all the conversations/chat-rooms that a user is in
-   **MG_RecyclerAdapter.java**: RecyclerView that displays all the information for every MGCinema Object that we constructed from the MGRequest class (MovieGlu api call)
-   **ProfileFragment.java**: Fragment displays all the info pertaining to specific user
-   **ProfileOtherFragment.java**: Fragment displays all the info pertaining to another user
-   **RestaurantInfoFragment.java**: This class displays all the info pertaining to a specific restuarant when a user clicks on it from the RecyclerView in the RestaurantsFragment class
-   **RestaurantRecyclerAdapter.java**: RecyclerView that displays all the information for every Restaurant Object that we constructed from the YelpRequest class (Yelp api call)
-   **RestaurantsFragment.java**: Fragment from where the user can search for restaurants that are near a specific event
-   **SearchFragment.java**: Fragment from where the user can search for events/movies
-   **SettingsFragment.java**: Fragment that allows user to save their email and phone, and change their password
-   **signupActivity.java**: This activity is where the user can create an account to login with
-   **TM_EventInfoActivity.java**: Displays the info pertaining to specific event/movie
-   **TM_RecyclerAdpater.java**: RecyclerView that displays all the information for every TMEvent Object that we constructed from the TMRequest class (Ticketmaster api call)
-   **UserRecyclerAdapter.java**: RecyclerView that displays all the user's that are interested in an event
-   **UserSearchFragment.java**: Fragment that displays all the user's that are interested in an event/movie via UserRecyclerAdapter's RecyclerView

# api Package

-   **ApiEvent.java**: parent class for YelpEvent, MGEvent, TMEvent
-   **HttpUtils.java**: Utils class that contains methods for sending Http GET requests
-   **JSONUtil.java**: Utils Class that contains methods for getting JSON elements and catching JSON exceptions
-   **MGCinema.java**: class for MovieGlu cinemas
-   **MGRequest.java**: class for calling MovieGlu API
-   **MGTime.java**: class that keeps showing times for MovieGlu showings
-   **RequestListener.java**: interface that listens for data from API calls and updates views in activities / fragments
-   **SearchFilter.java**: class that stores API query parameters and their corresponding values
-   **TMEvent.java**: class of TicketMaster Events
-   **TMEventClassification.java**: class of TicketMaster event classifications
-   **TMEventImage.java**: class of TicketMaster event images
-   **TMEventSales.java**: class of TicketMaster event sales info
-   **TMEventTime.java**: class of TicketMaster event times info
-   **TMEventVenue.java**: class of TicketMaster event venue info
-   **TMRequest.java**: class that handles TicketMaster API calling
-   **YelpRequest.java**: class that handles Yelp API calling
-   **YelpRestaurant.java**: class that keeps Yelp restaurant info

# data Package
-   **LoginDataSource**: Class that handles authentication w/ login credentials and retrieves user information.
-   **LoginRepository**: Class that requests authentication and user information from the remote data source and maintains an in-memory cache of login status and user credentials information.
-   **Result**: A generic class that holds a result success w/ data or an error exception.

## model Sub-package
-	**LoggedInUser**: Data class that captures user information for logged in users retrieved from LoginRepository

# messages Package
-   **MessageWrapper**: MessageWrapper that is required for CometChat to work
-   **UserWrapper**: UserWrapper that is required for CometChat to work

