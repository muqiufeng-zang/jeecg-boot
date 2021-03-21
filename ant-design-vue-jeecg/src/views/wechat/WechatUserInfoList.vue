<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('微信用户表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wechat-user-info-modal ref="modalForm" @ok="modalFormOk"></wechat-user-info-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WechatUserInfoModal from './modules/WechatUserInfoModal'

  export default {
    name: 'WechatUserInfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WechatUserInfoModal
    },
    data () {
      return {
        description: '微信用户表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'手机号',
            align:"center",
            dataIndex: 'mobile'
          },
          {
            title:'邮箱',
            align:"center",
            dataIndex: 'email'
          },
          {
            title:'微信昵称',
            align:"center",
            dataIndex: 'nickName'
          },
          {
            title:'固定号码',
            align:"center",
            dataIndex: 'telephone'
          },
          {
            title:'头像',
            align:"center",
            dataIndex: 'avatar'
          },
          {
            title:'用户状态',
            align:"center",
            dataIndex: 'userStatus'
          },
          {
            title:'微信端id',
            align:"center",
            dataIndex: 'wxAppId'
          },
          {
            title:'0 微信；1 小程序',
            align:"center",
            dataIndex: 'authType'
          },
          {
            title:'微信union_id',
            align:"center",
            dataIndex: 'unionId'
          },
          {
            title:'微信open_id',
            align:"center",
            dataIndex: 'appOpenId'
          },
          {
            title:'公众号的open ID',
            align:"center",
            dataIndex: 'subscribeOpenId'
          },
          {
            title:'用户是否关注公众号,1-关注,0,取消关注',
            align:"center",
            dataIndex: 'subscribeState'
          },
          {
            title:'语言',
            align:"center",
            dataIndex: 'language'
          },
          {
            title:'城市',
            align:"center",
            dataIndex: 'city'
          },
          {
            title:'性别',
            align:"center",
            dataIndex: 'sex'
          },
          {
            title:'性别描述信息',
            align:"center",
            dataIndex: 'sexdesc'
          },
          {
            title:'省份',
            align:"center",
            dataIndex: 'province'
          },
          {
            title:'国家',
            align:"center",
            dataIndex: 'country'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'关注时间',
            align:"center",
            dataIndex: 'subscribetime'
          },
          {
            title:'组id',
            align:"center",
            dataIndex: 'groupid'
          },
          {
            title:'标签',
            align:"center",
            dataIndex: 'tagids'
          },
          {
            title:'关注的渠道来源',
            align:"center",
            dataIndex: 'subscribescene'
          },
          {
            title:'二维码扫码场景',
            align:"center",
            dataIndex: 'qrscene'
          },
          {
            title:'二维码扫码场景描述',
            align:"center",
            dataIndex: 'qrscenestr'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/wechat/wechatUserInfo/list",
          delete: "/wechat/wechatUserInfo/delete",
          deleteBatch: "/wechat/wechatUserInfo/deleteBatch",
          exportXlsUrl: "/wechat/wechatUserInfo/exportXls",
          importExcelUrl: "wechat/wechatUserInfo/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'mobile',text:'手机号',dictCode:''})
        fieldList.push({type:'string',value:'email',text:'邮箱',dictCode:''})
        fieldList.push({type:'string',value:'nickName',text:'微信昵称',dictCode:''})
        fieldList.push({type:'string',value:'telephone',text:'固定号码',dictCode:''})
        fieldList.push({type:'string',value:'avatar',text:'头像',dictCode:''})
        fieldList.push({type:'int',value:'userStatus',text:'用户状态',dictCode:''})
        fieldList.push({type:'string',value:'wxAppId',text:'微信端id',dictCode:''})
        fieldList.push({type:'int',value:'authType',text:'0 微信；1 小程序',dictCode:''})
        fieldList.push({type:'string',value:'unionId',text:'微信union_id',dictCode:''})
        fieldList.push({type:'string',value:'appOpenId',text:'微信open_id',dictCode:''})
        fieldList.push({type:'string',value:'subscribeOpenId',text:'公众号的open ID',dictCode:''})
        fieldList.push({type:'int',value:'subscribeState',text:'用户是否关注公众号,1-关注,0,取消关注',dictCode:''})
        fieldList.push({type:'string',value:'language',text:'语言',dictCode:''})
        fieldList.push({type:'string',value:'city',text:'城市',dictCode:''})
        fieldList.push({type:'int',value:'sex',text:'性别',dictCode:''})
        fieldList.push({type:'string',value:'sexdesc',text:'性别描述信息',dictCode:''})
        fieldList.push({type:'string',value:'province',text:'省份',dictCode:''})
        fieldList.push({type:'string',value:'country',text:'国家',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'subscribetime',text:'关注时间',dictCode:''})
        fieldList.push({type:'int',value:'groupid',text:'组id',dictCode:''})
        fieldList.push({type:'string',value:'tagids',text:'标签',dictCode:''})
        fieldList.push({type:'string',value:'subscribescene',text:'关注的渠道来源',dictCode:''})
        fieldList.push({type:'string',value:'qrscene',text:'二维码扫码场景',dictCode:''})
        fieldList.push({type:'string',value:'qrscenestr',text:'二维码扫码场景描述',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>