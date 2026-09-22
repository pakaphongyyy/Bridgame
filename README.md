# Bird Game Starter

Java Swing starter project based on the agreed game architecture.

## Current rules already coded

- Main -> StartFrame -> PlayerSetupFrame -> GameFrame -> ResultFrame
- Players can be created with names.
- Hearts are counted per level and reset on level change.
- Heart requirements:
  - Level 1 = 10
  - Level 2 = 15
  - Level 3 = 20
  - Level 4 = 25
- Passed pipes accumulate across all levels and do not reset.
- Level 5 is reserved for the ending scene where the bird flies to the nest.
- Ranking:
  1. Players who reached the nest first, ordered by finish order.
  2. Unfinished players follow, ordered by highest level.
  3. If same level, higher total pipes wins.
  4. If still tied, earlier play order wins.

## Test controls in this starter

This starter first verifies the game rules before real physics is added.

- H = collect a heart
- P = pass one pipe pair
- X = collide / lose current turn
- N = finish by reaching the nest (only on level 5)

## Run in VS Code

1. Open this folder in VS Code.
2. Make sure Extension Pack for Java is installed.
3. Open `src/birdgame/Main.java`.
4. Press Run / F5.

Next step: replace test controls with actual bird physics, moving pipes, hearts, collisions, and the final nest animation.


## Thai font fix

The project now automatically selects an installed font that supports Thai, preferring:

1. Tahoma
2. Leelawadee UI
3. Noto Sans Thai
4. Noto Sans
5. Arial
6. Dialog

VS Code and the Java runtime are also configured to use UTF-8.


## Single-player flow with saved history

The game now runs one player at a time:

Main -> StartFrame -> PlayerSetupFrame -> GameFrame -> ResultFrame

- Only one player name is entered before each game.
- When that player's turn ends, the result is appended to `data/player_history.csv`.
- Previous player records remain available after restarting the application.
- `ResultFrame` displays the saved history and has a "ผู้เล่นคนถัดไป" button.
- Each play is stored as a separate record. If the same name plays again, another record is added.
