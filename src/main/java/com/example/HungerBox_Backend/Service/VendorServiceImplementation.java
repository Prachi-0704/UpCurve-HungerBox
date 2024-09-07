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
public class VendorServiceImplementation implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    /**
     * Creates a new vendor using the provided request data and assigns the user as the owner.
     *
     * @param req the request containing vendor details.
     * @param user the user who owns the vendor.
     * @return the newly created Vendor.
     */
    @Override
    public Vendor createVendor(CreateVendorRequest req, User user) {
        Vendor vendor = new Vendor();

        vendor.setContactInformation(req.getContactInformation());
        vendor.setVendorName(req.getVendorName());
        vendor.setCuisineType(req.getCuisineType());
        vendor.setDescription(req.getDescription());
        vendor.setImages(req.getImages());
        vendor.setOpeningHours(req.getOpeningHours());
        vendor.setRegistrationDate(LocalDateTime.now()); // Set the current date and time
        vendor.setOwner(user);

        return vendorRepository.save(vendor);
    }

    /**
     * Updates the details of an existing vendor identified by its ID.
     *
     * @param vendorId the ID of the vendor to update.
     * @param updateVendor the request containing updated vendor details.
     * @return the updated Vendor.
     * @throws Exception if the vendor is not found.
     */
    @Override
    public Vendor updateVendor(long vendorId, CreateVendorRequest updateVendor) throws Exception {
        Vendor vendor = findVendorById(vendorId);

        // Update fields only if they are not null
        if (updateVendor.getCuisineType() != null) {
            vendor.setCuisineType(updateVendor.getCuisineType());
        }
        if (updateVendor.getDescription() != null) {
            vendor.setDescription(updateVendor.getDescription());
        }
        if (updateVendor.getVendorName() != null) {
            vendor.setVendorName(updateVendor.getVendorName());
        }

        return vendorRepository.save(vendor);
    }

    /**
     * Deletes the vendor identified by the specified ID.
     *
     * @param vendorId the ID of the vendor to delete.
     * @throws Exception if the vendor is not found.
     */
    @Override
    public void deleteVendor(long vendorId) throws Exception {
        Vendor vendor = findVendorById(vendorId);
        vendorRepository.delete(vendor);
    }

    /**
     * Retrieves all vendors from the repository.
     *
     * @return a list of all Vendors.
     */
    @Override
    public List<Vendor> getAllVendor() {
        return vendorRepository.findAll();
    }

    /**
     * Searches for vendors based on a keyword in their details.
     *
     * @param keyword the keyword to search for.
     * @return a list of Vendors matching the search query.
     */
    @Override
    public List<Vendor> searchVendor(String keyword) {
        return vendorRepository.findBySearchQuery(keyword);
    }

    /**
     * Finds a vendor by its ID.
     *
     * @param vendorId the ID of the vendor to find.
     * @return the Vendor with the specified ID.
     * @throws Exception if the vendor is not found.
     */
    @Override
    public Vendor findVendorById(long vendorId) throws Exception {
        Optional<Vendor> opt = vendorRepository.findById(vendorId);

        if (opt.isEmpty()) {
            throw new Exception("Vendor not found with ID " + vendorId);
        }

        return opt.get();
    }

    /**
     * Retrieves the vendor associated with the specified owner ID.
     *
     * @param ownerId the ID of the owner.
     * @return the Vendor owned by the specified user.
     * @throws Exception if the vendor is not found.
     */
    @Override
    public Vendor getVendorByUserId(long ownerId) throws Exception {
        Vendor vendor = vendorRepository.findByOwnerUserId(ownerId);

        if (vendor == null) {
            throw new Exception("Vendor not found with owner ID " + ownerId);
        }

        return vendor;
    }

    /**
     * Toggles the open status of a vendor identified by its ID.
     *
     * @param vendorId the ID of the vendor to update.
     * @return the updated Vendor with its open status toggled.
     * @throws Exception if the vendor is not found.
     */
    @Override
    public Vendor updateVendorStatus(long vendorId) throws Exception {
        Vendor vendor = findVendorById(vendorId);

        // Toggle the open status
        vendor.setOpen(!vendor.isOpen());

        return vendorRepository.save(vendor);
    }
}
