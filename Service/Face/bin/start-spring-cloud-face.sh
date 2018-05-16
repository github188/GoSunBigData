#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-spring-cloud-face
## Description: 启动 spring cloud
## Author:      wujiaqi
## Created:     2018-5-7
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                  ### bin 目录

cd ..
FACE_DIR=`pwd`                              ### face 目录
LIB_FACE_DIR=${FACE_DIR}/lib              ### face lib
CONF_FACE_DIR=${FACE_DIR}/conf            ### face 配置文件目录
LOG_DIR=${FACE_DIR}/logs                    ### LOG 目录
LOG_FILE=$LOG_DIR/start-spring-cloud.log
cd ..
SERVICE_DIR=`pwd`                              ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf             ### service 配置文件

cd ..
OBJECT_DIR=`pwd`                               ### RealTimeFaceCompare 目录
CONF_DIR=${OBJECT_DIR}/conf/project-conf.properties
OBJECT_LIB_DIR=${OBJECT_DIR}/lib               ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`
LIB_FACE=`ls ${FACE_DIR}| grep ^face-[0-9].[0-9].[0-9].jar$`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi
#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud()
{
    LIB_JARS=`ls $LIB_FACE_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_FACE_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    # java -classpath $CONF_FACE_DIR:$LIB_JARS com.hzgc.service.face.FaceApplication  | tee -a  ${LOG_FILE}
    java -Xbootclasspath/a:$LIB_JARS -jar ${LIB_FACE} | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: update_properties
# 描述: 修改jar包中的配置文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function update_properties()
{
    # cd ${OBJECT_LIB_DIR}
    ## 获取需要修改的配置文件
    # jar xf ${LIB_FACE} faceApplication.properties

    # PROPERTIES_DIR=faceApplication.properties
    ## 获取新端口号
    # FACE_SERVER_PORT=${grep "face.server.port" ${CONF_DIR} | cut -d '=' -f2}
    ## 获取旧端口号
    # old_port=`grep server.port ${PROPERTIES_DIR}`
    ## 替换
    # sed -i "s#^${old_port}#server.port=${FACE_SERVER_PORT}#g" ${PROPERTIES_DIR}

    # FACE_APPLICATION_NAME=${grep "face.spring.application.name" ${CONF_DIR} | cut -d '=' -f2}
    # old_name=`grep spring.application.name ${PROPERTIES_DIR}`
    # sed -i "s#^${old_name}#spring.application.name={FACE_APPLICATION_NAME}#g" ${PROPERTIES_DIR}

    ## 更新jar包中的配置文件
    cd ${FACE_DIR}
    jar uf ${LIB_FACE} conf/faceApplication.properties
    # rm -f ${PROPERTIES_DIR}
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
    update_properties
    start_spring_cloud
}



## 打印时间
echo ""  | tee  -a  ${LOG_FILE}
echo ""  | tee  -a  ${LOG_FILE}
echo "==================================================="  | tee -a ${LOG_FILE}
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  ${LOG_FILE}
main

set +x