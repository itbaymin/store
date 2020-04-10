import com.byc.permission.shiro.PermissionApplication;
import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.permission.shiro.mvc.vo.PageVo;
import com.byc.permission.shiro.mvc.vo.system.UserVO;
import com.byc.permission.shiro.service.system.UserService;
import com.byc.persisent.permission.entity.SysMenu;
import com.byc.persisent.permission.entity.SysRole;
import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysMenuRepository;
import com.byc.persisent.permission.repository.SysRoleRepository;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PermissionApplication.class)
public class Test {

    @Autowired
    SysUserRepository userRepository;
    @Autowired
    SysRoleRepository roleRepository;
    @Autowired
    SysMenuRepository menuRepository;
    @Autowired
    UserService userService;

    @org.junit.Test
    public void test(){
        String username = "system";
        String password = "system";

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        SimpleHash b = new SimpleHash("MD5", password, ByteSource.Util.bytes("123"),2);
        sysUser.setPassword(b.toString());
        sysUser.setSalt("123");
        Optional<SysMenu> byId = menuRepository.findById(1L);
        SysMenu menu = byId.orElse(null);
        SysRole role = new SysRole();
        role.setRolename("管理员");
        role.setMenus(Arrays.asList(menu));
        roleRepository.save(role);
        sysUser.setRoles(Arrays.asList(role));
        userRepository.save(sysUser);


        System.out.printf(b.toString());

        SysUser baiyongcheng = userService.findByUsername("system");
        if(baiyongcheng!=null)
            System.out.println(baiyongcheng.getCreateTime());

//        Optional<SysUser> byId = userRepository.findById(2L);
//        SysUser sysUser1 = byId.orElse(null);
//        System.out.println(sysUser1.getCreateTime());
    }


    @org.junit.Test
    @Transactional
    public void test1(){
        Optional<SysMenu> all = menuRepository.findById(1L);
        System.out.println(all.orElse(null).getChildren());
    }

    @org.junit.Test
    @Transactional
    public void test2(){
        Page<SysUser> users = userService.findUsers(new QueryParam());
        PageVo page = UserVO.page(users);
        System.out.println(page);
    }
}
