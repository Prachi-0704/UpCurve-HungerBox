package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Repository.VendorRepository;
import com.example.HungerBox_Backend.Request.CreateVendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImplementation implements VendorService{

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public Vendor createVendor(CreateVendorRequest req, User user) {

        Vendor vendor = new Vendor();

        vendor.setContactInformation(req.getContactInformation());

        vendor.setVendorName(req.getVendorName());

        vendor.setCuisineType(req.getCuisineType());

        vendor.setDescription(req.getDescription());

        vendor.setImages(req.getImages());

        vendor.setOpeningHours(req.getOpeningHours());

        vendor.setRegistrationDate(LocalDateTime.now());

        vendor.setOwner(user);

        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor updateVendor(long vendorId, CreateVendorRequest updateVendor) throws Exception {

        Vendor vendor = findVendorById(vendorId);

        if(vendor.getCuisineType() != null){
            vendor.setCuisineType(updateVendor.getCuisineType());
        }

        if(vendor.getDescription() != null){
            vendor.setDescription(updateVendor.getDescription());
        }

        if(vendor.getVendorName() != null){
            vendor.setVendorName(updateVendor.getVendorName());
        }

        return vendorRepository.save(vendor);
    }

    @Override
    public void deleteVendor(long vendorId) throws Exception {

        Vendor vendor = findVendorById(vendorId);

        vendorRepository.delete(vendor);
    }

    @Override
    public List<Vendor> getAllVendor() {
        return vendorRepository.findAll();
    }

    @Override
    public List<Vendor> searchVendor(String keyword) {
        return vendorRepository.findBySearchQuery(keyword);
    }

    @Override
    public Vendor findVendorById(long vendorId) throws Exception {
        Optional<Vendor> opt = vendorRepository.findById(vendorId);

        if(opt.isEmpty()){
            throw new Exception("Vendor not found with id "+vendorId);
        }

        return opt.get();
    }

    @Override
    public Vendor getVendorByUserId(long ownerId) throws Exception {

        Vendor vendor = vendorRepository.findByOwnerUserId(ownerId);

        if(vendor == null){
            throw new Exception("Vendor not found with owner id "+ownerId);
        }

        return vendor;
    }

    @Override
    public Vendor updateVendorStatus(long vendorId) throws Exception {

        Vendor vendor = findVendorById(vendorId);

        vendor.setOpen(!vendor.isOpen());

        return vendorRepository.save(vendor);
    }
}
