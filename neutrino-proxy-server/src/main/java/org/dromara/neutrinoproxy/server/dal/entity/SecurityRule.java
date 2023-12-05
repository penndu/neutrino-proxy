package org.dromara.neutrinoproxy.server.dal.entity;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.neutrinoproxy.server.constant.EnableStatusEnum;
import org.dromara.neutrinoproxy.server.constant.SecurityRulePassTypeEnum;
import org.noear.solon.core.util.IpUtil;

import java.util.Date;

@Data
@ToString
@Accessors(chain = true)
@TableName("security_rule")
public class SecurityRule {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属安全组
     */
    private Integer groupId;

    /**
     * 规则名
     */
    private String name;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则,ipv6只支持单个ip判断
     * 单个ip：192.168.1.1,0:0:0:0:0:0:10.0.0.1
     * 范围类型：192.168.1.0-192.168.1.255
     * 掩码类型：192.168.1.0/24
     * 泛型：0.0.0.0/ALL
     * 每个类型中间以英文逗号分隔
     */
    private String rule;

    /**
     * 放行类型，reject 或 allow
     * {@link SecurityRulePassTypeEnum}
     */
    private SecurityRulePassTypeEnum passType;

    /**
     * 优先级，数字越小，优先级越高
     */
    private Integer priority;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 启用状态
     * {@link EnableStatusEnum}
     */
    private Integer enable;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 判断当前规则是否允许指定ip同行
     * @param ip
     * @return
     */
    public boolean allow(String ip) {

        // 被判断的IP地址为空，不允许访问
        if (StrUtil.isEmpty(ip)) {
            return false;
        }

        // 没有规则，默认允许访问
        if (StrUtil.isEmpty(rule)) {
            return true;
        }

        // ipv6只适配单ip形式
        boolean isIpv6 = ip.contains(":");
        long ipLong = -1L;
        if (!isIpv6) {
            ipLong = Ipv4Util.ipv4ToLong(ip);
        }

        String[] rules = this.rule.split(",");
        for (String rule : rules) {

            // 单个ip,ipv6在此步已处理，后面不需要额外判断ipv6的情况
            if (rule.matches("(\\d+\\.){3}\\d+") || isIpv6) {
                return passType == SecurityRulePassTypeEnum.ALLOW && rule.equals(ip);
            }

            // 范围类型
            if (rule.matches("(\\d+\\.){3}\\d+-(\\d+\\.){3}\\d+")) {
                String[] ipRange = rule.split("-");
                if (ipRange[0].compareTo(ip) <= 0 && ip.compareTo(ipRange[1]) <= 0) {
                    return passType == SecurityRulePassTypeEnum.ALLOW;
                }
            }

            // 掩码类型
            if (rule.matches("(\\d+\\.){3}\\d+/\\d+")) {
                String[] netIp = rule.split("/");
                Long beginIp = Ipv4Util.getBeginIpLong(netIp[0], Integer.valueOf(netIp[1]));
                Long endIp = Ipv4Util.getEndIpLong(netIp[0], Integer.valueOf(netIp[1]));
                if (beginIp <= ipLong && ipLong <= endIp) {
                    return passType == SecurityRulePassTypeEnum.ALLOW;
                }
            }

            if (rule.equalsIgnoreCase("ALL") || rule.equals("0.0.0.0") || rule.equals("0..0.0.0/0")) {
                return passType == SecurityRulePassTypeEnum.ALLOW;
            }

        }

        // 都没有匹配到，默认放行
        return true;
    }

}
