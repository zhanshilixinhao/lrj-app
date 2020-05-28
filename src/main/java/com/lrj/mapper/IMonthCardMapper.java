package com.lrj.mapper;

import com.lrj.VO.MonthCardWashingCountVo;
import com.lrj.pojo.MonthCard;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;


/**
 * @author Lxh
 * @date 2020/4/16 11:21
 */
@Repository
public interface IMonthCardMapper extends Mapper<MonthCard>,MySqlMapper<MonthCard> {
    /**
     * 查询所有可购买的月卡
     * @return
     */
    List<MonthCard> getMonthCardList();

    /**
     *通过Id查询 月卡具体信息
     */
    MonthCard getMonthCardById(Integer monthCardId);

    /**
     * 通过ID 查询月卡可洗具体内容
     * @param id
     * @return
     */
    List<MonthCardWashingCountVo> getMonthCardWashingCountList(Integer id);
}
