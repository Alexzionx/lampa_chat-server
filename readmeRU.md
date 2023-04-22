<p align="center">
  <img title="logo" src='https://github.com/Alexzionx/lampa_chat-client/blob/master/assets/images/logo.png?raw=true' />
</p>

## lampa_chat-server

# - Ru|[En](https://github.com/Alexzionx/lampa_chat-server/blob/main/README.md)

**LampaChat** это простой текстовый мессенджер для размещения на своем собственном сервере, с регистрацией и базой данных (sqlite3).

[LampaChat Client APP](https://github.com/Alexzionx/lampa_chat-client/)

# Начало работы
- ### Docker

  LampaChat сервер доступен в docker.
  
  [![Docker Pulls](https://img.shields.io/docker/image-size/alexzionx/lampa_chat-server/8-0.1?style=for-the-badge)](https://hub.docker.com/r/alexzionx/lampa_chat-server)
  
  После запуска будут автоматичесски созданы DATABASE и CONFIG [docker volume](https://docs.docker.com/storage/volumes/) на вашем хосте.
Docker volumes (разделы) позволяют сохранить содежимое (настройки, базаданных) после остановки, перезапуска или удаления контейнера.
- ### Запуск локально
  LampaChat серверу для работы требуется **Java 8** или новее.
  ```
  java -jar LampaChat_server.jar
  ```
- ### Настройка
  В данный момент из настроек доступна блокировка регистрации новых пользователей и изменение портов работы сервера.
  Все настройки сервера производятся в файле /config/options.properties или если вы запустили в докере то в CONFIG [docker volume](https://docs.docker.com/storage/volumes/) имеется файл options.properties, 
  в самом файле есть подсказки для настройки.
