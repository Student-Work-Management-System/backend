package edu.guet.studentworkmanagementsystem.service.other;

import com.mybatisflex.core.query.QueryChain;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.Counselor;
import edu.guet.studentworkmanagementsystem.entity.po.other.Degree;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.entity.po.other.Politic;
import edu.guet.studentworkmanagementsystem.mapper.other.CounselorMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.DegreeMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.GradeMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.PoliticMapper;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.CounselorTableDef.COUNSELOR;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DegreeMapper degreeMapper;
    @Autowired
    private PoliticMapper politicMapper;
    @Autowired
    private CounselorMapper counselorMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    public BaseResponse<List<Grade>> getAllGrades() {
        CompletableFuture<List<Grade>> future = CompletableFuture.supplyAsync(() -> gradeMapper.selectAll(), readThreadPool);
        List<Grade> result = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(result);
    }

    @Override
    public BaseResponse<List<Degree>> getAllDegrees() {
        CompletableFuture<List<Degree>> future = CompletableFuture.supplyAsync(() -> degreeMapper.selectAll(), readThreadPool);
        List<Degree> result = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(result);
    }

    @Override
    public BaseResponse<List<Politic>> getAllPolitics() {
        CompletableFuture<List<Politic>> future = CompletableFuture.supplyAsync(() -> politicMapper.selectAll(), readThreadPool);
        List<Politic> result = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(result);
    }

    @Override
    public List<Counselor> getAllCounselors() {
        CompletableFuture<List<Counselor>> future = CompletableFuture.supplyAsync(() -> counselorMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    public List<Counselor> getCounselorByUid(String uid) {
        CompletableFuture<List<Counselor>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(Counselor.class)
                .select(COUNSELOR.ALL_COLUMNS)
                .where(COUNSELOR.UID.eq(uid))
                .list(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }
}
