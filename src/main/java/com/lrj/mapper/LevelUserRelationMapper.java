package com.lrj.mapper;

import com.lrj.pojo.LevelUserRelation;
import org.springframework.stereotype.Repository;

/**
 * @author Lxh
 * @date 2020/4/2 16:09
 */
@Repository
public interface LevelUserRelationMapper {
    void insert(LevelUserRelation levelUserRelation);
}
