# Project 5 - Group 004-1
CS 18000 | Option Three | Due: December 7th, 2020

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#overview">Overview</a></li>
    <li><a href="#classes-and-testing">Classes and Testing</a></li>
    <li><a href="#contributors">Contributors</a></li>
    <li><a href="#acknowledgements-and-references">Acknowledgements and References</a></li>
  </ol>
</details>

## Overview 

We utilized Java Swing, ServerSocket, threading, and more to create a basic messaging application. The messaging application allows for creation of direct and group messages. To begin execution of the program, a user must run the `MessageServer.java` class. Then, the user can run the `HomePage.java` class to begin account creation. Once the `MessageServer.java` class is run, it does not have to be run again since the application works solely on localhost (unless the user wants a new instance of the message application). Each user that desires to create an account must begin by running the `HomePage.java` class. We have a `Test` class that can be run to start the program.

## Classes and Testing
A description for the testing and implementation of classes is below, in order of appearance in the src folder. All testing was done using JUnit, with reference to the `RunLocalTest` files from coursework.

#### AddChatPage.java
- **Description**: This Java class creates a GUI page that allows users to create new chats. On this page, the user is able to see all other users that have accounts on the application (on the left scroll pane). Then, the user can select users that he/she wants added into a conversation with (visible on the right scroll pane). 
- **Testing**: By default, the `AddChatPage.java` class tests if 1) A user creates a chat that he/she already has a chat with and 2) if a user clicks on the 'submit' button without adding users. Both errors are handled with JOptionPane messages. In our JUnit test file, `AddChatPageTest.java`, we test `AddChatPage.java` by creating instances of the class, passing through test arguments into the constructor. Additionally, we test the methods that appear in `AddChatPage.java`. If a test were to fail, a print statement appears in console. 

#### Chat.java
- **Description**: This Java class creates individual chat objects. Each chat object consists of a `name` (if a chat is a group chat), an `id` (a unique identifier that consists of the usernames of the first two users (both users in the case of a direct message) and a random integer from 0 to 1,000,000), a boolean `groupchat` that determines whether or not a chat is a group chat, an ArrayList `messages` that holds all the messages in the chat, and an ArrayList `users` that holds all the user objects in the chat.
- **Testing**: The Chat class constructor creates instances of a Chat. Therefore, the Chat class contains accessors and mutator methods for each field in an instance of a Chat. The JUnit testing for the Chat class consisted of a static class, similar to the public test cases received throughout the course. This static class contains methods that test every field in the Chat class, as well as the accessors and mutators for each class. Each test is inside a try-catch block, so if any errors with test input were to arise, they would be caught, and the output of running the test class would detail what test failed, along with where in the test class the failure occurred. For the test cases that purposely failed with invalid input, we assigned the wrong data types to certain accessors, and passed in invalid data types to mutator methods. Upon testing, each invalid test case failed, which lets us know that the Chat class is working as intended. 

#### ChatsPage.java 
- **Description**: This Java class is the "home page" for users. It consists of a JScrollPane of all the chats in which a user is a participant of, as well as buttons that allow for adding chats, deleting accounts, signing out, refreshing the chat page, editing account information, and switching the UI theme to dark.A `Conversation` page is opened once a chat is clicked on. A chat can be deleted by right-clicking on the chat.
- **Testing**: The ChatsPage class contains a mix of majority GUI components, as well as some accessor and mutator methods for fields within an instance of a ChatsPage. Testing for the accessor and mutator methods is similar as the aforementioned classes. JUnit tests were run for both valid and invalid input, to test the accessor and mutator methods. Additionally, the ChatsPage class has a method that populates the JScrollPane with a JList of Chat objects. This is how the users are able to see the Chats on the screen. To test this, we passed example JLists and ran the program to see the changes on the screen. For the GUI, we exhausted all possible user inputs for each possible text field, and made sure that there was nothing that the user could input that would "break" the program. Additionally, we ensured that adding chats on the GUI side of the application updated on the backend, and were stored properly. 

