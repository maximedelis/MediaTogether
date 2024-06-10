# MediaTogether

A Spring app to consume content with your friends.

## Status

In dev...

## Getting started (local dev)

### Lazy run

```bash
sudo docker compose up
```

Add `-d` to run in detached mode

### Requirements

- Docker: `sudo apt install docker.io`
- Pull the Official MS SQL image: [MS guide](https://learn.microsoft.com/fr-fr/sql/linux/quickstart-install-connect-docker?view=sql-server-linux-ver16&pivots=cs1-bash)
- Make sure OpenJDK 21 is installed on your machine (IntelliJ can do it for you)

### Start a DB instance

Run the following command to start a MSSQL instance on your machine:

```bash
sudo docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=<YourStrong@Passw0rd>" \
   -p 1433:1433 --name mssql --hostname mssql \
   -d \
   mcr.microsoft.com/mssql/server:2022-latest
```

Check that your instance is running using `sudo docker ps -a`

Create a database:

```bash
sudo docker exec -it mssql /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "<YourStrong@Passw0rd>" -Q "CREATE DATABASE mssql"
```

### Create a .env file

```bash
nano .env
```

Add the following lines:

```bash
DB_URL=jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=mssql
DB_USER=SA
DB_PASSWORD=<YourStrong@Passw0rd>
DB_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
HOST_IP=<Your_IP>

HIBERNATE_DIALECT=org.hibernate.dialect.SQLServerDialect # or your driver if not using MSSQL
```

### Misc

Check error logs: `docker exec -t mssql cat /var/opt/mssql/log/errorlog | grep connection`
