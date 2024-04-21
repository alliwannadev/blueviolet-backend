#!/bin/bash

docker-compose down -v
rm -rf ./mysql/primary/data/*
rm -rf ./mysql/secondary/data/*
docker-compose build
docker-compose up -d

until docker exec mysql_primary sh -c 'export MYSQL_PWD=123456; mysql -u root -e ";"'
do
    echo "mysql_primary 데이터베이스에 연결 중 ..."
    sleep 5
done

PRIVILEGE_STATEMENT='CREATE USER "blueviolet_secondary_user"@"%" IDENTIFIED BY "blueviolet_secondary_pwd"; GRANT REPLICATION SLAVE ON *.* TO "blueviolet_secondary_user"@"%"; FLUSH PRIVILEGES;'
docker exec mysql_primary sh -c "export MYSQL_PWD=123456; mysql -u root -e '$PRIVILEGE_STATEMENT'"

until docker-compose exec mysql_secondary sh -c 'export MYSQL_PWD=123456; mysql -u root -e ";"'
do
    echo "mysql_secondary 데이터베이스에 연결 중 ..."
    sleep 5
done

MASTER_STATUS=`docker exec mysql_primary sh -c 'export MYSQL_PWD=123456; mysql -u root -e "SHOW MASTER STATUS"'`
CURRENT_LOG=`echo $MASTER_STATUS | awk '{print $6}'`
CURRENT_POS=`echo $MASTER_STATUS | awk '{print $7}'`

START_SECONDARY_STATEMENT="CHANGE MASTER TO MASTER_HOST='mysql_primary',MASTER_USER='blueviolet_secondary_user',MASTER_PASSWORD='blueviolet_secondary_pwd',MASTER_LOG_FILE='$CURRENT_LOG',MASTER_LOG_POS=$CURRENT_POS; START SLAVE;"
START_SECONDARY_CMD='export MYSQL_PWD=123456; mysql -u root -e "'
START_SECONDARY_CMD+="$START_SECONDARY_STATEMENT"
START_SECONDARY_CMD+='"'

docker exec mysql_secondary sh -c "$START_SECONDARY_CMD"
docker exec mysql_secondary sh -c "export MYSQL_PWD=123456; mysql -u root -e 'SHOW SLAVE STATUS \G'"

echo "초기 데이터 설정 중 ..."
docker cp ./mysql/data_settings.sql mysql_primary:/data_settings.sql
DATA_SETTINGS_STATEMENT='USE blueviolet; SOURCE data_settings.sql;'
docker exec mysql_primary sh -c "export MYSQL_PWD=123456; mysql -u root -e '$DATA_SETTINGS_STATEMENT'"