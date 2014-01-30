AudioReviser
============

An android app for revising material you've read. You take audio notes as you read, and every day you listen to previous notes you've taken. In keeping with the best advice for revision, the app plays first the notes from yesterday, then those from a week ago, and then those from a month ago. While listenening to your own voice you hear relaxing music in the background.

A demonstration of the first version of the app I did is [here](http://www.youtube.com/watch?v=6vWXp_yq9yA)

About the code
--------------
This is the second version and still a work in progress

 + I've switched to using fragments instead of two activities.
 + I've created an android app that handles all the UI elements, but all the business logic will be moved to its own Java class(s).
 + The android app calls functions on the businessLogicUnit when ever UI events happen, and then calls a function updateUI().
 + There is an Array of widgets, all widgets can be reffered to by constants, the businessLogicUnit changes these elements to update the UI.
 + This means the businessLogicUnit can be tested seperately like any other java code.
 + It also means the businessLogicUnit can be developed on a computer not powerful enough for the normal Android IDEs.
 + It also keeps with the convention of seperating code from content.
