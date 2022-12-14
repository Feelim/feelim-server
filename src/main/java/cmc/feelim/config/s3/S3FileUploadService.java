package cmc.feelim.config.s3;

import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.image.ImageRepository;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.review.Review;
import cmc.feelim.domain.user.User;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {
    private final UploadService s3Service;
    private final AmazonS3Client amazonS3Client;
    private final S3Component s3Component;
    private final ImageRepository imageRepository;

    //Multipart를 통해 전송된 파일을 업로드 하는 메소드
    public List<Image> uploadImageFromPost(List<MultipartFile> files, Post post){
        //반환값
        List<Image> imageList = new ArrayList<>();

        for(MultipartFile multipartFile : files) {

            String fileName = post.getCategory() + "/" + createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            try (InputStream inputStream = multipartFile.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", multipartFile.getOriginalFilename()));
            }

            Image image = new Image(fileName, s3Service.getFileUrl(fileName), multipartFile.getSize(), multipartFile.getContentType());
            image.updatePost(post);
            imageRepository.save(image);
            imageList.add(image);
        }
        return imageList;
    }

    public List<Image> uploadImageFromLaboratory(List<MultipartFile> files, ProcessingLaboratory laboratory){
        //반환값
        List<Image> imageList = new ArrayList<>();

        for(MultipartFile multipartFile : files) {

            String fileName = laboratory.getName() + "/" + createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            try (InputStream inputStream = multipartFile.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", multipartFile.getOriginalFilename()));
            }

            Image image = new Image(fileName, s3Service.getFileUrl(fileName), multipartFile.getSize(), multipartFile.getContentType());
            image.updateLaboratory(laboratory);
            imageRepository.save(image);
            imageList.add(image);
        }
        return imageList;
    }

    /** 현상소 프사 업로드 **/
    public Image uploadLabProfile(MultipartFile file, ProcessingLaboratory laboratory) {
        String fileName = laboratory.getName() + "/" + createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", file.getOriginalFilename()));
        }

        Image image = new Image(fileName, s3Service.getFileUrl(fileName), file.getSize(), file.getContentType());
        image.updateLabProfile(laboratory);
        imageRepository.save(image);
        return image;
    }

    /** 현상소 배경사진 업로드 **/
    public Image uploadLabBackground(MultipartFile file, ProcessingLaboratory laboratory) {
        String fileName = laboratory.getName() + "/" + createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", file.getOriginalFilename()));
        }

        Image image = new Image(fileName, s3Service.getFileUrl(fileName), file.getSize(), file.getContentType());
        image.updateLabBackground(laboratory);
        imageRepository.save(image);
        return image;
    }

    //Multipart를 통해 전송된 파일을 업로드 하는 메소드
    public String uploadImage(MultipartFile file, String dir){
        String fileName = dir +"/"+ createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch(IOException e){
            throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", file.getOriginalFilename()));
        }

        return s3Service.getFileUrl(fileName);
    }

    public void deleteFile(String url, String dir) {
        if(url==null)
            return;
        String[] temp = url.split("/");
        amazonS3Client.deleteObject(s3Component.getBucket(),dir + "/" + temp[temp.length-1]);

    }


    //기존 확장자명 유지한 채로 유니크한 파일 이름을 생성하는 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch(StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.", fileName));
        }
    }

    public Image uploadImageFromUser(MultipartFile profile, User user) {
        String fileName = "profile/" + createFileName(profile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(profile.getSize());
        objectMetadata.setContentType(profile.getContentType());
        try (InputStream inputStream = profile.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", profile.getOriginalFilename()));
        }

        Image image = new Image(fileName, s3Service.getFileUrl(fileName), profile.getSize(), profile.getContentType());
        image.updateUser(user);
        imageRepository.save(image);
        return image;
    }

    public List<Image> uploadFromReview(List<MultipartFile> files, Review review) {
        List<Image> imageList = new ArrayList<>();

        for(MultipartFile multipartFile : files) {

            String fileName = "review/" + createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            try (InputStream inputStream = multipartFile.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 오류 발생 ($s)", multipartFile.getOriginalFilename()));
            }

            Image image = new Image(fileName, s3Service.getFileUrl(fileName), multipartFile.getSize(), multipartFile.getContentType());
            image.updateReview(review);
            imageRepository.save(image);
            imageList.add(image);
        }
        return imageList;
    }
}
