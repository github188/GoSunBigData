#!/bin/bash
########################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    create-schedule-job-to-zip.sh
## Description: 将定时任务生成job文件并打包成zip包
## Author:      chenke
## Created:     2018-03-27
#########################################################################
#set -x ##用于调试使用，不用的时候可以注释掉

#----------------------------------------------------------------------#
#                              定义变量                                #
#----------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`   ###bin目录
cd ..
AZKABAN_DIR=`pwd`  ###azkaban目录
LOG_DIR=${AZKABAN_DIR}/logs  ###集群log日志目录
LOG_FILE=${LOG_DIR}/create-schedule-job-to-zip.log  ##log日志文件

cd ../../../../project
PROJECT_DIR=`pwd`
PORJECT_CONF_DIR=${PROJECT_DIR}/conf
PROJECT_CONF_FILE=${PORJECT_CONF_DIR}/project-conf.properties

SCHEMA_FILE="schema-merge-parquet-file.sh"
DISCOVER_FILE="discover.sh"
OFFLINE_FILE="start-face-offline-alarm-job.sh"
DYNAMICSHOW_TABLE="get-dynamicshow-table-run.sh"

DEVICE_RECOGIZE_TABLE="device_recogize_table.sh"
IMSI_BLACKLIST_TABLE="imsi_blacklist_table.sh"
NEWPEOPLE_TABLE="newpeople_table.sh"
FUSION_IMSI_TABLE="fusion_imsi_table.sh"
HOUR_COUNT_TABLE="24hour_count_table.sh"
OUTPEOPLE_TABLE="outpeople_table.sh"

cd ../..
OBJECT_DIR=`pwd`                                 ## 根目录
CLUSTER_BIN_DIR=/opt/GoSunBigData/Cluster/spark/bin

