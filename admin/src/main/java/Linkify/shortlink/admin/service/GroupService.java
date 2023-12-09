package Linkify.shortlink.admin.service;

import Linkify.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import Linkify.shortlink.admin.dao.entity.GroupDO;
import Linkify.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import Linkify.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * 新增短链接
     *
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);

    void saveGroup(String username, String groupName);

    /**
     * 查询用户 短链接分组集合
     * @return 短链接分组集合
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     * @param requestParm 修改短链接参数
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParm);

    /**
     * 删除短链接分组
     * @param gid 短链接分组标识
     */
    void deleteGroup(String gid);

    /**
     * 短链接分组排序
     * @param requestParam 短链接分组排序参数
     */
    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
