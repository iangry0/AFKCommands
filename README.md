# AFKCommands
SpigotMC AFKCommands Plugin



 
Hooks into Essentials and EssentialsX
You can add customisable commands and messages once a player goes AFK and comes back from AFK so you could have a player get sent to a certain warp or a server, or even kicked from the server when they go AFK, it's all customisable.
[SPOILER="Example Config"]
#Player placeholder is %player%
#The commands and message can be customised to your liking and can do anything
command-on-afk: 'warp limbo %player%'
disable-message: false
message-to-player-on-afk: '&c%player% you have been sent to the &4Limbo &cwarp for being idle'
command-on-unafk: 'spawn %player%'
message-to-player-on-unafk: '&c%player% you have been sent to the &4Spawn'
checkUpdates: true
[/SPOILER]

/AFK - Default Essentials AFK command or automatic once players put into AFK by Essentials

/AFKCommand - Shows plugin info such as version and developer

/AFKCommand Reload - Reloads configuration - afkcommand.reload

Please leave your plugin suggestions on the Discussion page or contribute yourself on github GitHub - iangry0/AFKCommands: SpigotMC AFKCommands Plugin

Please leave your server IP in the reviews and I'll add it here.
