package com.otakumap.domain.place_like.service;

import com.otakumap.domain.mapping.PlaceAnimation;
import com.otakumap.domain.place.entity.Place;
import com.otakumap.domain.place.repository.PlaceRepository;
import com.otakumap.domain.place_animation.repository.PlaceAnimationRepository;
import com.otakumap.domain.place_like.converter.PlaceLikeConverter;
import com.otakumap.domain.place_like.dto.PlaceLikeRequestDTO;
import com.otakumap.domain.place_like.entity.PlaceLike;
import com.otakumap.domain.place_like.repository.PlaceLikeRepository;
import com.otakumap.domain.user.entity.User;
import com.otakumap.global.apiPayload.code.status.ErrorStatus;
import com.otakumap.global.apiPayload.exception.handler.PlaceHandler;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceLikeCommandServiceImpl implements PlaceLikeCommandService {
    private final PlaceLikeRepository placeLikeRepository;
    private final EntityManager entityManager;
    private final PlaceAnimationRepository placeAnimationRepository;
    private final PlaceRepository placeRepository;

    @Override
    public void deletePlaceLike(List<Long> placeIds, User user) {
        List<Long> placeLikeIds = placeIds.stream()
                .map(placeId -> placeLikeRepository.findByPlaceIdAndUserId(placeId, user.getId())
                        .orElseThrow(()-> new PlaceHandler(ErrorStatus.PLACE_LIKE_NOT_FOUND))
                        .getId()
                )
                .toList();

        placeLikeRepository.deleteAllByIdInBatch(placeLikeIds);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void savePlaceLike(User user, Long placeId, PlaceLikeRequestDTO.SavePlaceLikeDTO request) {
        PlaceAnimation placeAnimation = placeAnimationRepository.findByPlaceIdAndAnimationId(placeId, request.getAnimationId()).orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_ANIMATION_NOT_FOUND));

        // Place 존재 여부 확인
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        // 이미 PlaceLike가 존재하는지 확인
        if (placeLikeRepository.existsByUserAndPlaceAnimation(user, placeAnimation))
            throw new PlaceHandler(ErrorStatus.PLACE_LIKE_ALREADY_EXISTS);

        placeLikeRepository.save(PlaceLikeConverter.toPlaceLike(user, place, placeAnimation));
    }

    @Override
    public PlaceLike favoritePlaceLike(Long placeLikeId, PlaceLikeRequestDTO.FavoriteDTO request) {
        PlaceLike placeLike = placeLikeRepository.findById(placeLikeId).orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_LIKE_NOT_FOUND));
        placeLike.setIsFavorite(request.getIsFavorite());
        return placeLikeRepository.save(placeLike);
    }
}