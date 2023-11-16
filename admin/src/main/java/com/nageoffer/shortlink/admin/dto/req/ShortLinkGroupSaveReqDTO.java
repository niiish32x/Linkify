package com.nageoffer.shortlink.admin.dto.req;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.service.GroupService;
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
