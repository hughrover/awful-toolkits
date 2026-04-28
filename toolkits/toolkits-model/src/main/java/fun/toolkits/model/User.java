package fun.toolkits.model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @JSONField(name = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @JSONField(name = "用户名")
    private String username;

    /**
     * 密码
     */
    @JSONField(name = "密码")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("<PASSWORD>");
        user.setEmail("<EMAIL>");

        System.out.println(JSON.toJSONString(user));
    }
}
