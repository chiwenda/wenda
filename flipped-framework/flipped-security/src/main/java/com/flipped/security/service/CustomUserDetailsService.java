package com.flipped.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.flipped.admin.api.dto.UserInfo;
import com.flipped.admin.api.entity.SysUser;
import com.flipped.common.core.contants.SecurityConstants;
import com.flipped.common.core.utils.R;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * @author cwd
 * @date 2022/1/26 下午3:46
 */
public interface CustomUserDetailsService extends UserDetailsService , Ordered {
    /**
     * 是否支持此客户端校验
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    default UserDetails getUserDetails(R<UserInfo> result){
        if(result==null||result.getData()==null){
            throw new UsernameNotFoundException("用户不存在");
        }

        UserInfo info = result.getData();
        Set<String> dbAuthsSet=new HashSet<>();

        if(ArrayUtil.isNotEmpty(info.getRoles())){
            //获取角色
            Arrays.stream(info.getRoles()).forEach(role->dbAuthsSet.add(SecurityConstants.ROLE+role));
            //获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
        }

        Collection<? extends GrantedAuthority> authority = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user=info.getSysUser();

        return new User(user.getUserId(), user.getDeptId(), user.getUsername(),
                SecurityConstants.BCRYPT+user.getPassword(),user.getPhone(),true,true,
                true, StrUtil.equals(user.getLockFlag(),SecurityConstants.STATUS_NORMAL),authority);
    }
    /**
     * 通过用户实体查询
     * @param user user
     * @return
     */
    default UserDetails loadUserByUser(User user) {
        return this.loadUserByUsername(user.getUsername());
    }
}
