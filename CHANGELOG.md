# Run For Money - 更新日志

注意:

* 本 CHANGELOG 所有标注的时间是对应版本完成的时间。
* 当有功能更新时 (也就是当版本号第二个数字有变时)，推荐认真看看更新日志，有些新功能的实现需要配置文件的参与，您可能需要更新配置文件。

## v1.2.1

* **!道具更新!** 移除了 保命符 (曾用内部名称: sl) 。因为从未使用，并且不完善。 ~~(其实是我懒得写)~~
* 技术性更新: 修复未对 HunterCatchPlayerEvent#isCancelled 方法的返回值做出处理的错误。
* 技术性更新: 现在 GameProcess#addTimer 方法不会检查定时器类型，因为考虑到 DelayedTimer 这个对其他 BaseCountDownTimer 的子类的包装。
* 技术性更新: 向 DelayedTimer 添加了一个构造方法: DelayedTimer(int secs, Runnable runnable, GameProcess processToBind) ，为下一个功能更新做准备。

## v1.2.0 (2022/2/27)

API 版本: v1.2.0

修复了一些严重错误，强烈建议更新！

* **!功能性更新!** 增加了复活功能，详情见以下日志。
* 游戏逻辑更新: 在配置文件内增加了名为 "stop_game_on_no_runner_alive" 和 "coin_multiplier_on_be_catched" 的项，详情见新配置文件。
* 游戏逻辑更新: 修复了 游戏结束时因为内部代码使用 Bukkit.getScheduler()#cancelTasks 方法后又遍历调用 BukkitRunnable#cancel 方法导致抛出异常 的问题。**若未修复此问题，会导致游戏不能正常停止，从而导致不能启动新游戏。**
* 游戏逻辑更新: 修复了上个版本的猎人暂停卡不工作的问题。
* 游戏逻辑更新: 改进了猎人暂停卡的工作方式。 ~~(这个似乎更偏技术一点)~~
* 游戏逻辑更新: 修复了一些潜在问题。
* 游戏逻辑更新: 现在，游戏结束时逃走成功的玩家将不会被传送到终止间。
* 命令更新: **增加了 /rfmrespawn 命令** ，可以让一个玩家重新加入逃走中游戏(其实从未在游戏中的玩家也可以)。
* 命令更新: 增加了 /rfmsettingsquery 命令 (别称 /rsq)，可以查询现有的逃走中游戏配置。
* 命令更新: 向 /start 命令增加了名为 countDownTime 的参数，可以控制猎人释放时间。
* 技术性更新: 重命名并重写了 GameProcess 类的 letHunterCannotMove 方法，现在此方法名为 setHunterNoMoveTime 。
* 技术性更新: 修复了已经结束的游戏进程对应的 GameController 实例仍然可以用 RunForMoney#getGameController 方法得到所造成的可能产生的问题。
* 技术性更新: 修复了更新检查器判断版本号时不严谨的问题。
* 技术性更新: 更新 RFM API 到 v1.2.0 。 **修复了 API 中 RunForMoney#getCoinEarned 方法的返回值的类型与插件本体的方法的返回值的类型不一致的问题。**


## v1.1.12 (2022/2/20)

API 版本: v1.0.1

* 游戏逻辑更新: 废弃自 v1.1.3 以来一直在使用的禁止猎人移动的方式，因为有漏洞 (同时疯狂按空格+前进键可以进行小距离移动)。改用曾经用过的监听 PlayerMoveEvent 的方法。
* 命令更新: 修复了一个潜在问题: 游戏正常运行时执行 /resume 命令会导致命令执行器抛出异常。 ~~(虽然不影响游戏进程)~~
* 命令更新: 对一些命令进行了改进。
* 技术性更新: 更新 RFM API 到 v1.0.1 版本。
* 技术性更新: 内部代码一行调用 snw.rfm.game.GameProcess#resume 方法的代码用 snw.rfm.game.GameController#resume 方法替代。
* 技术性更新: 将一些可以存储玩家实例的对象改为存储玩家名。

## v1.1.11 (2022/2/19)

特以此版本，献礼即将闭幕的北京冬奥会，希望运动健儿们能再拼一把，再创辉煌！

API 版本: v1.0.0

* 游戏逻辑更新: 修复了一个潜在问题: 猎人暂停卡将要生效时未正确处理速度效果。
* 游戏逻辑更新: 修复了一个潜在问题: 时间倒流只做到了 B币 倒流，现在才算是真正的时间倒流。
* 技术性更新: 删除了 snw.rfm 包下的 package-info.java ，因为其无意义。
* 技术性更新: **将 API 和 插件本体分割。API 现在是一个单独的工件。** pom.xml 的 repositories 项增加了我的 Maven 私服的引用。
* 技术性更新: 除部分必须覆盖的类外，删除了所有与 API 重复的类。
* 技术性更新: 将 snw.rfm.Util.registerCommand 方法移至 snw.rfm.RunForMoney 插件主类，并设为 private 方法。

## v1.1.10 (2022/2/14)

* 技术性更新: 增加了 GameController (在 snw.rfm.api 包下) 。可以对游戏进行修改。其实现是 snw.rfm.game.GameController 。
* 技术性更新: 增加了 AlreadyException, AlreadyPausedException, AlreadyRunningException (均在 snw.rfm.api.throwables 包下) 。详情见其 Javadoc 。
* 技术性更新: AlreadyRegisteredException 现在是 AlreadyException 的子类。

## v1.1.9 (2022/2/11)

* 游戏逻辑更新: 修复了 v1.1.8 更新弃权镐处理带来的两个严重问题。
* 技术性更新: 移除了 snw.rfm.api.events.BaseEvent 类，修复了一个潜在问题。

## v1.1.8 (2022/2/10)

* 游戏逻辑更新: 移除了 v1.1.6 分割弃权镐处理后 EventProcessor 类内的仍未移除的弃权镐处理函数。
* 技术性更新: **重命名 物品事件注册表 为 道具注册表。**
* 技术性更新: 为 snw.rfm.ItemRegistry 类增加了 registerItem, getRegisteredItemNames, getRegisteredItemByName, getProcessorByName 方法。用法见 Wiki 。
* 技术性更新: 此插件内置道具的注册改用新方法。
* 技术性更新: 因为内置道具注册方法的改进，废弃并移除了 snw.rfm.RFMItems 类。
* 技术性更新: rfmitem 命令改用 ItemRegistry 的方法。
* 技术性更新: 增加了 snw.rfm.api.throwables 包。
* 技术性更新: 修改了一些 Javadoc 。


## v1.1.7 (2022/2/10)

* 游戏逻辑更新: 修复了向 ItemRegistry 注册监听器时，可能因为物品数量不一致，导致无法查询相应触发器的问题。
* 技术性更新: 将 snw.rfm.Util 类设为 final 类。

## v1.1.6 (2022/2/9)

* 技术性更新: 增加了 更新检查器 。可以配置。 ~~妈妈再也不用担心我用旧版插件了。~~
* 技术性更新: 增加了 物品事件注册表 。开发者可以监听游戏内玩家使用特定物品并作出操作。
* 技术性更新: 增加了 snw.rfm.api 包，面向开发者。
* 技术性更新: 将 snw.rfm.events 包迁移进 snw.rfm.api 包。
* 技术性更新: 将弃权镐的处理从 snw.rfm.processor.EventProcessor 类分割到 snw.rfm.processor.ExitingPickaxeProcessor 类。
* 技术性更新: 将 snw.rfm.processor.HunterPauseCardItemProcessor 类重命名为 HunterPauseCardProcessor 。

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

## v1.1.3 (2022/2/3)

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
