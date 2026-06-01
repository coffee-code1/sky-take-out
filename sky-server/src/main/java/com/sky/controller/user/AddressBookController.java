package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "User address book controller")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("Add address")
    public Result addAddress(@RequestBody AddressBook addressBook) {
        log.info("user save address, userId: {}, payload: {}", BaseContext.getCurrentId(), addressBook);
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("List addresses")
    public Result<List<AddressBook>> getAddressBook() {
        List<AddressBook> list = addressBookService.getallAddress(BaseContext.getCurrentId());
        return Result.success(list);
    }

    @PutMapping("/default")
    @ApiOperation("Set default address")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get address by id")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("Update address")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("user update address, userId: {}, payload: {}", BaseContext.getCurrentId(), addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("Delete address")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("Get default address")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("No default address");
    }
}
