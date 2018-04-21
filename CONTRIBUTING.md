# Contributing

This project is open source and free to contribute to as there are probably tons of room for improvement.

## Setting up your Environment

Follow these instructions to set up your environment.

    1. Create a fork (if you already have a local repository, skip to step 3)

    2. Clone the repository through command line or a GUI (I personally use GitKraken)

    3. Move to your local repository

    4. Configure an upstream remote to keep your fork updated

    ```
    $ git remote add upstream https://github.com/NiflheimDev/Stockfish-Java.git
    ```

    5. Create a branch based on upstream/development

    ```
    $ git checkout -b patch-1 upstream/development
    Switched to new branch 'upstream/development'
    ```

## Making Changes

Depending on your changes there are certain rules you have to follow if you expect your Pull Request to be merged.

    1. Adding a new Method or Class

        If the addition is not internal(e.g. an impl class or private method) please write documentation.

    2. Making a commit

        When you commit your changes, make sure you write a proper commit caption explaining your work.

    3. Updating your fork

        Before you start committing, make sure your fork is up to date.

## Creating a Pull Request

Once you have made your changes, commit and push them. Then click *Create a Pull Request* and away for a review!