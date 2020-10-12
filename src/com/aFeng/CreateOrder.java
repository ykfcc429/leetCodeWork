package com.aFeng;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.caucho.services.server.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.sharera.db.dao.TransactionalCallBack;
import com.sharera.gzctesb.service.Resp4Esbcrm;
import com.sharera.imarket3.actions.api.protocol.base.Request;
import com.sharera.imarket3.actions.api.protocol.base.Response;
import com.sharera.imarket3.actions.api.protocol.create.Req4Arrearspay;
import com.sharera.imarket3.actions.api.protocol.create.Req4Broadband;
import com.sharera.imarket3.actions.api.protocol.create.Req4Combined;
import com.sharera.imarket3.actions.api.protocol.create.Req4Goventidentity;
import com.sharera.imarket3.actions.api.protocol.create.Req4Hffpk;
import com.sharera.imarket3.actions.api.protocol.create.Req4Intention;
import com.sharera.imarket3.actions.api.protocol.create.Req4Kdxy;
import com.sharera.imarket3.actions.api.protocol.create.Req4Kdzz;
import com.sharera.imarket3.actions.api.protocol.create.Req4Miscellaneous;
import com.sharera.imarket3.actions.api.protocol.create.Req4Mobile;
import com.sharera.imarket3.actions.api.protocol.create.Req4Monthlyfee;
import com.sharera.imarket3.actions.api.protocol.create.Req4Prepaidgift;
import com.sharera.imarket3.actions.api.protocol.create.Req4Replacepackage;
import com.sharera.imarket3.actions.api.protocol.create.Req4Retractphone;
import com.sharera.imarket3.actions.api.protocol.create.Req4ThdOrderRealname;
import com.sharera.imarket3.actions.api.protocol.create.Req4Yffpk;
import com.sharera.imarket3.actions.api.protocol.create.ReqGiftdelivery;
import com.sharera.imarket3.actions.api.service.ArrearspayService;
import com.sharera.imarket3.actions.api.service.BroadbandService;
import com.sharera.imarket3.actions.api.service.CombinedService;
import com.sharera.imarket3.actions.api.service.GiftdeliveryService;
import com.sharera.imarket3.actions.api.service.GoventidentityService;
import com.sharera.imarket3.actions.api.service.HffpkService;
import com.sharera.imarket3.actions.api.service.IntentionService;
import com.sharera.imarket3.actions.api.service.KdxyService;
import com.sharera.imarket3.actions.api.service.KdzzService;
import com.sharera.imarket3.actions.api.service.MiscellaneousService;
import com.sharera.imarket3.actions.api.service.MobileService;
import com.sharera.imarket3.actions.api.service.MonthlyfeeService;
import com.sharera.imarket3.actions.api.service.OrderRealnameService;
import com.sharera.imarket3.actions.api.service.PrepaidgiftService;
import com.sharera.imarket3.actions.api.service.ProductService;
import com.sharera.imarket3.actions.api.service.ReplacePackageService;
import com.sharera.imarket3.actions.api.service.RetractphoneService;
import com.sharera.imarket3.actions.api.service.YffpkService;
import com.sharera.imarket3.actions.api.util.BeanUtils;
import com.sharera.imarket3.actions.api.util.SignUtils;
import com.sharera.imarket3.actions.base.APIConstant;
import com.sharera.imarket3.actions.base.Constant;
import com.sharera.imarket3.actions.base.NotifyConstant;
import com.sharera.imarket3.actions.base.OrderStatus;
import com.sharera.imarket3.actions.web.basic.UserThreadLocal;
import com.sharera.imarket3.actions.web.callback.CallBack;
import com.sharera.imarket3.actions.web.callback.OrderChangeImpl;
import com.sharera.imarket3.actions.web.order.DeliveryService;
import com.sharera.imarket3.actions.web.order.OrderCreate;
import com.sharera.imarket3.actions.web.order.combined.CombinedUtil;
import com.sharera.imarket3.actions.web.order.create.CreateService;
import com.sharera.imarket3.actions.web.order.create.ParameterCheckService;
import com.sharera.imarket3.actions.web.order.create.QualityCheckService;
import com.sharera.imarket3.actions.web.order.o2ollb.OrderToO2ollb;
import com.sharera.imarket3.actions.web.order.ordertoai.Order2AiService;
import com.sharera.imarket3.actions.web.order.replacepackage.ReplacepackageUtil;
import com.sharera.imarket3.auth.WebUser;
import com.sharera.imarket3.buss.BlackUserBuss;
import com.sharera.imarket3.buss.GzctOrderSMSBuss;
import com.sharera.imarket3.buss.ImarketGzctpay;
import com.sharera.imarket3.buss.OcrVerifyBuss;
import com.sharera.imarket3.db.handler.Services;
import com.sharera.imarket3.db.vo.Dictionary;
import com.sharera.imarket3.db.vo.ThdAcceptFlow;
import com.sharera.imarket3.db.vo.ThdBusinessTag;
import com.sharera.imarket3.db.vo.ThdJobNumber;
import com.sharera.imarket3.db.vo.ThdOrder;
import com.sharera.imarket3.db.vo.ThdOrderCustomerinfo;
import com.sharera.imarket3.db.vo.ThdOrderDeliveryinfo;
import com.sharera.imarket3.db.vo.ThdOrderProcess;
import com.sharera.imarket3.db.vo.ThdOrderRealname;
import com.sharera.imarket3.db.vo.ThdPermission;
import com.sharera.imarket3.db.vo.ThdProduct;
import com.sharera.imarket3.db.vo.ThdProductProperties;
import com.sharera.imarket3.db.vo.ThdProductTemplate;
import com.sharera.imarket3.db.vo.ThdUser;
import com.sharera.imarket3.db.vo.ThdUserPermission;
import com.sharera.imarket3.newimarket.NewSystemInterface;
import com.sharera.imarket3.util.JedisService;
import com.sharera.imarket3.util.OrderUtils;
import com.sharera.imarket3.util.sms.GzctFnSmsService;
import com.sharera.imarket3.webservice.client.ESBWebServices;
import com.sharera.imarket3.webservice.server.GzctwmsService;
import com.sharera.service.IService;
import com.sharera.util.FastJSONUtils;
import com.sharera.util.IpUtil;
import com.sharera.util.StateUtil;
import com.sharera.util.log.RealnameLog;


public class CreateOrder extends ActionSupport implements ServletRequestAware, CallBack {

	private static final long serialVersionUID = 1L;

	@Value("${project.imarketnew.url}")
	private String url;
	
	private String json;
	
	@Resource
	private NewSystemInterface newSystemInterface;

	@Resource
	private Order2AiService order2AiService;
	@Resource
	private BroadbandService broadbandService;
	@Resource
	private OrderRealnameService orderRealnameService;
	@Resource
	private ProductService productService;
	@Resource
	private PrepaidgiftService prepaidgiftService;
	@Resource
	private MobileService mobileService;
	@Resource
	private KdxyService kdxyService;
	@Resource
	private KdzzService kdzzService;
	@Resource
	private CombinedService combinedService;
	@Resource 
	private MonthlyfeeService monthlyfeeService;
	@Resource 
	private IntentionService intentionService;

	@Resource
	private HffpkService hffpkService;
	@Resource
	private MiscellaneousService miscellaneousService;
	@Resource
	private YffpkService yffpkService;
	@Resource
	private GiftdeliveryService giftdeliveryService;
	@Resource
	private OrderChangeImpl orderChangeImpl;
	@Resource 
	private GoventidentityService goventidentityService;
	@Resource 
	private RetractphoneService retractphoneService;
	@Resource
	private ReplacePackageService replacePackageService;
	@Resource
	private OrderToO2ollb orderToO2ollb;
	@Resource
	private ArrearspayService arrearspayService;
	@Resource
	private ParameterCheckService parameterCheckService;
	@Resource
	private CreateService createService;
	@Resource
	private QualityCheckService qualityCheckService;
	@Resource
	private GzctwmsService gzctwmsService;
	@Resource
	private CombinedUtil combinedUtil;
	@Resource
	private ReplacepackageUtil replacepackageUtil;
	@Resource
	private ESBWebServices esbWebServices;
	
	private File sfzzmFile;
	private String sfzzmFileContentType;
	private String sfzzmFileFileName;

	private File sfzbmFile;
	private String sfzbmFileContentType;
	private String sfzbmFileFileName;

	private File sfzscFile;
	private String sfzscFileContentType;
	private String sfzscFileFileName;

	private File sfzsc2File;
	private String sfzsc2FileContentType;
	private String sfzsc2FileFileName;

	private File yyzzFile;
	private String yyzzFileContentType;
	private String yyzzFileFileName;

	private File thzhxxFile;
	private String thzhxxFileContentType;
	private String thzhxxFileFileName;
	
	private File file;//附件
	private String fileFileName;//附件名称

	@Resource
	private BlackUserBuss blackUserBuss;
	@Resource
	private DeliveryService deliveryService;

	@Resource
	private ImarketGzctpay imarketGzctpay;

//	@Resource
//	private PushSubscription pushSubscription;

	@Resource
	private OrderUtils orderUtils;
	
	@Resource
	private GzctFnSmsService gzctFnSmsService;
	
	@Resource
	private GzctOrderSMSBuss gzctOrderSMSBuss;
	
	@Resource
	private OcrVerifyBuss ocrVerifyBuss;
	
	@Resource
	private JedisService jedisService;

	private static final Logger logger = Logger.getLogger(CreateOrder.class);


	private String result = "";// 订单保存结果 失败返回空；需要直推仓储成功返回蓝单号,失败返回空
	private Req4Prepaidgift req4Prepaidgift2 = new Req4Prepaidgift();
	private Response resp2 = new Response();

