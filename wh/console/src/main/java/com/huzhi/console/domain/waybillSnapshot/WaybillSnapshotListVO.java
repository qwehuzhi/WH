package com.huzhi.console.domain.waybillSnapshot;

import com.huzhi.console.domain.waybill.WaybillListBaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain= true)
public class WaybillSnapshotListVO {
    private Integer total;//总数
    private Integer pageSize;//每页大小
    private List<WaybillSnapshotListBaseVO> list;
}
