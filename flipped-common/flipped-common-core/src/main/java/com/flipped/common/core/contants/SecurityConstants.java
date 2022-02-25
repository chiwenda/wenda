package com.flipped.common.core.contants;

/**
 * 安全常量
 *
 * @author cwd
 * @date 2022/1/14 上午11:52
 */
public class SecurityConstants {
    /**
     * 角色前缀
     */
    public final static String ROLE = "ROLE_";
    /**
     * 验证码有效期,默认 60秒
     */
    public final static long CODE_TIME = 60;
    /**
     * 默认登录URL
     */
    public final static String OAUTH_TOKEN_URL = "/oauth/token";

    /**
     * grant_type
     */
    public final static String REFRESH_TOKEN = "refresh_token";

    /**
     * 标志
     */
    public final static String FROM = "from";

    /**
     * 协议字段
     */
    public final static String DETAILS_LICENSE = "license";

    /**
     * 项目的license
     */
    public final static String PROJECT_LICENSE = "made by flipped";

    /**
     * 客户端ID
     */
    public final static String CLIENT_ID = "clientId";

    /**
     * 客户端模式
     */
    public final static String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 用户信息
     */
    public final static String DETAILS_USER = "user_info";

    /**
     * {bcrypt} 加密的特征码
     */
    public final static String BCRYPT = "{bcrypt}";

    /***
     * 资源服务器默认bean名称
     */
    public final static String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 正常
     */
    public final static String STATUS_NORMAL = "0";
    /**
     * 内部
     */
    public final static String FROM_IN = "Y";
    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    public final static String CLIENT_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";
    /**
     * JdbcClientDetailsService 查询语句
     */
    public final static String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from sys_oauth_client_details";
    /**
     * 按条件client_id 查询
     */
    public final static String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";
    /**
     * 默认的查询语句
     */
    public final static String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";
}
