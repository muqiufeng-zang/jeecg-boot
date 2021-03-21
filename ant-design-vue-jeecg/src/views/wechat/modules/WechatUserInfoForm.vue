<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['mobile']" placeholder="请输入手机号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['email']" placeholder="请输入邮箱"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="微信昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['nickName']" placeholder="请输入微信昵称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="固定号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['telephone']" placeholder="请输入固定号码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['avatar']" placeholder="请输入头像"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="用户状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['userStatus']" placeholder="请输入用户状态" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="微信端id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wxAppId']" placeholder="请输入微信端id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="0 微信；1 小程序" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['authType']" placeholder="请输入0 微信；1 小程序" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="微信union_id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['unionId']" placeholder="请输入微信union_id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="微信open_id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['appOpenId']" placeholder="请输入微信open_id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="公众号的open ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['subscribeOpenId']" placeholder="请输入公众号的open ID"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="用户是否关注公众号,1-关注,0,取消关注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['subscribeState']" placeholder="请输入用户是否关注公众号,1-关注,0,取消关注" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="语言" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['language']" placeholder="请输入语言"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['city']" placeholder="请输入城市"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['sex']" placeholder="请输入性别" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="性别描述信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sexdesc']" placeholder="请输入性别描述信息"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="省份" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['province']" placeholder="请输入省份"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="国家" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['country']" placeholder="请输入国家"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['remark']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="关注时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['subscribetime']" placeholder="请输入关注时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="组id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['groupid']" placeholder="请输入组id" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标签" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['tagids']" placeholder="请输入标签"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="关注的渠道来源" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['subscribescene']" placeholder="请输入关注的渠道来源"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="二维码扫码场景" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qrscene']" placeholder="请输入二维码扫码场景"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="二维码扫码场景描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qrscenestr']" placeholder="请输入二维码扫码场景描述"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'WechatUserInfoForm',
    components: {
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
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
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/wechat/wechatUserInfo/add",
          edit: "/wechat/wechatUserInfo/edit",
          queryById: "/wechat/wechatUserInfo/queryById"
        }
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
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'mobile','email','nickName','telephone','avatar','userStatus','wxAppId','authType','unionId','appOpenId','subscribeOpenId','subscribeState','language','city','sex','sexdesc','province','country','remark','subscribetime','groupid','tagids','subscribescene','qrscene','qrscenestr'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'mobile','email','nickName','telephone','avatar','userStatus','wxAppId','authType','unionId','appOpenId','subscribeOpenId','subscribeState','language','city','sex','sexdesc','province','country','remark','subscribetime','groupid','tagids','subscribescene','qrscene','qrscenestr'))
      },
    }
  }
</script>