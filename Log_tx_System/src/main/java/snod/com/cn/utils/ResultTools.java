package snod.com.cn.utils;

import java.util.Map;

public class ResultTools {
	/****
	 * 错误码记录：
	 * 0--------成功 
	 * 1000-----[服务器]运行时异常
	 * 1001-----[服务器]空值异常
	 * 1002-----[服务器]数据类型转换异常
	 * 1003-----[服务器]IO异常
	 * 1004-----[服务器]未知方法异常
	 * 1005-----[服务器]数组越界异常
	 * 1006-----[服务器]网络异常
	 * 
	 * 2001-----邮箱未注册
	 * 2002-----该邮箱已注册
	 * 2003-----用户名或密码错误
	 * 2004-----用户帐号冻结
	 * 2005-----用户信息编辑失败
	 * 2006-----用户信息失效，请重新获取
	 * 2007-----注册失败
	 * 2008-----超过登录次数限制
	 * 2009-----邮箱或密码不能为空
	 * 2010-----密码修改失败
	 * 2011-----该用户不存在
	 * 2012-----查询条件不能为空
	 * 2013-----邮箱不能为空
	 * 2014-----角色名称不能为空
	 * 2015-----角色分组名称已存在
	 * 2016-----头像下载失败，该用户头像不存在
	 * 2017-----邮箱格式不合法，请核对后重新输入
	 * 2018-----密码格式不合法，请核对后重新输入
	 * 2019-----公司名称不能包含特殊字符，请核对后重新输入
	 * 2020-----部门名称不能包含特殊字符，请核对后重新输入
	 * 2021-----职称不能包含特殊字符，请核对后重新输入
	 * 2022-----手机号不能包含特殊字符，请核对后重新输入
	 * 2023-----手机号超过长度限制，请核对后重新输入
	 * 2024-----头像上传失败，头像不能为空
	 * 
	 * 3001-----验证码发送失败
	 * 3002-----验证码失效
	 * 3003-----验证码错误
	 * 3004-----验证码不可用
	 * 3005-----短信平台异常
	 * 3006-----验证码超过发送次数限制
	 * 
	 * 4001-----参数不合法
	 * 4002-----无效的Token
	 * 4003-----无操作权限
	 * 4004-----请重新登录
	 * 4005-----token为空
	 * 4006-----key为空
	 * 4007-----无效的key
		
	 * 5001-----会议ID不存在
	 * 5002-----该用户不是会议创建者，没权限取消会议
	 * 5003-----该用户不是会议创建者，没权限开始会议
	 * 5004-----该用户不是会议创建者，没权限结束会议
	 * 5005-----取消会议失败，只能取消状态为“已创建”的会议
	 * 5006-----开始会议失败，只能开始状态为“已创建”的会议
	 * 5007-----结束会议失败，只能结束状态为“已创建”的会议.
	 * 5008-----会议ID不能为空
	 * 
	 * 
	 * 6001-----添加联系人失败
	 * 
	 * 7001-----翻译文件不存在，下载失败
	 * 7002-----内容文件不存在，下载失败
	 * 7003-----录音文件不存在，下载失败
	 * 
	 * 
	 * 8001-----文件分片上传成功
	 * 8002-----文件上传成功
	 */
	
	 /**
     * 请求成功
     */
    public static final int ERROR_CODE_0 =0;
	 /**
     * [服务器]运行时异常
     */
    public static final int ERROR_CODE_1000 =1000;
    /**
     * [服务器]空值异常
     */
    public static final int ERROR_CODE_1001 =1001; 
    /**
     * [服务器]数据类型转换异常
     */
    public static final int ERROR_CODE_1002 =1002;
    /**
     * [服务器]IO异常
     */
    public static final int ERROR_CODE_1003 =1003;
    /**
     * [服务器]未知方法异常
     */
    public static final int ERROR_CODE_1004 =1004;
    /**
     * [服务器]数组越界异常
     */
    public static final int ERROR_CODE_1005 =1005;
    /**
     * [服务器]网络异常
     */
    public static final int ERROR_CODE_1006 =1006;
    /**
     * 邮箱未注册
     */
    public static final int ERROR_CODE_2001 =2001;
    /**
     * 该邮箱已注册
     */
    public static final int ERROR_CODE_2002 =2002;
    /**
     * 用户名或密码错误
     */
    public static final int ERROR_CODE_2003 =2003;
    /**
     * 用户帐号冻结
     */
    public static final int ERROR_CODE_2004 =2004;
    /**
     * 用户信息编辑失败
     */
    public static final int ERROR_CODE_2005 =2005;
    /**
     * 用户信息失效，请重新获取
     */
    public static final int ERROR_CODE_2006 =2006;
    /**
     * 注册失败
     */
    public static final int ERROR_CODE_2007 =2007;
    /**
     * 超过登录次数限制
     */
    public static final int ERROR_CODE_2008 =2008;
    /**
     * 邮箱或密码不能为空
     */
    public static final int ERROR_CODE_2009 =2009;
    /**
     * 密码修改失败
     */
    public static final int ERROR_CODE_2010 =2010;
    /**
     * 该用户不存在
     */
    public static final int ERROR_CODE_2011 =2011;
    /**
     * 查询条件不能为空
     */
    public static final int ERROR_CODE_2012 =2012;
    
