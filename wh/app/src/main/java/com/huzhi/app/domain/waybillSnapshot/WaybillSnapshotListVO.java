package com.huzhi.app.domain.waybillSnapshot;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain= true)
public class WaybillSnapshotListVO {
    private Boolean isEnd;
    private String wp;
    private List<WaybillSnapshotListBaseVO> list;
}
