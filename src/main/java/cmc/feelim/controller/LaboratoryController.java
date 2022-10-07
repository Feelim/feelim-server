package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.laboratory.dto.GetLaboratoriesRes;
import cmc.feelim.domain.laboratory.dto.GetLaboratoryRes;
import cmc.feelim.domain.laboratory.dto.PostLaboratoryReq;
import cmc.feelim.service.LaboratoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/laboratory")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @ApiOperation("현상소 추가")
    @PostMapping(value = "/new", consumes = {"multipart/form-data"})
    public BaseResponse<Long> create(@ModelAttribute PostLaboratoryReq postLaboratoryReq) throws BaseException {
        return new BaseResponse<Long>(laboratoryService.save(postLaboratoryReq));
    }

    @ApiOperation("현상소 모두 불러오기")
    @GetMapping("")
    public BaseResponse<List<GetLaboratoriesRes>> getAll() throws BaseException {
        return new BaseResponse<List<GetLaboratoriesRes>>(laboratoryService.getAll());
    }

    @ApiOperation("현상소 상세 보기")
    @GetMapping("/{laboratoryId}")
    public BaseResponse<GetLaboratoryRes> getOne(@PathVariable Long laboratoryId) throws BaseException {
        return new BaseResponse<GetLaboratoryRes>(laboratoryService.getOne(laboratoryId));
    }

}
