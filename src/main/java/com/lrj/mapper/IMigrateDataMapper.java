package com.lrj.mapper;

/**
 * @author : cwj
 * @describe :
 * @date : ${date}
 */

import com.lrj.pojo.UserFromOldDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMigrateDataMapper {

        List<UserFromOldDataBase> UserFromOldDataBase();

}
