# AutoFighter

A stateful combat automation script for Old School RuneScape, built on the ORB
scripting API ([osrsbots.com](https://osrsbots.com)).

## What it does

Runs an unattended combat loop against a configurable target: engages, loots,
buries bones, drinks potions on cooldown/health thresholds, and banks when
supplies run low — all driven by a single state machine so it can recover
from interruptions instead of stalling.

```java
public enum State {
    CONFIG, START, COMBAT, BURY_BONES, TO_COMBAT,
    OPEN_BANK, DEPOSIT, WITHDRAW, CLOSE_BANK, TO_BANK, STOP
}
```

- `AutoFighter.java` — main script class, state machine and script lifecycle.
- `CombatHandler.java` — target selection, engagement, loot/bury logic.
- `PotionHandler.java` — potion tracking and consumption thresholds.
- `FightProfile.java` — per-target combat configuration.
- `ui/` — Swing configuration panel (built with IntelliJ's GUI Designer,
  hence `UserInterface.form`).

## Requirements

This depends on the private ORB scripting API and its RuneLite fork, which
are not included here and aren't publicly distributable — the source is
provided as a reference/portfolio sample, not a standalone buildable project.
