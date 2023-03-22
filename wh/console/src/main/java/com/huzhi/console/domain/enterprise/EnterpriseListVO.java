package com.huzhi.console.domain.enterprise;

import com.huzhi.console.domain.client.ClientListBaseVO;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain= true)
public class EnterpriseListVO {
    private Integer total;//总数
    private Integer pageSize;//每页大小
    private List<EnterpriseListBaseVO> list;
}
