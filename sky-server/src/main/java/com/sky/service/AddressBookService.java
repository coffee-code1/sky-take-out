package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    void addAddressBook(AddressBook addressBook);

    List<AddressBook>getallAddress(Long userId);

    /**
     * 设置成默认
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void deleteById(Long id);

    List<AddressBook> list(AddressBook addressBook);
}