	@Override
	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload", params = { "allowedExtensions",
			"jpg,gif,jpeg,bmp,png", "maximumSize", "10485760" }), @InterceptorRef("extStack") })
	public String execute() throws Exception{

		String ip = request.getRemoteAddr();
		String XForwardedFor = request.getHeader("x-forwarded-for");
		
		Map<String, String[]> map = request.getParameterMap();
		if (map == null) {
			Response resp = new Response();
			resp.setStatus(APIConstant.PARAMS_NOTFOUND);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.PARAMS_NOTFOUND_MSG);
			logger.error("Ip地址：" + ip+"，x-forwarded-for："+XForwardedFor+"，"+APIConstant.PARAMS_NOTFOUND_MSG);
			Map<String, String> result = BeanUtils.getMapFromObject(resp, null);
			json = FastJSONUtils.getJsonHelper().toJSONString(result);
			return StateUtil.json;
		}
		TreeMap<String, String[]> properties = new TreeMap<String, String[]>();
		for (String key : map.keySet()) {
			properties.put(key, new String[] { StringEscapeUtils.unescapeHtml(map.get(key)[0]) });// escape
		}
		final Request request = BeanUtils.getObjectFromMap(properties, Request.class);
		logger.info("[CREATE_ORDER]["+request.getUserName()+"]下单业务标识:" + request.getService()+",时间戳："+request.getTimeStamp());
		
		if (StringUtils.isBlank(request.getUserName())) {// 用户名
			Response resp = new Response();
			resp.setStatus(APIConstant.PARTNER_CODE_NOTFOUND);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.PARTNER_CODE_NOTFOUND_MSG);
			logger.error("Ip地址：" + ip+"，x-forwarded-for："+XForwardedFor+"，"+APIConstant.PARTNER_CODE_NOTFOUND_MSG);
			json = FastJSONUtils.getJsonHelper().toJSONString(resp);
			return StateUtil.json;
		}
		logger.info("[CREATE_ORDER]下单帐号："+request.getUserName()+"，Ip地址：" + ip+"，x-forwarded-for："+XForwardedFor);
		
		if (StringUtils.isBlank(request.getService())) {// 接口标识
			Response resp = new Response();
			resp.setStatus(APIConstant.SERVICE_NOTFOUND);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.SERVICE_NOTFOUND_MSG);
			logger.error(APIConstant.SERVICE_NOTFOUND_MSG);
			Map<String, String> result = BeanUtils.getMapFromObject(resp, null);
			json = FastJSONUtils.getJsonHelper().toJSONString(result);
			return StateUtil.json;
		}
		if (StringUtils.isBlank(request.getTimeStamp())) {// 时间戳
			Response resp = new Response();
			resp.setStatus(APIConstant.TIMESTAMP_NOTFOUND);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.TIMESTAMP_NOTFOUND_MSG);
			logger.error(APIConstant.TIMESTAMP_NOTFOUND_MSG);
			json = FastJSONUtils.getJsonHelper().toJSONString(resp);
			return StateUtil.json;
		}
		if (SignUtils.checkTimeStamp(request.getTimeStamp())) {// 验证时间戳
			Response resp = new Response();
			resp.setStatus(APIConstant.TIMESTAMP_ERROR);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.TIMESTAMP_ERROR_MSG);
			logger.error("Ip地址：" + ip+"，x-forwarded-for："+XForwardedFor+"，"+APIConstant.TIMESTAMP_ERROR_MSG);
			json = FastJSONUtils.getJsonHelper().toJSONString(resp);
			return StateUtil.json;
		}
		
		
		
		final ThdUser user = Services.getThdUserService().createHelper().getUsername().Eq(request.getUserName())
				.getEnabled().Eq(true).getApiStatus().Eq(true).limit(1).uniqueResult();
		if (user == null) {// 用户不存在
			Response resp = new Response();
			resp.setStatus(APIConstant.PARTNER_DISTRIBUTOR_NOTFOUND);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.PARTNER_DISTRIBUTOR_NOTFOUND_MSG);
			logger.error(APIConstant.PARTNER_DISTRIBUTOR_NOTFOUND_MSG);
			json = FastJSONUtils.getJsonHelper().toJSONString(resp);
			return StateUtil.json;
		}
		
        if(user!=null){
        	Boolean checkIp = user.getCheckIp();
    		//读取thd_user表的check_ip字段，如果字段为0，则不需要检验。如果为1，则开始检验
    		//0为false 1为true
    		if(checkIp==null||checkIp){
    			HttpServletRequest request2 = ServletActionContext.getRequest(); 
    			//获取ip地址
    			String ipAddr = IpUtil.getIpAddr(request2);
    			Dictionary dictionary = Services.getDictionaryService().createHelper().getKeyName().Eq("public.valid.ip").limit(1).uniqueResult();
    			if(dictionary!=null){
    				String keyValue = dictionary.getKeyValue();
    				if(StringUtils.isBlank(keyValue)){
    					Response resp = new Response();
						resp.setStatus(APIConstant.IP_WRONGFUL_ERROE);
						resp.setOrderNumber("");
						resp.setMsg(APIConstant.IP_WRONGFUL_ERROE_MSG);
						logger.error("账号IP不合法!");
						json = FastJSONUtils.getJsonHelper().toJSONString(resp);
						return StateUtil.json;
    				}
    				boolean ipcheck = IpUtil.ipcheck(ipAddr, keyValue);
    				//如果客户的真实IP在public.valid.ip清单里，则校验通过,否者继续校验用户信息表的ip
    				if(!ipcheck){                   //绑定的ip列表
    					String accessIpList = user.getAccessIpList();
    					if(StringUtils.isBlank(accessIpList)){
        					Response resp = new Response();
    						resp.setStatus(APIConstant.IP_WRONGFUL_ERROE);
    						resp.setOrderNumber("");
    						resp.setMsg(APIConstant.IP_WRONGFUL_ERROE_MSG);
    						logger.error("账号IP不合法!");
    						json = FastJSONUtils.getJsonHelper().toJSONString(resp);
    						return StateUtil.json;
        				}              //校验ip是否在本系统存在
    					boolean ipcheck2 = IpUtil.ipcheck(ipAddr, accessIpList);
    					if(!ipcheck2){
    						Response resp = new Response();
    						resp.setStatus(APIConstant.IP_WRONGFUL_ERROE);
    						resp.setOrderNumber("");
    						resp.setMsg(APIConstant.IP_WRONGFUL_ERROE_MSG);
    						logger.error("账号IP不合法!");
    						json = FastJSONUtils.getJsonHelper().toJSONString(resp);
    						return StateUtil.json;
    					}
    					
    				}
    				
    			}
    		}
        }
		// 验证签名
		if (!SignUtils.verify(user.getApiToken(), request.getSign(), properties, new String[] { "sign" },
				APIConstant.CHARSET_GBK)) {
			Response resp = new Response();
			resp.setStatus(APIConstant.MEMBER_SING_ERROR);
			resp.setOrderNumber("");
			resp.setMsg(APIConstant.MEMBER_SING_ERROR_MSG);
			logger.error(APIConstant.MEMBER_SING_ERROR_MSG);
			json = FastJSONUtils.getJsonHelper().toJSONString(resp);
			return StateUtil.json;
		}

		// 业务统一处理方法（LZW只是将原本放这里的代码搬到了下面createOrder方法里了，如遇到问题请咨询小蔡）
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		logger.info("[CREATE_ORDER] params map = " + gson.toJson(properties));
		Response resp = createOrder(properties, request, user);

		// 新起线程关联穗易付订单
		if (resp != null && resp.getOrderNumber() != null) {
			imarketGzctpay.setGzctpayOrder(resp.getOrderNumber());
		}
		json = FastJSONUtils.getJsonHelper().toJSONString(resp);
		return StateUtil.json;
	}

	// 业务统一处理方法
	public Response createOrder(Map<String, String[]> properties, final Request request, final ThdUser user) throws Exception {
		
		//获取分销商
		ThdUser sysAgentUser = null;	
		//杂项甩单
		if(APIConstant.ORDER_MISCELLANEOUS_CREATE.equals(request.getService())){

//			final Req4Miscellaneous req4Miscellaneous = BeanUtils.getObjectFromMap(properties, Req4Miscellaneous.class);
//			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Miscellaneous.getUserName()).limit(1)
//					.uniqueResult();
//			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {
//
//				@Override
//				public Object execute(IService arg0) {
//
//					Response resp = new Response();
//					try {
//						resp = miscellaneousService.order2(req4Miscellaneous);
//						resp2.setOrderNumber(resp.getOrderNumber());
//					} catch (Exception e) {
//						logger.error("[CreateOrder][execute] 生成杂项甩单订单异常，" + e.getMessage(), e);
//						resp.setStatus(APIConstant.INTERFACE_ERROR);
//						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
//					}
//					return resp;
//				}
//
//			});
			final Req4Miscellaneous req4Miscellaneous = BeanUtils.getObjectFromMap(properties, Req4Miscellaneous.class);
			req4Miscellaneous.setProductCategory("杂项甩单");
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Miscellaneous.getUserName()).limit(1)
					.uniqueResult();
			//生成对象  关联实名不在这个方法  不要点进去看了
			long times1 = System.currentTimeMillis();
			Response resp = miscellaneousService.order2(req4Miscellaneous);
			long times2 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][杂项甩单][生成],耗时:"+(times2-times1)+"毫秒.");
			
			//校验参数
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times3 = System.currentTimeMillis();
				try{
				    resp = parameterCheckService.checkOrderData(resp.getOrder(), Constant.BUSINESS_MISCELLANEOUS);
				}catch(RuntimeException e){
					logger.error("[CreateOrder][createOrder]校验参数异常：",e);
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times4 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][杂项甩单][参数检验],耗时:"+(times4-times3)+"毫秒.");
			}
			
			//保存对象
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times5 = System.currentTimeMillis();
				try{
					resp =createService.saveOrder(resp.getOrder(),sysAgentUser);
				}catch(Exception e){
					logger.info("[CreateOrder][createOrder]保存对象异常："+e.getMessage());
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times6 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][杂项甩单][保存],耗时:"+(times6-times5)+"毫秒.");
			}
			
			//释放上锁的来源单号、外部订单号
			long times7 = System.currentTimeMillis();
			if(resp != null 
					&& sysAgentUser != null 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Miscellaneous.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Miscellaneous.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			//启动校验线程
			qualityCheckService.run(resp.getOrderNumber());
			long times8 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][杂项甩单][返回前],耗时:"+(times8-times7)+"毫秒.");
			return resp;
		}

		//预付费派卡业务
		if(APIConstant.ORDER_YFFPK_CREATE.equals(request.getService())){

			final Req4Yffpk req4Yffpk = BeanUtils.getObjectFromMap(properties, Req4Yffpk.class);
			//订单关联的分销商账号，如果为空则使用下单账号关联订单
			if(StringUtils.isNotBlank(req4Yffpk.getAgentName())){
				sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Yffpk.getAgentName()).getEnabled().Eq(true).limit(1).uniqueResult();		
			}else{
				sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Yffpk.getUserName()).getEnabled().Eq(true).limit(1).uniqueResult();
			}

			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {

					Response resp = new Response();
					try {
						resp = yffpkService.order(req4Yffpk, sfzzmFile, sfzzmFileFileName, sfzbmFile, sfzbmFileFileName,
								sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
								thzhxxFileFileName);
					} catch (Exception e) {
						logger.error("[CreateOrder][execute] 生成预付费派卡订单异常，" + e.getMessage(), e);
						resp.setStatus(APIConstant.INTERFACE_ERROR);
						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
					}
					return resp;
				}

			});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Yffpk.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Yffpk.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}


			if ("000001".equals(resp.getStatus())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
//				ThdOrderRealname orderRealname = Services.getThdOrderRealnameService().createHelper().getThdOrder().Eq(to).getSmrzType().Eq(Constant.SMRZ_TYPE_YFFPKSM).limit(1).uniqueResult();
//				if(OrderUtils.checkMonthlyfee(orderRealname)){
					
					orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
					//推单到AI受理助手
					order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);
//				}else{
//					to.setStatus(OrderStatus.REALNAME_NOTSIGN);
//				    Services.getThdOrderService().update(to);
//				    OrderUtils.saveChange(to, "已实名待签约","预付费派卡业务实名需要签约", "系统",null);
//				    orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
//				}
			}

			if("000002".equals(resp.getStatus()) || "000003".equals(resp.getStatus())
					|| "000004".equals(resp.getStatus()) || "000005".equals(resp.getStatus())){
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;

		}
		// 移动后付费派卡业务
		if (APIConstant.ORDER_HFFPK_CREATE.equals(request.getService())) {
			final Req4Hffpk req4Hffpk = BeanUtils.getObjectFromMap(properties, Req4Hffpk.class);
			//订单关联的分销商账号，如果为空则使用下单账号关联订单
			if(StringUtils.isNotBlank(req4Hffpk.getAgentName())){
				sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Hffpk.getAgentName()).getEnabled().Eq(true).limit(1).uniqueResult();		
			}else{
				sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Hffpk.getUserName()).getEnabled().Eq(true).limit(1).uniqueResult();
			}
			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {

					Response resp = new Response();
					try {
						resp = hffpkService.order(req4Hffpk, sfzzmFile, sfzzmFileFileName, sfzbmFile, sfzbmFileFileName,
								sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
								thzhxxFileFileName);
					} catch (Exception e) {
						logger.error("[CreateOrder][execute] 生成后付费派卡订单异常，" + e.getMessage(), e);
						resp.setStatus(APIConstant.INTERFACE_ERROR);
						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
					}
					return resp;
				}

			});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Hffpk.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Hffpk.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			if ("000001".equals(resp.getStatus())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
//				ThdOrderRealname orderRealname = Services.getThdOrderRealnameService().createHelper().getThdOrder().Eq(to).getSmrzType().Eq(Constant.SMRZ_TYPE_HFFPKSM).limit(1).uniqueResult();
//				if(OrderUtils.checkMonthlyfee(orderRealname)){
					//
				
					
					
					ThdProduct product = Services.getThdProductService().createHelper()
							.getThdOrder().Eq(to).uniqueResult();
					//天翼销售商企甩单调二次受理接口，其它推ai
					if(product.getSkuCode().equals("1127")){
//						ThdProductProperties productProperties = Services.getThdProductPropertiesService().createHelper().getThdProduct().Eq(product).getName().Eq("properties_业务号码").uniqueResult();
						Resp4Esbcrm resp4Esbcrm = esbWebServices.getFunc2100(to.getOrderNum(),req4Hffpk.getPhone());
						if(resp4Esbcrm!=null){
							logger.info("[CREATE_ORDER][Func2100][" + to.getOrderNum()
							+ "]二次受理接口返回报文：" + FastJSONUtils.getJsonHelper().toJSONString(resp4Esbcrm));
							if (StringUtils.isNotBlank(resp4Esbcrm.getResultCode())
        							&& "0".equals(resp4Esbcrm.getResultCode())) {
								ThdOrderRealname tho = Services.getThdOrderRealnameService().createHelper()
										.getPhone().Eq(req4Hffpk.getPhone()).getThdOrder().Eq(to)
										.uniqueResult();
								if (tho != null) {
									tho.setRealnameActivateTime(new Date());
									tho.setRealnameActivate(Constant.PRODUCT_ACTIVE_SUCCESS);
									Services.getThdOrderRealnameService().update(tho);
								}
								to.setStatus(OrderStatus.ARCHIVE);
								Services.getThdOrderService().update(to);
								OrderUtils.saveChange(to, OrderStatus.ORDER_STATES.get(to.getStatus()), "号码["+req4Hffpk.getPhone()+"]激活成功", "系统", null);
								orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
							}else{
								ThdOrderRealname tho = Services.getThdOrderRealnameService().createHelper()
										.getPhone().Eq(req4Hffpk.getPhone()).getThdOrder().Eq(to)
										.uniqueResult();
								if (tho != null) {
									tho.setRealnameActivate(Constant.PRODUCT_ACTIVE_FAILURE);
									Services.getThdOrderRealnameService().update(tho);
								}
								to.setStatus(OrderStatus.WAIT_ACCEPT);
								Services.getThdOrderService().update(to);
								OrderUtils.saveChange(to,"激活号码",
										req4Hffpk.getPhone() + " 该号码激活失败 "+resp4Esbcrm.getResultMessage(), "系统", null);
								orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
							}		
						}else{
							logger.info("[CREATE_ORDER][Func2100]二次受理返回结果为null！激活失败!orderNum=" + to.getOrderNum());
							ThdOrderRealname tho = Services.getThdOrderRealnameService().createHelper()
									.getPhone().Eq(req4Hffpk.getPhone()).getThdOrder().Eq(to)
									.uniqueResult();
							if (tho != null) {
								tho.setRealnameActivate("激活失败");
								Services.getThdOrderRealnameService().update(tho);
							}
							to.setStatus(OrderStatus.WAIT_ACCEPT);
							Services.getThdOrderService().update(to);
							OrderUtils.saveChange(to,"激活号码",
									req4Hffpk.getPhone() + " 该号码激活失败 ", "系统", null);
							orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
						}
					}else{
						orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
						//推单到AI受理助手
						order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);
					}
//				}else{
//					to.setStatus(OrderStatus.REALNAME_NOTSIGN);
//				    Services.getThdOrderService().update(to);
//				    OrderUtils.saveChange(to, "已实名待签约","后付费派卡业务实名需要签约", "系统",null);
//				    orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
//				}
			}

			if("000002".equals(resp.getStatus()) || "000003".equals(resp.getStatus())
					|| "000004".equals(resp.getStatus()) || "000005".equals(resp.getStatus())){
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
			}
			
