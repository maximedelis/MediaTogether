#!/bin/bash

# Start SQL Server
/opt/mssql/bin/configure-db.sh &
/opt/mssql/bin/sqlservr
