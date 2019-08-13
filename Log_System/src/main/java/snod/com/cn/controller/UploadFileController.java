package snod.com.cn.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import snod.com.cn.entity.vo.FileVo;
import snod.com.cn.service.Log_tx_Service;

/**
 * Create by tianci
 * 2019/1/10 15:41
 */
@RestController
@RequestMapping("/tx_log")
public class UploadFileController {

	@Autowired
	private Log_tx_Service logService;


    @GetMapping("/open")
    public ModelAndView open() {

//        return new ModelAndView("upload");
    	return new ModelAndView("uploadbigFile");
    }

    @PostMapping("/isUpload")
    public Map<String, Object> isUpload(@Valid FileVo form) {

        return logService.findByFiletxInfo(form.getSn());

    }

//    @PostMapping("/upload")
//    public Map<String, Object> upload(@Valid FileForm form,
//                                      @RequestParam(value = "data", required = false)MultipartFile multipartFile) {
//        Map<String, Object> map = null;
//
//        try {
//            map = uploadFileService.realUpload(form, multipartFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
}
