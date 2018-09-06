package org.galsang.service;

import lombok.extern.slf4j.Slf4j;
import org.galsang.controller.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 用户业务层
 *
 * @author tengpeng.gao
 * @since 2018/9/5
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 使用默认的事务进行管理
     *
     * @param userBean
     * @return
     */
    @Transactional
    public boolean addUserByDefaultTransaction(UserBean userBean) {

        String sql = " INSERT INTO user_info (username, password) VALUES ('" + userBean.getUsername() + "' , '" +
                userBean.getPassword() + "' ) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long userId = keyHolder.getKey().longValue();

        log.info("userId --->{}", userId);

        String extSql = "INSERT INTO user_ext_info(userId, phone) values (?, ?)";
        int resultInt = jdbcTemplate.update(extSql, new Object[]{userId, userBean.getPhone()});

        log.info("resultInt --->{}", resultInt);

        if (resultInt > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 使用默认的事务进行管理
     *
     * @param userBean
     * @return
     */
    @Transactional
    public boolean addUser(UserBean userBean) {
        Long userId = this.addUserInfo(userBean.getUsername(), userBean.getPassword());
        return this.addUserExtInfo(userId, userBean.getPhone());
    }

    /**
     * 增加用户基础信息
     *
     * @param username 用户名称
     * @param password 用户密码
     * @return 用户id
     */
    @Transactional
    public Long addUserInfo(@NotNull String username, @NotNull String password) {

        String sql = " INSERT INTO user_info (username, password) VALUES ('" + username + "' , '" + password + "' ) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        };
        int resultInt = jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long userId = null;
        if (resultInt > 0) {
            userId = keyHolder.getKey().longValue();
        }
        return userId;
    }

    /**
     * 增加扩展信息
     *
     * @param userId 用户id
     * @param phone  点好
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addUserExtInfo(@NotNull Long userId, @NotNull String phone) {
        String extSql = "INSERT INTO user_ext_info(userId, phone) values (?, ?)";
        int resultInt = jdbcTemplate.update(extSql, new Object[]{userId, phone});

        log.info("resultInt --->{}", resultInt);

        if (resultInt > 0) {
            return true;
        } else {
            return false;
        }
    }


}
