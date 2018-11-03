package com.hzgc.service.white.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "白名单库模糊查询出参")
@Data
public class SearchDispatchWhiteVO implements Serializable {

    @ApiModelProperty(value = "总条数")
    private int total;

    @ApiModelProperty(value = "布控名称列表")
    private List<DispatchWhiteVO> dispatch_list;


}
