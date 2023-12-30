package ${package}.common.model.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页返回实体
 *
 * @author alex meng
 * @createDate 2023-12-23 22:57
 */
@Data
@Schema(description = "分页数据结构")
public class PageResultRecord<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页码")
    private Long current;

    @Schema(description = "每页条数")
    private Long pageSize;

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "数据列表")
    private List<T> records;

    public static <T> PageResultRecord<T> page2Result(IPage<T> result) {
        PageResultRecord<T> data = new PageResultRecord<>();
        data.setCurrent(result.getCurrent());
        data.setPageSize(result.getSize());
        data.setTotal(ObjectUtils.isNotEmpty(result.getTotal()) ? result.getTotal() : 0L);
        data.setRecords(result.getRecords());
        return data;
    }

    public static <T> PageResultRecord<T> page2Result(Long total, List<T> records) {
        PageResultRecord<T> data = new PageResultRecord<>();
        data.setTotal(total);
        data.setRecords(records);
        return data;
    }
}