package com.atguigu.ggkt.vod.service;


import com.atguigu.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-02
 */
public interface SubjectService extends IService<Subject> {
    //查询下一层课程分类
    List<Subject> selectList(Long id);

    /**
     * 导出
     * @param response
     */
    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);

}
