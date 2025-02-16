package com.otakumap.domain.place_like.controller;

import com.otakumap.domain.auth.jwt.annotation.CurrentUser;
import com.otakumap.domain.place_like.converter.PlaceLikeConverter;
import com.otakumap.domain.place_like.dto.PlaceLikeRequestDTO;
import com.otakumap.domain.place_like.dto.PlaceLikeResponseDTO;
import com.otakumap.domain.place_like.service.PlaceLikeCommandService;
import com.otakumap.domain.place_like.service.PlaceLikeQueryService;
import com.otakumap.domain.user.entity.User;
import com.otakumap.global.apiPayload.ApiResponse;

import com.otakumap.global.validation.annotation.ExistPlace;
import com.otakumap.global.validation.annotation.ExistPlaceLike;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place-likes")
@RequiredArgsConstructor
@Validated
public class PlaceLikeController {
    private final PlaceLikeQueryService placeLikeQueryService;
    private final PlaceLikeCommandService placeLikeCommandService;

    @Operation(summary = "저장된 장소 목록 조회(+ 즐겨찾기 목록 조회)", description = "저장된 장소 목록을 불러옵니다.")
    @GetMapping("")
    @Parameters({
            @Parameter(name = "isFavorite", description = "즐겨찾기 여부(필수 X) -> true: 즐겨찾기 목록 조회"),
            @Parameter(name = "lastId", description = "마지막으로 조회된 저장된 장소 id, 처음 가져올 때 -> 0"),
            @Parameter(name = "limit", description = "한 번에 조회할 최대 장소 수. 기본값은 10입니다.")
    })
    public ApiResponse<PlaceLikeResponseDTO.PlaceLikePreViewListDTO> getPlaceLikeList(@CurrentUser User user, @RequestParam(required = false) Boolean isFavorite, @RequestParam(defaultValue = "0") Long lastId, @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.onSuccess(placeLikeQueryService.getPlaceLikeList(user, isFavorite, lastId, limit));
    }

    @Operation(summary = "저장된 장소 삭제", description = "저장된 장소를 삭제합니다.")
    @DeleteMapping("")
    @Parameters({
            @Parameter(name = "placeIds", description = "장소 id List"),
    })
    public ApiResponse<String> deletePlaceLike(@RequestParam(required = false) List<Long> placeIds, @CurrentUser User user) {
        placeLikeCommandService.deletePlaceLike(placeIds, user);
        return ApiResponse.onSuccess("저장된 장소가 성공적으로 삭제되었습니다");
    }

    @Operation(summary = "장소 저장", description = "장소를 저장합니다.")
    @PostMapping("/{placeId}")
    @Parameters({
            @Parameter(name = "placeId", description = "장소 Id")
    })
    public ApiResponse<String> savePlaceLike(@ExistPlace @PathVariable Long placeId, @CurrentUser User user, @RequestBody @Validated PlaceLikeRequestDTO.SavePlaceLikeDTO request) {
         placeLikeCommandService.savePlaceLike(user, placeId, request);
         return ApiResponse.onSuccess("장소가 성공적으로 저장되었습니다.");
    }

    @Operation(summary = "저장된 장소 즐겨찾기/즐겨찾기 취소", description = "저장된 장소를 즐겨찾기 또는 취소합니다.")
    @PatchMapping("/{placeLikeId}/favorites")
    public ApiResponse<PlaceLikeResponseDTO.FavoriteResultDTO> favoritePlaceLike(@PathVariable Long placeLikeId, @RequestBody @Valid PlaceLikeRequestDTO.FavoriteDTO request) {
        return ApiResponse.onSuccess(PlaceLikeConverter.toFavoriteResultDTO(placeLikeCommandService.favoritePlaceLike(placeLikeId, request)));
    }

    @Operation(summary = "저장된 장소 상세 조회", description = "지도에서 저장된 장소의 정보를 확인합니다.")
    @GetMapping("/{placeLikeId}")
    public ApiResponse<PlaceLikeResponseDTO.PlaceLikeDetailDTO> getPlaceLike(@PathVariable @ExistPlaceLike Long placeLikeId) {
        return ApiResponse.onSuccess(placeLikeQueryService.getPlaceLike(placeLikeId));
    }
}