    /**
     * 邮箱不能为空
     */
    public static final int ERROR_CODE_2013 =2013;
    
    /**
     * 角色名称不能为空
     */
    public static final int ERROR_CODE_2014 =2014;
    
    /**
     * 角色分组名称已存在
     */
    public static final int ERROR_CODE_2015 =2015;
    /**
     * 头像下载失败，该用户头像不存在
     */
    public static final int ERROR_CODE_2016 =2016;
    /**
     * 邮箱格式不合法，请核对后重新输入
     */
    public static final int ERROR_CODE_2017 =2017;
    /**
     * 密码格式不合法，请核对后重新输入
     */
    public static final int ERROR_CODE_2018 =2018;
    /**
     * 公司名称不能包含特殊字符，请核对后重新输入
     */
    public static final int ERROR_CODE_2019 =2019;
    /**
     * 部门名称不能包含特殊字符，请核对后重新输入
     */
    public static final int ERROR_CODE_2020 =2020;
    /**
     * 职称不能包含特殊字符，请核对后重新输入
     */
    public static final int ERROR_CODE_2021 =2021;
    /**
     * 手机号不能包含特殊字符，请核对后重新输入
     */
    public static final int ERROR_CODE_2022 =2022;
    /**
     * 手机号超过长度限制，请核对后重新输入
     */
    public static final int ERROR_CODE_2023 =2023;
    
    /**
     * 头像上传失败，头像不能为空
     */
    public static final int ERROR_CODE_2024 =2024;
    /**
     * 验证码发送失败
     */
    public static final int ERROR_CODE_3001 =3001;
    /**
     * 验证码失效
     */
    public static final int ERROR_CODE_3002 =3002;
    /**
     * 验证码错误
     */
    public static final int ERROR_CODE_3003 =3003;
    /**
     * 验证码不可用
     */
    public static final int ERROR_CODE_3004 =3004;
    /**
     * 短信平台异常
     */
    public static final int ERROR_CODE_3005 =3005;
    /**
     * 验证码超过发送次数限制
     */
    public static final int ERROR_CODE_3006 =3006;
    
    /**
     * 参数不合法
     */
    public static final int ERROR_CODE_4001 =4001;
    /**
     * 无效的Token
     */
    public static final int ERROR_CODE_4002 =4002;
    /**
     * 无操作权限
     */
    public static final int ERROR_CODE_4003 =4003;
    /**
     * 请重新登录
     */
    public static final int ERROR_CODE_4004 =4004;
    
    /**
     * token为空
     */
    public static final int ERROR_CODE_4005 =4005;
    /**
     * key为空
     */
    public static final int ERROR_CODE_4006 =4006;
    /**
     * 无效的key
     */
    public static final int ERROR_CODE_4007 =4007;
    
    /**
     * 会议ID不存在
     */
    public static final int ERROR_CODE_5001 =5001;

    
    /**
     * 该用户不是会议创建者，没权限取消会议
     */
    public static final int ERROR_CODE_5002 =5002;
    /**
     * 该用户不是会议创建者，没权限开始会议
     */
    public static final int ERROR_CODE_5003 =5003;
    /**
     * 该用户不是会议创建者，没权限结束会议
     */
    public static final int ERROR_CODE_5004 =5004;
    
    /**
     * 取消会议失败，只能取消状态为“已创建”的会议
     */
    public static final int ERROR_CODE_5005 =5005;
    
    /**
     * 开始会议失败，只能开始状态为“已创建”的会议
     */
    public static final int ERROR_CODE_5006 =5006;
    
    /**
     * 结束会议失败，只能结束状态为“进行中”的会议
     */
    public static final int ERROR_CODE_5007 =5007;
    /**
     * 会议ID不能为空
     */
    public static final int ERROR_CODE_5008 =5008;
    
    /**
     * 添加联系人失败
     */
    public static final int ERROR_CODE_6001 =6001;
    /**
     * 翻译文件不存在，下载失败
     */
    public static final int ERROR_CODE_7001 =7001;
    /**
     * 内容文件不存在，下载失败
     */
    public static final int ERROR_CODE_7002 =7002;
    /**
     * 录音文件不存在，下载失败
     */
    public static final int ERROR_CODE_7003 =7003;
    
    /**
     * 文件分片上传成功
     */
    public static final int ERROR_CODE_8001 =8001;
    /**
     * 文件上传成功
     */
    public static final int ERROR_CODE_8002 =8002;
    
