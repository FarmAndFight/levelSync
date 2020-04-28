# levelSync

levelSync is a spigot plugin which help you to synchronize minecraft vanilla experience between yours servers using MySQL.

__IMPORTANT:__ This plugin do no support player in offline mode, please enable online-mode on bungeecord

## Installation

- Put the .jar file in your spigot (not bungeecord) plugin folder

- restart your server to generate config file, then edit it

- restart server, enjoy !

## Configuration



```YAML
mysql:
  # mysql server hostname or ip adress
  host: "example.com"
  # mysql socket
  port: 3306
  username: "username"
  password: "password"
  # mysql database, you need to create it first
  database: "minecraft"
  # table name, automatically created by the plugin
  table: "playerLevel"
```

## Need help ?

you can discuss my plugin and ask for help here: 
https://www.spigotmc.org/threads/levelsync.434249/