#### ConversationPage.java
- **Description**: This Java class creates the GUI for specific conversations between users. It utilizes user and chat objects to populate the message scroll pane. Users are able to send messages to the other user(s) in the chat. Users can also edit and delete messages by clicking on a message. Users are only able to edit or delete their messages.
- **Testing**: This class contains a mix of majority conversation GUI as well as some basic accessors and mutators. Testing for the accessors and mutators were conducted in the same manner as mentioned above. The populateJList method was tested the same as mentioned above as well. GUI was again exhausted with a large variety of user input to ensure that the program wouldn't fail. Finally, the backend was tested to make sure that message objects were being read properly and that instances of conversations between person(s) was stored and able to be accessed again. 

#### EditAccountPage.java
- **Description**: This Java class creates the GUI for the page that allows users to edit and view account information. On this page, users are able to edit their password, which is then updated on the server side. Once the user signs out and signs back in, they will have to enter the updated password. 
- **Testing**: This class contains entirely GUI and said GUI was tested exactly as the above classes. 

#### HomePage.java
- **Description**: This Java class creates the GUI for the splash page of the application. When the user launches the application, he/she will be greeted with this page, which prompts for either signing up for a new account or signing in to a preexisting account. 
- **Testing**: This class contains entirely GUI and said GUI was tested exactly as the above classes.

#### LoginPage.java
- **Description**: This Java class creates the GUI for the log-in page. On this page, the user is able to enter his/her credentials and sign into his/her account. 
- **Testing**: By deafult, the log-in page tests if the fields entered are blank, and throws an error message. Outside of this, the class contains entirely GUI and said GUI was tested exactly as the above classes.

#### Message.java
- **Description**: This Java class creates objects for individual messages. Each message consists of a String `message` (the message content itself), as well as a `uid` (a unique identifier for each message). 
- **Testing**: The `MessageTest.java` class creates a test message object, passes in parameters, and tests each method in `message.java`. `MessageTest.java` prints the result of testing to console. Since each message sent by a user is an object, testing was conducted similarly to the `Chat.java` class. Each accessor and mutator method was tested in the `MessageTest.java` class, as well as invalid input testing to ensure that the correct return type was returned.  

#### MessageServer.java
- **Description**: This Java class is the backend for an entire instance of a message application. It contains functionality for connecting to the right socket; processing changes to user accounts, messages, and chats; listens for changes in chats; and dynamic data structures that contain user information, and more. All changes to the aforementioned fields, objects, etc occur here.  
- **Testing**: 

#### ProcessAddNewChat.java
- **Description**: This Java class sends message objects to their respective message servers. 
- **Testing**: The accessor and mutator methods for the Chat and User objects were tested as described above. Invalid input was tested as well, and failed accordingly. 

#### ProcessDeleteAccount.java
- **Description**: This Java class handles the deletion of accounts. If a user account is found in the respective message server, it is removed from the hashmap of users. 
- **Testing**: By default, this class handles the case where an account that has already been deleted is attempted to be deleted. In testing, accounts were created on the GUI side of the application and deleted accordingly. Each attempt passed. For the JUnit testing, the two methods that accessed and set the unique ID were tested and passed accordingly, and failed with invalid input. 

#### ProcessDeleteChat.java
- **Description**: This Java class serves the same functionality as `ProcessDeleteAccount.java`, but for chat objects instead of user objects. 
- **Testing**: By default, this class handles the case whether or not a chat could be deleted. Since this class is solely GUI based, we tested by creating and deleting chats on numerous instances of chat pages. Each test (deletion of a conversation) passed accordingly. 

#### ProcessDeleteMessage.java
- **Description**: This Java class serves the same functionality as `ProcessDeleteAccount.java`, but for individual message objects instead of user objects. 
- **Testing**: As mentioned in the testing portion of the `ProcessDeleteChat.java` class above, testing was done similarly. Messages were deleted numerous times on numerous conversation pages, and passed accordingly. 

#### ProcessEditAccount.java
- **Description**: This Java class processes edits to a user's account. Specifically, it handles the editing of a user's password through the `handleEditPassword` method in `MessageServer`.
- **Testing**: This class is entirely GUI based. For this, we created numerous accounts and edited the password for each. Each test passed accordingly. 