MYSQL=`grep "mysql_host" ${PROJECT_CONF_FILE} | cut -d "=" -f2`
MYSQLIP=${MYSQL##:*}
MYSQLPORT=${MYSQL%%:*}

cd ${AZKABAN_DIR}

sed -i "s#^IP=.*#IP=${MYSQLIP}#g" `grep -r "IP=" ./*.sh | awk -F ":" '{print $1}'`
sed -i "s#^IP=.*#PORT=${MYSQLPORT}#g" `grep -r "PORT=" ./*.sh | awk -F ":" '{print $1}'`

if [[ ! -f "${DEVICE_RECOGIZE_TABLE}"  ]]; then
    echo "the device_recogize_table_one_day.sh is not exist!!!"
    else
    touch device_recogize_table_one_day.job
    echo "type=command" >> device_recogize_table_one_day.job
    echo "command=sh ${AZKABAN_DIR}/${DEVICE_RECOGIZE_TABLE}" >> device_recogize_table_one_day.job
fi

if [[ ! -f "${NEWPEOPLE_TABLE}"  ]]; then
    echo "the newpeople_table_one_month.sh is not exist!!!"
    else
    touch newpeople_table_one_month.job
    echo "type=command" >> newpeople_table_one_month.job
    echo "command=sh ${AZKABAN_DIR}/${NEWPEOPLE_TABLE}" >> newpeople_table_one_month.job
fi

if [[ ! -f "${IMSI_BLACKLIST_TABLE}"  ]]; then
    echo "the imsi_blacklist_table_one_day.sh is not exist!!!"
    else
    touch imsi_blacklist_table_one_day.job
    echo "type=command" >> imsi_blacklist_table_one_day.job
    echo "command=sh ${AZKABAN_DIR}/${IMSI_BLACKLIST_TABLE}" >> imsi_blacklist_table_one_day.job
fi

if [[ ! -f "${FUSION_IMSI_TABLE}"  ]]; then
    echo "the fusion_imsi_table_one_day.sh is not exist!!!"
    else
    touch fusion_imsi_table_one_day.job
    echo "type=command" >> fusion_imsi_table_one_day.job
    echo "command=sh ${AZKABAN_DIR}/${FUSION_IMSI_TABLE}" >> fusion_imsi_table_one_day.job
    echo "dependencies=imsi_blacklist_table_one_day" >> fusion_imsi_table_one_day.job
fi

if [[ ! -f "${HOUR_COUNT_TABLE}"  ]]; then
    echo "the 24hour_count_table_one_day.sh is not exist!!!"
    else
    touch 24hour_count_table_one_day.job
    echo "type=command" >> 24hour_count_table_one_day.job
    echo "command=sh ${AZKABAN_DIR}/${HOUR_COUNT_TABLE}" >> 24hour_count_table_one_day.job
fi

zip device_recogize_table_one_day.zip device_recogize_table_one_day.job
zip fusion_imsi_table_one_day.zip fusion_imsi_table_one_day.job imsi_blacklist_table_one_day.job
zip 24hour_count_table_one_day.zip 24hour_count_table_one_day.job
zip newpeople_table_one_month.zip newpeople_table_one_month.job

cd ${CLUSTER_BIN_DIR}  ##进入cluster的bin目录
mkdir -p schema-parquet-one-hour
if [ ! -f "${SCHEMA_FILE}" ]; then
   echo "The schema-merge-parquet-file.sh is not exist!!!"
else
   touch mid_table-one-hour.job     ##创建mid_table-one-hour.job文件
   echo "type=command" >> mid_table-one-hour.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> mid_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh mid_table" >> mid_table-one-hour.job

   touch person_table-one-hour.job  ##创建person_table-one-hour.job文件
   echo "type=command" >> person_table-one-hour.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> person_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table now" >> person_table-one-hour.job
   echo "dependencies=mid_table-one-hour" >> person_table-one-hour.job

   touch person_table_one-day.job  ##创建person_table_one-day.job文件
   echo "type=command" >> person_table_one-day.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> person_table_one-day.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table before" >> person_table_one-day.job

fi
if [ ! -f "${OFFLINE_FILE}" ]; then
   echo "The start-face-offline-alarm-job.sh is not exist!!!"
else
   touch start-face-offline-alarm-job.job  ##创建离线告警的job文件
   echo "type=command" >> start-face-offline-alarm-job.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> start-face-offline-alarm-job.job
   echo "command=sh \${cluster_home}/start-face-offline-alarm-job.sh" >> start-face-offline-alarm-job.job
fi

if [ ! -f "$DISCOVER_FILE" ]; then
   echo "The discover.sh is not exist!!!"
else
   touch discover-one-day.job       ##创建discover-one-day.job文件
   echo "type=command" >> discover-one-day.job
   echo "cluster_home=${CLUSTER_BIN_DIR}" >> discover-one-day.job
   echo "command=sh \${cluster_home}/discover.sh"  >> discover-one-day.job
fi

cd ${CLUSTER_BIN_DIR}
mv mid_table-one-hour.job person_table-one-hour.job  schema-parquet-one-hour
zip schema-parquet-one-hour.zip schema-parquet-one-hour/*
zip discover-one-day.zip discover-one-day.job
zip person_table_one-day.job.zip person_table_one-day.job
zip start-face-offline-alarm-job_oneday.job.zip start-face-offline-alarm-job.job
rm -rf person_table_one-day.job start-face-offline-alarm-job.job schema-parquet-one-hour discover-one-day.job

cd ${AZKABAN_DIR}
mkdir -p zip
mv ${BIN_DIR}/24hour_count_table_one_day.zip ${BIN_DIR}/device_recogize_table_one_day.zip ${BIN_DIR}/fusion_imsi_table_one_day.zip ${BIN_DIR}/newpeople_table_one_month.zip ${CLUSTER_BIN_DIR}/discover-one-day.zip  ${CLUSTER_BIN_DIR}/schema-parquet-one-hour.zip ${CLUSTER_BIN_DIR}/person_table_one-day.job.zip ${CLUSTER_BIN_DIR}/start-face-offline-alarm-job_oneday.job.zip zip