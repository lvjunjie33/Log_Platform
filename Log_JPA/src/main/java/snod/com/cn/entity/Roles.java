package snod.com.cn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色表
 * */
@Entity
@Table(name="t_roles")
@Setter
@Getter
public class Roles implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2806376999413647220L;

	@Id	//主键id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//主键生成策略
	@Column(name="id")//数据库字段名
	private Long id;
	
	@Column(name="role_name")
	private String roleName;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="create_user_id")
	private String createUserId;
	
	@Column(name="create_time")
	private Date createTime;
	
	/**
	 * 注意：
	 * 1.mappedBy与@JoinTable互斥，使用了mappedBy就不能使用@JoinTable
	 * 2.mappedBy由外键的一方维护对应关系
	 * 例：用户表外键角色表，就由角色表使用mappedBy维护对应关系，反之同理
	 * 
	 * */
	@JsonIgnoreProperties({"roles"})
	@ManyToMany(fetch = FetchType.LAZY,
	    cascade = {
	        CascadeType.PERSIST,
	        CascadeType.MERGE
	    })
	@JoinTable(name="t_roleUsers",joinColumns = {@JoinColumn(name="roles_id")},
	inverseJoinColumns = {@JoinColumn(name = "users_id")})
    private List<Users> users;
}
