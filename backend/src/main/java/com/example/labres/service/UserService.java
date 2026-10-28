package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.common.Constants;
import com.example.labres.domain.User;
import com.example.labres.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<Map<String, Object>> list() {
        return userMapper.findAll().stream().map(AuthService::userToMap).collect(Collectors.toList());
    }

    public void updateStatus(Long id, String status) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!Constants.USER_ACTIVE.equals(status) && !Constants.USER_DISABLED.equals(status)) {
            throw new BusinessException("非法的用户状态");
        }
        if (Constants.ROLE_SYS_ADMIN.equals(user.getRole()) && Constants.USER_DISABLED.equals(status)) {
            throw new BusinessException("不能停用系统管理员账号");
        }
        userMapper.updateStatus(id, status);
    }
}
