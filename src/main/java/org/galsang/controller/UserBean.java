package org.galsang.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 用户信息bean
 *
 * @author tengpeng.gao
 * @since 2018/9/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBean implements Serializable {

    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String phone;

}