    /**
     * 服务器异常
     */
    public static final int ERROR_CODE_404 =404;
	
 
	/**
	 * @param Errcode--返回码
	 * @param Errmsg---404服务器内部异常时提示消息(返回码不是404时传空即可)
	 * @param map------数据源
	 * @return
	 */
	public static ResultInfo result(int Errcode, String Errmsg, Map<String, Object> map) {
		ResultInfo model = new ResultInfo();
		model.setCode(Errcode);
		switch (Errcode) {
		case 0:
			model.setMessage("成功");
			if (map != null) {
				model.setData(map);
			}
			break;
		case 1000:
			model.setMessage("[服务器]运行时异常 ");
			break;
		case 1001:
			model.setMessage("[服务器]空值异常 ");
			break;
		case 1002:
			model.setMessage("[服务器]数据类型转换异常 ");
			break;
		case 1003:
			model.setMessage("[服务器]IO异常");
			break;
		case 1004:
			model.setMessage("[服务器]未知方法异常");
			break;
		case 1005:
			model.setMessage("[服务器]数组越界异常");
			break;
		case 1006:
			model.setMessage("[服务器]网络异常");
			break;
		case 2001:
			model.setMessage("邮箱未注册");
			break;
		case 2002:
			model.setMessage("该邮箱已注册");
			break;
		case 2003:
			model.setMessage("用户名或密码错误");
			break;
		case 2004:
			model.setMessage("用户帐号冻结");
			break;
		case 2005:
			model.setMessage("用户信息编辑失败");
			break;
		case 2006:
			model.setMessage("用户信息失效，请重新获取");
			break;
		case 2007:
			model.setMessage("注册失败");
			break;
		case 2008:
			model.setMessage("超过登录次数限制");
			break;
		case 2009:
			model.setMessage("邮箱或密码不能为空");
			break;
		case 2010:
			model.setMessage("密码修改失败");
			break;	
		case 2011:
			model.setMessage("该用户不存在");
			break;
		case 2012:
			model.setMessage("查询条件不能为空");
			break;
		case 2013:
			model.setMessage("邮箱不能为空");
			break;
		case 2014:
			model.setMessage("角色名称不能为空");
			break;
		case 2015:
			model.setMessage("角色分组名称已存在");
			break;
		case 2016:
			model.setMessage("头像下载失败，该用户头像不存在");
			break;
		case 2017:
			model.setMessage("邮箱格式不合法，请核对后重新输入");
			break;
		case 2018:
			model.setMessage("密码格式不合法，请核对后重新输入");
			break;
		case 2019:
			model.setMessage("公司名称不能包含特殊字符，请核对后重新输入");
			break;
		case 2020:
			model.setMessage("部门名称不能包含特殊字符，请核对后重新输入");
			break;
		case 2021:
			model.setMessage("职称不能包含特殊字符，请核对后重新输入");
			break;
		case 2022:
			model.setMessage("手机号不能包含特殊字符，请核对后重新输入");
			break;
		case 2023:
			model.setMessage("手机号超过长度限制，请核对后重新输入");
			break;
		case 2024:
			model.setMessage("头像上传失败，头像不能为空");
			break;
		case 3001:
			model.setMessage("验证码发送失败");
			break;
		case 3002:
			model.setMessage("验证码失效");
			break;
		case 3003:
			model.setMessage("验证码错误");
			break;
		case 3004:
			model.setMessage("验证码不可用");
			break;
		case 3005:
			model.setMessage("短信平台异常");
			break;
		case 3006:
			model.setMessage("验证码超过发送次数限制");
			break;
		case 4001:
			model.setMessage("参数不合法");
			break;
		case 4002:
			model.setMessage("无效的Token");
			break;
		case 4003:
			model.setMessage("无操作权限");
			break;
		case 4004:
			model.setMessage("请重新登录");
			break;
		case 4005:
			model.setMessage("token为空");
			break;
		case 4006:
			model.setMessage("key为空");
			break;
		case 4007:
			model.setMessage("无效的key");
			break;
		case 5001:
			model.setMessage("会议ID不存在");
			break;
		case 5002:
			model.setMessage("该用户不是会议创建者，没权限取消会议");
			break;
		case 5003:
			model.setMessage("该用户不是会议创建者，没权限开始会议");
			break;
		case 5004:
			model.setMessage("该用户不是会议创建者，没权限结束会议");
			break;
		case 5005:
			model.setMessage("取消会议失败，只能取消状态为“已创建”的会议");
			break;
		case 5006:
			model.setMessage("开始会议失败，只能开始状态为“已创建”的会议");
			break;
		case 5007:
			model.setMessage("结束会议失败，只能结束状态为“进行中”的会议");
			break;
		case 5008:
			model.setMessage("会议ID不能为空");
			break;
		case 6001:
			model.setMessage("添加联系人失败");
			break;
		case 7001:
			model.setMessage("翻译文件不存在，下载失败");
			break;
		case 7002:
			model.setMessage("内容文件不存在，下载失败");
			break;
		case 7003:
			model.setMessage("录音文件不存在，下载失败");
			break;
		case 8001:
			model.setMessage("文件分片上传成功");
			break;
		case 8002:
			model.setMessage("文件上传成功");
			break;
		case 404:
			model.setMessage(Errmsg);
			break;
		default:
			model.setMessage("未知错误");
			break;
		}
		return model;
	}
}
