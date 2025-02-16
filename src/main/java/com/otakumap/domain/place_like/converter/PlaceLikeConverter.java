package com.otakumap.domain.place_like.converter;

import com.otakumap.domain.hash_tag.converter.HashTagConverter;
import com.otakumap.domain.mapping.PlaceAnimation;
import com.otakumap.domain.place.entity.Place;
import com.otakumap.domain.place_like.dto.PlaceLikeResponseDTO;
import com.otakumap.domain.place_like.entity.PlaceLike;
import com.otakumap.domain.user.entity.User;

import java.util.List;

public class PlaceLikeConverter {
    public static PlaceLikeResponseDTO.PlaceLikePreViewDTO placeLikePreViewDTO(PlaceLike placeLike, Place place) {
        return PlaceLikeResponseDTO.PlaceLikePreViewDTO.builder()
                .id(placeLike.getId())
                .placeId(place.getId())
                .name(place.getName())
                .detail(place.getDetail())
                .lat(place.getLat())
                .lng(place.getLng())
                .isFavorite(placeLike.getIsFavorite())
                .build();

    }

    public static PlaceLikeResponseDTO.PlaceLikePreViewListDTO placeLikePreViewListDTO(List<PlaceLikeResponseDTO.PlaceLikePreViewDTO> placeLikes, boolean hasNext, Long lastId) {
        return PlaceLikeResponseDTO.PlaceLikePreViewListDTO.builder()
                .placeLikes(placeLikes)
                .hasNext(hasNext)
                .lastId(lastId)
                .build();
    }

    public static PlaceLike toPlaceLike(User user, Place place, PlaceAnimation placeAnimation) {
        return PlaceLike.builder()
                .user(user)
                .place(place)
                .placeAnimation(placeAnimation)
                .isFavorite(false)
                .build();
    }

    public static PlaceLikeResponseDTO.FavoriteResultDTO toFavoriteResultDTO(PlaceLike placeLike) {
        return PlaceLikeResponseDTO.FavoriteResultDTO.builder()
                .placeLikeId(placeLike.getId())
                .isFavorite(placeLike.getIsFavorite())
                .build();
    }

    public static PlaceLikeResponseDTO.PlaceLikeDetailDTO placeLikeDetailDTO(PlaceLike placeLike, Place place) {
        return PlaceLikeResponseDTO.PlaceLikeDetailDTO.builder()
                .placeLikeId(placeLike.getId())
                .placeName(place.getName())
                .animationName(placeLike.getPlaceAnimation().getAnimation().getName())
                .lat(place.getLat())
                .lng(place.getLng())
                .isLiked(Boolean.TRUE) // 저장한 장소를 조회하는 거니까 항상 true
                // 장소-애니메이션에 대한 해시태그
                .hashtags(placeLike.getPlaceAnimation().getPlaceAnimationHashTags()
                        .stream()
                        .map(placeHashTag -> HashTagConverter.toHashTagDTO(placeHashTag.getHashTag())).toList())
                .build();
    }
}