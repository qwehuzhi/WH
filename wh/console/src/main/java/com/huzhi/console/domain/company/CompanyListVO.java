package com.huzhi.console.domain.company;

import com.huzhi.console.domain.client.ClientListBaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain= true)
public class CompanyListVO {
    private Integer total;//总数
    private Integer pageSize;//每页大小
    private List<CompanyListBaseVO> list;
}
