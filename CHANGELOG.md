# Run For Money - 更新日志

## v1.1.5 (2022/2/7)

* 命令更新: 给一些命令增加了命令补全。
* 命令更新: 修复了一些命令补全在判断时未考虑玩家是否是管理员的问题。 ~~玩家仍然不能以非管理员身份执行这些命令。那只是错觉。~~
* 命令更新: 对一些命令执行后的提示信息进行了修正。
* 技术性更新: 改进了 snw.rfm.group.Group 类的一些方法。
* 技术性更新: 在包 snw.rfm.events 下增加了一个没什么用的 package-info.java 。
* 技术性更新: 修正了 snw.rfm.Util.getAllTheStringsStartingWithListInTheList 方法的一个严重错误。
* 技术性更新: 将 snw.rfm.config.GameConfiguration 类的 parseXYZStringIntoLocation 方法移至 snw.rfm.Util 类。
* 技术性更新: 将原有的 snw.rfm.game.GameConfiguration, snw.rfm.game.Preset, snw.rfm.item.ItemConfiguration 类并入新的 snw.rfm.config 包。
* 技术性更新: 将 snw.rfm.item.RFMItems 类移至根包 snw.rfm 。
* 技术性更新: 将 snw.rfm.game.EventProcessor, snw.rfm.item.processor.HunterPauseCardItemProcessor 类并入新的 snw.rfm.processor 包。
* 技术性更新: 移除了包 snw.rfm.item 。
* 技术性更新: 移除了 snw.rfm.config.GameConfiguration, snw.rfm.config.Preset, snw.rfm.config.ItemConfiguration 类的 getInstance 方法，并将其引用修正。

## v1.1.4 (2022/2/6)

* **此仓库开始使用 GPLv3 作为许可证。同时，所有旧版本的代码及其产物自动受到 GPLv3 的保护。**
* 游戏逻辑更新: 将 B币榜初始值 (0.00) 放置时机推迟至 CoinTimer 第一次计数。
* 游戏逻辑更新: 改进了移除所有药水效果的方法。同样用到了 Java 8 的 Stream 。
* 游戏逻辑更新: 移除了对猎人移动事件的检查。
* 命令更新: 加入了 exportcoinlist 命令，可以导出现在的 B币榜 数据到一个文件。方便玩家们统计数据。
* 命令更新: 改进了 rfmitem 命令的判断 (改用 switch 结构) 和命令自动补全 (更加智能)，从而优化了性能。
* 命令更新: 改进了 coinlist 命令的数据读取。新的 exportcoinlist 命令也使用~~(照抄)~~改进版的代码。
* 命令更新: 修复了只禁用一个组时进行了启用的误操作。
* 命令更新: 修复了只有在玩家已经在某个组内时才能使玩家加入组的错误。
* 命令更新: 修复了能在游戏中换队伍的错误。
* 命令更新: 现在 deactivategroup 命令不能在禁用组的同时移除组了。
* 命令更新: 现在 rfmitem 命令可以批量获取道具，例如 "/rfmitem hpc ep"。
* 命令更新: 现在所有和组有关的命令都拥有了命令补全，除了"创建组"命令。
* 命令更新: 现在所有命令都有了全称，原简称仍然保留。
* 技术性更新: 将 snw.rfm.Util.registerTabCompleter 方法合并入 snw.rfm.Util.registerCommand 方法。
* 技术性更新: 将 snw.rfm.commands.CoinListCommand 的 sortDescend 方法移至 snw.rfm.Util 类中。
* 技术性更新: 用 Java 8 的 Stream 优化了一些细节。~~(Stream 真好用)~~

## v1.1.3

果然，大错不断，小错多多！

* 游戏逻辑更新: 修复了猎人释放倒计时显示一次"0"后再释放的错误。此错误来自 v1.1.2 对 BaseCountDownTimer 的 run 方法重写。
* 游戏逻辑更新: **修复了"暂停"功能的严重错误。**
* 游戏逻辑更新: 修复了猎人使用此道具无效还使道具消失的错误。
* 游戏逻辑更新: 改进了猎人暂停卡使猎人暂停的方法: 同 v1.1.2 的方法。
* 游戏逻辑更新: 将 B币初始化 延迟到开始游戏时。
* 命令更新: 修复后台可以在游戏中修改玩家队伍的错误。
* 命令更新: 现在 B币榜 会增加排名前缀。例如 "1. SNWCreations: 114514" 。
* 命令更新: 现在 B币榜 只会在非空时(至少进行一次游戏后)显示，如果你尝试在一次游戏都没进行的情况下查看 B币榜 ，只会给出一条错误信息。
* 技术性更新: 随着猎人暂停卡使猎人暂停的方法的改进，移除了 snw.rfm.tasks.items.HunterPauseTimer 以及 snw.rfm.item.tasks 包。
* 技术性更新: 移除了 snw.rfm.tasks.CoinTimer 类中自 v1.1.2 更新后从未使用的方法 getCoinEarned 。
* 技术性更新: 优化(重写)了 snw.rfm.game.TeamHolder 中的 isNoHunterFound 和 isNoRunnerFound 方法。

