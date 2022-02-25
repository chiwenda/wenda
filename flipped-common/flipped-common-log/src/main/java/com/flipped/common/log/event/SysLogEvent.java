package com.flipped.common.log.event;

import com.flipped.admin.api.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 *
 * @author chiwenda
 * @date 2022/1/29 下午3:05
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(SysLog source) {
        super(source);
    }
}
