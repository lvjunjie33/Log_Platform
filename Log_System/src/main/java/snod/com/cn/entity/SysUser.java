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
	private Long userId;

	private String username;

	private String password;

	private String email;
	
	private String mobile;
	
	private int status;
	
	private Long createUserId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	private Long shopId;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = true;
	@TableField(exist=false)
	private List<Long> roles;
	@TableField(exist=false)
	private List<Long> roleIdList;

}
