<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-item label="运单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['waybillNo']" placeholder="请输入运单号" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="运单状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['waybillSate', validatorRules.waybillSate]" :trigger-change="true" dictCode="waybill_state_dict" placeholder="请选择运单状态" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="运单发货人" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="waybillConsignorTable.loading"
          :columns="waybillConsignorTable.columns"
          :dataSource="waybillConsignorTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="运单收货人" :key="refKeys[1]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[1]"
          :loading="waybillConsigneeTable.loading"
          :columns="waybillConsigneeTable.columns"
          :dataSource="waybillConsigneeTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="运单状态通知人" :key="refKeys[2]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[2]"
          :loading="waybillNoticeTable.loading"
          :columns="waybillNoticeTable.columns"
          :dataSource="waybillNoticeTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
      <a-tab-pane tab="运单通知历史" :key="refKeys[3]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[3]"
          :loading="waybillNoticeHistoryTable.loading"
          :columns="waybillNoticeHistoryTable.columns"
          :dataSource="waybillNoticeHistoryTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'WaybillInfoForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JDictSelectTag,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          waybillSate: {
            initialValue:"",
            rules: [
            ]
          },
        },
        refKeys: ['waybillConsignor', 'waybillConsignee', 'waybillNotice', 'waybillNoticeHistory', ],
        tableKeys:['waybillConsignor', 'waybillConsignee', 'waybillNotice', 'waybillNoticeHistory', ],
        activeKey: 'waybillConsignor',
        // 运单发货人
        waybillConsignorTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '发货人',
              key: 'consignorId',
              type: FormTypes.sel_search,
              dictCode:"sys_depart,depart_name,id",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        // 运单收货人
        waybillConsigneeTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '收货人',
              key: 'consigneeId',
              type: FormTypes.sel_search,
              dictCode:"sys_depart,depart_name,id",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        // 运单状态通知人
        waybillNoticeTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '接受通知人',
              key: 'userId',
              type: FormTypes.sel_search,
              dictCode:"sys_user,realname,id",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '通知类型',
              key: 'notifyType',
              type: FormTypes.select,
              dictCode:"notify_type_dict",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        // 运单通知历史
        waybillNoticeHistoryTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '通知人',
              key: 'notifyUserId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '通知状态',
              key: 'notifyState',
              type: FormTypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/waybillInfo/waybillInfo/add",
          edit: "/waybillInfo/waybillInfo/edit",
          queryById: "/waybillInfo/waybillInfo/queryById",
          waybillConsignor: {
            list: '/waybillInfo/waybillInfo/queryWaybillConsignorByMainId'
          },
          waybillConsignee: {
            list: '/waybillInfo/waybillInfo/queryWaybillConsigneeByMainId'
          },
          waybillNotice: {
            list: '/waybillInfo/waybillInfo/queryWaybillNoticeByMainId'
          },
          waybillNoticeHistory: {
            list: '/waybillInfo/waybillInfo/queryWaybillNoticeHistoryByMainId'
          },
        }
      }
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：false流程表单 true普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      addBefore(){
        this.form.resetFields()
        this.waybillConsignorTable.dataSource=[]
        this.waybillConsigneeTable.dataSource=[]
        this.waybillNoticeTable.dataSource=[]
        this.waybillNoticeHistoryTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'waybillNo','waybillSate')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.waybillConsignor.list, params, this.waybillConsignorTable)
          this.requestSubTableData(this.url.waybillConsignee.list, params, this.waybillConsigneeTable)
          this.requestSubTableData(this.url.waybillNotice.list, params, this.waybillNoticeTable)
          this.requestSubTableData(this.url.waybillNoticeHistory.list, params, this.waybillNoticeHistoryTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          waybillConsignorList: allValues.tablesValue[0].values,
          waybillConsigneeList: allValues.tablesValue[1].values,
          waybillNoticeList: allValues.tablesValue[2].values,
          waybillNoticeHistoryList: allValues.tablesValue[3].values,
        }
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          })
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'waybillNo','waybillSate'))
     },

    }
  }
</script>

<style scoped>
</style>