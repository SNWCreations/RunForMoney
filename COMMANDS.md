# RunForMoney - 命令说明书

其实是我一个个照着 plugin.yml 抄的 (笑) 。做了些修改，更详细。

一些有参数的命令会特别写明命令格式，没参数的不会写。

用 `<>` 包裹的参数为必填参数，用 `[]` 包裹的参数为可选参数。

## 玩家常用命令

### /hunter

成为猎人。

用法: `/hunter [playername..]`

参数:

    playername: 此参数存在时，则让名称与此参数的值相同的玩家成为猎人。如 /hunter AA 会让玩家 AA 成为猎人，也可以是多个。

### /runner

成为逃走队员。

用法: `/runner [playername..]`

参数:

    playername: 此参数存在时，则让名称与此参数的值相同的玩家成为逃走队员。如 /hunter AA 会让玩家 AA 成为逃走队员，也可以是多个。

## 管理员常用命令

此栏下所有命令的执行一定需要管理员权限。

### /start

开始 "逃走中" 游戏。

用法: `/start [countDownTime]`

参数:

    countDownTime: 猎人的释放时间。如果没有提供值或提供的值为负值，则使用配置文件提供的值。

### /pause

暂停 "逃走中" 游戏，并且通知所有玩家。

### /resume

继续 "逃走中" 游戏，并且通知所有玩家。一般在所有游戏玩家掉线但都成功重连后使用。

### /forcestart

强制启动一次 "逃走中" 游戏。

因为是我自己写出来用来调试插件的，请尽可能不要使用。

### /forcestop

强制停止 "逃走中" 游戏。建议只在调试时使用。

### /endroom

设置位于 worldName 世界的 x y z 坐标为终止间位置。若 x y z 未提供且执行者是玩家，则设置执行者的位置为终止间位置。

若提供参数，则三个都要同时提供。

用法: `/endroom [x: int] [y: int] [z: int] [worldName]`

参数:

    x: 终止间的 X 坐标
    y: 终止间的 Y 坐标
    z: 终止间的 Z 坐标
    worldName: 坐标所在世界，未提供则使用配置提供的值，或执行此命令的玩家所在的世界。

### /newgroup

新建名为 `<name>` 的猎人组。

用法: `/newgroup <name>`

参数:

    name: 新组的名字。不能有空格。

### /removegroup

移除名为 `<name>` 的猎人组。

用法: `/removegroup <name>`

参数:

    name: 将被移除的组的名字。

### /activategroup

启用在 `<name>` 组内的所有猎人。

用法: `/activategroup <name>`

参数:

    name: 将被启用的猎人组的名字

### /deactivategroup

禁用在 `<name>` 组内的所有猎人。

用法: `/deactivategroup <name>`

参数:

    name: 将被禁用的猎人组的名字

### /activatehunter

启用猎人。当 x y z 提供时则将该猎人放置到提供的位置（适用于重复利用已禁用猎人的时候）。

用法: `/activatehunter <playername> [x: int] [y: int] [z: int]`

参数:

    playername: 将被启用的猎人的玩家名
    x: 猎人出现位置的 X 坐标
    y: 猎人出现位置的 Y 坐标
    z: 猎人出现位置的 Z 坐标

### /deactivatehunter

禁用猎人。

用法: `/deactivatehunter <playername>`

参数:

    playername: 将被禁用的猎人的玩家名

### /rfmitem

获取本插件的一些游戏内道具。此命令属于配置工具，仅管理员可用。

也可以用此命令获取其他插件注册的道具。具体请移步 [RFM API](https://github.com/SNWCreations/RunForMoneyAPI) 了解。

用法: `/rfmitem <itemname>`

参数:

    itemname: 道具的内部名称

### /grouplist

列出所有已创建的组。

### /teamlist

列出两队伍中所有玩家的名字。

### /coinlist

列出所有玩家的B币数量。

此命令所有玩家均可用，但我认为管理员更经常用这个命令，故放在 管理员常用命令 这一节。

### /exportcoinlist

导出现在的 B币榜 数据到一个文件。

### /rfmrespawn

使 `<playername>` 重新加入逃走中游戏。可以批量处理。

用法: `/rfmrespawn <playername..>`

参数:

    playername: 玩家名，可以是多个，用空格隔开。

### /rfmsettingsquery

查询现有的逃走中游戏配置。

### /rfmtimer

启用/禁用 剩余时间的显示。默认不显示剩余时间。

### /rfmreload

重新从硬盘加载配置。但玩家不会从所在的队伍或组离开。