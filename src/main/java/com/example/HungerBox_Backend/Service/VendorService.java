package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Request.CreateVendorRequest;

import java.util.List;

public interface VendorService {

    public Vendor createVendor(CreateVendorRequest req, User user);

    public Vendor updateVendor(long vendorId, CreateVendorRequest updateVendor) throws Exception;

    public void deleteVendor(long vendorId) throws Exception;

    public List<Vendor> getAllVendor();

    public List<Vendor> searchVendor(String keyword);

    public  Vendor findVendorById(long vendorId) throws Exception;

    public Vendor getVendorByUserId(long ownerId) throws Exception;

    public Vendor updateVendorStatus(long vendorId) throws Exception;
}