//			if(StringUtils.isNotBlank(resp.getOrderNumber())){
//				/**
//			 	 * 推送消息到消息中心
//			 	 */
//				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
//						.uniqueResult();
//				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
//			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		// 单宽带下单业务
		if (APIConstant.ORDER_BROADBAND_CREATE.equals(request.getService())) {
			final Req4Broadband req4Broadband = BeanUtils.getObjectFromMap(properties, Req4Broadband.class);
			req4Broadband.setProductCategory("单宽带");
			// 获取当前登录用户
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Broadband.getUserName()).limit(1)
					.uniqueResult();

			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {

					Response resp = new Response();
					try {
						resp = broadbandService.order(req4Broadband, sfzzmFile, sfzzmFileFileName, sfzbmFile,
								sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
								thzhxxFileFileName);
					} catch (Exception e) {
						logger.error("[CreateOrder][execute] 生成单宽带订单异常，" + e.getMessage(), e);
						resp.setStatus(APIConstant.INTERFACE_ERROR);
						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
					}
					return resp;
				}

			});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Broadband.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Broadband.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			if ("000001".equals(resp.getStatus())) {

				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				// 保存订单时证件类型如果是身份证且证件号码不为空则校验订单是否实名
				if (("身份证").equals(req4Broadband.getCustomerCardType()) && req4Broadband.getCustomerCardNum() != null) {
					// 黑名单校验
					blackUserBuss.orderCreateNew(CreateOrder.this, resp.getOrderNumber(), req4Broadband.getCustomerCardNum(),
							req4Broadband.getUserName());
				}else{
					ThdOrderProcess orderProcess = Services.getThdOrderProcessService().createHelper().enterThdOrder().getOrderNum().Eq(resp.getOrderNumber()).back2ThdOrderProcess().uniqueResult();
					if(StringUtils.isNotBlank(to.getRealnameNum())){
						//发短信
						ThdUser sysuser = new ThdUser();
						sysuser.setUsername("system");
						sysuser.setFullname("系统");
						//发短信
						ThdOrderCustomerinfo customerinfo = Services.getThdOrderCustomerinfoService()
								.findById(to.getId());
						String content = gzctOrderSMSBuss.sendSms(to.getOrderNum(), Constant.SMSMODEL_SMS_31
								, customerinfo.getCustomerPhone(), null,to.getOrderSourceNum(), null, user, null, null, null, null);
						if(content == null){
							//删除该短信
							content = "发送短信异常";
						}
						if(content.equals("TriesLimit")){
							//删除该短信
							content = "发送短信超出限制次数";
						}
						/**记录订单快照begin*/
						OrderUtils.saveChange(to, "短信催实名", content, "系统",null);
					}else{
	
						//实人信息关联&客户校验通过了
						if(("身份证").equals(to.getThdOrderCustomerinfo().getCustomerCardType())
								&&Constant.QUALITY_CHECK_SUCCESS.equals(orderProcess.getQualityCheck()) 
								&& StringUtils.isNotBlank(to.getRealnameNum())){
							ThdProduct tpd = Services.getThdProductService().createHelper().getThdOrder().Eq(to).uniqueResult();
							ThdProductTemplate ttp = Services.getThdProductTemplateService().createHelper().getId()
									.Eq(Long.parseLong(tpd.getSkuCode())).uniqueResult();
							if(ttp!=null && StringUtils.isNotBlank(ttp.getSkuCode())){
								//推单到AI受理助手
								order2AiService.pushOrderToAi(to.getOrderNum(), CreateOrder.this);
							}else{
								to.setStatus(OrderStatus.NOT_ACCEPT);
								Services.getThdOrderService().saveOrUpdate(to);
								OrderUtils.saveChange(to, OrderStatus.ORDER_STATES.get(OrderStatus.NOT_ACCEPT), "用户已通过实名认证", "系统", null);
								/**
								 * 推送消息到消息中心
								 */
								orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
							}
						}else if(StringUtils.isNotBlank(to.getRealnameNum())){
							ThdProduct tpd = Services.getThdProductService().createHelper().getThdOrder().Eq(to).uniqueResult();
							ThdProductTemplate ttp = Services.getThdProductTemplateService().createHelper().getId()
									.Eq(Long.parseLong(tpd.getSkuCode())).uniqueResult();
							if(ttp!=null && StringUtils.isNotBlank(ttp.getSkuCode())){
								//推单到AI受理助手
								order2AiService.pushOrderToAi(to.getOrderNum(), CreateOrder.this);
							}else{
								to.setStatus(OrderStatus.NOT_ACCEPT);
								Services.getThdOrderService().saveOrUpdate(to);
								OrderUtils.saveChange(to, OrderStatus.ORDER_STATES.get(OrderStatus.NOT_ACCEPT), "用户已通过实名认证", "系统", null);
								/**
								 * 推送消息到消息中心
								 */
								orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
							}
						}
					}
				}

			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		// 预付费下单
		if (APIConstant.ORDER_PREPAIDGIFT_CREATE.equals(request.getService())) {
			final Req4Prepaidgift req4Prepaidgift = BeanUtils.getObjectFromMap(properties, Req4Prepaidgift.class);
			req4Prepaidgift.setProductCategory("预付费礼包");
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Prepaidgift.getUserName()).limit(1)
					.uniqueResult();
			//生成对象
			long times1 = System.currentTimeMillis();
			Response resp = prepaidgiftService.order2(req4Prepaidgift, sfzzmFile, sfzzmFileFileName, sfzbmFile,
					sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
					thzhxxFileFileName);
			long times2 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][预付费单][生成],耗时:"+(times2-times1)+"毫秒.");
			
			//校验参数
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times3 = System.currentTimeMillis();
				try{
				    resp =parameterCheckService.checkOrderData(resp.getOrder(), Constant.BUSSINESS_PREPAIDGIFT);
				}catch(RuntimeException e){
					logger.error("[CreateOrder][createOrder]校验参数异常：",e);
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times4 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][预付费单][参数检验],耗时:"+(times4-times3)+"毫秒.");
			}

			//保存对象
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times5 = System.currentTimeMillis();
				try{
					resp =createService.saveOrder(resp.getOrder(),sysAgentUser);
				}catch(Exception e){
					logger.info("[CreateOrder][createOrder]保存对象异常："+e.getMessage());
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times6 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][预付费单][保存],耗时:"+(times6-times5)+"毫秒.");
			}
			
			//释放上锁的来源单号、外部订单号
			long times7 = System.currentTimeMillis();
			if(resp != null 
					&& sysAgentUser != null 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Prepaidgift.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Prepaidgift.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			//启动校验线程
			qualityCheckService.run(resp.getOrderNumber());
			long times8 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][预付费单][返回前],耗时:"+(times8-times7)+"毫秒.");
			return resp;
		}

		// 移动产品下单
		if (APIConstant.ORDER_MOBILE_CREATE.equals(request.getService())) {
			final Req4Mobile req4Mobile = BeanUtils.getObjectFromMap(properties, Req4Mobile.class);
			req4Mobile.setProductCategory("移动产品");
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Mobile.getUserName()).limit(1)
					.uniqueResult();
			//生成对象  关联实名不在这个方法  不要点进去看了
			long times1 = System.currentTimeMillis();
			Response resp = mobileService.order2(req4Mobile, sfzzmFile, sfzzmFileFileName, sfzbmFile,
					sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
					thzhxxFileFileName);
			long times2 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][移动产品][生成],耗时:"+(times2-times1)+"毫秒.");
			
			//校验参数
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times3 = System.currentTimeMillis();
				try{
				    resp = parameterCheckService.checkOrderData(resp.getOrder(), Constant.BUSINESS_MOBILE);
				}catch(RuntimeException e){
					logger.error("[CreateOrder][createOrder]校验参数异常：",e);
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times4 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][移动产品][参数检验],耗时:"+(times4-times3)+"毫秒.");
			}
			
			//保存对象
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times5 = System.currentTimeMillis();
				try{
					resp =createService.saveOrder(resp.getOrder(),sysAgentUser);
				}catch(Exception e){
					logger.info("[CreateOrder][createOrder]保存对象异常："+e.getMessage());
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times6 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][移动产品][保存],耗时:"+(times6-times5)+"毫秒.");
			}
			
			//释放上锁的来源单号、外部订单号
			long times7 = System.currentTimeMillis();
			if(resp != null 
					&& sysAgentUser != null 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Mobile.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Mobile.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			if(StringUtils.isNotBlank(resp.getOrderNumber())){
				//启动校验线程
				qualityCheckService.run(resp.getOrderNumber());
			}
			long times8 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][移动产品][返回前],耗时:"+(times8-times7)+"毫秒.");
			return resp;
		}

		// 融合产品下单
		if (APIConstant.ORDER_COMBINED_CREATE.equals(request.getService())) {
			final Req4Combined req4Combined = BeanUtils.getObjectFromMap(properties, Req4Combined.class);
			req4Combined.setProductCategory("融合产品");
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Combined.getUserName()).limit(1)
					.uniqueResult();
			//生成对象  关联实名不在这个方法  不要点进去看了
			long times1 = System.currentTimeMillis();
			Response resp = combinedService.order2(req4Combined, sfzzmFile, sfzzmFileFileName, sfzbmFile,
					sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
					thzhxxFileFileName);
			long times2 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][融合产品][生成],耗时:"+(times2-times1)+"毫秒.");
			
			//校验参数
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times3 = System.currentTimeMillis();
				try{
				    resp = parameterCheckService.checkOrderData(resp.getOrder(), Constant.BUSSINESS_COMBINED);
				}catch(RuntimeException e){
					logger.error("[CreateOrder][createOrder]校验参数异常：",e);
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times4 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][融合产品][参数检验],耗时:"+(times4-times3)+"毫秒.");
			}

			//保存对象
			if(StringUtils.isNotBlank(resp.getStatus()) && APIConstant.SUCCESS.equals(resp.getStatus())){
				long times5 = System.currentTimeMillis();
				try{
					resp =createService.saveOrder(resp.getOrder(),sysAgentUser);
				}catch(Exception e){
					logger.info("[CreateOrder][createOrder]保存对象异常："+e.getMessage());
					resp.setStatus(APIConstant.INTERFACE_ERROR);
					resp.setOrderNumber("");
					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
				long times6 = System.currentTimeMillis();
				logger.info("[CreateOrder][createOrder][融合产品][保存],耗时:"+(times6-times5)+"毫秒.");
			}

			//释放上锁的来源单号、外部订单号
			long times7 = System.currentTimeMillis();
			if(resp != null 
					&& sysAgentUser != null 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Combined.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Combined.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			//启动校验线程
			qualityCheckService.run(resp.getOrderNumber());
			long times8 = System.currentTimeMillis();
			logger.info("[CreateOrder][createOrder][融合产品][返回前],耗时:"+(times8-times7)+"毫秒.");
			return resp;
		}

		// 老用户续约下单
		if (APIConstant.ORDER_KDXY_CREATE.equals(request.getService())) {
			final Req4Kdxy req4Kdxy = BeanUtils.getObjectFromMap(properties, Req4Kdxy.class);
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Kdxy.getUserName()).limit(1)
					.uniqueResult();
			final Response resp = (Response) Services.getThdOrderService()
					.executeTransactional(new TransactionalCallBack() {
						@Override
						public Object execute(IService arg0) {

							Response resp = new Response();
							try {
								resp = kdxyService.order(req4Kdxy, sfzzmFile, sfzzmFileFileName, sfzbmFile,
										sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName,
										thzhxxFile, thzhxxFileFileName);
								resp2.setOrderNumber(resp.getOrderNumber());
							} catch (Exception e) {
								logger.error("[CreateOrder][execute] 生成老用户续约订单异常，" + e.getMessage(), e);
								resp.setStatus(APIConstant.INTERFACE_ERROR);
								resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
							}
							return resp;
						}

					});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Kdxy.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Kdxy.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			// 推单到AI中转站
			if (StringUtils.isNotBlank(resp.getOrderNumber())) {
				// 消息中心通知
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}
		// 增值业务下单
		if (APIConstant.ORDER_ZZYW_CREATE.equals(request.getService())) {
			final Req4Kdzz req4kdzz = BeanUtils.getObjectFromMap(properties, Req4Kdzz.class);
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4kdzz.getUserName()).limit(1)
					.uniqueResult();
			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {

					Response resp = new Response();
					try {
						resp = kdzzService.order(req4kdzz, sfzzmFile, sfzzmFileFileName, sfzbmFile, sfzbmFileFileName,
								sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
								thzhxxFileFileName);
						resp2.setOrderNumber(resp.getOrderNumber());
					} catch (Exception e) {
						logger.error("[CreateOrder][execute] 生成老用户续约订单异常，" + e.getMessage(), e);
						resp.setStatus(APIConstant.INTERFACE_ERROR);
						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
					}
					return resp;
				}

			});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4kdzz.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4kdzz.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			// 推单到AI中转站
			if (StringUtils.isNotBlank(resp.getOrderNumber())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				if(to.getStatus() != OrderStatus.DISUSED){
					ThdProduct product = Services.getThdProductService().createHelper().getThdOrder().Eq(to).getId().Desc().limit(1).uniqueResult();
					ThdOrderProcess thdOrderProcess = Services.getThdOrderProcessService().createHelper().enterThdOrder().getId().Eq(to.getId()).back2ThdOrderProcess().limit(1).uniqueResult();
					if(product!=null){
						Long skuc = null;
						try{
							skuc =  Long.parseLong(product.getSkuCode());
						}catch(RuntimeException e){
							logger.error("[ORDER_CREATE] 订单["+to.getOrderNum()+"]sku异常");
							String remark = "sku异常";
							
							//操作人
							thdOrderProcess.setWaitAcceptUsername("系统");
							thdOrderProcess.setProcessPersonFullname("系统");
							thdOrderProcess.setProcessPersonUsername("系统");
							thdOrderProcess.setProcessRemarks(remark);
							thdOrderProcess.setProcessTime(new Date());
							
							//订单状态流转到受理【人工处理】
							to.setStatus(OrderStatus.WAIT_ACCEPT);
							to.setThdOrderProcess(thdOrderProcess);
							Services.getThdOrderService().saveOrUpdate(to);
							
							/**记录订单快照begin*/
							OrderUtils.saveChange(to, "受理【人工处理】", remark, "系统", null);
							// 消息中心通知
							orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
						}
						if(skuc!=null){
							//模板有sku的
							ThdProductTemplate productTemplate = Services.getThdProductTemplateService().createHelper().getId().Eq(skuc).getSkuCode().IsNotNull().uniqueResult();
							if(productTemplate!=null){
								if(Constant.BUSINESS_VALUEADDED_LLB.equals(productTemplate.getPropell())){
									//调用流量包接口
									try{
										orderToO2ollb.orderToO2ollbRunnable(to.getOrderNum());
									}catch(RuntimeException e){
										logger.error("系统异常",e);
										String remark = "系统异常";
										//操作人
										thdOrderProcess.setWaitAcceptUsername("系统");
										thdOrderProcess.setProcessPersonFullname("系统");
										thdOrderProcess.setProcessPersonUsername("系统");
										thdOrderProcess.setProcessRemarks(remark);
										thdOrderProcess.setProcessTime(new Date());
										Services.getThdOrderProcessService().saveOrUpdate(thdOrderProcess);
										
										//订单状态流转到受理【人工处理】
										to.setStatus(OrderStatus.WAIT_ACCEPT);
										Services.getThdOrderService().saveOrUpdate(to);
										
										/**记录订单快照begin*/
										OrderUtils.saveChange(to, "受理【人工处理】", remark, "系统", null);
										// 消息中心通知
										orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
									}
								}else if(Constant.BUSINESS_VALUEADDED_AI.equals(productTemplate.getPropell())){
									//推送ai
									order2AiService.pushOrderToAi(to.getOrderNum(),CreateOrder.this);
								}else{
									//装维
									thdOrderProcess.setCollectFeesTime(new Date());
									thdOrderProcess.setCollectFeesUsername("系统");
									thdOrderProcess.setCollectFeesFullname("系统");
									//订单状态流转到受理【人工处理】
									to.setStatus(OrderStatus.ACCEPT_NO_PAID);
									to.setThdOrderProcess(thdOrderProcess);
									Services.getThdOrderService().saveOrUpdate(to);
									
									/**记录订单快照begin*/
									OrderUtils.saveChange(to, "受理成功【待收费】", null, "系统", null);
									// 消息中心通知
									orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
								}
							}
						}
					}
				}
				// 消息中心通知
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				/*if(to.getStatus() == OrderStatus.DISUSED){
					OrderUtils.saveChange(to, "受理类型为【作废退费】，自动作废处理", null, "系统", null);
				}*/
			}
			// if (StringUtils.isNotBlank(resp.getOrderNumber())) {
			// if (("身份证").equals(req4Kdxy.getCustomerCardType()) &&
			// req4Kdxy.getCustomerCardNum() != null
			// &&
			// Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
			// .uniqueResult().getStatus() == OrderStatus.NOT_ACCEPT) {
			// // 黑名单校验
			// blackUserBuss.orderCreateNew(null, resp.getOrderNumber(),
			// req4Kdxy.getCustomerCardNum(), req4kdzz.getUserName());
			// }
			// }
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		// 礼品配送下单
		if (APIConstant.ORDER_GIFT_CREATE.equals(request.getService())) {
			final ReqGiftdelivery reqGiftdelivery = BeanUtils.getObjectFromMap(properties, ReqGiftdelivery.class);
//			Response respCheckProductId = checkProductId(reqGiftdelivery.getDeliveryProvince(),reqGiftdelivery.getProductJson());
//			if(respCheckProductId!=null){
//				return respCheckProductId;
//			}
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(reqGiftdelivery.getUserName()).limit(1)
					.uniqueResult();
			// req4Prepaidgift2.setUserName(req4Mobile.getUserName());
			Response resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {
				@Override
				public Object execute(IService arg0) {
					Response resp = new Response();
					try {
						resp = giftdeliveryService.order(reqGiftdelivery, sfzzmFile, sfzzmFileFileName, sfzbmFile,
								sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
								thzhxxFileFileName);
						resp2.setOrderNumber(resp.getOrderNumber());

					} catch (Exception e) {
						logger.error("[CreateOrder][execute] 生成礼品配送订单异常，" + e.getMessage(), e);
						resp.setStatus(APIConstant.INTERFACE_ERROR);
						resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
					}
					return resp;
				}
			});

			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+reqGiftdelivery.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+reqGiftdelivery.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			if (StringUtils.isNotBlank(resp.getOrderNumber())) {
				// 消息中心通知
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				//				if (("身份证").equals(reqGiftdelivery.getCustomerCardType())
				//						&& reqGiftdelivery.getCustomerCardNum() != null && to.getStatus() == OrderStatus.NOT_SUBMIT) {
				//					// 黑名单校验
				//					blackUserBuss.orderCreateNew(CreateOrder.this, resp.getOrderNumber(),
				//							reqGiftdelivery.getCustomerCardNum(), reqGiftdelivery.getUserName());
				//				}
			}
			// 先创建蓝单编号放着
			// 获取当前登录用户
			ThdOrder orderDb = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
					.uniqueResult();
			if (orderDb != null) {
				result = deliveryService.createDelivery(orderDb);
				orderDb.setDeliveryNum(result);
				Services.getThdOrderService().update(orderDb);
				List<ThdOrderRealname> realnames = Services.getThdOrderRealnameService().createHelper().getSmrzType().NeOrNull(Constant.SMRZ_TYPE_PAGE).getThdOrder()
						.Eq(orderDb).list();
				// 推仓储的代码
				if (StringUtils.isNotBlank(result) && orderDb.getPushWarehouse()) {
					String deliveryQuantity_msg = orderUtils.deliveryQuantity(orderDb.getOrderNum());
					if(StringUtils.isNotBlank(deliveryQuantity_msg)){
						orderDb.setStatus(OrderStatus.PENDING_DELIVERY);	
						ThdOrderProcess updateProcess = Services.getThdOrderProcessService().createHelper().getId().Eq(orderDb.getId()).uniqueResult();
						updateProcess.setQualityCheck("物流配送商下单量校验");
						updateProcess.setQualityCheckRemark(deliveryQuantity_msg);
						Services.getThdOrderProcessService().update(updateProcess);
						logger.info("[CREATE_ORDER] 订单保存成功。[orderNum="+orderDb.getOrderNum()+"]");
						OrderUtils.saveChange(orderDb, "推送仓储", deliveryQuantity_msg, null, user);
						try {
							orderChangeImpl.statusChange(orderDb.getOrderNum(), orderDb.getStatus());
						} catch(Exception exce) {
							logger.error("[CREATE_ORDER]["+orderDb.getOrderNum()+"]待配送 推送到消息中心出错",exce);
						}
						resp.setStatus(APIConstant.MAXIMUN_DELIVERY_ERROR);
						resp.setOrderNumber("");
						resp.setMsg(APIConstant.MAXIMUN_DELIVERY_ERROR_MSG);
						return resp;
					}else{
						ThdOrderDeliveryinfo orderDeliveryinfo = Services.getThdOrderDeliveryinfoService().findById(orderDb.getId());
						if(orderDeliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_TD) ){
							jedisService.distributorthreshold(orderDeliveryinfo.getDeliveryCompany());
						}
					}
					logger.info("[CreateOrder] [礼品配送] 开始推仓储......");
					final ThdUser sysuser = Services.getThdUserService().createHelper().getUsername()
							.Eq(reqGiftdelivery.getUserName()).limit(1).uniqueResult();
					new Thread() {
						@Override
						public void run() {
							deliveryService.doOrderPush(sysuser, result, "");
						}
					}.start();
				}
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		// 实名制登记
		if (APIConstant.ORDER_REALNAME_CREATE.equals(request.getService())) {
			final Req4ThdOrderRealname req = BeanUtils.getObjectFromMap(properties, Req4ThdOrderRealname.class);
			req.setProductCategory("实名制登记");
			String orderNumber = null;
			Response resp = null;
			if (null == resp) {

				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

					@Override
					public Object execute(IService arg0) {

						try {

							return orderRealnameService.order(req, user, sfzzmFile, sfzzmFileFileName, sfzbmFile,
									sfzbmFileFileName, sfzscFile, sfzscFileFileName, sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName,
									thzhxxFile, thzhxxFileFileName, request.getUserName());
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成实名制订单异常，" + e.getMessage(), e);
							return "";
						}
					}

				});
				orderNumber = resp.getOrderNumber();
				sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req.getUserName()).limit(1).uniqueResult();
				if (StringUtils.isNotBlank(orderNumber)) {
					if("只生成实名制记录".equals(orderNumber)){
						resp = new Response();
						resp.setOrderNumber(orderNumber);
						resp.setStatus(APIConstant.SUCCESS);
					}else{
						Gson gson = new GsonBuilder().disableHtmlEscaping().create();
						logger.log(RealnameLog.REALNAME, "[CREATE_ORDER][ORDER_REALNAME_CREATE] params map = " + gson.toJson(properties)+"图片名称"+sfzzmFileFileName+sfzbmFileFileName+sfzscFileFileName+sfzsc2FileFileName);
						// 实名制订单
						ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNumber).limit(1)
								.uniqueResult();
						orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
						//释放上锁的来源单号、外部订单号
						sysAgentUser = Services.getThdUserService().createHelper().enterThdOrdersForUserId().getId().Eq(to.getId()).back2ThdUserByUserId().limit(1)
								.uniqueResult();
						/**单宽带实名,融合订单实名实名信息和客户信息相同才关联订单*/
						ThdOrderRealname thdOrderRealname = Services.getThdOrderRealnameService().createHelper().enterThdOrder().getOrderNum().Eq(orderNumber).back2ThdOrderRealnames().limit(1).uniqueResult();
						ThdOrderCustomerinfo thdOrderCustomerinfo = Services.getThdOrderCustomerinfoService().createHelper().enterThdOrder().getOrderNum().Eq(orderNumber).back2ThdOrderCustomerinfo().limit(1).uniqueResult();
						if(thdOrderRealname!=null
								&& thdOrderCustomerinfo!=null
								&& StringUtils.isNotBlank(thdOrderRealname.getName())
								&& StringUtils.isNotBlank(thdOrderRealname.getIdcardNum())
								&& StringUtils.isNotBlank(thdOrderRealname.getContactPhone())
								&& thdOrderRealname.getName().equalsIgnoreCase(thdOrderCustomerinfo.getCustomerName()) 
								&& thdOrderRealname.getIdcardNum().equalsIgnoreCase(thdOrderCustomerinfo.getCustomerCardNum())
								&& thdOrderRealname.getContactPhone().equalsIgnoreCase(thdOrderCustomerinfo.getCustomerPhone())){
							//下单成功后调用一下这个方法关联订单
							orderUtils.relatedRealName2(orderNumber,user);
						}else{		
							logger.info("[OrderRealnameService][order]单宽带实名信息和客户信息不匹配"
									+ "["+thdOrderRealname.getName()+"="+thdOrderCustomerinfo.getCustomerName()+"]"
									+ "["+thdOrderRealname.getIdcardNum()+"="+thdOrderCustomerinfo.getCustomerCardNum()+"]"
									+ "["+thdOrderRealname.getContactPhone()+"="+thdOrderCustomerinfo.getCustomerPhone()+"]");
						}
	
						ThdOrderRealname orderRealname = Services.getThdOrderRealnameService().createHelper().getThdOrder()
								.Eq(to).getSmrzType().NeOrNull(Constant.SMRZ_TYPE_PAGE).getId().Desc().limit(1).uniqueResult();
						String realnameNumber = orderRealname == null ? "" : orderRealname.getRealnameNumber();
						if (!StringUtil.isBlank(realnameNumber)) {
							// 宽带订单
							ThdOrder order = Services.getThdOrderService().createHelper().getRealnameNum().Eq(realnameNumber).getEnabled().Eq(true)
									.limit(1).uniqueResult();
							// 如果订单不存在，就是号卡实名而非身份实名。
							if (order != null) {
								String userName = order.getThdUserByUserId().getUsername();
								// 单宽带产品 关联用户信息后，将关联的订单信息推送到消息中心 。先宽带后实名。
								orderChangeImpl.infoToSubscription(userName, realnameNumber, order.getOrderSourceNum(),null,
										new Date(), "关联成功");
								// 宽带订单状态变化推送消息中心
								orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
							}
						}
	
						ThdOrder order = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNumber).limit(1).uniqueResult();
						ThdOrderRealname realname = Services.getThdOrderRealnameService().createHelper()
								.enterThdOrder().getId().Eq(order.getId()).back2ThdOrderRealnames()
								.getSmrzType().NeOrNull(Constant.SMRZ_TYPE_PAGE).limit(1).uniqueResult();
						if (realname!=null && !req.getSmrzType().equals(Constant.SMRZ_TYPE_SFSM)) {
							if(req.getSmrzType().equals(Constant.SMRZ_TYPE_KBGLSM)){
								resp = new Response();
								resp.setOrderNumber(orderNumber);
								resp.setStatus(APIConstant.SUCCESS);
								resp.setMsg("下单成功");
							}
							if (StringUtils.isNotBlank(realname.getRemark())) {
								if (realname.getRemark().indexOf("异常")>=0) {
	
									resp = new Response();
									resp.setOrderNumber(orderNumber);
									resp.setStatus(APIConstant.ESBCRMTXD_INTERFACE_PARAM);
									resp.setMsg(realname.getRemark());
	
								}else if(realname.getRemark().indexOf("失败")>=0){
	
									resp = new Response();
									resp.setOrderNumber(orderNumber);
									resp.setStatus(APIConstant.ESBCRMTXD_INTERFACE_DEFEATED);
									resp.setMsg(realname.getRemark());
	
								}else if(realname.getRemark().indexOf("成功")>=0){
	
									resp = new Response();
									resp.setOrderNumber(orderNumber);
									resp.setStatus(APIConstant.SUCCESS);
									resp.setMsg(realname.getRemark());
	
								}else if(req.getSmrzType().equals(Constant.SMRZ_TYPE_KBGLSM)){
									resp = new Response();
									resp.setOrderNumber(orderNumber);
									resp.setStatus(APIConstant.SUCCESS);
									resp.setMsg("下单成功");
								}else {
									resp = new Response();
									resp.setOrderNumber(orderNumber);
									resp.setStatus(APIConstant.ESBCRMTXD_INTERFACE_DEFEATED);
									resp.setMsg("省能力平台实名登记失败，没有返回值");
								}
							}
						}else if(req.getSmrzType().equals(Constant.SMRZ_TYPE_SFSM)){
							resp = new Response();
							resp.setOrderNumber(orderNumber);
							resp.setStatus(APIConstant.SUCCESS);
						}
					}
				} else {
					if(sysAgentUser != null 
							&& StringUtils.isBlank(orderNumber) 
							&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
							&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
							){
						String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req.getOrderSourceNum();
						String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req.getOrderOutNum();
						orderUtils.removeRepeatOrder(orderSourceNumKey);
						orderUtils.removeRepeatOrder(orderOutNumKey);
					}
//					resp = new Response();
//					resp.setStatus(APIConstant.INTERFACE_ERROR);
//					resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
				}
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}
		// 第三方托收
		if (APIConstant.ORDER_MONTHLYFEE_CREATE.equals(request.getService())) {
			final Req4Monthlyfee req4Monthlyfee = BeanUtils.getObjectFromMap(properties, Req4Monthlyfee.class);
			Response resp = monthlyfeeService.checkData(req4Monthlyfee);
			if (null == resp) {
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

					@Override
					public Object execute(IService arg0) {

						Response resp = new Response();
						try {
							resp = monthlyfeeService.order(req4Monthlyfee, sfzzmFile, sfzzmFileFileName, sfzbmFile,
									sfzbmFileFileName, sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
									thzhxxFileFileName);
							resp2.setOrderNumber(resp.getOrderNumber());
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成第三方托收订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						return resp;
					}

				});
			}
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Monthlyfee.getUserName()).limit(1)
					.uniqueResult();
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Monthlyfee.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Monthlyfee.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			//AI推送
			if(Constant.MONTHLYFEE_TRANSACTTYPE_DSDECZ.equals(req4Monthlyfee.getTransactType())){
				if ("000001".equals(resp.getStatus())) {
					ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
							.uniqueResult();
					long cardType_num = Services.getThdOrderCustomerinfoService().createHelper().getId().Eq(to.getId()).getCustomerCardType().In(Constant.Push_AI_ID_Type).rowCount();
					if(cardType_num>0){
						//推单到AI受理助手
						order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);
					}else{
						to.setStatus(OrderStatus.WAIT_ACCEPT);
						Services.getThdOrderService().update(to);
						OrderUtils.saveChange(to, "受理【人工处理】", "", "系统", null);
					}
					orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				}
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		//	客户身份创建
		if (APIConstant.ORDER_GOVENTIDENTITY_CREATE.equals(request.getService())) {
			final Req4Goventidentity req4Goventidentity = BeanUtils.getObjectFromMap(properties, Req4Goventidentity.class);
			//校验数据
			Response resp = goventidentityService.checkData(req4Goventidentity);

			if(null ==resp){
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {
					@Override
					public Object execute(IService arg0) {
						Response resp = new Response();
						String orderNumber = "";
						try {
							orderNumber = goventidentityService.order(req4Goventidentity,user, sfzzmFile, sfzzmFileFileName, sfzbmFile, sfzbmFileFileName,
									sfzscFile, sfzscFileFileName,sfzsc2File, sfzsc2FileFileName, yyzzFile, yyzzFileFileName, thzhxxFile,
									thzhxxFileFileName);
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成政企客户身份订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						resp.setOrderNumber(orderNumber);
						resp.setStatus(APIConstant.SUCCESS);
						return resp;
					}
				});
			}
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Goventidentity.getUserName()).limit(1)
					.uniqueResult();
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Goventidentity.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Goventidentity.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			if ("000001".equals(resp.getStatus())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				//推单到AI受理助手
				order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		//在线拆机创建
		if (APIConstant.ORDER_RETRACTPHONE_CREATE.equals(request.getService())) {
			final Req4Retractphone req4Retractphone = BeanUtils.getObjectFromMap(properties, Req4Retractphone.class);
			req4Retractphone.setProductCategory("在线拆机");
			//校验数据
			Response resp = retractphoneService.checkData(req4Retractphone);

			if(null ==resp){
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {
					@Override
					public Object execute(IService arg0) {
						Response resp = new Response();
						String orderNumber = "";
						try {
							orderNumber = retractphoneService.order(req4Retractphone,user);
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成在线拆机订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						resp.setOrderNumber(orderNumber);
						resp.setStatus(APIConstant.SUCCESS);
						return resp;
					}
				});
			}
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Retractphone.getUserName()).limit(1)
					.uniqueResult();
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Retractphone.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Retractphone.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			
			if ("000001".equals(resp.getStatus())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
			}
			
			ThdOrder realNumOrder = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
					.uniqueResult();
			if(realNumOrder!=null && StringUtils.isNotBlank(realNumOrder.getRealnameNum())){
				//推单到AI受理助手
				order2AiService.pushOrderToAi(resp.getOrderNumber(), CreateOrder.this);

				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}

		//老用户换套餐
		if(APIConstant.ORDER_REPLACEPACKAGE_CREATE.equals(request.getService())){
			final Req4Replacepackage req4PlReplacepackage = BeanUtils.getObjectFromMap(properties, Req4Replacepackage.class);
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4PlReplacepackage.getUserName()).limit(1)
					.uniqueResult();
			//校验数据
			Response resp = replacePackageService.checkData(req4PlReplacepackage);

			if(resp == null){
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

					@Override
					public Object execute(IService arg0) {

						Response resp = new Response();
						try {
							resp = replacePackageService.order(req4PlReplacepackage,file,fileFileName);
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成老用户更换套餐订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						return resp;
					}

				});
			}


			//释放上锁的来源单号、外部订单号
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4PlReplacepackage.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4PlReplacepackage.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}

			if (StringUtils.isNotBlank(resp.getOrderNumber())) {
				//启动校验线程
				qualityCheckService.run(resp.getOrderNumber());
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}
		
		//欠费催缴 
		if(APIConstant.ORDER_ARREARSPAY_CREATE.equals(request.getService())){
			final Req4Arrearspay req4Arrearspay = BeanUtils.getObjectFromMap(properties, Req4Arrearspay.class);
			Response resp = arrearspayService.checkData(req4Arrearspay);
			if (null == resp) {
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {
					@Override
					public Object execute(IService arg0) {

//						String ordernumber=null;
						Response resp = new Response();
						try {
							resp = arrearspayService.order(req4Arrearspay,user);
//							resp2.setOrderNumber(ordernumber);
//							resp2.setStatus(APIConstant.SUCCESS);
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成第三方托收订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						return resp;
					}
				});
			}
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Arrearspay.getUserName()).limit(1)
					.uniqueResult();
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Arrearspay.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Arrearspay.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			if(StringUtils.isNotBlank(resp.getOrderNumber())){
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				//推送欠费催缴平台
				arrearspayService.arrearspayNotic(resp.getOrderNumber());
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}
		
		// CRM(翼销售)甩单
		if (APIConstant.ORDER_INTENTION_CREATE.equals(request.getService())) {
			final Req4Intention req4Intention = BeanUtils.getObjectFromMap(properties, Req4Intention.class);
			Response resp = intentionService.checkData(req4Intention);
			if (null == resp) {
				resp = (Response) Services.getThdOrderService().executeTransactional(new TransactionalCallBack() {

					@Override
					public Object execute(IService arg0) {

						Response resp = new Response();
						try {
							resp = intentionService.order(req4Intention,user );
							resp2.setOrderNumber(resp.getOrderNumber());
						} catch (Exception e) {
							logger.error("[CreateOrder][execute] 生成CRM(翼销售)甩单订单异常，" + e.getMessage(), e);
							resp.setStatus(APIConstant.INTERFACE_ERROR);
							resp.setMsg(APIConstant.INTERFACE_ERROR_MSG);
						}
						return resp;
					}

				});
			}
			//释放上锁的来源单号、外部订单号
			sysAgentUser = Services.getThdUserService().createHelper().getUsername().Eq(req4Intention.getUserName()).limit(1)
					.uniqueResult();
			if(resp != null 
					&& sysAgentUser != null 
					&& StringUtils.isBlank(resp.getOrderNumber()) 
					&& !APIConstant.DDLY_WRONG.equals(resp.getStatus()) 
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					&& !APIConstant.ORDER_OUT_NUM_REPEAT.equals(resp.getStatus())
					){
				String orderSourceNumKey = "CHECK_REPEAT_ORDER_SOURCE"+sysAgentUser.getId()+"_"+req4Intention.getOrderSourceNum();
				String orderOutNumKey = "CHECK_REPEAT_ORDER_OUT"+sysAgentUser.getId()+"_"+req4Intention.getOrderOutNum();
				orderUtils.removeRepeatOrder(orderSourceNumKey);
				orderUtils.removeRepeatOrder(orderOutNumKey);
			}
			if (StringUtils.isNotBlank(resp.getOrderNumber())) {
				ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(resp.getOrderNumber())
						.uniqueResult();
				to.setStatus(OrderStatus.NOT_ACCEPT);
				Services.getThdOrderService().saveOrUpdate(to);
				OrderUtils.saveChange(to, OrderStatus.ORDER_STATES.get(OrderStatus.NOT_ACCEPT), "提交未受理", "系统", null);
				orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
			}
			if(resp!=null){
				logger.info(request.getService());
				logger.info(resp.toString());				
			}
			return resp;
		}
		return null;
	}

	@Override
	public void doing(String orderNum) {
		ThdBusinessTag business = Services.getThdBusinessTagService().createHelper().enterThdProducts().enterThdOrder()
				.getOrderNum().Eq(orderNum).back2ThdProducts().back2ThdBusinessTag().uniqueResult();
		ThdOrder to = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).getThdOrder().IsNull()
				.enterThdUserByUserId().back2ThdOrdersForUserId().uniqueResult();
		ThdOrderProcess qualityCheck1 = Services.getThdOrderProcessService().createHelper().getId().Eq(to.getId()).uniqueResult();
		ThdOrderCustomerinfo thdc = Services.getThdOrderCustomerinfoService().createHelper()
				.enterThdOrder().getOrderNum().Eq(orderNum).back2ThdOrderCustomerinfo().limit(1).uniqueResult();
		if(StringUtils.isNotBlank(thdc.getCustomerCardNum())){
			String re = orderUtils.highRiskBlacklist(thdc.getCustomerCardNum(),business.getId());
			if(StringUtils.isNotBlank(re)){
				to.setStatus(OrderStatus.NOT_SUBMIT);
				Services.getThdOrderService().update(to);
				OrderUtils.saveChange(to, "待提交", re, "系统", null);
				return;
			}
		}

		if((Constant.BUSSINESS_PREPAIDGIFT.equals(business.getName()) || Constant.BUSSINESS_BROADBAND.equals(business.getName())
				|| Constant.BUSSINESS_REALNAME.equals(business.getName()) || Constant.BUSSINESS_COMBINED.equals(business.getName())
				|| Constant.BUSINESS_REPLACEPACKAGE.equals(business.getName()) || Constant.BUSINESS_HFFPK.equals(business.getName())
				|| Constant.BUSINESS_YFFPK.equals(business.getName()) || Constant.BUSINESS_MOBILE.equals(business.getName()))
				&& Constant.QUALITY_LESS_THAN_16_FAIL.equals(qualityCheck1.getQualityCheck())){
			to.setStatus(OrderStatus.DISUSED);
			Services.getThdOrderService().saveOrUpdate(to);

			ThdOrderProcess process = to.getThdOrderProcess();
			process.setCancelPersonRemarks("用户年龄小于16岁");
			process.setCancelPersonReason("用户质量校验不通过");
			process.setCancelPersonTime(new Date());
			Services.getThdOrderProcessService().update(process);
			OrderUtils.saveChange(to, "作废", "用户年龄小于16岁", "系统", null);
		}else{
			if (Constant.BUSSINESS_PREPAIDGIFT.equals(business.getName())) {
				// 推物流标识
				boolean isdelivery = true;

				ThdOrderDeliveryinfo deliveryinfo = Services.getThdOrderDeliveryinfoService().findById(to.getId());
				List<String> productTemps = Services.getThdProductService().createHelper().getThdOrder().Eq(to)
						.getName().listProperty(true);
				List<String> products = Services.getDictionaryService().createHelper()
						.enterDictionary().getKeyName().Eq(Constant.PREPAIDGIFT_DELIVERY_ARCHIVE_FULL_KEY).back2Dictionary()
						.getStatus().Eq(true).getKeyValue().In(productTemps).getKeyValue().listProperty(false);

				// 查询该订单是否已有实名关联
				List<ThdOrderRealname> realnames = Services.getThdOrderRealnameService().createHelper().getThdOrder().Eq(to).getSmrzType().NeOrNull(Constant.SMRZ_TYPE_PAGE)
						.list();
				ThdOrderCustomerinfo customerinfo = Services.getThdOrderCustomerinfoService().findById(to.getId());

				if("否".equals(deliveryinfo.getBatchPickUp()) && (products != null && products.size() == 0)){
					//判断90天内是否实名2次
					Calendar calendar = Calendar.getInstance();//获取90天前时间
					calendar.add(Calendar.DATE, -90);
					if(Services.getThdOrderRealnameService().createHelper().getIdcardNum().Eq(customerinfo.getCustomerCardNum()).getRealnameSuccess().Eq("已实名")
							.getPaidType().Eq("预付费").getSmrzType().Eq("号卡实名").getCreateTime().Ge(calendar.getTime()).rowCount() >= 1){

						isdelivery = false;
						to.setStatus(OrderStatus.NOT_SUBMIT);
						Services.getThdOrderService().saveOrUpdate(to);
						OrderUtils.saveChange(to, "用户90天内已实名成功1次，无法再次办理", null, "系统", null);
						orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
					}
				}

				if (realnames != null && realnames.size() == 0) {
					int falg = OrderUtils.realnamePreposeCheck(to.getOrderNum(),business.getName());
					ThdOrderProcess process = Services.getThdOrderProcessService().createHelper()
							.getId().Eq(to.getId())
							.startOr()
							.getBlackUserState().In("正常","接口异常","无客户信息","黑名单接口异常")
							.getBlackUserState().IsNull()
							.stopOr()
							.uniqueResult();
					if(falg==1 && (products != null && products.size() == 0)){
//						to.setRealnamePrepose(true);
						to.setRealnamePrepose(falg);
						Services.getThdOrderService().saveOrUpdate(to);							
						// 检查是否有相关订单
						String realnameNum ="";
						//外部订单号不等雨空 先去关联捆绑实名  预付费礼包
						if(StringUtils.isNotBlank(to.getOrderOutNum())){
							realnameNum= OrderUtils.checkRealname2(customerinfo.getCustomerName(),
									customerinfo.getCustomerCardNum(), customerinfo.getAgentName(),
									customerinfo.getAgentCardNum(), business.getName(), orderNum, 1,to.getOrderOutNum());
						}
						if(StringUtils.isBlank(realnameNum)){
							realnameNum= OrderUtils.checkRealname(customerinfo.getCustomerName(),
									customerinfo.getCustomerCardNum(), customerinfo.getAgentName(),
									customerinfo.getAgentCardNum(), business.getName(), orderNum, 1);;
						}
						if (StringUtils.isNotBlank(realnameNum)) {
							// 如果有相关的实名制订单
							OrderUtils.copyRealname(orderNum, realnameNum);
							OrderUtils.saveChange(to, "订单已实名", null, "系统", null);
							if(process==null){
								isdelivery = false;
								to.setStatus(OrderStatus.NOT_SUBMIT);
								Services.getThdOrderService().update(to);
								orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
							}
						} else {
							// 关联不上不推物流 留在待提交
							isdelivery = false;
							to.setStatus(OrderStatus.NOT_SUBMIT);
							Services.getThdOrderService().update(to);
							OrderUtils.saveChange(to, "订单未实名,提交[待提交]状态", null, "系统", null);
							orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
							//发短信
							ThdUser user = new ThdUser();
							user.setUsername("system");
							user.setFullname("系统");
							if(!Constant.BLACK_STATE_1.equals(qualityCheck1.getQualityCheck())){
								//发短信
								String content = gzctOrderSMSBuss.sendSms(to.getOrderNum(), Constant.SMS_MODEL_PREPAIDGIFTREALNAME
										, to.getThdOrderCustomerinfo().getCustomerPhone(), null,to.getOrderSourceNum(), null, user, null, null, null, null);
								if(content == null){
									//删除该短信
									content = "发送短信异常";
								}
								if(content.equals("TriesLimit")){
									//删除该短信
									content = "发送短信超出限制次数";
								}
								/**记录订单快照begin*/
								OrderUtils.saveChange(to, "短信催实名", content, "系统",null);
							}
						}
					}else{
//						to.setRealnamePrepose(false);
						to.setRealnamePrepose(falg);
						Services.getThdOrderService().saveOrUpdate(to);
						if(process==null){
							isdelivery = false;
						}
					}
				}					

				if (isdelivery) {
					ThdOrderDeliveryinfo orderDeliveryinfo = Services.getThdOrderDeliveryinfoService().findById(to.getId());
					if (orderDeliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_SELF)) {// 代理商自行发货
						ThdOrderProcess process = Services.getThdOrderProcessService().findById(to.getId());
						process.setDeliveryPersonUsername("系统");
						process.setDeliveryPersonFullname("系统");
						process.setDeliveryTime(new Date());
						Services.getThdOrderProcessService().saveOrUpdate(process);
						OrderUtils.saveChange(to, "代理商自发货流转至物流配货中", null, null, null);
						to.setStatus(OrderStatus.LOGISTICS_DELIVERY);
						Services.getThdOrderService().update(to);
						orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
					} else if (orderDeliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_TD)) {
						final ThdUser sysuser = Services.getThdUserService().createHelper().getUsername()
								.Eq(req4Prepaidgift2.getUserName()).limit(1).uniqueResult();
						new Thread() {
							@Override
							public void run() {
								deliveryService.doOrderPush(sysuser, result, "");
							}
						}.start();
					}
				}
			}
			//移动业务实名关联逻辑
			else if(Constant.BUSINESS_MOBILE.equals(business.getName())){
				ThdOrder mobile_thdOrder = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).enterThdUserByUserId().back2ThdOrdersForUserId().limit(1).uniqueResult();
				ThdOrderDeliveryinfo deliveryinfo = Services.getThdOrderDeliveryinfoService().createHelper().enterThdOrder().getOrderNum().Eq(mobile_thdOrder.getOrderNum()).back2ThdOrderDeliveryinfo().uniqueResult();
				boolean isdelivery = false;
				if(Constant.DELIVERY_TYPE_SELF.equals(deliveryinfo.getDeliveryType())
						||Constant.DELIVERY_TYPE_TD.equals(deliveryinfo.getDeliveryType())
						||Constant.DELIVERY_TYPE_ZT.equals(deliveryinfo.getDeliveryType())){
					// 检查是否有相关订单
					int falg = OrderUtils.realnamePreposeCheck(to.getOrderNum(),business.getName());
					ThdOrderProcess qualityCheck = Services.getThdOrderProcessService().createHelper().getId().Eq(mobile_thdOrder.getId()).uniqueResult();
					
					if(falg==1){
						ThdOrderCustomerinfo customerinfo = Services.getThdOrderCustomerinfoService().findById(to.getId());
						String realnameNum ="";
						//外部订单号不等雨空 先去关联捆绑实名 移动业务
						if(StringUtils.isNotBlank(to.getOrderOutNum())){
							realnameNum= OrderUtils.checkRealname2(customerinfo.getCustomerName(),
									customerinfo.getCustomerCardNum(), customerinfo.getAgentName(),
									customerinfo.getAgentCardNum(), business.getName(), orderNum, 1,to.getOrderOutNum());
						}
						if(StringUtils.isBlank(realnameNum)){
							realnameNum= OrderUtils.checkRealname(customerinfo.getCustomerName(),
									customerinfo.getCustomerCardNum(), customerinfo.getAgentName(),
									customerinfo.getAgentCardNum(), business.getName(), orderNum, 1);;
						}
//						mobile_thdOrder.setRealnamePrepose(true);
						mobile_thdOrder.setRealnamePrepose(falg);
						if (StringUtils.isNotBlank(realnameNum)) {
							OrderUtils.copyRealname(orderNum, realnameNum);
							OrderUtils.saveChange(mobile_thdOrder, "订单已实名", null, "系统", null);
							if(!Constant.MOBILE_SUBMIT_QUALITY.contains(qualityCheck.getQualityCheck())){
								ThdProduct product = Services.getThdProductService().createHelper().getThdOrder().Eq(mobile_thdOrder).limit(1).uniqueResult();
								ThdProductTemplate productTemplate = Services.getThdProductTemplateService().createHelper().getId()
										.Eq(Long.parseLong(product.getSkuCode())).uniqueResult();
								//分销商查字典
								List<String> users = Services.getDictionaryService().createHelper()
										.enterDictionary().getKeyName().Eq(Constant.MOBILE_ZTTOAI).back2Dictionary()
										.getStatus().Eq(true).getKeyValue().Eq(mobile_thdOrder.getThdUserByUserId().getUsername()).getKeyValue().listProperty(false);
								//自提 sku不为空 
								if(Constant.DELIVERY_TYPE_ZT.equals(deliveryinfo.getDeliveryType()) && StringUtils.isNotBlank(productTemplate.getSkuCode()) && users.size()>0){
									//推送AI
									order2AiService.pushOrderToAi(mobile_thdOrder.getOrderNum(),CreateOrder.this);
								}else{
									//配送详细地址识别重复的省市区填写内容，如果把质量校验单独提取出来，则不需要在这里判断
									boolean flag = true;
									String result = orderUtils.distinctAddress(mobile_thdOrder.getOrderNum());
									if(StringUtils.isNotBlank(result)){
										flag = false;
									}
									if(flag){
										//广州市内配送地址黑名单校验，如果把质量校验单独提取出来，则不需要在这里判断
//										if(StringUtils.isBlank(orderUtils.guangzhouDelivery(mobile_thdOrder.getOrderNum()))){
////											mobile_thdOrder.setStatus(OrderStatus.NOT_ACCEPT);
//											isdelivery=true;
//										}else{
											mobile_thdOrder.setStatus(OrderStatus.NOT_SUBMIT);
											OrderUtils.saveChange(mobile_thdOrder, "待提交",result+"。订单流转到待提交", "系统", null);
//										}	
									}else{
										mobile_thdOrder.setStatus(OrderStatus.NOT_SUBMIT);
										OrderUtils.saveChange(mobile_thdOrder, "待提交","配送详细地址识别重复的省市区不通过："+result+"。订单流转到待提交", "系统", null);
									}
								}
								orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
								Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
							}
						}else{
							OrderUtils.saveChange(mobile_thdOrder, "订单未实名,提交[待提交]状态", null, "系统", null);
							//发短信
							ThdUser user = new ThdUser();
							user.setUsername("system");
							user.setFullname("系统");
							if(!Constant.BLACK_STATE_1.equals(qualityCheck1.getQualityCheck())){
								//发短信
								String content = gzctOrderSMSBuss.sendSms(to.getOrderNum(), Constant.SMS_MODEL_PREPAIDGIFTREALNAME
										, to.getThdOrderCustomerinfo().getCustomerPhone(), null,to.getOrderSourceNum(), null, user, null, null, null, null);
								if(content == null){
									//删除该短信
									content = "发送短信异常";
								}
								if(content.equals("TriesLimit")){
									//删除该短信
									content = "发送短信超出限制次数";
								}
								/**记录订单快照begin*/
								OrderUtils.saveChange(to, "短信催实名", content, "系统",null);
							}
						}
						Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
					}else{
						if(!Constant.MOBILE_SUBMIT_QUALITY.contains(qualityCheck.getQualityCheck())){
							//配送详细地址识别重复的省市区填写内容，如果把质量校验单独提取出来，则不需要在这里判断
							boolean flag = true;
							String result = orderUtils.distinctAddress(to.getOrderNum());
							if(StringUtils.isNotBlank(result)){
								flag = false;
							}
							if(flag){
								//广州市内配送地址黑名单校验，如果把质量校验单独提取出来，则不需要在这里判断
//								result = orderUtils.guangzhouDelivery(mobile_thdOrder.getOrderNum());
//								if(StringUtils.isBlank(result)){
//									mobile_thdOrder.setStatus(OrderStatus.NOT_ACCEPT);
//									isdelivery=true;
//								}else{
									mobile_thdOrder.setStatus(OrderStatus.NOT_SUBMIT);
									OrderUtils.saveChange(mobile_thdOrder, "待提交",result+"。订单流转到待提交", "系统", null);
//								}	
							}else{
								mobile_thdOrder.setStatus(OrderStatus.NOT_SUBMIT);
								OrderUtils.saveChange(mobile_thdOrder, "待提交","配送详细地址识别重复的省市区不通过："+result+"。订单流转到待提交", "系统", null);
							}
						}
//						mobile_thdOrder.setRealnamePrepose(false);
						mobile_thdOrder.setRealnamePrepose(falg);
						Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
						orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
					}
				}
				//如果黑名单和实名前置都关联了
				if(to.getDeliveryOrder()!=null){
					if (isdelivery && to.getDeliveryOrder()) {
						if(Constant.DELIVERY_TYPE_TD.equals(deliveryinfo.getDeliveryType())){
							ThdProduct thdProduct =  Services.getThdProductService().createHelper().getThdOrder().Eq(mobile_thdOrder).getId().Desc().limit(1).uniqueResult();
							List<ThdProductProperties> thdProductProperties_list = Services.getThdProductPropertiesService().createHelper().getThdProduct().Eq(thdProduct).list();
							String productProperties_phone = null;
							for(ThdProductProperties productProperties:thdProductProperties_list){
								if((Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_CARD_NUMBER+"1").equals(productProperties.getName())){
									productProperties_phone = productProperties.getValue();
									break;
								}
							}
							if(productProperties_phone!=null && StringUtils.isNotBlank(productProperties_phone)){
								//领串号
								String warehouse=Services.getDictionaryService().createHelper().getStatus().Eq(Boolean.TRUE)
										.enterDictionary().getKeyName().Eq("default.logistics").back2Dictionaries().getKeyName().Eq("warehouse").limit(1).uniqueResult().getKeyValue();
								Map<String, Object> result = gzctwmsService.lockProductBySku(null,mobile_thdOrder.getOrderNum(),warehouse,productProperties_phone,null,null,business.getName());
								if(result!=null && Boolean.TRUE.toString().equals(result.get("success")) && result.get("data")!=null){
									JSONObject data = JSONObject.parseObject(result.get("data").toString());
									//保存串号
									if(StringUtils.isNotBlank(data.getString("sn"))){
										ThdProductProperties productProperties = Services.getThdProductPropertiesService().createHelper().getThdProduct().Eq(thdProduct)
												.getName().Eq(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_SN+"1").limit(1).uniqueResult();
										if(productProperties==null){
											productProperties= new ThdProductProperties();
										}
										productProperties.setName(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_SN+"1");
										productProperties.setValue(data.getString("sn"));
										productProperties.setEnabled(true);
										productProperties.setThdProduct(thdProduct);
										Services.getThdProductPropertiesService().saveOrUpdate(productProperties);
									}
									if(StringUtils.isNotBlank(data.getString("crmsn"))){
										
										ThdProductProperties productProperties = Services.getThdProductPropertiesService().createHelper().getThdProduct().Eq(thdProduct)
												.getName().Eq(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_CRM_SN+"1").limit(1).uniqueResult();
										if(productProperties==null){
											productProperties= new ThdProductProperties();
										}
										productProperties.setName(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_CRM_SN+"1");
										productProperties.setValue(data.getString("crmsn"));
										productProperties.setEnabled(true);
										productProperties.setThdProduct(thdProduct);
										Services.getThdProductPropertiesService().saveOrUpdate(productProperties);
									}
									if(StringUtils.isNotBlank(data.getString("skuName"))){
										ThdProductProperties productProperties = Services.getThdProductPropertiesService().createHelper().getThdProduct().Eq(thdProduct)
												.getName().Eq(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_JOINNAME+"1").limit(1).uniqueResult();
										if(productProperties==null){
											productProperties= new ThdProductProperties();
										}
										productProperties.setName(Constant.PRODUCT_PROPERTIES_BASIC+Constant.PRODUCT_JOINNAME+"1");
										productProperties.setValue(data.getString("skuName"));
										productProperties.setEnabled(true);
										productProperties.setThdProduct(thdProduct);
										Services.getThdProductPropertiesService().saveOrUpdate(productProperties);
									}
									OrderUtils.saveChange(mobile_thdOrder, "自动领取串号", "自动领取串号成功", "系统", null);
								}else{
									OrderUtils.saveChange(mobile_thdOrder, "自动领取串号", "自动领取串号失败", "系统", null);
								}
							}else{
								OrderUtils.saveChange(mobile_thdOrder, "自动领取串号", "业务号码缺失，自动领取串号失败", "系统", null);
							}
						}
						final String deliveryNum = deliveryService.createDeliveryByorderNUmber(to.getId());
						mobile_thdOrder.setDeliveryNum(deliveryNum);
						Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
						
						// 获取当前登录用户
						final ThdUser sysuser = Services.getThdUserService().createHelper()
								.enterThdOrdersForUserId().getId().Eq(to.getId()).back2ThdUserByUserId()
								.limit(1).uniqueResult();
						if (StringUtils.isNotBlank(deliveryinfo.getDeliveryType()) && deliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_TD) ) {
							new Thread() {
								@Override
								public void run() {
									deliveryService.doOrderPush(sysuser, deliveryNum,"");
								}
							}.start();
						} else if (StringUtils.isNotBlank(deliveryinfo.getDeliveryType()) && deliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_SELF)) {
							mobile_thdOrder.setStatus(OrderStatus.LOGISTICS_DELIVERY);
							Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
							ThdOrderProcess process = Services.getThdOrderProcessService().createHelper().enterThdOrder().getId().Eq(to.getId()).back2ThdOrderProcess().limit(1).uniqueResult();
							process.setDeliveryPersonUsername("系统");
							process.setDeliveryPersonFullname("系统");
							process.setDeliveryTime(new Date());
							Services.getThdOrderProcessService().saveOrUpdate(process);
							OrderUtils.saveChange(mobile_thdOrder, "代理商自发货流转至物流配送中", null, "系统", null);
						}else if(StringUtils.isNotBlank(deliveryinfo.getDeliveryType()) && deliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_ZT)){
							mobile_thdOrder.setStatus(OrderStatus.DELIVERY_WAIT_ACTIVATE);
							Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
							ThdOrderProcess process = Services.getThdOrderProcessService().createHelper().enterThdOrder().getId().Eq(to.getId()).back2ThdOrderProcess().limit(1).uniqueResult();
							process.setDeliveryPersonUsername("系统");
							process.setDeliveryPersonFullname("系统");
							process.setDeliveryTime(new Date());
							Services.getThdOrderProcessService().saveOrUpdate(process);
							OrderUtils.saveChange(mobile_thdOrder, "自提流转至配送成功【待激活】(", null, "系统", null);
						}else{
							mobile_thdOrder.setStatus(OrderStatus.NOT_ACCEPT);
							Services.getThdOrderService().saveOrUpdate(mobile_thdOrder);
						}
					}
				}
			}
			if(Constant.BUSSINESS_BROADBAND.equals(business.getName())){
				
				//实人信息关联&客户校验通过了
				ThdOrderProcess orderProcess = Services.getThdOrderProcessService().createHelper().enterThdOrder().getOrderNum().Eq(to.getOrderNum()).back2ThdOrderProcess().uniqueResult();
				if(Constant.QUALITY_CHECK_SUCCESS.equals(orderProcess.getQualityCheck()) 
						&& StringUtils.isNotBlank(to.getRealnameNum())){
					ThdProduct tpd = Services.getThdProductService().createHelper().getThdOrder().Eq(to).uniqueResult();
					ThdProductTemplate ttp = Services.getThdProductTemplateService().createHelper().getId()
							.Eq(Long.parseLong(tpd.getSkuCode())).uniqueResult();
					if(ttp!=null && StringUtils.isNotBlank(ttp.getSkuCode())){
						//推单到AI受理助手
						order2AiService.pushOrderToAi(to.getOrderNum(), CreateOrder.this);
					}else{
						to.setStatus(OrderStatus.NOT_ACCEPT);
						Services.getThdOrderService().saveOrUpdate(to);
						OrderUtils.saveChange(to, OrderStatus.ORDER_STATES.get(OrderStatus.NOT_ACCEPT), "用户已通过实名认证", "系统", null);
						/**
						 * 推送消息到消息中心
						 */
						orderChangeImpl.statusChange(to.getOrderNum(),to.getStatus());
					}
				}
				if(StringUtils.isBlank(to.getRealnameNum()) && !Constant.BLACK_STATE_1.equals(qualityCheck1.getQualityCheck())){
					//发短信
					ThdUser user = new ThdUser();
					user.setUsername("system");
					user.setFullname("系统");
					//发短信
					String content = gzctOrderSMSBuss.sendSms(to.getOrderNum(), Constant.SMSMODEL_SMS_31
							, to.getThdOrderCustomerinfo().getCustomerPhone(), null,to.getOrderSourceNum(), null, user, null, null, null, null);
					if(content == null){
						//删除该短信
						content = "发送短信异常";
					}
					if(content.equals("TriesLimit")){
						//删除该短信
						content = "发送短信超出限制次数";
					}
					/**记录订单快照begin*/
					OrderUtils.saveChange(to, "短信催实名", content, "系统",null);
				}
			}
			//融合
			if(Constant.BUSSINESS_COMBINED.equals(business.getName())){
				ThdOrder combined_order = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).limit(1).uniqueResult();
				boolean flag = true;
				String result = orderUtils.distinctAddress(to.getOrderNum());
				if(StringUtils.isNotBlank(result)){
					flag = false;
				}
				if(flag){
					//
					boolean realnameFlag = false;
					if(Constant.RealnamePrepose_QZ.contains(to.getRealnamePrepose())){
						// 检查是否有相关订单
						String realnameNum ="";
						//外部订单号不等雨空 先去关联捆绑实名  预付费礼包
						if(StringUtils.isNotBlank(to.getOrderOutNum())){
							 realnameNum= OrderUtils.checkRealname2(
									to.getThdOrderCustomerinfo().getCustomerName(), 
									to.getThdOrderCustomerinfo().getCustomerCardNum(), 
									to.getThdOrderCustomerinfo().getAgentName(), 
									to.getThdOrderCustomerinfo().getAgentCardNum(),
									Constant.BUSSINESS_COMBINED, to.getOrderNum(), 0,to.getOrderOutNum());
						}
						if(StringUtils.isBlank(realnameNum)){
							 realnameNum = OrderUtils.checkRealname(
									to.getThdOrderCustomerinfo().getCustomerName(), 
									to.getThdOrderCustomerinfo().getCustomerCardNum(), 
									to.getThdOrderCustomerinfo().getAgentName(), 
									to.getThdOrderCustomerinfo().getAgentCardNum(),
									Constant.BUSSINESS_COMBINED, to.getOrderNum(), 0);
						}
						
						
						if (StringUtils.isNotBlank(realnameNum)) {
							combined_order.setRealnameNum(realnameNum);
							to.setRealnameNum(realnameNum);
							OrderUtils.copyRealname(to.getOrderNum(), realnameNum);
							realnameFlag = true;
						} else {
							combined_order.setRealnameNum(null);
							to.setRealnameNum(null);
							realnameFlag = false;
						}
						ThdOrderProcess orderProcess = Services.getThdOrderProcessService().createHelper().enterThdOrder().getOrderNum().Eq(to.getOrderNum()).back2ThdOrderProcess().uniqueResult();
						if( StringUtils.isBlank(realnameNum) && !Constant.BLACK_STATE_1.equals(orderProcess.getQualityCheck())){
							ThdUser user = new ThdUser();
							user.setUsername("system");
							user.setFullname("系统");
							if(!Constant.BLACK_STATE_1.equals(qualityCheck1.getQualityCheck())){
								//发短信
								String content = gzctOrderSMSBuss.sendSms(to.getOrderNum(), Constant.SMSMODEL_SMS_31
										, to.getThdOrderCustomerinfo().getCustomerPhone(), null,to.getOrderSourceNum(), null, user, null, null, null, null);
								if(content == null){
									//删除该短信
									content = "发送短信异常";
								}
								if(content.equals("TriesLimit")){
									//删除该短信
									content = "发送短信超出限制次数";
								}
								/**记录订单快照begin*/
								OrderUtils.saveChange(to, "短信催实名", content, "系统",null);
							}
						}
					}else{
						realnameFlag = true;
					}
					if(realnameFlag){
						if(!Constant.BLACK_STATE_1.equals(qualityCheck1.getQualityCheck())){
							if((Constant.RealnamePrepose_QZ.contains(to.getRealnamePrepose()) && StringUtils.isNotBlank(to.getRealnameNum()))|| !(Constant.RealnamePrepose_QZ.contains(to.getRealnamePrepose()))){
								if(combinedUtil.IsToAI(to.getOrderNum())){
									//领取串号
									combinedUtil.lockIccid(to.getId());
									//推送AI
									order2AiService.pushOrderToAi(to.getOrderNum(),CreateOrder.this);
								}else{
									combined_order.setStatus(OrderStatus.NOT_ACCEPT);
									Services.getThdOrderService().saveOrUpdate(combined_order);
									orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
								}
							}else{
								combined_order.setStatus(OrderStatus.NOT_SUBMIT);
								Services.getThdOrderService().saveOrUpdate(combined_order);
								orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
							}
						}
					}
				}else{
					OrderUtils.saveChange(to, "待提交", "配送详细地址识别重复的省市区不通过", "系统",null);
				}
			}
		}
		//移动产品：下单>>待分销商审核>>(审核通过)待受理，(审核不通过)作废。
		//哪些分销商账号 ？？？ key_name mobile.agent.auditcheck
		//WebUser currentUser = UserThreadLocal.get();
		//该业务的key
		ThdOrder mobileorder = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum)
				.uniqueResult();
		if(Constant.BUSINESS_MOBILE.equals(business.getName())&&mobileorder.getStatus().equals(OrderStatus.NOT_ACCEPT)){
			String dictionary_keyName="mobile.agent.auditcheck";
			ThdOrder mobileorder2 = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).enterThdUserByUserId().back2ThdOrdersForUserId()
					.uniqueResult();	
			String username =mobileorder2.getThdUserByUserId().getUsername();
			List<Dictionary> distributors = Services.getDictionaryService().createHelper().getKeyName().Eq(dictionary_keyName).getStatus().Eq(true).getKeyValue().Eq(username).list();
			if(distributors!=null && distributors.size()>0){
				//移动业务特定分销商账号，下单
				mobileorder.setStatus(OrderStatus.AGENT_CHECK);
				Services.getThdOrderService().update(mobileorder);
				OrderUtils.saveChange(mobileorder, "订单状态流转到待分销商审核【移动业务特定分销商账号下单】", null, "系统", null);
				orderChangeImpl.statusChange(to.getOrderNum(), to.getStatus());
				//106 您负责的企业有新入网申请，请登陆广州智能平台小程序审核订单。
				 ThdJobNumber jobNumber = Services.getThdJobNumberService().createHelper().
				    		enterThdAgentUnit().enterThdUsers().getUsername().Eq(username).
				    		back2ThdAgentUnit().back2ThdJobNumbers().getNumber().Eq(mobileorder.getLanId()).getEnabled().Eq(true).limit(1).uniqueResult();
			    if(jobNumber!=null&&StringUtils.isNotBlank(jobNumber.getPhone())){
			    	try{
			    		gzctFnSmsService.send(jobNumber.getPhone(), Constant.SMSMODEL_SMS_106, null, mobileorder.getOrderNum(), "系统");			    			
			    	}catch(Exception e){
			    		logger.error("移动业务特定分销商账号下单后，对揽装工号对应的手机号发送短信异常", e);
			    		OrderUtils.saveChange(mobileorder, "移动业务特定分销商账号下单", "对揽装工号对应的手机号发送短信异常", "系统", null);
			    	}			    	
			    }else{
			    	logger.error("移动业务特定分销商账号下单后，对揽装工号对应的手机号发送短信失败，手机号为空");
			    	OrderUtils.saveChange(mobileorder, "移动业务特定分销商账号下单", "移动业务特定分销商账号下单后，对揽装工号对应的手机号发送短信失败，手机号为空", "系统", null);
			    }
			}					
		}
		
		if(qualityCheck1.getQualityCheck().equals(Constant.QUALITY_CHECK_SUCCESS) && 
				Constant.BUSINESS_REPLACEPACKAGE.equals(business.getName())){
			// 新起线程关联穗易付订单
			imarketGzctpay.setGzctpayOrder(to.getOrderNum());
			//实名前置
			String frontRealname_msg = orderUtils.frontRealname(to.getOrderNum());
			if(frontRealname_msg==null){
				replacepackageUtil.replacepackageVerificationPass(to.getOrderNum());
			}
		}
	}

	public String _execute() throws Exception {

		return "";
	}

	HttpServletRequest request;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public File getSfzzmFile() {
		return sfzzmFile;
	}

	public void setSfzzmFile(File sfzzmFile) {
		this.sfzzmFile = sfzzmFile;
	}

	public String getSfzzmFileContentType() {
		return sfzzmFileContentType;
	}

	public void setSfzzmFileContentType(String sfzzmFileContentType) {
		this.sfzzmFileContentType = sfzzmFileContentType;
	}

	public String getSfzzmFileFileName() {
		return sfzzmFileFileName;
	}

	public void setSfzzmFileFileName(String sfzzmFileFileName) {
		this.sfzzmFileFileName = sfzzmFileFileName;
	}

	public File getSfzbmFile() {
		return sfzbmFile;
	}

	public void setSfzbmFile(File sfzbmFile) {
		this.sfzbmFile = sfzbmFile;
	}

	public String getSfzbmFileContentType() {
		return sfzbmFileContentType;
	}

	public void setSfzbmFileContentType(String sfzbmFileContentType) {
		this.sfzbmFileContentType = sfzbmFileContentType;
	}

	public String getSfzbmFileFileName() {
		return sfzbmFileFileName;
	}

	public void setSfzbmFileFileName(String sfzbmFileFileName) {
		this.sfzbmFileFileName = sfzbmFileFileName;
	}

	public File getSfzscFile() {
		return sfzscFile;
	}

	public void setSfzscFile(File sfzscFile) {
		this.sfzscFile = sfzscFile;
	}

	public String getSfzscFileContentType() {
		return sfzscFileContentType;
	}

	public void setSfzscFileContentType(String sfzscFileContentType) {
		this.sfzscFileContentType = sfzscFileContentType;
	}

	public String getSfzscFileFileName() {
		return sfzscFileFileName;
	}

	public void setSfzscFileFileName(String sfzscFileFileName) {
		this.sfzscFileFileName = sfzscFileFileName;
	}

	public File getSfzsc2File()
	{
		return sfzsc2File;
	}

	public void setSfzsc2File(File sfzsc2File)
	{
		this.sfzsc2File = sfzsc2File;
	}

	public String getSfzsc2FileContentType()
	{
		return sfzsc2FileContentType;
	}

	public void setSfzsc2FileContentType(String sfzsc2FileContentType)
	{
		this.sfzsc2FileContentType = sfzsc2FileContentType;
	}

	public String getSfzsc2FileFileName()
	{
		return sfzsc2FileFileName;
	}

	public void setSfzsc2FileFileName(String sfzsc2FileFileName)
	{
		this.sfzsc2FileFileName = sfzsc2FileFileName;
	}

	public File getYyzzFile() {
		return yyzzFile;
	}

	public void setYyzzFile(File yyzzFile) {
		this.yyzzFile = yyzzFile;
	}

	public String getYyzzFileContentType() {
		return yyzzFileContentType;
	}

	public void setYyzzFileContentType(String yyzzFileContentType) {
		this.yyzzFileContentType = yyzzFileContentType;
	}

	public String getYyzzFileFileName() {
		return yyzzFileFileName;
	}

	public void setYyzzFileFileName(String yyzzFileFileName) {
		this.yyzzFileFileName = yyzzFileFileName;
	}

	public File getThzhxxFile() {
		return thzhxxFile;
	}

	public void setThzhxxFile(File thzhxxFile) {
		this.thzhxxFile = thzhxxFile;
	}

	public String getThzhxxFileContentType() {
		return thzhxxFileContentType;
	}

	public void setThzhxxFileContentType(String thzhxxFileContentType) {
		this.thzhxxFileContentType = thzhxxFileContentType;
	}

	public String getThzhxxFileFileName() {
		return thzhxxFileFileName;
	}

	public void setThzhxxFileFileName(String thzhxxFileFileName) {
		this.thzhxxFileFileName = thzhxxFileFileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	@Override
	public void orderToAiSuccess(String orderNum, String type) {
		if (Constant.AICREATE.equals(type)) {
			ThdOrder order = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).getThdOrder().IsNull().uniqueResult();
			ThdBusinessTag business = Services.getThdBusinessTagService().createHelper().enterThdProducts().enterThdOrder()
					.getOrderNum().Eq(orderNum).back2ThdProducts().back2ThdBusinessTag().uniqueResult();
			order.setStatus(OrderStatus.AI_ACCEPTING);
			if(Constant.BUSSINESS_BROADBAND.equals(business.getName()) 
					|| Constant.BUSSINESS_COMBINED.equals(business.getName()) 
					|| Constant.BUSINESS_MOBILE.equals(business.getName())
					|| Constant.BUSINESS_REPLACEPACKAGE.equals(business.getName())){
//				order.setStatus(OrderStatus.AI_ACCEPTING);
				Services.getThdOrderService().update(order);
				ThdAcceptFlow acceptFlow = Services.getThdAcceptFlowService().createHelper().getOrderNum().Eq(orderNum)
						.getCreateTime().Desc().limit(1).uniqueResult();
				String acceptFlowNum = acceptFlow == null ? "" : acceptFlow.getAcceptFlowNum();
				OrderUtils.saveChange(order, OrderStatus.ORDER_STATES.get(order.getStatus()), "下单到AI平台成功，受理流水号：" + acceptFlowNum, "系统", null);
			}else{
//				order.setStatus(OrderStatus.NOT_ACCEPT);
				Services.getThdOrderService().update(order);
				ThdAcceptFlow acceptFlow = Services.getThdAcceptFlowService().createHelper().getOrderNum().Eq(orderNum)
						.getCreateTime().Desc().limit(1).uniqueResult();
				String acceptFlowNum = acceptFlow == null ? "" : acceptFlow.getAcceptFlowNum();
				OrderUtils.saveChange(order, OrderStatus.ORDER_STATES.get(order.getStatus()), "下单到AI平台成功，受理流水号：" + acceptFlowNum, "系统", null);

			}
			/**
			 * 推送消息到消息中心
			 */
			orderChangeImpl.statusChange(order.getOrderNum(), order.getStatus());
		}
	}

	@Override
	public void orderToAiFailure(String orderNum, String type, String msg) {
		if (Constant.AICREATE.equals(type)) {
			ThdOrder order = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum).getThdOrder().IsNull().uniqueResult();
			int orderStatus = OrderStatus.WAIT_ACCEPT;
			
			ThdProduct tpd = Services.getThdProductService().createHelper().getThdOrder().Eq(order).uniqueResult();
			if(tpd != null && StringUtils.isNotBlank(tpd.getSkuCode())){
				String key = "product.AIfail.waitCharge";
				Dictionary productAIfail = Services.getDictionaryService().createHelper().getKeyName().Eq(key).getKeyValue().Eq(tpd.getSkuCode()).uniqueResult();
				if(productAIfail!=null){
					orderStatus = OrderStatus.WAIT_CHARGE;
				}
				
			}
			ThdBusinessTag business = Services.getThdBusinessTagService().createHelper().enterThdProducts().enterThdOrder()
					.getOrderNum().Eq(orderNum).back2ThdProducts().back2ThdBusinessTag().uniqueResult();
			if(Constant.BUSSINESS_COMBINED.equals(business.getName()) || business.getName().equals(Constant.BUSINESS_REPLACEPACKAGE)){
				orderStatus = OrderStatus.NOT_ACCEPT;
			}
			order.setStatus(orderStatus);
			Services.getThdOrderService().update(order);

			ThdOrderProcess process = order.getThdOrderProcess();
			process.setWaitAcceptFullname("系统");
			process.setWaitAcceptUsername("admin");
			process.setWaitAcceptRemark("下单到AI平台失败 " + msg);
			process.setWaitAcceptTime(new Date());
			Services.getThdOrderProcessService().update(process);

			ThdAcceptFlow acceptFlow = Services.getThdAcceptFlowService().createHelper().getOrderNum().Eq(orderNum)
					.getCreateTime().Desc().limit(1).uniqueResult();
			String acceptFlowNum = acceptFlow == null ? "" : acceptFlow.getAcceptFlowNum();
			OrderUtils.saveChange(order, OrderStatus.ORDER_STATES.get(order.getStatus()), "下单到AI平台失败 " + msg + ",受理流水号：" + acceptFlowNum, "系统", null);

			/**
			 * 推送消息到消息中心
			 */
			orderChangeImpl.statusChange(order.getOrderNum(), order.getStatus());

		}

	}

	@Override
	public void ocrVerify(String orderNum) {
		// TODO Auto-generated method stub
		// 先创建蓝单编号放着
		// 获取当前登录用户
		ThdOrder orderDb = Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum)
				.enterThdUserByUserId().back2ThdOrdersForUserId().uniqueResult();		

		ThdOrderCustomerinfo customerinfo = Services.getThdOrderCustomerinfoService()
				.findById(orderDb.getId());
		ThdUser user = orderDb.getThdUserByUserId();
		ThdBusinessTag tag = Services.getThdBusinessTagService().createHelper().enterThdProducts()
				.enterThdOrder().getOrderNum().Eq(orderNum).back2ThdProducts().back2ThdBusinessTag().limit(1)
				.uniqueResult();
		ThdOrderDeliveryinfo deliveryinfo = Services.getThdOrderDeliveryinfoService().findById(orderDb.getId());			
		
		if(Constant.BUSINESS_MOBILE.equals(tag.getName())){
			orderChangeImpl.statusChange(orderDb.getOrderNum(), orderDb.getStatus());
		}
		
		// 保存订单时证件类型如果是身份证且证件号码不为空则校验订单是否实名
		if (("身份证").equals(customerinfo.getCustomerCardType())
				&& customerinfo.getCustomerCardNum() != null && Services.getThdOrderService().createHelper()
				.getOrderNum().Eq(orderNum).uniqueResult().getStatus().intValue() != 50) {// 待提交
			// 黑名单校验
			blackUserBuss.orderCreateNew(CreateOrder.this, orderNum,
					customerinfo.getCustomerCardNum(), user.getUsername());
		} else if(Services.getThdOrderService().createHelper()
				.getOrderNum().Eq(orderNum).uniqueResult().getStatus().intValue() != 50){
			if(Constant.BUSINESS_MOBILE.equals(tag.getName())){
				orderChangeImpl.statusChange(orderDb.getOrderNum(), orderDb.getStatus());
				
				ThdOrderProcess orderProcess = Services.getThdOrderProcessService().createHelper().enterThdOrder().getOrderNum().Eq(orderNum).back2ThdOrderProcess().uniqueResult();
				if(!Constant.BLACK_STATE_1.equals(orderProcess.getQualityCheck()) && StringUtils.isBlank(orderDb.getRealnameNum())){
					//发短信
					ThdUser sysuser = new ThdUser();
					sysuser.setUsername("system");
					sysuser.setFullname("系统");
					//发短信
					String content = gzctOrderSMSBuss.sendSms(orderDb.getOrderNum(), Constant.SMS_MODEL_PREPAIDGIFTREALNAME
							, orderDb.getThdOrderCustomerinfo().getCustomerPhone(), null,orderDb.getOrderSourceNum(), null, sysuser, null, null, null, null);
					if(content == null){
						//删除该短信
						content = "发送短信异常";
					}
					if(content.equals("TriesLimit")){
						//删除该短信
						content = "发送短信超出限制次数";
					}
					/**记录订单快照begin*/
					OrderUtils.saveChange(orderDb, "短信催实名", content, "系统",null);
				}
			}else{
				// 推物流标识
				boolean isdelivery = true;
				// 查询该订单是否已有实名关联
				List<ThdOrderRealname> realnames = Services.getThdOrderRealnameService().createHelper()
						.getThdOrder().Eq(orderDb).getSmrzType().NeOrNull(Constant.SMRZ_TYPE_PAGE).list();
				if (realnames != null && realnames.size() == 0) {
					int falg = OrderUtils.realnamePreposeCheck(orderDb.getOrderNum(),tag.getName());
					ThdOrderProcess process = Services.getThdOrderProcessService().createHelper()
							.getId().Eq(orderDb.getId())
							.startOr()
							.getBlackUserState().In("正常","接口异常","无客户信息","黑名单接口异常")
							.getBlackUserState().IsNull()
							.stopOr()
							.uniqueResult();
					if(falg==1){
//						orderDb.setRealnamePrepose(false);
						orderDb.setRealnamePrepose(falg);
						Services.getThdOrderService().saveOrUpdate(orderDb);
						// 检查是否有相关订单
						String realnameNum = OrderUtils.checkRealname(customerinfo.getCustomerName(),
								customerinfo.getCustomerCardNum(), customerinfo.getAgentName(),
								customerinfo.getAgentCardNum(), Constant.BUSSINESS_PREPAIDGIFT,
								orderDb.getOrderNum(), 1);
						if (StringUtils.isNotBlank(realnameNum)) {
							// 如果有相关的实名制订单
							OrderUtils.copyRealname(orderDb.getOrderNum(), realnameNum);
							OrderUtils.saveChange(orderDb, "订单已实名", null, "系统", null);
							if(process!=null){

							}else{
								//停留待提交
								isdelivery = false;
								orderDb.setStatus(OrderStatus.NOT_SUBMIT);
								Services.getThdOrderService().update(orderDb);
								//发短信
								ThdUser sysuser = new ThdUser();
								sysuser.setUsername("system");
								sysuser.setFullname("系统");
								//发短信
								String content = gzctOrderSMSBuss.sendSms(orderDb.getOrderNum(), Constant.SMS_MODEL_PREPAIDGIFTREALNAME
										, customerinfo.getCustomerPhone(), null,orderDb.getOrderSourceNum(), null, user, null, null, null, null);
								if(content == null){
									//删除该短信
									content = "发送短信异常";
								}
								if(content.equals("TriesLimit")){
									//删除该短信
									content = "发送短信超出限制次数";
								}
								/**记录订单快照begin*/
								OrderUtils.saveChange(orderDb, "短信催实名", content, "系统",null);
							}
						} else {
							// 关联不上不推物流 留在待提交
							isdelivery = false;
							orderDb.setStatus(OrderStatus.NOT_SUBMIT);
							Services.getThdOrderService().update(orderDb);
							OrderUtils.saveChange(orderDb, "订单未实名,提交[待提交]状态", null, "系统", null);
						}
					}else{
//						orderDb.setRealnamePrepose(true);
						orderDb.setRealnamePrepose(falg);
						Services.getThdOrderService().saveOrUpdate(orderDb);
						if(process!=null){

						}else{
							isdelivery = false;
						}
					}
				}
				if (isdelivery) {
					if (deliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_TD)) {
						if (Services.getThdOrderService().createHelper().getOrderNum().Eq(orderNum)
								.uniqueResult().getStatus() == 12 && StringUtils.isNotBlank(result)) {
							final ThdUser sysuser = Services.getThdUserService().createHelper().getUsername()
									.Eq(user.getUsername()).limit(1).uniqueResult();

							new Thread() {
								@Override
								public void run() {
									deliveryService.doOrderPush(sysuser, result, "");
								}
							}.start();
						}
					} else if (deliveryinfo.getDeliveryType().equals(Constant.DELIVERY_TYPE_SELF)) {
						ThdOrderProcess process = Services.getThdOrderProcessService().findById(orderDb.getId());
						process.setDeliveryPersonUsername("系统");
						process.setDeliveryPersonFullname("系统");
						process.setDeliveryTime(new Date());
						Services.getThdOrderProcessService().saveOrUpdate(process);
						orderDb.setStatus(OrderStatus.LOGISTICS_DELIVERY);
						Services.getThdOrderService().update(orderDb);
					}
				}
			}
		}
	}
	
	
}