#### ProcessEditMessage.java
- **Description**: This Java class processes edits to a message.
- **Testing**: For this class, we tested the editing of messages by testing numerous instances of conversations and editing the messages within them. Each edit of message passed accordingly. 

#### ProcessGetChat.java
- **Description**: This Java class handles whether or not the chat clicked on is a valid chat. 
- **Testing**: This class was tested by running multiple instances of the app and checking to make sure chats appeared.

#### ProcessGetNewChats.java
- **Description**: This Java class handles the refreshing of the chat page. When the `Refresh` button is clicked on the chat page, `ProcessGetNewChats.java` updates the chats page with any changes. 
- **Testing**: This class was tested by creating new chats between users, and ensuring that the participating users' chats appeared on their chat pages properly. To test this, we created multiple users, and created multiple conversations between them. Every time a conversation was created, we refreshed to ensure that this class was properly returning new chats, and that the ChatsPage was updating properly. Each attempt at refreshing passed. 

#### ProcessGetNewMessages.java
- **Description**: This Java class has the same functionality as the `ProcessGetnewChats` class, except for message objects instead of chats. 
- **Testing**: This class is similar to `ProcessGetNewChats`, except for messages within conversations. Testing was done similarly, except with new messages instead of chats. 

#### ProcessGetUserList.java
- **Description**: This Java class creates a thread that handles getting an ArrayList of users from the server. This list of users is returned to the `AddChatPage`
- **Testing**: This class was tested bby running multiple instances of the application and making sure users were populated in the "All Users" jlist on the `AddChatPage`.

#### Threads.ProcessLogin.java
- **Description**: This Java class creates a thread that handles the log-in process. It checks the hashmap of users for the credentials entered by a user on the log-in page, and launches the chat page if the credentials match. 
- **Testing**: This class was tested by passing in valid and invalid user credentials (Note: the term 'Invalid' refers to user credentials that did not exist on the backend, meaning that the account either was never created or the credentials were typed incorrectly). Valid user credentials successfully logged in and launched a new ChatsPage, while invalid user credentials notified the user. Each attempt at valid user credentials passed, and invalid user credentials failed.

#### ProcessSendNewMessages.java
- **Description**: This Java class handles the sending of new messages when it is called from any instance of a Conversation. 
- **Testing**: Since this class is mostly GUI based, we ensured that new messages sent were being attached to the proper Chat and user. The accessor and mutator methods were tested as described above, for valid and invalid input. 

#### ProcessSignUp.java
- **Description**: This Java class creates a thread that handles the process of signing up a new user account. If the entered user credentials are valid, it launches a new chat page for the recently created user. 
- **Testing**: Similar to the class that processed log-in, this class was tested by signing up for accounts using a variety of input, to ensure that proper user accounts were created. 

#### SignUpPage.java
- **Description**: This Java class contains the GUI for the sign-up page, if a user selects the 'sign-up' button on the home page (when the message application is launched). Additionally, a unique identifier `uid` is generated for each user. If credentials are valid, it creates a new `user` object using the given credentials. 
- **Testing**: By default, this class tests if either the username, password, or name fields are empty. Additionally, GUI elements were exhausted by us to ensure that nothing could cause the program to fail. 

#### User.java
- **Description**: This Java class creates user objects from valid parameters (`username`, `password`, `name`, `uid`). The `MessageServer` stores objects of type `User` in the `users` hashmap. 
- **Testing**: Similar to Chat and Message objects, this class creates instances of Users. Each accessor and mutator was tested to ensure proper input and output. 

#### Utility.java
- **Description**: This Java class contains methods for displaying error, information, or warning messages when needed throughout the application. 
- **Testing**: Throughout the program, we purposely created error events to ensure that the Utility class was displaying the proper JOptionPane message (error, information, warning, etc). The accessor and mutator methods for the hostname and port number were tested as well, and passed. 


## Contributors

- Alex Deja
- Alex Natalenko
- Arav Verma
- Joaquin McCreary
- Samuel Ford

## Acknowledgements and References 

Various Oracle libraries were referenced, such as JScrollPane, multithreaded servers, etc. All referenced documentation can be found here: https://docs.oracle.com/en/java/. 
