FROM ubuntu/chiselled-jre:edge
LABEL maintainer="alexzionx@gmail.com"
LABEL server_name="LampaChat_server"
LABEL server_version="0.1"
LABEL description="This is self-hosted simple text client-server messenger \
with logining/registration and database (sqlite3)"
LABEL "FAQ and info"="https://github.com/Alexzionx/lampa_chat-server"
LABEL "client app"="https://github.com/Alexzionx/lampa_chat-client"
LABEL "JDBC ver."="sqlite-jdbc-3.7.2"
COPY . .
VOLUME ["./base","./config"]
EXPOSE 30305
EXPOSE 30306
ENTRYPOINT ["java", "-jar", "/LampaChat_server.jar"]
