package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.business.BusinessFilterRequestDTO;
import com.example.testProject.dto.business.BusinessRequestDTO;
import com.example.testProject.dto.business.BusinessResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.User;
import com.example.testProject.error.NotFoundException;
import com.example.testProject.mapper.BusinessMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.security.SecurityUtils;
import com.example.testProject.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    // ================= CREATE =================
    @Override
    public BusinessResponseDTO create(BusinessRequestDTO dto) {

        User owner = SecurityUtils.currentUser();

        Business business = businessMapper.toEntity(dto);
        business.setOwner(owner);

        return businessMapper.toDTO(businessRepository.save(business));
    }

    // ================= GET BY ID =================
    @Override
    public BusinessResponseDTO getById(Long id) {
        return businessRepository.findById(id)
                .map(businessMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Business not found"));
    }

    // ================= GET ALL =================
    @Override
    public List<BusinessResponseDTO> getAll() {
        return businessRepository.findAll()
                .stream()
                .map(businessMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    @Override
    public BusinessResponseDTO update(Long id, BusinessRequestDTO dto) {

        User current = SecurityUtils.currentUser();

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Business not found"));

        if (!business.getOwner().getId().equals(current.getId())) {
            throw new RuntimeException("You are not the owner of this business");
        }

        businessMapper.update(dto, business);

        return businessMapper.toDTO(businessRepository.save(business));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {

        User current = SecurityUtils.currentUser();

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Business not found"));

        if (!business.getOwner().getId().equals(current.getId())) {
            throw new RuntimeException("You are not the owner of this business");
        }

        businessRepository.delete(business);
    }

    // ================= FILTER (NEW) =================
    @Override
    public List<BusinessResponseDTO> filter(BusinessFilterRequestDTO request) {

        return businessRepository.findAll().stream()

                // ⭐ rating
                .filter(b -> request.getMinRating() == null ||
                        b.getRating() >= request.getMinRating())

                // 🔎 search
                .filter(b -> request.getSearch() == null ||
                        b.getName().toLowerCase().contains(request.getSearch().toLowerCase()))

                // 💰 price (через услуги)
                .filter(b -> {

                    if (request.getMinPrice() == null && request.getMaxPrice() == null) {
                        return true;
                    }

                    return b.getServices().stream().anyMatch(s -> {

                        boolean minOk = request.getMinPrice() == null ||
                                s.getPrice() >= request.getMinPrice();

                        boolean maxOk = request.getMaxPrice() == null ||
                                s.getPrice() <= request.getMaxPrice();

                        return minOk && maxOk;
                    });
                })

                // 📍 distance
                .filter(b -> {

                    if (request.getLatitude() == null || request.getLongitude() == null) {
                        return true;
                    }

                    if (b.getLatitude() == null || b.getLongitude() == null) {
                        return false;
                    }

                    double distance = calculateDistanceKm(
                            request.getLatitude(),
                            request.getLongitude(),
                            b.getLatitude(),
                            b.getLongitude()
                    );

                    return request.getMaxDistanceKm() == null ||
                            distance <= request.getMaxDistanceKm();
                })

                .map(businessMapper::toDTO)
                .toList();
    }

    // ================= DISTANCE FUNCTION =================
    private double calculateDistanceKm(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) *
                                Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}