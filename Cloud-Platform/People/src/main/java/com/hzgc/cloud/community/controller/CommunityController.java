package com.hzgc.cloud.community.controller;

import com.hzgc.cloud.community.model.Count;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.cloud.community.param.*;
import com.hzgc.cloud.community.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Api(value = "/community", tags = "社区实有人口库服务")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @ApiOperation(value = "小区迁入迁出人口统计（实有人口首页）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_COUNT_NEW_OUT, method = RequestMethod.POST)
    public ResponseResult<List<NewAndOutPeopleCounVO>> countCommunityNewAndOutPeople(@RequestBody NewAndOutPeopleCountDTO param) {
        if (param == null) {
            log.error("Start count community new and out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start count community new and out people, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        if (param.getCommunityIdList() == null || param.getCommunityIdList().size() == 0) {
            log.error("Start count community new and out people, but community id list is null");
            return ResponseResult.init(null);
        }
        if (param.getMonth() == null || param.getMonth().length() != 6) {
            log.error("Start count community new and out people, but month error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份有误,请检查！");
        }
        if (param.getStart() < 0) {
            log.error("Start count community new and out people, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (param.getLimit() <= 0) {
            log.error("Start count community new and out people, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start count community new and out people, param is :" + JacksonUtil.toJson(param));
        List<NewAndOutPeopleCounVO> voList = communityService.countCommunityNewAndOutPeople(param);
        log.info("Count community new and out people successfully!");
        return ResponseResult.init(voList);
    }

    @ApiOperation(value = "小区迁入迁出人口查询（实有人口展示）", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_OUT, method = RequestMethod.POST)
    public ResponseResult<NewAndOutPeopleSearchVO> searchCommunityNewAndOutPeople(@RequestBody NewAndOutPeopleSearchDTO param) {
        if (param == null) {
            log.error("Start search community new and out people, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community new and out people, but region is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start search community new and out people, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        if (param.getMonth() == null || param.getMonth().length() != 6) {
            log.error("Start search community new and out people, but month error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份有误,请检查！");
        }
        if (param.getType() != 0 && param.getType() != 1) {
            log.error("Start search community new and out people, but type error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类型有误,请检查！");
        }
        if (param.getTypeStatus() != 0 && param.getTypeStatus() != 1 && param.getTypeStatus() != 2) {
            log.error("Start search community new and out people, but type status error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询类别状态有误,请检查！");
        }
        if (param.getStart() < 0) {
            log.error("Start search community new and out people, but start < 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "起始行数不能小于0,请检查！");
        }
        if (param.getLimit() <= 0) {
            log.error("Start search community new and out people, but limit <= 0");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "分页行数不能小于或等于0,请检查！");
        }
        log.info("Start search community new and out people, param is :" + JacksonUtil.toJson(param));
        NewAndOutPeopleSearchVO vo = communityService.searchCommunityNewAndOutPeople(param);
        log.info("Search community new and out people successfully!");
        return ResponseResult.init(vo, vo != null ? vo.getTotalNum() : 0);
    }

    @ApiOperation(value = "小区迁入迁出人口信息查询", response = CommunityPeopleInfoVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_INFO, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleInfoVO> searchCommunityPeopleInfo(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search community people info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        log.info("Start search community people info, people id is:" + peopleId);
        CommunityPeopleInfoVO vo = communityService.searchCommunityPeopleInfo(peopleId);
        log.info("Search community people info successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "身份证精准查询", response = CommunityPeopleInfoVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_PEOPLE_INFO_IDCARD, method = RequestMethod.GET)
    public ResponseResult<CommunityPeopleInfoVO> searchPeopleByIdCard(String idCard) {
        if (idCard == null) {
            log.info("Start search people by idCard, but idCard is null");
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询身份证不能为空,请检查!");
        }
        log.info("Start search people by idCard, idCard is:" + idCard);
        CommunityPeopleInfoVO people = communityService.searchPeopleByIdCard(idCard);
        log.info("Search people by idCard successfully, result:" + JacksonUtil.toJson(people));
        return ResponseResult.init(people);
    }

    @ApiOperation(value = "小区迁入人口抓拍详情", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_NEW_CAPTURE, method = RequestMethod.POST)
    public ResponseResult<CaptureDetailsVO> searchCommunityNewPeopleCaptureDetails(@RequestBody CaptureDetailsDTO param) {
        if (param.getPeopleId() == null) {
            log.error("Start search community new people capture details, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start search community new people capture details, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start search community new people capture details, but mouth is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询月份不能为空,请检查！");
        }
        log.info("Start search community new people capture details, param is:" + JacksonUtil.toJson(param));
        CaptureDetailsVO vo = communityService.searchCommunityNewPeopleCaptureDetails(param);
        log.info("Search community new people capture details successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "小区迁出人口最后抓拍查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_SEARCH_OUT_LAST_CAPTURE, method = RequestMethod.GET)
    public ResponseResult<OutPeopleLastCaptureVO> searchCommunityOutPeopleLastCapture(String peopleId) {
        if (StringUtils.isBlank(peopleId)) {
            log.error("Start search community people last capture info, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        log.info("Start search community people last capture info, people id is:" + peopleId);
        OutPeopleLastCaptureVO vo = communityService.searchCommunityOutPeopleLastCapture(peopleId);
        log.info("Search community people last capture info successfully");
        return ResponseResult.init(vo);
    }

    @ApiOperation(value = "小区确认迁出操作", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_OUT, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmOut(@RequestBody AffirmOperationDTO param) {
        if (param == null) {
            log.error("Start affirm out operation, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getPeopleId())) {
            log.error("Start affirm out operation, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start affirm out operation, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start affirm out operation, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "月份不能为空,请检查！");
        }
        if (param.getIsconfirm() != 2 && param.getIsconfirm() != 3) {
            log.error("Start affirm out operation, but isconfirm is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "确认标签有误,请检查！");
        }
        log.info("Start affirm out operation, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmOut(param);
        log.info("Affirm out operation successfully");
        return ResponseResult.init(integer);
    }

    @ApiOperation(value = "小区确认迁入操作", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmNew(@RequestBody AffirmOperationDTO param) {
        if (param == null) {
            log.error("Start affirm new operation, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getPeopleId())) {
            log.error("Start affirm new operation, but people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start affirm new operation, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "小区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getMonth())) {
            log.error("Start affirm new operation, but month is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "月份不能为空,请检查！");
        }
        if (param.getIsconfirm() != 2 && param.getIsconfirm() != 3) {
            log.error("Start affirm new operation, but isconfirm is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "确认标签有误,请检查！");
        }
        if (param.getFlag() != 0 && param.getFlag() != 1) {
            log.error("Start affirm new operation, but flag is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "迁入状态有误,请检查！");
        }
        log.info("Start affirm new operation, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmNew(param);
        log.info("Affirm new operation successfully");
        return ResponseResult.init(integer);
    }

    @ApiOperation(value = "小区迁入新增人员操作", response = PeopleCaptureVO.class)
    @RequestMapping(value = BigDataPath.COMMUNITY_AFFIRM_NEW_HANDLE, method = RequestMethod.POST)
    public ResponseResult<Integer> communityAffirmNew_newPeopleHandle(@RequestBody NewPeopleHandleDTO param) {
        if (param == null) {
            log.error("Start new people handle, but param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getNewPeopleId())) {
            log.error("Start new people handle, but new people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "新增人员ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getSearchPeopleId())) {
            log.error("Start new people handle, but search people id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询人员ID不能为空,请检查！");
        }
        if (param.getCommunityId() == null) {
            log.error("Start new people handle, but community id is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "社区ID不能为空,请检查！");
        }
        if (StringUtils.isBlank(param.getCapturePicture())) {
            log.error("Start new people handle, but capture picture is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "新增人员原图不能为空,请检查！");
        }
        log.info("Start new people handle, param is:" + JacksonUtil.toJson(param));
        Integer integer = communityService.communityAffirmNew_newPeopleHandle(param);
        if (integer == 1) {
            log.info("New people handle successfully");
        }
        return ResponseResult.init(integer);
    }
}
