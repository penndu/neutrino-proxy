package fun.asgc.neutrino.proxy.server.controller.req;

import lombok.Data;

/**
 * 修改端口分组请求
 */
@Data
public class PortGroupUpdateEnableStatusReq {

    private Integer id;
    private Integer enable;
}
