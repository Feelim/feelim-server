package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.report.dto.PostReportReq;
import cmc.feelim.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{postId}/report")
public class ReportController {
    private final ReportService reportService;

    @ApiOperation("게시물 신고")
    @PostMapping("")
    //게시물 신고
    private BaseResponse<Long> createReport(@PathVariable Long postId, @RequestBody PostReportReq postReportReq) throws BaseException {
        return new BaseResponse<Long>(reportService.save(postId, postReportReq));
    }
}
