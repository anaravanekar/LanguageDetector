package com.keysight.lang.detector.repo;

import org.springframework.data.repository.CrudRepository;

import com.keysight.lang.detector.model.MDMAccountAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MDMAccountAddressRepository extends PagingAndSortingRepository<MDMAccountAddress, Long> {

}
