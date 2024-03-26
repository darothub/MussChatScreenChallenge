# Chat App with Jetpack Compose

## Overview

This is a chat application built for Android using Jetpack Compose. It allows users to send and receive messages in a chat interface similar to the one found in the Muzz app. The application follows the MVVM (Model-View-ViewModel) architecture pattern for better separation of concerns and easier maintenance.

## Features

- Display a list of messages in a chat interface.
- Send messages to another user.
- Distinguish between sent and received messages.
- Group messages by date and time.
- Persistent storage of messages using Room database.
- Trigger messages from the 'other' user.
- Customizable themes for sender and receiver bubbles, text size, etc.

## Architecture

The application follows the MVVM architecture pattern:

- **Model**: Represents the data and business logic. It includes entities and data access objects (DAOs) for interacting with the database.
  
- **View**: Represents the UI components. It includes Jetpack Compose UI elements and functions to render the UI based on the ViewModel state.
  
- **ViewModel**: Acts as an intermediary between the Model and View. It retrieves data from the Model and prepares it for the View. It also handles UI-related logic and user interactions.

## Theme Customization

The application provides customizable themes for sender and receiver bubbles, text size, etc. These themes are defined in the `MyTheme.kt` file and can be easily modified to suit the application's design requirements.

## Installation

To run the application, follow these steps:

1. Clone the repository: `git clone [https://github.com/darothub/yMussChatScreenChallenge.git](https://github.com/darothub/MussChatScreenChallenge)`
2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.

## Dependencies

- Jetpack Compose: A modern toolkit for building native Android UI.
- Room Persistence Library: Provides an abstraction layer over SQLite to interact with the database.
- Material Components for Android: Provides customizable UI components following the Material Design guidelines.
- MockK: Mocking library for Kotlin `[visit](https://mockk.io/#junit4)`

## Screenshots

<img width="285" alt="Screenshot 2024-03-26 at 12 46 22" src="https://github.com/darothub/MussChatScreenChallenge/assets/21008156/cf586c15-c7a1-473b-9337-d46f2352b64a">
<img width="376" alt="Screenshot 2024-03-26 at 13 04 05" src="https://github.com/darothub/MussChatScreenChallenge/assets/21008156/dbce08e2-e272-445f-baf8-d6b9cac59fb5">


## Demo

https://github.com/darothub/MussChatScreenChallenge/assets/21008156/93004052-9789-4438-bbb5-708fec9d2b8d


## Credits

The design and requirements for this project are based on the Muzz app.


