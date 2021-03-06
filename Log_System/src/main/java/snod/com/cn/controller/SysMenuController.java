package snod.com.cn.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import snod.com.cn.common.UnauthorizedExceptionBase;
import snod.com.cn.constant.Constant;
import snod.com.cn.constant.MenuType;
import snod.com.cn.entity.SysMenu;
import snod.com.cn.service.SysMenuService;
import snod.com.cn.utils.SecurityUtils;

/**
 * 系统菜单
 * @author lvjj
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController{
	
	@Autowired
	private SysMenuService sysMenuService;


	@GetMapping("/nav")
	@ApiOperation(value="获取用户所拥有的菜单和权限", notes="通过登陆用户的userId获取用户所拥有的菜单和权限")
	public ResponseEntity<Map<Object, Object>> nav(){
		List<SysMenu> menuList = sysMenuService.listMenuByUserId(SecurityUtils.getSecurityUser().getUserId());
		Collection<? extends GrantedAuthority> authorities = SecurityUtils.getAuthentication().getAuthorities();
		return ResponseEntity.ok(MapUtil.builder().put("menuList", menuList).put("authorities", authorities).build());
	}

	/**
	 * 获取菜单页面的表
	 * @return
	 */
	@GetMapping("/table")
	public ResponseEntity<List<SysMenu>> table(){
		List<SysMenu> sysMenuList = sysMenuService.listMenuAndBtn();
		return ResponseEntity.ok(sysMenuList);
	}

	/**
	 * 所有菜单列表(用于新建、修改角色时 获取菜单的信息)
	 */
	@GetMapping("/list")
	@ApiOperation(value="获取用户所拥有的菜单(不包括按钮)", notes="通过登陆用户的userId获取用户所拥有的菜单和权限")
	public ResponseEntity<List<SysMenu>> list(){
		List<SysMenu> sysMenuList= sysMenuService.listSimpleMenuNoButton();
		return ResponseEntity.ok(sysMenuList);
	}

	/**
	 * 选择菜单
	 */
	@GetMapping("/listRootMenu")
	public ResponseEntity<List<SysMenu>> listRootMenu(){
		//查询列表数据
		List<SysMenu> menuList = sysMenuService.listRootMenu();

		return ResponseEntity.ok(menuList);
	}

	/**
	 * 选择子菜单
	 */
	@GetMapping("/listChildrenMenu")
	public ResponseEntity<List<SysMenu>> listChildrenMenu(Long parentId){
		//查询列表数据
		List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(parentId);

		return ResponseEntity.ok(menuList);
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/info/{menuId}")
	@PreAuthorize("hasAuthority('sys:menu:info')")
	public ResponseEntity<SysMenu> info(@PathVariable("menuId") Long menuId){
		SysMenu menu = sysMenuService.queryMenuInfo(menuId);
		return ResponseEntity.ok(menu);
	}

	/**
	 * 保存
	 */
//	@SysLog("保存菜单")
	@PostMapping
	@PreAuthorize("hasAuthority('sys:menu:save')")
	public ResponseEntity<Void> save(@Valid @RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
		sysMenuService.insertMenu(menu);
		return ResponseEntity.ok().build();
	}

	/**
	 * 修改
	 */
//	@SysLog("修改菜单")
	@PutMapping
	@PreAuthorize("hasAuthority('sys:menu:update')")
	public ResponseEntity<String> update(@Valid @RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);

		if(menu.getType() == MenuType.MENU.getValue()){
			if(StrUtil.isBlank(menu.getUrl())){
				return ResponseEntity.badRequest().body("菜单URL不能为空");
			}
		}
		sysMenuService.updateById(menu);

		return ResponseEntity.ok().build();
	}

	/**
	 * 删除
	 */
//	@SysLog("删除菜单")
	@DeleteMapping("/{menuId}")
	@PreAuthorize("hasAuthority('sys:menu:delete')")
	public ResponseEntity<String> delete(@PathVariable Long menuId){
		if(menuId <= Constant.SYS_MENU_MAX_ID){
			return ResponseEntity.badRequest().body("系统菜单，不能删除");
		}
		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(menuId);
		if(menuList.size() > 0){
			return ResponseEntity.badRequest().body("请先删除子菜单或按钮");
		}

		sysMenuService.deleteMenuAndRoleMenu(menuId);

		return ResponseEntity.ok().build();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenu menu){

		if(menu.getType() == MenuType.MENU.getValue()){
			if(StrUtil.isBlank(menu.getUrl())){
				throw new UnauthorizedExceptionBase("菜单URL不能为空");
			}
		}
		if(Objects.equals(menu.getMenuId(), menu.getParentId())){
			throw new UnauthorizedExceptionBase("自己不能是自己的上级");
		}

		//上级菜单类型
		int parentType = MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenu parentMenu = sysMenuService.queryMenuInfoByParentId(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == MenuType.CATALOG.getValue() ||
				menu.getType() == MenuType.MENU.getValue()){
			if(parentType != MenuType.CATALOG.getValue()){
				throw new UnauthorizedExceptionBase("上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮
		if(menu.getType() == MenuType.BUTTON.getValue()){
			if(parentType != MenuType.MENU.getValue()){
				throw new UnauthorizedExceptionBase("上级菜单只能为菜单类型");
			}
		}
	}
}
