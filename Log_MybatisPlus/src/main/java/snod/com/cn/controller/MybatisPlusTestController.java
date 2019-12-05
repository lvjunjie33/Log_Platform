package snod.com.cn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.elasticsearch.client.Node.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import snod.com.cn.entity.SysUser;
import snod.com.cn.service.MybatisPlusTestService;
import snod.com.cn.utils.EcpStackTrace;
import snod.com.cn.utils.ResultInfo;
import snod.com.cn.utils.ResultTools;

@RestController
@RequestMapping("/mybais_plus")
public class MybatisPlusTestController {
	
	private final static Logger logger = LoggerFactory.getLogger(MybatisPlusTestController.class);
	@Autowired
	private MybatisPlusTestService mybatisPlusTestService;
	/**
	 * jpa test save
	 */
	@RequestMapping("/save")
	public ResultInfo save(){
		try {
			mybatisPlusTestService.save();
//			jpaTestService.manytomanySave();
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	/**
	 * test jpa query
	 */
	@RequestMapping("/query")
	public ResultInfo query(String name,Long id){
		//Pageable:封装了分页的参数，当前页，煤业显示的条数。注意：它的当前页是从0开始
		//PageRequest(page,size):page表示当前页，size表示每页显示多少条
		//PageRequest pageable=new PageRequest(0, 1);已过时
//		PageRequest pageable=PageRequest.of(0, 1);
		
//		Sort.Order order=new Sort.Order(Sort.Direction.DESC,"id");
//		Sort sort=Sort.by(order);
//		Pageable pageable=PageRequest.of(0,2,sort);
//		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
//		queryWrapper.ge(column, val);
//		List<SysUser> list=mybatisPlusTestService.findAll(queryWrapper);
//		List<SysUser> list=mybatisPlusTestService.findByName(name);
		SysUser list=mybatisPlusTestService.findById(id);
		try {
			Map<String,Object> result=new HashMap<String,Object>();
//			List<Users> users=jpaTestService.findByName(name);
//			List<Users> users=jpaTestService.findByNameAndEmail(name,email);
//			List<Users> users=jpaTestService.findAllPage(pageable);
//			List<Users> users=jpaTestService.queryByNameUseHQL(name);
//			List<Users> users=jpaTestService.findAllSort(sort);
			result.put("users", list);
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, result);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	
	
	/**
	 * test jpa query multiple
	 * 多个查询条件
	 */
	@RequestMapping("/query/multiple")
	public ResultInfo queryMultiple(String name,Integer age) {
//		Specification<Users> spec=new Specification<Users>(){
//
//			/**
//			 * @param root		对查询对象属性的封装
//			 * @param criteriaQuery	封装了我们要执行的查询中的各个部分的信息，select from order
//			 * @param criteriaBuilder	查询条件的构造器
//			 * @return
//			 */
//			@Override
//			public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				// TODO Auto-generated method stub
//				CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
//				criteriaQuery.multiselect(criteriaBuilder.sum(root.get("age")));
////				Predicate predicate = 
////				criteriaBuilder.equal(root.get("age"), y)
////				List<Predicate> list=new ArrayList<Predicate>();
////				list.add(criteriaBuilder.equal(root.get("name"),name));
////				Predicate[] arr=new Predicate[list.size()];
////				criteriaBuilder.and(list.toArray(arr));
//				criteriaQuery.groupBy(root.get("name"));
//				return criteriaBuilder.conjunction();
//			}
//			
//		};
		/**group by 分组查询使用以下方式*/
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
//		Root<Users> root = criteriaQuery.from(Users.class);
//		criteriaQuery.multiselect(criteriaBuilder.sum(root.get("age")));
//		criteriaQuery.groupBy(root.get("name"));
//		criteriaBuilder.equal(root.get("name"),name);
//		TypedQuery<Long> query=entityManager.createQuery(criteriaQuery);
		try {
			Map<String,Object> result=new HashMap<String,Object>();
//			List<Users> users=jpaTestService.findAll(spec);
//			List<Long> ages=jpaTestService.findGroup(query);
//			Long l=jpaTestService.findGroupHql(name);
//			result.put("l", ages);
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, result);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	/**
	 * test jpa query multiple
	 * 关联查询
	 */
	@RequestMapping("/query/join")
	public ResultInfo queryJoin(String name,Integer age) {
		try {
			Map<String,Object> result=new HashMap<String,Object>();
//			jpaTestService.save();
//			List<Users> users=jpaTestService.queryInnerJoin(name);
//			List<Users> users=jpaTestService.findAll();
//			List<Roles> roles=jpaTestService.RolesfindAll();
//			List<Nickname> nickNames=jpaTestService.nickNamefindAll();
//			List<UsersDetail> usersDetails=jpaTestService.usersDetailfindAll();
//			result.put("users", users);
//			result.put("roles", roles);
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, result);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	
	/**
	 * test jpa update
	 */
	@RequestMapping("/update")
	public ResultInfo update(String name,Integer id){
		try {
//			jpaTestService.updateUsersNameById(name,id);
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	
	
	/**
	 * test jpa delete
	 */
	@RequestMapping("/delete")
	public ResultInfo delete(Long id){
		try {
			mybatisPlusTestService.deleteById(id);
			return ResultTools.result(ResultTools.ERROR_CODE_0,null, null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
}
