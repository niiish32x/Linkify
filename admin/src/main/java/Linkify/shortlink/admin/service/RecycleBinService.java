package Linkify.shortlink.admin.service;

import Linkify.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import Linkify.shortlink.admin.common.convention.result.Result;
import Linkify.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;

/**
 * URL 回收站接口层
 */
public interface RecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数包装
     */
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
