package edu.guet.studentworkmanagementsystem.controller;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.foreignLanguage.ForeignLanguageStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.ForeignLanguage;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.Language;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageItem;
import edu.guet.studentworkmanagementsystem.entity.vo.foreignLanguage.ForeignLanguageStatItem;
import edu.guet.studentworkmanagementsystem.service.foreignLanguage.ForeignLanguageService;
import edu.guet.studentworkmanagementsystem.service.foreignLanguage.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/language")
public class LanguageController {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ForeignLanguageService foreignLanguageService;

    @PreAuthorize("hasAuthority('foreign:select')")
    @GetMapping("/gets")
    public BaseResponse<List<Language>> getLanguages() {
        return languageService.getLanguages();
    }

    @PreAuthorize("hasAuthority('foreign:insert')")
    @PostMapping("/add")
    public <T> BaseResponse<T> addLanguage(@RequestBody @Validated({InsertGroup.class}) Language language) {
        return languageService.addLanguage(language);
    }

    @PreAuthorize("hasAuthority('foreign:update')")
    @PutMapping("/update")
    public <T> BaseResponse<T> updateLanguage(@RequestBody @Validated({UpdateGroup.class}) Language language) {
        return languageService.updateLanguage(language);
    }

    @PreAuthorize("hasAuthority('foreign:delete')")
    @DeleteMapping("/delete/{languageId}")
    public <T> BaseResponse<T> deleteLanguage(@PathVariable String languageId) {
        return languageService.deleteLanguage(languageId);
    }

    // 与学生相关的 👇
    @PreAuthorize("hasAuthority('foreign:select')")
    @PostMapping("/student/gets")
    public BaseResponse<Page<ForeignLanguageItem>> getForeignLanguages(@RequestBody ForeignLanguageQuery query) {
        return foreignLanguageService.getForeignLanguages(query);
    }

    @PreAuthorize("hasAuthority('foreign:insert')")
    @PostMapping("/student/add")
    public <T> BaseResponse<T> addForeignLanguageBatch(@RequestBody @Validated({InsertGroup.class}) ValidateList<ForeignLanguage> foreignLanguages) {
        return foreignLanguageService.insertForeignLanguageBatch(foreignLanguages);
    }

    @PreAuthorize("hasAuthority('foreign:update')")
    @PutMapping("/student/update")
    public <T> BaseResponse<T> updateForeignLanguage(@RequestBody @Validated({UpdateGroup.class}) ForeignLanguage foreignLanguage) {
        return foreignLanguageService.updateForeignLanguage(foreignLanguage);
    }

    @PreAuthorize("hasAuthority('foreign:delete')")
    @DeleteMapping("/student/delete/{foreignLanguageId}")
    public <T> BaseResponse<T> deleteForeignLanguage(@PathVariable String foreignLanguageId) {
        return foreignLanguageService.deleteForeignLanguage(foreignLanguageId);
    }

    @PreAuthorize("hasAuthority('foreign:select')")
    @PostMapping("/student/stat")
    public BaseResponse<List<ForeignLanguageStatItem>> getForeignLanguageStat(@RequestBody ForeignLanguageStatQuery query) {
        return foreignLanguageService.getForeignLanguageStat(query);
    }
}