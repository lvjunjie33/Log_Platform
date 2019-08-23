

package snod.com.cn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.hutool.core.util.StrUtil;
import snod.com.cn.common.PageParam;
import snod.com.cn.entity.SysRole;
import snod.com.cn.service.SysMenuService;
import snod.com.cn.service.SysRoleService;
import snod.com.cn.utils.SecurityUtils;



/**
 * 角色管理
 * @author lgh
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController{
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysMenuService sysMenuService;
	
	/**
	 * 角色列表
	 */
	@GetMapping("/page")
	@PreAuthorize("hasAuthority('sys:role:page')")
	public ResponseEntity<IPage<SysRole>> page(String roleName,PageParam<SysRole> page){
		IPage<SysRole> sysRoles = sysRoleService.queryRoleList(page,roleName);
		return ResponseEntity.ok(sysRoles);
	}
	
	/**
	 * 查询所有角色列表
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('sys:role:list')")
	public ResponseEntity<List<SysRole>> list(){
		List<SysRole> list = sysRoleService.queryRoleListAll();
		return ResponseEntity.ok(list);
	}
	
	/**
	 * 角色信息
	 */
	@GetMapping("/info/{roleId}")
	@PreAuthorize("hasAuthority('sys:role:info')")
	public ResponseEntity<SysRole> info(@PathVariable("roleId") Long roleId){
		SysRole role = sysRoleService.getroleInfoById(roleId);
		
		//查询角色对应的菜单
		List<Long> menuList = sysMenuService.listMenuIdByRoleId(roleId);
		role.setMenuIdList(menuList);
		
		return ResponseEntity.ok(role);
	}
	
	/**
	 * 保存角色
	 */
//	@SysLog("保存角色")
	@PostMapping
	@PreAuthorize("hasAuthority('sys:role:save')")
	public ResponseEntity<Void> save(@RequestBody SysRole role){
		role.setCreateUserId(SecurityUtils.getSecurityUser().getUserId());
		sysRoleService.saveRoleAndRoleMenu(role);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改角色
	 */
//	@SysLog("修改角色")
	@PutMapping
	@PreAuthorize("hasAuthority('sys:role:update')")
	public ResponseEntity<Void> update(@RequestBody SysRole role){
		sysRoleService.updateRoleAndRoleMenu(role);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除角色
	 */
//	@SysLog("删除角色")
	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:role:delete')")
	public ResponseEntity<Void> delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		return ResponseEntity.ok().build();
	}
}
