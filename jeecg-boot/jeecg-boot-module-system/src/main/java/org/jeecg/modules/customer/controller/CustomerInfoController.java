package org.jeecg.modules.customer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.entity.CustomerInfo;
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
import org.jeecg.modules.customer.vo.CustomerInfoPage;
import org.jeecg.modules.customer.service.ICustomerInfoService;
import org.jeecg.modules.customer.service.ICustomerContactsService;
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
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
@Api(tags="客户信息")
@RestController
@RequestMapping("/customer/customerInfo")
@Slf4j
public class CustomerInfoController {
	@Autowired
	private ICustomerInfoService customerInfoService;
	@Autowired
	private ICustomerContactsService customerContactsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customerInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户信息-分页列表查询")
	@ApiOperation(value="客户信息-分页列表查询", notes="客户信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CustomerInfo customerInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CustomerInfo> queryWrapper = QueryGenerator.initQueryWrapper(customerInfo, req.getParameterMap());
		Page<CustomerInfo> page = new Page<CustomerInfo>(pageNo, pageSize);
		IPage<CustomerInfo> pageList = customerInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerInfoPage
	 * @return
	 */
	@AutoLog(value = "客户信息-添加")
	@ApiOperation(value="客户信息-添加", notes="客户信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CustomerInfoPage customerInfoPage) {
		CustomerInfo customerInfo = new CustomerInfo();
		BeanUtils.copyProperties(customerInfoPage, customerInfo);
		customerInfoService.saveMain(customerInfo, customerInfoPage.getCustomerContactsList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerInfoPage
	 * @return
	 */
	@AutoLog(value = "客户信息-编辑")
	@ApiOperation(value="客户信息-编辑", notes="客户信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CustomerInfoPage customerInfoPage) {
		CustomerInfo customerInfo = new CustomerInfo();
		BeanUtils.copyProperties(customerInfoPage, customerInfo);
		CustomerInfo customerInfoEntity = customerInfoService.getById(customerInfo.getId());
		if(customerInfoEntity==null) {
			return Result.error("未找到对应数据");
		}
		customerInfoService.updateMain(customerInfo, customerInfoPage.getCustomerContactsList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户信息-通过id删除")
	@ApiOperation(value="客户信息-通过id删除", notes="客户信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		customerInfoService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户信息-批量删除")
	@ApiOperation(value="客户信息-批量删除", notes="客户信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.customerInfoService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户信息-通过id查询")
	@ApiOperation(value="客户信息-通过id查询", notes="客户信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerInfo customerInfo = customerInfoService.getById(id);
		if(customerInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(customerInfo);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户联系人通过主表ID查询")
	@ApiOperation(value="客户联系人主表ID查询", notes="客户联系人-通主表ID查询")
	@GetMapping(value = "/queryCustomerContactsByMainId")
	public Result<?> queryCustomerContactsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<CustomerContacts> customerContactsList = customerContactsService.selectByMainId(id);
		return Result.OK(customerContactsList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param customerInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CustomerInfo customerInfo) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<CustomerInfo> queryWrapper = QueryGenerator.initQueryWrapper(customerInfo, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<CustomerInfo> queryList = customerInfoService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
      if(oConvertUtils.isEmpty(selections)) {
          customerInfoList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          customerInfoList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<CustomerInfoPage> pageList = new ArrayList<CustomerInfoPage>();
      for (CustomerInfo main : customerInfoList) {
          CustomerInfoPage vo = new CustomerInfoPage();
          BeanUtils.copyProperties(main, vo);
          List<CustomerContacts> customerContactsList = customerContactsService.selectByMainId(main.getId());
          vo.setCustomerContactsList(customerContactsList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "客户信息列表");
      mv.addObject(NormalExcelConstants.CLASS, CustomerInfoPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户信息数据", "导出人:"+sysUser.getRealname(), "客户信息"));
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
              List<CustomerInfoPage> list = ExcelImportUtil.importExcel(file.getInputStream(), CustomerInfoPage.class, params);
              for (CustomerInfoPage page : list) {
                  CustomerInfo po = new CustomerInfo();
                  BeanUtils.copyProperties(page, po);
                  customerInfoService.saveMain(po, page.getCustomerContactsList());
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
