package Linkify.shortlink.project.service.impl;

import Linkify.shortlink.project.dao.entity.LinkStatsTodayDO;
import Linkify.shortlink.project.dao.mapper.LinkStatsTodayMapper;
import Linkify.shortlink.project.service.LinkStatsTodayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 短链接今日统计接口实现层
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {

}
