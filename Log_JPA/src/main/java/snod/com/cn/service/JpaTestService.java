package snod.com.cn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import snod.com.cn.dao.NicknameRepository;
import snod.com.cn.dao.RolesRepository;
import snod.com.cn.dao.UsersDetailRepository;
import snod.com.cn.dao.UsersRepository;
import snod.com.cn.entity.Nickname;
import snod.com.cn.entity.Roles;
import snod.com.cn.entity.Users;
import snod.com.cn.entity.UsersDetail;

@Service
public class JpaTestService {

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private NicknameRepository nicknameRepository;
	@Autowired
	private UsersDetailRepository usersDetailRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Transactional
	public void save() {
		
		Users user=new Users();
		user.setAddress("湖南省永州市零陵区南津北路4号");
		user.setAge(31L);
		user.setName("吕俊杰");
		user.setEmail("981723773@qq.com");
		usersRepository.save(user);
		Nickname nickname=new Nickname();
		nickname.setName("lvjunjie");
		nickname.setEnName("junjie Lv");
		nickname.setUsers(user);
		nicknameRepository.save(nickname);
		//一对多，多对一保存
		List<UsersDetail> list=new ArrayList<UsersDetail>();
		UsersDetail UsersDetail=new UsersDetail();
		UsersDetail.setAccount("981723773");
		UsersDetail.setPassword("123456");
		UsersDetail.setUsers(user);
		UsersDetail UsersDetail2=new UsersDetail();
		UsersDetail2.setAccount("981723773");
		UsersDetail2.setPassword("123456");
		UsersDetail2.setUsers(user);
		list.add(UsersDetail);
		list.add(UsersDetail2);
		usersDetailRepository.saveAll(list);
		

		
	}
	@Transactional
	public void manytomanySave() {
	
		
		//多对多保存
//		List<Roles> RolesList=new ArrayList<Roles>();
//		List<Users> userList=new ArrayList<Users>();
//		Roles roles=new Roles();
//		roles.setRoleName("test");
//		roles.setRemark("test");
//		roles.setCreateUserId("8");
//		roles.setCreateTime(new Date());
////		rolesRepository.save(roles);
//		Roles roles2=new Roles();
//		roles2.setRoleName("system");
//		roles2.setRemark("system test");
//		roles2.setCreateUserId("1");
//		roles2.setCreateTime(new Date());
//		RolesList.add(roles);
//		RolesList.add(roles2);
//
//		Users user=new Users();
//		user.setAddress("湖南省永州市零陵区南津北路4号");
//		user.setAge(31L);
//		user.setName("吕俊杰");
//		user.setEmail("981723773@qq.com");
//		user.setRoles(RolesList);
//		userList.add(user);
//		roles.setUsers(userList);
//		roles2.setUsers(userList);
//		
//		
//		usersRepository.save(user);
		
		List<Users> userList=new ArrayList<Users>();
		List<Roles> rolesList=new ArrayList<Roles>();
		Users users=new Users();
		users.setAddress("湖南省永州市零陵区南津北路4号");
		users.setAge(31L);
		users.setName("吕俊杰2");
		users.setEmail("981723773@qq.com");
		Users users2=new Users();
		users2.setAddress("湖南省永州市零陵区南津北路4号");
		users2.setAge(31L);
		users2.setName("吕俊杰3");
		users2.setEmail("981723773@qq.com");
		userList.add(users);
		userList.add(users2);
		
		Roles roles=new Roles();
		roles.setRoleName("test");
		roles.setRemark("test");
		roles.setCreateUserId("8");
		roles.setCreateTime(new Date());
		roles.setUsers(userList);
		rolesList.add(roles);
		userList.forEach(u->{
			u.setRoles(rolesList);
		});
		rolesRepository.save(roles);		
				
		
	}
	public List<Users> findByName(String name) {
		return usersRepository.findByName(name);
		
	}
	public List<Users> findByNameAndEmail(String name, String email) {
		// TODO Auto-generated method stub
		return usersRepository.findByNameAndEmail(name, email);
	}
	public List<Users> findAllPage(PageRequest pageable) {
		Page<Users> p=usersRepository.findAll(pageable);
		
		// TODO Auto-generated method stub
		return p.getContent();
	}
	public List<Users> findAllSort(Sort sort) {
		Iterable<Users> iterable=usersRepository.findAll(sort);
//		iterable.forEach(users->{
//			System.out.println(users.getAge());
//		});
		return Lists.newArrayList(iterable);
	}
	public List<Users> queryByNameUseHQL(String name) {
		
		return usersRepository.queryByNameUseHQL(name);
	}
	public void updateUsersNameById(String name,Integer id) {
//		 usersRepository.updateUsersNameById(name, id);
		usersRepository.updateUsersNameById(name, id);
	}
	
	@Transactional
	public void deleteById(Long id) {
//		Users users=usersRepository.findById(id);
//		nicknameRepository.deleteById(users.getId());
//		System.out.println(1/0);//测试事务
//		usersRepository.deleteById(id);
	}
	public List<Users> findAll(Specification<Users> spec) {
		// TODO Auto-generated method stub
		
		return usersRepository.findAll(spec);
	}
	public List<Long> findGroup(TypedQuery<Long> query) {
	
		List<Long> users=query.getResultList();
		return users;
	}
	public Long findGroupHql(String name) {
		
		return usersRepository.findGroupHql(name);
	}
	public List<Users> findAll() {
		
		return usersRepository.findAll();
	}
	public List<Users> queryInnerJoin(String name) {
		// TODO Auto-generated method stub
		return usersRepository.queryInnerJoin(name);
	}
	public List<Roles> RolesfindAll() {
		// TODO Auto-generated method stub
		return rolesRepository.findAll();
	}
	public List<Nickname> nickNamefindAll() {
		// TODO Auto-generated method stub
		return nicknameRepository.findAll();
	}
	public List<UsersDetail> usersDetailfindAll() {
		// TODO Auto-generated method stub
		return usersDetailRepository.findAll();
	}




	
}
