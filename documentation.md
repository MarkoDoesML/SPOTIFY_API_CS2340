# Spotify Wrapped CS2340 Project

## Table of Contents
- [Introduction](#introduction)
- [Team Members](#team-members)
- [Scrum Roles](#scrum-roles)
- [User Stories](#user-stories)
- [Codebase Overview](#codebase-overview)
- [Setup Guide](#setup-guide)
- [Development Process](#development-process)
- [Version Control Guidelines](#version-control-guidelines)
- [Testing](#testing)
- [FAQ](#faq)
- [Troubleshooting](#troubleshooting)
- [Contributions](#contributions)

## Introduction
The Spotify Wrapped CS2340 Project aims to provide users with a personalized music experience by summarizing their listening habits on Spotify. The app integrates with the Spotify API to retrieve user data and present a colorful, engaging summary of their music preferences, listening history, and more.

## Team Members
- Andrew Boyer - Scrum Master
- Marko Gjurevski - Product Owner
- Max Edmonds - Developer
- Ali Asgar Momin - Developer
- Joshua Thomas - Documentarian
- Roderic Parson - Tech Support

## Scrum Roles
Each team member has unique responsibilities that contribute to the project's success:
- **Scrum Master** (Andrew): Facilitates Scrum meetings and ensures the team follows agile practices.
- **Product Owner** (Marko): Represents the stakeholders and the voice of the customer.
- **Developers** (Max and Ali): Implement the features of the project.
- **Documentarian** (Joshua): Responsible for maintaining all project documentation.
- **Tech Support** (Roderic): Assists with technical issues and maintains the development environment.

## User Stories
We are focusing on the following user stories for our project:
- **Base User Stories**:
  - User Story #1: Summary presentation of user's Spotify music listening habits.
  - User Story #2: Account creation and viewing of past summaries.

- **Additional User Stories**:
  - User Story #9: A feature allowing users to share their Spotify Wrapped with friends.
  - User Story #12: Cross-device account login using Firebase for data persistence.

## Codebase Overview

This project is structured as a typical Android Studio project with the following key directories and files:

- `.github`: Contains GitHub-specific configurations, including workflows for CI/CD.
- `.gradle` and `gradle`: Include gradle wrapper files and scripts that ensure the use of the correct Gradle version.
- `.idea`: Stores project-specific settings for Android Studio, such as code styles and configurations.
- `app`:
  - `src`:
    - `androidTest`: Contains automated Android test files.
    - `main`:
      - `java/com/example/spotify_api_app`: This is the primary package for the Java source files. 
        - `MainActivity.java`: The main entry point for the app's UI.
      - `res`: Resources directory containing all non-code assets, like layouts, drawable images, and values for strings and styles.
        - `drawable`: Directory for graphical assets that can be drawn to the screen.
        - `layout`: XML files defining the UI structure of activities and fragments.
        - `mipmap-*`: Directories containing different resolution icons for the app.
        - `values`: Contains strings, dimensions, and other values that can be used globally in the app.
        - `values-night`: Values specifically for the night mode (dark theme) of the app.
        - `xml`: XML files that can define certain configurations like preferences.
      - `AndroidManifest.xml`: Configures fundamental information about the app, such as components and permissions.
    - `test`: Contains unit tests that run on the local machine's JVM.
  - `.gitignore`: Specifies intentionally untracked files that Git should ignore.
  - `build.gradle`: Defines the build configuration for the app module.
  - `google-services.json`: Configuration file for using Google services, likely for Firebase integration.
  - `proguard-rules.pro`: Rules for ProGuard to shrink and obfuscate the code.
- `documentation.md`: The markdown file where this documentation is maintained.

Each file and directory serves a specific purpose in the development and deployment of the Android application. The structure follows Android's best practices and ensures that the codebase is maintainable and scalable.


## Setup Guide
To set up the project on your local machine:
1. Clone the repository: `git clone https://github.com/MarkoDoesML/SPOTIFY_API_CS2340.git`
2. Open Android Studio and import the project.
3. Sync Gradle and wait for the dependencies to resolve.
4. Run the app on an emulator or a physical device.

## Development Process
Our development process is iterative and follows agile methodologies:
1. Sprint Planning: Define the scope for the upcoming sprint.
2. Daily Standups: Brief daily meetings to discuss progress and blockers.
3. Pair Programming: Developers work in pairs for complex features.
4. Code Reviews: Each pull request is reviewed by at least one other team member before merging.

## Version Control Guidelines
- **Branch Naming**:
  - Feature branches should be named `feature/<feature-name>`.
  - Bugfix branches should be named `bugfix/<bug-name>`.
- **Commit Messages**:
  - Write clear, concise commit messages in the imperative mood.
- **Pull Requests**:
  - Ensure PRs describe the changes and link to the user story or task being addressed.

## Testing
Describe the testing strategies used, including unit tests, instrumentation tests, and UI tests. Mention how to run these tests in Android Studio.

## FAQ
Answer common questions about the project, its setup, and the development process.

## Troubleshooting
Provide solutions to common issues encountered during setup and development, such as environment configuration problems or common build errors.

## Contributions
Guide on how to contribute to the project, including coding conventions, PR guidelines, and more.

