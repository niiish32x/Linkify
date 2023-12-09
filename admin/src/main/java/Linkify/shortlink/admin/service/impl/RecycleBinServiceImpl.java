package Linkify.shortlink.admin.service.impl;
import Linkify.shortlink.admin.common.convention.exception.ServiceException;
import Linkify.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import Linkify.shortlink.admin.common.biz.user.UserContext;
import Linkify.shortlink.admin.common.convention.result.Result;
import Linkify.shortlink.admin.dao.entity.GroupDO;
import Linkify.shortlink.admin.dao.mapper.GroupMapper;
import Linkify.shortlink.admin.remote.ShortLinkRemoteService;
import Linkify.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import Linkify.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * URL 回收站接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final GroupMapper groupMapper;

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        // * 1. 匹配权限 该条短链接的拥有者 与 要删除短链接的用户相同则可以删除
        // * 2. 如果 DelFlag = 0 即没有删除 那么就还可以查到
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }
}
