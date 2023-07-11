# Plugiix

A paper plugin made for my personnal minecraft servers.


## Features

### Commands
- /feed [pseudo]
- /setfood [pseudo] <foodlevel>
- /heal [pseudo]
- /sethealth [pseudo] <health>
- /tpa <pseudo>

To see a full list of availible commands and their usage, please run :

```minecraft
/help plugiix
```
## Build from source code

You first need to install mvn (maven cli).

```bash
  mvn clean install
```

This will create a directory named "target" containing the jar file of the compiled plugin.
Just move this file to your minecraft server "/plugins" directory and run the command :

```minecraft
/rl confirm
```

To check if the plugin is correcty installed, run the command

```minecraft
/plugins
```

You should see Plugiix in the list.
## Authors

- [@matiix310](https://www.github.com/matiix310)


## Acknowledgements

 - [Paper MC](https://papermc.io/)
