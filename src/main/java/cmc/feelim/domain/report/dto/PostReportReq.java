package cmc.feelim.domain.report.dto;

import cmc.feelim.domain.report.Reason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReportReq {
    private Reason reason;
    private String etc;
}
