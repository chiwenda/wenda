package com.flipped.admin.service.impl;

import com.flipped.admin.api.entity.SysLog;
import com.flipped.admin.mapper.SysLogMapper;
import com.flipped.admin.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

}
