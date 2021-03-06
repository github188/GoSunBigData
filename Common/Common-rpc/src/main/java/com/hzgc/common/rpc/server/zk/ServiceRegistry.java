package com.hzgc.common.rpc.server.zk;

import com.google.common.base.Strings;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.common.rpc.util.ZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务注册工具，会将当前服务相关信息注册在zk节点上面
 */
public class ServiceRegistry extends ZookeeperClient {
//    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    private static Logger logger = Logger.getLogger(ServiceRegistry.class);

    public ServiceRegistry(String zkAddress, Constant constant) {
        super(zkAddress, constant);
    }

    public CuratorFramework getConnect(){
        return zkClient;
    }

    public void register(String data, Constant constant) {
        if (!Strings.isNullOrEmpty(data)) {
            String flag = createZnode(data, constant);
            if (!Strings.isNullOrEmpty(flag)) { //&& data.contains(constant.getNodePath())
                logger.info("Create znode " + flag + " successfull");
            } else if(constant.isExitIfFaild()){
                logger.info("Create znode " + flag + " faild. The constant is " + constant.toString());
                System.exit(0);
            }
        }
    }


    private String createZnode(String data, Constant constant) {
        try {
            Map<String, String> param = constant.getParam();
            if(param == null){
                param = new HashMap<>();
            }
            param.put("address", data);
            return zkClient.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(constant.getCreateMode())
                    .forPath(constant.getNodePath(), GsonUtil.mapToJson(param).getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
