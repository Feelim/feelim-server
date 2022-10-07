package cmc.feelim.service;

import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.laboratory.LaboratoryRepository;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.laboratory.dto.GetLaboratoriesRes;
import cmc.feelim.domain.laboratory.dto.PostLaboratoryReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LaboratoryService {
    private final LaboratoryRepository laboratoryRepository;
    private final S3FileUploadService fileUploadService;

    /** 현상소 저장 **/
    @Transactional
    public Long save(PostLaboratoryReq postLaboratoryReq) {
        ProcessingLaboratory laboratory = new ProcessingLaboratory(postLaboratoryReq);

        System.out.println("name: " + postLaboratoryReq.getName() + "!!!!!!!!!!!!");
        System.out.println("point: " + postLaboratoryReq.getPoint() + "!!!!!!!!!!!!!!!!!!");

        if(postLaboratoryReq.getBills() != null) {
            laboratory.updateImage(fileUploadService.uploadImageFromLaboratory(postLaboratoryReq.getBills(), laboratory));
        }

        return laboratoryRepository.save(laboratory).getId();
    }

    /** 현상소 모두 보기 **/
    public List<GetLaboratoriesRes> getAll() {
        List<ProcessingLaboratory> processingLaboratories = laboratoryRepository.findAll();
        List<GetLaboratoriesRes> getLaboratoriesRes = processingLaboratories.stream()
                .map(GetLaboratoriesRes::new)
                .collect(Collectors.toList());

        return getLaboratoriesRes;
    }
}
