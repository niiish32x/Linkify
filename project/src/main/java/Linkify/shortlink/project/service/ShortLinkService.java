package Linkify.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import Linkify.shortlink.project.dao.entity.ShortLinkDO;
import Linkify.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import Linkify.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import Linkify.shortlink.project.dto.req.ShortLinkPageReqDTO;
import Linkify.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import Linkify.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import Linkify.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import Linkify.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     * @param requestParam 短链接请求参数
     * @return  返回短链接实体信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    @Transactional(rollbackFor = Exception.class)
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam);

    /**
     * 短链接跳转
     *
     * @param shortUri 短链接后缀
     * @param request  HTTP 请求
     * @param response HTTP 响应
     */
    void restoreUrl(String shortUri, ServletRequest request, ServletResponse response);


    /**
     * 短链接统计
     *
     * @param fullShortUrl         完整短链接
     * @param gid                  分组标识
     * @param shortLinkStatsRecord 短链接统计实体参数
     */
    void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO shortLinkStatsRecord);
}
