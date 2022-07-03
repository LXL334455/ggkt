package com.atguigu.ggkt.vod.service.impl;


import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.listener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-02
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Autowired
    private SubjectListener subjectListener;
    //查询下一层课程分类
    @Override
    public List<Subject> selectList(Long id) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Subject> subjects = baseMapper.selectList(wrapper);
        for (Subject subject : subjects) {
            Long subjectId = subject.getId();
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }

        return subjects;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //查询subject表中的数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //将subject 集合转换为SubjectEeVo集合
            List<SubjectEeVo> subjectEeVos = new ArrayList<>();
            for (Subject subject : subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                //调用BeanUtils工具类，将subject里的属性数据 复制给subjectEeVo（仅复制相同属性）
                BeanUtils.copyProperties(subject, subjectEeVo);
                subjectEeVos.add(subjectEeVo);
            }

            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVos);
        } catch (IOException e) {
            throw new GgktException(20001, "导出失败");
        }

    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }

    private boolean isChildren(Long id) {
        QueryWrapper<Subject> wr = new QueryWrapper<>();
        wr.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wr);
        return count > 0;

    }
}
