hi, this is how i want to schedule the backend -- api things.

Gordon and Terry are going to go through this. Since we're all using the same framework on API implementation which is created by Terry, I think it won't be a problem to do the following:

First: at present, our api-functions are all embedded in the activities, which is ez to implement for several demo-app, but not a good idea for a intergrated app. 
Can u guys remove these codes and put them into seperate classes, so we can call these functions using 'import'? All these JAVA classes should be put in a folder called utilities. 
I can provide u an example in GitHub:
https://github.com/kigold/movie_land/tree/master/app/src/main/java/com/bignerdranch/android/movieland
See how they put the apis in utilities, and see how we gonna import these api-functions in MainActivities.

Second: use the sandbox api-key. For movieGlu, the api-key has limits, so test it using api-key.

Third: try to impelement a test example after finishing the above :) thus we have examples of how to connect api-functions into a view :)
