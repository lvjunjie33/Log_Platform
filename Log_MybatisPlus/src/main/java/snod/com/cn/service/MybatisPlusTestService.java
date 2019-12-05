package snod.com.cn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;

import snod.com.cn.dao.MybatisPlusDao;
import snod.com.cn.entity.SysUser;



@Service
public class MybatisPlusTestService {
	@Autowired
	private MybatisPlusDao mybatisPlusDao;

	@Transactional
	public void save() {
		SysUser su=new SysUser();
		su.setUsername("吕俊杰");
		su.setStatus(1);
		su.setCreateTime(new Date());
		su.setCreateUserId(1L);
		su.setEmail("981723773@qq.com");
		su.setEnabled(true);
		su.setMobile("13926570220");
		su.setPassword("123456");
		su.setShopId(1L);
		mybatisPlusDao.insert(su);
		
		

		
	}
	@Transactional
	public void manytomanySave() {
	
		
		
				
		
	}
	public List<SysUser> findByName(String name) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("username", name);
		return mybatisPlusDao.selectByMap(param);
		
	}
	
	public SysUser findById(Long id){
		return mybatisPlusDao.selectById(id);
	}
//	public List<Users> findByNameAndEmail(String name, String email) {
		// TODO Auto-generated method stub
//		return usersRepository.findByNameAndEmail(name, email);
//	}
//	public List<Users> findAllPage(PageRequest pageable) {
//		Page<Users> p=usersRepository.findAll(pageable);
//		
		// TODO Auto-generated method stub
//		return p.getContent();
//	}
//	public List<Users> findAllSort(Sort sort) {
//		Iterable<Users> iterable=usersRepository.findAll(sort);
//		iterable.forEach(users->{
//			System.out.println(users.getAge());
//		});
//		return Lists.newArrayList(iterable);
//	}
//	public List<Users> queryByNameUseHQL(String name) {
//		
//		return usersRepository.queryByNameUseHQL(name);
//	}
//	public void updateUsersNameById(String name,Integer id) {
//		 usersRepository.updateUsersNameById(name, id);
//		usersRepository.updateUsersNameById(name, id);
//	}
	
	@Transactional
	public void deleteById(Long id) {
//		Users users=usersRepository.findById(id);
//		nicknameRepository.deleteById(users.getId());
//		System.out.println(1/0);//测试事务
//		usersRepository.deleteById(id);
	}
	public List<SysUser> findAll(QueryWrapper<SysUser> queryWrapper) {
		// TODO Auto-generated method stub
		
		return mybatisPlusDao.selectList(queryWrapper);
	}
//	public List<Long> findGroup(TypedQuery<Long> query) {
//	
//		List<Long> users=query.getResultList();
//		return users;
//	}
//	public Long findGroupHql(String name) {
//		
//		return usersRepository.findGroupHql(name);
//	}
//	public List<Users> findAll() {
//		
//		return usersRepository.findAll();
//	}
//	public List<Users> queryInnerJoin(String name) {
//		 TODO Auto-generated method stub
//		return usersRepository.queryInnerJoin(name);
//	}
//	public List<Roles> RolesfindAll() {
		// TODO Auto-generated method stub
//		return rolesRepository.findAll();
//	}
//	public List<Nickname> nickNamefindAll() {
		// TODO Auto-generated method stub
//		return nicknameRepository.findAll();
//	}
//	public List<UsersDetail> usersDetailfindAll() {
		// TODO Auto-generated method stub
//		return usersDetailRepository.findAll();
//	}





	
}
