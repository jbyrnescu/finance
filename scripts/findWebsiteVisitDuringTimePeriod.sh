#!/bin/bash

#FIVE_DAYS_AGO=`date -v-5d "+%Y-%m-%d %H:%M:%S"`
#TODAY=`date "+%Y-%m-%d %H:%M:%S"`

BEGINDATE=$1
ENDDATE=$2

if [ "$1" == "" ];
then
    echo "Usage: findWebsitedurintTimePeriod.sh <begin date> <end date>"
    echo "Date format: YYYY-MM-DD HH:MM:SS"
fi

echo "finding history between dates ${FIVE_DAYS_AGO} and ${TODAY}"

echo "sqlite3 ~/Library/Safari/History.db \"select datetime(history_visits.visit_time + 978307200, 'unixepoch', 'localtime') as date, url from history_visits join history_items on history_items.id = history_visits.history_item \
where date between 'BEGINDATE' and '${ENDDATE}' \
;" 

#sqlite3 ~/Library/Safari/History.db "select datetime(history_visits.visit_time + 978307200, 'unixepoch', 'localtime') as date, url from history_visits join history_items on history_items.id = history_visits.history_item where date between '2024-07-16 00:00:00' and '2024-07-20 00:00:00';"

sqlite3 ~/Library/Safari/History.db "select datetime(history_visits.visit_time + 978307200, 'unixepoch', 'localtime') as date, url from history_visits join history_items on history_items.id = history_visits.history_item \
where date between '$BEGINDATE' and '$ENDDATE' \
order by date;"

