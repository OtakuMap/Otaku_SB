package com.otakumap.domain.place_like.service;

import com.otakumap.domain.place_like.dto.PlaceLikeRequestDTO;
import com.otakumap.domain.place_like.entity.PlaceLike;
import com.otakumap.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceLikeCommandService {
    void deletePlaceLike(List<Long> placeIds, User user);
    void savePlaceLike(User user, Long placeId, PlaceLikeRequestDTO.SavePlaceLikeDTO request);
    PlaceLike favoritePlaceLike(Long placeLikeId, PlaceLikeRequestDTO.FavoriteDTO request);
}
