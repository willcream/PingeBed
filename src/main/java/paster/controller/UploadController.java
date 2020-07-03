package paster.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import paster.bean.PicUploadResult;
import paster.log.Logger;
import paster.prop.Prop;
import paster.utlis.FileUtils;
import paster.utlis.IPUtils;
import paster.utlis.LittleTools;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;


@Controller
public class UploadController {
    @Value("${bed.uploadPath}")
    private String UPLOAD_PATH;
    @Value("${bed.urlPrefix}")
    private String URL_PREFIX;

    public static boolean anonymous = true;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> upload(MultipartFile file, HttpServletRequest request) {
        anonymous = Boolean.parseBoolean(Prop.getProperty("anonymous"));
        if (anonymous) {
//            String address = IPUtils.getIpAddress(request);
            String address = "10.1.1.1";
            Logger.log("地址: " + address + "上传了文件 " + file.getOriginalFilename());
            return FileUtils.saveFile(file, address);
        }
        Map map = new TreeMap();
        map.put("state", "!admin");
        map.put("msg", "管理员禁止了匿名用户上传文件");

        return map;
    }



    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    @ResponseBody
    public PicUploadResult uploadPic(MultipartFile file, HttpServletRequest request){
        String oriName = file.getOriginalFilename();
        String name = UUID.randomUUID().toString()+LittleTools.getSuffix(oriName);
        String realPath = FileUtils.fixPath(UPLOAD_PATH);
        Path dir = Paths.get(realPath);
        if(!dir.toFile().exists()){
            dir.toFile().mkdir();
        }
        Path path = Paths.get(realPath+name);

        PicUploadResult result = new PicUploadResult();
        try {
//            path.toFile().createNewFile();
            Files.write(path,file.getBytes());
            result.setData(URL_PREFIX+name);
            result.setCode(200);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setData(e);
            result.setMessage("上传图片失败");
            result.setCode(500);
            return result;
        }
    }




    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> clone(@RequestBody Map map, HttpServletRequest request) {
        anonymous = Boolean.parseBoolean(Prop.getProperty("anonymous"));
        if (anonymous) {
            // 获取要克隆的网址
            String url = (String) map.get("url");
            return FileUtils.cloneFile(url, IPUtils.getIpAddress(request));
        }
        Map map2 = new TreeMap();
        map2.put("state", "!admin");
        map2.put("msg", "管理员禁止了匿名用户上传文件");

        return map2;

    }
}