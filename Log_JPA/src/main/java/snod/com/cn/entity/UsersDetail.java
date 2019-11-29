package snod.com.cn.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="t_users_detail")
@Setter
@Getter
public class UsersDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5094891630124490365L;
	
	
	@Id	//主键id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//主键生成策略
	@Column(name="id")//数据库字段名
	private Long id;
	
	@Column(name="account")
	private String account;
	
	@Column(name="password")
	private String password;
	@JsonIgnoreProperties({"usersDetails"})
	@ManyToOne(cascade={CascadeType.ALL},optional=false)
	@JoinColumn(name="users_id")//设置在article表中的关联字段(外键)
	private Users users;
}
