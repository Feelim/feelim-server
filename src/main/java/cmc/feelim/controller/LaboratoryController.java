package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.laboratory.dto.GetLaboratoriesRes;
import cmc.feelim.domain.laboratory.dto.PostLaboratoryReq;
import cmc.feelim.service.LaboratoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/laboratory")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @ApiOperation("현상소 추가")
    @PostMapping(value = "/new", consumes = {"multipart/form-data"})
    public BaseResponse<Long> create(@ModelAttribute PostLaboratoryReq postLaboratoryReq) {
        return new BaseResponse<Long>(laboratoryService.save(postLaboratoryReq));
    }

    @ApiOperation("현상소 모두 불러오기")
    @GetMapping("")
    public BaseResponse<List<GetLaboratoriesRes>> getAll() {
        return new BaseResponse<List<GetLaboratoriesRes>>(laboratoryService.getAll());
    }

}
