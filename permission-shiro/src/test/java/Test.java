import com.byc.permission.shiro.PermissionApplication;
import com.byc.permission.shiro.service.SysUserService;
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
    SysUserService userService;

    @org.junit.Test
    @Transactional
    public void test(){
//        SysUser sysUser = new SysUser();
//        sysUser.setUsername("baiyongcheng");
//        sysUser.setPassword("byc123");
//        sysUser.setSalt("123");
//        SysMenu menu = new SysMenu();
//        menu.setMenuname("主页");
//        menuRepository.save(menu);
//        SysRole role = new SysRole();
//        role.setRolename("管理员");
//        role.setMenus(Arrays.asList(menu));
//        roleRepository.save(role);
//        sysUser.setRoles(Arrays.asList(role));
//        userRepository.save(sysUser);


        SimpleHash b = new SimpleHash("MD5", ByteSource.Util.bytes("12345"), "123");
        System.out.printf(b.toString());

        SysUser baiyongcheng = userService.findByUsername("baiyongcheng");
        if(baiyongcheng==null)
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
}
