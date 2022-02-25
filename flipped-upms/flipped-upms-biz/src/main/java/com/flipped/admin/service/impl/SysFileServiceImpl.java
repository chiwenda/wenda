package com.flipped.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.admin.api.entity.SysFile;
import com.flipped.admin.mapper.SysFileMapper;
import com.flipped.admin.service.ISysFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件管理表 服务实现类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

}
