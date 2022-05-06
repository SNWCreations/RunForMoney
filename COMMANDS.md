# RunForMoney - 命令说明书

其实是我一个个照着 plugin.yml 抄的 (笑) 。做了些修改，更详细。

一些有参数的命令会特别写明命令格式，没参数的不会写。

用 `<>` 包裹的参数为必填参数，用 `[]` 包裹的参数为可选参数。

## /rfmgame

一个对逃走中游戏进行基本操作的命令。

需要管理员权限来执行此命令以及此命令的所有子命令。

用法: `/rfmgame <start/stop/pause/resume/respawn> [arguments..]`

参数:

    start: 启动游戏。
        hunterReleaseTime: 此参数会影响猎人释放的时间。默认使用配置项 'hunter_release_time' 指定的值。
    stop: 强制停止游戏。
    pause: 暂停游戏。
    resume: 继续游戏。
    respawn: 复活某个已不在游戏中的玩家。
        playerName: 将被复活的玩家的名称。

## /rfmteam

队伍系统命令。

除了 list 子命令外，其他两个命令在游戏时不可用。

参数:

    join: 加入某队伍。
        teamName: 将加入的队伍的名称。
            players: (可选) 提供时，此参数内所有已列出的玩家将被强制加入指定的队伍。
    leave: 离开所在队伍。
        players: (可选) 提供时，此参数内所有已列出的玩家将强制从所在的队伍离开。
    list: 列出所有已定义的队伍。

## /rfmgroup

关于猎人组的命令。

参数:

    join: 加入某个组。
        groupName: 将要加入的组的名称。
    leave: 离开所在组。
    create: 创建组。需要管理员权限。
        groupName: 将要创建的组的名称。
    delete: 删除组，并且通知指定组内的所有猎人。需要管理员权限。
        groupName: 将要删除的组的名称。
    activate: 激活某个组的所有猎人。
        groupName: 将要激活的组的名称。
    deactivate: 禁用某个组的所有猎人。
        groupName: 将要禁用的组的名称。
    list: 列出所有已存在的组。

## /rfmdata

有关游戏数据的命令。

参数:

    coin: 显示硬币排行榜。
    exportcoin: 导出现在的硬币榜数据到一个文件。需要管理员权限。
    playerremaining: 显示所有存活的和已不在游戏中的玩家的名称。
    timer: 启用/禁用 剩余时间倒计时。
    settings: 显示游戏配置。
    reload: 从磁盘重新加载插件配置。需要管理员权限。

## /hunter

成为猎人。

用法: `/hunter [playername..]`

参数:

    playername: 此参数存在时，则让名称与此参数的值相同的玩家成为猎人。如 /hunter AA 会让玩家 AA 成为猎人，也可以是多个。提供此参数时需要管理员权限才能批量操作。

## /runner

成为逃走队员。

用法: `/runner [playername..]`

参数:

    playername: 此参数存在时，则让名称与此参数的值相同的玩家成为逃走队员。如 `/hunter AA` 会让玩家 `AA` 成为逃走队员，也可以是多个。提供此参数时需要管理员权限才能批量操作。

## /joingroup

成为 `<name>` 组的一具猎人。

用法: `/joingroup [playername..]`

参数:

    playername: 当 playername 参数存在时，将强制把名称与此参数的值相同的玩家放入 `<name>` 组，但前提是该玩家需要是猎人。playername 可以为多个，例如 `/joingroup AA BB`。提供此参数时需要管理员权限才能批量操作。

## /leavegroup

从所在的组离开。

用法: `/leavegroup [playername..]`

参数:

    playername: 当 playername 参数存在时，将强制使 <playername> 玩家离开其所在的组（不是队伍），但前提是该玩家需要是猎人。playername 可以为多个，例如 /leavegroup AA BB。提供此参数时需要管理员权限才能批量操作。

## /rfmtimer

启用/禁用 剩余时间的显示。默认不显示剩余时间。

## /playerremaining

查看存活和被淘汰的逃走队员列表。

## 管理员常用命令

此栏下所有命令的执行一定需要管理员权限。

## /start

开始 "逃走中" 游戏。

用法: `/start [countDownTime]`

参数:

    countDownTime: 猎人的释放时间。如果没有提供值或提供的值为负值，则使用配置文件提供的值。

## /pause

暂停 "逃走中" 游戏，并且通知所有玩家。

## /resume

继续 "逃走中" 游戏，并且通知所有玩家。一般在所有游戏玩家掉线但都成功重连后使用。

## /forcestart

强制启动一次 "逃走中" 游戏。

因为是我自己写出来用来调试插件的，请尽可能不要使用。

## /forcestop

强制停止 "逃走中" 游戏。建议只在调试时使用。

## /endroom

设置位于 worldName 世界的 x y z 坐标为终止间位置。若 x y z 未提供且执行者是玩家，则设置执行者的位置为终止间位置。

若提供参数，则三个都要同时提供。

用法: `/endroom [x: int] [y: int] [z: int] [worldName]`

参数:

    x: 终止间的 X 坐标
    y: 终止间的 Y 坐标
    z: 终止间的 Z 坐标
    worldName: 坐标所在世界，未提供则使用配置提供的值，或执行此命令的玩家所在的世界。

## /newgroup

新建名为 `<name>` 的猎人组。

用法: `/newgroup <name>`

参数:

    name: 新组的名字。不能有空格。

## /removegroup

移除名为 `<name>` 的猎人组。

用法: `/removegroup <name>`

参数:

    name: 将被移除的组的名字。

## /activategroup

启用在 `<name>` 组内的所有猎人。

用法: `/activategroup <name>`

参数:

    name: 将被启用的猎人组的名字

## /deactivategroup

禁用在 `<name>` 组内的所有猎人。

用法: `/deactivategroup <name>`

参数:

    name: 将被禁用的猎人组的名字

## /activatehunter

启用猎人。当 x y z 提供时则将该猎人放置到提供的位置（适用于重复利用已禁用猎人的时候）。

用法: `/activatehunter <playername> [x: int] [y: int] [z: int]`

参数:

    playername: 将被启用的猎人的玩家名
    x: 猎人出现位置的 X 坐标
    y: 猎人出现位置的 Y 坐标
    z: 猎人出现位置的 Z 坐标

## /deactivatehunter

禁用猎人。

用法: `/deactivatehunter <playername>`

参数:

    playername: 将被禁用的猎人的玩家名

## /rfmitem

获取本插件的一些游戏内道具。此命令属于配置工具，仅管理员可用。

也可以用此命令获取其他插件注册的道具。具体请移步 [RFM API](https://github.com/SNWCreations/RunForMoneyAPI) 了解。

用法: `/rfmitem <itemname>`

参数:

    itemname: 道具的内部名称

## /grouplist

列出所有已创建的组。

## /teamlist

列出两队伍中所有玩家的名字。

## /coinlist

列出所有玩家的B币数量。

此命令所有玩家均可用，但我认为管理员更经常用这个命令，故放在 管理员常用命令 这一节。

## /exportcoinlist

导出现在的 B币榜 数据到一个文件。

## /rfmrespawn

使 `<playername>` 重新加入逃走中游戏。可以批量处理。

用法: `/rfmrespawn <playername..>`

参数:

    playername: 玩家名，可以是多个，用空格隔开。

## /rfmsettingsquery

查询现有的逃走中游戏配置。

## /rfmreload

重新从硬盘加载配置。但玩家不会从所在的队伍或组离开。