## v1.1.2 (2022/2/3)

* 游戏逻辑更新: 修复了猎人可以抓捕队友的错误。
* 游戏逻辑更新: 修复了在游戏开始前移出有玩家在内的组会导致玩家变为旁观者模式的错误。
* 游戏逻辑更新: 改进了使猎人不能动的方法: 利用原版特性(给予 255 级缓慢 以及 129 级跳跃提升使其不能跳跃从而防止通过跳跃移动) 使猎人不能移动。
* 游戏逻辑更新: 将原本的游戏运行前重新上线时仅移除速度效果改为移除其有的所有药水效果。
* 命令更新: 修复了一些命令因为没有去除重复参数导致的对玩家重复操作的错误。
* 命令更新: **重写"加入组"命令。**
* 命令更新: 进行了一些细节优化。
* 命令更新: 修复了一些因为直接照抄自己代码造成的错误。~~这个故事告诉我们，抄代码要检查(苦笑)。~~
* 技术性更新: 对 BaseCountDownTimer 的 run 方法进行了重写。不影响插件运行。
* 技术性更新: 将 BaseCountDownTimer 的两个 abstract 方法以及其子类的方法 设为 protected 访问级。
* 技术性更新: 将 InGameEventProcessor 命名为曾经的名字 EventProcessor 。
* 技术性更新: 将加入组命令和离开组命令的类从包 snw.rfm.commands.hunter 移至包 snw.rfm.commands.group 。

## v1.1.1 (2022/2/1)

刚发完 v1.1.0 就发现了 Bug ，无语...

* 游戏逻辑更新: 修复了一个可能的潜在问题: B币计数器在启动时又重置了一次B币数量，与 InGameEventProcessor 的玩家加入时的B币初始化冲突。
* 技术性更新: 对 BaseCountDownTimer 的 run 方法进行细节调整，并将其设为 final 方法。

## v1.1.0 (2022/2/1)

新年快乐！虎年大吉！

* **!功能性更新!** 增加了猎人暂停卡道具。可配置。
* 游戏逻辑更新: 增加了最少玩家限制。现在插件会在启动游戏时检查玩家数量是否满足配置文件的要求。
* 游戏逻辑更新: 移除挂机提醒功能。因为不实用，而且错误较多。
* 游戏逻辑更新: 修复启动游戏时游戏人数检查不严谨的错误。
* 游戏逻辑更新: 修复 v1.0.1 中 "修复了弃权后方块未消失的错误" 带来的一个新错误。
* 游戏逻辑更新: 修复移除猎人时未将其从所在组移除的错误。
* 命令更新: 对 teamlist 命令进行了优化。
* 命令更新: 修复后台批量向队伍添加玩家时未正确处理的错误。
* 命令更新: ag 命令现在可以批量启用猎人组。
* 命令更新: 将原有的 ep 命令并入新的 rfmitem 命令中。
* 命令更新: 给一些命令增加了别名。
* 技术性更新: **此插件开始内置 NBTAPI，服主不再需要专门为此插件安装 NBTAPI。**
* 技术性更新: 将 plugin.yml 中的 authors 项改为 author。
* 技术性更新: 对游戏内容配置 (snw.rfm.game.GameConfiguration 类) 进行了优化，当需要时再取值。
* 技术性更新: 增加了 snw.rfm.item 包作为物品模块包。
* 技术性更新: 增加了 forcestart 命令调试用。**请您不要使用此命令！若您执意使用，则因此带来的一切后果由您自行承担。**

## v1.0.1 (2022/1/27)
* 游戏逻辑更新: 修复了弃权后弃权方块未消失的错误。
* 技术性更新: 移除了 snw.rfm.events 包中的 GameStopType 枚举。同时移除了 GameStopEvent 类中的 getStopType() 方法。

## v1.0.0 (2022/1/26)
最早版本。
