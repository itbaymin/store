import com.byc.permission.security.SecurityPermissionApplication;
import com.byc.persisent.permission.entity.SysMenu;
import com.byc.persisent.permission.entity.SysRole;
import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysMenuRepository;
import com.byc.persisent.permission.repository.SysRoleRepository;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityPermissionApplication.class)
public class Test {

    @Autowired
    SysUserRepository userRepository;
    @Autowired
    SysRoleRepository roleRepository;
    @Autowired
    SysMenuRepository menuRepository;

    @org.junit.Test
//    @Transactional
    public void test(){
        String username = "admin";
        String password = "admin";

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        password = new BCryptPasswordEncoder().encode(password);
        sysUser.setPassword(password);
        sysUser.setSalt("123");
        Optional<SysMenu> byId = menuRepository.findById(1L);
        SysMenu menu = byId.orElse(null);
        SysRole role = new SysRole();
        role.setRolename("管理员");
        role.setMenus(Arrays.asList(menu));
        roleRepository.save(role);
        sysUser.setRoles(Arrays.asList(role));
        userRepository.save(sysUser);


        SysUser baiyongcheng = userRepository.findByUsername("admin");
        if(baiyongcheng!=null)
            System.out.println(baiyongcheng.getCreateTime());

//        Optional<SysUser> byId = userRepository.findById(2L);
//        SysUser sysUser1 = byId.orElse(null);
//        System.out.println(sysUser1.getCreateTime());
    }
}
