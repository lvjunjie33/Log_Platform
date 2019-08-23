package snod.com.cn.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("tz_sys_user")
public class SysUser{
	
	@TableId
	private long userId;

	private String userName;

	private String password;

	private String email;
	
	private String mobile;
	
	private int status;
	
	private int createUserId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	private long shopId;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = true;
	@TableField(exist=false)
	private List<SysRole> roles;
	
	

}
