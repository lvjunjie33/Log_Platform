package snod.com.cn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="t_users_nickname")
@Setter
@Getter
public class Nickname implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4682463676457952843L;

	@Id	//主键id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//主键生成策略
	@Column(name="id")//数据库字段名
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="en_name")
	private String enName;
	
//	@OneToOne(cascade=CascadeType.ALL)//People是关系的维护端，当删除 people，会级联删除 address
//	@JoinColumn(name = "users_id", referencedColumnName = "id")//people中的address_id字段参考address表中
//	@OneToOne(mappedBy = "nickname",cascade=CascadeType.ALL)
//	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
//    @JoinColumn(name = "users_id")
	@JsonIgnoreProperties({"nickname"})
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
	private Users users; 
}

