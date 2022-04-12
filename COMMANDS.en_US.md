# RunForMoney - command manual

In fact, I copied them one by one according to the plugin.yml (laughs). Made some changes, more details.

Some commands with parameters will specify the command format, and those without parameters will not be written.

Parameters wrapped with `<>` are required parameters, and parameters wrapped with `[]` are optional parameters.

## Player common commands

### /hunter

Become a hunter.

Usage: `/hunter [playername..]`

parameter:

    playername: When this parameter exists, the player whose name is the same as the value of this parameter will become the hunter. For example /hunter AA will make player AA a hunter, or more than one.

### /runner

Become a runner.

Usage: `/runner [playername..]`

parameter:

    playername: When this parameter exists, the player whose name is the same as the value of this parameter will become the escape team. For example, /hunter AA will make player AA a runaway, or more than one.

## Administrator common commands

The execution of all commands in this column must require administrator privileges.

### /start

Start the "Run For Money" game.

Usage: `/start [countDownTime]`

parameter:

    countDownTime: Hunter's release time. If no value is provided or a negative value is provided, the value provided by the configuration file is used.

### /pause

Pause the "Run For Money" game and notify all players.

### /resume

Continue the "running away" game and notify all players. It is generally used after all game players have been disconnected but have successfully reconnected.

### /forcestart

Force a "Run For Money" game to start.

Because I wrote it myself to debug the plugin, please don't use it as much as possible.

### /forcestop

Force stop the "Run For Money" game. Recommended only for debugging.

### /endroom

Sets the x y z coordinates in the world worldName as the end point position. If x y z is not provided and the player is the player, set the player's position to the end position.

If parameters are provided, all three must be provided at the same time.

Usage: `/endroom [x: int] [y: int] [z: int] [worldName]`

parameter:

    x: X coordinate between terminations
    y: Y coordinate between terminations
    z: Z coordinate between terminations
    worldName: The world where the coordinates are located. If not provided, use the value provided by the configuration, or the world where the player executing this command is located.

### /newgroup

Create a new hunter group named `<name>`.

Usage: `/newgroup <name>`

parameter:

    name: The name of the new group. There cannot be spaces.

### /removegroup

Remove hunter group named `<name>`.

Usage: `/removegroup <name>`

parameter:

    name: The name of the group to be removed.

### /activategroup

Enables all hunters in the `<name>` group.

Usage: `/activategroup <name>`

parameter:

    name: the name of the hunter group that will be enabled

### /deactivategroup

Disables all hunters in the `<name>` group.

Usage: `/deactivategroup <name>`

parameter:

    name: the name of the hunter group that will be disabled

### /activatehunter

Enable hunters. Place the hunter in the provided position when x y z is provided (useful when reusing a disabled hunter).

Usage: `/activatehunter <playername> [x: int] [y: int] [z: int]`

parameter:

    playername: the player name of the hunter that will be enabled
    x: the X coordinate of where the hunter appears
    y: Y coordinate of where the hunter appears
    z: Z coordinate of where the hunter appears

### /deactivatehunter

Disable hunters.

Usage: `/deactivatehunter <playername>`

parameter:

    playername: the player name of the hunter that will be banned

### /rfmitem

Get some in-game items for this plugin. This command is a configuration tool and is only available to administrators.

You can also use this command to get props registered by other plugins. Please visit [RFM API](https://github.com/SNWCreations/RunForMoneyAPI) for details.

Usage: `/rfmitem <itemname>`

parameter:

    itemname: the internal name of the item

### /grouplist

List all created groups.

### /teamlist

List the names of all players on both teams.

### /coinlist

List the amount of coins for all players.

This command is available to all players, but I think admins use this command more often, so I put it in the section Common Commands for Admins.

### /exportcoinlist

Export the current coin list data to a file.

### /rfmrespawn

Makes `<playername>` rejoin the runaway game. Can be processed in batches.

Usage: `/rfmrespawn <playername..>`

parameter:

    playername: Player name, which can be multiple, separated by spaces.

### /rfmsettingsquery

Query existing runaway game configurations.

### /rfmtimer

Enable/disable display of remaining time. The remaining time is not displayed by default.

### /rfmreload

Reload the configuration from the hard disk. But players will not leave the team or group they are in.