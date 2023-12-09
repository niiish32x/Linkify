package Linkify.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接分组 创建参数
 */
@Data
public class ShortLinkGroupSaveReqDTO {
    /**
     * 分组名
     */
    private String name;
}
