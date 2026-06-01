package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    void add(AddressBook addressBook);

    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> getallAddress(@Param("userId") Long userId);

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    @Update("update address_book set is_default = #{isDefault} where id = #{id}")
    void updateDefaultById(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    @Delete("delete from address_book where id = #{id}")
    void deleteById(@Param("id") Long id);

    List<AddressBook> list(AddressBook addressBook);
}
