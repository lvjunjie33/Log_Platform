package snod.com.cn.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import snod.com.cn.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long>,JpaSpecificationExecutor<Users> {
	//方法名称必须要遵循驼峰式命名规则，findBy（关键字）+属性名称（首字母大写）+查询条件（首字母大写）
    List<Users> findByName(String name);
    List<Users> findByNameAndAge(String name,Integer age);
    List<Users> findByNameLike(String name);
    List<Users> findByNameAndEmail(String name,String email);
    void deleteById(Long id);
    
    @Query("from Users where name = ?1")
    public List<Users> queryByNameUseHQL(String name);

//    @Query(value = "select * from t_users where name=?1",nativeQuery = true)
    @Query(value = "select * from t_users INNER JOIN t_users_nickname on t_users.nickname_id=t_users_nickname.id where t_users.name=?1",nativeQuery = true)
    public List<Users> queryInnerJoin(String name);
    
    @Modifying //需要执行一个更新操作
    @Query("update Users set name=?1 where id=?2")
    @Transactional
    public void updateUsersNameById(String name,Integer id);
    
    @Modifying//需要执行一个更新操作
    @Query("delete from Users  where id=?1")
    public void deleteUsersNameById(Long id);
    
    @Query(value = "select sum(u.age) from t_users u  where u.name=:name GROUP BY u.name ",nativeQuery = true)
	public Long findGroupHql(@Param("name") String name);
    
}
