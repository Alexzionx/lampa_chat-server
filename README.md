<p align="center">
  <img title="logo" src='https://github.com/Alexzionx/lampa_chat-client/blob/master/assets/images/logo.png?raw=true' />
</p>

## lampa_chat-server

# - [Ru](https://github.com/Alexzionx/lampa_chat-server/blob/main/readmeRU.md)|En

**LampaChat** it`s self-hosted client-server simple text messenger with registration and database (sqlite3).

  [![](https://img.shields.io/github/last-commit/alexzionx/lampa_chat-client?label=LampaChat%20CLIENT)](https://github.com/Alexzionx/lampa_chat-client)

# Getting started
- ### Docker

  LampaChat server is available in docker.
  
  [![Docker Pulls](https://img.shields.io/docker/image-size/alexzionx/lampa_chat-server/latest?style=for-the-badge)](https://hub.docker.com/r/alexzionx/lampa_chat-server)
  
  After start automatically created a DATABASE and CONFIG [docker volume](https://docs.docker.com/storage/volumes/) on the host machine.
Docker volumes retain their content even when the container is stopped, started, or deleted.
- ### Local start
  LampaChat server requires **Java 8** or latest
  ```
  java -jar LampaChat_server.jar
  ```
- ### Configuration
  At the moment, blocking the registration of new users and changing the ports of the server are available from the settings.
  All server settings in file /config/options.properties or if you run in docker then in [docker volume](https://docs.docker.com/storage/volumes/) there is a file options.properties, there are instructions for setting up.

# Public server
   server adress - **45.141.103.116**
   
   port1 - **30305**
   
   port2 - **30306**
  
  - ### Demo user
    
    login - **demo1**
    
    pass - **demo**
  
  
