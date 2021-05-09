package org.jeecg.modules.waybillInfo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.vo.WaybillInfoPage;
import org.jeecg.modules.waybillInfo.service.IWaybillInfoService;
import org.jeecg.modules.waybillInfo.service.IWaybillConsignorService;
import org.jeecg.modules.waybillInfo.service.IWaybillConsigneeService;
import org.jeecg.modules.waybillInfo.service.IWaybillNoticeService;
import org.jeecg.modules.waybillInfo.service.IWaybillNoticeHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date:   2021-05-09
 * @Version: V1.0
 */
@Api(tags="运单信息表")
@RestController
@RequestMapping("/waybillInfo/waybillInfo")
@Slf4j
public class WaybillInfoController {
	@Autowired
	private IWaybillInfoService waybillInfoService;
	@Autowired
	private IWaybillConsignorService waybillConsignorService;
	@Autowired
	private IWaybillConsigneeService waybillConsigneeService;
	@Autowired
	private IWaybillNoticeService waybillNoticeService;
	@Autowired
	private IWaybillNoticeHistoryService waybillNoticeHistoryService;

	/**
	 * 分页列表查询
	 *
	 * @param waybillInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "运单信息表-分页列表查询")
	@ApiOperation(value="运单信息表-分页列表查询", notes="运单信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WaybillInfo waybillInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WaybillInfo> queryWrapper = QueryGenerator.initQueryWrapper(waybillInfo, req.getParameterMap());
		Page<WaybillInfo> page = new Page<WaybillInfo>(pageNo, pageSize);
		IPage<WaybillInfo> pageList = waybillInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param waybillInfoPage
	 * @return
	 */
	@AutoLog(value = "运单信息表-添加")
	@ApiOperation(value="运单信息表-添加", notes="运单信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WaybillInfoPage waybillInfoPage) {
		WaybillInfo waybillInfo = new WaybillInfo();
		BeanUtils.copyProperties(waybillInfoPage, waybillInfo);
		waybillInfoService.saveMain(waybillInfo, waybillInfoPage.getWaybillConsignorList(),waybillInfoPage.getWaybillConsigneeList(),waybillInfoPage.getWaybillNoticeList(),waybillInfoPage.getWaybillNoticeHistoryList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param waybillInfoPage
	 * @return
	 */
	@AutoLog(value = "运单信息表-编辑")
	@ApiOperation(value="运单信息表-编辑", notes="运单信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WaybillInfoPage waybillInfoPage) {
		WaybillInfo waybillInfo = new WaybillInfo();
		BeanUtils.copyProperties(waybillInfoPage, waybillInfo);
		WaybillInfo waybillInfoEntity = waybillInfoService.getById(waybillInfo.getId());
		if(waybillInfoEntity==null) {
			return Result.error("未找到对应数据");
		}
		waybillInfoService.updateMain(waybillInfo, waybillInfoPage.getWaybillConsignorList(),waybillInfoPage.getWaybillConsigneeList(),waybillInfoPage.getWaybillNoticeList(),waybillInfoPage.getWaybillNoticeHistoryList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单信息表-通过id删除")
	@ApiOperation(value="运单信息表-通过id删除", notes="运单信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		waybillInfoService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "运单信息表-批量删除")
	@ApiOperation(value="运单信息表-批量删除", notes="运单信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.waybillInfoService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单信息表-通过id查询")
	@ApiOperation(value="运单信息表-通过id查询", notes="运单信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WaybillInfo waybillInfo = waybillInfoService.getById(id);
		if(waybillInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(waybillInfo);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单发货人通过主表ID查询")
	@ApiOperation(value="运单发货人主表ID查询", notes="运单发货人-通主表ID查询")
	@GetMapping(value = "/queryWaybillConsignorByMainId")
	public Result<?> queryWaybillConsignorListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WaybillConsignor> waybillConsignorList = waybillConsignorService.selectByMainId(id);
		return Result.OK(waybillConsignorList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单收货人通过主表ID查询")
	@ApiOperation(value="运单收货人主表ID查询", notes="运单收货人-通主表ID查询")
	@GetMapping(value = "/queryWaybillConsigneeByMainId")
	public Result<?> queryWaybillConsigneeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WaybillConsignee> waybillConsigneeList = waybillConsigneeService.selectByMainId(id);
		return Result.OK(waybillConsigneeList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单状态通知人通过主表ID查询")
	@ApiOperation(value="运单状态通知人主表ID查询", notes="运单状态通知人-通主表ID查询")
	@GetMapping(value = "/queryWaybillNoticeByMainId")
	public Result<?> queryWaybillNoticeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WaybillNotice> waybillNoticeList = waybillNoticeService.selectByMainId(id);
		return Result.OK(waybillNoticeList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "运单通知历史通过主表ID查询")
	@ApiOperation(value="运单通知历史主表ID查询", notes="运单通知历史-通主表ID查询")
	@GetMapping(value = "/queryWaybillNoticeHistoryByMainId")
	public Result<?> queryWaybillNoticeHistoryListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WaybillNoticeHistory> waybillNoticeHistoryList = waybillNoticeHistoryService.selectByMainId(id);
		return Result.OK(waybillNoticeHistoryList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param waybillInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaybillInfo waybillInfo) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<WaybillInfo> queryWrapper = QueryGenerator.initQueryWrapper(waybillInfo, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<WaybillInfo> queryList = waybillInfoService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<WaybillInfo> waybillInfoList = new ArrayList<WaybillInfo>();
      if(oConvertUtils.isEmpty(selections)) {
          waybillInfoList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          waybillInfoList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<WaybillInfoPage> pageList = new ArrayList<WaybillInfoPage>();
      for (WaybillInfo main : waybillInfoList) {
          WaybillInfoPage vo = new WaybillInfoPage();
          BeanUtils.copyProperties(main, vo);
          List<WaybillConsignor> waybillConsignorList = waybillConsignorService.selectByMainId(main.getId());
          vo.setWaybillConsignorList(waybillConsignorList);
          List<WaybillConsignee> waybillConsigneeList = waybillConsigneeService.selectByMainId(main.getId());
          vo.setWaybillConsigneeList(waybillConsigneeList);
          List<WaybillNotice> waybillNoticeList = waybillNoticeService.selectByMainId(main.getId());
          vo.setWaybillNoticeList(waybillNoticeList);
          List<WaybillNoticeHistory> waybillNoticeHistoryList = waybillNoticeHistoryService.selectByMainId(main.getId());
          vo.setWaybillNoticeHistoryList(waybillNoticeHistoryList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "运单信息表列表");
      mv.addObject(NormalExcelConstants.CLASS, WaybillInfoPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("运单信息表数据", "导出人:"+sysUser.getRealname(), "运单信息表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<WaybillInfoPage> list = ExcelImportUtil.importExcel(file.getInputStream(), WaybillInfoPage.class, params);
              for (WaybillInfoPage page : list) {
                  WaybillInfo po = new WaybillInfo();
                  BeanUtils.copyProperties(page, po);
                  waybillInfoService.saveMain(po, page.getWaybillConsignorList(),page.getWaybillConsigneeList(),page.getWaybillNoticeList(),page.getWaybillNoticeHistoryList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
