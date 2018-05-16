#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-spring-cloud
## Description: 启动 spring cloud
## Author:      wujiaqi
## Created:     2018-5-4
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                               ### bin 目录

cd ..
SERVICE_DIR=`pwd`                           ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf          ### service 配置文件
LIB_STAREPO_DIR=${SERVICE_DIR}/starepo/lib  ### starepo lib
LOG_DIR=${SERVICE_DIR}/logs                 ### LOG 目录
LOG_FILE=$LOG_DIR/start-spring-cloud.log

cd ..
OBJECT_DIR=`pwd`                            ### RealTimeFaceCompare 目录
CONF_FILE=${OBJECT_DIR}/conf/project-conf.properties
OBJECT_LIB_DIR=${OBJECT_DIR}/lib            ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#


#####################################################################
# 函数名: start
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################

function start()
{
     case $class in
         [sS][tT][aA][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/starepo/bin/start-spring-cloud-starepo.sh;;
         [fF][aA][cC][eE] )
             sh ${SERVICE_DIR}/face/bin/start-spring-cloud-face.sh;;
         [dD][yY][nN][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/dynrepo/bin/start-spring-cloud-dynrepo.sh;;
         [dD][eE][vV][iI][cC][eE] )
             sh ${SERVICE_DIR}/device/bin/start-spring-cloud-device.sh;;
         [cC][lL][uU][sS][tT][eE][rR][iI][nN][gG] )
             sh ${SERVICE_DIR}/clustering/bin/start-spring-cloud-clustering.sh;;
         [aA][dD][dD][rR][eE][sS][sS] )
             sh ${SERVICE_DIR}/address/bin/start-spring-cloud-address.sh;;
         [vV][iI][sS][uU][aA][lL] )
             sh ${SERVICE_DIR}/visual/bin/start-spring-cloud-address.sh;;
     esac
}

#####################################################################
# 函数名: start_all
# 描述: 启动所有Application
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_all()
{
    SPRING_CLASS=$(grep spring_cloud_service_classes ${CONF_FILE}|cut -d '=' -f2)
    spring_arr=(${SPRING_CLASS//;/ })
    for spring_class in ${spring_arr[@]}
    do
        echo "启动${spring_class}................."  | tee  -a  $LOG_FILE
        class=${spring_class}
        start
    done

    check_spring_pid=$(ps -ef | grep check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
    if [ -n "${check_spring_pid}" ];then
        echo "check_spring_cloud is exit,nothing to do " | tee -a ${LOG_FILE}
    else
        echo "check_spring_cloud is not exit, just to start check_spring_cloud."   | tee -a ${LOG_FILE}
        nohup sh ${BIN_DIR}/check-spring-cloud.sh &
    fi
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
    start_all
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  ${LOG_FILE}
echo ""  | tee  -a  ${LOG_FILE}
echo "==================================================="  | tee -a ${LOG_FILE}
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  ${LOG_FILE}
echo "开始配置service中的conf文件"                       | tee  -a  ${LOG_FILE}
main

set +x