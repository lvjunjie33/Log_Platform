package snod.com.cn.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表
 * */
@Entity
@Table(name="t_users")
@Setter
@Getter
public class Users implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5776880400969454796L;

	@Id	//主键id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//主键生成策略
	@Column(name="id")//数据库字段名
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="age")
	private Long age;
	
	@Column(name="address")
	private String address;
	
	@Column(name="email")
	private String email;
	@JsonIgnoreProperties({"users"})
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "users")
	private Nickname nickname;//昵称表
	
	@JsonIgnoreProperties({"users"})
    @OneToMany(mappedBy = "users",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<UsersDetail> usersDetails;//文章列表
	   
	@JsonIgnoreProperties({"users"})
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    },mappedBy = "users")
	private List<Roles> roles;
}