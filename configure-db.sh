#!/bin/bash

sleep 30
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P $MSSQL_SA_PASSWORD -Q "CREATE DATABASE mssql"
